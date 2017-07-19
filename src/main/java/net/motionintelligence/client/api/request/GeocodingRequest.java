package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.Address;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.response.GeocodingResponse;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Calculates coordinates using the ...
 * TODO proper documentation
 * TODO refactor all requests - a lot of potential since it's all kind of repetitive
 * TODO make other requests with no Client deprecated too
 */
public class GeocodingRequest implements GetRequest<String, GeocodingResponse> {

    /**
     * Special Geocoding request options that can be set at creation of the request.
     * For more details please see REST service documentation.
     *
     * @see <a href="https://developers.arcgis.com/rest/geocode/api-reference/geocoding-find-address-candidates.htm">Documentation</a>
     */
    public enum Option {
        SOURCE_COUNTRY("sourceCountry"), // Limits the candidates returned by the findAddressCandidates operation to the specified country or countries, e.g. "sourceCountry=FRA,DEU,ESP" - see https://developers.arcgis.com/rest/geocode/api-reference/geocode-coverage.htm
        SEARCH_EXTENT("searchExtent"), //A set of bounding box coordinates that limit the search area to a specific region, e.g. "-104,35.6,-94.32,41"
        CLOSE_LOCATION("location"), //Defines an origin point location that is used to sort geocoding candidates based on their proximity to the location.
        SPATIAL_REFERENCE("outSR"), //The spatial reference of the x/y coordinates returned by a geocode request, e.g. outSR=102100 (http://resources.arcgis.com/EN/HELP/REST/APIREF/PCS.HTML and http://resources.arcgis.com/EN/HELP/REST/APIREF/GCS.HTML)
        //OUTPUT_FIELDS("outFields"), //The list of fields to be returned in the response. So far only default coordinates are returned
        MAX_LOCATIONS("maxLocations"), //If more geocode candidates are supposed to be returned; by default only the one best fitting candidate is returned
        FOR_STORAGE("forStorage"); //Specifies whether the results of the operation will be persisted, if true an authentication is required and the operation execution will be charged to the account

        private String name;
        Option(String repName){
            this.name = repName;
        }
    }

    private static final String       REST_URI            = "http://geocode.arcgis.com";
    private static final String       PATH_SINGLE_ADDRESS = "arcgis/rest/services/World/GeocodeServer/findAddressCandidates";

    private static final Logger       LOGGER      = LoggerFactory.getLogger(GeocodingRequest.class);
    private static final ObjectMapper JSON_PARSER = JsonFactory.create();

    private final Client client;
    private final Map<Option,String> requestOptions;

    /**
     * Creation of a default geo coding request.
     *
     * Note1: The client's lifecycle is not handled here. Please make sure to properly close the client when not
     * needed anymore.
     *
     * Note2: For the parallel batch requests it may be required to set a certain connection pool size when client is
     * created. (e.g. this was necessary for a JBoss client but not for the Jersey client)
     *
     * @param client specified Client implementation to be used, e.g. Jersey or jBoss client
     */
    public GeocodingRequest(Client client){
        this(client, new EnumMap<>(Option.class));
    }

    /**
     * Use a custom client implementation for the geo coding request with non-default request parameters.
     *
     * Note1: The client's lifecycle is not handled here. Please make sure to properly close the client when not
     * needed anymore.
     *
     * Note2: For the parallel batch requests it may be required to set a certain connection pool size when client is
     * created. (e.g. this was necessary for a JBoss client but not for the Jersey client)
     *
     * @param client specified Client implementation to be used, e.g. Jersey or jBoss client
     * @param extraOptions see {@link Option} for possibilities - null pointer and empty strings will be ignored
     */
    public GeocodingRequest(Client client, EnumMap<Option,String> extraOptions){
        this.client	        = client;
        this.requestOptions = extraOptions;
    }

    /**
     * Execute request TODO docu
     *
     * @return GeocodingResponse
     * @throws Route360ClientException In case of error other than Gateway Timeout
     */
    @Override
    public GeocodingResponse get(String singleLineAddress) throws Route360ClientException {
        return get( webTarget -> webTarget.queryParam("singleLine", singleLineAddress));
    }

    /**
     * Execute request TODO docu
     *
     * @return GeocodingResponse
     * @throws Route360ClientException In case of error
     */
    public GeocodingResponse get(Address address) throws Route360ClientException {
        return get( webTarget ->
            conditionalQueryParam("address", address.street,
            conditionalQueryParam("address2", address.streetDetails,
            conditionalQueryParam("city", address.city,
            conditionalQueryParam("postal", address.postalCode,
            conditionalQueryParam("countryCode", address.country, webTarget))))));
    }

    private WebTarget conditionalQueryParam(String key, String value, WebTarget webTarget) {
        if( value == null || value.length() == 0 )
            return webTarget;
        else
            return webTarget.queryParam(key,value);
    }

    private GeocodingResponse get(Function<WebTarget,WebTarget> queryPrep)
            throws Route360ClientException {

        final long requestStart = System.currentTimeMillis();
        WebTarget target = queryPrep.apply(client.target(REST_URI)
                .path(PATH_SINGLE_ADDRESS)
                .queryParam("f", "json"));
        if( !requestOptions.containsKey(Option.MAX_LOCATIONS) )
            target = target.queryParam(Option.MAX_LOCATIONS.name, 1);
        for(Map.Entry<Option,String> entry : requestOptions.entrySet())
            target = conditionalQueryParam(entry.getKey().name, entry.getValue(), target);

        LOGGER.debug("Executing geocoding request to URI: '%s'", target.getUri());
        Response response = target.request().buildGet().invoke();
        try {
            final long roundTripTime = System.currentTimeMillis() - requestStart;
            return validateResponse(response, requestStart, roundTripTime);
        } finally {
            response.close();
        }
    }

    public GeocodingResponse[] getBatchParallel( final int parallelThreads, final int triesBeforeFail,
                                                 final String... addresses) throws Route360ClientException {
        return getBatchParallel( this::get, parallelThreads, triesBeforeFail, addresses);
    }

    public GeocodingResponse[] getBatchParallel( final int parallelThreads, final int triesBeforeFail,
                                                 final Address... addresses) throws Route360ClientException {
        return getBatchParallel( this::get, parallelThreads, triesBeforeFail, addresses);
    }

    private <ADDRESS_TYPE> GeocodingResponse[] getBatchParallel(
            final GetRequest<ADDRESS_TYPE,GeocodingResponse> singleRequest,
            final int parallelThreads, final int triesBeforeFail, final ADDRESS_TYPE[] addresses)
            throws Route360ClientException {

        final ExecutorService executor = Executors.newFixedThreadPool(parallelThreads);
        final List<Callable<GeocodingResponse>> requests = new ArrayList<>();
        for (ADDRESS_TYPE singleAddress : addresses) {
            requests.add( () -> { //Adding individual Callables to be executed in available parallel Threads
                // will terminate after the 5th try
                for( int numberOfTries = 0; numberOfTries < triesBeforeFail; numberOfTries ++) {
                    try {
                        return singleRequest.get(singleAddress);
                        // special case since the service is sometimes unavailable when too many parallel requests are processed
                    } catch (ServiceUnavailableException e) {
                        continue;
                    }
                }
                throw new ServiceUnavailableException("Even after " + triesBeforeFail + " tries the service was still " +
                        "unavailable. Try reducing the thread number or increasing the number of tries.");
            });
        }
        try {
            //Execution of the requests
            List<Future<GeocodingResponse>> results = executor.invokeAll(requests);
            //at finish shutdown of the parallel executor
            shutdownServiceExecutor(executor);
            //Collecting results
            GeocodingResponse[] resultArray = new GeocodingResponse[addresses.length];
            int i = 0;
            for(Future<GeocodingResponse> result : results)
                resultArray[i++] = result.get();
            return resultArray;
        } catch(InterruptedException | ExecutionException e) {
            throw new Route360ClientException("Parallel Execution Failed! Cause: ", e);
        }
    }

    private void shutdownServiceExecutor(ExecutorService executor) throws InterruptedException {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS))
                executor.shutdownNow();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            throw e;
        }
    }

    public GeocodingResponse[] getBatchSequential(String... addresses) throws Route360ClientException {
        GeocodingResponse[] batchResults = new GeocodingResponse[addresses.length];
        for( int i = 0; i<addresses.length; i++)
            batchResults[i] = get(addresses[i]);
        return batchResults;
    }

    public GeocodingResponse[] getBatchSequential(Address... addresses) throws Route360ClientException {
        GeocodingResponse[] batchResults = new GeocodingResponse[addresses.length];
        for( int i = 0; i<addresses.length; i++)
            batchResults[i] = get(addresses[i]);
        return batchResults;
    }

    //TODO include request times
    private GeocodingResponse validateResponse(final Response response,
                                                  final long requestStart, final long roundTripTime)
            throws Route360ClientException {
        // compare the HTTP status codes, NOT the route 360 code
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            // parse the results
            String res = response.readEntity(String.class);

            GeocodingResponse parsedResponse = JSON_PARSER.fromJson(res, GeocodingResponse.class);
            return parsedResponse;
        } else if(response.getStatus() == Response.Status.SERVICE_UNAVAILABLE.getStatusCode() )
            throw new ServiceUnavailableException();
        else
            throw new Route360ClientException("Request failed with response: \n" + response.readEntity(String.class));
    }

}

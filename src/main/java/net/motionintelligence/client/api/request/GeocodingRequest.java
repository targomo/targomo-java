package net.motionintelligence.client.api.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.response.GeocodingResponse;
//import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
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
public class GeocodingRequest {

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

    private static final String REST_URI            = "http://geocode.arcgis.com";
    private static final String PATH_SINGLE_ADDRESS = "arcgis/rest/services/World/GeocodeServer/findAddressCandidates";

    private static final Logger LOGGER      = LoggerFactory.getLogger(GeocodingRequest.class);
    private static final Gson   GSON_PARSER = new GsonBuilder().create();

    private final Client client;
    private final Map<Option,?> requestOptions;

    /**
     * Use a custom client implementation for the geo coding request
     * @param client specified Client implementation to be used
     */
    public GeocodingRequest(Client client){
        this(client, new EnumMap<>(Option.class));
    }

    /**
     * Use a custom client implementation for the geo coding request and set non-default request parameters.
     *
     * @see <a href="https://developers.arcgis.com/rest/geocode/api-reference/geocoding-find-address-candidates.htm">Documentation</a>
     *
     * @param client specified Client implementation to be used
     * @param extraOptions as map
     */
    public GeocodingRequest(Client client, EnumMap<Option,?> extraOptions){
        this.client	        = client;
        this.requestOptions = extraOptions;
    }

    /**
     * Execute request TODO docu
     *
     * @return GeocodingResponse
     * @throws Route360ClientException In case of error other than Gateway Timeout
     */
    public GeocodingResponse get(String address) throws Route360ClientException {
        return get( webTarget -> webTarget.queryParam("singleLine", address));
    }

    /**
     * Execute request TODO docu
     *
     * @return GeocodingResponse
     * @throws Route360ClientException In case of error
     */
    public GeocodingResponse get(String address, String address2, String city, String postalCode, String countryCode)
            throws Route360ClientException {
        return get( webTarget -> webTarget.queryParam("address", address)
                .queryParam("address2", address2)
                .queryParam("city", city)
                .queryParam("postal", postalCode)
                .queryParam("countryCode", countryCode));
    }

    private GeocodingResponse get(Function<WebTarget,WebTarget> queryPrep)
            throws Route360ClientException {

        final long requestStart = System.currentTimeMillis();
        WebTarget target = queryPrep.apply(client.target(REST_URI)
                .path(PATH_SINGLE_ADDRESS)
                .queryParam("f", "json"));
        if( !requestOptions.containsKey(Option.MAX_LOCATIONS) )
            target = target.queryParam(Option.MAX_LOCATIONS.name, 1);
        for(Map.Entry<Option,?> entry : requestOptions.entrySet())
            target = target.queryParam( entry.getKey().name, entry.getValue());

        LOGGER.debug(String.format("Executing geocoding request to URI: '%s'", target.getUri()));
        Response response = target.request().buildGet().invoke();
        try {
            final long roundTripTime = System.currentTimeMillis() - requestStart;
            return validateResponse(response, requestStart, roundTripTime);
        } finally {
            response.close();
        }
    }

    public GeocodingResponse[] getBatchParallel(final int parallelThreads, final int triesBeforeFail,
                                                final String... addresses) throws Route360ClientException {
        final ExecutorService executor = Executors.newFixedThreadPool(parallelThreads);
        final List<Callable<GeocodingResponse>> requests = new ArrayList<>();
        for (String singleAddress : addresses) {
            requests.add( () -> {
                // will terminate after the 5th try
                for( int nbrTry = 0; nbrTry < triesBeforeFail; nbrTry ++) {
                    try {
                        return get(singleAddress);
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
            //shutdown of the parallel executor
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS))
                    executor.shutdownNow();
            } catch (InterruptedException e) { executor.shutdownNow(); }
            //Collecting results
            GeocodingResponse[] resultArray = new GeocodingResponse[addresses.length];
            int i=0;
            for(Future<GeocodingResponse> result : results)
                resultArray[i++] = result.get();
            return resultArray;
        } catch(InterruptedException | ExecutionException e) {
            throw new Route360ClientException("Parallel Execution Failed! Cause: ", e);
        }
    }

    public GeocodingResponse[] getBatchSequential(String... addresses) throws Route360ClientException {
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

            GeocodingResponse parsedResponse = GSON_PARSER.fromJson(res, GeocodingResponse.class);
            return parsedResponse;
        } else if(response.getStatus() == Response.Status.SERVICE_UNAVAILABLE.getStatusCode() )
            throw new ServiceUnavailableException();
        else
            throw new Route360ClientException("Request failed with response: \n" + response.readEntity(String.class));
    }

}

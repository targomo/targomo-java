package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.Address;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.exception.Route360ClientRuntimeException;
import net.motionintelligence.client.api.request.esri.ESRIAuthenticationDetails;
import net.motionintelligence.client.api.response.GeocodingResponse;
import net.motionintelligence.client.api.response.esri.AuthenticationResponse;
import net.motionintelligence.client.api.util.POJOUtil;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * The {@link GeocodingRequest} uses the ESRI webservice to request a coordinate candidates for one or more addresses.
 *
 * Multiple requests can be made with one {@link GeocodingRequest} object.
 *
 * @see <a href="https://developers.arcgis.com/rest/geocode/api-reference/geocoding-find-address-candidates.htm">Documentation</a>
 */
public class GeocodingRequest implements GetRequest<String, GeocodingResponse> {

    /**
     * Special Geocoding request options that can be set at creation of the request.
     * For more details please see REST service documentation.
     *
     * @see <a href="https://developers.arcgis.com/rest/geocode/api-reference/geocoding-find-address-candidates.htm">Documentation</a>
     */
    public enum Option {
        /**
         * Limits the candidates returned by the findAddressCandidates operation to the specified country or countries,
         * e.g. "sourceCountry=FRA,DEU,ESP"
         * @see <a href="https://developers.arcgis.com/rest/geocode/api-reference/geocode-coverage.htm">Country Codes</a>
         */
        SOURCE_COUNTRY("sourceCountry"),
        /**
         * A set of bounding box coordinates that limit the search area to a specific region, e.g. "-104,35.6,-94.32,41"
         */
        SEARCH_EXTENT("searchExtent"),
        /**
         * Defines an origin point location that is used to sort geocoding candidates based on their proximity to the location.
         */
        CLOSE_LOCATION("location"),
        /**
         * The spatial reference of the x/y coordinates returned by a geocode request, e.g. "outSR=102100"
         * @see <a href="http://resources.arcgis.com/EN/HELP/REST/APIREF/PCS.HTML">PCS</a>
         * @see <a href="http://resources.arcgis.com/EN/HELP/REST/APIREF/GCS.HTML">GCS</a>
         */
        SPATIAL_REFERENCE("outSR"),
        //The list of fields to be returned in the response. So far only default coordinates are returned -
        //https://developers.arcgis.com/rest/geocode/api-reference/geocoding-service-output.htm#ESRI_SECTION1_42D7D3D0231241E9B656C01438209440
        //OUTPUT_FIELDS("outFields"),
        /**
         * If more geocode candidates are supposed to be returned; by default only the one best fitting candidate is returned
         */
        MAX_LOCATIONS("maxLocations"),
        /**
         * Specifies whether the results of the operation will be persisted, if true an authentication is required and
         * the operation execution will be charged to the account
         */
        FOR_STORAGE("forStorage");

        private String name;
        Option(String repName){
            this.name = repName;
        }
    }

    //ESRI Service constants
    private static final String REST_URI            = "http://geocode.arcgis.com";
    private static final String PATH_SINGLE_ADDRESS = "arcgis/rest/services/World/GeocodeServer/findAddressCandidates";
    private static final String URI_AUTHENTICATION  = "https://www.arcgis.com";
    private static final String PATH_AUTHENTICATION = "sharing/oauth2/token";
    private static final Integer ESRI_ERROR_INVALID_TOKEN = 498;

    //Class constants
    private static final ObjectMapper JSON_PARSER   = JsonFactory.create();
    private static final Logger       LOGGER        = LoggerFactory.getLogger(GeocodingRequest.class);

    //"Immutable" Object values
    private final Client client;
    private final ESRIAuthenticationDetails authenticationDetails;
    private final Map<Option,String> requestOptions;

    //currently valid access token
    private String  currentAccessToken = null;

    /**
     * Creation of a default geo coding request without ESRI credentials (for batch requests slower than with credentials)
     *
     * <p>
     * Note1: The client's lifecycle is not handled here. Please make sure to properly close the client when not
     * needed anymore. <br>
     * Note2: For the parallel batch requests it may be required to set a certain connection pool size when client is
     * created. (e.g. this was necessary for a JBoss client but not for the Jersey client)
     * </p>
     *
     * @param client specified Client implementation to be used, e.g. Jersey or jBoss client
     */
    public GeocodingRequest(Client client){
        this(client, new EnumMap<>(Option.class));
    }

    /**
     * Use a custom client implementation for the geo coding request with non-default request parameters and no
     * ESRI credentials)
     *
     * <p>
     * Note1: The client's lifecycle is not handled here. Please make sure to properly close the client when not
     * needed anymore.<br>
     * Note2: For the parallel batch requests it may be required to set a certain connection pool size when client is
     * created. (e.g. this was necessary for a JBoss client but not for the Jersey client)
     * </p>
     *
     * @param client specified Client implementation to be used, e.g. Jersey or jBoss client
     * @param extraOptions see {@link Option} for possibilities - null pointer and empty strings will be ignored
     */
    public GeocodingRequest(Client client, Map<Option,String> extraOptions){
        this(client, null, extraOptions);
    }

    /**
     * Creation of a default geo coding request with ESRI credentials.
     *
     * <p>
     * Note1: The client's lifecycle is not handled here. Please make sure to properly close the client when not
     * needed anymore. <br>
     * Note2: For the parallel batch requests it may be required to set a certain connection pool size when client is
     * created. (e.g. this was necessary for a JBoss client but not for the Jersey client)
     * </p>
     *
     * @param client specified Client implementation to be used, e.g. Jersey or jBoss client
     * @param authenticationDetails the authentication details with which accessToken will be retrieved
     */
    public GeocodingRequest(Client client, ESRIAuthenticationDetails authenticationDetails){
        this(client, authenticationDetails, new EnumMap<>(Option.class));
    }

    /**
     * Use a custom client implementation for the geo coding request with non-default request parameters and
     * ESRI credentials).
     *
     * <p>
     * Note1: The client's lifecycle is not handled here. Please make sure to properly close the client when not
     * needed anymore.<br>
     * Note2: For the parallel batch requests it may be required to set a certain connection pool size when client is
     * created. (e.g. this was necessary for a JBoss client but not for the Jersey client)
     * </p>
     *
     * @param client specified Client implementation to be used, e.g. Jersey or jBoss client
     * @param authenticationDetails the authentication details with which accessToken will be retrieved
     * @param extraOptions see {@link Option} for possibilities - null pointer and empty strings will be ignored
     */
    public GeocodingRequest(Client client, ESRIAuthenticationDetails authenticationDetails, Map<Option,String> extraOptions){
        this.client	                = client;
        this.requestOptions         = extraOptions;
        this.authenticationDetails  = authenticationDetails;
        //validation
        if("true".equals(extraOptions.get(Option.FOR_STORAGE))) {
            Objects.requireNonNull(this.authenticationDetails,
                    "client authorization is required for option " + Option.FOR_STORAGE.name + "=true");
        }
    }

    /**
     * Invokes a simple request to geocode one address. The address is given in a single {@link String}.
     *
     * @param singleLineAddress e.g. "Chausseestr. 101, 10115 Berlin"
     * @return the parsed REST service response
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    @Override
    public GeocodingResponse get(String singleLineAddress) throws Route360ClientException, ProcessingException {
        return get( webTarget -> webTarget.queryParam("singleLine", singleLineAddress));
    }

    /**
     * Invokes a simple request to geocode one address. The address is given in an {@link Address}.
     * Empty or null fields will be ignored by the request.
     *
     * @param address e.g. <pre>new Address("Chausseestr. 101","","Berlin","10115",null);</pre>
     * @return the parsed REST service response
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    public GeocodingResponse get(Address address) throws Route360ClientException, ProcessingException {
        return get( webTarget ->
            conditionalQueryParam("address", address.street,
            conditionalQueryParam("address2", address.streetDetails,
            conditionalQueryParam("city", address.city,
            conditionalQueryParam("postal", address.postalCode,
            conditionalQueryParam("countryCode", address.country, webTarget))))));
    }

    /**
     * Private method to only add a query paramter when the value is not null and not an empty string.
     */
    private WebTarget conditionalQueryParam(String key, String value, WebTarget webTarget) {
        if( value == null || value.isEmpty() )
            return webTarget;
        else
            return webTarget.queryParam(key,value);
    }

    /**
     * Private Method facilitating a single geocoding request, i.e. creating web target, requesting the result, and
     * validating/parsing the result. Also includes authentication and the usage of access tokens if this request
     * was created with user credentials.
     *
     * @param queryPrep function to prepare the query
     * @return the resulting {@link GeocodingResponse}
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    private GeocodingResponse get(Function<WebTarget,WebTarget> queryPrep)
            throws Route360ClientException, ProcessingException {

        //prepare statement
        WebTarget target = queryPrep.apply(client.target(REST_URI)
                .path(PATH_SINGLE_ADDRESS)
                .queryParam("f", "json"));
        if(!requestOptions.containsKey(Option.MAX_LOCATIONS) )
            target = target.queryParam(Option.MAX_LOCATIONS.name, 1);
        for(Map.Entry<Option,String> entry : requestOptions.entrySet())
            target = conditionalQueryParam(entry.getKey().name, entry.getValue(), target);

        boolean tokenIsInvalid = this.currentAccessToken == null;
        WebTarget finalTarget;
        do {
            //add token if authorization available
            if (authenticationDetails != null) {
                if ( tokenIsInvalid )
                    authenticateWithAccountAndRetrieveValidToken(); //throws Exception if unsuccessful
                finalTarget = target.queryParam("token", this.currentAccessToken);
            } else
                finalTarget = target;
            //execute request
            LOGGER.debug("Executing geocoding request to URI: " + finalTarget.getUri());
            Response response = finalTarget.request().buildGet().invoke();
            try {
                GeocodingResponse reqResponse = validateGeocodingResponse(response);
                if (reqResponse.wasErrorResponse() &&
                        reqResponse.getError().getCode().equals(ESRI_ERROR_INVALID_TOKEN)) {
                    if( tokenIsInvalid ) // do not loop a second time - instead throw an error
                        throw new Route360ClientException( Thread.currentThread() + "Freshly retrieved token already " +
                                "invalid - should never happen: \n" + POJOUtil.prettyPrintPOJO(reqResponse.getError()));
                    tokenIsInvalid = true;
                } else
                    return reqResponse; //successful request with valid response (can also be an error unrelated to the token validity)
            } finally {
                response.close();
            }
        } while (true);
        // The above while-loop is looping at least once and a maximum of two times - there are three possible outcomes:
        // (1) The authentication (either executed in the first or second loop iteration) produced an error -> Route360ClientException
        // (2) An Token invalid error occurred AFTER a token was retrieved through authentication -> Route360ClientException
        // (3) The geocoding request finished "successfully" without the above two errors:
        //      (3a) Token there and still valid -> valid response
        //      (3b) Token not there -> authentication -> request -> valid response
        //      (3c) Token there but invalid -> 2. iteration of loop -> (3b)
        //     Note that a valid response can also be an error caused by a different request problem
    }

    /**
     * Private method to make sure a valid access token is set for the next request
     *
     * @return true if successful
     * @throws Route360ClientException if unsuccessful
     */
    private boolean authenticateWithAccountAndRetrieveValidToken() throws Route360ClientException {

        this.currentAccessToken = null;
        synchronized (this.authenticationDetails) {
            //make sure only one authorization is carried out
            if (this.currentAccessToken == null)
                this.currentAccessToken = retrieveNewTokenViaAuthentication();
        }
        return true;
    }

    /**
     * Caries out the authentication against the ESRI service interface - uses authenticationDetails to retrieve a
     * valid token (which can expire after a while)
     *
     * @return the access token
     * @throws Route360ClientException if unsuccessful - otherwise returns the access token
     */
    private String retrieveNewTokenViaAuthentication() throws Route360ClientException {
        WebTarget target = client
                .target(URI_AUTHENTICATION)
                .path(PATH_AUTHENTICATION)
                .queryParam("grant_type", "client_credentials")
                .queryParam("f", "json")
                .queryParam("client_id", this.authenticationDetails.getClientID())
                .queryParam("client_secret", this.authenticationDetails.getClientSecret())
                .queryParam("expiration", this.authenticationDetails.getTokenExpirationInMinutes());

        LOGGER.debug("Have to redo authentication for ESRI user with client id: {}",
                this.authenticationDetails.getClientID());
        Response response = target.request().buildGet().invoke();
        AuthenticationResponse auth;
        try {
            auth = validateAuthenticationResponse(response);
            if (auth.wasErrorResponse())
                throw new Route360ClientException("Error occurred during authentication with ESRI Service - \nRequest: \n" +
                        target.getUri() + "\nResponse: \n" + POJOUtil.prettyPrintPOJO(auth.getError()));
        } finally {
            response.close();
        }
        return auth.getAccessToken();
    }

    /**
     * Facilitating a parallel batch request for geocoding multiple addresses given as single String. It uses an
     * {@link ExecutorService} with a specified thread pool size. These threads are used to request single geocoding
     * results in parallel. Due to temporal unavailability of the service the request may be repeated a number
     * of times before failing.
     *
     * @see GeocodingRequest#get(String)
     *
     * @param parallelThreads greater than or equal to 1 (==1 means sequential processing) - be careful to not create too many Threads
     * @param triesBeforeFail greater than or equal to 1
     * @param addresses not null and with at least on element
     * @return the resulting individual responses - same order as input addresses
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    public GeocodingResponse[] getBatchParallel( final int parallelThreads, final int triesBeforeFail,
                                                 final String... addresses)
                                                throws Route360ClientException, ProcessingException {
        return getBatchParallel( this::get, parallelThreads, triesBeforeFail, addresses);
    }

    /**
     * Facilitating a parallel batch request for geocoding multiple {@link Address Addresses}. It uses an
     * {@link ExecutorService} with a specified thread pool size. These threads are used to request single geocoding
     * results in parallel. Due to temporal unavailability of the service the request may be repeated a number
     * of times before failing.
     *
     * @see GeocodingRequest#get(Address)
     *
     * @param parallelThreads greater than or equal to 1 (==1 means sequential processing) - be careful to not create too many Threads
     * @param triesBeforeFail greater than or equal to 1
     * @param addresses not null and with at least on element
     * @return the resulting individual responses - same order as input addresses
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    public GeocodingResponse[] getBatchParallel( final int parallelThreads, final int triesBeforeFail,
                                                 final Address... addresses)
                                                throws Route360ClientException, ProcessingException {
        return getBatchParallel( this::get, parallelThreads, triesBeforeFail, addresses);
    }

    /**
     * Private Method facilitating a parallel batch request for geocoding multiple addresses. It uses an
     * {@link ExecutorService} with a specified thread pool size. These threads are used to request single geocoding
     * results in parallel. Due to temporal unavailability of the service the request may be repeated a number
     * of times before failing.
     *
     * @see GeocodingRequest#get(Function)
     *
     * @param singleRequest
     * @param parallelThreads greater than or equal to 1 (==1 means sequential processing) - be careful to not create too many Threads
     * @param triesBeforeFail greater than or equal to 1
     * @param addresses not null and with at least on element
     * @param <A> address type (String or {@link Address})
     * @return the resulting individual responses - same order as input addresses
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    private <A> GeocodingResponse[] getBatchParallel(
            final GetRequest<A,GeocodingResponse> singleRequest,
            final int parallelThreads, final int triesBeforeFail, final A[] addresses)
            throws Route360ClientException, ProcessingException {

        if(parallelThreads<1)
            throw new Route360ClientRuntimeException("The number of specified threads has to be equal or greater than one.");
        if(triesBeforeFail<1)
            throw new Route360ClientRuntimeException("The number of specified tries has to be equal or greater than one.");
        if(addresses == null || addresses.length == 0)
            throw new Route360ClientRuntimeException("The addresses array has to be not null and contain at least one element.");

        final ExecutorService executor = Executors.newFixedThreadPool(parallelThreads);
        final List<Callable<GeocodingResponse>> requests = new ArrayList<>();
        for (A singleAddress : addresses) {
            requests.add( () -> { //Adding individual Callables to be executed in available parallel Threads
                // will terminate after the n-th try
                for( int numberOfTries = 0; numberOfTries < triesBeforeFail; numberOfTries ++) {
                    try {
                        return singleRequest.get(singleAddress);
                        // special case since the service is sometimes unavailable when too many parallel requests are processed
                    } catch (ServiceUnavailableException e) { /* do nothing, i.e. ignore the exception and try again */ }
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

    /**
     * Oracle's proposed routine to savely shutdown the {@link ExecutorService}.
     * @param executor
     * @throws InterruptedException
     */
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

    /**
     * Facilitating a sequential batch request for geocoding multiple addresses each given as single String.
     *
     * @see GeocodingRequest#get(String)
     *
     * @param addresses not null and with at least on element
     * @return the resulting individual responses - same order as input addresses
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    public GeocodingResponse[] getBatchSequential(String... addresses) throws Route360ClientException, ProcessingException {
        return getBatchSequential( this::get, addresses);
    }

    /**
     * Facilitating a sequential batch request for geocoding multiple {@link Address Addresses}.
     *
     * @see GeocodingRequest#get(Address)
     *
     * @param addresses not null and with at least on element
     * @return the resulting individual responses - same order as input addresses
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    public GeocodingResponse[] getBatchSequential(Address... addresses) throws Route360ClientException, ProcessingException {
        return getBatchSequential( this::get, addresses);
    }

    public String getCurrentAccessToken() {
        return currentAccessToken;
    }

    /**
     * Private helper method to sequentially batch request for geocoding multiple {@link Address Addresses}.
     *
     * @see GeocodingRequest#get(Address)
     *
     * @param addresses not null and with at least on element
     * @return the resulting individual responses - same order as input addresses
     * @throws Route360ClientException when error occurs during request
     * @throws ProcessingException when connection error occurs
     */
    private <A> GeocodingResponse[] getBatchSequential(final GetRequest<A,GeocodingResponse> singleRequest,
                                                      A[] addresses) throws Route360ClientException, ProcessingException {
        if(addresses == null || addresses.length == 0)
            throw new Route360ClientException("The addresses array has to be not null and contain at least one element.");

        GeocodingResponse[] batchResults = new GeocodingResponse[addresses.length];
        for( int i = 0; i<addresses.length; i++)
            batchResults[i] = singleRequest.get(addresses[i]);
        return batchResults;
    }

    /**
     * Private method to validate and parse the geocoding request response.
     *
     * @param response object to be validated
     * @return interpreted {@link GeocodingResponse}
     * @throws Route360ClientException when error occurs during request
     */
    private GeocodingResponse validateGeocodingResponse(final Response response) throws Route360ClientException {
        return validateResponse(response, GeocodingResponse::createFromJson);
    }

    /**
     * Private method to validate and parse the ESRI authentication request response.
     *
     * @param response object to be validated
     * @return interpreted {@link AuthenticationResponse}
     * @throws Route360ClientException when error occurs during request
     */
    private AuthenticationResponse validateAuthenticationResponse(final Response response) throws Route360ClientException {
        return validateResponse(response, jsonString -> JSON_PARSER.fromJson(jsonString, AuthenticationResponse.class) );
    }

    /**
     * Private method to help validate and parse a request response.
     *
     * @param response object to be validated
     * @param parser the parser that is executed if no errors occurred
     * @param <T> the return type of the response, e.g. {@link AuthenticationResponse} or {@link GeocodingResponse}
     * @return interpreted/parsed response of type T
     * @throws Route360ClientException when an unexpected error occurs during request
     */
    private <T> T validateResponse(final Response response, final Function<String,T> parser) throws Route360ClientException {
        // compare the HTTP status codes, NOT the route 360 code
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            // parse the results
            String jsonString = response.readEntity(String.class);
            return parser.apply(jsonString);
        } else if(response.getStatus() == Response.Status.SERVICE_UNAVAILABLE.getStatusCode() )
            throw new ServiceUnavailableException(); // Some clients (e.g. jBoss) return SERVICE_UNAVAILABLE while others will wait
        else
            throw new Route360ClientException("Request failed with response: \n" + response.readEntity(String.class));
    }

}

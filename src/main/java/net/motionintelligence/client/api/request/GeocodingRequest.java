package net.motionintelligence.client.api.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.response.GeocodingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Map;

/**
 * Calculates coordinates using the ...
 * TODO proper documentation
 * TODO refactor all requests - a lot of potential since it's all kind of repetitive
 * TODO make other requests with no Client deprecated too *
 */
public class GeocodingRequest {

    private static final String REST_URI            = "http://geocode.arcgis.com";
    private static final String PATH_SINGLE_ADDRESS = "arcgis/rest/services/World/GeocodeServer/findAddressCandidates";
    private static final String PATH_BATCH_REQUEST  = "arcgis/rest/services/World/GeocodeServer/geocodeAddresses";

    private static final Logger LOGGER      = LoggerFactory.getLogger(GeocodingRequest.class);
    private static final Gson   GSON_PARSER = new GsonBuilder().create();

    private final Client client;
    private final Map<String,?> requestOptions;

    /**
     * Use default client implementation for the geo coding request
     * Default client uses {@link ClientBuilder}.
     *
     * Notice: Is deprecated since client lifecylce should be managed by the parent class (opening and closing clients)
     */
    @Deprecated
    public GeocodingRequest() {
        this(ClientBuilder.newClient());
    }

    /**
     * Use a custom client implementation for the geo coding request
     * @param client specified Client implementation to be used
     */
    public GeocodingRequest(Client client){
        this(client, Collections.EMPTY_MAP);
    }

    /**
     * Use a custom client implementation for the geo coding request and set non-default request parameters.
     *
     * @see <a href="https://developers.arcgis.com/rest/geocode/api-reference/geocoding-find-address-candidates.htm">Documentation</a>
     *
     * @param client specified Client implementation to be used
     * @param extraOptions as map
     *
     * TODO may set region, city, country code
     * TODO may set different spatial reference point (outSR)
     * TODO may set maxLocations
     * TODO matchOutOfRange
     * TODO may set sourceCountry
     * TODO forStorage
     * TODO outFields
     */
    public GeocodingRequest(Client client, Map<String,?> extraOptions){
        this.client	        = client;
        this.requestOptions = extraOptions;
    }

    /**
     * Execute request TODO
     *
     * @return GeocodingResponse
     * @throws Route360ClientException In case of error other than Gateway Timeout
     */
    public GeocodingResponse get(String address) throws Route360ClientException {

        final long requestStart = System.currentTimeMillis();
        WebTarget target = client.target(REST_URI)
                .path(PATH_SINGLE_ADDRESS)
                .queryParam("f", "pjson")
                .queryParam("singleLine", address);
        if( !requestOptions.containsKey("maxLocations") )
            target = target.queryParam("maxLocations", 1);
        for(Map.Entry<String,?> entry : requestOptions.entrySet())
            target = target.queryParam( entry.getKey(), entry.getValue());

        LOGGER.debug(String.format("Executing reachability request to URI: '%s'", target.getUri()));
        Response response = target.request().get();

        final long roundTripTime = System.currentTimeMillis() - requestStart;
        return validateResponse(response, requestStart, roundTripTime);
    }

    public GeocodingResponse getBatch(String... address) throws Route360ClientException {
        //(1) Get batch request

        //(2) If fails do single requests (but in parallel threads)

        return null; //TODO
    }

    private GeocodingResponse validateResponse(final Response response,
                                                  final long requestStart, final long roundTripTime)
            throws Route360ClientException {
        // compare the HTTP status codes, NOT the route 360 code
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            // consume the results
            String res = response.readEntity(String.class);
            System.out.println( res );

            GeocodingResponse r = GSON_PARSER.fromJson(res, GeocodingResponse.class);
            System.out.println( r );
            return r; //FIXME
//            return new GeocodingResponse(travelOptions, JsonUtil.parseString(res), requestStart);
        } else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
            return null;
//            return new GeocodingResponse("gateway-time-out", roundTripTime, requestStart);
        } else {
            throw new Route360ClientException(response.readEntity(String.class), null);
        }
    }

    public static void main(String[] args) throws Route360ClientException {
        Client client = ClientBuilder.newClient();
        new GeocodingRequest(client).get( "380 New York St, Redlands, California 92373");
        new GeocodingRequest(client).get( "Chaussee 101 Berlin");
        client.close();
    }
}

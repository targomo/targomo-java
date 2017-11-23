package net.motionintelligence.client.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.request.ssl.SslClientGenerator;
import net.motionintelligence.client.api.response.DefaultResponse;
import net.motionintelligence.client.api.response.MultiGraphResponse;
import net.motionintelligence.client.api.util.IOUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.function.Function;

/**
 * TODO update documentation
 * Creates polygons for the source points with specified travel times in minutes.
 * In case of GeoJson output, Polygons will be buffered, simplified and transformed
 * according to the respective values in {@link net.motionintelligence.client.api.TravelOptions}.
 * Buffer should be given in meters or in degrees, depending on the output CRS's unit.
 */
public class MultiGraphRequest {

    private static final Logger LOGGER      = LoggerFactory.getLogger(MultiGraphRequest.class);
    private static final String HTTP_METHOD = HttpMethod.GET; //HttpMethod.POST; FIXME make it a parameter maybe

    private final Client client;
    private final TravelOptions travelOptions;

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call.
     *
     * @param travelOptions
     * @return
     */
    public static MultiGraphResponse executeRequest(TravelOptions travelOptions) throws Route360ClientException {
        Client client = SslClientGenerator.initClient();
        try{
            return new MultiGraphRequest(client,travelOptions).get();
        } finally {
            client.close();
        }
    }

    /**
     * Use custom client with specified travelOptions
     * @param client Client to be used
     * @param travelOptions Travel options parameters
     */
    public MultiGraphRequest(Client client, TravelOptions travelOptions) {
        this.client	= client;
        this.travelOptions = travelOptions;
    }

    /**
     * Execute request
     * @return the request's response - preferably a multigraph
     * @throws Route360ClientException In case of error other than Gateway Timeout
     */
    public MultiGraphResponse get() throws Route360ClientException {

        long startTimeMillis = System.currentTimeMillis();

        WebTarget request = client.target(travelOptions.getServiceUrl())
                .path("v1/multigraph")
                .queryParam("cb", Constants.CALLBACK)
                .queryParam("key", travelOptions.getServiceKey());

        // Execute request
        Response response;
        String config = RequestConfigurator.getConfig(travelOptions);
        if (HttpMethod.GET.equals(HTTP_METHOD)) {
            request  = request.queryParam("cfg", IOUtil.encode(config));
            response = request.request().get();
        }
        else if (HttpMethod.POST.equals(HTTP_METHOD)) {
            response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
        } else {
            throw new Route360ClientException("HTTP Method not supported: " + HTTP_METHOD);
        }

        // Validate & return
        return validateResponse(response, System.currentTimeMillis() - startTimeMillis);
    }

    /**
     * Validate HTTP response and return a Response FIXME
     * /**
     * Generated polygons in JSON format. <p>
     * Example: <br>
     * <code> {
         "requestTime": "2314",
         "code": "ok",
         "data": ...
        } </code> </p>
     *
     *
     *
     * @param response HTTP response
     * @param roundTripTimeMillis Execution time in milliseconds
     * @return PolygonResponse
     * @throws Route360ClientException In case of errors other than GatewayTimeout
     */
    private MultiGraphResponse validateResponse(final Response response, final long roundTripTimeMillis)
            throws Route360ClientException {

        // Check HTTP status
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            long startParsing = System.currentTimeMillis();
            String resultString = IOUtil.getResultString(response);
            try {
                MultiGraphResponse parsedResponse  = new ObjectMapper().readValue(resultString, MultiGraphResponse.class);
                long parseTime = System.currentTimeMillis() - startParsing;
                final String responseCode = parsedResponse.getCode();
                if (Constants.EXCEPTION_ERROR_CODE_NO_ROUTE_FOUND.equals(responseCode)
                        || Constants.EXCEPTION_ERROR_CODE_COULD_NOT_CONNECT_POINT_TO_NETWORK.equals(responseCode)
                        || Constants.EXCEPTION_ERROR_CODE_TRAVEL_TIME_EXCEEDED.equals(responseCode)
                        || Constants.EXCEPTION_ERROR_CODE_UNKNOWN_EXCEPTION.equals(responseCode)) {
                    throw new Route360ClientException(resultString.toString(), null);
                }
                parsedResponse.setExtraParameters(travelOptions,roundTripTimeMillis,parseTime);
                return parsedResponse;
            } catch (IOException e) {
                throw new Route360ClientException("Exception occurred for result: " + resultString, e);
            }
        } else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
            return createGatewayTimeoutMultiGraphResponse(roundTripTimeMillis);
        } else {
            throw new Route360ClientException("Status: " + response.getStatus() + ": " + response.readEntity(String.class), null);
        }
    }

    private MultiGraphResponse createGatewayTimeoutMultiGraphResponse(long roundTripTimeMillis) {
        MultiGraphResponse gateWayTimeoutResponse = new MultiGraphResponse();
        gateWayTimeoutResponse.setCode("gateway-time-out");
        gateWayTimeoutResponse.setData(Collections.EMPTY_MAP);
        gateWayTimeoutResponse.setMessage("");
        gateWayTimeoutResponse.setRequestTimeMillis(-1);
        gateWayTimeoutResponse.setExtraParameters(travelOptions, roundTripTimeMillis, -1);
        return gateWayTimeoutResponse;
    }
}
package net.motionintelligence.client.api.request.refactored;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.request.ssl.SslClientGenerator;
import net.motionintelligence.client.api.response.refactored.DefaultResponse;
import net.motionintelligence.client.api.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.function.BiFunction;

/**
 * TODO update documentation
 */
public abstract class R360Request<O,I,R extends DefaultResponse<O,I>> {

    private static final Logger LOGGER      = LoggerFactory.getLogger(R360Request.class);

    private final Class<R> clazz;
    private final String httpMethod;
    private final String path;
    private final Client client;
    private final TravelOptions travelOptions;

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call.
     *
     * Example how to call it: MultiGraphResponse r = R360Request.executeRequest(MultiGraphRequest::new,travelOptions);
     *
     * @param travelOptions
     * @return
     */
    protected static <O,I,RS extends DefaultResponse<O,I>,RQ extends R360Request<O,I,RS>> RS
                    executeRequest(BiFunction<Client,TravelOptions,RQ> constructor,
                                   TravelOptions travelOptions) throws Route360ClientException {
        Client client = SslClientGenerator.initClient();
        try{
            return constructor.apply(client,travelOptions).get();
        } finally {
            client.close();
        }
    }

    /**
     * Use custom client with specified travelOptions
     * @param client Client to be used
     * @param travelOptions Travel options parameters
     */
    public R360Request(Client client, TravelOptions travelOptions, String path, String httpMethod, Class<R> clazz) {
        this.client	= client;
        this.travelOptions = travelOptions;
        this.path = path;
        this.httpMethod = httpMethod;
        this.clazz = clazz;
    }

    /**
     * Execute request
     * @return the request's response - preferably a multigraph
     * @throws Route360ClientException In case of error other than Gateway Timeout
     */
    public R get() throws Route360ClientException {

        long startTimeMillis = System.currentTimeMillis();
        WebTarget request = client.target(travelOptions.getServiceUrl())
                .path(path)
                .queryParam("cb", Constants.CALLBACK)
                .queryParam("key", travelOptions.getServiceKey());

        // Execute request
        Response response;
        String config = RequestConfigurator.getConfig(travelOptions);
        if (HttpMethod.GET.equals(httpMethod)) {
            request  = request.queryParam("cfg", IOUtil.encode(config));
            response = request.request().get();
        }
        else if (HttpMethod.POST.equals(httpMethod)) {
            response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
        } else {
            throw new Route360ClientException("HTTP Method not supported: " + httpMethod);
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
    private R validateResponse(final Response response, final long roundTripTimeMillis)
            throws Route360ClientException {

        // Check HTTP status
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            long startParsing = System.currentTimeMillis();
            String resultString = IOUtil.getResultString(response);
            try {
                R parsedResponse  = new ObjectMapper().readValue(resultString, clazz);
                long parseTime = System.currentTimeMillis() - startParsing;
                final String responseCode = parsedResponse.getCode();
                if (Constants.EXCEPTION_ERROR_CODE_NO_ROUTE_FOUND.equals(responseCode)
                        || Constants.EXCEPTION_ERROR_CODE_COULD_NOT_CONNECT_POINT_TO_NETWORK.equals(responseCode)
                        || Constants.EXCEPTION_ERROR_CODE_TRAVEL_TIME_EXCEEDED.equals(responseCode)
                        || Constants.EXCEPTION_ERROR_CODE_UNKNOWN_EXCEPTION.equals(responseCode)) {
                    throw new Route360ClientException(resultString, null);
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

    private R createGatewayTimeoutMultiGraphResponse(long roundTripTimeMillis) throws Route360ClientException {
        try {
            R gateWayTimeoutResponse = clazz.newInstance();
            gateWayTimeoutResponse.setCode("gateway-time-out");
            gateWayTimeoutResponse.setMessage("");
            gateWayTimeoutResponse.setRequestTimeMillis(-1);
            gateWayTimeoutResponse.setExtraParameters(travelOptions, roundTripTimeMillis, -1);
            return gateWayTimeoutResponse;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new Route360ClientException("Response Instantiation failed with error", e);
        }
    }
}

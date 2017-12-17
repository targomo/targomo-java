package net.motionintelligence.client.api.request.refactored;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.jackson.datatype.trove.TroveModule;
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
 *
 * @param <O> Final Type of the output stored in the "data" field of the response
 * @param <I> Type of data that Jackson does create and from which the data of type O is parsed,
 *           e.g. usually {@link java.util.Map} of {@link String} to {@link Object} for an
 *           object or a {@link java.util.List} for an array/list
 * @param <R> The Response type of the request
 */
public abstract class R360Request<O,I,R extends DefaultResponse<O,I>> {

    //TODO use logger
    private static final Logger       LOGGER = LoggerFactory.getLogger(R360Request.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new TroveModule(-1));
    }

    private final Class<R> clazz;
    private final String httpMethod;
    private final String path;
    private final Client client;
    private final TravelOptions travelOptions;

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * Example how to call it: MultiGraphResponse r = R360Request.executeRequest(MultiGraphRequest::new,travelOptions);
     *
     * @param constructor the request constructor expecting two parameters: client and traveloptions
     * @param travelOptions the travel options for this request
     * @param <O> Final Type of the output stored in the "data" field of the response
     * @param <I> Type of data that Jackson does create and from which the data of type <O> is parsed,
     *           e.g. usually Map<String,Object> for an object or List<..> for an array/list
     * @param <RS> The Response type of the request
     * @param <RQ> The Request type of this execution
     * @return the response of the type RS
     * @throws Route360ClientException when an error occurred during the request call
     */
    static <O,I,RS extends DefaultResponse<O,I>,RQ extends R360Request<O,I,RS>> RS
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
     *
     * @param client Client to be used
     * @param travelOptions Travel options parameters
     * @param path path of request (URL is in travel options)
     * @param httpMethod (HttpMethod.GET or HttpMethod.POST) are options
     * @param clazz the Response class, e.g. PolygonResponse.class
     */
    R360Request(Client client, TravelOptions travelOptions, String path, String httpMethod, Class<R> clazz) {
        this.client	= client;
        this.travelOptions = travelOptions;
        this.path = path;
        this.httpMethod = httpMethod;
        this.clazz = clazz;
    }

//TODO add this method
//    /**
//     * For debugging.
//     *
//     * @return the request as curl String
//     * @throws Route360ClientException when error occurred during parsing of the travel options
//     */
//    public String toCurl() throws Route360ClientException {
//        String url = travelOptions.getServiceUrl().endsWith("/") ?
//                travelOptions.getServiceUrl() : travelOptions.getServiceUrl() + "/";
//        return "curl -X POST '" +
//                url + "v1/time" +
//                "?cb=" + CALLBACK +
//                "&key=" + travelOptions.getServiceKey() + "' " +
//                "-H 'content-type: application/json' " +
//                "-d '" + RequestConfigurator.getConfig(travelOptions) + "'";
//    }

    /**
     * Executes the request
     *
     * @return the request's response of type R
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
     * Validate HTTP response and return a Response
     * Generated polygons in JSON format. <p>
     * Example: <br>
     * <code> {
         "requestTime": "2314",
         "code": "ok",
         "data": ...
         "message": "all good"
     } </code> </p>
     *
     *
     * @param response HTTP response
     * @param roundTripTimeMillis Execution time in milliseconds
     * @return the validated reponse of type R
     * @throws Route360ClientException in case of errors other than GatewayTimeout
     */
    private R validateResponse(final Response response, final long roundTripTimeMillis)
            throws Route360ClientException {

        // Check HTTP status
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            long startParsing = System.currentTimeMillis();
            String resultString = IOUtil.getResultString(response);
            try {
                R parsedResponse  = MAPPER.readValue(resultString, clazz);
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

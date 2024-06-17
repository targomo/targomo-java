package com.targomo.client.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.request.ssl.SslClientGenerator;
import com.targomo.client.api.response.DefaultResponse;
import com.targomo.client.api.response.ResponseCode;
import com.targomo.client.api.util.IOUtil;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


/**
 * Base request to the targomo API. Currently supported requests:
 * - MultiGraph
 *
 * @param <R> The Response type of the request
 */
public abstract class TargomoRequest<R extends DefaultResponse<?,?>> {

    protected static final ObjectMapper MAPPER = new ObjectMapper(); //protected so you might add additional parsing modules
    private static final String CALLBACK = "callback";

    private final Class<R> clazz;
    private final String httpMethod;
    private final String path;
    private final Client client;
    private final TravelOptions travelOptions;
    private final MultivaluedMap<String, Object> headers;

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * Example how to call it: MultiGraphResponse r = TargomoRequest.executeRequest(MultiGraphRequest::new,travelOptions);
     *
     * @param constructor the request constructor expecting two parameters: client and traveloptions
     * @param travelOptions the travel options for this request
     * @param <O> The Response type of the request
     * @param <C> The Request type of this execution
     * @return the response of the type O
     * @throws TargomoClientException when an error occurred during the request call
     */
    static <O extends DefaultResponse<?,?>, C extends TargomoRequest<O>> O
                    executeRequest(BiFunction<Client,TravelOptions, C> constructor,
                                   TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
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
     * @param headers the request headers
     */
    TargomoRequest(Client client, TravelOptions travelOptions, String path, String httpMethod, Class<R> clazz, MultivaluedMap<String, Object> headers) {
        this.client	= client;
        this.travelOptions = travelOptions;
        this.path = path;
        this.httpMethod = httpMethod;
        this.clazz = clazz;
        this.headers = headers;
    }

    /**
     * For debugging.
     *
     * @return the request as curl String
     * @throws TargomoClientException when error occurred during parsing of the travel options
     */
    public String toCurl() throws TargomoClientException {
        String url = travelOptions.getServiceUrl().endsWith("/") ?
                travelOptions.getServiceUrl() : travelOptions.getServiceUrl() + "/";
        return "curl -X " + this.httpMethod + " '" +
                url + this.path +
                "?cb=" + CALLBACK +
                "&key=" + travelOptions.getServiceKey() + "' " +
                "-H 'content-type: application/json' " +
                headers.entrySet()
                        .stream()
                        .map(entry -> "-H '" + entry.getKey() + ": " + entry.getValue().stream().map(Objects::toString).collect(Collectors.joining(", ")) + "' " )
                        .collect(Collectors.joining()) +
                "-d '" + RequestConfigurator.getConfig(travelOptions) + "'";
    }

    /**
     * Executes the request
     *
     * @return the request's response of type R
     * @throws TargomoClientException In case of error other than Gateway Timeout
     */
    public R get() throws TargomoClientException, ResponseErrorException {

        long startTimeMillis = System.currentTimeMillis();
        WebTarget request = client.target(travelOptions.getServiceUrl())
                .path(path)
                .queryParam("cb", Constants.CALLBACK)
                .queryParam("key", travelOptions.getServiceKey())
                .queryParam(Constants.INTER_SERVICE_KEY, travelOptions.getInterServiceKey())
                .queryParam(Constants.INTER_SERVICE_REQUEST, travelOptions.getInterServiceRequestType());

        // Execute request
        Response response;
        String config = RequestConfigurator.getConfig(travelOptions);
        if (HttpMethod.GET.equals(httpMethod)) {
            request  = request.queryParam("cfg", IOUtil.encode(config));
            Invocation.Builder invocationBuilder = request.request();

            if (!headers.isEmpty())
                invocationBuilder = request.request().headers(headers);

            response = invocationBuilder.get();
        }
        else if (HttpMethod.POST.equals(httpMethod)) {
            Invocation.Builder invocationBuilder = request.request();

            if (!headers.isEmpty())
                invocationBuilder = request.request().headers(headers);

            response = invocationBuilder.post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
        } else {
            throw new TargomoClientException("HTTP Method not supported: " + httpMethod);
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
     * @return the validated response of type R
     * @throws TargomoClientException in case of errors other than GatewayTimeout
     * @throws ResponseErrorException
     */
    private R validateResponse(final Response response, final long roundTripTimeMillis)
            throws TargomoClientException, ResponseErrorException {

        // Check HTTP status
        R parsedResponse = null;
        long startParsing = System.currentTimeMillis();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String resultString = IOUtil.getResultString(response);
            try {
                parsedResponse  = MAPPER.readValue(resultString, clazz);
            } catch (IOException e) {
                throw new TargomoClientException("Exception occurred for result: " + resultString, e, response.getStatus());
            }
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TargomoClientException(String.format("Service not found: %s", travelOptions.getServiceUrl()), response.getStatus());
        } else{
            throw new TargomoClientException(String.format("Request failed with %s: %s", response.getStatus(), response.readEntity(String.class)), response.getStatus());
        }
        if (parsedResponse.getCode() != ResponseCode.OK) {
            String msg = "Request returned an error";
            if (!StringUtils.isEmpty(parsedResponse.getMessage())) {
                msg += ": " + parsedResponse.getMessage();
            }
            throw new ResponseErrorException(parsedResponse.getCode(), msg);
        }

        parsedResponse.finishDeserialization(travelOptions, roundTripTimeMillis, System.currentTimeMillis() - startParsing);
        return parsedResponse;
    }
}

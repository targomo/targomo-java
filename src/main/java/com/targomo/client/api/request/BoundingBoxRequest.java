package com.targomo.client.api.request;

import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.BoundingBoxResponse;
import com.targomo.client.api.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class BoundingBoxRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoundingBoxRequest.class);

    private final Client client;
    private final MultivaluedMap<String, Object> headers;

    TravelOptions travelOptions;

    public BoundingBoxRequest(Client client, TravelOptions travelOptions, MultivaluedMap<String, Object> headers) {
        this.client	= client;
        this.headers = headers;
        this.travelOptions = travelOptions;
    }

    public BoundingBoxRequest(TravelOptions travelOptions) {
        this(ClientBuilder.newClient(), travelOptions, new MultivaluedHashMap<>());
    }

    public BoundingBoxRequest(Client client, TravelOptions travelOptions){
        this(client, travelOptions, new MultivaluedHashMap<>());
    }

    public BoundingBoxResponse get() throws TargomoClientException {

        long startTimeMillis = System.currentTimeMillis();

        WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/boundingbox")
                .queryParam("key", travelOptions.getServiceKey())
                .queryParam(Constants.INTER_SERVICE_KEY, travelOptions.getInterServiceKey())
                .queryParam(Constants.INTER_SERVICE_REQUEST, travelOptions.getInterServiceRequestType());

        final Entity<String> entity = Entity.entity(RequestConfigurator.getConfig(travelOptions), MediaType.APPLICATION_JSON_TYPE);

        LOGGER.debug("Executing reachability request to URI: '{}}'", target.getUri());

        Response response;

        // Execute POST request
        response = target.request().headers(headers).post(entity);


        // Execution time
        long roundTripTimeMillis = (System.currentTimeMillis() - startTimeMillis);

        return parseResponse(response, roundTripTimeMillis);
    }

    private BoundingBoxResponse parseResponse(final Response response, final long roundTripTimeMillis)
            throws TargomoClientException {

        // compare the HTTP status codes, NOT the route 360 code
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            // consume the results
            String responseStr = response.readEntity(String.class);
            JSONObject json = JsonUtil.parseString(responseStr);

            // Check response code
            final String code = json.has("code") ? JsonUtil.getString(json, "code") : null;
            final String message = json.has("message") ? JsonUtil.getString(json, "message") : null;

            if (code != null && !StringUtils.equals(code, "ok")) {
                String msg = "Boundingbox request returned an error";
                if (!StringUtils.isEmpty(message)) {
                    msg += ": " + message;
                }
                throw new TargomoClientException(String.format("Status: %s: %s", code, msg), response.getStatus());
            }

            final Double minX = json.has("minX") ? JsonUtil.getDouble(json, "minX") : null;
            final Double maxX = json.has("maxX") ? JsonUtil.getDouble(json, "maxX") : null;
            final Double minY = json.has("minY") ? JsonUtil.getDouble(json, "minY") : null;
            final Double maxY = json.has("maxY") ? JsonUtil.getDouble(json, "maxY") : null;

            return new BoundingBoxResponse(minX, maxX, minY, maxY, roundTripTimeMillis);
        } else {
            throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
        }
    }

}

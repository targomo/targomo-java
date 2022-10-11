package com.targomo.client.api.request;

import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.quality.Location;
import com.targomo.client.api.quality.criterion.CriterionDefinition;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.ScoreResponse;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ScoreRequest {

    private final Client client;
    private final Map<String, CriterionDefinition> criteria;
    private final List<Location> locations;
    private final List<Location> competitors;

    private final String serviceUrl;
    private final String apiKey;

    private final Boolean showDetails; // default value: false
    private final Boolean forceRecalculate; // default value: false
    private final Boolean cacheResult; // default value: true
    private final MultivaluedMap<String, Object> headers;

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, MultivaluedMap<String, Object> headers) {
        this(ClientBuilder.newClient(), criteria, locations, Collections.emptyList(), serviceUrl, key, null, null, null, headers);
    }

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations) {
        this(ClientBuilder.newClient(), criteria, locations, Collections.emptyList(), serviceUrl, key, null, null, null, new MultivaluedHashMap<>());
    }

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, List<Location> competitors) {
        this(ClientBuilder.newClient(), criteria, locations, competitors, serviceUrl, key, null, null, null, new MultivaluedHashMap<>());
    }
    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, List<Location> competitors, boolean showDetails, boolean forceRecalculate, boolean cacheResult) {
        this(ClientBuilder.newClient(), criteria, locations, competitors, serviceUrl, key, showDetails, forceRecalculate, cacheResult, new MultivaluedHashMap<>());
    }
    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, boolean showDetails, boolean forceRecalculate, boolean cacheResult) {
        this(ClientBuilder.newClient(), criteria, locations, Collections.emptyList(), serviceUrl, key, showDetails, forceRecalculate, cacheResult, new MultivaluedHashMap<>());
    }

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, boolean showDetails, boolean forceRecalculate, boolean cacheResult, MultivaluedMap<String, Object> headers) {
        this(ClientBuilder.newClient(), criteria, locations, Collections.emptyList(), serviceUrl, key, showDetails, forceRecalculate, cacheResult, headers);
    }
    
    //added this for backward compatibility with any of the callers using the all-args constructor directly
    public ScoreRequest(Client client, Map<String, CriterionDefinition> criteria,List<Location> locations, List<Location> competitors,  String serviceUrl, String key, boolean showDetails, boolean forceRecalculate, boolean cacheResult) {
        this(client, criteria, locations, competitors, serviceUrl, key, showDetails, forceRecalculate, cacheResult, new MultivaluedHashMap<>());
    }

    public ScoreResponse get() throws TargomoClientException {
        WebTarget request = client.target(serviceUrl).path("v1/scores")
                .queryParam("apiKey", apiKey);
        if(showDetails != null) {
            request = request.queryParam("showDetails", showDetails);
        }
        if(forceRecalculate != null) {
            request = request.queryParam("forceRecalculate", forceRecalculate);
        }
        if(cacheResult != null) {
            request = request.queryParam("cacheResult", cacheResult);
        }

        String config = RequestConfigurator.getConfig(criteria, locations, competitors);
        Response response = request.request().headers(headers).post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
        return validateResponse(response);
    }

    /**
     * Validate HTTP response and return a PolygonResponse
     * @param response HTTP response
     * @return ScoreResponse
     * @throws TargomoClientException In case of errors other than GatewayTimeout
     */
    public static ScoreResponse validateResponse(final Response response) throws TargomoClientException {
        // compare the HTTP status codes, NOT the route 360 code
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            // consume the results
            JSONObject result = JsonUtil.parseString(IOUtil.getResultString(response));

            String message;
            JSONObject data;
            JSONArray errors = new JSONArray();
            try {
                data = result.getJSONObject("data");
                message = result.getString("message");
                if (result.has("errors")) {
                    errors = result.getJSONArray("errors");
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            DateTime timestamp = DateTime.now();
            return new ScoreResponse(data, message, errors, timestamp);
        } else {
            throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
        }
    }
}

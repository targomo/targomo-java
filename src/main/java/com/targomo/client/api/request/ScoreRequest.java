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

    private final boolean showDetails;

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations) {
        this(ClientBuilder.newClient(), criteria, locations, Collections.emptyList(), serviceUrl, key, false);
    }

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, List<Location> competitors) {
        this(ClientBuilder.newClient(), criteria, locations, competitors, serviceUrl, key, false);
    }
    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, List<Location> competitors, boolean showDetails) {
        this(ClientBuilder.newClient(), criteria, locations, competitors, serviceUrl, key, showDetails);
    }
    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, boolean showDetails) {
        this(ClientBuilder.newClient(), criteria, locations, Collections.emptyList(), serviceUrl, key, showDetails);
    }

    public ScoreResponse get() throws TargomoClientException {
        WebTarget request = client.target(serviceUrl).path("v1/scores")
                .queryParam("apiKey", apiKey)
                .queryParam("showDetails", showDetails);

        String config = RequestConfigurator.getConfig(criteria, locations, competitors);
        Response response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
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
            try {
                data = result.getJSONObject("data");
                message = result.getString("message");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            JSONArray errors = new JSONArray();
            DateTime timestamp = DateTime.now();
            return new ScoreResponse(data, message, errors, timestamp);
        } else {
            throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
        }
    }
}
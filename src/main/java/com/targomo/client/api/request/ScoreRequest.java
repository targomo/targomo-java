package com.targomo.client.api.request;

import com.google.gson.JsonObject;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.quality.Location;
import com.targomo.client.api.quality.criterion.CriterionDefinition;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.ResponseCode;
import com.targomo.client.api.response.RouteResponse;
import com.targomo.client.api.response.ScoreResponse;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ScoreRequest {

    private Client client;
    private Map<String, CriterionDefinition> criteria;
    private List<Location> locations;
    private List<Location> competitors;

    private String serviceUrl;
    private String apiKey;

    private boolean showDetails = false;

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations) {
        this.serviceUrl = serviceUrl;
        this.apiKey = key;
        this.client	= ClientBuilder.newClient();
        this.criteria = criteria;
        this.locations = locations;
    }

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, List<Location> competitors) {
        this(serviceUrl, key, criteria, locations);
        this.competitors = competitors;
    }
    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, List<Location> competitors, boolean showDetails) {
        this(serviceUrl, key, criteria, locations, competitors);
        this.showDetails = showDetails;
    }
    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<Location> locations, boolean showDetails) {
        this(serviceUrl, key, criteria, locations);
        this.showDetails = showDetails;
    }

    public ScoreResponse get() throws TargomoClientException, ResponseErrorException {
        WebTarget request = client.target(serviceUrl).path("v1/scores")
                .queryParam("apiKey", apiKey)
                .queryParam("showDetails", showDetails);

        String config = RequestConfigurator.getConfig(criteria, locations, competitors);
        Response response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
        return validateResponse(response);
    }

    private String getJson(Map<String, CriterionDefinition> criteria, List<Location> locations, List<Location> competitors) {
        Map<String, Object> body = new HashMap<>();
        body.put("criteria", criteria);
        body.put("locations", locations);
        if (competitors != null) {
            body.put("competitors", competitors);
        }
        JSONObject json = new JSONObject(body);
        return JsonUtil.toString(json, 1);
    }

    /**
     * Validate HTTP response and return a PolygonResponse
     * @param response HTTP response
     * @return RouteResponse
     * @throws TargomoClientException In case of errors other than GatewayTimeout
     */
    private ScoreResponse validateResponse(final Response response) throws TargomoClientException {
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
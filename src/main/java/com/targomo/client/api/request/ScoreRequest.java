package com.targomo.client.api.request;

import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.quality.PublicLocation;
import com.targomo.client.api.quality.criterion.CriterionDefinition;
import com.targomo.client.api.request.config.RequestConfigurator;
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
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ScoreRequest {

    private Client client;
    private Map<String, CriterionDefinition> criteria;
    private List<PublicLocation> locations;
    private List<PublicLocation> competitors;

    private String serviceUrl;
    private String apiKey;

    private boolean showDetails = false;

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<PublicLocation> locations) {
        this.serviceUrl = serviceUrl;
        this.apiKey = key;
        this.client	= ClientBuilder.newClient();
        this.criteria = criteria;
        this.locations = locations;
    }

    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<PublicLocation> locations, List<PublicLocation> competitors) {
        this(serviceUrl, key, criteria, locations);
        this.competitors = competitors;
    }
    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<PublicLocation> locations, List<PublicLocation> competitors, boolean showDetails) {
        this(serviceUrl, key, criteria, locations, competitors);
        this.showDetails = showDetails;
    }
    public ScoreRequest(String serviceUrl, String key, Map<String, CriterionDefinition> criteria, List<PublicLocation> locations, boolean showDetails) {
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
package com.targomo.client.api.request;

import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.quality.Location;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.ScoreResponse;
import lombok.AllArgsConstructor;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@AllArgsConstructor
public class RatingRequest {
    private final Client client;
    private final List<Location> locations;
    private final List<Location> competitors;

    private final String serviceUrl;
    private final String apiKey;

    private final boolean showDetails;
    private final String ratingId;

    public RatingRequest(String serviceUrl, String apiKey, String ratingId, List<Location> locations) {
        this(ClientBuilder.newClient(), locations, null, serviceUrl, apiKey, false, ratingId);
    }

    public RatingRequest(String serviceUrl, String key, String ratingId, List<Location> locations, List<Location> competitors) {
        this(ClientBuilder.newClient(), locations, competitors, serviceUrl, key, false, ratingId);
    }

    public RatingRequest(String serviceUrl, String key, String ratingId, List<Location> locations, List<Location> competitors, boolean showDetails) {
        this(ClientBuilder.newClient(), locations, competitors, serviceUrl, key, showDetails, ratingId);
    }

    public RatingRequest(String serviceUrl, String key, String ratingId, List<Location> locations, boolean showDetails) {
        this(ClientBuilder.newClient(), locations, null, serviceUrl, key, showDetails, ratingId);
    }

    public ScoreResponse get() throws TargomoClientException {
        WebTarget request = client.target(serviceUrl).path("v1/rating").path(ratingId).path("/location")
                .queryParam("apiKey", apiKey)
                .queryParam("showDetails", showDetails);

        String config = RequestConfigurator.getConfig(locations, competitors);
        Response response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
        return ScoreRequest.validateResponse(response);
    }
}
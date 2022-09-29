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

    private final Boolean showDetails; // default value: false
    private final Boolean forceRecalculate; // default value: false
    private final Boolean cacheResult; // default value: true
    private final String ratingId;

    public RatingRequest(String serviceUrl, String apiKey, String ratingId, List<Location> locations) {
        this(ClientBuilder.newClient(), locations, null, serviceUrl, apiKey, null, null, null, ratingId);
    }

    public RatingRequest(String serviceUrl, String key, String ratingId, List<Location> locations, List<Location> competitors) {
        this(ClientBuilder.newClient(), locations, competitors, serviceUrl, key, null, null, null, ratingId);
    }

    public RatingRequest(String serviceUrl, String key, String ratingId, List<Location> locations, List<Location> competitors, boolean showDetails, boolean forceRecalculate, boolean cacheResult) {
        this(ClientBuilder.newClient(), locations, competitors, serviceUrl, key, showDetails, forceRecalculate, cacheResult, ratingId);
    }

    public RatingRequest(String serviceUrl, String key, String ratingId, List<Location> locations, boolean showDetails, boolean forceRecalculate, boolean cacheResult) {
        this(ClientBuilder.newClient(), locations, null, serviceUrl, key, showDetails, forceRecalculate, cacheResult, ratingId);
    }

    public ScoreResponse get() throws TargomoClientException {
        WebTarget request = client.target(serviceUrl).path("v1/rating").path(ratingId).path("/location")
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

        String config = RequestConfigurator.getConfig(locations, competitors);
        Response response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
        return ScoreRequest.validateResponse(response);
    }
}
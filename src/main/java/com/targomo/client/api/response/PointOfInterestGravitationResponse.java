package com.targomo.client.api.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PointOfInterestGravitationResponse {

    private final TravelOptions travelOptions;
    private final JSONObject result;
    private final long requestEnd;
    private GravitationResult gravitationResult;

    /**
     * Create a response from JSON results, using given travel options
     *
     * @param travelOptions travel options, from the request
     * @param result        Travel times in JSON
     * @param requestStart  Start time of execution
     */
    public PointOfInterestGravitationResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {

        this.travelOptions = travelOptions;
        this.requestEnd = System.currentTimeMillis() - requestStart;
        this.result = result;

        parseResults();
    }

    /**
     * Parse results in JSON to Poi Gravitation object.
     */
    public void parseResults() {
        try {
            TypeReference<HashMap<String, Float>> typeRef
                    = new TypeReference<HashMap<String, Float>>() {};
            Map<String, Float> resultMap = new ObjectMapper().readValue(this.result.toString(), typeRef);
            Float all = resultMap.get("all");
            resultMap.remove("all");
            gravitationResult = new GravitationResult(all, resultMap);
        } catch (JsonProcessingException e) {
            throw new TargomoClientRuntimeException("Couldn't parse POI reachability summary response", e);
        }
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class GravitationResult {
        private final Float all;
        private final Map<String, Float> clusters;
    }
}

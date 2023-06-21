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

    private static final String ALL_FIELD = "all";
    private final TravelOptions travelOptions;
    private final JSONObject result;
    private final long requestEnd;
    private Map<String, GravitationResult> gravitationResult;

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
            TypeReference<HashMap<String, Map<String, Float>>> typeRef
                    = new TypeReference<HashMap<String, Map<String, Float>>>() {};
            HashMap<String, Map<String, Float>> resultMap = new ObjectMapper().readValue(this.result.toString(), typeRef);
            gravitationResult = new HashMap<>();
            resultMap.forEach((sourceId, resultForThisSource) -> {
                Float all = resultForThisSource.get(ALL_FIELD);
                resultForThisSource.remove(ALL_FIELD);
                gravitationResult.put(sourceId, new GravitationResult(all, resultForThisSource));
            });
        } catch (JsonProcessingException e) {
            throw new TargomoClientRuntimeException("Couldn't parse POI gravitation analysis response", e);
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

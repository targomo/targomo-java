package com.targomo.client.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

import java.util.Map;

@Getter
public class PointOfInterestSummaryResponse {

    private final TravelOptions travelOptions;
    private final JSONObject result;
    private final long requestEnd;
    private POISummary summary;

    /**
     * Create a response from JSON results, using given travel options
     * @param travelOptions travel options, from the request
     * @param result Travel times in JSON
     * @param requestStart Start time of execution
     */
    public PointOfInterestSummaryResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {

        this.travelOptions 	= travelOptions;
        this.requestEnd		= System.currentTimeMillis() - requestStart;
        this.result         = result;

        parseResultResults();
    }

    /**
     * Parse results in JSON to Poi Summary object.
     */
    public void parseResultResults() {
        try {
            summary = new ObjectMapper().readValue(this.result.toString(), POISummary.class);
        }
        catch (JsonProcessingException e){
            throw new TargomoClientRuntimeException("Couldn't parse POI reachability summary response", e);
        }
    }


    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true) // Used to ignore FeatureCollection attribute when deserializing from JSON
    public static class POISummary {
        private int totalPoi;
        private Map<String, Integer> osmTypesCount;
        private Map<String, Integer> groupIdCount;
        private Map<String, Integer> clusterIdCount;
    }
}

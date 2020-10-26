package com.targomo.client.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.Format;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PointOfInterestResponse {

    private final TravelOptions travelOptions;
    private final JSONObject result;
    private final long requestEnd;
    private final Map<String, Integer> edgeWeights;

    /**
     * Create a response from JSON results, using given travel options
     * @param travelOptions travel options, from the request
     * @param result Travel times in JSON
     * @param requestStart Start time of execution
     */
    public PointOfInterestResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {

        this.travelOptions 	= travelOptions;
        this.requestEnd		= System.currentTimeMillis() - requestStart;
        this.result         = result;
        this.edgeWeights    = new HashMap<>();

        mapResults();
    }

    /**
     * Parse results in JSON to travel times map.
     */
    public void mapResults() {

        if (Format.GEOJSON.equals(this.travelOptions.getFormat())) {

            JSONArray jsonArray = JsonUtil.getJsonArray(result, "features");
            for (int i = 0; i < jsonArray.length(); i++) {

                final JSONObject target     = JsonUtil.getJSONObject(jsonArray, i);
                final JSONObject properties = JsonUtil.getJSONObject(target, "properties");

                String trgId = JsonUtil.getString(properties, "id");

                this.addEdgeWeight(trgId, JsonUtil.getInt(properties, Constants.EDGE_WEIGHT));
            }
        }
        else if ( Format.JSON.equals(this.travelOptions.getFormat()) ) {

            final Iterator keysIterator = this.result.keys();
            while ( keysIterator.hasNext() ) {

                final JSONObject obj = JsonUtil.getJSONObject(this.result, (String) keysIterator.next());

                this.addEdgeWeight(JsonUtil.getString(obj, "id"), JsonUtil.getInt(obj, Constants.EDGE_WEIGHT));
            }
        }
        else {

            throw new TargomoClientRuntimeException("Unknown format given: " + this.travelOptions.getFormat());
        }
    }

    /**
     * Add travel time to a target
     * @param targetId Target ID
     * @param travelTime Travel time for the target
     */
    public void addEdgeWeight(String targetId, Integer travelTime) {
        this.edgeWeights.put(targetId, travelTime);
    }


    /**
     * @return the travelOptions
     */
    public TravelOptions getTravelOptions() {
        return travelOptions;
    }

    /**
     * Map of source IDs - travel times
     * @return travel time map
     */
    public Map<String, Integer> getEdgeWeights() {
        return this.edgeWeights;
    }

    public JSONObject getResult() {
        return result;
    }

    public HashMap<String,POI> getResultAsMap() throws IOException
    {
        return new ObjectMapper().readValue(this.result.toString(), POIResponse.class);
    }



    static class POIResponse extends HashMap<String,POI> {}

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true) // Used to ignore FeatureCollection attribute when deserializing from JSON
    public static class POI {
        String type;
        String id;
        double lat;
        double lng;
        Map<String,String> tags;
        String osmType;
        int edgeWeight;
        List<String> groupIds;
        List<String> clusterIds;
        String closestSource;
        boolean bounded;
    }
}

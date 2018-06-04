package com.targomo.client.api.response;

import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.PoiTargetCoordinate;
import com.targomo.client.api.statistic.PoiType;
import com.targomo.client.api.util.JsonUtil;
import com.targomo.client.api.TravelOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OverpassResponse {

    private final TravelOptions travelOptions;
    private final JSONObject result;
    private final long requestEnd;
    private final Map<String, Coordinate> points;
    private List<String> keys = Arrays.asList("amenity", "shop", "tourism");

    /**
     * Create a response from JSON results, using given travel options
     * @param travelOptions travel options, from the request
     * @param result Travel times in JSON
     * @param requestStart Start time of execution
     */
    public OverpassResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {

        this.travelOptions 	= travelOptions;
        this.requestEnd		= System.currentTimeMillis() - requestStart;
        this.result         = result;
        this.points         = new HashMap<>();

        parseResults();
    }

    /**
     * Parse results in JSON to travel times map.
     */
    public void parseResults() {

        final JSONArray elements = JsonUtil.getJsonArray(this.result, "elements");
        String type = this.travelOptions.getOsmTypes().iterator().next().toString();

        for (int i = 0; i < elements.length(); i++) {

            JSONObject element = JsonUtil.getJSONObject(elements, i);
            Coordinate coordinate;

            if ( "node".equals(JsonUtil.getString(element, "type")) ) {

                coordinate = new PoiTargetCoordinate(
                        JsonUtil.getString(element, "id") + type ,
                        JsonUtil.getDouble(element, "lon"),
                        JsonUtil.getDouble(element, "lat"));
            }
            else if ( "way".equals(JsonUtil.getString(element, "type")) && element.has("center") ) {

                coordinate = new PoiTargetCoordinate(
                        JsonUtil.getString(element, "id") + type,
                        JsonUtil.getDouble(JsonUtil.getJSONObject(element, "center"), "lon"),
                        JsonUtil.getDouble(JsonUtil.getJSONObject(element, "center"), "lat"));
            }
            else {

                throw new TargomoClientRuntimeException("only way and node are supported, given: "
                        + JsonUtil.getString(element, "type"));
            }

            ((PoiTargetCoordinate) coordinate).setType(getType(element));

            this.points.put(coordinate.getId(), coordinate);
        }
    }

    private PoiType getType(JSONObject element) {

        final JSONObject tags = JsonUtil.getJSONObject(element, "tags");

        for ( String key : travelOptions.getOsmTypes().stream().map(PoiType::getKey).collect(Collectors.toList()) )  {

            if ( tags.has(key) )
                return new PoiType(key, JsonUtil.getString(tags, key));
        }

        throw new TargomoClientRuntimeException("Not supported type: " + JsonUtil.toString(tags, 2) );
    }

    /**
     * @return the travelOptions
     */
    public TravelOptions getTravelOptions() {
        return travelOptions;
    }

    public JSONObject getResult() {
        return result;
    }

    public Map<String, Coordinate> getTargets() {
        return points;
    }
}

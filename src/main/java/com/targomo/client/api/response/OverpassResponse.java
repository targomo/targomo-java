package com.targomo.client.api.response;

import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.PoiTargetCoordinate;
import com.targomo.client.api.statistic.PoiType;
import com.targomo.client.api.util.JsonUtil;
import com.targomo.client.api.TravelOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OverpassResponse {

    private final TravelOptions travelOptions;
    private final JSONObject result;
    private final PoiType poiType;
    private final long requestEnd;
    private final Map<String, Coordinate> points;

    private static final String CENTER = "center";

    /**
     * Create a response from JSON results, using given travel options
     * @param travelOptions travel options, from the request
     * @param result Travel times in JSON
     * @param requestStart Start time of execution
     * @param poiType Specified PoiType for returned elements
     */
    public OverpassResponse(TravelOptions travelOptions, JSONObject result, long requestStart, PoiType poiType) {

        this.travelOptions 	= travelOptions;
        this.result         = result;
        this.points         = new HashMap<>();
        this.poiType        = poiType;
        parseResults();
        this.requestEnd     =  System.currentTimeMillis() - requestStart;
    }

    /**
     * Responses without a given PoI type will use the first one found in travel options as before for backwards
     * compatibility
     */
    public OverpassResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {
        this(travelOptions, result, requestStart, travelOptions.getOsmTypes().iterator().next());
    }

    /**
     * Parse results in JSON to travel times map.
     */
    public void parseResults() {

        final JSONArray elements = JsonUtil.getJsonArray(this.result, "elements");
        String type = this.poiType.toString();

        for (int i = 0; i < elements.length(); i++) {

            JSONObject element = JsonUtil.getJSONObject(elements, i);
            Coordinate coordinate;

            if ( "node".equals(JsonUtil.getString(element, "type")) ) {

                coordinate = new PoiTargetCoordinate(
                        JsonUtil.getString(element, "id") + type ,
                        JsonUtil.getDouble(element, "lon"),
                        JsonUtil.getDouble(element, "lat"));
            }
            else if ( "way".equals(JsonUtil.getString(element, "type")) && element.has(CENTER) ) {

                coordinate = new PoiTargetCoordinate(
                        JsonUtil.getString(element, "id") + type,
                        JsonUtil.getDouble(JsonUtil.getJSONObject(element, CENTER), "lon"),
                        JsonUtil.getDouble(JsonUtil.getJSONObject(element, CENTER), "lat"));
            }
            else {

                throw new TargomoClientRuntimeException("only way and node are supported, given: "
                        + JsonUtil.getString(element, "type"));
            }

            ((PoiTargetCoordinate) coordinate).setType(getType(element));

            this.points.put(coordinate.getId(), coordinate);
        }
    }

    /**
     * Check if the poiType of the element is the selected poiType or in the list of OSM types
     * in travel options
     * @param element PoI element
     * @return PoiType of the element or exception if unsupported type
     */
    private PoiType getType(JSONObject element) {

        final JSONObject tags = JsonUtil.getJSONObject(element, "tags");

        List<String> keys = travelOptions.getOsmTypes().stream().map(PoiType::getKey).collect(Collectors.toList());
        keys.add(poiType.getKey());

        for ( String key : keys )  {

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

    public long getRequestEnd() {
        return requestEnd;
    }
}

package net.motionintelligence.client.api.response.refactored;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class PolygonResponse extends DefaultResponse<JSONArray,List<Map<String,?>>> {

    @Override
    protected JSONArray parseData(List<Map<String,?>> jacksonData) {
        return new JSONArray(jacksonData);
    }
}

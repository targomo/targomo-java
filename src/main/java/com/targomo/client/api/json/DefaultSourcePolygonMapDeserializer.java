package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.geo.DefaultSourcePolygon;
import com.targomo.client.api.geo.Polygon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultSourcePolygonMapDeserializer extends JsonDeserializer<Map<String, Polygon>> {
    private ObjectMapper mapper = new ObjectMapper();

    public DefaultSourcePolygonMapDeserializer() {}

    @Override
    public Map<String, Polygon> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws JsonProcessingException, IOException {

        JsonNode coordinatesArray = jsonParser.getCodec().readTree(jsonParser);

        Map<String,Polygon> polygons = new HashMap<>();

        for (JsonNode coordinateNode : coordinatesArray) {
            polygons.put(coordinateNode.get("id").asText(),
                    mapper.readValue(coordinateNode.toString(), DefaultSourcePolygon.class));
        }

        return polygons;
    }
}

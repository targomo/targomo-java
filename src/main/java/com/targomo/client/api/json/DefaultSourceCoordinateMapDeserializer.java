package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gerb on 01/02/2017.
 */

public class DefaultSourceCoordinateMapDeserializer extends JsonDeserializer<Map<String, Coordinate>> {

    private ObjectMapper mapper = new ObjectMapper();

    public DefaultSourceCoordinateMapDeserializer() {}

    @Override
    public Map<String, Coordinate> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode coordinatesArray = jsonParser.getCodec().readTree(jsonParser);

        Map<String,Coordinate> coordinates = new HashMap<>();

        for (JsonNode coordinateNode : coordinatesArray) {
            coordinates.put(coordinateNode.get("id").asText(),
                mapper.readValue(coordinateNode.toString(), DefaultSourceCoordinate.class));
        }

        return coordinates;
    }
}

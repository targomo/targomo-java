package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.geo.AbstractGeometry;
import com.targomo.client.api.geo.DefaultSourceGeometry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gideon
 */
public class DefaultSourceGeometriesMapDeserializer extends JsonDeserializer<Map<String, AbstractGeometry>> {
    private ObjectMapper mapper = new ObjectMapper();

    public DefaultSourceGeometriesMapDeserializer() {}

    @Override
    public Map<String, AbstractGeometry> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode polygonsArray = jsonParser.getCodec().readTree(jsonParser);

        Map<String, AbstractGeometry> polygons = new HashMap<>();

        for (JsonNode polygonNode : polygonsArray) {
            polygons.put(polygonNode.get("id").asText(),
                    mapper.readValue(polygonNode.toString(), DefaultSourceGeometry.class));
        }

        return polygons;
    }
}

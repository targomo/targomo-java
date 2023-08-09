package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.geo.Location;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gideon Cohen
 */

public abstract class AbstractLocationMapDeserializer<K extends Location> extends JsonDeserializer<Map<String, K>> {

    private ObjectMapper mapper = new ObjectMapper();
    
    public Map<String, K> deserialize(String key, JsonParser jsonParser, Class<K> deserializationClass)
            throws IOException {
        
        JsonNode locationArray = jsonParser.getCodec().readTree(jsonParser);

        Map<String,K> locations = new HashMap<>();

        for (JsonNode coordinateNode : locationArray) {
            locations.put(coordinateNode.get(key).asText(),
                mapper.readValue(coordinateNode.toString(), deserializationClass));
        }

        return locations;
    }
}

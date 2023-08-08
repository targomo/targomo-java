package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceAddress;
import com.targomo.client.api.geo.DefaultSourceCoordinate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gerb on 01/02/2017.
 */

public class DefaultSourceAddressMapDeserializer extends JsonDeserializer<Map<String, DefaultSourceAddress>> {

    private ObjectMapper mapper = new ObjectMapper();

    public DefaultSourceAddressMapDeserializer() {}

    @Override
    public Map<String, DefaultSourceAddress> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode coordinatesArray = jsonParser.getCodec().readTree(jsonParser);

        Map<String,DefaultSourceAddress> addresses = new HashMap<>();

        for (JsonNode coordinateNode : coordinatesArray) {
            addresses.put(coordinateNode.get("h3Address").asText(),
                mapper.readValue(coordinateNode.toString(), DefaultSourceAddress.class));
        }

        return addresses;
    }
}

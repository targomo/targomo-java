package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.geo.DefaultTargetCoordinate;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gerb on 01/02/2017.
 */

public class DefaultTargetCoordinateMapDeserializer extends AbstractLocationMapDeserializer<DefaultTargetCoordinate> {

    private ObjectMapper mapper = new ObjectMapper();

    public DefaultTargetCoordinateMapDeserializer() {}

    @Override
    public Map<String, DefaultTargetCoordinate> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return deserialize("id", jsonParser, DefaultTargetCoordinate.class);
    }
}

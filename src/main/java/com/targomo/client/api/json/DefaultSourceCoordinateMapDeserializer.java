package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.targomo.client.api.geo.DefaultSourceCoordinate;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gerb on 01/02/2017.
 */

public class DefaultSourceCoordinateMapDeserializer extends AbstractLocationMapDeserializer<DefaultSourceCoordinate> {

    public DefaultSourceCoordinateMapDeserializer() {}

    @Override
    public Map<String, DefaultSourceCoordinate> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return deserialize("id", jsonParser, DefaultSourceCoordinate.class);
    }
}

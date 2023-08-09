package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.targomo.client.api.geo.DefaultSourceGeometry;

import java.io.IOException;
import java.util.Map;

/**
 * @author gideon
 */
public class DefaultSourceGeometriesMapDeserializer extends AbstractLocationMapDeserializer<DefaultSourceGeometry> {
    public DefaultSourceGeometriesMapDeserializer() {}

    @Override
    public Map<String, DefaultSourceGeometry> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return deserialize("id", jsonParser, DefaultSourceGeometry.class);
    }
}

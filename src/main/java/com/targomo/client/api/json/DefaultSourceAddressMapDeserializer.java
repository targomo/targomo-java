package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.targomo.client.api.geo.DefaultSourceAddress;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Gideon Cohen
 */

public class DefaultSourceAddressMapDeserializer extends AbstractLocationMapDeserializer<DefaultSourceAddress> {

    public DefaultSourceAddressMapDeserializer() {}

    @Override
    public Map<String, DefaultSourceAddress> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return deserialize("h3Address", jsonParser, DefaultSourceAddress.class);
    }
}

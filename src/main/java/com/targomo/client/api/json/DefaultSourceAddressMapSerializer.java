package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.targomo.client.api.geo.DefaultSourceAddress;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Gideon Cohen
 */
public class DefaultSourceAddressMapSerializer extends AbstractSourceMapSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();

        for ( Map.Entry<String, DefaultSourceAddress> entry : ((Map<String, DefaultSourceAddress>) o).entrySet())  {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("h3Address", entry.getKey());
            writeExtraData(entry.getValue(), jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray(); // ]
    }
}

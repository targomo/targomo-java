package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.targomo.client.api.geo.DefaultSourceCoordinate;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gerb on 01/03/2017.
 */
public class DefaultSourceCoordinateMapSerializer extends AbstractSourceMapSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray(); // [

        for ( Map.Entry<String, DefaultSourceCoordinate> entry : ((Map<String, DefaultSourceCoordinate>) o).entrySet())  {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", entry.getKey());
            jsonGenerator.writeNumberField("y", entry.getValue().getY());
            jsonGenerator.writeNumberField("x", entry.getValue().getX());
            writeExtraData(entry.getValue(), jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray(); // ]
    }
}

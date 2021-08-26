package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.targomo.client.api.geo.DefaultTargetCoordinate;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gerb on 01/03/2017.
 */
public class DefaultTargetCoordinateMapSerializer extends JsonSerializer {

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartArray(); // [

        for ( Map.Entry<String, DefaultTargetCoordinate> entry : ((Map<String, DefaultTargetCoordinate>) o).entrySet())  {

            jsonGenerator.writeStartObject(); // {
            jsonGenerator.writeStringField("id", entry.getKey());
            jsonGenerator.writeNumberField("y", entry.getValue().getY());
            jsonGenerator.writeNumberField("x", entry.getValue().getX());
            jsonGenerator.writeEndObject(); // }
        }

        jsonGenerator.writeEndArray(); // ]
    }
}

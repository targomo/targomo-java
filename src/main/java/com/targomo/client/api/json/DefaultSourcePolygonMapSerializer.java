package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.targomo.client.api.geo.DefaultSourcePolygon;

import java.io.IOException;
import java.util.Map;

public class DefaultSourcePolygonMapSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartArray(); // [

        for ( Map.Entry<String, DefaultSourcePolygon> entry : ((Map<String, DefaultSourcePolygon>) o).entrySet())  {

            jsonGenerator.writeStartObject(); // {
            jsonGenerator.writeStringField("id", entry.getKey());
            if ( entry.getValue().getTravelType() != null ) jsonGenerator.writeStringField("tm", entry.getValue().getTravelType().toString());
            jsonGenerator.writeStringField("geojson", entry.getValue().getGeojson());
            jsonGenerator.writeNumberField("crs", entry.getValue().getCrs());
            jsonGenerator.writeEndObject(); // {
        }

        jsonGenerator.writeEndArray(); // ]
    }
}

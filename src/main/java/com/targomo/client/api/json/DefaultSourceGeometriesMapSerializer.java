package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.targomo.client.api.geo.DefaultSourceGeometry;

import java.io.IOException;
import java.util.Map;

/**
 * @author gideon
 */
public class DefaultSourceGeometriesMapSerializer extends AbstractSourceMapSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray(); // [

        for ( Map.Entry<String, DefaultSourceGeometry> entry : ((Map<String, DefaultSourceGeometry>) o).entrySet())  {

            jsonGenerator.writeStartObject(); // {
            jsonGenerator.writeStringField("id", entry.getKey());
            jsonGenerator.writeStringField("data", entry.getValue().getData());
            jsonGenerator.writeNumberField("crs", entry.getValue().getCrs());
            jsonGenerator.writeBooleanField("routeFromCentroid", entry.getValue().isRouteFromCentroid());
            writeExtraData(entry.getValue(), jsonGenerator);
            jsonGenerator.writeEndObject(); // }
        }

        jsonGenerator.writeEndArray(); // ]
    }
}

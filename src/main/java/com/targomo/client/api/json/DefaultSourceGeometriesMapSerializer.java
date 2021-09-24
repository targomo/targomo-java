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
public class DefaultSourceGeometriesMapSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray(); // [

        for ( Map.Entry<String, DefaultSourceGeometry> entry : ((Map<String, DefaultSourceGeometry>) o).entrySet())  {

            jsonGenerator.writeStartObject(); // {
            jsonGenerator.writeStringField("id", entry.getKey());
            if ( entry.getValue().getTravelType() != null ) jsonGenerator.writeStringField("tm", entry.getValue().getTravelType().toString());
            jsonGenerator.writeStringField("data", entry.getValue().getData());
            jsonGenerator.writeNumberField("crs", entry.getValue().getCrs());
            jsonGenerator.writeBooleanField("routeFromCentroid", entry.getValue().isRouteFromCentroid());
            if( entry.getValue().getProperties() != null){
                jsonGenerator.writeFieldName("aggregationInputParameters");
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("inputFactor", entry.getValue().getProperties().getInputFactor());
                jsonGenerator.writeNumberField("gravitationAttractionStrength", entry.getValue().getProperties().getGravitationAttractionStrength());
                jsonGenerator.writeBooleanField("gravitationPositiveInfluence", entry.getValue().getProperties().getGravitationPositiveInfluence());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndObject(); // }
        }

        jsonGenerator.writeEndArray(); // ]
    }
}

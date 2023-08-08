package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.targomo.client.api.geo.DefaultSourceAddress;
import com.targomo.client.api.geo.DefaultSourceCoordinate;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gerb on 01/03/2017.
 */
public class DefaultSourceAddressMapSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray(); // [

        for ( Map.Entry<String, DefaultSourceAddress> entry : ((Map<String, DefaultSourceAddress>) o).entrySet())  {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("h3Address", entry.getKey());
            if ( entry.getValue().getTravelType() != null ) jsonGenerator.writeStringField("tm", entry.getValue().getTravelType().toString());
            if( entry.getValue().getProperties() != null){
                jsonGenerator.writeFieldName("aggregationInputParameters");
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("inputFactor", entry.getValue().getProperties().getInputFactor());
                jsonGenerator.writeNumberField("gravitationAttractionStrength", entry.getValue().getProperties().getGravitationAttractionStrength());
                jsonGenerator.writeBooleanField("gravitationPositiveInfluence", entry.getValue().getProperties().getGravitationPositiveInfluence());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray(); // ]
    }
}

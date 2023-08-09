package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.targomo.client.api.geo.Location;

import java.io.IOException;

/**
 * Created by Gideon Cohen
 */
public abstract class AbstractSourceMapSerializer extends JsonSerializer {


    public void writeExtraData(Location location, JsonGenerator jsonGenerator) throws IOException {
        if ( location.getTravelType() != null ) jsonGenerator.writeStringField("tm", location.getTravelType().toString());
        if( location.getProperties() != null){
            jsonGenerator.writeFieldName("aggregationInputParameters");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("inputFactor", location.getProperties().getInputFactor());
            jsonGenerator.writeNumberField("gravitationAttractionStrength", location.getProperties().getGravitationAttractionStrength());
            jsonGenerator.writeBooleanField("gravitationPositiveInfluence", location.getProperties().getGravitationPositiveInfluence());
            jsonGenerator.writeEndObject();
        }
    }
}

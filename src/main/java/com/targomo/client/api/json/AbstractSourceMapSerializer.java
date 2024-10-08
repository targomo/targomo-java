package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.targomo.client.api.geo.Location;

import java.io.IOException;

import static com.targomo.client.api.util.SerializationUtil.travelTypeListToJsonGenerator;

/**
 * Created by Gideon Cohen
 */
public abstract class AbstractSourceMapSerializer extends JsonSerializer {


    public void writeExtraData(Location location, JsonGenerator jsonGenerator) throws IOException {

        travelTypeListToJsonGenerator(location.getTravelTypes(), jsonGenerator, "travelTypes", "travelType");

        if( location.getProperties() != null) {
            jsonGenerator.writeFieldName("aggregationInputParameters");
            jsonGenerator.writeStartObject();
            if (location.getProperties().getInputFactor() != null) {
                jsonGenerator.writeNumberField("inputFactor", location.getProperties().getInputFactor());
            }
            if (location.getProperties().getGravitationAttractionStrength() != null) {
                jsonGenerator.writeNumberField("gravitationAttractionStrength", location.getProperties().getGravitationAttractionStrength());
            }
            if (location.getProperties().getGravitationPositiveInfluence() != null) {
                jsonGenerator.writeBooleanField("gravitationPositiveInfluence", location.getProperties().getGravitationPositiveInfluence());
            }
            jsonGenerator.writeEndObject();
        }
    }
}

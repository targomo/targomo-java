package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.Location;
import com.targomo.client.api.statistic.PoiType;

import java.io.IOException;
import java.util.List;

import static com.targomo.client.api.util.SerializationUtil.travelTypeListToJsonGenerator;

/**
 * Created by Gideon Cohen
 */
public abstract class AbstractSourceMapSerializer extends JsonSerializer {


    public void writeExtraData(Location location, JsonGenerator jsonGenerator) throws IOException {

        travelTypeListToJsonGenerator(location.getTravelTypes(), jsonGenerator, "travelTypes", "travelType");

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

package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.targomo.client.api.pojo.TransitTravelTimeValue;

import java.io.IOException;

public class TransitTravelTimeValueDeserializer extends JsonDeserializer<TransitTravelTimeValue> {

    public TransitTravelTimeValueDeserializer() {}

    @Override
    public TransitTravelTimeValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode arrayValues = jsonParser.getCodec().readTree(jsonParser);

        return TransitTravelTimeValue.builder()
                .departure(arrayValues.get(0).asLong())
                .arrival(arrayValues.get(1).asLong())
                .build();
    }
}

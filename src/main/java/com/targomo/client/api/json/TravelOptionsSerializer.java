package com.targomo.client.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;

import java.io.IOException;

public class TravelOptionsSerializer extends StdSerializer<TravelOptions> {

    public TravelOptionsSerializer() {
        super((Class<TravelOptions>)null);
    }

    @Override
    public void serialize(TravelOptions travelOptions, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            gen.writeRawValue(RequestConfigurator.getConfig(travelOptions));
        } catch (TargomoClientException e) {
            throw new IOException("Failed to serialize TravelOptions object.", e);
        }
    }
}
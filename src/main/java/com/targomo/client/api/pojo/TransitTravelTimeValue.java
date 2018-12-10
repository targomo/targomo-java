package com.targomo.client.api.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.targomo.client.api.json.TransitTravelTimeValueDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@JsonDeserialize(using=TransitTravelTimeValueDeserializer.class)
public class TransitTravelTimeValue {

    @Getter @Setter
    private long departure;

    @Getter @Setter
    private long arrival;
}

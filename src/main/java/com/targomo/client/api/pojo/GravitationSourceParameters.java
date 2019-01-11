package com.targomo.client.api.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class GravitationSourceParameters {

    private boolean positiveInfluence = false;
    private double attractionStrength = 1.0;
}

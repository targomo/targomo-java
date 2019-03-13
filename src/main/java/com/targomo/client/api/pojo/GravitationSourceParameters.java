package com.targomo.client.api.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(includeFieldNames=true)
public class GravitationSourceParameters {

    private boolean positiveInfluence = false;
    private double attractionStrength = 1.0;
}

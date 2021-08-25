package com.targomo.client.api.pojo;

import lombok.*;


// Only relevant for POI service's gravitation requests
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString(includeFieldNames=true)
public class LocationProperties {
    private Float inputFactor;
    private Boolean gravitationPositiveInfluence;
    private Double gravitationAttractionStrength;
}

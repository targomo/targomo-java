package com.targomo.client.api.pojo;

import lombok.*;


// Only relevant for POI service's gravitation requests (and quality requests)
// gravitationAttractionStrength is the only attribute used by quality requests, therefore the constructor with only that
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

    public LocationProperties(Double gravitationAttractionStrength) {
        this.gravitationAttractionStrength = gravitationAttractionStrength;
    }
}

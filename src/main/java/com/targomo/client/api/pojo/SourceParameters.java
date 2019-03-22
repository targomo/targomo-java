package com.targomo.client.api.pojo;

import lombok.*;

@Getter()
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString(includeFieldNames=true)
public class SourceParameters {
    private Float inputFactor;
    private Boolean gravitationPositiveInfluence;
    private Double gravitationAttractionStrength;

    public Float getInputFactor(){
        return inputFactor == null ? 1.0f : inputFactor;
    }
}

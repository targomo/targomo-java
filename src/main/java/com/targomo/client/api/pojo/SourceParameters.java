package com.targomo.client.api.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(includeFieldNames=true)
public class SourceParameters {

    private Float inputFactor;
    private Boolean gravitationPositiveInfluence;
    private Double gravitationAttractionStrength;
}

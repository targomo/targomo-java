package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.quality.PublicLocation;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = PoiGravitationCriterionDefinition.PoiGravitationCriterionDefinitionBuilderImpl.class)
public class PoiGravitationCriterionDefinition extends PoiReachabilityCriterionDefinition implements GravitationCriterionDefinition {

    @Setter
    private List<PublicLocation> competitors;

    @Setter
    private Double gravitationExponent;

    @Setter
    private Double probabilityDecay;

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        if(!isScore){
            throw new IllegalArgumentException("'PoiGravitationCriterionDefinition' can't be used in a rating");
        }
        return FineGrainedRequestTypeEnum.QUALITY_SCORE_POI_GRAVITATION;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class PoiGravitationCriterionDefinitionBuilderImpl extends PoiGravitationCriterionDefinition.PoiGravitationCriterionDefinitionBuilder {
    }
}

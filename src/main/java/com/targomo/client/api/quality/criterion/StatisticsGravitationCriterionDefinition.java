package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.quality.Location;
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
@JsonDeserialize(builder = StatisticsGravitationCriterionDefinition.StatisticsGravitationCriterionDefinitionBuilderImpl.class)
public class StatisticsGravitationCriterionDefinition extends StatisticsReachabilityCriterionDefinition implements GravitationCriterionDefinition {

    @Setter
    private List<Location> competitors;

    @Setter
    private Double gravitationExponent;

    @Setter
    private Double probabilityDecay;

    private final Boolean calculateGravitationPerReferenceId;

    private final Float routingLowerBoundValue;

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        if(!isScore){
            throw new IllegalArgumentException("'StatisticsGravitationCriterionDefinition' can't be used in a rating");
        }
        return FineGrainedRequestTypeEnum.QUALITY_SCORE_STATISTICS_GRAVITATION;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class StatisticsGravitationCriterionDefinitionBuilderImpl extends StatisticsGravitationCriterionDefinition.StatisticsGravitationCriterionDefinitionBuilder {
    }
}

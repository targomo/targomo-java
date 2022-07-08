package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = TransitStopsCriterionDefinition.TransitStopsCriterionDefinitionBuilderImpl.class)
public class TransitStopsCriterionDefinition extends RoutingBasedCriterionDefinition {

    private final Integer startTime;
    private final Integer endTime;
    private final Integer referenceInterval;

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        return isScore ? FineGrainedRequestTypeEnum.QUALITY_SCORE_TRANSIT_STOPS : FineGrainedRequestTypeEnum.QUALITY_RATING_TRANSIT_STOPS;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransitStopsCriterionDefinitionBuilderImpl extends TransitStopsCriterionDefinition.TransitStopsCriterionDefinitionBuilder {
    }
}

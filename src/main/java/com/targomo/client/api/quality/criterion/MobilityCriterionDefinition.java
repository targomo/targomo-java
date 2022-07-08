package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.core.enums.EdgeWeightType;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import com.targomo.core.util.logging.model.RequestAttributes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = MobilityCriterionDefinition.MobilityCriterionDefinitionBuilderImpl.class)
public class MobilityCriterionDefinition extends CriterionDefinition {

    @Setter
    @NotNull
    private String mobilityServiceUrl;

    private final Integer minDuration;
    private final Integer maxDuration;

    private final Integer hourStart;
    private final Integer hourEnd;

    private final Integer dayStart;
    private final Integer dayEnd;

    private final Integer dayOfYearStart;
    private final Integer dayOfYearEnd;

    private final Boolean unique;
    private final Boolean exact;
    private final Boolean excludeNightLocations;

    private final Integer radius;

    @Override
    public RequestAttributes.RequestAttributesBuilder getRequestAttributesBuilder(boolean isScore) {
        return RequestAttributes.builder()
                .reqType(getRequestType(isScore))
                // radius is always a distance in m
                .edgeWeight(EdgeWeightType.DISTANCE)
                .maxEdgeWeight(this.radius);
    }

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        return isScore ? FineGrainedRequestTypeEnum.QUALITY_SCORE_MOBILITY : FineGrainedRequestTypeEnum.QUALITY_RATING_MOBILITY;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class MobilityCriterionDefinitionBuilderImpl extends MobilityCriterionDefinition.MobilityCriterionDefinitionBuilder {
    }
}

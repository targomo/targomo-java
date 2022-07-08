package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.enums.EdgeStatisticDirection;
import com.targomo.client.api.enums.TravelType;
import com.targomo.core.enums.EdgeWeightType;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import com.targomo.core.util.logging.model.RequestAttributes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = EdgeStatisticsCriterionDefinition.EdgeStatisticsCriterionDefinitionBuilderImpl.class)
public class EdgeStatisticsCriterionDefinition extends CriterionDefinition {

    @Setter
    @NotNull
    private String edgeStatisticsServiceUrl;

    @NotEmpty
    private final Integer edgeStatisticGroupId;
    @NotEmpty
    private final Integer edgeStatisticId;
    
    private final Integer radius;
    private final List<Integer> radii;

    private final TravelType travelType;

    private final EdgeStatisticDirection direction;

    private final List<Integer> ignoreRoadClasses;

    @Override
    public RequestAttributes.RequestAttributesBuilder getRequestAttributesBuilder(boolean isScore) {
        return RequestAttributes.builder()
                .reqType(getRequestType(isScore))
                // radius (or radii) is always a distance in m
                .edgeWeight(EdgeWeightType.DISTANCE)
                .maxEdgeWeight(this.radius == null ? this.radii.stream().mapToInt(Integer::intValue).max().orElse(0) : this.radius);
    }

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        return isScore ? FineGrainedRequestTypeEnum.QUALITY_SCORE_EDGE_STATISTICS : FineGrainedRequestTypeEnum.QUALITY_RATING_EDGE_STATISTICS;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class EdgeStatisticsCriterionDefinitionBuilderImpl extends EdgeStatisticsCriterionDefinitionBuilder {
    }
}

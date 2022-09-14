package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.enums.EdgeStatisticDirection;
import com.targomo.client.api.enums.TravelType;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    @JsonPOJOBuilder(withPrefix="")
    public static class EdgeStatisticsCriterionDefinitionBuilderImpl extends EdgeStatisticsCriterionDefinitionBuilder {
    }
}

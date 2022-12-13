package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.targomo.client.api.enums.EdgeStatisticDirection;
import com.targomo.client.api.enums.TravelType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EdgeStatisticsCriterionDefinition extends CriterionDefinition {

    @Setter
    @NotNull
    private String edgeStatisticsServiceUrl;

    /**
     * @deprecated Use rather edgeStatisticCollectionId
     */
    @Deprecated
    private final Integer edgeStatisticGroupId;

    /**
     * @deprecated Use rather edgeStatisticIds
     */
    @Deprecated
    private final Integer edgeStatisticId;

    private final Integer edgeStatisticCollectionId;
    private final List<Integer> edgeStatisticIds;

    private final Integer radius;
    private final List<Integer> radii;

    private final TravelType travelType;

    private final EdgeStatisticDirection direction;

    private final List<Integer> ignoreRoadClasses;

}

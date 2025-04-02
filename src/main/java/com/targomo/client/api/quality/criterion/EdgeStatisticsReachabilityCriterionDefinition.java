package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.targomo.client.api.enums.EdgeStatisticAggregationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EdgeStatisticsReachabilityCriterionDefinition extends RoutingBasedCriterionDefinition {

    @Setter
    private String edgeStatisticsServiceUrl;

    private final Integer edgeStatisticCollectionId;

    private final EdgeStatisticAggregationType aggregationType;

    // Use either edgeStatisticIds or edgeStatisticId
    private final List<Integer> edgeStatisticIds;
    private final Integer edgeStatisticId;

    // If true, reachability will be calculated even when using fly mode and only reachable edges considered
    private final boolean calculateReachabilityInFlyMode;

    private final List<Integer> excludeEdgeClassesFromRouting;
}

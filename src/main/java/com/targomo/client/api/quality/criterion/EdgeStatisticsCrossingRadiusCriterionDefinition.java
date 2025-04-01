package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
public class EdgeStatisticsCrossingRadiusCriterionDefinition extends EdgeStatisticsReachabilityCriterionDefinition {

    private final Integer radius;
    private final List<Integer> radii;

    private final boolean ignoreReachability;

    private final List<Integer> excludeEdgeClassesFromRouting;
}

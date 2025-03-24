package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.targomo.client.api.enums.EdgeStatisticAggregationType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractEdgeStatisticsCrossingRadiusOptions {

    Set<Integer> edgeStatisticIds = new HashSet<>();

    // Map of aggregations id to a list of statistics ids to aggregate
    private Map<String, List<Integer>> aggregateEdgeStatisticIds = new HashMap<>();

    // Type of aggregation
    private EdgeStatisticAggregationType aggregationType = EdgeStatisticAggregationType.SUM;

    private Integer radius = 100;

    private List<Integer> ignoreRoadClasses = new ArrayList<>();

    private boolean ignoreReachability = false;
}

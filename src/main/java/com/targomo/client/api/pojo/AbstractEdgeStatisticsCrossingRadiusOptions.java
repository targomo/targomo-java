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

    Set<Integer> edgeStatisticIds;

    // Map of aggregations id to a list of statistics ids to aggregate
    private Map<String, List<Integer>> aggregateEdgeStatisticIds;

    // How the edge statistics in aggregateEdgeStatisticIds should be aggregated
    private EdgeStatisticAggregationType aggregationType;

    // Radius in meters
    private Integer radius;

    // Road classes that will be ignored
    private List<Integer> ignoreRoadClasses;

    // If true no reachability will be calculated, otherwise a routing from the location will be performed and only reachable edges considered
    private boolean ignoreReachability;
}

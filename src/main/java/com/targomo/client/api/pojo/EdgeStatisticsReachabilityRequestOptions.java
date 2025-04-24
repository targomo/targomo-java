package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeStatisticAggregationType;
import com.targomo.client.api.json.TravelOptionsSerializer;
import lombok.*;

import java.util.*;

/**
 * Parameters for an edge statistics reachability request.
 */
@Getter @Setter
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdgeStatisticsReachabilityRequestOptions {

    Set<Integer> edgeStatisticIds = new HashSet<>();

    // Map of aggregations id to a list of statistics ids to aggregate
    private Map<String, List<Integer>> aggregateEdgeStatisticIds = new HashMap<>();

    // Type of aggregation
    private EdgeStatisticAggregationType aggregationType = EdgeStatisticAggregationType.SUM;

    // Road classes that will be ignored
    private List<Integer> ignoreRoadClasses = new ArrayList<>();

    // If true, reachability will be calculated even when using fly mode and only reachable edges considered
    private boolean calculateReachabilityInFlyMode;

    @JsonSerialize(using = TravelOptionsSerializer.class)
    TravelOptions routingOptions;
}

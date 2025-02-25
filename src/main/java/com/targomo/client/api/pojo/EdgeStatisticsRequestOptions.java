package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeStatisticAggregationType;
import com.targomo.client.api.enums.EdgeStatisticCalculationType;
import com.targomo.client.api.enums.EdgeStatisticDirection;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import com.targomo.client.api.json.TravelOptionsSerializer;
import lombok.*;

import java.util.*;

/**
 * Parameters for an edge statistics location request.
 */
@Getter @Setter
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdgeStatisticsRequestOptions {

    private EdgeStatisticCalculationType calculationType = EdgeStatisticCalculationType.MAX;

    private Set<Integer> edgeStatisticIds = new HashSet<>();

    // Map of aggregations id to a list of statistics ids to aggregate
    private Map<String, List<Integer>> aggregateEdgeStatisticIds = new HashMap<>();

    // Type of aggregation
    private EdgeStatisticAggregationType aggregationType = EdgeStatisticAggregationType.SUM;

    private Integer radius = 100;
    private TravelType travelType = TravelType.CAR;
    private EdgeStatisticDirection direction = EdgeStatisticDirection.ANY;
    private List<Integer> ignoreRoadClasses = new ArrayList<>();

    @JsonIgnoreProperties({"travelType", "properties", "travelTypes"})
    private List<DefaultTargetCoordinate> locations = new ArrayList<>();
}

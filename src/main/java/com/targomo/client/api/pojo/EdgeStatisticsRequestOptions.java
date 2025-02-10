package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.targomo.client.api.enums.EdgeStatisticAggregationType;
import com.targomo.client.api.enums.EdgeStatisticDirection;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parameters for an edge statistics location request.
 */
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EdgeStatisticsRequestOptions {

    private List<Integer> edgeStatisticIds = new ArrayList<>();

    private Map<String, List<Integer>> aggregateEdgeStatisticIds = new HashMap<>();

    private EdgeStatisticAggregationType aggregationType = EdgeStatisticAggregationType.SUM;

    private Integer radius = 100;
    private TravelType travelType = TravelType.CAR;
    private EdgeStatisticDirection direction = EdgeStatisticDirection.ANY;
    private List<Integer> ignoreRoadClasses = new ArrayList<>();

    @JsonIgnoreProperties({"travelType", "properties", "travelTypes"})
    private List<DefaultTargetCoordinate> locations = new ArrayList<>();
}

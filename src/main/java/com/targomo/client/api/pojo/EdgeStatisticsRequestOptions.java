package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.targomo.client.api.enums.EdgeStatisticDirection;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parameters for an edge statistics request.
 */
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EdgeStatisticsRequestOptions {

    private List<Integer> edgeStatisticIds = new ArrayList<>();

    private Integer radius = 100;
    private TravelType travelType = TravelType.CAR;
    private EdgeStatisticDirection direction = EdgeStatisticDirection.ANY;
    private List<Integer> ignoreRoadClasses = new ArrayList<>();

    @JsonIgnoreProperties({"travelType", "properties"})
    private List<DefaultTargetCoordinate> locations = new ArrayList<>();
}

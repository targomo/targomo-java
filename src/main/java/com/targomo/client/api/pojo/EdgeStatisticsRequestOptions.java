package com.targomo.client.api.pojo;

import com.targomo.client.api.enums.EdgeStatisticDirection;
import com.targomo.client.api.enums.TravelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Parameters for an edge statistics request.
 */
@Getter @AllArgsConstructor
public class EdgeStatisticsRequestOptions {

    private final String serviceUrl;
    private final String serviceKey;

    private final Integer edgeStatisticGroupId;
    private final Integer edgeStatisticId;

    private final Integer radius;
    private final TravelType travelType;
    private final EdgeStatisticDirection direction;
    private final List<Integer> ignoreRoadClasses;
}

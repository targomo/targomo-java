package com.targomo.client.api.quality.criterion;

import com.targomo.client.Constants;

public enum SourceType {
    POI(Constants.SOURCE_TYPE_POI),
    POI_IN_ZONE(Constants.SOURCE_TYPE_POI_IN_ZONE),
    POI_GRAVITATION(Constants.SOURCE_TYPE_POI_GRAVITATION),
    STATISTICS(Constants.SOURCE_TYPE_STATISTICS),
    STATISTICS_IN_ZONE(Constants.SOURCE_TYPE_STATISTICS_IN_ZONE),
    STATISTICS_GRAVITATION(Constants.SOURCE_TYPE_STATISTICS_GRAVITATION),
    EDGE_STATISTICS(Constants.SOURCE_TYPE_EDGE_STATISTICS),
    MATH_AGGREGATION(Constants.SOURCE_TYPE_MATH_AGGREGATION),
    MOBILITY(Constants.SOURCE_TYPE_MOBILITY),
    TRANSIT_STOPS(Constants.SOURCE_TYPE_TRANSIT_STOPS);

    private final String name;

    SourceType(String name) {
        this.name = name;
    }
}

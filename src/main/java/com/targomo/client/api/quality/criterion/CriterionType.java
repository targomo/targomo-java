package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;
import com.targomo.core.exception.RequestConfigurationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CriterionType {
    POI_COVERAGE_COUNT(Constants.CRITERION_TYPE_POI_COVERAGE_COUNT),
    POI_COVERAGE_DISTANCE(Constants.CRITERION_TYPE_POI_COVERAGE_DISTANCE),
    CLOSEST_POI_DISTANCE(Constants.CRITERION_TYPE_CLOSEST_POI_DISTANCE),
    POI_COUNT_IN_ZONE(Constants.CRITERION_TYPE_POI_COUNT_IN_ZONE),
    STATISTICS_SUM(Constants.CRITERION_TYPE_STATISTICS_SUM),
    STATISTICS_DISTANCE(Constants.CRITERION_TYPE_STATISTICS_DISTANCE),
    STATISTICS_GRAVITATION_SUM(Constants.CRITERION_TYPE_GRAVITATION_SUM),
    STATISTICS_SUM_IN_ZONE(Constants.CRITERION_TYPE_STATISTICS_SUM_IN_ZONE),
    POI_GRAVITATION_SUM(Constants.CRITERION_TYPE_POI_GRAVITATION_SUM),
    EDGE_STATISTICS(Constants.CRITERION_TYPE_EDGE_STATISTICS),
    MATH_AGGREGATION(Constants.CRITERION_TYPE_MATH_AGGREGATION),
    STAYPOINT_COUNT(Constants.CRITERION_TYPE_STAYPOINT_COUNT),
    TRANSIT_STOPS_SUM(Constants.CRITERION_TYPE_TRANSIT_STOPS_SUM),
    TRANSIT_STOPS_DISTANCE(Constants.CRITERION_TYPE_TRANSIT_STOPS_DISTANCE),
    CRITERION_REFERENCE(Constants.CRITERION_TYPE_REFERENCE);

    private final String name;

    @JsonValue
    public String getName() {
        return this.name;
    }
}

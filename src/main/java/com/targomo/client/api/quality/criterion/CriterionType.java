package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;
import com.targomo.core.exception.RequestConfigurationException;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum CriterionType {
    POI_COVERAGE_COUNT(Constants.CRITERION_TYPE_POI_COVERAGE_COUNT, SourceType.POI, DistanceInfluence.NONE, EntityAggregation.SUM),
    POI_COVERAGE_DISTANCE(Constants.CRITERION_TYPE_POI_COVERAGE_DISTANCE, SourceType.POI, DistanceInfluence.QUADRATIC, EntityAggregation.SUM),
    CLOSEST_POI_DISTANCE(Constants.CRITERION_TYPE_CLOSEST_POI_DISTANCE, SourceType.POI, DistanceInfluence.QUADRATIC, EntityAggregation.MIN),
    POI_COUNT_IN_ZONE(Constants.CRITERION_TYPE_POI_COUNT_IN_ZONE, SourceType.POI_IN_ZONE, DistanceInfluence.NONE, EntityAggregation.SUM),
    STATISTICS_SUM(Constants.CRITERION_TYPE_STATISTICS_SUM, SourceType.STATISTICS, DistanceInfluence.NONE, EntityAggregation.SUM),
    STATISTICS_DISTANCE(Constants.CRITERION_TYPE_STATISTICS_DISTANCE, SourceType.STATISTICS, DistanceInfluence.QUADRATIC, EntityAggregation.SUM),
    STATISTICS_GRAVITATION_SUM(Constants.CRITERION_TYPE_GRAVITATION_SUM, SourceType.STATISTICS_GRAVITATION, DistanceInfluence.NONE, EntityAggregation.SUM),
    STATISTICS_SUM_IN_ZONE(Constants.CRITERION_TYPE_STATISTICS_SUM_IN_ZONE, SourceType.STATISTICS_IN_ZONE, DistanceInfluence.NONE, EntityAggregation.SUM),
    POI_GRAVITATION_SUM(Constants.CRITERION_TYPE_POI_GRAVITATION_SUM, SourceType.POI_GRAVITATION, DistanceInfluence.NONE, EntityAggregation.SUM),
    EDGE_STATISTICS(Constants.CRITERION_TYPE_EDGE_STATISTICS, SourceType.EDGE_STATISTICS, DistanceInfluence.NONE, EntityAggregation.MEAN),
    MATH_AGGREGATION(Constants.CRITERION_TYPE_MATH_AGGREGATION, SourceType.MATH_AGGREGATION, DistanceInfluence.NONE, EntityAggregation.SUM),
    STAYPOINT_COUNT(Constants.CRITERION_TYPE_STAYPOINT_COUNT, SourceType.MOBILITY, DistanceInfluence.NONE, EntityAggregation.SUM),
    TRANSIT_STOPS_SUM(Constants.CRITERION_TYPE_TRANSIT_STOPS_SUM, SourceType.TRANSIT_STOPS, DistanceInfluence.NONE, EntityAggregation.SUM),
    TRANSIT_STOPS_DISTANCE(Constants.CRITERION_TYPE_TRANSIT_STOPS_DISTANCE, SourceType.TRANSIT_STOPS, DistanceInfluence.QUADRATIC, EntityAggregation.SUM),
    CRITERION_REFERENCE(Constants.CRITERION_TYPE_REFERENCE, null, null, null);

    private final String name;
    private final SourceType sourceType;
    private final DistanceInfluence distanceInfluence;
    private final EntityAggregation entityAggregation;

    CriterionType(String name, SourceType sourceType, DistanceInfluence distanceInfluence, EntityAggregation entityAggregation) {
        this.name = name;
        this.sourceType = sourceType;
        this.distanceInfluence = distanceInfluence;
        this.entityAggregation = entityAggregation;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CriterionType fromString(String name) {
        return name == null ? null : Stream.of(CriterionType.values())
                .filter(enu -> enu.name.equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new RequestConfigurationException("Unsupported criterion type"));
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}

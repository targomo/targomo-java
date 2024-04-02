package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.targomo.client.Constants;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Locale;
import java.util.Map;

@JsonSubTypes({
        @JsonSubTypes.Type(value = ReferenceCriterionDefinition.class, name = Constants.CRITERION_TYPE_REFERENCE),
        @JsonSubTypes.Type(value = PoiReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_CLOSEST_POI_DISTANCE),
        @JsonSubTypes.Type(value = PoiReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_CLOSEST_POI_ABSOLUTE_DISTANCE),
        @JsonSubTypes.Type(value = PoiReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_POI_COVERAGE_COUNT),
        @JsonSubTypes.Type(value = PoiReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_POI_COVERAGE_DISTANCE),
        @JsonSubTypes.Type(value = PoiInZoneCriterionDefinition.class, name = Constants.CRITERION_TYPE_POI_COUNT_IN_ZONE),
        @JsonSubTypes.Type(value = StatisticsReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_STATISTICS_SUM),
        @JsonSubTypes.Type(value = StatisticsReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_STATISTICS_MAX),
        @JsonSubTypes.Type(value = StatisticsReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_STATISTICS_DISTANCE),
        @JsonSubTypes.Type(value = StatisticsInZoneCriterionDefinition.class, name = Constants.CRITERION_TYPE_STATISTICS_SUM_IN_ZONE),
        @JsonSubTypes.Type(value = StatisticsGravitationCriterionDefinition.class, name = Constants.CRITERION_TYPE_GRAVITATION_SUM),
        @JsonSubTypes.Type(value = StatisticsOnEnclosingCellCriterionDefinition.class, name = Constants.CRITERION_TYPE_STATISTICS_ON_ENCLOSING_CELL),
        @JsonSubTypes.Type(value = PoiGravitationCriterionDefinition.class, name = Constants.CRITERION_TYPE_POI_GRAVITATION_SUM),
        @JsonSubTypes.Type(value = EdgeStatisticsCriterionDefinition.class, name = Constants.CRITERION_TYPE_EDGE_STATISTICS),
        @JsonSubTypes.Type(value = EdgeStatisticsReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_EDGE_STATISTICS_REACHABILITY),
        @JsonSubTypes.Type(value = MathAggregationCriterionDefinition.class, name = Constants.CRITERION_TYPE_MATH_AGGREGATION),
        @JsonSubTypes.Type(value = MobilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_STAYPOINT_COUNT),
        @JsonSubTypes.Type(value = TransitStopsCriterionDefinition.class, name = Constants.CRITERION_TYPE_TRANSIT_STOPS_SUM),
        @JsonSubTypes.Type(value = TransitStopsCriterionDefinition.class, name = Constants.CRITERION_TYPE_TRANSIT_STOPS_DISTANCE),
        @JsonSubTypes.Type(value = PolygonAreaCriterionDefinition.class, name = Constants.CRITERION_TYPE_POLYGON_AREA)
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder(toBuilder = true)
public abstract class CriterionDefinition implements CriterionDefinitionInterface{
    private final CriterionType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double distanceExponent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double scalingFactor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double distanceModifier;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double lowerBound;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Map<Locale, CriterionMetadata> metadata;
}

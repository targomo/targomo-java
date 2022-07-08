package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.quality.Constants;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import com.targomo.core.util.logging.model.RequestAttributes;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonSubTypes({
        @JsonSubTypes.Type(value = ReferenceCriterionDefinition.class, name = Constants.CRITERION_TYPE_REFERENCE),
        @JsonSubTypes.Type(value = PoiReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_CLOSEST_POI_DISTANCE),
        @JsonSubTypes.Type(value = PoiReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_POI_COVERAGE_COUNT),
        @JsonSubTypes.Type(value = PoiReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_POI_COVERAGE_DISTANCE),
        @JsonSubTypes.Type(value = PoiInZoneCriterionDefinition.class, name = Constants.CRITERION_TYPE_POI_COUNT_IN_ZONE),
        @JsonSubTypes.Type(value = StatisticsReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_STATISTICS_SUM),
        @JsonSubTypes.Type(value = StatisticsReachabilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_STATISTICS_DISTANCE),
        @JsonSubTypes.Type(value = StatisticsInZoneCriterionDefinition.class, name = Constants.CRITERION_TYPE_STATISTICS_SUM_IN_ZONE),
        @JsonSubTypes.Type(value = StatisticsGravitationCriterionDefinition.class, name = Constants.CRITERION_TYPE_GRAVITATION_SUM),
        @JsonSubTypes.Type(value = PoiGravitationCriterionDefinition.class, name = Constants.CRITERION_TYPE_POI_GRAVITATION_SUM),
        @JsonSubTypes.Type(value = EdgeStatisticsCriterionDefinition.class, name = Constants.CRITERION_TYPE_EDGE_STATISTICS),
        @JsonSubTypes.Type(value = MathAggregationCriterionDefinition.class, name = Constants.CRITERION_TYPE_MATH_AGGREGATION),
        @JsonSubTypes.Type(value = MobilityCriterionDefinition.class, name = Constants.CRITERION_TYPE_STAYPOINT_COUNT),
        @JsonSubTypes.Type(value = TransitStopsCriterionDefinition.class, name = Constants.CRITERION_TYPE_TRANSIT_STOPS_SUM),
        @JsonSubTypes.Type(value = TransitStopsCriterionDefinition.class, name = Constants.CRITERION_TYPE_TRANSIT_STOPS_DISTANCE)
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
@SuperBuilder(toBuilder = true)
@JsonDeserialize(builder = CriterionDefinition.CriterionDefinitionBuilderImpl.class)
public abstract class CriterionDefinition {
    private final CriterionType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double distanceExponent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double scalingFactor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double distanceModifier;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double lowerBound;

    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LookUpTableCacheStatus lookUpTableCacheStatus;

    /**
     * Generates a RequestAttribute builder in order to log the criterion definition into the user service
     * @return a partial RequestAttributes.RequestAttributesBuilder
     */
    @JsonIgnore
    public abstract RequestAttributes.RequestAttributesBuilder getRequestAttributesBuilder(boolean isScore);

    /**
     * Gets the request type depending if a request is a score request or a rating one
     * @param isScore true if the request is a score request
     * @return FineGrainedRequestTypeEnum
     */
    @JsonIgnore
    public abstract FineGrainedRequestTypeEnum getRequestType(boolean isScore);


    @JsonPOJOBuilder(withPrefix="")
    public abstract static class CriterionDefinitionBuilderImpl extends CriterionDefinitionBuilder<CriterionDefinition, CriterionDefinitionBuilderImpl> {
    }
}

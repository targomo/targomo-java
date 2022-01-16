package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

/**
 * The currently implemented aggregations that are supported by the multi graph request.
 */
public enum MultiGraphAggregationType {

    NONE                        (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NONE,                        false, false, false),
    ONE                         (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_ONE,                         false, false, false),
    MINIMUM                     (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MINIMUM,                     true,  false, false),
    MAXIMUM                     (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MAXIMUM,                     true,  false, false),
    SUM                         (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_SUM,                         true,  false, false),
    MEAN                        (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEAN,                        true,  false, false),
    MEDIAN                      (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEDIAN,                      true,  false, false),
    NEAREST                     (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NEAREST,                     false, false, false),
    COUNT                       (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_COUNT,                       true,  false, false),
    ROUTING_UNION               (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_ROUTING_UNION,               false, false, true),
    MATH                        (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MATH,                        false, false, false),
    GRAVITATION_HUFF            (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_GRAVITATION_HUFF,            true,  true,  false),
    LOGIT                       (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_LOGIT,                       true,  true,  false),
     /**
     * the following two aggregations can only be used as main aggregation with no other aggregations in the pipeline - needs to be enabled for the endpoint
     * **/
    GRAVITATION_HUFF_OPTIMIZED          (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_GRAVITATION_HUFF_OPTIMIZED,         true, false),
    GRAVITATION_HUFF_OPTIMIZED_HEATMAP  (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_GRAVITATION_HUFF_OPTIMIZED_HEATMAP, true, true),
    LOGIT_OPTIMIZED                     (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_LOGIT_OPTIMIZED,                    true, false),
    LOGIT_OPTIMIZED_HEATMAP             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_GRAVITATION_HUFF_OPTIMIZED_HEATMAP, true, true);

    private final String key;
    private final boolean mayIgnoreOutliers;
    private final boolean requiresGravitationParameters;
    private final boolean aggregationInRouting;
    private final boolean optimizedProbabilityAggregation;
    private final boolean optimizedProbabilityAggregationForHeatmap;

    /**
     * Constructor for normal multigraph aggregations
     */
    MultiGraphAggregationType(String key,
                              boolean mayIgnoreOutliers,
                              boolean requiresGravitationParameters,
                              boolean aggregationInRouting) {
        this.key = key;
        this.mayIgnoreOutliers = mayIgnoreOutliers;
        this.aggregationInRouting = aggregationInRouting;
        this.requiresGravitationParameters = requiresGravitationParameters;
        this.optimizedProbabilityAggregation = false;
        this.optimizedProbabilityAggregationForHeatmap = false;
    }

    /**
     * Constructor for optimized probability aggregations
     */
    MultiGraphAggregationType(String key,
                              boolean optimizedProbabilityAggregation,
                              boolean optimizedProbabilityAggregationForHeatmap) {
        this.key = key;
        this.mayIgnoreOutliers = false;
        this.aggregationInRouting = false;
        this.requiresGravitationParameters = true;
        this.optimizedProbabilityAggregation = optimizedProbabilityAggregation;
        this.optimizedProbabilityAggregationForHeatmap = optimizedProbabilityAggregationForHeatmap;
    }

    @JsonCreator
    public static MultiGraphAggregationType fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphAggregationType.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        MultiGraphAggregationType.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    @JsonIgnore
    public boolean isAggregationInRouting() {
        return aggregationInRouting;
    }

    @JsonIgnore
    public boolean mayIgnoreOutliers() {
        return mayIgnoreOutliers;
    }

    @JsonIgnore
    public boolean requiresGravitationParameters() {
        return requiresGravitationParameters;
    }

    @JsonIgnore
    public boolean isOptimizedProbabilityAggregation() {
        return optimizedProbabilityAggregation;
    }

    @JsonIgnore
    public boolean isOptimizedProbabilityAggregationForHeatmap() {
        return optimizedProbabilityAggregationForHeatmap;
    }
}

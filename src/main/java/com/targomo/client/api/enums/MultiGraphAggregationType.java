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
    GRAVITATION_HUFF            (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_GRAVITATION_HUFF,            true,  true, false),
    /**
     * can only be used as main aggregation with no other aggregations in the pipeline - needs to be enabled for the endpoint
     * **/
    GRAVITATION_HUFF_OPTIMIZED  (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_GRAVITATION_HUFF_OPTIMIZED,  false,  true, false);

    private final String key;
    private final boolean mayIgnoreOutliers;
    private final boolean requiresGravitationParameters;
    private final boolean aggregationInRouting;

    MultiGraphAggregationType(String key,
                              boolean mayIgnoreOutliers,
                              boolean requiresGravitationParameters,
                              boolean aggregationInRouting) {
        this.key = key;
        this.mayIgnoreOutliers = mayIgnoreOutliers;
        this.aggregationInRouting = aggregationInRouting;
        this.requiresGravitationParameters = requiresGravitationParameters;
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
}

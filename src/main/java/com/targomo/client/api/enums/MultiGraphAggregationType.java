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

    NONE                (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NONE,                 false, false, false),
    ONE                 (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_ONE,                  false, false, false),
    MINIMUM             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MINIMUM,              false, false, false),
    MAXIMUM             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MAXIMUM,              false, false, false),
    SUM                 (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_SUM,                  false, false, false),
    MEAN                (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEAN,                 false, false, false),
    MEDIAN              (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEDIAN,               false, false, false),
    NEAREST             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NEAREST,              true, false, false),
    ROUTING_UNION       (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_ROUTING_UNION,        false, false, true),
    GRAVITATION_HUFF    (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_GRAVITATION_HUFF,     true, true , false),
    MATH                (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MATH,                 true, false, false);

    private final String key;
    private final boolean requireValuesForAllInputs;
    private final boolean requiresGravitationParameters;
    private final boolean aggregationInRouting;

    MultiGraphAggregationType(String key,
                              boolean requireValuesForAllInputs,
                              boolean requiresGravitationParameters,
                              boolean aggregationInRouting) {
        this.key = key;
        this.requireValuesForAllInputs = requireValuesForAllInputs;
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
    public boolean isRequireValuesForAllInputs() {
        return requireValuesForAllInputs;
    }

    @JsonIgnore
    public boolean isAggregationInRouting() {
        return aggregationInRouting;
    }

    @JsonIgnore
    public boolean getRequiresGravitationParameters() {
        return requiresGravitationParameters;
    }
}

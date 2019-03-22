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

    ONE                 (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_ONE,                  false, null),
    MINIMUM             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MINIMUM,              false, null),
    MAXIMUM             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MAXIMUM,              false, null),
    SUM                 (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_SUM,                  false, null),
    MEAN                (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEAN,                 false, null),
    MEDIAN              (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEDIAN,               false, null),
    NEAREST             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NEAREST,              false, null),
    ROUTING_UNION       (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_ROUTING_UNION,        false, PolygonIntersectionMode.UNION),
    GRAVITATION_HUFF    (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_GRAVITATION_HUFF,     true , null),
    MATHS               (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MATHS,                false, null);

    private final String key;
    private final PolygonIntersectionMode associatedRoutingAggregation;
    private final boolean requiresGravitationParameters;

    MultiGraphAggregationType(String key,
                              boolean requiresGravitationParameters,
                              PolygonIntersectionMode associatedRoutingAggregation) {
        this.key = key;
        this.associatedRoutingAggregation = associatedRoutingAggregation;
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
    public PolygonIntersectionMode getAssociatedRoutingAggregation() {
        return associatedRoutingAggregation;
    }

    @JsonIgnore
    public boolean getRequiresGravitationParameters() {
        return requiresGravitationParameters;
    }
}

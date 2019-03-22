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

    NONE                (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NONE,                 null),
    MINIMUM             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MINIMUM,              null),
    MAXIMUM             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MAXIMUM,              null),
    SUM                 (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_SUM,                  null),
    MEAN                (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEAN,                 null),
    MEDIAN              (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEDIAN,               null),
    NEAREST             (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NEAREST,              null),
    MATHS               (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MATHS,                null),
    ROUTING_UNION       (Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_ROUTING_UNION,        PolygonIntersectionMode.UNION);

    private final String key;
    private final PolygonIntersectionMode associatedRoutingAggregation;

    MultiGraphAggregationType(String key, PolygonIntersectionMode associatedRoutingAggregation) {
        this.key = key;
        this.associatedRoutingAggregation = associatedRoutingAggregation;
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
}

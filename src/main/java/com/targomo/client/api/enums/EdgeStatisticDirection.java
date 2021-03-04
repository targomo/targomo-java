package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

/**
 * Specifying which directions of the edge should be considered and if more then one then how they will be aggregated.
 * 0: the direction from the edges start node to its end node
 * 1: the direction from the edges end node to its start node
 * 2: both directions. The statistics value in this case is an already aggregated value for both directions.
 *    Usually either the directions 0 and 1 or direction 2 are available for an edge statistics group.
 * any: all/any available direction(s). Depending on the service all directions that are available (0, 1, or 2)
 *      will be considered in the computation or will be returned in the request.
 * sum: values for both direction (0 and 1) will be added. Only available if directions 0 and 1 are available.
 * mean: the average of the values for both direction (0 and 1) will be used. Only available if directions 0 and 1 are available.
 */
public enum EdgeStatisticDirection {
    DIR0("0"),
    DIR1("1"),
    DIR2("2"),
    ANY("any"),
    SUM("sum"),
    MEAN("mean");

    @JsonValue
    private String type;

    EdgeStatisticDirection(String type){
        this.type = type;
    }

    public boolean isAggregation() {
        return this == SUM || this == MEAN;
    }

    public static EdgeStatisticDirection fromString(final String type) {
        return type == null ? null : Stream.of(EdgeStatisticDirection.values())
                .filter(enu -> type.equalsIgnoreCase(enu.type))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return type;
    }
}

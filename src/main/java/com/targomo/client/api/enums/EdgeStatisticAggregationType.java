package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum EdgeStatisticAggregationType {
    NONE("none"),
    SUM("sum"),
    AVERAGE("average");

    @JsonValue
    private String type;

    EdgeStatisticAggregationType(String type){
        this.type = type;
    }

    public static EdgeStatisticAggregationType fromString(String type) {
        return type == null ? null : Stream.of(EdgeStatisticAggregationType.values())
                .filter( enu -> type.equalsIgnoreCase(enu.type))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return type;
    }
}

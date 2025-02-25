package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum EdgeStatisticCalculationType {
    MAX("max"),
    CROSSING_RADIUS("crossingRadius");

    @JsonValue
    private String type;

    EdgeStatisticCalculationType(String type){
        this.type = type;
    }

    public static EdgeStatisticCalculationType fromString(String type) {
        return type == null ? null : Stream.of(EdgeStatisticCalculationType.values())
                .filter( enu -> type.equalsIgnoreCase(enu.type))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return type;
    }
}

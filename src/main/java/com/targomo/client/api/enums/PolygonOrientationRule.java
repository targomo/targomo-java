package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum PolygonOrientationRule {

    RIGHT_HAND("right_hand"),
    LEFT_HAND("left_hand");

    private String key;

    PolygonOrientationRule(String key) {
        this.key = key;
    }

    @JsonCreator
    public static PolygonOrientationRule fromString(String key) {
        return key == null ? null : Stream.of(PolygonOrientationRule.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        Format.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

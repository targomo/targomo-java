package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum PolygonOrientation {
    RIGHT_HAND("right_hand"),
    LEFT_HAND("left_hand");

    private String key;

    PolygonOrientation(String key) {
        this.key = key;
    }

    @JsonCreator
    public static PolygonOrientation fromString(String key) {
        return key == null ? null : Stream.of(PolygonOrientation.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        Format.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

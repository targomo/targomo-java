package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

/**
 * Created by gerb on 27/03/2017.
 */
public enum Format {

    JSON("json"),
    GEOJSON("geojson");

    private String key;

    Format(String key) {
        this.key = key;
    }

    @JsonCreator
    public static Format fromString(String key) {
        return key == null ? null : Stream.of(Format.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        Format.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

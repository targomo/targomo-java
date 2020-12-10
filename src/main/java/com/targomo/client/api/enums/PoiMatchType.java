package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum PoiMatchType {
    ANY("any"),
    ALL("all");

    private String name;

    PoiMatchType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static PoiMatchType fromString(String name) {
        return name == null ? null : Stream.of(PoiMatchType.values())
                .filter(enu -> enu.name.equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported match type"));
    }
}

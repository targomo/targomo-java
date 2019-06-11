package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

/**
 * Enumeration for custom geometry layers aggregation on MultiGraph requests
 */
public enum GeometryMergeAggType {
    MAX ("max"),
    MEAN("mean"),
    MIN("min"),
    SUM("sum");

    private String name;

    GeometryMergeAggType(String name){
        this.name = name;
    }

    @JsonCreator
    public static GeometryMergeAggType fromString(String key) {
        return key == null ? null : Stream.of(GeometryMergeAggType.values())
                .filter( enu -> key.equalsIgnoreCase(enu.name))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }

}

package com.targomo.client.api.enums;

import java.util.stream.Stream;

public enum MultiGraphGeometryMergeAggregationType {
    MAX ("max"),
    MEAN("mean"),
    MIN("min"),
    SUM("sum");

    private String name;

    private MultiGraphGeometryMergeAggregationType(String name){
        this.name = name;
    }

    public static MultiGraphGeometryMergeAggregationType fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphGeometryMergeAggregationType.values())
                .filter( enu -> key.equalsIgnoreCase(enu.name))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }

}

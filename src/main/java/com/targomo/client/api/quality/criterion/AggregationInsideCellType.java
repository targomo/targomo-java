package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum AggregationInsideCellType {
    AVG("AVG"),
    SUM("SUM");

    private String name;

    AggregationInsideCellType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static AggregationInsideCellType fromString(String name) {
        return name == null ? null : Stream.of(AggregationInsideCellType.values())
                .filter(enu -> enu.name.equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported aggregation in cell type"));
    }
}

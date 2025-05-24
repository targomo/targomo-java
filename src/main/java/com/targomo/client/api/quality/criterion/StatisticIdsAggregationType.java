package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum StatisticIdsAggregationType {
    AVG("AVG"),
    SUM("SUM");

    private String name;

    StatisticIdsAggregationType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static StatisticIdsAggregationType fromString(String name) {
        return name == null ? null : Stream.of(StatisticIdsAggregationType.values())
                .filter(enu -> enu.name.equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported statistic ids aggregation type"));
    }
}

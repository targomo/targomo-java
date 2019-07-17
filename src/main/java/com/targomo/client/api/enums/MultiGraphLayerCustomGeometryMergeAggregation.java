package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

/**
 * Enumeration for custom geometry layers aggregation on MultiGraph requests
 */
public enum MultiGraphLayerCustomGeometryMergeAggregation {

    MAX(Constants.KEY_MULTIGRAPH_LAYER_CUSTOM_GEOMETRY_MERGE_AGGREGATION_MAX),
    MEAN(Constants.KEY_MULTIGRAPH_LAYER_CUSTOM_GEOMETRY_MERGE_AGGREGATION_MEAN),
    MIN(Constants.KEY_MULTIGRAPH_LAYER_CUSTOM_GEOMETRY_MERGE_AGGREGATION_MIN),
    SUM(Constants.KEY_MULTIGRAPH_LAYER_CUSTOM_GEOMETRY_MERGE_AGGREGATION_SUM);

    private String key;

    MultiGraphLayerCustomGeometryMergeAggregation(String key){
        this.key = key;
    }

    @JsonCreator
    public static MultiGraphLayerCustomGeometryMergeAggregation fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphLayerCustomGeometryMergeAggregation.values())
                .filter( enu -> key.equalsIgnoreCase(enu.key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        MultiGraphLayerType.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }

}

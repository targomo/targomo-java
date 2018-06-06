package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

public enum MultiGraphLayerType {

    NODE        (Constants.KEY_MULTIGRAPH_LAYER_TYPE_NODE,          false,  false),
    EDGE        (Constants.KEY_MULTIGRAPH_LAYER_TYPE_EDGE,          true,   false),
    TILE        (Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILE,          true,   true),
    TILE_NODE   (Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILE_NODE,     false,  true),
    HEXAGON     (Constants.KEY_MULTIGRAPH_LAYER_TYPE_HEXAGON,       true,   true),
    HEXAGON_NODE(Constants.KEY_MULTIGRAPH_LAYER_TYPE_HEXAGON_NODE,  false,  true);

    private String key;
    private boolean withEdges;
    private boolean isGeometryMerge;

    MultiGraphLayerType(String key, boolean withEdges, boolean isGeometryMerge) {
        this.key             = key;
        this.withEdges       = withEdges;
        this.isGeometryMerge = isGeometryMerge;
    }

    @JsonCreator
    public static MultiGraphLayerType fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphLayerType.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        MultiGraphLayerType.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    @JsonIgnore
    public boolean isWithEdges() {
        return withEdges;
    }

    @JsonIgnore
    public boolean isGeometryMerge() {
        return isGeometryMerge;
    }

}

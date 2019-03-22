package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

/**
 * The currently implemented layer types that are supported by the multi graph request.
 */
public enum MultiGraphLayerType {

    NODE                (Constants.KEY_MULTIGRAPH_LAYER_TYPE_NODE,                  false,  false,  false),
    EDGE                (Constants.KEY_MULTIGRAPH_LAYER_TYPE_EDGE,                  true,   false,  false),
    TILE                (Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILE,                  true,   true,   false),
    TILE_NODE           (Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILE_NODE,             false,  true,   false),
    HEXAGON             (Constants.KEY_MULTIGRAPH_LAYER_TYPE_HEXAGON,               true,   true,   false),
    HEXAGON_NODE        (Constants.KEY_MULTIGRAPH_LAYER_TYPE_HEXAGON_NODE,          false,  true,   false),
    GEOMETRY_STATISTICS (Constants.KEY_MULTIGRAPH_LAYER_TYPE_GEOMETRY_STATISTICS,   false,  false,  true),
    TILE_STATISTICS     (Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILE_STATISTICS,       false,  true,   true);

    //TODO we should differentiate between input and output layers - for instance NODE, EDGE, GEOMETRY can be input layers
    // (saved in the multigraph) and NODE, EDGE, TILE, HEXAGON, GEOMETRY can be output layers (for serializations)
    //TODO not all combinations are valid, e.g. invalid examples:
    // - node -> edge
    // - anything other than geometry -> geometry

    private String key;
    private boolean withEdges;
    private boolean geometryMerge;
    private boolean basedOnStatistics;

    MultiGraphLayerType(String key, boolean withEdges, boolean geometryMerge, boolean basedOnStatistics) {
        this.key             = key;
        this.withEdges       = withEdges;
        this.geometryMerge = geometryMerge;
        this.basedOnStatistics = basedOnStatistics;
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
        return geometryMerge;
    }

    @JsonIgnore
    public boolean isBasedOnStatistics() {
        return basedOnStatistics;
    }

}

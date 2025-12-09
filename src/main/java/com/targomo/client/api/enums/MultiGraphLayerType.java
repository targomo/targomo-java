package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.targomo.client.api.enums.MultiGraphDomainType.*;

/**
 * The currently implemented layer types that are supported by the multi graph request.
 */
public enum MultiGraphLayerType {

    IDENTITY            (Constants.KEY_MULTIGRAPH_LAYER_TYPE_IDENTITY,          false, EDGE, NODE, STATISTIC_GEOMETRY),
    TILE                (Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILE,              true,  EDGE, NODE),
    HEXAGON             (Constants.KEY_MULTIGRAPH_LAYER_TYPE_HEXAGON,           true,  EDGE, NODE),
    H3HEXAGON           (Constants.KEY_MULTIGRAPH_LAYER_TYPE_H3HEXAGON,         true,  EDGE, NODE, STATISTIC_GEOMETRY, NONE),
    CUSTOM_GEOMETRIES   (Constants.KEY_MULTIGRAPH_LAYER_TYPE_CUSTOM_GEOMETRIES, false, EDGE, NODE, STATISTIC_GEOMETRY);

    private final String key;
    private final boolean requiresFixedPrecisionOrTile;
    private final List<MultiGraphDomainType> supportedDomainTypes;

    MultiGraphLayerType(String key,
                        boolean requiresFixedPrecisionOrTile,
                        MultiGraphDomainType... supportedDomainTypes) {

        this.key                          = key;
        this.requiresFixedPrecisionOrTile = requiresFixedPrecisionOrTile;
        this.supportedDomainTypes         = Arrays.asList(supportedDomainTypes);
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
    public boolean isRequiresFixedPrecisionOrTile() {
        return requiresFixedPrecisionOrTile;
    }

    @JsonIgnore
    public List<MultiGraphDomainType> getSupportedDomainTypes() {
        return supportedDomainTypes;
    }

}

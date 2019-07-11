package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

import static com.targomo.client.api.enums.MultiGraphDomainType.*;

/**
 * The currently implemented layer types that are supported by the multi graph request.
 */
public enum MultiGraphLayerType {

    IDENTITY            (Constants.KEY_MULTIGRAPH_LAYER_TYPE_IDENTITY,          false, MultiGraphDomainType.values()),
    TILE                (Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILE,              true,  EDGE, NODE),
    HEXAGON             (Constants.KEY_MULTIGRAPH_LAYER_TYPE_HEXAGON,           true,  EDGE, NODE),
    CUSTOM_GEOMETRIES   (Constants.KEY_MULTIGRAPH_LAYER_TYPE_CUSTOM_GEOMETRIES, false, MultiGraphDomainType.values());

    private final String key;
    private final boolean geometryMerge; //TODO probably needs to be renamed -
    // TODO only applicable to tile and hexagon? only for tiled requests? what's with customGeometries
    // TODO basically all of them can have a customGeometryAggregation - is the default one chosen for all of them as well?
    private final MultiGraphDomainType[] supportedDomainTypes;

    MultiGraphLayerType(String key,
                        boolean geometryMerge,
                        MultiGraphDomainType... supportedDomainTypes) {

        this.key                         = key;
        this.geometryMerge               = geometryMerge;
        this.supportedDomainTypes = supportedDomainTypes;
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
    public boolean isGeometryMerge() {
        return geometryMerge;
    }

    @JsonIgnore
    public MultiGraphDomainType[] getSupportedDomainTypes() {
        return supportedDomainTypes;
    }

}

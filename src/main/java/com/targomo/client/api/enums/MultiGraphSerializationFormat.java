package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

import static com.targomo.client.api.enums.MultiGraphLayerType.*;

/**
 * The currently implemented serialization formats that are supported by the multi graph request.
 */
public enum MultiGraphSerializationFormat {

    //JSON does also currently not support "identity" for "domainType": "statistic_cell"
    JSON(Constants.KEY_MULTIGRAPH_SERIALIZATION_FORMAT_JSON,                                CUSTOM_GEOMETRIES, IDENTITY),
    GEOJSON(Constants.KEY_MULTIGRAPH_SERIALIZATION_FORMAT_GEOJSON,                          IDENTITY, TILE, HEXAGON),
    MAPBOX_VECTOR_TILES(Constants.KEY_MULTIGRAPH_SERIALIZATION_FORMAT_MAPBOX_VECTOR_TILES,  IDENTITY, TILE, HEXAGON);

    private String key;
    private final MultiGraphLayerType[] supportedLayerTypes;

    MultiGraphSerializationFormat(String key, MultiGraphLayerType... supportedLayerTypes) {
        this.key                 = key;
        this.supportedLayerTypes = supportedLayerTypes;
    }

    @JsonCreator
    public static MultiGraphSerializationFormat fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphSerializationFormat.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        MultiGraphSerializationFormat.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    @JsonIgnore
    public MultiGraphLayerType[] getSupportedLayerTypes() {
        return supportedLayerTypes;
    }
}

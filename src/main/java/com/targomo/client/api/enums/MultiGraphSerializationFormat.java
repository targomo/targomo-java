package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;


/**
 * The currently implemented serialization formats that are supported by the multi graph request.
 */
public enum MultiGraphSerializationFormat {

    JSON                (Constants.KEY_MULTIGRAPH_SERIALIZATION_FORMAT_JSON),
    GEOJSON             (Constants.KEY_MULTIGRAPH_SERIALIZATION_FORMAT_GEOJSON),
    MAPBOX_VECTOR_TILES (Constants.KEY_MULTIGRAPH_SERIALIZATION_FORMAT_MAPBOX_VECTOR_TILES);

    private String key;

    MultiGraphSerializationFormat(String key) {
        this.key                 = key;
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
}

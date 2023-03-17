package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

public enum MultiGraphSerializationH3IdFormat {

    STRING  (Constants.KEY_MULTIGRAPH_SERIALIZATION_H3_ID_FORMAT_STRING),
    NUMERIC (Constants.KEY_MULTIGRAPH_SERIALIZATION_H3_ID_FORMAT_NUMERIC);

    private String key;

    MultiGraphSerializationH3IdFormat(String key) {
        this.key = key;
    }

    @JsonCreator
    public static MultiGraphSerializationH3IdFormat fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphSerializationH3IdFormat.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        MultiGraphSerializationH3IdFormat.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum EdgeWeightType {

    TIME("time"),
    DISTANCE("distance");

    private String key;

    EdgeWeightType(String key) {
        this.key = key;
    }

    @JsonCreator
    public static EdgeWeightType fromString(String key) {
        return key == null ? null : Stream.of(EdgeWeightType.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        EdgeWeightType.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

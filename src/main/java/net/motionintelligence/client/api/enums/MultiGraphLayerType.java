package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MultiGraphLayerType {

    PERVERTEX("perVertex"),
    PEREDGE("perEdge");

    private String key;

    MultiGraphLayerType(String key) {
        this.key = key;
    }

    @JsonCreator
    public static MultiGraphLayerType fromString(String key) {
        return key == null ? null : MultiGraphLayerType.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return key;
    }

}

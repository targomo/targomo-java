package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MultiGraphEdgeAggregationType {

    MIN("min"),
    MAX("max"),
    MEAN("mean");

    private String key;

    MultiGraphEdgeAggregationType(String key) {
		this.key = key;
	}

    @JsonCreator
    public static MultiGraphEdgeAggregationType fromString(String key) {
        return key == null ? null : MultiGraphEdgeAggregationType.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
		return key;
	}
}

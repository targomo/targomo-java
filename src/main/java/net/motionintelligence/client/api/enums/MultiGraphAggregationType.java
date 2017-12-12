package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MultiGraphAggregationType {

    NONE("none"),
    MIN("min"),
    MAX("max"),
    SUM("sum"),
    MEAN("mean"),
    MEDIAN("median"),
    NEAREST("nearest");

    private String key;

    MultiGraphAggregationType(String key) {
		this.key = key;
	}

    @JsonCreator
    public static MultiGraphAggregationType fromString(String key) {
        return key == null ? null : MultiGraphAggregationType.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
		return key;
	}
}

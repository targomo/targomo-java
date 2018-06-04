package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EdgeWeightType {

	TIME("time"),
	DISTANCE("distance");

	private String key;

	EdgeWeightType(String key) {
		this.key = key;
	}

	@JsonCreator
	public static EdgeWeightType fromString(String key) {
		return key == null ? null : EdgeWeightType.valueOf(key.toUpperCase());
	}

	@JsonValue
	public String getKey() {
		return key;
	}
}

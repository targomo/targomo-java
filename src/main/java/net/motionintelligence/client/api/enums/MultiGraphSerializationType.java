package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MultiGraphSerializationType {

	JSON("json"),
	GEOJSON("geojson");

	private String key;

	MultiGraphSerializationType(String key) {
		this.key = key;
	}

	@JsonCreator
	public static MultiGraphSerializationType fromString(String key) {
		return key == null ? null : MultiGraphSerializationType.valueOf(key.toUpperCase());
	}

	@JsonValue
	public String getKey() {
		return key;
	}
}

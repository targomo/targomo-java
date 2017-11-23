package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Daniel Gerber
 *
 */
public enum MultiGraphSerializerType {

	JSON("json"),
	GEOJSON("geojson");

	private String key;

	MultiGraphSerializerType(String key) {
		this.key = key;
	}

	@JsonCreator
	public static MultiGraphSerializerType fromString(String key) {
		return key == null ? null : MultiGraphSerializerType.valueOf(key.toUpperCase());
	}

	@JsonValue
	public String getKey() {
		return key;
	}
}

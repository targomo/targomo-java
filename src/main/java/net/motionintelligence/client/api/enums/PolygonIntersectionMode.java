/**
 * 
 */
package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * @author Daniel Gerber
 *
 */
public enum PolygonIntersectionMode {

	AVERAGE("average"),
	UNION("union"),
	INTERSECTION("intersection"),
	NONE("none");

	private String key;

	PolygonIntersectionMode(String key) {
		this.key = key;
	}

	@JsonCreator
	public static PolygonIntersectionMode fromString(String key) {
		return key == null ? null : PolygonIntersectionMode.valueOf(key.toUpperCase(Locale.ENGLISH));
	}

	@JsonValue
	public String getKey() {
		return key;
	}
}

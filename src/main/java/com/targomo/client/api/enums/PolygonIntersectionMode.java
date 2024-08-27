/**
 * 
 */
package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

/**
 * @author Daniel Gerber
 *
 */
public enum PolygonIntersectionMode {

    AVERAGE("average"),
    UNION("union"),
    INTERSECTION("intersection");

    private String key;

    PolygonIntersectionMode(String key) {
		this.key = key;
	}

	@JsonCreator
	public static PolygonIntersectionMode fromString(String key) {
        return key == null ? null : Stream.of(PolygonIntersectionMode.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                     PolygonIntersectionMode.class.getSimpleName() + " specified: " + key + " doesn't exist"));
	}

    @JsonValue
    public String getKey() {
		return key;
	}
}

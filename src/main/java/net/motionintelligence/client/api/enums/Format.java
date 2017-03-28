package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by gerb on 27/03/2017.
 */
public enum Format {

    JSON("json"),
    GEOJSON("geojson");

    private String key;

    Format(String key) {
        this.key = key;
    }

    @JsonCreator
    public static Format fromString(String key) {
        return key == null ? null : Format.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

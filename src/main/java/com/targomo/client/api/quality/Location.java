package com.targomo.client.api.quality;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.targomo.client.api.pojo.LocationProperties;
import com.targomo.client.api.quality.deserializers.LocationDeserializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.geojson.GeoJsonObject;

@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@JsonDeserialize(contentAs=Location.class, using=LocationDeserializer.class)
// todo: used for test but might be dangerous. Check later if we can do differently.
@EqualsAndHashCode
public class Location {
    private static final String CACHE_KEY_SEPARATOR = "_";
    @Setter
    private String id;
    private final Double lat;
    private final Double lng;
    @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer crs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final GeoJsonObject geometry;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final LocationProperties properties;
    @JsonIgnore
    private final boolean point;
    @Setter @JsonIgnore
    private boolean competitor;

    public Location(String id, Double lat, Double lng, LocationProperties properties) {
        this(id, lat, lng, null, null, properties, true, false);
    }

    /**
     * @return true if location is a point, false if it is a geometry.
     */
    @JsonIgnore
    public boolean point() {
        return geometry == null;
    }

    public String computeCacheId(String apiKey, String ratingId) {
        String pointGeomSuffix = point() ? "POINT" : "GEOMETRY";
        return apiKey + CACHE_KEY_SEPARATOR + ratingId + CACHE_KEY_SEPARATOR + String.format("%.6f", lat) +
                CACHE_KEY_SEPARATOR + String.format("%.6f", lng) + CACHE_KEY_SEPARATOR + pointGeomSuffix;
    }
}

package com.targomo.client.api.quality;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.targomo.client.api.pojo.LocationProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.geojson.GeoJsonObject;

@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode
public class Location {
    @Setter
    private String id;
    private final Double lat;
    private final Double lng;
    @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer crs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final GeoJsonObject geometry;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String collectionId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final LocationProperties properties;
    @JsonIgnore
    private final boolean point;
    @JsonIgnore
    private final boolean reference;
    @Setter @JsonIgnore
    private boolean competitor;
    @Setter @JsonIgnore
    private boolean potential;

    public Location(String id, Double lat, Double lng, LocationProperties properties) {
        this(id, lat, lng, null, null, null, properties, true, false, false, false);
    }
}

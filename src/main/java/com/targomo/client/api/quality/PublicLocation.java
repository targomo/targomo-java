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
@JsonDeserialize(contentAs=PublicLocation.class, using=LocationDeserializer.class)
@EqualsAndHashCode
public class PublicLocation {
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

    public PublicLocation(String id, Double lat, Double lng, LocationProperties properties) {
        this(id, lat, lng, null, null, properties, true, false);
    }
}

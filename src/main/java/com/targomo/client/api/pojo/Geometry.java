package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.Constants;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by gerb on 07.06.18.
 *
 * This class is purposefully created very generally. Currently (13.06.2018) the targomo
 * backend only supports geojson objects. See the
 * <a href="https://tools.ietf.org/html/rfc7946">GeoJSON specification</a> for further details.
 * The geometry types <b>Polygon</b> and <b>MultiPolygon</b> are supported.
 *
 */
@EqualsAndHashCode
public class Geometry implements Serializable {

    private static final long serialVersionUID = 196773737265051450L;

    private final String type;
    private final String data;
    private final Integer crs;

    /**
     * Creates a geometry that is typed as geojson with the specified CRS.
     *
     * @param crs the coordinate reference system of the geometry
     * @param data the serialized geometry in a specified format
     */
    @JsonCreator
    public Geometry(@JsonProperty("crs") Integer crs, @JsonProperty("data") String data) {
        this.type = Constants.GEO_JSON;
        this.data = data;
        this.crs = crs;
    }

    /**
     * @return the type of this geometry
     */
    public String getType() {
        return type;
    }

    /**
     * @return the string representation of this geometry
     */
    public String getData() {
        return data;
    }

    /**
     * @return the coordinate reference system for this geometry
     */
    public Integer getCrs() {
        return crs;
    }
}

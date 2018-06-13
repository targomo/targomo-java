package com.targomo.client.api.pojo;

import com.targomo.client.Constants;
import com.targomo.client.api.enums.PolygonSerializerType;

/**
 * Created by gerb on 07.06.18.
 *
 * This class is purposefully create very generally. Currently (13.06.2018) the targomo
 * backend only supports geojson objects. See the
 * <a href="https://tools.ietf.org/html/rfc7946">GeoJSON specification</a> for further details.
 * The geometry types <b>Polygon</b> and <b>MultiPolygon</b> are supported.
 *
 */
public class Geometry {

    private final String type;
    private final String data;
    private final Integer crs;

    /**
     * Creates a geometry that is typed as geojson with the specified CRS.
     *
     * @param crs the coordinate reference system of the geometry
     * @param data the serialized geometry in a specified format
     */
    public Geometry(Integer crs, String data) {
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

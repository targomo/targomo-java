package com.targomo.client.api.pojo;

import com.targomo.client.api.enums.PolygonSerializerType;

/**
 * Created by gerb on 07.06.18.
 */
public class Geometry {

    private final PolygonSerializerType type;
    private final String data;
    private final Integer crs;

    /**
     * Creates a geometry that is typed as geojson.
     *
     * @param data the serialized geometry in a specified format
     */
    public Geometry(Integer crs, String data) {
        this.type = PolygonSerializerType.GEO_JSON_POLYGON_SERIALIZER;
        this.data = data;
        this.crs = crs;
    }

    public PolygonSerializerType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public Integer getCrs() {
        return crs;
    }
}

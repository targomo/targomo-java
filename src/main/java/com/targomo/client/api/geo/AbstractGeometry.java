package com.targomo.client.api.geo;

import com.targomo.client.api.pojo.LocationProperties;

import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Simple abstract class to use for storing geometry data with IDs and travel types.
 * @author gideon
 */
@MappedSuperclass
public abstract class AbstractGeometry extends AbstractLocation implements RoutingGeometry {

    private Integer crs;
    private String data;
    private Boolean routeFromCentroid = true;

    public AbstractGeometry() {} //For jackson test

    public AbstractGeometry(String id, Integer crs, String data, Boolean routeFromCentroid, LocationProperties locationProperties) {
        super(id, locationProperties);
        this.crs = crs;
        this.data = data;
        this.routeFromCentroid = routeFromCentroid;
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

    /**
     * @return route from geometry centroid boolean
     */
    public Boolean isRouteFromCentroid() {
        return routeFromCentroid;
    }

    /**
     * @param data the string representation of this geometry
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @param crs coordinate reference system for this geometry
     */
    public void setCrs(Integer crs) {
        this.crs = crs;
    }

    /**
     * @param routeFromCentroid if true route from/to the centroid of this geometry when no intersections found
     */
    public void setRouteFromCentroid(Boolean routeFromCentroid) {
        this.routeFromCentroid = routeFromCentroid;
    }

    /**
     * Returns a JSON String representation of the LocationGeometry with ID, geojson and crs values.
     * @return JSON representation of the geometry
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName());
        builder.append(" {\n\tid: ");
        builder.append(id);
        builder.append("\n\tdata: ");
        builder.append(getData());
        builder.append("\n\tcrs: ");
        builder.append(getCrs());
        builder.append("\n\trouteFromCentroid: ");
        builder.append(isRouteFromCentroid());
        builder.append("\n}\n");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGeometry that = (AbstractGeometry) o;

        if (!that.getData().equals(getData())) return false;
        if (!that.getCrs().equals(getCrs())) return false;
        if (!that.isRouteFromCentroid().equals(isRouteFromCentroid())) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        int temp = this.data != null ? this.data.hashCode() : 0;
        int crs = this.crs != null ? this.crs : 0;
        result = 31 * result + temp + crs + (routeFromCentroid ? 1 : 0);
        return result;
    }
}

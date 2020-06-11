package com.targomo.client.api.geo;

import javax.persistence.MappedSuperclass;

/**
 * Simple abstract class to use for storing geometry data with IDs and travel types.
 * @author gideon
 */
@MappedSuperclass
public abstract class AbstractGeometry implements RoutingGeometry {

    private String id;
    private Integer crs;
    private String data;

    public AbstractGeometry() {} //For jackson test

    public AbstractGeometry(String id, Integer crs, String data) {
        this.id = id;
        this.crs = crs;
        this.data = data;
    }

    /**
     * Get the ID associated with the geometry.
     * @return LocationGeometry ID
     */
    public String getId() {
        return id;
    }

    /**
     * Assign an ID to the geometry
     * @param id ID to be assigned
     */
    public void setId(final String id) {
        this.id = id;
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
        builder.append("\n}\n");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGeometry that = (AbstractGeometry) o;

        if (!that.getData().equals(getData())) return false;
        if (that.getCrs() != getCrs()) return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result;
        int temp;
        int crs;
        result = id != null ? id.hashCode() : 0;
        temp = getData() != null ? getData().hashCode() : 0;
        crs = getCrs() != null ? getCrs() : 0;
        result = 31 * result + temp + crs;
        return result;
    }
}

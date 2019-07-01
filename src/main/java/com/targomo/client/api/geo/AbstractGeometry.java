package com.targomo.client.api.geo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.api.pojo.Geometry;
import org.json.JSONObject;

import javax.persistence.MappedSuperclass;

/**
 * Simple abstract class to use for storing coordinates with IDs and travel types.
 * @author gideon
 */
@MappedSuperclass
public abstract class AbstractGeometry extends Geometry implements Location {

    private String id;

    @JsonCreator
    public AbstractGeometry(@JsonProperty("id") String id, @JsonProperty("crs") Integer crs, @JsonProperty("data") String data) {
        super(crs, data);
        this.id = id;
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

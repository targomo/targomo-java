package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
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

    private JSONObject geojson;

    public AbstractGeometry() {
        super(4326, "");
    }

    /**
     * Generate a LocationGeometry with an ID along with geojson and a crs value.
     * @param id ID to associate with the target coordinate
     * @param geojson LocationGeometry geojson String
     */
    public AbstractGeometry(final String id, final JSONObject geojson) {
        super(4326, geojson.toString());
        this.id = id;
        this.geojson = geojson;
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
     * Get the geometry as geojson
     * @return Geojson of a geometry
     */
    public JSONObject getGeojson() {
        return geojson;
    }

    /**
     * Set the geometry as geojson
     * @param geojson Geojson of a geometry
     */
    public void setGeojson(final JSONObject geojson) {
        this.geojson = geojson;
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
        builder.append("\n\tgeojson: ");
        builder.append(geojson);
        builder.append("\n}\n");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGeometry that = (AbstractGeometry) o;

        if (!that.geojson.toString().equals(geojson.toString())) return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result;
        int temp;
        result = id != null ? id.hashCode() : 0;
        temp = geojson != null ? geojson.hashCode() : 0;
        result = 31 * result + temp ;
        return result;
    }
}

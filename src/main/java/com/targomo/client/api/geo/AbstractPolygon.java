package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import org.json.JSONObject;

import javax.persistence.MappedSuperclass;

/**
 * Simple abstract class to use for storing coordinates with IDs and travel types.
 * @author gideon
 */
@MappedSuperclass
public abstract class AbstractPolygon extends Polygon{
    private String id;

    private JSONObject geojson;

    private int crs;

    // needed for jackson
    public AbstractPolygon(){}

    /**
     * Generate a Polygon with an ID along with geojson and a crs value.
     * @param id ID to associate with the target coordinate
     * @param geojson Geometry geojson String
     * @param crs Coordinate Reference System used in the geojson
     */
    public AbstractPolygon(final String id, final JSONObject geojson, final int crs) {
        this.id = id;
        this.geojson = geojson;
        this.crs = crs;
    }

    /**
     * Get TravelType of polygon.
     * @return Travel type associated with the polygon
     */
    public abstract TravelType getTravelType();

    /**
     * Set a travel type for the polygon.
     * @param travelType Travel type to be associated with the polygon.
     */
    public abstract void setTravelType(final TravelType travelType);

    /**
     * Get the ID associated with the polygon.
     * @return Polygon ID
     */
    public String getId() {
        return id;
    }

    /**
     * Assign an ID to the polygon
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
     * Get the Coordinate Reference System of the geometry
     * @return CRS of the geometry
     */
    public int getCrs() {
        return crs;
    }

    /**
     * Set the Coordinate Reference System of the geometry
     * @param crs CRS value of the geometry
     */
    public void setCrs(final int crs) {
        this.crs = crs;
    }

    /**
     * Returns a JSON String representation of the Polygon with ID, geojson and crs values.
     * @return JSON representation of the polygon
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName());
        builder.append(" {\n\tid: ");
        builder.append(id);
        builder.append("\n\tgeojson: ");
        builder.append(geojson);
        builder.append("\n\tcrs: ");
        builder.append(crs);
        builder.append("\n}\n");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPolygon that = (AbstractPolygon) o;

        if (!that.geojson.toString().equals(geojson.toString())) return false;
        if (that.crs != crs) return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result;
        int temp;
        result = id != null ? id.hashCode() : 0;
        temp = geojson != null ? geojson.hashCode() : 0;
        result = 31 * result + temp + crs;
        return result;
    }
}

package com.targomo.client.api.geo;

import org.json.JSONObject;

/**
 * Geometries act in a similar manner to {@link Coordinate} except they represent geometries
 * (Polygons, MultiPolygons or LineStrings As Geojson with CRS) rather than points
 *
 * @author gideon
 */
public interface Geometry extends Location {

    /**
     * Get the GeoJSON string of the geometry.
     * @return Geometry as a GeoJSON String
     */
    JSONObject getGeojson();

    /**
     * Set the GeoJSON string of the geometry.
     * @param geojson Geometry as a GeoJSON String
     */
    void setGeojson(final JSONObject geojson);

    /**
     * Get Coordinate Reference System (CRS) Value
     * @return CRS value
     */
    int getCrs();

    /**
     * Set the Coordinate Reference System (CRS) Value
     * @param crs CRS value to be set
     */
    void setCrs(final int crs);
}

package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;

public interface Polygon {

    /**
     * Get TravelType of Polygon.
     * @return Travel type associated with the Polygon
     */
    public TravelType getTravelType();

    /**
     * Set a travel type for the Polygon.
     * @param travelType Travel type to be associated with the Polygon.
     */
    public void setTravelType(final TravelType travelType);

    /**
     * Get the ID associated with the Polygon.
     * @return Polygon ID
     */
    public String getId();

    /**
     * Assign an ID to the Polygon
     * @param id ID to be assigned
     */
    public void setId(final String id);

    /**
     * Get the GeoJSON string of the geometry.
     * @return Geometry as a GeoJSON String
     */
    public String getGeojson();

    /**
     * Set the GeoJSON string of the geometry.
     * @param geojson Geometry as a GeoJSON String
     */
    public void setGeojson(final String geojson);

    /**
     * Get Coordinate Reference System (CRS) Value
     * @return CRS value
     */
    public int getCrs();

    /**
     * Set the Coordinate Reference System (CRS) Value
     * @param crs CRS value to be set
     */
    public void setCrs(final int crs);
}

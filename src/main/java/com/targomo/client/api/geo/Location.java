package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;

/**
 * Contains ID and Travel type for both Coordinates and Polygons
 */
public interface Location {

    /**
     * Get TravelType of coordinate.
     * @return Travel type associated with the coordinate
     */
    public TravelType getTravelType();

    /**
     * Set a travel type for the coordinate.
     * @param travelType Travel type to be associated with the coordinate.
     */
    public void setTravelType(final TravelType travelType);

    /**
     * Get the ID associated with the coordinate.
     * @return Coordinate ID
     */
    public String getId();

    /**
     * Assign an ID to the coordinate
     * @param id ID to be assigned
     */
    public void setId(final String id);

}

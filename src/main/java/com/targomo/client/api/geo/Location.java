package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.pojo.LocationProperties;

import javax.persistence.Column;
import java.util.Collections;
import java.util.List;

/**
 * Contains ID and Travel type for both Coordinates and Polygons
 */
public interface Location {

    /**
     * Get the travel type(s) associated with the location.
     * If there is more than one element in the travelTypes list, multi modal routing will be used.
     */
    public List<TravelType> getTravelTypes();

    /**
     * Set the travel type(s) to use when routing.
     * If there is more than one element in the travelTypes list, multi modal routing will be used.
     */
    public void setTravelTypes(List<TravelType> travelTypes);

    /**
     * Set the travel type to use when routing.
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

    /**
     * Get the properties of a location
     * @return Location Properties
     */
    public LocationProperties getProperties();

    /**
     * Assign properties to a location
     * @param locationProperties location properties to be assigned
     */
    public void setProperties(final LocationProperties locationProperties);

}

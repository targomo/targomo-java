package com.targomo.client.api.geo;

import com.targomo.client.api.pojo.LocationProperties;

public abstract class AbstractLocation {

    protected String id;
    protected LocationProperties properties;

    // needed for jackson
    public AbstractLocation(){}


    public AbstractLocation(String id, LocationProperties properties){
        this.id = id;
        this.properties = properties;
    }

    /**
     * Get the ID associated with the location.
     * @return Location ID
     */
    public String getId() {
        return id;
    }

    /**
     * Assign an ID to the location
     * @param id ID to be assigned
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get the properties of a location
     * @return Location Properties
     */
    public LocationProperties getProperties() { return this.properties; }

    /**
     * Assign properties to a location
     * @param properties location properties to be assigned
     */
    public void setProperties(final LocationProperties properties){ this.properties = properties; }

}

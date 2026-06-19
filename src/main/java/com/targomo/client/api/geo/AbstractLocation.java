package com.targomo.client.api.geo;

import com.targomo.client.api.pojo.LocationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractLocation {

    protected String id;
    protected LocationProperties properties;

    // needed for jackson
    public AbstractLocation(){}


    public AbstractLocation(String id, LocationProperties properties){
        this.id = id;
        this.properties = properties;
    }
}

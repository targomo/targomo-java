package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

public enum TravelType {

    UNSPECIFIED("unspecified"),
    WALK("walk"),
    BIKE("bike"),
    TRANSIT("transit"),
    WALKTRANSIT("walktransit"),
    BIKETRANSIT("biketransit"),
    CAR("car");

    private String key;

    TravelType(String key) {
        this.key = key;
    }

    @JsonCreator
    public static TravelType fromString(String key) {
        return key == null ? null : Stream.of(TravelType.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " + 
                        TravelType.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    public String toString(){
        return name().toLowerCase(); 
    }
    
    public static TravelType parse(String travelType) {
        
        switch ( travelType.toLowerCase() ) {
            case Constants.TRAVEL_TYPE_WALK: return WALK;
            case Constants.TRAVEL_TYPE_BIKE: return BIKE;
            case Constants.TRAVEL_TYPE_CAR: return CAR;
            case Constants.TRAVEL_TYPE_TRANSIT: return TRANSIT;
            case Constants.TRAVEL_TYPE_WALKTRANSIT: return WALKTRANSIT;
            case Constants.TRAVEL_TYPE_BIKETRANSIT: return BIKETRANSIT;
            default: return TravelType.UNSPECIFIED;
        }
    }

    public boolean isTransit() {
        // TODO Auto-generated method stub
        return this.toString().toLowerCase().contains("transit");
    }
}

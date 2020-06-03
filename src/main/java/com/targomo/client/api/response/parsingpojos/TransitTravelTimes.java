package com.targomo.client.api.response.parsingpojos;

import com.targomo.client.api.pojo.TransitTravelTimeValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class TransitTravelTimes extends ElementWithId {

    @Getter @Setter
    private TransitTravelTimeValue[] travelTimesTransit;

    @Getter @Setter
    private int travelTimeWalk;

    @Getter @Setter
    private int travelDistanceWalk;
}

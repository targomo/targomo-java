package com.targomo.client.api.pojo;

import com.targomo.client.api.TravelOptions;
import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;

@ToString
public class CompetingRoutingOption {

    public final TravelOptions routingOptions; //TODO preferably this could be a smaller object with really just routing options included. I think this could be quite helpful in many other projects TravelOptions could inherit form this object
    public final int routingValueOffset;
    public final float routingValueMultiplier;

    @Builder
    public CompetingRoutingOption(@NonNull TravelOptions routingOptions, Integer routingValueOffset, Float routingValueMultiplier) {
        this.routingOptions = routingOptions;
        this.routingValueOffset = routingValueOffset == null ? 0 : routingValueOffset;
        this.routingValueMultiplier = routingValueMultiplier == null ? 1.0f : routingValueMultiplier;
    }
}

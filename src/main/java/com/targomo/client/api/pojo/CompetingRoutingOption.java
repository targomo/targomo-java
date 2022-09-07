package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@EqualsAndHashCode
public class CompetingRoutingOption {

    public final RoutingOptions routingOptions;
    public final int routingValueOffset;
    public final float routingValueMultiplier;

    @Builder
    @JsonCreator
    public CompetingRoutingOption(@JsonProperty("routingOptions") RoutingOptions routingOptions,
                                  @JsonProperty("routingValueOffset") Integer routingValueOffset,
                                  @JsonProperty("routingValueMultiplier") Float routingValueMultiplier) {
        this.routingOptions = routingOptions == null ? new RoutingOptions() : routingOptions;
        this.routingValueOffset = routingValueOffset == null ? 0 : routingValueOffset;
        this.routingValueMultiplier = routingValueMultiplier == null ? 1.0f : routingValueMultiplier;
    }
}

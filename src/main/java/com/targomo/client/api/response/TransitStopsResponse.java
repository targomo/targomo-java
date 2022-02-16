package com.targomo.client.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransitStopsResponse {
    private String name;
    private String edgeType;
    private int edgeWeight;
    private Set<ReachableTransitStopTime> nextStops;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReachableTransitStopTime {
        private int time;
        private String name;
        private String line;
        private String endStation;
    }
}
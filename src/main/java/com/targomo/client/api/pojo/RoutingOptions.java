package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.targomo.client.api.enums.TravelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoutingOptions {

    private Integer maxEdgeWeight;
    private TravelType travelType;
    private Map<String,Double> travelTimeFactors;
    private Boolean reverse;
    private Boolean rushHour;
    private Integer time;
    private Integer date;
    private Integer frame;
    private Boolean elevationEnabled;
    private Double bikeSpeed;
    private Double bikeDownhill;
    private Double bikeUphill;
    private Double walkSpeed;
    private Double walkDownhill;
    private Double walkUphill;
    private Integer trafficJunctionPenalty;
    private Integer trafficSignalPenalty;
    private Integer trafficLeftTurnPenalty;
    private Integer trafficRightTurnPenalty;
    private Integer maxTransfers;
    private Integer maxWalkingTimeFromSource;
    private Integer maxWalkingTimeToTarget;
    private List<Integer> avoidTransitRouteTypes;
}

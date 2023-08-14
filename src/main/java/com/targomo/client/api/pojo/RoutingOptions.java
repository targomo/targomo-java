package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.targomo.client.api.enums.TravelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutingOptions {

    private Integer maxEdgeWeight;
    private TravelType travelType;
    private Map<String,Double> travelTimeFactors;
    private Boolean reverse;
    private Boolean rushHour;
    private Integer time;
    private Integer date;
    private Integer frame;
    private Integer arrivalOrDepartureDuration;
    private Boolean elevationEnabled;
    private Double bikeSpeed;
    private Double bikeDownhill;
    private Double bikeUphill;
    private Double walkSpeed;
    private Double walkDownhill;
    private Double walkUphill;
    private Boolean allowPrivateAndServiceRoads;
    private Integer trafficJunctionPenalty;
    private Integer trafficSignalPenalty;
    private Integer trafficLeftTurnPenalty;
    private Integer trafficRightTurnPenalty;
    private Integer maxTransfers;
    private Integer maxWalkingTimeFromSource;
    private Integer maxWalkingTimeToTarget;
    private List<Integer> avoidTransitRouteTypes;
    private Integer maxSnapDistance;
    private List<Integer> excludeEdgeClassesFromSnapping;
    private Boolean useAreaSnapping;
    private Integer snapRadius;
    private Boolean includeSnapDistance;

    @Override //overriding hash code since the has would change with each execution for enums (travelType)
    public int hashCode(){
        return Objects.hash(maxEdgeWeight, travelType.getKey(), travelTimeFactors, reverse, rushHour, time, date, frame, arrivalOrDepartureDuration,
                elevationEnabled, bikeSpeed, bikeDownhill, bikeUphill, walkSpeed, walkDownhill, walkUphill, allowPrivateAndServiceRoads,
                trafficJunctionPenalty, trafficSignalPenalty, trafficLeftTurnPenalty, trafficRightTurnPenalty,
                maxTransfers, maxWalkingTimeFromSource, maxWalkingTimeToTarget, avoidTransitRouteTypes,
                maxSnapDistance, excludeEdgeClassesFromSnapping, useAreaSnapping, snapRadius, includeSnapDistance);
    }
}

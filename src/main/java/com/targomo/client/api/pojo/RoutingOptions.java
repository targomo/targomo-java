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
    // Currently multi modal routing is not supported in competing routing options
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
    private Boolean includeSnapDistanceForTargets;

    @Override //overriding hash code since the has would change with each execution for enums (travelType)
    public int hashCode(){
        return Objects.hash(maxEdgeWeight, travelType.ordinal(), travelTimeFactors, reverse, rushHour, time, date, frame, arrivalOrDepartureDuration,
                elevationEnabled, bikeSpeed, bikeDownhill, bikeUphill, walkSpeed, walkDownhill, walkUphill, allowPrivateAndServiceRoads,
                trafficJunctionPenalty, trafficSignalPenalty, trafficLeftTurnPenalty, trafficRightTurnPenalty,
                maxTransfers, maxWalkingTimeFromSource, maxWalkingTimeToTarget, avoidTransitRouteTypes,
                maxSnapDistance, excludeEdgeClassesFromSnapping, useAreaSnapping, snapRadius, includeSnapDistance, includeSnapDistanceForTargets);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingOptions that = (RoutingOptions) o;
        return Objects.equals(maxEdgeWeight, that.maxEdgeWeight) && travelType == that.travelType && Objects.equals(travelTimeFactors, that.travelTimeFactors)
                && Objects.equals(reverse, that.reverse) && Objects.equals(rushHour, that.rushHour) && Objects.equals(time, that.time)
                && Objects.equals(date, that.date) && Objects.equals(frame, that.frame) && Objects.equals(arrivalOrDepartureDuration, that.arrivalOrDepartureDuration)
                && Objects.equals(elevationEnabled, that.elevationEnabled) && Objects.equals(bikeSpeed, that.bikeSpeed) && Objects.equals(bikeDownhill, that.bikeDownhill)
                && Objects.equals(bikeUphill, that.bikeUphill) && Objects.equals(walkSpeed, that.walkSpeed) && Objects.equals(walkDownhill, that.walkDownhill)
                && Objects.equals(walkUphill, that.walkUphill) && Objects.equals(allowPrivateAndServiceRoads, that.allowPrivateAndServiceRoads)
                && Objects.equals(trafficJunctionPenalty, that.trafficJunctionPenalty) && Objects.equals(trafficSignalPenalty, that.trafficSignalPenalty)
                && Objects.equals(trafficLeftTurnPenalty, that.trafficLeftTurnPenalty) && Objects.equals(trafficRightTurnPenalty, that.trafficRightTurnPenalty)
                && Objects.equals(maxTransfers, that.maxTransfers) && Objects.equals(maxWalkingTimeFromSource, that.maxWalkingTimeFromSource)
                && Objects.equals(maxWalkingTimeToTarget, that.maxWalkingTimeToTarget) && Objects.equals(avoidTransitRouteTypes, that.avoidTransitRouteTypes)
                && Objects.equals(maxSnapDistance, that.maxSnapDistance) && Objects.equals(excludeEdgeClassesFromSnapping, that.excludeEdgeClassesFromSnapping)
                && Objects.equals(useAreaSnapping, that.useAreaSnapping) && Objects.equals(snapRadius, that.snapRadius) && Objects.equals(includeSnapDistance, that.includeSnapDistance)
                && Objects.equals(includeSnapDistanceForTargets, that.includeSnapDistanceForTargets);
    }

}

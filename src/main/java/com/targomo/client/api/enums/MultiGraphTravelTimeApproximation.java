package com.targomo.client.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MultiGraphTravelTimeApproximation {

    NONE                                           (false, false),
    USING_CACHED                                   (true,  false),
    OUTLIER_PENALTY_ESTIMATION                     (false, true),
    OUTLIER_PENALTY_ESTIMATION_AND_USING_CACHED    (true,  true);

    private final boolean allowUsingCached;         //if this is true RoutingSourceData will allow to use values that were cached for a higher maxEdgeWeight
    private final boolean basedOnOutlierEstimate;   //if this is true RoutingSourceData estimates an outlier value based on the received data
}

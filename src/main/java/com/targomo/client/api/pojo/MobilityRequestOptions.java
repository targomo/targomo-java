package com.targomo.client.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Parameters for an edge statistics request.
 */
@Getter @AllArgsConstructor
public class MobilityRequestOptions {
    private final String mobilityServiceUrl;

    private final Integer radius;

    private Integer minDuration;
    private Integer maxDuration;

    private final Integer hourStart;
    private final Integer hourEnd;

    private final Integer dayStart;
    private final Integer dayEnd;

    private final Integer dayOfYearStart;
    private final Integer dayOfYearEnd;

    private final Boolean unique;
    private final Boolean exact;
    private final Boolean excludeNightLocations;

    private final String apiKey;
}

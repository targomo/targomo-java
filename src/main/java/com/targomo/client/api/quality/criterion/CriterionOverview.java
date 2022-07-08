package com.targomo.client.api.quality.criterion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CriterionOverview {
    private final double[] kMeansValueBreaks;
    private final Double minValue;
    private final Double maxValue;
}

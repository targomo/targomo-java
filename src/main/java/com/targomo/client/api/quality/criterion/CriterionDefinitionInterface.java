package com.targomo.client.api.quality.criterion;

public interface CriterionDefinitionInterface {
    CriterionType getType();

    Double getDistanceExponent();

    Double getScalingFactor();

    Double getDistanceModifier();

    Double getLowerBound();
}

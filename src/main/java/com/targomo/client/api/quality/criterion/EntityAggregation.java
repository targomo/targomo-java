package com.targomo.client.api.quality.criterion;

/**
 * Defines how the scores of each entity (absolute value) will be aggregated to calculate the score of one location
 * @author Hugo Lucas
 */
public enum EntityAggregation {
    // only keeps the entity with the minimum time/distance
    MIN("min"),
    // sums all entity scores
    SUM("sum"),
    // averages all entity scores
    MEAN("mean");

    private final String name;

    EntityAggregation(String name) {
        this.name = name;
    }
}

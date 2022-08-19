package com.targomo.client.api.quality.criterion;

/**
 * Defines how the distance/time will influences on the score
 * @author Hugo Lucas
 */
public enum DistanceInfluence {
    NONE("none"),
    QUADRATIC("quadratic");
    //TODO: LINEAR("linear")

    private final String name;

    DistanceInfluence(String name) {
        this.name = name;
    }
}

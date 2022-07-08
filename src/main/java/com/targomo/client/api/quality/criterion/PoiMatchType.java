package com.targomo.client.api.quality.criterion;

/**
 * Checks for combination of poi tags. match=any or match=all.
 * @author Brian Pondi   - 09/11/2021
 */
public enum PoiMatchType {
    //The service will retrieve all POIs that contain at least one tag
    ANY("any"),
    //The service will retrieve all POIs that contain all tags
    ALL("all");

    private final String name;

    PoiMatchType(String name) {
        this.name = name;
    }
}

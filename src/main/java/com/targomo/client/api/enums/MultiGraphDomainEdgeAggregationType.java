package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

/**
 * The currently implemented aggregations for edges that are supported by the multi graph request.
 */
public enum MultiGraphDomainEdgeAggregationType {

    MIN(Constants.KEY_MULTIGRAPH_DOMAIN_EDGE_AGGREGATION_TYPE_MINIMUM),
    MAX(Constants.KEY_MULTIGRAPH_DOMAIN_EDGE_AGGREGATION_TYPE_MAXIMUM),
    MEAN(Constants.KEY_MULTIGRAPH_DOMAIN_EDGE_AGGREGATION_TYPE_MEAN);

    private String key;

    MultiGraphDomainEdgeAggregationType(String key) {
        this.key = key;
    }

    @JsonCreator
    public static MultiGraphDomainEdgeAggregationType fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphDomainEdgeAggregationType.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        MultiGraphDomainEdgeAggregationType.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

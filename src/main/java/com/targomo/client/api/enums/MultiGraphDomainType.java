package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.targomo.client.Constants;

import java.util.stream.Stream;

/**
 * The currently implemented domain types (on which the aggregation is based on) that are supported by the multi graph request.
 */
public enum MultiGraphDomainType {

    NODE                (Constants.KEY_MULTIGRAPH_DOMAIN_TYPE_NODE, false),
    EDGE                (Constants.KEY_MULTIGRAPH_DOMAIN_TYPE_EDGE, false),
    STATISTIC_GEOMETRY  (Constants.KEY_MULTIGRAPH_DOMAIN_TYPE_STATISTIC_GEOMETRY, true);

    private final String key;
    private final boolean onlyStatistics;

    MultiGraphDomainType(String key, boolean onlyStatistics) {
        this.key            = key;
        this.onlyStatistics = onlyStatistics;
    }

    @JsonCreator
    public static MultiGraphDomainType fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphDomainType.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid " +
                        MultiGraphDomainType.class.getSimpleName() + " specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    @JsonIgnore
    public boolean isOnlyStatistics(){
        return onlyStatistics;
    }
}

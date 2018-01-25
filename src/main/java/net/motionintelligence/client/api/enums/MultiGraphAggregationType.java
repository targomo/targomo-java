package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import net.motionintelligence.client.Constants;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.Arrays;
import java.util.stream.Stream;

public enum MultiGraphAggregationType {

    NONE(Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NONE),
    MINIMUM(Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MINIMUM),
    MAXIMUM(Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MAXIMUM),
    SUM(Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_SUM),
    MEAN(Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEAN),
    MEDIAN(Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_MEDIAN),
    NEAREST(Constants.KEY_MULTIGRAPH_AGGREGATION_TYPE_NEAREST);

    private String key;

    MultiGraphAggregationType(String key) {
		this.key = key;
	}

    @JsonCreator
    public static MultiGraphAggregationType fromString(String key) {
        return key == null ? null : Stream.of(MultiGraphAggregationType.values())
                .filter( enu -> enu.key.equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new InvalidStateException("Invalid MultiGraphAggregationType specified: " + key + " doesn't exist"));
    }

    @JsonValue
    public String getKey() {
		return key;
	}
}

package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import net.motionintelligence.client.Constants;

import java.util.Locale;

public enum MultiGraphLayerEdgeAggregationType {

    MIN(Constants.KEY_MULTIGRAPH_LAYER_EDGE_AGGREGATION_TYPE_MINIMUM),
    MAX(Constants.KEY_MULTIGRAPH_LAYER_EDGE_AGGREGATION_TYPE_MAXIMUM),
    MEAN(Constants.KEY_MULTIGRAPH_LAYER_EDGE_AGGREGATION_TYPE_MEAN);

    private String key;

    MultiGraphLayerEdgeAggregationType(String key) {
		this.key = key;
	}

    @JsonCreator
    public static MultiGraphLayerEdgeAggregationType fromString(String key) {
        return key == null ? null : MultiGraphLayerEdgeAggregationType.valueOf(key.toUpperCase(Locale.ENGLISH));
    }

    @JsonValue
    public String getKey() {
		return key;
	}
}

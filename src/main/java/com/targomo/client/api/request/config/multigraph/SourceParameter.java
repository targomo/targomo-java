package com.targomo.client.api.request.config.multigraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceParameter {
    private Integer factor;

    @JsonCreator
    public SourceParameter(@JsonProperty(Constants.MULTIGRAPH_AGGREGATION_FACTOR) Integer factor) {
        this.factor = factor == null ? 1 : factor;
    }

    public Integer getFactor() {
        return factor;
    }
}
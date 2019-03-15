package com.targomo.client.api.request.config.multigraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.Constants;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceParameter {
    private Float factor;

    @JsonCreator
    public SourceParameter(@JsonProperty(Constants.MULTIGRAPH_AGGREGATION_FACTOR) Float factor) {
        this.factor = factor == null ? 1f : factor;
    }

    public Float getFactor() {
        return factor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SourceParameter that = (SourceParameter) o;

        return new EqualsBuilder()
                .append(factor, that.factor)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(factor)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "SourceParameter{" +
                "factor=" + factor +
                '}';
    }
}
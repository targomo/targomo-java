package com.targomo.client.api.request.config.multigraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.Constants;
import com.targomo.client.api.enums.MultiGraphAggregationType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * POJO to define pipeline aggregations for the multigraph.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AggregationConfig {
    private MultiGraphAggregationType type;
    private Boolean ignoreOutlier;
    private Integer outlierPenalty;
    private Double minSourcesRatio;
    private Integer minSourcesCount;
    private Double maxResultValueRatio;
    private Integer maxResultValue;
    private Set<String> filterValuesForSourceOrigins;
    private Map<String, SourceParameter> sourceParameters = new LinkedHashMap<>();

    public AggregationConfig() {
    }

    @JsonCreator
    public AggregationConfig(@JsonProperty(Constants.MULTIGRAPH_AGGREGATION_TYPE) MultiGraphAggregationType type,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_IGNORE_OUTLIERS) Boolean ignoreOutlier,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_OUTLIER_PENALTY) Integer outlierPenalty,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_MIN_SOURCES_RATIO) Double minSourcesRatio,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_MIN_SOURCES_COUNT) Integer minSourcesCount,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE_RATIO) Double maxResultValueRatio,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE) Integer maxResultValue,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_SOURCE_PARAMETERS) Map<String, SourceParameter> sourceParameters,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_FILTER_VALUES_FOR_SOURCE_ORIGINS) Set<String> filterValuesForSourceOrigins) {
        this.type = type;
        this.ignoreOutlier = ignoreOutlier;
        this.outlierPenalty = outlierPenalty;
        this.minSourcesRatio = minSourcesRatio;
        this.minSourcesCount = minSourcesCount;
        this.maxResultValueRatio = maxResultValueRatio;
        this.maxResultValue = maxResultValue;
        this.sourceParameters = sourceParameters;
        this.filterValuesForSourceOrigins = filterValuesForSourceOrigins;
    }

    public static AggregationConfigBuilder builder() {
        return new AggregationConfigBuilder();
    }

    public MultiGraphAggregationType getType() {
        return type;
    }

    public void setType(MultiGraphAggregationType type) {
        this.type = type;
    }

    public Boolean isIgnoreOutlier() {
        return ignoreOutlier;
    }

    public Integer getOutlierPenalty() {
        return outlierPenalty;
    }

    public void setOutlierPenalty(Integer outlierPenalty) {
        this.outlierPenalty = outlierPenalty;
    }

    public Double getMinSourcesRatio() {
        return minSourcesRatio;
    }

    public void setMinSourcesRatio(Double minSourcesRatio) {
        this.minSourcesRatio = minSourcesRatio;
    }

    public Integer getMinSourcesCount() {
        return minSourcesCount;
    }

    public void setMinSourcesCount(Integer minSourcesCount) {
        this.minSourcesCount = minSourcesCount;
    }

    public Double getMaxResultValueRatio() {
        return maxResultValueRatio;
    }

    public void setMaxResultValueRatio(Double maxResultValueRatio) {
        this.maxResultValueRatio = maxResultValueRatio;
    }

    public Integer getMaxResultValue() {
        return maxResultValue;
    }

    public void setMaxResultValue(Integer maxResultValue) {
        this.maxResultValue = maxResultValue;
    }

    public Map<String, SourceParameter> getSourceParameters() {
        return sourceParameters;
    }

    public void setSourceParameters(Map<String, SourceParameter> sourceParameters) {
        this.sourceParameters = sourceParameters;
    }

    public void setIgnoreOutlier(Boolean ignoreOutlier) {
        this.ignoreOutlier = ignoreOutlier;
    }

    public Set<String> getFilterValuesForSourceOrigins() {
        return filterValuesForSourceOrigins;
    }

    public void setFilterValuesForSourceOrigins(Set<String> filterValuesForSourceOrigins) {
        this.filterValuesForSourceOrigins = filterValuesForSourceOrigins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AggregationConfig that = (AggregationConfig) o;

        return new EqualsBuilder()
                .append(ignoreOutlier, that.ignoreOutlier)
                .append(minSourcesCount, that.minSourcesCount)
                .append(type, that.type)
                .append(outlierPenalty, that.outlierPenalty)
                .append(minSourcesRatio, that.minSourcesRatio)
                .append(maxResultValueRatio, that.maxResultValueRatio)
                .append(maxResultValue, that.maxResultValue)
                .append(filterValuesForSourceOrigins, that.filterValuesForSourceOrigins)
                .append(sourceParameters, that.sourceParameters)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(ignoreOutlier)
                .append(outlierPenalty)
                .append(minSourcesRatio)
                .append(minSourcesCount)
                .append(maxResultValueRatio)
                .append(maxResultValue)
                .append(filterValuesForSourceOrigins)
                .append(sourceParameters)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "AggregationConfig{" +
                "type=" + type +
                ", ignoreOutlier=" + ignoreOutlier +
                ", outlierPenalty=" + outlierPenalty +
                ", minSourcesRatio=" + minSourcesRatio +
                ", minSourcesCount=" + minSourcesCount +
                ", maxResultValueRatio=" + maxResultValueRatio +
                ", maxResultValue=" + maxResultValue +
                ", filterValuesForSourceOrigins=" + filterValuesForSourceOrigins +
                ", sourceParameters=" + sourceParameters +
                '}';
    }

    public static final class AggregationConfigBuilder {
        private MultiGraphAggregationType type;
        private Boolean ignoreOutlier;
        private Integer outlierPenalty;
        private Double minSourcesRatio;
        private Integer minSourcesCount;
        private Double maxResultValueRatio;
        private Integer maxResultValue;
        private Set<String> filterValuesForSourceOrigins;
        private Map<String, SourceParameter> sourceParameters = new LinkedHashMap<>();

        private AggregationConfigBuilder() {
        }

        public AggregationConfigBuilder type(MultiGraphAggregationType type) {
            this.type = type;
            return this;
        }

        public AggregationConfigBuilder ignoreOutlier(Boolean ignoreOutlier) {
            this.ignoreOutlier = ignoreOutlier;
            return this;
        }

        public AggregationConfigBuilder outlierPenalty(Integer outlierPenalty) {
            this.outlierPenalty = outlierPenalty;
            return this;
        }

        public AggregationConfigBuilder minSourcesRatio(Double minSourcesRatio) {
            this.minSourcesRatio = minSourcesRatio;
            return this;
        }

        public AggregationConfigBuilder minSourcesCount(Integer minSourcesCount) {
            this.minSourcesCount = minSourcesCount;
            return this;
        }

        public AggregationConfigBuilder maxResultValueRatio(Double maxResultValueRatio) {
            this.maxResultValueRatio = maxResultValueRatio;
            return this;
        }

        public AggregationConfigBuilder maxResultValue(Integer maxResultValue) {
            this.maxResultValue = maxResultValue;
            return this;
        }

        public AggregationConfigBuilder filterValuesForSourceOrigins(Set<String> filterValuesForSourceOrigins) {
            this.filterValuesForSourceOrigins = filterValuesForSourceOrigins;
            return this;
        }

        public AggregationConfigBuilder sourceParameters(Map<String, SourceParameter> sourceParameters) {
            this.sourceParameters = sourceParameters;
            return this;
        }

        public AggregationConfig build() {
            return new AggregationConfig(type, ignoreOutlier, outlierPenalty, minSourcesRatio, minSourcesCount, maxResultValueRatio, maxResultValue, sourceParameters, filterValuesForSourceOrigins);
        }
    }
}


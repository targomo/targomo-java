package com.targomo.client.api.request.config.multigraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.Constants;
import com.targomo.client.api.enums.MultiGraphAggregationType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AggregationConfig {
    private MultiGraphAggregationType type;
    private boolean ignoreOutlier;
    private Integer outlierPenalty;
    private Double minSourcesRatio;
    private int minSourcesCount;
    private Double maxResultValueRatio;
    private Integer maxResultValue;
    private Map<String, SourceParameter> sourceParameters = new LinkedHashMap<>();

    public AggregationConfig() {
    }

    @JsonCreator
    public AggregationConfig(@JsonProperty(Constants.MULTIGRAPH_AGGREGATION_TYPE) MultiGraphAggregationType type,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_IGNORE_OUTLIERS) boolean ignoreOutlier,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_OUTLIER_PENALTY) Integer outlierPenalty,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_MIN_SOURCES_RATIO) Double minSourcesRatio,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_MIN_SOURCES_COUNT) int minSourcesCount,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE_RATIO) Double maxResultValueRatio,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE) Integer maxResultValue,
                             @JsonProperty(Constants.MULTIGRAPH_AGGREGATION_SOURCE_PARAMETERS) Map<String, SourceParameter> sourceParameters) {
        this.type = type;
        this.ignoreOutlier = ignoreOutlier;
        this.outlierPenalty = outlierPenalty;
        this.minSourcesRatio = minSourcesRatio;
        this.minSourcesCount = minSourcesCount;
        this.maxResultValueRatio = maxResultValueRatio;
        this.maxResultValue = maxResultValue;
        this.sourceParameters = sourceParameters;
    }

    public MultiGraphAggregationType getType() {
        return type;
    }

    public boolean isIgnoreOutlier() {
        return ignoreOutlier;
    }

    public Integer getOutlierPenalty() {
        return outlierPenalty;
    }

    public Double getMinSourcesRatio() {
        return minSourcesRatio;
    }

    public int getMinSourcesCount() {
        return minSourcesCount;
    }

    public Double getMaxResultValueRatio() {
        return maxResultValueRatio;
    }

    public Integer getMaxResultValue() {
        return maxResultValue;
    }

    public Map<String, SourceParameter> getSourceParameters() {
        return sourceParameters;
    }

    public void setType(MultiGraphAggregationType type) {
        this.type = type;
    }

    public void setIgnoreOutlier(boolean ignoreOutlier) {
        this.ignoreOutlier = ignoreOutlier;
    }

    public void setOutlierPenalty(Integer outlierPenalty) {
        this.outlierPenalty = outlierPenalty;
    }

    public void setMinSourcesRatio(Double minSourcesRatio) {
        this.minSourcesRatio = minSourcesRatio;
    }

    public void setMinSourcesCount(int minSourcesCount) {
        this.minSourcesCount = minSourcesCount;
    }

    public void setMaxResultValueRatio(Double maxResultValueRatio) {
        this.maxResultValueRatio = maxResultValueRatio;
    }

    public void setMaxResultValue(Integer maxResultValue) {
        this.maxResultValue = maxResultValue;
    }

    public void setSourceParameters(Map<String, SourceParameter> sourceParameters) {
        this.sourceParameters = sourceParameters;
    }

    public static PreAggregationConfigBuilder builder() {
        return new PreAggregationConfigBuilder();
    }

    public static final class PreAggregationConfigBuilder {
        private MultiGraphAggregationType type;
        private boolean ignoreOutlier;
        private Integer outlierPenalty;
        private Double minSourcesRatio;
        private int minSourcesCount;
        private Double maxResultValueRatio;
        private Integer maxResultValue;
        private Map<String, SourceParameter> sourceParameters = new HashMap<>();

        private PreAggregationConfigBuilder() {
        }

        public PreAggregationConfigBuilder type(MultiGraphAggregationType type) {
            this.type = type;
            return this;
        }

        public PreAggregationConfigBuilder ignoreOutlier(boolean ignoreOutlier) {
            this.ignoreOutlier = ignoreOutlier;
            return this;
        }

        public PreAggregationConfigBuilder outlierPenalty(Integer outlierPenalty) {
            this.outlierPenalty = outlierPenalty;
            return this;
        }

        public PreAggregationConfigBuilder minSourcesRatio(Double minSourcesRatio) {
            this.minSourcesRatio = minSourcesRatio;
            return this;
        }

        public PreAggregationConfigBuilder minSourcesCount(int minSourcesCount) {
            this.minSourcesCount = minSourcesCount;
            return this;
        }

        public PreAggregationConfigBuilder maxResultValueRatio(Double maxResultValueRatio) {
            this.maxResultValueRatio = maxResultValueRatio;
            return this;
        }

        public PreAggregationConfigBuilder maxResultValue(Integer maxResultValue) {
            this.maxResultValue = maxResultValue;
            return this;
        }

        public PreAggregationConfigBuilder sourceParameters(Map<String, SourceParameter> sourceParameters) {
            this.sourceParameters = sourceParameters;
            return this;
        }

        public AggregationConfig build() {
            AggregationConfig aggregationConfig = new AggregationConfig();
            aggregationConfig.type = this.type;
            aggregationConfig.maxResultValue = this.maxResultValue;
            aggregationConfig.outlierPenalty = this.outlierPenalty;
            aggregationConfig.minSourcesCount = this.minSourcesCount;
            aggregationConfig.ignoreOutlier = this.ignoreOutlier;
            aggregationConfig.minSourcesRatio = this.minSourcesRatio;
            aggregationConfig.sourceParameters = this.sourceParameters;
            aggregationConfig.maxResultValueRatio = this.maxResultValueRatio;
            return aggregationConfig;
        }
    }
}


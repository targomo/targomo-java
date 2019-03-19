package com.targomo.client.api.pojo;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.MultiGraphAggregationType;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * POJO to define pipeline aggregations for the multigraph.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(includeFieldNames=true)
public class AggregationConfiguration {

    // The specified type of the aggregation to be used on Multigraph layers. E.g.: mean, min, max, etc.
    private MultiGraphAggregationType type;
    // Whether or not layers with no value should be included in the aggregation
    private Boolean ignoreOutlier;
    private Float outlierPenalty;
    private Double minSourcesRatio;
    private Integer minSourcesCount;
    // The ratio which defines how many of the best result values should be included
    // (if set to 0.6 that means that 60% of the best/lowest results are included)
    private Double maxResultValueRatio;
    // The maximum value that should still be acceptable to be included into the aggregation result
    private Float maxResultValue;
    private Float accuracy;
    // Source origin ids (should be equal to layer ids) for filtering the elements.
    // Only elements whose origin (layer with the lowest value for the element id) is in this set
    // will be included in the aggregation. If null, the elements are not filtered by source origin.
    private Set<String> filterValuesForSourceOrigins;
    private Double gravitationExponent;
    private Map<String, SourceParameters> sourceParameters;

    public static class AggregationConfigurationBuilder {
        private MultiGraphAggregationType type;
        private Boolean ignoreOutlier;
        private Float outlierPenalty;
        private Double minSourcesRatio;
        private Integer minSourcesCount;
        private Double maxResultValueRatio;
        private Float maxResultValue;
        private Float accuracy;
        private Set<String> filterValuesForSourceOrigins;
        private Double gravitationExponent;
        private Map<String, SourceParameters> sourceParameters;

        public AggregationConfigurationBuilder() {}

        public AggregationConfigurationBuilder(AggregationConfiguration toCopy) {
            this.type = toCopy.type;
            this.ignoreOutlier = toCopy.ignoreOutlier;
            this.outlierPenalty = toCopy.outlierPenalty;
            this.minSourcesRatio = toCopy.minSourcesRatio;
            this.minSourcesCount = toCopy.minSourcesCount;
            this.maxResultValueRatio = toCopy.maxResultValueRatio;
            this.maxResultValue = toCopy.maxResultValue;
            this.filterValuesForSourceOrigins = Optional.ofNullable(toCopy.filterValuesForSourceOrigins).map(HashSet::new).orElse(null);
            this.gravitationExponent = toCopy.gravitationExponent;
            this.accuracy = toCopy.accuracy;
            this.sourceParameters = Optional.ofNullable(toCopy.sourceParameters)
                    .map(map -> map.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> new SourceParameters(
                                            entry.getValue().getInputFactor(),
                                            entry.getValue().getGravitationPositiveInfluence(),
                                            entry.getValue().getGravitationAttractionStrength()))))
                    .orElse(null);
        }

        public AggregationConfigurationBuilder(TravelOptions travelOptions) {
            this.type = travelOptions.getMultiGraphAggregationType();
            this.ignoreOutlier = Optional.ofNullable(travelOptions.getMultiGraphAggregationIgnoreOutlier()).orElse(false);
            this.outlierPenalty = travelOptions.getMultiGraphAggregationOutlierPenalty();
            this.minSourcesRatio = travelOptions.getMultiGraphAggregationMinSourcesRatio();
            this.minSourcesCount = Optional.ofNullable(travelOptions.getMultiGraphAggregationMinSourcesCount()).orElse(1);
            this.maxResultValueRatio = travelOptions.getMultiGraphAggregationMaxResultValueRatio();
            this.maxResultValue = travelOptions.getMultiGraphAggregationMaxResultValue();
            this.accuracy = travelOptions.getMultiGraphAggregationAccuracy();
            this.filterValuesForSourceOrigins = Optional.ofNullable(travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins())
                    .map(HashSet::new).orElse(null);
            this.gravitationExponent = travelOptions.getMultiGraphAggregationGravitationExponent();
            this.sourceParameters = Optional.ofNullable(travelOptions.getMultiGraphAggregationSourceParameters())
                    .map(map -> map.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> new SourceParameters(
                                            entry.getValue().getInputFactor(),
                                            entry.getValue().getGravitationPositiveInfluence(),
                                            entry.getValue().getGravitationAttractionStrength()))))
                    .orElse(null);
        }

        public AggregationConfigurationBuilder type(MultiGraphAggregationType type) {
            this.type = type;
            return this;
        }

        public AggregationConfigurationBuilder ignoreOutlier(Boolean ignoreOutlier) {
            this.ignoreOutlier = ignoreOutlier;
            return this;
        }

        public AggregationConfigurationBuilder outlierPenalty(Float outlierPenalty) {
            this.outlierPenalty = outlierPenalty;
            return this;
        }

        public AggregationConfigurationBuilder minSourcesRatio(Double minSourcesRatio) {
            this.minSourcesRatio = minSourcesRatio;
            return this;
        }

        public AggregationConfigurationBuilder minSourcesCount(Integer minSourcesCount) {
            this.minSourcesCount = minSourcesCount;
            return this;
        }

        public AggregationConfigurationBuilder maxResultValueRatio(Double maxResultValueRatio) {
            this.maxResultValueRatio = maxResultValueRatio;
            return this;
        }

        public AggregationConfigurationBuilder maxResultValue(Float maxResultValue) {
            this.maxResultValue = maxResultValue;
            return this;
        }

        public AggregationConfigurationBuilder filterValuesForSourceOrigins(Set<String> filterValuesForSourceOrigins) {
            this.filterValuesForSourceOrigins = filterValuesForSourceOrigins;
            return this;
        }

        public AggregationConfigurationBuilder gravitationExponent(Double gravitationTravelTimeExponent) {
            this.gravitationExponent = gravitationTravelTimeExponent;
            return this;
        }

        public AggregationConfigurationBuilder sourceParameters(Map<String, SourceParameters> sourceParameters) {
            this.sourceParameters = sourceParameters;
            return this;
        }

        public AggregationConfigurationBuilder accuracy(Float accuracy) {
            this.accuracy = accuracy;
            return this;
        }

        public AggregationConfiguration build() {
            return new AggregationConfiguration(type, ignoreOutlier, outlierPenalty, minSourcesRatio, minSourcesCount,
                    maxResultValueRatio, maxResultValue, accuracy, filterValuesForSourceOrigins, gravitationExponent,
                    sourceParameters);
        }
    }
}


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
    private Boolean ignoreOutliers;
    private Float outlierPenalty;
    private Double minSourcesRatio;
    private Integer minSourcesCount;
    private Float minSourcesValue;
    private Float maxSourcesValue;
    // The ratio which defines how many of the best result values should be included
    // (if set to 0.6 that means that 60% of the best/lowest results are included)
    private Double maxResultValueRatio;
    // The maximum value that should still be acceptable to be included into the aggregation result
    private Float maxResultValue;
    private Float postAggregationFactor;
    // Source origin ids (should be equal to layer ids) for filtering the elements.
    // Only elements whose origin (layer with the lowest value for the element id) is in this set
    // will be included in the aggregation. If null, the elements are not filtered by source origin.
    private Set<String> filterValuesForSourceOrigins;
    private Double gravitationExponent;
    private Map<String, AggregationInputParameters> aggregationInputParameters;
    private String mathExpression;

    public static class AggregationConfigurationBuilder {
        private MultiGraphAggregationType type;
        private Boolean ignoreOutliers;
        private Float outlierPenalty;
        private Double minSourcesRatio;
        private Integer minSourcesCount;
        private Float minSourcesValue;
        private Float maxSourcesValue;
        private Double maxResultValueRatio;
        private Float maxResultValue;
        private Double gravitationExponent;
        private Float postAggregationFactor;
        private Set<String> filterValuesForSourceOrigins;
        private Map<String, AggregationInputParameters> aggregationInputParameters;
        private String mathExpression;

        public AggregationConfigurationBuilder() {}

        public AggregationConfigurationBuilder(AggregationConfiguration toCopy) {
            this.type = toCopy.type;
            this.ignoreOutliers = toCopy.ignoreOutliers;
            this.outlierPenalty = toCopy.outlierPenalty;
            this.minSourcesRatio = toCopy.minSourcesRatio;
            this.minSourcesCount = toCopy.minSourcesCount;
            this.minSourcesValue = toCopy.minSourcesValue;
            this.maxSourcesValue = toCopy.maxSourcesValue;
            this.maxResultValueRatio = toCopy.maxResultValueRatio;
            this.maxResultValue = toCopy.maxResultValue;
            this.filterValuesForSourceOrigins = Optional.ofNullable(toCopy.filterValuesForSourceOrigins).map(HashSet::new).orElse(null);
            this.gravitationExponent = toCopy.gravitationExponent;
            this.postAggregationFactor = toCopy.postAggregationFactor;
            this.aggregationInputParameters = Optional.ofNullable(toCopy.aggregationInputParameters)
                    .map(map -> map.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> new AggregationInputParameters(
                                            entry.getValue().getInputFactor(),
                                            entry.getValue().getGravitationPositiveInfluence(),
                                            entry.getValue().getGravitationAttractionStrength()))))
                    .orElse(null);
            this.mathExpression = toCopy.mathExpression;
        }

        public AggregationConfigurationBuilder(TravelOptions travelOptions, boolean deepCopy) {
            this.type = travelOptions.getMultiGraphAggregationType();
            this.ignoreOutliers = travelOptions.getMultiGraphAggregationIgnoreOutliers();
            this.outlierPenalty = travelOptions.getMultiGraphAggregationOutlierPenalty();
            this.minSourcesRatio = travelOptions.getMultiGraphAggregationMinSourcesRatio();
            this.minSourcesCount = travelOptions.getMultiGraphAggregationMinSourcesCount();
            this.minSourcesValue = travelOptions.getMultiGraphAggregationMinSourcesValue();
            this.maxSourcesValue = travelOptions.getMultiGraphAggregationMaxSourcesValue();
            this.maxResultValueRatio = travelOptions.getMultiGraphAggregationMaxResultValueRatio();
            this.maxResultValue = travelOptions.getMultiGraphAggregationMaxResultValue();
            this.postAggregationFactor = travelOptions.getMultiGraphAggregationPostAggregationFactor();
            this.filterValuesForSourceOrigins = !deepCopy ? travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins() :
                    Optional.ofNullable(travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins()).map(HashSet::new).orElse(null);
            this.gravitationExponent = travelOptions.getMultiGraphAggregationGravitationExponent();
            this.aggregationInputParameters = !deepCopy ? travelOptions.getMultiGraphAggregationInputParameters() :
                    Optional.ofNullable(travelOptions.getMultiGraphAggregationInputParameters()).map(map ->
                            map.entrySet().stream().collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> new AggregationInputParameters(
                                            entry.getValue().getInputFactor(),
                                            entry.getValue().getGravitationPositiveInfluence(),
                                            entry.getValue().getGravitationAttractionStrength()))))
                    .orElse(null);
            this.mathExpression = travelOptions.getMultiGraphAggregationMathExpression();
        }

        public AggregationConfigurationBuilder type(MultiGraphAggregationType type) {
            this.type = type;
            return this;
        }

        public AggregationConfigurationBuilder ignoreOutliers(Boolean ignoreOutliers) {
            this.ignoreOutliers = ignoreOutliers;
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

        public AggregationConfigurationBuilder minSourcesValue(Float minSourcesValue) {
            this.minSourcesValue = minSourcesValue;
            return this;
        }

        public AggregationConfigurationBuilder maxSourcesValue(Float maxSourcesValue) {
            this.maxSourcesValue = maxSourcesValue;
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

        public AggregationConfigurationBuilder aggregationInputParameters(Map<String, AggregationInputParameters> aggregationInputParameters) {
            this.aggregationInputParameters = aggregationInputParameters;
            return this;
        }

        public AggregationConfigurationBuilder postAggregationFactor(Float postAggregationFactor) {
            this.postAggregationFactor = postAggregationFactor;
            return this;
        }

        public AggregationConfigurationBuilder mathExpression(String mathExpression) {
            this.mathExpression = mathExpression;
            return this;
        }

        public AggregationConfiguration build() {
            return new AggregationConfiguration(type, ignoreOutliers, outlierPenalty, minSourcesRatio, minSourcesCount,
                    minSourcesValue, maxSourcesValue, maxResultValueRatio, maxResultValue, postAggregationFactor,
                    filterValuesForSourceOrigins, gravitationExponent, aggregationInputParameters, mathExpression);
        }
    }
}


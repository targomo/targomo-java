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
    // for any type of special probability aggregations only minSourcesCount, sourceValuesLowerBound, sourceValuesUpperBound
    // and probability specific parameters such as gravitationExponent, probabilityDecay, logitBetaAttractionStrength/TravelTime are considered
    private MultiGraphAggregationType type;
    // Whether or not layers with no value should be included in the aggregation
    private Boolean ignoreOutliers; //specific to some aggregations
    private Float outlierPenalty;   //specific to some aggregations
    private Double minSourcesRatio; //no effect on routing union
    private Integer minSourcesCount;//no effect on routing union
    // a lower bound for input values can be set, i.e. v = v < sourceValuesLowerBound ? sourceValuesLowerBound : v
    private Float sourceValuesLowerBound;
    // an upper bound for input values can be set, i.e. v = v > sourceValuesUpperBound ? sourceValuesUpperBound : v;
    private Float sourceValuesUpperBound;
    // The ratio which defines how many of the highest result values should be included
    // (if set to 0.9 that means that 90% of the highest results are included)
    // default: 1.0; condition: 1 < minResultValueRatio + maxResultValueRatio <= 2.0)
    private Double minResultValueRatio;
    // The minimum value that should still be acceptable to be included into the aggregation result - if set it has to be <= maxResultValueRatio
    private Float minResultValue;
    // The ratio which defines how many of the lowest result values should be included
    // (if set to 0.6 that means that 60% of the lowest results are included)
    // default: 1.0; condition: 1 < minResultValueRatio + maxResultValueRatio <= 2.0)
    private Double maxResultValueRatio;
    // The maximum value that should still be acceptable to be included into the aggregation result - if set it has to be >= minResultValueRatio
    private Float maxResultValue;
    //This factor is applied to the output of the aggregation; (before the filtering is applied - see minResultValue, maxResultValue)
    private Float postAggregationFactor;
    // Source origin ids (should be equal to layer ids) for filtering the elements.
    // Only elements whose origin (layer with the lowest value for the element id) is in this set
    // will be included in the aggregation. If null, the elements are not filtered by source origin.
    private Set<String> filterValuesForSourceOrigins;
    private Double gravitationExponent;
    private Double probabilityDecay;
    private Double logitBetaAttractionStrength;
    private Double logitBetaTravelTime;
    private Map<String, AggregationInputParameters> aggregationInputParameters;
    private String mathExpression;

    public static class AggregationConfigurationBuilder {
        private MultiGraphAggregationType type;
        private Boolean ignoreOutliers;
        private Float outlierPenalty;
        private Double minSourcesRatio;
        private Integer minSourcesCount;
        private Float sourceValuesLowerBound;
        private Float sourceValuesUpperBound;
        private Double minResultValueRatio;
        private Float minResultValue;
        private Double maxResultValueRatio;
        private Float maxResultValue;
        private Double gravitationExponent;
        private Double probabilityDecay;
        private Double logitBetaAttractionStrength;
        private Double logitBetaTravelTime;
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
            this.sourceValuesLowerBound = toCopy.sourceValuesLowerBound;
            this.sourceValuesUpperBound = toCopy.sourceValuesUpperBound;
            this.minResultValueRatio = toCopy.minResultValueRatio;
            this.minResultValue = toCopy.minResultValue;
            this.maxResultValueRatio = toCopy.maxResultValueRatio;
            this.maxResultValue = toCopy.maxResultValue;
            this.filterValuesForSourceOrigins = Optional.ofNullable(toCopy.filterValuesForSourceOrigins).map(HashSet::new).orElse(null);
            this.gravitationExponent = toCopy.gravitationExponent;
            this.probabilityDecay = toCopy.probabilityDecay;
            this.logitBetaAttractionStrength = toCopy.logitBetaAttractionStrength;
            this.logitBetaTravelTime = toCopy.logitBetaTravelTime;
            this.postAggregationFactor = toCopy.postAggregationFactor;
            this.aggregationInputParameters = Optional.ofNullable(toCopy.aggregationInputParameters)
                    .map(map -> map.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> new AggregationInputParameters(
                                            entry.getValue().getInputFactor(),
                                            entry.getValue().getGravitationPositiveInfluence(),
                                            entry.getValue().getGravitationAttractionStrength(),
                                            entry.getValue().getGravitationCompetingPositiveInfluence()))))
                    .orElse(null);
            this.mathExpression = toCopy.mathExpression;
        }

        public AggregationConfigurationBuilder(TravelOptions travelOptions, boolean deepCopy) {
            this.type = travelOptions.getMultiGraphAggregationType();
            this.ignoreOutliers = travelOptions.getMultiGraphAggregationIgnoreOutliers();
            this.outlierPenalty = travelOptions.getMultiGraphAggregationOutlierPenalty();
            this.minSourcesRatio = travelOptions.getMultiGraphAggregationMinSourcesRatio();
            this.minSourcesCount = travelOptions.getMultiGraphAggregationMinSourcesCount();
            this.sourceValuesLowerBound = travelOptions.getMultiGraphAggregationSourceValuesLowerBound();
            this.sourceValuesUpperBound = travelOptions.getMultiGraphAggregationSourceValuesUpperBound();
            this.minResultValueRatio = travelOptions.getMultiGraphAggregationMinResultValueRatio();
            this.minResultValue = travelOptions.getMultiGraphAggregationMinResultValue();
            this.maxResultValueRatio = travelOptions.getMultiGraphAggregationMaxResultValueRatio();
            this.maxResultValue = travelOptions.getMultiGraphAggregationMaxResultValue();
            this.postAggregationFactor = travelOptions.getMultiGraphAggregationPostAggregationFactor();
            this.filterValuesForSourceOrigins = !deepCopy ? travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins() :
                    Optional.ofNullable(travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins()).map(HashSet::new).orElse(null);
            this.gravitationExponent = travelOptions.getMultiGraphAggregationGravitationExponent();
            this.probabilityDecay = travelOptions.getMultiGraphAggregationProbabilityDecay();
            this.logitBetaAttractionStrength = travelOptions.getMultiGraphAggregationLogitBetaAttractionStrength();
            this.logitBetaTravelTime = travelOptions.getMultiGraphAggregationLogitBetaTravelTime();
            this.aggregationInputParameters = !deepCopy ? travelOptions.getMultiGraphAggregationInputParameters() :
                    Optional.ofNullable(travelOptions.getMultiGraphAggregationInputParameters()).map(map ->
                            map.entrySet().stream().collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> new AggregationInputParameters(
                                            entry.getValue().getInputFactor(),
                                            entry.getValue().getGravitationPositiveInfluence(),
                                            entry.getValue().getGravitationAttractionStrength(),
                                            entry.getValue().getGravitationCompetingPositiveInfluence()))))
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

        public AggregationConfigurationBuilder sourceValuesLowerBound(Float sourceValuesLowerBound) {
            this.sourceValuesLowerBound = sourceValuesLowerBound;
            return this;
        }

        public AggregationConfigurationBuilder sourceValuesUpperBound(Float sourceValuesUpperBound) {
            this.sourceValuesUpperBound = sourceValuesUpperBound;
            return this;
        }

        public AggregationConfigurationBuilder minResultValueRatio(Double minResultValueRatio) {
            this.minResultValueRatio = minResultValueRatio;
            return this;
        }

        public AggregationConfigurationBuilder minResultValue(Float minResultValue) {
            this.minResultValue = minResultValue;
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

        public AggregationConfigurationBuilder probabilityDecay(Double probabilityDecay) {
            this.probabilityDecay = probabilityDecay;
            return this;
        }

        public AggregationConfigurationBuilder logitBetaAttractionStrength(Double logitBetaAttractionStrength) {
            this.logitBetaAttractionStrength = logitBetaAttractionStrength;
            return this;
        }

        public AggregationConfigurationBuilder logitBetaTravelTime(Double logitBetaTravelTime) {
            this.logitBetaTravelTime = logitBetaTravelTime;
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
            return new AggregationConfiguration(
                    type,
                    ignoreOutliers,
                    outlierPenalty,
                    minSourcesRatio,
                    minSourcesCount,
                    sourceValuesLowerBound,
                    sourceValuesUpperBound,
                    minResultValueRatio,
                    minResultValue,
                    maxResultValueRatio,
                    maxResultValue,
                    postAggregationFactor,
                    filterValuesForSourceOrigins,
                    gravitationExponent,
                    probabilityDecay,
                    logitBetaAttractionStrength,
                    logitBetaTravelTime,
                    aggregationInputParameters,
                    mathExpression);
        }
    }
}


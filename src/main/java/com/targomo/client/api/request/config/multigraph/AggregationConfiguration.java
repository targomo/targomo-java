package com.targomo.client.api.request.config.multigraph;

import com.targomo.client.api.enums.MultiGraphAggregationType;
import com.targomo.client.api.pojo.SourceParameters;
import lombok.*;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * POJO to define pipeline aggregations for the multigraph.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString(includeFieldNames=true)
public class AggregationConfiguration {

    private MultiGraphAggregationType type;
    private Boolean ignoreOutlier;
    private Float outlierPenalty;
    private Double minSourcesRatio;
    private Integer minSourcesCount;
    private Double maxResultValueRatio;
    private Float maxResultValue;
    private Float accuracy;
    private Double gravitationalExponent;
    private Set<String> filterValuesForSourceOrigins;
    private LinkedHashMap<String, SourceParameters> sourceParameters;
}


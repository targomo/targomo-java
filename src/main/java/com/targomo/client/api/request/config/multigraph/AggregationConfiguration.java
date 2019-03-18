package com.targomo.client.api.request.config.multigraph;

import com.targomo.client.api.enums.MultiGraphAggregationType;
import com.targomo.client.api.pojo.SourceParameters;
import lombok.*;
import java.util.Map;
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
public class AggregationConfig {

    private MultiGraphAggregationType type;
    private Boolean ignoreOutlier;
    private Integer outlierPenalty;
    private Double minSourcesRatio;
    private Integer minSourcesCount;
    private Double maxResultValueRatio;
    private Integer maxResultValue;
    private Set<String> filterValuesForSourceOrigins;
    private Map<String, SourceParameters> sourceParameters;
}


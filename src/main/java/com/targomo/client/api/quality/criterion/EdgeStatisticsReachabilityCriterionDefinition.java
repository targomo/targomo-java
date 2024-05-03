package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EdgeStatisticsReachabilityCriterionDefinition extends RoutingBasedCriterionDefinition {

    @Setter
    private String edgeStatisticsServiceUrl;

    private final Integer edgeStatisticCollectionId;

    // Use either edgeStatisticIds or edgeStatisticId
    private final List<Integer> edgeStatisticIds;
    private final Integer edgeStatisticId;

}

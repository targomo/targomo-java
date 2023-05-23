package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.targomo.client.api.enums.RoutingAggregationType;
import com.targomo.client.api.pojo.CompetingRoutingOption;
import com.targomo.client.api.quality.Location;
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
public class StatisticsGravitationCriterionDefinition extends StatisticsReachabilityCriterionDefinition implements GravitationCriterionDefinition {

    @Setter
    private List<Location> competitors;

    @Setter
    private Double gravitationExponent;

    @Setter
    private Double probabilityDecay;

    private final Boolean calculateGravitationPerReferenceId;

    private final Float routingLowerBoundValue;

    private final Float routingSourceModifier;

    private final List<CompetingRoutingOption> competingRoutingOptions;
    private final RoutingAggregationType routingAggregationType;
}

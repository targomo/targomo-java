package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.quality.Location;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = StatisticsGravitationCriterionDefinition.StatisticsGravitationCriterionDefinitionBuilderImpl.class)
public class StatisticsGravitationCriterionDefinition extends StatisticsReachabilityCriterionDefinition implements GravitationCriterionDefinition {

    @Setter
    private List<Location> competitors;

    @Setter
    private Double gravitationExponent;

    @Setter
    private Double probabilityDecay;

    private final Boolean calculateGravitationPerReferenceId;

    private final Double routingLowerBoundValue;

    @JsonPOJOBuilder(withPrefix="")
    public static class StatisticsGravitationCriterionDefinitionBuilderImpl extends StatisticsGravitationCriterionDefinition.StatisticsGravitationCriterionDefinitionBuilder {
    }
}

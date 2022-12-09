package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoiGravitationCriterionDefinition extends PoiReachabilityCriterionDefinition implements GravitationCriterionDefinition {

    @Setter
    private List<Location> competitors;

    @Setter
    private Double gravitationExponent;

    @Setter
    private Double probabilityDecay;

}

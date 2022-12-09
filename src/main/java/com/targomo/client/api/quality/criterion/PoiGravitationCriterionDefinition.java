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
@JsonDeserialize(builder = PoiGravitationCriterionDefinition.PoiGravitationCriterionDefinitionBuilderImpl.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoiGravitationCriterionDefinition extends PoiReachabilityCriterionDefinition implements GravitationCriterionDefinition {

    @Setter
    private List<Location> competitors;

    @Setter
    private Double gravitationExponent;

    @Setter
    private Double probabilityDecay;

    public abstract static class PoiGravitationCriterionDefinitionBuilder {}

    @JsonPOJOBuilder(withPrefix="")
    public static class PoiGravitationCriterionDefinitionBuilderImpl extends PoiGravitationCriterionDefinition.PoiGravitationCriterionDefinitionBuilder {
    }
}

package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Getter
@JsonDeserialize(builder = PolygonAreaCriterionDefinition.PolygonAreaCriterionDefinitionBuilderImpl.class)
public class PolygonAreaCriterionDefinition extends RoutingBasedCriterionDefinition{

    @JsonPOJOBuilder(withPrefix="")
    public static class PolygonAreaCriterionDefinitionBuilderImpl extends PolygonAreaCriterionDefinition.PolygonAreaCriterionDefinitionBuilder{
    }
}

package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolygonAreaCriterionDefinition extends RoutingBasedCriterionDefinition{

    private final Integer srid;
    private final Double simplify;
    private final Double buffer;
    private final Integer quadrantSegments;

    public abstract static class PolygonAreaCriterionDefinitionBuilder {}

    @JsonPOJOBuilder(withPrefix="")
    public static class PolygonAreaCriterionDefinitionBuilderImpl extends PolygonAreaCriterionDefinition.PolygonAreaCriterionDefinitionBuilder{
    }
}

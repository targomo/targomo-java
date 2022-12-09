package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolygonAreaCriterionDefinition extends RoutingBasedCriterionDefinition{

    private final Integer srid;
    private final Double simplify;
    private final Double buffer;
    private final Integer quadrantSegments;

}

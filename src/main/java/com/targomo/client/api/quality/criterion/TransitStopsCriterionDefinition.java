package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
public class TransitStopsCriterionDefinition extends RoutingBasedCriterionDefinition {

    private final Integer startTime;
    private final Integer endTime;
    private final Integer referenceInterval;
    private final List<Integer> routeTypes;

}

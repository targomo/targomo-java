package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MobilityCriterionDefinition extends CriterionDefinition {

    @Setter
    @NotNull
    private String mobilityServiceUrl;

    private final Integer minDuration;
    private final Integer maxDuration;

    private final Integer hourStart;
    private final Integer hourEnd;

    private final Integer dayStart;
    private final Integer dayEnd;

    private final Integer dayOfYearStart;
    private final Integer dayOfYearEnd;

    private final Boolean unique;
    private final Boolean exact;
    private final Boolean excludeNightLocations;

    private final Integer radius;

}

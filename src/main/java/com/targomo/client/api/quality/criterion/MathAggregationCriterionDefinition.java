package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MathAggregationCriterionDefinition extends CriterionDefinition {

    @NonNull
    private final String mathExpression;
    @NonNull
    private final Map<String, CriterionDefinition> criterionParameters;

    @Setter
    @JsonIgnore
    private String validatedMathExpression;
    @Setter
    @JsonIgnore
    private List<String> orderedSubCriterionKey;

}

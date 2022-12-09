package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = MathAggregationCriterionDefinition.MathAggregationCriterionDefinitionBuilderImpl.class)
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

    public abstract static class MathAggregationCriterionDefinitionBuilder {}

    @JsonPOJOBuilder(withPrefix="")
    public static class MathAggregationCriterionDefinitionBuilderImpl extends
            MathAggregationCriterionDefinition.MathAggregationCriterionDefinitionBuilder {}
}

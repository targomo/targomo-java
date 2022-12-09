package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = ReferenceCriterionDefinition.ReferenceCriterionDefinitionBuilderImpl.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceCriterionDefinition extends CriterionDefinition {

    private final String baseCriterionId;

    public abstract static class ReferenceCriterionDefinitionBuilder {}

    @JsonPOJOBuilder(withPrefix = "")
    public static class ReferenceCriterionDefinitionBuilderImpl extends ReferenceCriterionDefinition.ReferenceCriterionDefinitionBuilder {
    }
}

package com.targomo.client.api.quality.criterion;

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
public class ReferenceCriterionDefinition extends CriterionDefinition {

    private String baseCriterionId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ReferenceCriterionDefinitionBuilderImpl extends ReferenceCriterionDefinition.ReferenceCriterionDefinitionBuilder {
    }
}

package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import com.targomo.core.util.logging.model.RequestAttributes;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = ReferenceCriterionDefinition.ReferenceCriterionDefinitionBuilderImpl.class)
public class ReferenceCriterionDefinition extends CriterionDefinition {

    private String baseCriterionId;

    @Override
    public RequestAttributes.RequestAttributesBuilder getRequestAttributesBuilder(boolean isScore) {
        throw new IllegalStateException("Better use base criterion's request attributes");
    }

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        return null;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ReferenceCriterionDefinitionBuilderImpl extends ReferenceCriterionDefinition.ReferenceCriterionDefinitionBuilder {
    }
}

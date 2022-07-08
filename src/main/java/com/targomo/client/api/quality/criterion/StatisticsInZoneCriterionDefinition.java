package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import com.targomo.core.util.logging.model.RequestAttributes;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Getter
@JsonDeserialize(builder = StatisticsInZoneCriterionDefinition.StatisticsInZoneCriterionDefinitionBuilderImpl.class)
public class StatisticsInZoneCriterionDefinition extends CriterionDefinition implements StatisticsCriterionDefinition{

    private final Integer statisticGroupId;
    private final Integer statisticCollectionId;

    @NotEmpty
    private final List<Short> statisticsIds;
    private final List<Short> referenceStatisticsIds;
    @Setter
    private String statisticsServiceUrl;

    private final String coreServiceUrl;

    public RequestAttributes.RequestAttributesBuilder getRequestAttributesBuilder(boolean isScore) {
        return RequestAttributes.builder()
                .reqType(getRequestType(isScore));
    }

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        return isScore ? FineGrainedRequestTypeEnum.QUALITY_SCORE_STATISTICS_IN_ZONE : FineGrainedRequestTypeEnum.QUALITY_RATING_STATISTICS_IN_ZONE;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class StatisticsInZoneCriterionDefinitionBuilderImpl extends StatisticsInZoneCriterionDefinition.StatisticsInZoneCriterionDefinitionBuilder{
    }
}

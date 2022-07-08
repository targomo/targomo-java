package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Getter
@JsonDeserialize(builder = StatisticsReachabilityCriterionDefinition.StatisticsReachabilityCriterionDefinitionBuilderImpl.class)
public class StatisticsReachabilityCriterionDefinition extends RoutingBasedCriterionDefinition implements StatisticsCriterionDefinition{
    
    private final Integer statisticGroupId;
    private final Integer statisticCollectionId;

    @NotEmpty
    private final List<Short> statisticsIds;
    private final List<Short> referenceStatisticsIds;
    @Setter
    private String statisticsServiceUrl;

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        return isScore ? FineGrainedRequestTypeEnum.QUALITY_SCORE_STATISTICS_REACHABILITY : FineGrainedRequestTypeEnum.QUALITY_RATING_STATISTICS_REACHABILITY;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class StatisticsReachabilityCriterionDefinitionBuilderImpl extends StatisticsReachabilityCriterionDefinition.StatisticsReachabilityCriterionDefinitionBuilder{
    }
}

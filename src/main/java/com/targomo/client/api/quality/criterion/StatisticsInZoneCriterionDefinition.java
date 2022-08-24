package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    private final Boolean cellsCount;

    @Setter
    private String statisticsServiceUrl;

    private final String coreServiceUrl;

    @JsonPOJOBuilder(withPrefix="")
    public static class StatisticsInZoneCriterionDefinitionBuilderImpl extends StatisticsInZoneCriterionDefinition.StatisticsInZoneCriterionDefinitionBuilder{
    }
}

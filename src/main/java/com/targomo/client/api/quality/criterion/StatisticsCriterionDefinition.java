package com.targomo.client.api.quality.criterion;

import java.util.List;

public interface StatisticsCriterionDefinition  extends CriterionDefinitionInterface{

    CriterionType getType();

    Integer getStatisticCollectionId();
    Integer getStatisticGroupId();

    List<Short> getStatisticsIds();

    List<Short> getReferenceStatisticsIds();

    String getStatisticsServiceUrl();

    void setStatisticsServiceUrl(String url);

    String getCoreServiceUrl();

}

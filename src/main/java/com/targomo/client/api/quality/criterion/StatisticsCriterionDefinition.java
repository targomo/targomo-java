package com.targomo.client.api.quality.criterion;

import com.targomo.core.user.FineGrainedRequestTypeEnum;

import java.util.List;

public interface StatisticsCriterionDefinition {

    Integer getStatisticCollectionId();
    Integer getStatisticGroupId();

    List<Short> getStatisticsIds();

    List<Short> getReferenceStatisticsIds();

    String getStatisticsServiceUrl();

    void setStatisticsServiceUrl(String url);

    String getCoreServiceUrl();

    FineGrainedRequestTypeEnum getRequestType(boolean isScore);
}

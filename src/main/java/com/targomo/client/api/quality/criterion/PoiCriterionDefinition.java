package com.targomo.client.api.quality.criterion;

import com.targomo.client.api.statistic.PoiType;
import com.targomo.core.user.FineGrainedRequestTypeEnum;


import java.util.Set;

public interface PoiCriterionDefinition {

    CriterionType getType();

    Set<PoiType> getOsmTypes();

    Set<PoiType> getReferenceOsmTypes();

    String getWeightedBy();

    String getPoiServiceUrl();

    void setPoiServiceUrl(String poiServiceUrl);

    Set<PoiType> getExclude();

    PoiMatchType getMatch();

    FineGrainedRequestTypeEnum getRequestType(boolean isScore);

}

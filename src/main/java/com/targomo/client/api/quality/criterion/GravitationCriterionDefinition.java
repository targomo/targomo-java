package com.targomo.client.api.quality.criterion;

import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.quality.Location;

import java.util.List;

public interface GravitationCriterionDefinition {

    TravelType getTravelType();

    EdgeWeightType getEdgeWeight();

    Integer getMaxEdgeWeight();

    List<Location> getCompetitors();

    void setCompetitors(List<Location> competitors);

}

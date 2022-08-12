package com.targomo.client.api.quality.criterion;

import com.targomo.client.api.quality.Location;

import java.util.List;

public interface GravitationCriterionDefinition {

    List<Location> getCompetitors();

    void setCompetitors(List<Location> competitors);

}

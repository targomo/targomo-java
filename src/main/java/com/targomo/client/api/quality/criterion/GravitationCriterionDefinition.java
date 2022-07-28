package com.targomo.client.api.quality.criterion;

import com.targomo.client.api.quality.PublicLocation;

import java.util.List;

public interface GravitationCriterionDefinition {

    List<PublicLocation> getCompetitors();

    void setCompetitors(List<PublicLocation> competitors);

}

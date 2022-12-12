package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.statistic.PoiType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PoiReachabilityCriterionDefinition extends RoutingBasedCriterionDefinition implements PoiCriterionDefinition {
    @NotEmpty
    private final Set<PoiType> osmTypes;
    private final Set<PoiType> referenceOsmTypes;
    //Variable holder for a POI Tag e.g. capacity in bike parking osm data
    private final String weightedBy;
    private final Set<PoiType> exclude;
    private final PoiMatchType match;

    @Setter
    private String poiServiceUrl;

}

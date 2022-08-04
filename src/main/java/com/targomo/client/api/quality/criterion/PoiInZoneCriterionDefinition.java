package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.statistic.PoiType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = PoiInZoneCriterionDefinition.PoiInZoneCriterionDefinitionBuilderImpl.class)
public class PoiInZoneCriterionDefinition extends CriterionDefinition implements PoiCriterionDefinition {
    @NotEmpty
    private final Set<PoiType> osmTypes;
    private final Set<PoiType> referenceOsmTypes;
    //Variable holder for a POI Tag e.g. capacity in bike parking osm data
    private final String weightedBy;
    @Setter
    private String poiServiceUrl;
    private final Set<PoiType> exclude;
    private final PoiMatchType match;

    @JsonPOJOBuilder(withPrefix="")
    public static class PoiInZoneCriterionDefinitionBuilderImpl extends PoiInZoneCriterionDefinition.PoiInZoneCriterionDefinitionBuilder {
    }
}

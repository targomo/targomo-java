package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.statistic.PoiType;
import com.targomo.core.user.FineGrainedRequestTypeEnum;
import com.targomo.core.util.logging.model.RequestAttributes;
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

    @Override
    public RequestAttributes.RequestAttributesBuilder getRequestAttributesBuilder(boolean isScore) {
        return RequestAttributes.builder()
                .reqType(getRequestType(isScore));
    }

    @Override
    public FineGrainedRequestTypeEnum getRequestType(boolean isScore) {
        return isScore ? FineGrainedRequestTypeEnum.QUALITY_SCORE_POI_IN_ZONE : FineGrainedRequestTypeEnum.QUALITY_RATING_POI_IN_ZONE;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class PoiInZoneCriterionDefinitionBuilderImpl extends PoiInZoneCriterionDefinition.PoiInZoneCriterionDefinitionBuilder {
    }
}

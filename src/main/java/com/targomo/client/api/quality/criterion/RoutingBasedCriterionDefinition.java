package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientRequestConfigurationException;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = RoutingBasedCriterionDefinition.RoutingBasedCriterionDefinitionBuilderImpl.class)
public abstract class RoutingBasedCriterionDefinition extends CriterionDefinition {

    @NonNull
    private final EdgeWeightType edgeWeight;
    @NonNull
    private final Integer maxEdgeWeight;
    @NotEmpty
    private final CaseInsensitiveMap travelMode;
    private final Boolean elevation;
    private final Map<String, Double> travelTimeFactors;
    private final Boolean reverse;
    private final Integer maxSnapDistance;
    @Setter
    private String coreServiceUrl;

    @JsonIgnore
    public TravelType getTravelType() {
        if (travelMode.get("transit") != null) {
            return TravelType.TRANSIT;
        } else if (travelMode.get("car") != null) {
            return TravelType.CAR;
        } else if (travelMode.get("bike") != null) {
            return TravelType.BIKE;
        } else if (travelMode.get("walk") != null) {
            return TravelType.WALK;
        }
        throw new TargomoClientRequestConfigurationException("Travel type not supported");
    }

    @JsonIgnore
    public Map getTravelModeProperties() {
        Object propertiesObj = travelMode.get(getTravelType());
        if (propertiesObj == null) {
            return new HashMap();
        }
        if (!(propertiesObj instanceof Map)) {
            throw new TargomoClientRequestConfigurationException(String.format("Invalid configuration for travel mode '%s'", getTravelType()));
        }

        return (Map) propertiesObj;
    }

    @JsonPOJOBuilder(withPrefix="")
    public abstract static class RoutingBasedCriterionDefinitionBuilderImpl extends RoutingBasedCriterionDefinitionBuilder {
    }
}

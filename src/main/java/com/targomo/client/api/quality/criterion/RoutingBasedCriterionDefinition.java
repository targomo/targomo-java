package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientRequestConfigurationException;
import com.targomo.client.api.pojo.Geometry;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class RoutingBasedCriterionDefinition extends CriterionDefinition {

    @NotNull
    private final EdgeWeightType edgeWeight;
    @NotNull
    private final Integer maxEdgeWeight;
    @NotEmpty
    private final CaseInsensitiveMap travelMode;
    private final Boolean elevation;
    private final Map<String, Double> travelTimeFactors;
    private final Boolean reverse;
    private final Geometry exclusionGeometry;
    private final List<Integer> excludeEdgeClasses;

    private final Boolean routeFromGeometryCentroid;

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
        } else if (travelMode.get("fly") != null) {
            return TravelType.FLY;
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

}

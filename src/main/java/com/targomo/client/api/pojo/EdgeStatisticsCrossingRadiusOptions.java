package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.json.TravelOptionsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdgeStatisticsCrossingRadiusOptions extends AbstractEdgeStatisticsCrossingRadiusOptions {

    @JsonSerialize(using = TravelOptionsSerializer.class)
    private TravelOptions routingOptions;
}

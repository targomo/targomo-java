package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.json.TravelOptionsSerializer;
import lombok.*;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdgeStatisticsReachabilityInRadiusOptions extends AbstractEdgeStatisticsReachabilityInRadiusOptions {

    @JsonSerialize(using = TravelOptionsSerializer.class)
    TravelOptions routingOptions;
}

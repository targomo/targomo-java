package com.targomo.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Response object of EdgeStatisticsReachabilityRequest
 */
@Getter
@AllArgsConstructor @NoArgsConstructor
public class EdgeStatisticsReachabilityResponse {

    // map from location id to a map of statistic id (or aggregations id) to statistic value
    HashMap<String, Map<String, Double>> data;

    // response code
    String code;

    // response message
    String message;

    // time spent processing the request
    Integer requestTime;
}

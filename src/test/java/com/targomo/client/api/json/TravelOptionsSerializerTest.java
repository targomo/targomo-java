package com.targomo.client.api.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeStatisticAggregationType;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.pojo.EdgeStatisticsReachabilityInRadiusOptions;
import com.targomo.client.api.pojo.EdgeStatisticsReachabilityRequestOptions;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class TravelOptionsSerializerTest {

    @Test
    public void testTravelOptionsSerializer() throws IOException {
        TravelOptions options = new TravelOptions();
        options.setEdgeWeightType(EdgeWeightType.TIME);
        options.setMaxEdgeWeight(80);
        options.setTravelType(TravelType.CAR);
        Coordinate source = new DefaultSourceCoordinate("p1", 13.42883045, 52.5494892);
        options.addSource(source);
        options.setServiceKey("KEY");
        options.setServiceUrl("https://api.targomo.com/edge-statistics/");

        EdgeStatisticsReachabilityRequestOptions cfg = new EdgeStatisticsReachabilityRequestOptions(new HashSet<>(Arrays.asList(0, 1)), new HashMap<>(), null, options);
        String requestBody = new ObjectMapper().writeValueAsString(cfg);

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedJson = IOUtils.toString(classLoader.getResourceAsStream("data/EdgeStatisticsReachabilityRequest.json"));
        Assert.assertEquals(StringUtils.deleteWhitespace(expectedJson), StringUtils.deleteWhitespace(requestBody));

        Map<String, List<Integer>> aggStats = new HashMap<>();
        aggStats.put("asdf", Arrays.asList(0, 1, 2));
        EdgeStatisticsReachabilityRequestOptions cfg2 = new EdgeStatisticsReachabilityRequestOptions(new HashSet<>(Arrays.asList(0, 1)), aggStats, EdgeStatisticAggregationType.SUM, options);
        String requestBody2 = new ObjectMapper().writeValueAsString(cfg2);

        String expectedJson2 = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("data/EdgeStatisticsReachabilityRequest2.json"));
        Assert.assertEquals(StringUtils.deleteWhitespace(expectedJson2), StringUtils.deleteWhitespace(requestBody2));
    }

    @Test
    public void testEdgeStatisticsReachabilityInRadiusOptions() throws IOException {
        TravelOptions options = new TravelOptions();
        options.setEdgeWeightType(EdgeWeightType.TIME);
        options.setMaxEdgeWeight(80);
        options.setTravelType(TravelType.CAR);
        Coordinate source = new DefaultSourceCoordinate("p1", 13.42883045, 52.5494892);
        options.addSource(source);
        options.setServiceKey("KEY");
        options.setServiceUrl("https://api.targomo.com/edge-statistics/");

        HashMap<String, List<Integer>> aggregateIds = new HashMap<>();
        aggregateIds.put("abc", Arrays.asList(0, 1));
        EdgeStatisticsReachabilityInRadiusOptions cfg = EdgeStatisticsReachabilityInRadiusOptions.builder()
                .edgeStatisticIds(new HashSet<>(Arrays.asList(0, 1)))
                .aggregateEdgeStatisticIds(aggregateIds)
                .aggregationType(EdgeStatisticAggregationType.SUM)
                .radius(50)
                .ignoreRoadClasses(Arrays.asList(11, 13))
                .routingOptions(options)
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(cfg);

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedJson = IOUtils.toString(classLoader.getResourceAsStream("data/EdgeStatisticsReachabilityInRadiusRequest.json"));
        Assert.assertEquals(StringUtils.deleteWhitespace(expectedJson), StringUtils.deleteWhitespace(requestBody));
    }

}

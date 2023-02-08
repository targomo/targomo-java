package com.targomo.client.api.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.request.EdgeStatisticsReachabilityRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

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

        EdgeStatisticsReachabilityRequest.EdgeStatisticsReachabilityBody cfg = new EdgeStatisticsReachabilityRequest.EdgeStatisticsReachabilityBody(Arrays.asList(0, 1), options);
        String requestBody = new ObjectMapper().writeValueAsString(cfg);

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedJson = IOUtils.toString(classLoader.getResourceAsStream("data/EdgeStatisticsReachabilityRequest.json"));
        Assert.assertEquals(StringUtils.deleteWhitespace(expectedJson), StringUtils.deleteWhitespace(requestBody));

    }
}

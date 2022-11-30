package com.targomo.client.api;

import com.targomo.client.api.enums.RoutingAggregationTypeTest;
import com.targomo.client.api.pojo.GeometryTest;
import com.targomo.client.api.request.*;
import com.targomo.client.api.request.config.RequestConfiguratorTest;
import com.targomo.client.api.request.ssl.SslClientGeneratorTest;
import com.targomo.client.api.response.ReachabilityResponseTest;
import com.targomo.client.api.response.TimeResponseTest;
import com.targomo.client.api.util.CurlUtilTest;
import com.targomo.client.api.util.GeojsonUtilTest;
import com.targomo.client.api.util.IOUtilTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GeometryTest.class,
        RequestConfiguratorTest.class,
        SslClientGeneratorTest.class,
        GeocodingRequestTest.class,
        MultiGraphRequestTest.class,
        PointOfInterestRequestTest.class,
        PolygonRequestTest.class,
        ReachabilityRequestTest.class,
        RouteRequestTest.class,
        ScoreRequestTest.class,
        TimeRequestTest.class,
        TimeVectorRequestTest.class,
        ReachabilityResponseTest.class,
        TimeResponseTest.class,
        CurlUtilTest.class,
        GeojsonUtilTest.class,
        IOUtilTest.class,
        RoutingAggregationTypeTest.class
})
public class TestSuite {
}

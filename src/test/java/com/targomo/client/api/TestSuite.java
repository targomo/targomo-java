package com.targomo.client.api;

import com.targomo.client.api.pojo.GeometryTest;
import com.targomo.client.api.request.*;
import com.targomo.client.api.request.config.RequestConfiguratorTest;
import com.targomo.client.api.request.ssl.SslClientGeneratorTest;
import com.targomo.client.api.response.TimeResponseTest;
import com.targomo.client.api.util.CurlUtilTest;
import com.targomo.client.api.util.GeojsonUtilTest;
import com.targomo.client.api.util.IOUtilTest;
import com.targomo.client.api.response.ReachabilityResponseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TimeVectorRequestTest.class,
        MultiGraphRequestTest.class,
        RequestConfiguratorTest.class,
        GeometryRequestTest.class,
        ReachabilityRequestTest.class,
        RouteRequestTest.class,
        TimeRequestTest.class,
        IOUtilTest.class,
        TimeResponseTest.class,
        ReachabilityResponseTest.class,
        SslClientGeneratorTest.class,
        GeocodingRequestTest.class,
        GeojsonUtilTest.class,
        CurlUtilTest.class,
        GeometryTest.class
})
public class TestSuite {
}

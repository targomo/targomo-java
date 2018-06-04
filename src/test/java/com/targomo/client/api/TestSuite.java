package com.targomo.client.api;

import com.targomo.client.api.request.GeocodingRequestTest;
import com.targomo.client.api.request.ReachabilityRequestTest;
import com.targomo.client.api.request.TimeRequestTest;
import com.targomo.client.api.request.config.RequestConfiguratorTest;
import com.targomo.client.api.request.ssl.SslClientGeneratorTest;
import com.targomo.client.api.response.TimeResponseTest;
import com.targomo.client.api.util.IOUtilTest;
import com.targomo.client.api.request.PolygonRequestTest;
import com.targomo.client.api.request.RouteRequestTest;
import com.targomo.client.api.response.ReachabilityResponseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		RequestConfiguratorTest.class,
		PolygonRequestTest.class,
		ReachabilityRequestTest.class,
		RouteRequestTest.class,
		TimeRequestTest.class,
		IOUtilTest.class,
		TimeResponseTest.class,
		ReachabilityResponseTest.class,
		SslClientGeneratorTest.class,
        GeocodingRequestTest.class
})
public class TestSuite {
}

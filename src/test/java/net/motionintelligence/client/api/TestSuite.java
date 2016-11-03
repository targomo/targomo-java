package net.motionintelligence.client.api;

import net.motionintelligence.client.api.request.PolygonRequestTest;
import net.motionintelligence.client.api.request.ReachabilityRequestTest;
import net.motionintelligence.client.api.request.RouteRequestTest;
import net.motionintelligence.client.api.request.TimeRequestTest;
import net.motionintelligence.client.api.request.config.RequestConfiguratorTest;
import net.motionintelligence.client.api.response.ReachabilityResponseTest;
import net.motionintelligence.client.api.response.TimeResponseTest;
import net.motionintelligence.client.api.util.IOUtilTest;
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
		ReachabilityResponseTest.class
})
public class TestSuite {
}

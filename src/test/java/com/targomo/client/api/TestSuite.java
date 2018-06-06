package com.targomo.client.api;

<<<<<<< HEAD:src/test/java/net/motionintelligence/client/api/TestSuite.java
import net.motionintelligence.client.api.request.PolygonRequestTest;
import net.motionintelligence.client.api.request.ReachabilityRequestTest;
import net.motionintelligence.client.api.request.RouteRequestTest;
import net.motionintelligence.client.api.request.TimeRequestTest;
import net.motionintelligence.client.api.request.config.RequestConfiguratorTest;
import net.motionintelligence.client.api.request.refactored.MultiGraphRequestTest;
import net.motionintelligence.client.api.request.ssl.SslClientGeneratorTest;
import net.motionintelligence.client.api.request.GeocodingRequestTest;
import net.motionintelligence.client.api.response.ReachabilityResponseTest;
import net.motionintelligence.client.api.response.TimeResponseTest;
import net.motionintelligence.client.api.util.IOUtilTest;
=======
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
>>>>>>> develop:src/test/java/com/targomo/client/api/TestSuite.java
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		MultiGraphRequestTest.class,
		net.motionintelligence.client.api.request.refactored.PolygonRequestTest.class,
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

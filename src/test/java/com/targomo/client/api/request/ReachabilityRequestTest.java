package com.targomo.client.api.request;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.PathSerializerType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import com.targomo.client.api.response.ReachabilityResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class ReachabilityRequestTest extends RequestTest {
    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/ReachabilityResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

        ReachabilityRequest reachabilityRequest = new ReachabilityRequest(mockClient, getTravelOptions());
        ReachabilityResponse reachabilityResponse = reachabilityRequest.get();

        assertEquals("ok", reachabilityResponse.getCode());
        assertEquals(517, reachabilityResponse.getRequestTimeMillis());
        assertNotNull(reachabilityResponse.getTravelTimes());
    }

    @Test
    public void get_gateway_timeout() throws Exception {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.GATEWAY_TIMEOUT.getStatusCode());

        TravelOptions options = getTravelOptions();
        ReachabilityRequest reachabilityRequest = new ReachabilityRequest(mockClient, options);
        ReachabilityResponse reachabilityResponse = reachabilityRequest.get();
        assertEquals("gateway-time-out", reachabilityResponse.getCode());
    }

	@Test(expected = TargomoClientException.class)
	public void get_exception() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        TravelOptions options = getTravelOptions();
        ReachabilityRequest reachabilityRequest = new ReachabilityRequest(mockClient, options);
        reachabilityRequest.get();
    }

	private TravelOptions getTravelOptions() {
		TravelOptions options = new TravelOptions();
		options.setTravelTimes(Arrays.asList(600, 1200, 1800));
		options.setTravelType(TravelType.CAR);
		options.setPathSerializer(PathSerializerType.COMPACT_PATH_SERIALIZER);
		options.addSource(new DefaultSourceCoordinate("id0", 10.639872441947901, -17.37236573607632, TravelType.CAR));
		options.addSource(new DefaultSourceCoordinate("id1", 49.75913939827413, -75.83100508496594, TravelType.CAR));
		options.addSource(new DefaultSourceCoordinate("id2", 64.68336864385466, 62.545199028051314, TravelType.CAR));
		options.addTarget(new DefaultTargetCoordinate("id0", -84.01440151465849, 41.805968836500426));
		options.addTarget(new DefaultTargetCoordinate("id1", 71.34898703519798, -36.16051650737182));
		options.addTarget(new DefaultTargetCoordinate("id2", 57.12383436652155, 33.64895304828113));
		options.setServiceKey("INSERT_YOUR_KEY_HERE");
		options.setServiceUrl("https://api.targomo.com/na_northeast/");
		return options;
	}

}
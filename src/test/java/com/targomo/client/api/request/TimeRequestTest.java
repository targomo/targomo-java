package com.targomo.client.api.request;

import com.targomo.client.api.response.TimeResponse;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.PathSerializerType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class TimeRequestTest extends RequestTest {
    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/TimeResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

        TimeRequest timeRequest = new TimeRequest(mockClient, getTravelOptions());
        TimeResponse timeResponse = timeRequest.get();

        assertEquals("ok", timeResponse.getCode());
        assertEquals(472, timeResponse.getRequestTimeMillis());
        assertNotNull(timeResponse.getTravelTimes());
    }

    @Test
    public void get_gateway_timeout() throws Exception {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.GATEWAY_TIMEOUT.getStatusCode());

        TravelOptions options = getTravelOptions();
        TimeRequest timeRequest = new TimeRequest(mockClient, options);
        TimeResponse timeResponse = timeRequest.get();
        assertEquals("gateway-time-out", timeResponse.getCode());
    }

<<<<<<< HEAD:src/test/java/net/motionintelligence/client/api/request/TimeRequestTest.java
    @Test(expected = Route360ClientException.class)
    public void get_exception() throws Exception {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
=======
	@Test(expected = TargomoClientException.class)
	public void get_exception() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
>>>>>>> develop:src/test/java/com/targomo/client/api/request/TimeRequestTest.java

        TravelOptions options = getTravelOptions();
        TimeRequest timeRequest = new TimeRequest(mockClient, options);
        timeRequest.get();
    }

<<<<<<< HEAD:src/test/java/net/motionintelligence/client/api/request/TimeRequestTest.java
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
        options.setServiceUrl("https://service.route360.net/na_northeast/");
        return options;
    }
=======
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
>>>>>>> develop:src/test/java/com/targomo/client/api/request/TimeRequestTest.java

}
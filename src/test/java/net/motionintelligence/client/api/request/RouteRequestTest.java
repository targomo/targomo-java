package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.response.RouteResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouteRequestTest {

	@Mock
	Client mockClient;
	@Mock
	WebTarget mockWebTarget;
	@Mock
	Invocation.Builder mockBuilder;
	@Mock
	Response sampleResponse;

	@Before
	public void setUp() throws Exception {
		when(mockClient.target(anyString())).thenReturn(mockWebTarget);

		when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
		when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
		when(mockWebTarget.request()).thenReturn(mockBuilder);

		when(mockBuilder.get()).thenReturn(sampleResponse);
	}

	@Test
	public void get_success() throws Exception {
		// Mock success response
		when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

		// Get sample json when success response is queried
		ClassLoader classLoader = getClass().getClassLoader();
		String sampleJson = IOUtils.toString(classLoader.getResourceAsStream("data/RouteResponse.json"));
		when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

		// Make the call
		RouteRequest routeRequest = new RouteRequest(mockClient, getTravelOptions());
		RouteResponse routeResponse = routeRequest.get();

		// Check result
		assertEquals("ok", routeResponse.getCode());
		assertEquals(162, routeResponse.getRequestTimeMillis());
		assertEquals(1, routeResponse.getRoutes().length());

		JSONObject route = (JSONObject) routeResponse.getRoutes().get(0);
		assertEquals(146, route.getInt("travelTime"));
		assertEquals(976.1585626427711, route.getDouble("length"), 0);
		assertEquals("target0", route.getString("target_id"));
		assertEquals("source0", route.getString("source_id"));
		assertNotNull(route.getJSONArray("segments"));
	}

	@Test
	public void get_gateway_timeout() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.GATEWAY_TIMEOUT.getStatusCode());

		TravelOptions options = getTravelOptions();
		RouteRequest routeRequest = new RouteRequest(mockClient, options);
		RouteResponse routeResponse = routeRequest.get();
		assertEquals("gateway-time-out", routeResponse.getCode());
	}

	@Test(expected = Route360ClientException.class)
	public void get_exception() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

		TravelOptions options = getTravelOptions();
		RouteRequest routeRequest = new RouteRequest(mockClient, options);
		routeRequest.get();
	}

	private TravelOptions getTravelOptions() {
		TravelOptions options = new TravelOptions();
		options.setTravelTimes(Arrays.asList(600, 1200, 1800, 2400, 3000, 3600));
		options.setTravelType(TravelType.TRANSIT);
		options.addSource(new DefaultSourceCoordinate("id1", -73.976636, 40.608155));
		options.setServiceKey("INSERT_YOUR_KEY_HERE");
		options.setServiceUrl("https://service.route360.net/na_northeast/");
		options.setDate(20161020);
		options.setTime(55852);
		return options;
	}
}
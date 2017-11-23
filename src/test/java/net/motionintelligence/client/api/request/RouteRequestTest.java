package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.PathSerializerType;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.geo.DefaultTargetCoordinate;
import net.motionintelligence.client.api.response.RouteResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class RouteRequestTest extends RequestTest {

    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/RouteResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

        // Make the call
        RouteRequest routeRequest = new RouteRequest(mockClient, getTravelOptions());
        RouteResponse routeResponse = routeRequest.get();

        // Check result
        assertEquals("ok", routeResponse.getCode());
        assertEquals(39, routeResponse.getRequestTimeMillis());
        assertEquals(1, routeResponse.getRoutes().length());

        JSONObject route = (JSONObject) routeResponse.getRoutes().get(0);
        assertEquals(483, route.getInt("travelTime"));
        assertEquals(4064.2324777363283, route.getDouble("length"), 0);
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
        options.addSource(new DefaultSourceCoordinate("source0", 13.386154, 52.516221, TravelType.CAR));
        options.addTarget(new DefaultTargetCoordinate("target0", 13.41259, 52.497518));
        options.setPathSerializer(PathSerializerType.COMPACT_PATH_SERIALIZER);
        options.setElevationEnabled(true);
        options.setReverse(false);

        options.setServiceKey("INSERT_YOUR_KEY_HERE");
        options.setServiceUrl("https://service.route360.net/na_northeast/");
        return options;
    }
}
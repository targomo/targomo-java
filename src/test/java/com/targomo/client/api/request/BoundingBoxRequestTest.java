package com.targomo.client.api.request;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.response.BoundingBoxResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BoundingBoxRequestTest extends RequestTest {

    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/BoundingBoxResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

        // Make the call
        BoundingBoxRequest request = new BoundingBoxRequest(mockClient, getTravelOptions());
        BoundingBoxResponse response = request.get();

        // Check result
        assertEquals(13.486794, response.getMinX(), 0.001);
        assertEquals(13.4993255, response.getMaxX(), 0.001);
        assertEquals(52.6723343, response.getMinY(), 0.001);
        assertEquals(52.67545, response.getMaxY(), 0.001);
    }

    @Test
    public void get_snap_exception() throws Exception {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(sampleResponse.readEntity(String.class)).thenReturn("{\"code\": \"snap-distance-max-exceeded\",\"data\": {},\"message\": \"Closest edge found to point 'src' was 6179123.5m away, max snap distance is 1000m\"}");

        BoundingBoxRequest request = new BoundingBoxRequest(mockClient, getTravelOptions());

        try {
            BoundingBoxResponse response = request.get();
        }
        catch (TargomoClientException e) {
            assertEquals("Status: snap-distance-max-exceeded: Boundingbox request returned an error: Closest edge found to point 'src' was 6179123.5m away, max snap distance is 1000m", e.getMessage());
        }
    }

    @Test(expected = TargomoClientException.class)
    public void get_exception() throws Exception {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        BoundingBoxRequest polygonRequest = new BoundingBoxRequest(mockClient, getTravelOptions());
        polygonRequest.get();
    }

    private TravelOptions getTravelOptions() {
        TravelOptions options = new TravelOptions();
        options.setTravelType(TravelType.TRANSIT);
        options.addSource(new DefaultSourceCoordinate("id1", 13.5, 53.5));
        options.setServiceKey("INSERT_YOUR_KEY_HERE");
        options.setServiceUrl("https://api.targomo.com/westcentraleurope/");
        options.setDate(20240120);
        options.setTime(55852);
        return options;
    }
}

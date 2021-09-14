package com.targomo.client.api.request;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.response.ResponseCode;
import com.targomo.client.api.response.TimeVectorResponse;
import com.targomo.client.api.response.parsingpojos.TransitTravelTimes;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.message.GZipEncoder;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeVectorRequestTest extends RequestTest {

    private static final String INTEGRATION_TEST_KEY = "T17ZPR4F190APX61I1DR141142829";

	@Test
	public void get_success() throws Exception {
		// Mock success response
		when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

		// Get sample json when success response is queried
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/TimeVectorResponseSample.json");
		String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
		when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

		// Make the call
		TimeVectorResponse response = TimeVectorRequest.executeRequest(mockClient, getTravelOptions());

		// Check result
		assertEquals(ResponseCode.OK, response.getCode());
        assertEquals(6070, response.getRequestTimeMillis());
        assertThat( response.getData() ).isNotEmpty();
        assertThat( response.getTravelTimeVectors().size() ).isEqualTo(2);
        assertThat( response.getTravelTimeVectors().entrySet().stream().mapToInt( entry -> entry.getValue().size() ).toArray())
                .containsExactlyInAnyOrder(2,2);
        response.getTravelTimeVectors().forEach( (key,value) ->
            assertThat( value.values().stream().map(TransitTravelTimes::getTravelTimesTransit)).isNotEmpty() );
        response.getTravelTimeVectors().forEach( (key,value) ->
                assertThat( value.values().stream().map(TransitTravelTimes::getTravelTimeWalk)).isNotEmpty() );
	}

    /**
     * This is an integration test that tests against the westcentraleurope endpoint
     * @throws Exception
     */
    @Test
    public void get_success_API_test() throws Exception {

	    //prepare travelOptions
        TravelOptions tO = getTravelOptions();
        tO.setServiceKey(INTEGRATION_TEST_KEY);
        tO.setServiceUrl("https://service.route360.net/westcentraleurope");

        // Make the call
        Client client = ClientBuilder.newClient();
        client.register(new GZipEncoder());
        TimeVectorRequest request = new TimeVectorRequest(client,tO);
        TimeVectorResponse response = request.get();

        System.out.println(request.toCurl());

        assertEquals(ResponseCode.OK, response.getCode());
        assertThat( response.getData() ).isNotEmpty();
        assertThat( response.getTravelTimeVectors().size() ).isEqualTo(2);
        assertThat( response.getTravelTimeVectors().entrySet().stream().mapToInt( entry -> entry.getValue().size() ).toArray())
                .containsExactlyInAnyOrder(2,2);
        response.getTravelTimeVectors().forEach( (key,value) ->
                assertThat( value.values().stream().map(TransitTravelTimes::getTravelTimesTransit)).isNotEmpty() );
        response.getTravelTimeVectors().forEach( (key,value) ->
                assertThat( value.values().stream().map(TransitTravelTimes::getTravelTimeWalk)).isNotEmpty() );

    }

    @Test(expected = TargomoClientException.class)
	public void get_gateway_timeout() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.GATEWAY_TIMEOUT.getStatusCode());

        // Make the call
        TimeVectorResponse response = TimeVectorRequest.executeRequest(mockClient, getTravelOptions());
	}

	@Test(expected = TargomoClientException.class)
	public void get_exception() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        // Make the call
        TimeVectorRequest.executeRequest(mockClient, getTravelOptions());
	}

	private TravelOptions getTravelOptions() throws IOException {

        TravelOptions options = new TravelOptions();
        options.addSource(new DefaultSourceCoordinate("POI:1",8.620987,47.384197));
        options.addSource(new DefaultSourceCoordinate("POI:2",8.497925,47.385334));
        options.addTarget(new DefaultSourceCoordinate("Home 3",8.511658,47.322069));
        options.addTarget(new DefaultSourceCoordinate("Home 4",8.572083,47.439235));
        options.setServiceKey("YOUR_API_KEY_HERE");
        options.setServiceUrl("http://127.0.0.1:8080/");
        options.setEdgeWeightType(EdgeWeightType.TIME);
        options.setMaxEdgeWeight(720);
        options.setTravelType(TravelType.TRANSIT);
        options.setDate(20180815);
        options.setTime(40000);
        options.setFrame(14400);
        options.setMaxWalkingTimeFromSource(500);
        options.setMaxWalkingTimeToTarget(500);
        return options;
	}
}
package com.targomo.client.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.MultiGraphSerializationFormat;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.request.ssl.SslClientGenerator;
import com.targomo.client.api.response.MultiGraphResponse;
import com.targomo.client.api.response.MultiGraphResponse.MultiGraphGeoJsonResponse;
import com.targomo.client.api.response.MultiGraphResponse.MultiGraphJsonResponse;
import com.targomo.client.api.response.TimeVectorResponse;
import com.targomo.client.api.util.IOUtil;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.message.GZipEncoder;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TimeVectorRequestTest extends RequestTest {

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
		assertEquals("ok", response.getCode());
		//TODO
//		assertEquals(13, response.getRequestTimeMillis());
//
//        assertNotNull(response.getData());
//		assertNotNull(response.getData().getNodes());
//        assertFalse(response.getData().getNodes().isEmpty());
//        assertNotNull(response.getData().getLayers());
//        assertFalse(response.getData().getLayers().isEmpty());
//        assertNotNull(response.getData().getEdges());
//        assertFalse(response.getData().getEdges().isEmpty());
//        assertNotNull(response.getData().getSupportingPoints());
//        assertFalse(response.getData().getSupportingPoints().isEmpty());
	}
//
//    @Test
//    @Ignore("To test this you will have to fill in your API key first")
//    public void get_success_API_test() throws Exception {
//
//	    String apiKey = "YOUR_API_KEY_HERE";
//
//	    //prepare travelOptions
//        TravelOptions tO = getTravelOptions();
//        tO.setServiceKey(apiKey);
//        tO.setServiceUrl("https://service.route360.net/westcentraleurope");
//
//        // Make the call
//        Client client = ClientBuilder.newClient();
//        client.register(new GZipEncoder());
//        MultiGraphRequest<MultiGraphJsonResponse> request = new MultiGraphRequest<>(client,tO,MultiGraphJsonResponse.class);
//        MultiGraphJsonResponse response = MultiGraphRequest.executeRequestJson(client,tO);
//
//        System.out.println(request.toCurl());
//
//        assertNotNull(response.getData());
//    }
//
//	@Test
//	public void get_gateway_timeout() throws Exception {
//		when(sampleResponse.getStatus()).thenReturn(Response.Status.GATEWAY_TIMEOUT.getStatusCode());
//
//        // Make the call
//        MultiGraphResponse response = MultiGraphRequest.executeRequestJson(mockClient, getTravelOptions());
//
//        assertEquals("gateway-time-out", response.getCode());
//	}
//
//	@Test(expected = TargomoClientException.class)
//	public void get_exception() throws Exception {
//		when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
//
//        // Make the call
//        MultiGraphRequest.executeRequestJson(mockClient, getTravelOptions());
//	}
//
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
//
//    @Test
//    @Ignore("System test - needs local R360 server to run - also a valid API_KEY needs to be set")
//    public void systemTestLocally() throws Exception {
//
//        Client client = SslClientGenerator.initClient();
//        client.register(GZipEncoder.class);
//        TravelOptions travelOptions = getTravelOptions();
//        travelOptions.setTravelType(TravelType.TRANSIT);
//        travelOptions.setMultiGraphSerializationFormat(MultiGraphSerializationFormat.GEOJSON);
//        for(int maxEdgeWeight = 1800; maxEdgeWeight <= 7200; maxEdgeWeight += 1800) {
//            travelOptions.setMaxEdgeWeight(maxEdgeWeight);
//            MultiGraphGeoJsonResponse response = MultiGraphRequest.executeRequestGeoJson(client, getTravelOptions());
//            try(  PrintWriter out = new PrintWriter( travelOptions.getTravelType() + Integer.toString(maxEdgeWeight) + ".geojson" )  ){
//                out.println( new ObjectMapper().writeValueAsString(response.getData()) );
//            }
//        }
//    }
//
//    @Test
//    @Ignore("System test - needs local R360 server to run - also a valid API_KEY needs to be set")
//    public void geoJSONGZipped() throws Exception {
//
//	    //this tests writes the results into files
//        Client client = SslClientGenerator.initClient();
//        TravelOptions travelOptions = getTravelOptions();
//        travelOptions.setTravelType(TravelType.BIKE);
//        travelOptions.setMultiGraphSerializationFormat(MultiGraphSerializationFormat.GEOJSON);
//        for(int maxEdgeWeight = 1800; maxEdgeWeight <= 7200; maxEdgeWeight += 1800) {
//            travelOptions.setMaxEdgeWeight(maxEdgeWeight);
//
//            WebTarget request = client.target(travelOptions.getServiceUrl())
//                    .path("v1/multigraph")
//                    .queryParam("cb", Constants.CALLBACK)
//                    .queryParam("key", travelOptions.getServiceKey());
//
//            // Execute request
//            Response response;
//            String config = RequestConfigurator.getConfig(travelOptions);
//            response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
//
//            try(  PrintWriter out = new PrintWriter( travelOptions.getTravelType() + Integer.toString(maxEdgeWeight) + ".gzip" )  ){
//                out.println( IOUtil.getResultString(response) );
//            }
//        }
//    }
}
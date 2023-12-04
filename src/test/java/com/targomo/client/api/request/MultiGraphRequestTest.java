package com.targomo.client.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.*;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.request.ssl.SslClientGenerator;
import com.targomo.client.api.response.MultiGraphResponse;
import com.targomo.client.api.response.MultiGraphResponse.*;
import com.targomo.client.api.response.ResponseCode;
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
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class MultiGraphRequestTest extends RequestTest {

	@Test
	public void get_success() throws Exception {
		// Mock success response
		when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

		// Get sample json when success response is queried
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/MultiGraphResponseSample.json");
		String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
		when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

		// Make the call
		MultiGraphJsonResponse response = MultiGraphRequest.executeRequestJson(mockClient, getTravelOptions());

		// Check result
		assertEquals(ResponseCode.OK, response.getCode());
		assertEquals(13, response.getRequestTimeMillis());

        assertNotNull(response.getData());
		assertNotNull(response.getData().getNodes());
        assertFalse(response.getData().getNodes().isEmpty());
        assertNotNull(response.getData().getLayers());
        assertFalse(response.getData().getLayers().isEmpty());
        assertNotNull(response.getData().getEdges());
        assertFalse(response.getData().getEdges().isEmpty());
        assertNotNull(response.getData().getSupportingPoints());
        assertFalse(response.getData().getSupportingPoints().isEmpty());
	}

    @Test
    @Ignore("To test this you will have to fill in your API key first")
    public void get_success_API_test() throws Exception {

	    String apiKey = "YOUR_API_KEY_HERE";

	    //prepare travelOptions
        TravelOptions tO = getTravelOptions();
        tO.setServiceKey(apiKey);
        tO.setServiceUrl("https://api.targomo.com/westcentraleurope");

        // Make the call
        Client client = ClientBuilder.newClient();
        client.register(new GZipEncoder());
        MultiGraphRequest<MultiGraphJsonResponse> request = new MultiGraphRequest<>(client,tO,MultiGraphJsonResponse.class);
        System.out.println(request.toCurl());

        MultiGraphJsonResponse response = request.get();

        assertNotNull(response.getData());
    }

    @Test(expected = TargomoClientException.class)
	public void get_gateway_timeout() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.GATEWAY_TIMEOUT.getStatusCode());

        // Make the call
        MultiGraphResponse response = MultiGraphRequest.executeRequestJson(mockClient, getTravelOptions());
	}

	@Test(expected = TargomoClientException.class)
	public void get_exception() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        // Make the call
        MultiGraphRequest.executeRequestJson(mockClient, getTravelOptions());
	}

	private TravelOptions getTravelOptions() {
		TravelOptions options = new TravelOptions();
        options.addSource(new DefaultSourceCoordinate("POI:0",13.42883045,52.5494892));
		options.setServiceKey("YOUR_API_KEY_HERE");
		options.setServiceUrl("http://127.0.0.1:8080/");
		options.setEdgeWeightType(EdgeWeightType.TIME);
		options.setMaxEdgeWeight(300);
		options.setTravelType(TravelType.BIKE);
		options.setMultiGraphSerializationDecimalPrecision(5);
        options.setMultiGraphSerializationFormat(MultiGraphSerializationFormat.JSON);
		return options;
	}

    @Test
    @Ignore("System test - needs local R360 server to run - also a valid API_KEY needs to be set")
    public void systemTestLocally() throws Exception {

        Client client = SslClientGenerator.initClient();
        client.register(GZipEncoder.class);
        TravelOptions travelOptions = getTravelOptions();
        travelOptions.setTravelType(TravelType.TRANSIT);
        travelOptions.setMultiGraphSerializationFormat(MultiGraphSerializationFormat.GEOJSON);
        for(int maxEdgeWeight = 1800; maxEdgeWeight <= 7200; maxEdgeWeight += 1800) {
            travelOptions.setMaxEdgeWeight(maxEdgeWeight);
            MultiGraphGeoJsonResponse response = MultiGraphRequest.executeRequestGeoJson(client, getTravelOptions());
            try(  PrintWriter out = new PrintWriter( travelOptions.getTravelTypes().get(0) + Integer.toString(maxEdgeWeight) + ".geojson" )  ){
                out.println( new ObjectMapper().writeValueAsString(response.getData()) );
            }
        }
    }

    @Test
    @Ignore("System test - needs local R360 server to run - also a valid API_KEY needs to be set")
    public void testH3JsonLocally() throws Exception {

        Client client = SslClientGenerator.initClient();
        client.register(GZipEncoder.class);
        TravelOptions travelOptions = getTravelOptions();
        travelOptions.setTravelType(TravelType.CAR);
        travelOptions.setMaxEdgeWeight(600);
        travelOptions.setMultiGraphLayerType(MultiGraphLayerType.H3HEXAGON);
        travelOptions.setMultiGraphLayerGeometryDetailLevel(10);
        travelOptions.setMultiGraphSerializationFormat(MultiGraphSerializationFormat.JSON);
        travelOptions.setMultiGraphSerializationH3IdFormat(MultiGraphSerializationH3IdFormat.STRING);
        MultiGraphResponse.MultiGraphH3JsonResponse<String> response = MultiGraphRequest.executeRequestH3StringJson(client, travelOptions);
        System.out.println(response.getData());

        travelOptions.setMultiGraphSerializationH3IdFormat(MultiGraphSerializationH3IdFormat.NUMERIC);
        MultiGraphResponse.MultiGraphH3JsonResponse<Long> response2 = MultiGraphRequest.executeRequestH3NumericJson(client, travelOptions);
        System.out.println(response2.getData());
    }

    @Test
    @Ignore("System test - needs local R360 server to run - also a valid API_KEY needs to be set")
    public void geoJSONGZipped() throws Exception {

	    //this tests writes the results into files
        Client client = SslClientGenerator.initClient();
        TravelOptions travelOptions = getTravelOptions();
        travelOptions.setTravelType(TravelType.BIKE);
        travelOptions.setMultiGraphSerializationFormat(MultiGraphSerializationFormat.GEOJSON);
        for(int maxEdgeWeight = 1800; maxEdgeWeight <= 7200; maxEdgeWeight += 1800) {
            travelOptions.setMaxEdgeWeight(maxEdgeWeight);

            WebTarget request = client.target(travelOptions.getServiceUrl())
                    .path("v1/multigraph")
                    .queryParam("cb", Constants.CALLBACK)
                    .queryParam("key", travelOptions.getServiceKey());

            // Execute request
            Response response;
            String config = RequestConfigurator.getConfig(travelOptions);
            response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));

            try(  PrintWriter out = new PrintWriter( travelOptions.getTravelTypes().get(0) + Integer.toString(maxEdgeWeight) + ".gzip" )  ){
                out.println( IOUtil.getResultString(response) );
            }
        }
    }

    @Test
    @Ignore("System test - needs local R360 server to run - also a valid API_KEY needs to be set")
    public void testTilehashJsonLocally() throws Exception {

        Client client = SslClientGenerator.initClient();
        client.register(GZipEncoder.class);
        TravelOptions travelOptions = getTravelOptions();
        travelOptions.setTravelType(TravelType.CAR);
        travelOptions.setMaxEdgeWeight(600);
        travelOptions.setMultiGraphLayerType(MultiGraphLayerType.TILE);
        travelOptions.setMultiGraphLayerGeometryDetailLevel(10);
        travelOptions.setMultiGraphSerializationFormat(MultiGraphSerializationFormat.TILEHASH);
        MultiGraphResponse.MultiGraphTileHashResponse response = MultiGraphRequest.executeRequestTileHash(client, travelOptions);
        System.out.println(response.getData());
    }
}
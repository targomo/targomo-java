package net.motionintelligence.client.api.request.refactored;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.EdgeWeightType;
import net.motionintelligence.client.api.enums.MultiGraphSerializationType;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.request.RequestTest;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.request.ssl.SslClientGenerator;
import net.motionintelligence.client.api.response.refactored.MultiGraphResponse;
import net.motionintelligence.client.api.util.IOUtil;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.message.GZipEncoder;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class MultiGraphRequestTest extends RequestTest {

	@Test
	public void get_success() throws Exception {
		// Mock success response
		when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

		// Get sample json when success response is queried
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/MultigraphResponseSample.json");
		String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
		when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

		// Make the call
		MultiGraphRequest request = new MultiGraphRequest(mockClient, getTravelOptions());
		MultiGraphResponse response = request.get();

		// Check result
		assertEquals("ok", response.getCode());
		assertEquals(374, response.getRequestTimeMillis());

		assertNotNull(response.getData());
	}

	@Test
	public void get_gateway_timeout() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.GATEWAY_TIMEOUT.getStatusCode());

		MultiGraphRequest request = new MultiGraphRequest(mockClient, getTravelOptions());
		MultiGraphResponse response = request.get();

		assertEquals("gateway-time-out", response.getCode());
	}

	@Test(expected = Route360ClientException.class)
	public void get_exception() throws Exception {
		when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        MultiGraphRequest request = new MultiGraphRequest(mockClient, getTravelOptions());
        request.get();
	}

	private TravelOptions getTravelOptions() {
		TravelOptions options = new TravelOptions();
        options.addSource(new DefaultSourceCoordinate("POI:0",13.42883045,52.5494892));
		options.setServiceKey("YOUR_API_KEY_HERE");
		options.setServiceUrl("http://127.0.0.1:8080/");
		options.setEdgeWeightType(EdgeWeightType.TIME);
		options.setMaxEdgeWeight(300);
		options.setTravelType(TravelType.BIKE);
		options.setDecimalPrecision(5);
		return options;
	}

    @Test
    @Ignore("System test - needs local R360 server to run - also a valid API_KEY needs to be set")
    public void systemTestLocally() throws Exception {

        Client client = SslClientGenerator.initClient();
        client.register(GZipEncoder.class);
        TravelOptions travelOptions = getTravelOptions();
        travelOptions.setTravelType(TravelType.TRANSIT);
        travelOptions.setMultiGraphSerializationType(MultiGraphSerializationType.GEOJSON);
        for(int maxEdgeWeight = 1800; maxEdgeWeight <= 7200; maxEdgeWeight += 1800) {
            travelOptions.setMaxEdgeWeight(maxEdgeWeight);
            MultiGraphRequest request = new MultiGraphRequest(client, travelOptions);
            MultiGraphResponse response = request.get();
            try(  PrintWriter out = new PrintWriter( travelOptions.getTravelType() + Integer.toString(maxEdgeWeight) + ".geojson" )  ){
                out.println( new ObjectMapper().writeValueAsString(response.getData()) );
            }
        }
    }

    @Test
    @Ignore("System test - needs local R360 server to run - also a valid API_KEY needs to be set")
    public void geoJSONGZipped() throws Exception {

        Client client = SslClientGenerator.initClient();
        TravelOptions travelOptions = getTravelOptions();
        travelOptions.setTravelType(TravelType.BIKE);
        travelOptions.setMultiGraphSerializationType(MultiGraphSerializationType.GEOJSON);
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

            try(  PrintWriter out = new PrintWriter( travelOptions.getTravelType() + Integer.toString(maxEdgeWeight) + ".gzip" )  ){
                out.println( IOUtil.getResultString(response) );
            }
        }
    }
}
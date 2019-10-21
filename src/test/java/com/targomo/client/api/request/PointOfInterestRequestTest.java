package com.targomo.client.api.request;

import com.google.common.collect.Sets;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.PathSerializerType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.response.PointOfInterestResponse;
import com.targomo.client.api.statistic.PoiType;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PointOfInterestRequestTest extends RequestTest {

    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

//         Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/PointOfInterestResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);
        PointOfInterestRequest poiRequest = new PointOfInterestRequest(mockClient, getTravelOptions());
        PointOfInterestResponse poiResponse = poiRequest.get();
        HashMap<String,PointOfInterestResponse.POI> result = poiResponse.getResultAsMap();
        assertEquals(4, result.size());

        // old poi service ids
        assert(result.keySet().contains("5846976255"));
        assert(result.get("5846976255").getId().equals("5846976255"));

        // new poi service ids
        assert(result.keySet().contains("1_1639569032"));
        assert(result.get("1_1639569032").getId().equals("1_1639569032"));
    }

    @Test
    @Ignore("To test this you will have to fill in your API key first")
    public void get_success_API_test() throws Exception {
        Client client = ClientBuilder.newClient();

        PointOfInterestRequest poiRequest = new PointOfInterestRequest(client, getTravelOptions());
        PointOfInterestResponse poiResponse = poiRequest.get();
        HashMap<String,PointOfInterestResponse.POI> result = poiResponse.getResultAsMap();
        assertNotNull(result);
        assertTrue( result.size() > 0);
    }


    @Test(expected = TargomoClientException.class)
    public void get_exception() throws Exception {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        TravelOptions options = getTravelOptions();
        PointOfInterestRequest poiRequest = new PointOfInterestRequest(mockClient, options);
        poiRequest.get();
    }

    private TravelOptions getTravelOptions() {

        TravelOptions options = new TravelOptions();
        options.setMaxEdgeWeight(3600);
        options.addSource(new DefaultSourceCoordinate("first", 9.971495, 53.556482));
        options.setTravelTimes(Arrays.asList(600, 1200, 1800));
        options.setTravelType(TravelType.CAR);
        options.setPathSerializer(PathSerializerType.COMPACT_PATH_SERIALIZER);
        options.setOsmTypes(Sets.newHashSet(new PoiType("amenity", "bar")));
        options.setEdgeWeightType(EdgeWeightType.TIME);
        options.setServiceKey("INSERT_YOUR_KEY_HERE");
        options.setServiceUrl("https://api.targomo.com/westcentraleurope/");

        return options;
    }

}

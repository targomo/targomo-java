package com.targomo.client.api.request;

import com.google.common.collect.Sets;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.PathSerializerType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.AbstractGeometry;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.geo.DefaultSourceGeometry;
import com.targomo.client.api.pojo.LocationProperties;
import com.targomo.client.api.response.PointOfInterestGravitationResponse;
import com.targomo.client.api.response.PointOfInterestResponse;
import com.targomo.client.api.response.PointOfInterestSummaryResponse;
import com.targomo.client.api.statistic.PoiType;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PointOfInterestRequestTest extends RequestTest {

    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
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

        assertEquals(4, poiResponse.getEdgeWeights().size());
    }

    @Test
    public void getPOIsWithinGeometry_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/PointOfInterestGeometryResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);
        PointOfInterestRequest poiRequest = new PointOfInterestRequest(mockClient, getTravelOptions());
        PointOfInterestResponse poiResponse = poiRequest.getPOIsWithinGeometry();
        HashMap<String,PointOfInterestResponse.POI> result = poiResponse.getResultAsMap();
        assertEquals(4, result.size());

        // old poi service ids
        assert(result.keySet().contains("5846976255"));
        assert(result.get("5846976255").getId().equals("5846976255"));

        // new poi service ids
        assert(result.keySet().contains("1_1639569032"));
        assert(result.get("1_1639569032").getId().equals("1_1639569032"));

        assertEquals(0, poiResponse.getEdgeWeights().size());
    }

    @Test
    public void getSummary_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/PointOfInterestSummaryResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);
        PointOfInterestRequest poiRequest = new PointOfInterestRequest(mockClient, getTravelOptions());
        PointOfInterestSummaryResponse poiResponse = poiRequest.getSummary();
        PointOfInterestSummaryResponse.POISummary result = poiResponse.getSummary();

        Map<String, Integer> expectedOsmTypesCount = new HashMap<>();
        expectedOsmTypesCount.put("cuisine=bavarian", 1);
        expectedOsmTypesCount.put("cuisine=diner", 1);
        expectedOsmTypesCount.put("building=supermarket", 6);
        expectedOsmTypesCount.put("building=apartments", 3);
        Map<String, Integer> expectedGroupIdCount = new HashMap<>();
        expectedGroupIdCount.put("uncategorized", 88);
        expectedGroupIdCount.put("restaurant", 469);
        Map<String, Integer> expectedClusterIdCount = new HashMap<>();
        expectedClusterIdCount.put("c_1", 88);
        expectedClusterIdCount.put("c_2", 469);

        assertEquals(557, result.getTotalPoi());
        assertEquals(4, result.getOsmTypesCount().size());
        Assertions.assertThat(result.getOsmTypesCount()).containsAllEntriesOf(expectedOsmTypesCount);
        assertEquals(2, result.getGroupIdCount().size());
        Assertions.assertThat(result.getGroupIdCount()).containsAllEntriesOf(expectedGroupIdCount);
        assertEquals(2, result.getClusterIdCount().size());
        Assertions.assertThat(result.getClusterIdCount()).containsAllEntriesOf(expectedClusterIdCount);
    }

    @Test
    public void getPOIsWithinGeometrySummary_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/PointOfInterestSummaryResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);
        PointOfInterestRequest poiRequest = new PointOfInterestRequest(mockClient, getTravelOptions());
        PointOfInterestSummaryResponse poiResponse = poiRequest.getPOIsWithinGeometrySummary();
        PointOfInterestSummaryResponse.POISummary result = poiResponse.getSummary();

        Map<String, Integer> expectedOsmTypesCount = new HashMap<>();
        expectedOsmTypesCount.put("cuisine=bavarian", 1);
        expectedOsmTypesCount.put("cuisine=diner", 1);
        expectedOsmTypesCount.put("building=supermarket", 6);
        expectedOsmTypesCount.put("building=apartments", 3);
        Map<String, Integer> expectedGroupIdCount = new HashMap<>();
        expectedGroupIdCount.put("uncategorized", 88);
        expectedGroupIdCount.put("restaurant", 469);
        Map<String, Integer> expectedClusterIdCount = new HashMap<>();
        expectedClusterIdCount.put("c_1", 88);
        expectedClusterIdCount.put("c_2", 469);

        assertEquals(557, result.getTotalPoi());
        assertEquals(4, result.getOsmTypesCount().size());
        Assertions.assertThat(result.getOsmTypesCount()).containsAllEntriesOf(expectedOsmTypesCount);
        assertEquals(2, result.getGroupIdCount().size());
        Assertions.assertThat(result.getGroupIdCount()).containsAllEntriesOf(expectedGroupIdCount);
        assertEquals(2, result.getClusterIdCount().size());
        Assertions.assertThat(result.getClusterIdCount()).containsAllEntriesOf(expectedClusterIdCount);
    }

    @Test
    public void getGravitation_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/PointOfInterestGravitationResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);
        PointOfInterestRequest poiRequest = new PointOfInterestRequest(mockClient, getTravelOptionsWithSourceProperties());
        PointOfInterestGravitationResponse poiResponse = poiRequest.getGravitationAnalysis();
        Map<String, PointOfInterestGravitationResponse.GravitationResult> result = poiResponse.getGravitationResult();

        Map<String, Float> expectedClusters = new HashMap<>();
        expectedClusters.put("c_1", 88.21f);
        expectedClusters.put("c_2", 469.34f);

        PointOfInterestGravitationResponse.GravitationResult resultForLoc = result.get("first");
        Assertions.assertThat(resultForLoc.getAll()).isEqualTo(557.55f);
        Assertions.assertThat(resultForLoc.getClusters()).containsAllEntriesOf(expectedClusters);
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
        return getTravelOptions(new DefaultSourceCoordinate("first", 9.971495, 53.556482));
    }

    private TravelOptions getTravelOptionsWithSourceProperties() {
        return getTravelOptions(new DefaultSourceCoordinate("first", 9.971495, 53.556482, new LocationProperties(null, true, 1.2)));
    }

    private TravelOptions getTravelOptions(Coordinate source) {
        TravelOptions options = new TravelOptions();
        options.setMaxEdgeWeight(3600);
        options.addSource(source);
        options.setTravelTimes(Arrays.asList(600, 1200, 1800));
        options.setTravelType(TravelType.CAR);
        options.setPathSerializer(PathSerializerType.COMPACT_PATH_SERIALIZER);
        options.setOsmTypes(Sets.newHashSet(new PoiType("amenity", "bar")));
        options.setEdgeWeightType(EdgeWeightType.TIME);
        options.setServiceKey("INSERT_YOUR_KEY_HERE");
        options.setServiceUrl("https://api.targomo.com/westcentraleurope/");

        String testPoly = "{\"type\":\"Polygon\",\"coordinates\":[[[51.98,13.98],[52.0,13.98],[52.0,14.0],[51.98,14.0],[51.98,13.98]]]}";
        AbstractGeometry geometry = new DefaultSourceGeometry("location_0", testPoly, 4326, null, null);
        options.setFilterGeometryForPOIs(geometry);

        return options;
    }

}

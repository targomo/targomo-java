package com.targomo.client.api.request;

import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.quality.Location;
import com.targomo.client.api.quality.criterion.CriterionDefinition;
import com.targomo.client.api.quality.criterion.CriterionType;
import com.targomo.client.api.quality.criterion.StatisticsReachabilityCriterionDefinition;
import com.targomo.client.api.response.ScoreResponse;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ScoreRequestTest extends RequestTest {
    private static final String CORE_URL = "https://api.targomo.com/westcentraleurope";

    @Test
    public void get_success() throws Exception {

        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/QualityScoreResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

        Map<String, CriterionDefinition> criteria = new HashMap<>();
        criteria.put("criterion1", StatisticsReachabilityCriterionDefinition.builder()
                .statisticCollectionId(100)
                .statisticsIds(Collections.singletonList(Short.valueOf("0")))
                .type(CriterionType.STATISTICS_DISTANCE)
                .edgeWeight(EdgeWeightType.TIME)
                .maxEdgeWeight(400)
                .travelMode(new CaseInsensitiveMap(Collections.singletonMap("car", new LinkedHashMap<>())))
                .elevation(true)
                .coreServiceUrl(CORE_URL)
                .build()
        );

        List<Location> locations = new ArrayList<>();
        locations.add(Location.builder()
                .id("loc1")
                .lat(52.49368)
                .lng(13.360687)
                .build());

        ScoreRequest scoreRequest = new ScoreRequest(mockClient, criteria, locations, Collections.emptyList(), "INSERT_YOUR_KEY_HERE", "https://api.targomo.com/westcentraleurope", false);
        ScoreResponse scoreResponse = scoreRequest.get();

        // Check result
        assertEquals("Scores calculated", scoreResponse.getMessage());
        assertEquals(0, scoreResponse.getErrors().length());
        assertTrue(scoreResponse.getData().has("loc1"));
        assertTrue(scoreResponse.getData().getJSONObject("loc1").getJSONObject("scores").has("criterion1"));

        assertEquals(28804.024527683574, scoreResponse.getData().getJSONObject("loc1").getJSONObject("scores").getDouble("criterion1"), 0.00001);
    }
    @Test
    public void get_errors() throws Exception {

        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/QualityScoreErrorsResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

        Map<String, CriterionDefinition> criteria = new HashMap<>();
        criteria.put("criterion1", StatisticsReachabilityCriterionDefinition.builder()
                .statisticCollectionId(28)
                .statisticsIds(Collections.singletonList(Short.valueOf("0")))
                .type(CriterionType.STATISTICS_DISTANCE)
                .edgeWeight(EdgeWeightType.TIME)
                .maxEdgeWeight(400)
                .travelMode(new CaseInsensitiveMap(Collections.singletonMap("car", new LinkedHashMap<>())))
                .elevation(true)
                .coreServiceUrl(CORE_URL)
                .build()
        );

        List<Location> locations = new ArrayList<>();
        locations.add(Location.builder()
                .id("loc1")
                .lat(52.49368)
                .lng(13.360687)
                .build());

        ScoreRequest scoreRequest = new ScoreRequest(mockClient, criteria, locations, Collections.emptyList(), "INSERT_YOUR_KEY_HERE", "https://api.targomo.com/westcentraleurope", false);
        ScoreResponse scoreResponse = scoreRequest.get();

        // Check result
        assertEquals("Scores calculated", scoreResponse.getMessage());
        assertEquals(1, scoreResponse.getErrors().length());
        assertTrue(scoreResponse.getData().has("loc1"));
        assertTrue(scoreResponse.getData().getJSONObject("loc1").getJSONObject("scores").has("criterion1"));

        assertTrue(null, scoreResponse.getData().getJSONObject("loc1").getJSONObject("scores").isNull("criterion1"));
    }
}

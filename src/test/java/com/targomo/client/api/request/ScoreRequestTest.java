package com.targomo.client.api.request;

import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.quality.PublicLocation;
import com.targomo.client.api.quality.criterion.CriterionDefinition;
import com.targomo.client.api.quality.criterion.CriterionType;
import com.targomo.client.api.quality.criterion.StatisticsReachabilityCriterionDefinition;
import com.targomo.client.api.response.ScoreResponse;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ScoreRequestTest extends RequestTest {
    private static final String CORE_URL = "https://api.targomo.com/westcentraleurope";
    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        // Make the call

        Map<String, CriterionDefinition> criteria = new HashMap<>();
        criteria.put("criterion1", StatisticsReachabilityCriterionDefinition.builder()
                .statisticCollectionId(100)
                .statisticsIds(Collections.singletonList(Short.valueOf("0")))
                .type(CriterionType.STATISTICS_DISTANCE)
                .edgeWeight(EdgeWeightType.TIME)
                .maxEdgeWeight(900)
                .travelMode(new CaseInsensitiveMap(Collections.singletonMap("bike", new LinkedHashMap<>())))
                .elevation(true)
                .coreServiceUrl(CORE_URL)
                .build()
        );

        List<PublicLocation> locations = new ArrayList<>();
        locations.add(PublicLocation.builder()
                .id("loc1")
                .lat(52.49368)
                .lng(13.360687)
                .build());

        ScoreRequest scoreRequest = new ScoreRequest("https://api.targomo.com/quality", "1S10GQZPB1ZWNJV0A2Y5361557401", criteria, locations);
        ScoreResponse scoreResponse = scoreRequest.get();

        // Check result
        assertEquals("Scores calculated", scoreResponse.getMessage());
        assertEquals(0, scoreResponse.getErrors().length());
        assertTrue(scoreResponse.getData().has("loc1"));
        assertTrue(scoreResponse.getData().getJSONObject("loc1").getJSONObject("scores").has("criterion1"));

        assertEquals(23609.8670193027, scoreResponse.getData().getJSONObject("loc1").getJSONObject("scores").getDouble("criterion1"), 0.00001);
    }
}

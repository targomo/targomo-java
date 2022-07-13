package com.targomo.client.api.request;

import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.quality.Location;
import com.targomo.client.api.quality.criterion.CriterionDefinition;
import com.targomo.client.api.quality.criterion.CriterionType;
import com.targomo.client.api.quality.criterion.StatisticsReachabilityCriterionDefinition;
import com.targomo.client.api.response.ScoreResponse;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RatingRequestTest extends RequestTest {
    @Test
    public void get_success() throws Exception {

        List<Location> locations = new ArrayList<>();
        locations.add(Location.builder()
                .id("loc1")
                .lat(52.515)
                .lng(13.403)
                .build());

        RatingRequest ratingRequest = new RatingRequest("https://api.targomo.com/quality", "1S10GQZPB1ZWNJV0A2Y5361557401", "ratingTest", locations);
        ScoreResponse ratingResponse = ratingRequest.get();

        // Check result
        assertEquals("Rating 'ratingTest' for 1 locations.", ratingResponse.getMessage());
        assertEquals(0, ratingResponse.getErrors().length());
        assertTrue(ratingResponse.getData().has("loc1"));
        assertTrue(ratingResponse.getData().getJSONObject("loc1").getJSONObject("scores").has("closestPost30min"));

        assertEquals(0.06758197693802682, ratingResponse.getData().getJSONObject("loc1").getJSONObject("scores").getDouble("closestPost30min"), 0.00001);
    }
}

package com.targomo.client.api.request;

import com.targomo.client.api.quality.PublicLocation;
import com.targomo.client.api.response.ScoreResponse;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RatingRequestTest extends RequestTest {
    @Test
    public void get_success() throws Exception {

        List<PublicLocation> locations = new ArrayList<>();
        locations.add(PublicLocation.builder()
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

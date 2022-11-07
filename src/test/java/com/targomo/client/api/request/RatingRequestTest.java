package com.targomo.client.api.request;

import com.targomo.client.api.quality.Location;
import com.targomo.client.api.response.ScoreResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RatingRequestTest extends RequestTest {
    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/QualityRatingResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

        List<Location> locations = new ArrayList<>();
        locations.add(Location.builder()
                .id("loc1")
                .lat(52.515)
                .lng(13.403)
                .build());

        RatingRequest ratingRequest = new RatingRequest(mockClient, locations, Collections.emptyList(), "https://api.targomo.com/quality", "INSERT_YOUR_KEY_HERE", false, false, true,"ratingTest");
        ScoreResponse ratingResponse = ratingRequest.get();

        // Check result
        assertEquals("Rating 'ratingTest' for 1 locations.", ratingResponse.getMessage());
        assertEquals(0, ratingResponse.getErrors().length());
        assertTrue(ratingResponse.getData().has("loc1"));
        assertTrue(ratingResponse.getData().getJSONObject("loc1").getJSONObject("scores").has("closestPost30min"));

        assertEquals(0.06758197693802682, ratingResponse.getData().getJSONObject("loc1").getJSONObject("scores").getDouble("closestPost30min"), 0.00001);

        assertEquals(86, ratingResponse.getData().getJSONObject("loc1").getJSONObject("ratings").getJSONObject("nahversorgung").getJSONObject("0").getInt("referenceAreaId"));
        assertEquals("Deutschland", ratingResponse.getData().getJSONObject("loc1").getJSONObject("ratings").getJSONObject("nahversorgung").getJSONObject("0").getString("referenceAreaName"));
        assertEquals(0.7909763956592565, ratingResponse.getData().getJSONObject("loc1").getJSONObject("ratings").getJSONObject("nahversorgung").getJSONObject("0").getDouble("groupRating"), 0.00001);
    }
}

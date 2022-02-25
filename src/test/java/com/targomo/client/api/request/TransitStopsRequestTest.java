package com.targomo.client.api.request;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.response.TransitStation;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class TransitStopsRequestTest extends RequestTest {

    @Test
    public void get_success() throws Exception {
        // Mock success response
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Get sample json when success response is queried
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/TransitStopsResponse.json");
        String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);

        TransitStopsRequest transitStopsRequest = new TransitStopsRequest(mockClient, getTravelOptions());
        Map<String, List<TransitStation>> transitStopsResponse = transitStopsRequest.get();

        TransitStation.ReachableTransitStopTime stop1 = new TransitStation.ReachableTransitStopTime();
        stop1.setTime(300);
        stop1.setName("Segitzdamm (Berlin)");
        stop1.setLine("140");
        stop1.setEndStation("S Ostbahnhof (Berlin)");
        TransitStation expectedTransitStation = new TransitStation();
        expectedTransitStation.setName("Prinzenstr./Ritterstr. (Berlin)");
        expectedTransitStation.setEdgeType("time");
        expectedTransitStation.setEdgeWeight(119);
        expectedTransitStation.setNextStops(Collections.singleton(stop1));

        Assertions.assertThat(transitStopsResponse).hasSize(1)
                .containsKey("id0");
        Assertions.assertThat(transitStopsResponse.get("id0"))
                .hasSize(1);
        Assertions.assertThat(transitStopsResponse.get("id0").get(0))
                .isEqualToComparingFieldByFieldRecursively(expectedTransitStation);
    }

    @Test
    public void get_exception() {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        TravelOptions options = getTravelOptions();
        TransitStopsRequest reachabilityRequest = new TransitStopsRequest(mockClient, options);
        Assertions.assertThatThrownBy(reachabilityRequest::get)
                .isInstanceOf(TargomoClientException.class);
    }

	private TravelOptions getTravelOptions() {
		TravelOptions options = new TravelOptions();
		options.addSource(new DefaultSourceCoordinate("id0", 10.639872441947901, -17.37236573607632, TravelType.CAR));
        options.setEdgeWeightType(EdgeWeightType.TIME);
        options.setMaxEdgeWeight(900);
        options.setTravelType(TravelType.CAR);

        options.setNextStopsStartTime(0);
        options.setNextStopsEndTime(3600);

		options.setServiceKey("INSERT_YOUR_KEY_HERE");
		options.setServiceUrl("https://api.targomo.com/na_northeast/");
		return options;
	}

}
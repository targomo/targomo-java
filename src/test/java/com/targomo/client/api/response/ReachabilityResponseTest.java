package com.targomo.client.api.response;

import com.targomo.client.api.enums.PathSerializerType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import com.targomo.client.api.util.JsonUtil;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReachabilityResponseTest {
	@Test
	public void mapResults() throws Exception {
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/ReachabilityResponse.json");

		String sampleJson = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
		JSONObject sampleObject = JsonUtil.parseString(sampleJson);
		TravelOptions options = getTravelOptions();

		ReachabilityResponse reachabilityResponse = new ReachabilityResponse(options, sampleObject, 123);
		assertEquals("ok", reachabilityResponse.getCode());
		Map<String, Integer> resultMap = reachabilityResponse.getTravelTimes();
		JSONArray sampleDataArray = sampleObject.getJSONArray("data");
		assertEquals(sampleDataArray.length(), resultMap.size());
		for (int i = 0; i < sampleDataArray.length(); i++) {
			JSONObject element = (JSONObject) sampleDataArray.get(i);
			String sourceId = element.getString("id");
			Integer travelTime = element.getInt("travelTime");
			assertTrue(resultMap.containsKey(sourceId));
			assertNotNull(travelTime);
		}
	}

	private TravelOptions getTravelOptions() {
		TravelOptions options = new TravelOptions();
		options.setTravelTimes(Arrays.asList(600, 1200, 1800));
		options.setTravelType(TravelType.CAR);
		options.setPathSerializer(PathSerializerType.COMPACT_PATH_SERIALIZER);
		options.addSource(new DefaultSourceCoordinate("id0", 10.639872441947901, -17.37236573607632, TravelType.CAR));
		options.addSource(new DefaultSourceCoordinate("id1", 49.75913939827413, -75.83100508496594, TravelType.CAR));
		options.addSource(new DefaultSourceCoordinate("id2", 64.68336864385466, 62.545199028051314, TravelType.CAR));
		options.addTarget(new DefaultTargetCoordinate("id0", -84.01440151465849, 41.805968836500426));
		options.addTarget(new DefaultTargetCoordinate("id1", 71.34898703519798, -36.16051650737182));
		options.addTarget(new DefaultTargetCoordinate("id2", 57.12383436652155, 33.64895304828113));
		options.setServiceKey("INSERT_YOUR_KEY_HERE");
		options.setServiceUrl("https://api.targomo.com/germany/");
		return options;
	}

}
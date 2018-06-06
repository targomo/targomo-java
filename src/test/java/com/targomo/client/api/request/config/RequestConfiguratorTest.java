package com.targomo.client.api.request.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.StatisticTravelOptions;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import org.apache.commons.io.IOUtils;
import org.boon.Maps;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RequestConfiguratorTest {
    @Test
    public void getConfig_check_polygons() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        try {
	        // Generate input
            TravelOptions options = new TravelOptions();
            options.setTravelTimes(Arrays.asList(600, 1200, 1800, 2400, 3000, 3600));
            options.setTravelType(TravelType.TRANSIT);
            options.addSource(new DefaultSourceCoordinate("id1", -73.976636, 40.608155));
            options.setServiceKey("INSERT_YOUR_KEY_HERE");
            options.setServiceUrl("https://api.targomo.com/na_northeast/");
            options.setDate(20161020);
            options.setTime(55852);
            options.setSrid(25833);
	        options.setEdgeWeightType(EdgeWeightType.DISTANCE);
            options.setMaxTransfers(2);
            options.getTravelTimeFactors().put("all",0.9);
            options.getTravelTimeFactors().put("motorway",0.7);

	        // Run configurator && get object
            String cfg = RequestConfigurator.getConfig(options);
            JSONObject actualObject = new JSONObject(cfg);

	        // Load sample json & load object
            String sampleJson = IOUtils.toString(classLoader.getResourceAsStream("data/PolygonRequestCfgSample.json"));
            JSONObject sampleObject = new JSONObject(sampleJson);

	        // Compare two objects
	        checkPolygons(actualObject, sampleObject);
            Assert.assertEquals(sampleObject.getString(Constants.SOURCES), actualObject.getString(Constants.SOURCES));
	        Assert.assertEquals(
                    sampleObject.getString(Constants.ENABLE_ELEVATION),
                    actualObject.getString(Constants.ENABLE_ELEVATION)
            );
            Assert.assertEquals(sampleObject.getString(Constants.REVERSE), actualObject.getString(Constants.REVERSE));
	        Assert.assertEquals(sampleObject.getString(Constants.EDGE_WEIGHT), actualObject.getString(Constants.EDGE_WEIGHT));

            Assert.assertEquals(sampleObject.getString(Constants.TRAVEL_TIME_FACTORS), actualObject.getString(Constants.TRAVEL_TIME_FACTORS));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readTravelOptionsWithJackson() throws IOException {
        StatisticTravelOptions parsed = new ObjectMapper()
                .readValue( "{\n" +
                                "            \"travelType\": \"car\",\n" +
                                "            \"elevationEnabled\": true,\n" +
                                "            \"maxEdgeWeight\": 7200,\n" +
                                "            \"travelTimeFactors\":{\"all\":1.5},\n" +
                                "            \"edgeWeight\": \"time\",\n" +
                                "            \"serviceUrl\": \"https://api.targomo.com/westcentraleurope/\",\n" +
                                "            \"serviceKey\": \"{{api-key}}\"\n" +
                                "        }",
                        StatisticTravelOptions.class);

        Assert.assertEquals(parsed.getTravelTimeFactors(), Maps.map("all",1.5));
    }

    @Test
    public void getConfig_many_targets() throws Exception {

        try {
	        // Generate random input
	        final int numOfSources = 1000;
	        final int numOfTargets = 500000;

            TravelOptions options = new TravelOptions();
            options.setMaxRoutingTime(3600);

	        // Sources
	        double min = -90;
	        double max = 90;
	        for (int i=0; i < numOfSources; i++) {
		        double randomX = ThreadLocalRandom.current().nextDouble(min, max);
		        double randomY = ThreadLocalRandom.current().nextDouble(min, max);
		        options.addSource(new DefaultSourceCoordinate("id" + i, randomX, randomY));
	        }

	        // Targets
	        Map<String, Coordinate> targets = new HashMap<>();
            for (int i=0; i < numOfTargets; i++) {
	            double randomX = ThreadLocalRandom.current().nextDouble(min, max);
	            double randomY = ThreadLocalRandom.current().nextDouble(min, max);
	            targets.put("id" + i, new DefaultTargetCoordinate("id" + i, randomX, randomY));
            }
            options.setTargets(targets);

            options.setTravelType(TravelType.CAR);
            options.setServiceKey("ENTER YOUR KEY HERE");
            options.setServiceUrl("https://api.targomo.com/germany/");
	        
	        // Run configurator & create JSON Object
            String cfg = RequestConfigurator.getConfig(options);
	        JSONObject actualObject = new JSONObject(cfg);

	        // Load sample json file & create sample object
	        InputStream resource = getClass().getClassLoader().getResourceAsStream("data/TimeRequestCfgSample.json");
	        String sampleJson = IOUtils.toString(resource, Charset.forName("UTF-8"));
	        JSONObject sampleObject = new JSONObject(sampleJson);

	        // Input is random at each test, so we only compare array lengths to make sure all data is converted
	        Assert.assertEquals(numOfSources, actualObject.getJSONArray(Constants.SOURCES).length());
	        Assert.assertEquals(numOfTargets, actualObject.getJSONArray(Constants.TARGETS).length());

            Assert.assertEquals(sampleObject.get(Constants.MAX_EDGE_WEIGTH),
                    actualObject.get(Constants.MAX_EDGE_WEIGTH));

            Assert.assertEquals(sampleObject.get(Constants.EDGE_WEIGHT),
                    actualObject.get(Constants.EDGE_WEIGHT));

            Assert.assertEquals(sampleObject.get(Constants.POLYGON_INTERSECTION_MODE),
			        actualObject.get(Constants.POLYGON_INTERSECTION_MODE));

	        Assert.assertEquals(sampleObject.get(Constants.ENABLE_ELEVATION),
			        actualObject.get(Constants.ENABLE_ELEVATION));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void checkPolygons(final JSONObject actualObject, final JSONObject sampleObject) throws JSONException {
		
		JSONObject actualPolygon = actualObject.getJSONObject(Constants.POLYGON);
		JSONObject samplePolygon = sampleObject.getJSONObject(Constants.POLYGON);

		Assert.assertEquals(samplePolygon.get(Constants.POLYGON_VALUES).toString(),
				actualPolygon.get(Constants.POLYGON_VALUES).toString());

		Assert.assertEquals(samplePolygon.get(Constants.POLYGON_INTERSECTION_MODE),
				actualPolygon.get(Constants.POLYGON_INTERSECTION_MODE));

		Assert.assertEquals(samplePolygon.get(Constants.POINT_REDUCTION),
				actualPolygon.get(Constants.POINT_REDUCTION));

		Assert.assertEquals(samplePolygon.get(Constants.MIN_POLYGON_HOLE_SIZE),
				actualPolygon.get(Constants.MIN_POLYGON_HOLE_SIZE));
		
		Assert.assertEquals(samplePolygon.get(Constants.SRID),
				actualPolygon.get(Constants.SRID));
	}
}
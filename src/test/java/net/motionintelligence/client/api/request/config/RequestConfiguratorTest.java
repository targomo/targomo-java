package net.motionintelligence.client.api.request.config;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.EdgeWeightType;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.geo.DefaultTargetCoordinate;
import org.apache.commons.io.IOUtils;
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
            options.setServiceUrl("https://service.route360.net/na_northeast/");
            options.setDate(20161020);
            options.setTime(55852);
            options.setSrid(25833);
	        options.setEdgeWeightType(EdgeWeightType.DISTANCE);

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

        } catch (IOException e) {
            e.printStackTrace();
        }
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
            options.setServiceUrl("https://service.route360.net/germany/");
	        
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

	        Assert.assertEquals(sampleObject.get(Constants.MAX_ROUTING_TIME),
			        actualObject.get(Constants.MAX_ROUTING_TIME));

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
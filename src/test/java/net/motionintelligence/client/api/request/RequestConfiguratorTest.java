package net.motionintelligence.client.api.request;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.config.RequestConfigurator;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.geo.DefaultTargetCoordinate;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RequestConfiguratorTest {
    @Test
    public void getPolygonConfig() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            TravelOptions options = new TravelOptions();
            options.setTravelTimes(Arrays.asList(600, 1200, 1800, 2400, 3000, 3600));
            options.setTravelType(TravelType.TRANSIT);
            options.addSource(new DefaultSourceCoordinate("id1", -73.976636, 40.608155));
            options.setServiceKey("INSERT_YOUR_KEY_HERE");
            options.setServiceUrl("https://service.route360.net/na_northeast/");
            options.setDate(20161020);
            options.setTime(55852);

            String cfg = RequestConfigurator.getPolygonConfig(options);

            JSONObject actualObject = new JSONObject(cfg);

            String sampleJson = IOUtils.toString(classLoader.getResourceAsStream("data/PolygonRequestCfgSample.json"));
            JSONObject sampleObject = new JSONObject(sampleJson);

            Assert.assertEquals(sampleObject.getString(Constants.POLYGON), actualObject.getString(Constants.POLYGON));
            Assert.assertEquals(sampleObject.getString(Constants.SOURCES), actualObject.getString(Constants.SOURCES));
            Assert.assertEquals(
                    sampleObject.getString(Constants.ENABLE_ELEVATION),
                    actualObject.getString(Constants.ENABLE_ELEVATION)
            );
            Assert.assertEquals(sampleObject.getString(Constants.REVERSE), actualObject.getString(Constants.REVERSE));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTimeConfig() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            TravelOptions options = new TravelOptions();
            options.setMaxRoutingTime(3600);
	        double min = -90;
	        double max = 90;
	        Map<String, Coordinate> targets = new HashMap<>();
            for (int i=0; i < 500; i++) {
	            double randomX = ThreadLocalRandom.current().nextDouble(min, max);
	            double randomY = ThreadLocalRandom.current().nextDouble(min, max);
	            options.addSource(new DefaultSourceCoordinate("id" + i, randomX, randomY));
	            randomX = ThreadLocalRandom.current().nextDouble(min, max);
	            randomY = ThreadLocalRandom.current().nextDouble(min, max);
	            targets.put("id" + i, new DefaultTargetCoordinate("id" + i, randomX, randomY));
            }
            options.setTargets(targets);
            options.setTravelType(TravelType.CAR);
            options.setServiceKey("ENTER YOUR KEY HERE");
            options.setServiceUrl("https://service.route360.net/germany/");

            options.setDate(20161020);
            options.setTime(55852);

            String cfg = RequestConfigurator.getTimeConfig(options);

            String sampleJson = IOUtils.toString(classLoader.getResourceAsStream("data/TimeRequestCfgSample.json"));

	        JSONObject actualObject = new JSONObject(cfg);
	        JSONObject sampleObject = new JSONObject(sampleJson);

	        Assert.assertEquals(sampleObject.getJSONArray(Constants.SOURCES).length(),
			        actualObject.getJSONArray(Constants.SOURCES).length());

	        Assert.assertEquals(sampleObject.getJSONArray(Constants.TARGETS).length(),
			        actualObject.getJSONArray(Constants.TARGETS).length());

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

}
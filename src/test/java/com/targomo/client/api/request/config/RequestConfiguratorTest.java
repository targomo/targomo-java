package com.targomo.client.api.request.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.StatisticTravelOptions;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.*;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.geo.DefaultSourceGeometry;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import com.targomo.client.api.util.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestConfiguratorTest {

    @Test
    public void testTimeVectorConfig() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            // Generate input
            TravelOptions options = new TravelOptions();
            options.addSource(new DefaultSourceCoordinate("POI:1",8.620987,47.384197));
            options.addSource(new DefaultSourceCoordinate("POI:2",8.497925,47.385334));
            options.addTarget(new DefaultSourceCoordinate("Home 3",8.511658,47.322069));
            options.addTarget(new DefaultSourceCoordinate("Home 4",8.572083,47.439235));
            options.setServiceKey("YOUR_API_KEY_HERE");
            options.setServiceUrl("http://127.0.0.1:8080/");
            options.setEdgeWeightType(EdgeWeightType.TIME);
            options.setMaxEdgeWeight(720);
            options.setTravelType(TravelType.TRANSIT);
            options.setDate(20180815);
            options.setTime(40000);
            options.setFrame(14400);
            options.setEarliestArrival(true);
            options.setMaxWalkingTimeFromSource(500);
            options.setMaxWalkingTimeToTarget(500);

            // Run configurator && get object
            String cfg = RequestConfigurator.getConfig(options);
            JSONObject actualObject = new JSONObject(cfg);

            // Load sample json & load object
            String sampleJson = IOUtils.toString(classLoader.getResourceAsStream("data/TimeVectorRequestCfgSample.json"));
            JSONObject sampleObject = new JSONObject(sampleJson);

            // Compare source and target objects
            assertThat(sampleObject.getJSONArray(Constants.SOURCES)).isEqualToComparingFieldByFieldRecursively(
                    actualObject.getJSONArray(Constants.SOURCES));
            assertThat(sampleObject.getJSONArray(Constants.TARGETS)).isEqualToComparingFieldByFieldRecursively(
                    actualObject.getJSONArray(Constants.TARGETS));

            //assert other elements
            assertThat(sampleObject.getInt(Constants.MAX_EDGE_WEIGHT)).isEqualTo(actualObject.getInt(Constants.MAX_EDGE_WEIGHT));
            assertThat(sampleObject.getString(Constants.EDGE_WEIGHT)).isEqualToIgnoringCase(actualObject.getString(Constants.EDGE_WEIGHT));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGeometryTimeVector() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        String sourceGeom = "{\"type\":\"Polygon\",\"coordinates\":[[[13.396024703979492,52.51264288568319],[13.399758338928223,52.51264288568319],[13.399758338928223,52.514784484308684],[13.396024703979492,52.514784484308684],[13.396024703979492,52.51264288568319]]]}";
        try {
            // Generate input
            TravelOptions options = new TravelOptions();
            options.addSource(new DefaultSourceCoordinate("POI:1",8.620987,47.384197));
            options.addSourceGeometry(new DefaultSourceGeometry("POI:2",sourceGeom,4326));
            options.addSourceGeometry(new DefaultSourceGeometry("POI:3",sourceGeom,4326, false));
            options.addTarget(new DefaultSourceCoordinate("Home 3",8.511658,47.322069));
            options.addTarget(new DefaultSourceCoordinate("Home 4",8.572083,47.439235));
            options.addTargetGeohash("u33d8zxfptcp");
            options.addTargetGeohash("u33d4zxf4tb4");
            options.setServiceKey("YOUR_API_KEY_HERE");
            options.setServiceUrl("http://127.0.0.1:8080/");
            options.setEdgeWeightType(EdgeWeightType.TIME);
            options.setMaxEdgeWeight(720);
            options.setTravelType(TravelType.TRANSIT);
            options.setDate(20180815);
            options.setTime(40000);
            options.setFrame(14400);
            options.setEarliestArrival(false);
            options.setMaxWalkingTimeFromSource(500);
            options.setMaxWalkingTimeToTarget(500);

            // Run configurator && get object
            String cfg = RequestConfigurator.getConfig(options);
            JSONObject actualObject = new JSONObject(cfg);

            // Load sample json & load object
            String sampleJson = IOUtils.toString(classLoader.getResourceAsStream("data/TimeVectorRequestCfgWithGeometriesSample.json"));
            JSONObject sampleObject = new JSONObject(sampleJson);

            // Compare source and target objects
            assertThat(sampleObject.getJSONArray(Constants.SOURCES)).isEqualToComparingFieldByFieldRecursively(
                    actualObject.getJSONArray(Constants.SOURCES));
            assertThat(sampleObject.getJSONArray(Constants.SOURCE_GEOMETRIES)).isEqualToComparingFieldByFieldRecursively(
                    actualObject.getJSONArray(Constants.SOURCE_GEOMETRIES));
            assertThat(sampleObject.getJSONArray(Constants.TARGETS)).isEqualToComparingFieldByFieldRecursively(
                    actualObject.getJSONArray(Constants.TARGETS));
            assertThat(sampleObject.getJSONArray(Constants.TARGET_GEOHASHES)).isEqualToComparingFieldByFieldRecursively(
                    actualObject.getJSONArray(Constants.TARGET_GEOHASHES));

            //assert other elements
            assertThat(sampleObject.getInt(Constants.MAX_EDGE_WEIGHT)).isEqualTo(actualObject.getInt(Constants.MAX_EDGE_WEIGHT));
            assertThat(sampleObject.getString(Constants.EDGE_WEIGHT)).isEqualToIgnoringCase(actualObject.getString(Constants.EDGE_WEIGHT));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultiGraphConfig() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            // Generate input
            TravelOptions options = new TravelOptions();
            options.addSource(new DefaultSourceCoordinate("POI:0",13.42883045,52.5494892));
            options.setServiceKey("INSERT_YOUR_KEY_HERE");
            options.setServiceUrl("http://127.0.0.1:8080/");
            options.setEdgeWeightType(EdgeWeightType.TIME);
            options.setMaxEdgeWeight(300);
            options.setTravelType(TravelType.BIKE);
            options.setMultiGraphEdgeClasses(CollectionUtils.safeSortedSet(11,12,16,18));
            options.setMultiGraphDomainType(MultiGraphDomainType.EDGE);
            options.setMultiGraphDomainEdgeAggregationType(MultiGraphDomainEdgeAggregationType.MIN);
            options.setMultiGraphLayerType(MultiGraphLayerType.IDENTITY);
            options.setMultiGraphLayerGeometryDetailPerTile(3);
            options.setMultiGraphLayerMinGeometryDetailLevel(2);
            options.setMultiGraphLayerMaxGeometryDetailLevel(10);
            options.setMultiGraphLayerGeometryDetailLevel(8);
            options.setMultiGraphLayerCustomGeometryMergeAggregation(MultiGraphLayerCustomGeometryMergeAggregation.SUM);
            options.setMultiGraphTileZoom(5);
            options.setMultiGraphTileX(3);
            options.setMultiGraphTileY(103);
            options.setMultiGraphSerializationFormat(MultiGraphSerializationFormat.JSON);
            options.setMultiGraphSerializationDecimalPrecision(5);
            options.setMultiGraphSerializationMaxGeometryCount(100000);
            options.setMultiGraphSerializationH3MaxBuffer(300);
            options.setMultiGraphSerializationH3BufferSpeed(5.5f);
            options.setMultiGraphSerializationH3BufferFixedValue(false);
            options.setMultiGraphSerializationH3IdFormat(MultiGraphSerializationH3IdFormat.STRING);
            options.setMultiGraphAggregationType(MultiGraphAggregationType.NONE);
            options.setMultiGraphAggregationIgnoreOutliers(true);
            options.setMultiGraphAggregationOutlierPenalty(1000.0f);
            options.setMultiGraphAggregationMinSourcesCount(3);
            options.setMultiGraphAggregationMinSourcesRatio(0.5);
            options.setMultiGraphAggregationSourceValuesLowerBound(1.0f);
            options.setMultiGraphAggregationSourceValuesUpperBound(10000.0f);
            options.setMultiGraphAggregationSourceValuesModifier(12f);
            options.setMultiGraphAggregationMinResultValue(10.0f);
            options.setMultiGraphAggregationMinResultValueRatio(0.8);
            options.setMultiGraphAggregationMaxResultValue(1000.0f);
            options.setMultiGraphAggregationMaxResultValueRatio(0.6);
            options.setMultiGraphAggregationFilterValuesForSourceOrigins(CollectionUtils.safeSortedSet("POI:0"));

            // Run configurator && get object
            String cfg = RequestConfigurator.getConfig(options);
            JSONObject actualObject = new JSONObject(cfg);

            // Load sample json & load object
            String sampleJson = IOUtils.toString(classLoader.getResourceAsStream("data/MultiGraphRequestCfgSample.json"), Charset.forName("UTF-8"));
            JSONObject sampleObject = new JSONObject(sampleJson);

            // Compare two objects
            assertThat(sampleObject.getJSONObject(Constants.MULTIGRAPH)).isEqualToComparingFieldByFieldRecursively(
                    actualObject.getJSONObject(Constants.MULTIGRAPH));


            //second test with tile discarded
            // all values have to be set to null otherwise there will be an illegal argument exception cause
            options.setMultiGraphTileZoom(null);
            options.setMultiGraphTileX(null);
            options.setMultiGraphTileY(null);
            //remove the tile from the json String
            sampleJson = sampleJson.replaceFirst("(\"tile\")([^<]*?)(\"serialization\")", "\"serialization\"" );

            sampleObject = new JSONObject(sampleJson);
            actualObject = new JSONObject(RequestConfigurator.getConfig(options));

            // Compare two objects
            assertThat(sampleObject.getJSONObject(Constants.MULTIGRAPH)).isEqualToComparingFieldByFieldRecursively(
                    actualObject.getJSONObject(Constants.MULTIGRAPH));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            options.setFrame(18000);
            options.setSrid(25833);
            options.setPolygonOrientationRule(PolygonOrientationRule.RIGHT_HAND);
	        options.setEdgeWeightType(EdgeWeightType.DISTANCE);
            options.setMaxTransfers(2);
            options.getTravelTimeFactors().put("all",0.9);
            options.getTravelTimeFactors().put("motorway",0.7);

            options.setIncludeSnapDistance(true);
            options.setUseAreaSnapping(true);
            options.setSnapRadius(200);
            options.setExcludeEdgeClassesFromSnapping(Arrays.asList(11, 12));

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
	        Assert.assertEquals(sampleObject.getString(Constants.EDGE_WEIGHT).toLowerCase(),
                    actualObject.getString(Constants.EDGE_WEIGHT).toLowerCase());

            Assert.assertEquals(sampleObject.getString(Constants.TRAVEL_TIME_FACTORS), actualObject.getString(Constants.TRAVEL_TIME_FACTORS));

            //// test with car
            options.setTravelType(TravelType.CAR);
            cfg = RequestConfigurator.getConfig(options);
            actualObject = new JSONObject(cfg);
            sampleJson = IOUtils.toString(classLoader.getResourceAsStream("data/PolygonRequestCarCfgSample.json"));
            sampleObject = new JSONObject(sampleJson);
            Assert.assertEquals(sampleObject.getString(Constants.SOURCES), actualObject.getString(Constants.SOURCES));
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
                                "            \"forceRecalculate\": true,\n" +
                                "            \"cacheResult\": false,\n" +
                                "            \"edgeWeight\": \"time\",\n" +
                                "            \"serviceUrl\": \"https://api.targomo.com/westcentraleurope/\",\n" +
                                "            \"serviceKey\": \"{{api-key}}\"\n" +
                                "        }",
                        StatisticTravelOptions.class);

        Assert.assertEquals(parsed.getTravelTimeFactors(), CollectionUtils.map("all",1.5));
        Assert.assertTrue(parsed.isForceRecalculate());
        Assert.assertFalse(parsed.isCacheResult());
    }

    @Test
    public void readGeometryWithJackson() throws IOException {
        String id = "geom";
        TravelOptions parsed = new ObjectMapper()
                .readValue( "{ \"sourceGeometries\" : [ {\n" +
                                "      \"id\": \"" + id + "\",\n" +
                                "      \"data\": \"{\\\"type\\\":\\\"Polygon\\\",\\\"coordinates\\\":[[[13.396024703979492,52.51264288568319],[13.399758338928223,52.51264288568319],[13.399758338928223,52.514784484308684],[13.396024703979492,52.514784484308684],[13.396024703979492,52.51264288568319]]]}\",\n" +
                                "      \"crs\":4326,\n" +
                                "      \"travelType\": \"walk\" } ] }",
                        TravelOptions.class);

        Assert.assertEquals(1, parsed.getSourceGeometries().size());
        DefaultSourceGeometry geom = (DefaultSourceGeometry) parsed.getSourceGeometries().get(id);
        Assert.assertEquals(id, geom.getId());
        Assert.assertEquals(4326, geom.getCrs().longValue());
        Assert.assertEquals(TravelType.WALK, geom.getTravelType());

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

            Assert.assertEquals(sampleObject.get(Constants.MAX_EDGE_WEIGHT),
                    actualObject.get(Constants.MAX_EDGE_WEIGHT));

            Assert.assertEquals(sampleObject.getString(Constants.EDGE_WEIGHT).toLowerCase(),
                    actualObject.getString(Constants.EDGE_WEIGHT).toLowerCase());

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

        Assert.assertEquals(samplePolygon.getString(Constants.POLYGON_ORIENTATION_RULE).toLowerCase(),
                actualPolygon.getString(Constants.POLYGON_ORIENTATION_RULE).toLowerCase());
	}
}
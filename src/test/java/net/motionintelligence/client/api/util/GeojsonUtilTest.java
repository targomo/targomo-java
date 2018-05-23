package net.motionintelligence.client.api.util;

import com.google.common.collect.ImmutableMap;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wololo.geojson.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * In order to run the tests you should include a file "test.properties"
 * in your "src/test/resources/" folder with the following content:
 * <pre>
 *    github.user=abc
 *    github.gistUploadToken=abc
 * </pre>
 */

public class GeojsonUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeojsonUtilTest.class);

    private static String YOUR_GITHUB_USER_ACCOUNT;
    private static String YOUR_TOKEN_WITH_GIST_UPLOAD_SCOPE;

    @BeforeClass
    public static void setup() {
        InputStream stream = LOGGER.getClass().getClassLoader().getResourceAsStream("test.properties");
        if(stream == null) {
            LOGGER.warn("No test.properties found in src/test/resources : Tests are skipped");
        } else {
            Properties prop = new Properties();
            try {
                prop.load(stream);
                YOUR_GITHUB_USER_ACCOUNT = prop.getProperty("github.user");
                YOUR_TOKEN_WITH_GIST_UPLOAD_SCOPE = prop.getProperty("github.gistUploadToken");
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.error("test.properties not properly formed : Tests are skipped", e);
            }
        }
    }

    @Test
    @Ignore("It is a visual test to executed on a local machine with desktop")
    public void testVisualisationGeojsonIO() throws Exception {
        FeatureCollection featureCollection = buildR360Berlin();
        System.out.println(YOUR_GITHUB_USER_ACCOUNT);
        GeojsonUtil.openGeoJsonInBrowserWithGeojsonIO(
                YOUR_GITHUB_USER_ACCOUNT,YOUR_TOKEN_WITH_GIST_UPLOAD_SCOPE,
                ImmutableMap.of("testGeojson", featureCollection) );
    }

    @Test
    @Ignore("It is a visual test to executed on a local machine with desktop")
    public void testVisualisationGithubGist() throws Exception {
        FeatureCollection featureCollection = buildR360Berlin();
        GeojsonUtil.openGeoJsonInBrowserWithGitHubGist(
                YOUR_GITHUB_USER_ACCOUNT,YOUR_TOKEN_WITH_GIST_UPLOAD_SCOPE,
                ImmutableMap.of("testGeojson", featureCollection) );
    }

    private static FeatureCollection buildR360Berlin(){
        List<Feature> allFeatures = new ArrayList<>();
        //Build the R
        allFeatures.add(new Feature("R1", new LineString(new double[][] {{ 13.36, 52.53 }, { 13.34, 52.53 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("R2", new LineString(new double[][] {{ 13.34, 52.53 }, { 13.34, 52.51 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("R3", new LineString(new double[][] {{ 13.36, 52.52 }, { 13.36, 52.53 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("R4", new LineString(new double[][] {{ 13.36, 52.52 }, { 13.34, 52.52 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("R5", new LineString(new double[][] {{ 13.36, 52.51 }, { 13.34, 52.52 }}), Collections.emptyMap()));
        //Build the 3
        allFeatures.add(new Feature("31", new LineString(new double[][] {{ 13.39, 52.53 }, { 13.37, 52.53 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("32", new LineString(new double[][] {{ 13.39, 52.53 }, { 13.39, 52.51 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("33", new LineString(new double[][] {{ 13.39, 52.52 }, { 13.37, 52.52 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("34", new LineString(new double[][] {{ 13.39, 52.51 }, { 13.37, 52.51 }}), Collections.emptyMap()));
        //Build the 6
        allFeatures.add(new Feature("61", new LineString(new double[][] {{ 13.42, 52.53 }, { 13.40, 52.53 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("62", new LineString(new double[][] {{ 13.40, 52.53 }, { 13.40, 52.51 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("63", new LineString(new double[][] {{ 13.42, 52.52 }, { 13.40, 52.52 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("64", new LineString(new double[][] {{ 13.42, 52.51 }, { 13.40, 52.51 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("65", new LineString(new double[][] {{ 13.42, 52.52 }, { 13.42, 52.51 }}), Collections.emptyMap()));
        //Build the 0
        allFeatures.add(new Feature("01", new LineString(new double[][] {{ 13.45, 52.53 }, { 13.43, 52.53 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("02", new LineString(new double[][] {{ 13.43, 52.53 }, { 13.43, 52.51 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("03", new LineString(new double[][] {{ 13.45, 52.51 }, { 13.45, 52.53 }}), Collections.emptyMap()));
        allFeatures.add(new Feature("04", new LineString(new double[][] {{ 13.43, 52.51 }, { 13.45, 52.51 }}), Collections.emptyMap()));
        //Build the Point
        allFeatures.add(new Feature("P1", new Point(new double[]{13.359, 52.53275}),Collections.emptyMap()));
        return new FeatureCollection(allFeatures.toArray(new Feature[allFeatures.size()]));
    }
}

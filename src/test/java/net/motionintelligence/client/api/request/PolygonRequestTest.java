package net.motionintelligence.client.api.request;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class PolygonRequestTest {
    @Test
    public void getCfg() throws Exception {

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

            PolygonRequest request = new PolygonRequest(options);
            String cfg = request.getCfg();
            System.out.print(cfg);

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

}
package com.targomo.client.api.pojo;

import com.targomo.client.Constants;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gerb on 13.06.18.
 */
public class GeometryTest {

    @Test
    public void geometryTest() throws Exception {

        String data = "{ this is a cool geometry }";
        Geometry geom = new Geometry(3857, data);
        assertTrue(3857 == geom.getCrs());
        assertTrue(Constants.GEO_JSON == geom.getType());
        assertTrue(data == geom.getData());
    }
}
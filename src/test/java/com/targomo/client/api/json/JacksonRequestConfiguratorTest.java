package com.targomo.client.api.json;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.JacksonRequestConfigurator;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class JacksonRequestConfiguratorTest {

    @Test
    public void testJacksonRequestConfigurator() throws TargomoClientException {
        TravelOptions options = new TravelOptions();

        Entity<String> entity = Entity.entity(JacksonRequestConfigurator.getConfig(options), MediaType.APPLICATION_JSON_TYPE);
        Assert.assertNotNull(entity);
    }
}

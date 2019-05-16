package com.targomo.client.api.request.ssl;

import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SslClientGeneratorTest {
    @Test
    public void initClient_success() throws Exception {
        Client client = SslClientGenerator.initClient();
        assertNotNull(client);
        assertTrue(client.getHostnameVerifier() instanceof SslClientGenerator.TrustAllHostNameVerifier);
        assertEquals("TLSv1.2", client.getSslContext().getProtocol());
    }

	@Test
	public void makeRequest1() throws Exception {

        Client client = SslClientGenerator.initClient();
        WebTarget request = client.target("https://google.com");
        request.request().get();
    }

    @Test
    public void makeRequest2() throws Exception {

        Client client = SslClientGenerator.initClient();
        WebTarget request = client.target("https://api.targomo.com/westcentraleurope/v1/polygon");
        request.request().get();
    }
}
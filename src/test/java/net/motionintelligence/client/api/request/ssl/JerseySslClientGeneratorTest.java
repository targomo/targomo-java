package net.motionintelligence.client.api.request.ssl;

import org.glassfish.jersey.client.ClientProperties;
import org.junit.Test;

import javax.ws.rs.client.Client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JerseySslClientGeneratorTest {
	@Test
	public void initClient_success() throws Exception {
		Client client = JerseySslClientGenerator.initClient();
		assertNotNull(client);
		assertTrue(client.getHostnameVerifier() instanceof JerseySslClientGenerator.TrustAllHostNameVerifier);
		assertEquals("SSL", client.getSslContext().getProtocol());
		assertEquals(1000, client.getConfiguration().getProperty(ClientProperties.CONNECT_TIMEOUT));
		assertEquals(100000, client.getConfiguration().getProperty(ClientProperties.READ_TIMEOUT));
	}

}
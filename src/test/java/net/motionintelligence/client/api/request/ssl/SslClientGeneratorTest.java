package net.motionintelligence.client.api.request.ssl;

import org.junit.Test;

import javax.ws.rs.client.Client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SslClientGeneratorTest {
	@Test
	public void initClient_success() throws Exception {
		Client client = SslClientGenerator.initClient();
		assertNotNull(client);
		assertTrue(client.getHostnameVerifier() instanceof SslClientGenerator.TrustAllHostNameVerifier);
		assertEquals("SSL", client.getSslContext().getProtocol());
	}

}
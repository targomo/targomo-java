package com.targomo.client.api.request.ssl;

import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

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

	@Test
	public void makeRequest() throws Exception {

		Client client = SslClientGenerator.initClient();
		WebTarget request = client.target("https://api.targomo.com/westcentraleurope/v1/polygon?cb=callback&key=THISISAKEY&cfg=%7B%22polygon%22%3A%7B%22pointReduction%22%3Atrue%2C%22intersectionMode%22%3A%22UNION%22%2C%22values%22%3A%5B300%5D%2C%22minPolygonHoleSize%22%3A10000000%2C%22serializer%22%3A%22geojson%22%2C%22buffer%22%3A100%2C%22srid%22%3A25833%7D%2C%22intersectionMode%22%3A%22UNION%22%2C%22sources%22%3A%5B%7B%22lng%22%3A10.51137258210965%2C%22tm%22%3A%7B%22walk%22%3A%7B%22downhill%22%3A0%2C%22recommendations%22%3A0%2C%22speed%22%3A4.5%2C%22uphill%22%3A10%7D%7D%2C%22id%22%3A%221%22%2C%22reverse%22%3Afalse%2C%22lat%22%3A4.6900761743680125E-4%7D%5D%2C%22pathSerializer%22%3A%22compact%22%2C%22elevation%22%3Atrue%2C%22reverse%22%3Afalse%2C%22edgeWeight%22%3A%22TIME%22%2C%22serviceUrl%22%3A%22https%3A%2F%2Fapi.targomo.com%2Fwestcentraleurope%2F%22%2C%22serviceKey%22%3A%22R9x9UirsvJ7Uz8bFKqJa%22%2C%22osmTypes%22%3A%5B%5D%2C%22onlyPrintReachablePoints%22%3Atrue%2C%22maxEdgeWeight%22%3A1800%7D");
		request.request().get();
	}
}
package net.motionintelligence.client.api.util;

import net.motionintelligence.client.api.StatisticTravelOptions;
import net.motionintelligence.client.api.enums.EdgeWeightType;
import net.motionintelligence.client.api.enums.TravelType;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurlUtilTest {

	@Test
	public void buildCurlRequest() throws Exception {

		final String s1 = CurlUtil.buildCurlRequest("URL", Arrays.asList("header"), "body");
		assertEquals("curl -X POST 'URL' -H 'header' -d 'body' --insecure --compressed", s1);

		final String s2 = CurlUtil.buildCurlRequest("URL", new ArrayList<>(), "body");
		assertEquals("curl -X POST 'URL' -d 'body' --insecure --compressed", s2);

		final String s3 = CurlUtil.buildCurlRequest("URL", new ArrayList<>(), "");
		assertEquals("curl 'URL'", s3);

		final String s4 = CurlUtil.buildCurlRequest("URL", Arrays.asList("header"), "");
		assertEquals("curl 'URL' -H 'header'", s4);

		final String s5 = CurlUtil.buildCurlRequest("URL", null, null);
		assertEquals("curl 'URL'", s5);
	}
}
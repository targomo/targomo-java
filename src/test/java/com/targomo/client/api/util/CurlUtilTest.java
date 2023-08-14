package com.targomo.client.api.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

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
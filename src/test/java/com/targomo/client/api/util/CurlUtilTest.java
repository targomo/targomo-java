package com.targomo.client.api.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CurlUtilTest {

	@Test
	public void buildCurlRequest() {

		final String s1 = CurlUtil.buildCurlRequest("URL", Arrays.asList("header"), "body");
		assertEquals("curl --location -X POST 'URL' \\\n" +
				"-H 'header' \\\n" +
				"--data-raw 'body' \\\n" +
				"--insecure --compressed", s1);

		final String s2 = CurlUtil.buildCurlRequest("URL", new ArrayList<>(), "body");
		assertEquals("curl --location -X POST 'URL' \\\n" +
				"--data-raw 'body' \\\n" +
				"--insecure --compressed", s2);

		final String s3 = CurlUtil.buildCurlRequest("URL", new ArrayList<>(), "");
		assertEquals("curl --location -X GET 'URL' \\\n" +
				"--insecure --compressed", s3);

		final String s4 = CurlUtil.buildCurlRequest("URL", Arrays.asList("header"), "");
		assertEquals("curl --location -X GET 'URL' \\\n" +
				"-H 'header' \\\n" +
				"--insecure --compressed", s4);

		final String s5 = CurlUtil.buildCurlRequest("URL", null, null);
		assertEquals("curl --location -X GET 'URL' \\\n" +
				"--insecure --compressed", s5);
	}
}
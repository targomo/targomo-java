package net.motionintelligence.client.api.util;

import net.motionintelligence.client.Constants;

import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class IOUtil {

	private IOUtil() {
	}

	/**
	 * Decode a URL-encoded String
	 * @param string URL-encoded string
	 * @return Decoded string
	 */
	public static String decode(final String string) {
		try {
			return URLDecoder.decode(string, StandardCharsets.UTF_8.name());
		} 
		catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 is unknown");
		}
	}

	/**
	 * Convert a String to URL-encoded version
	 * @param string input String
	 * @return URL-encoded version of string
	 */
	public static String encode(final String string) {
		try {
			return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
		} 
		catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 is unknown");
		}
	}

	/**
	 * Convert get body from HTTP response and remove callback field
	 * @param response HTTP response
	 * @return Response body without callback
	 */
	public static String getResultString(final Response response) {
		return response.readEntity(String.class).replace(Constants.CALLBACK + "(", "").replaceAll("\\)$", "");
	}
}

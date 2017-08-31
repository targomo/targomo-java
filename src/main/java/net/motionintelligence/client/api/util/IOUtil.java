package net.motionintelligence.client.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientRuntimeException;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class IOUtil {

	private IOUtil() {
	}

    /**
     * Deep cloning of the TravleOptions.
     *
     * @param travelOptions input travelOptions
     * @param clazz specified TravelOptions class
     * @param <T> type of the TravelOptions
     * @return clone of the travel options
     */
	public static <T extends TravelOptions> T cloneTravelOptions(T travelOptions, Class<T> clazz){
		try {
			ObjectMapper om = new ObjectMapper();
			return om.readValue(om.writeValueAsString(travelOptions), clazz);
		} catch (IOException e) {
			throw new Route360ClientRuntimeException("Could not duplicate travel options due to: " + e.getMessage());
		}
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

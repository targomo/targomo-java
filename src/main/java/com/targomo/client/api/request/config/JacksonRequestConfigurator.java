package com.targomo.client.api.request.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientException;
import org.apache.log4j.Logger;

/**
 * Parse TravelOptions into JSON strings that can be used when calling client methods.
 *
 * Targets are generated using StringBuilders for faster generation.
 * Polygon, sources array and other properties are created as JSONObjects, then appended as Strings.
 *
 */
public final class JacksonRequestConfigurator {

	private static final Logger LOG = Logger.getLogger(JacksonRequestConfigurator.class);

    private JacksonRequestConfigurator() {
    }

	/**
	 * Replaces getCfg methods of Request classes.
	 * Output should be encoded as URL if request method will be GET
	 * @param travelOptions Travel options to be parsed into JSON
	 * @return JSON output
	 * @throws TargomoClientException Thrown when JSON cannot be generated
	 */
	public static String getConfig(final TravelOptions travelOptions) throws TargomoClientException {

		try {
			return new ObjectMapper().writer().writeValueAsString(travelOptions);
		}
		catch (Exception ex) {

			throw new TargomoClientException("Error parsing travelOptions", ex);
		}
    }
}

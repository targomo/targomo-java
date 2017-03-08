package net.motionintelligence.client.api.request.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.request.config.builder.JSONBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	 * @throws Route360ClientException Thrown when JSON cannot be generated
	 */
	public static String getConfig(final TravelOptions travelOptions) throws Route360ClientException {

		try {
			return new ObjectMapper().writer().writeValueAsString(travelOptions);
		}
		catch (Exception ex) {

			throw new Route360ClientException("Error parsing traveltOptions", ex);
		}
    }
}

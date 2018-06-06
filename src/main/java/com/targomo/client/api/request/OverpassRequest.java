package com.targomo.client.api.request;

import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.response.OverpassResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Find reachable openstreetmap pois with this class.
 * Only accepts {@link HttpMethod} POST.
 */
public class OverpassRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(OverpassRequest.class);

	private Client client;
	private TravelOptions travelOptions;
	private static final String CALLBACK = "callback";

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}.
	 * @param travelOptions Options to be used
	 */
	public OverpassRequest(TravelOptions travelOptions) {

		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public OverpassRequest(Client client, TravelOptions travelOptions){

		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * Execute request
	 * @return point of interest response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public OverpassResponse get() throws TargomoClientException {

		long requestStart = System.currentTimeMillis();

		WebTarget target = client.target(travelOptions.getOverpassServiceUrl()).path("/api/interpreter");
        LOGGER.info(String.format("%s", target.getUri()));

		if (travelOptions.getOverpassQuery() == null || travelOptions.getOverpassQuery().isEmpty())
			throw new TargomoClientException("Empty query");

		final Entity<String> entity = Entity.entity(travelOptions.getOverpassQuery(), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug(String.format("Executing overpass query to URI: '%s'", target.getUri()));

		Response response = target.request().post(entity);

		return validateResponse(response, requestStart);
	}

	/**
	 * Validate HTTP response and return a OverpassResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @return OverpassResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private OverpassResponse validateResponse(final Response response, final long requestStart) throws TargomoClientException {

		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			return new OverpassResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart);
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), null);
		}
	}
}

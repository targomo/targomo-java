package com.targomo.client.api.request;

import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.statistic.PoiType;
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

/**
 * Find reachable openstreetmap pois with this class.
 * Only accepts {@link HttpMethod} POST.
 */
public class OverpassRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(OverpassRequest.class);

	private Client client;
	private TravelOptions travelOptions;
	private PoiType poiType;

	/**
	 * Use a custom client implementation with specified options and method and a specific PoI type
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 * @param poiType the Point of Interest type used for the request
	 */
	public OverpassRequest(TravelOptions travelOptions, Client client, PoiType poiType) {

		this.client	= client;
		this.travelOptions = travelOptions;
		this.poiType = poiType;
	}

	/**
	 * Use default client implementation with specified options and method and a specific PoI type
	 * Default client uses {@link ClientBuilder}.
	 * @param travelOptions Options to be used
	 * @param poiTye the Point of Interest type used for the request
	 */
	public OverpassRequest(TravelOptions travelOptions, PoiType poiTye) {
		this(travelOptions, ClientBuilder.newClient(), poiTye);
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * Default PoI type is the first taken from the osmTypes in travel options
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public OverpassRequest(Client client, TravelOptions travelOptions){
		this(travelOptions, client, travelOptions.getOsmTypes().iterator().next());
	}

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}.
	 * Default PoI type is the first taken from the osmTypes in travel options
	 * @param travelOptions Options to be used
	 */
	public OverpassRequest(TravelOptions travelOptions) {
		this(travelOptions, ClientBuilder.newClient(), travelOptions.getOsmTypes().iterator().next());
	}

	/**
	 * Execute request
	 * @return point of interest response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public OverpassResponse get() throws TargomoClientException {

		long requestStart = System.currentTimeMillis();

		WebTarget target = client.target(travelOptions.getOverpassServiceUrl()).path("/api/interpreter");
        LOGGER.info("{}", target.getUri());

		if (travelOptions.getOverpassQuery() == null || travelOptions.getOverpassQuery().isEmpty())
			throw new TargomoClientException("Empty query");

		final Entity<String> entity = Entity.entity(travelOptions.getOverpassQuery(), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug("Executing overpass query to URI: '{}'", target.getUri());

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
			OverpassResponse overpassResponse = new OverpassResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart, poiType);
			LOGGER.info("Request complete in {}ms", overpassResponse.getRequestEnd());
			return overpassResponse;
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}
}

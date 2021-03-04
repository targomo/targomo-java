package com.targomo.client.api.request;

import com.targomo.client.api.enums.Format;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.PointOfInterestResponse;
import com.targomo.client.api.response.PointOfInterestSummaryResponse;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import com.targomo.client.api.TravelOptions;
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
public class PointOfInterestRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointOfInterestRequest.class);

	private Client client;
	private TravelOptions travelOptions;

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}.
	 * @param travelOptions Options to be used
	 */
	public PointOfInterestRequest(TravelOptions travelOptions) {

		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public PointOfInterestRequest(Client client, TravelOptions travelOptions){

		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * Execute request
	 * @return point of interest response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public PointOfInterestResponse get() throws TargomoClientException {
		long requestStart = System.currentTimeMillis();

		Response response = getResponse("/reachability");
		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateResponse(response, requestStart, roundTripTime);
	}

	public PointOfInterestSummaryResponse getSummary() throws TargomoClientException {
		long requestStart = System.currentTimeMillis();

		Response response = getResponse("/reachability/summary");
		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateSummaryResponse(response, requestStart, roundTripTime);
	}

	private Response getResponse(String path) throws TargomoClientException {
		WebTarget target = client.target(travelOptions.getPointOfInterestServiceUrl()).path(path)
				.queryParam("key", travelOptions.getServiceKey());

		if (travelOptions.getFormat() == null) travelOptions.setFormat(Format.JSON);
		LOGGER.debug(String.format("Executing reachability request to URI: '%s'", target.getUri()));
		String config = RequestConfigurator.getConfig(travelOptions);
		final Entity<String> entity = Entity.entity(config, MediaType.APPLICATION_JSON_TYPE);

		return target.request().post(entity);
	}

	/**
	 * Validate HTTP response and return a PointOfInterestResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private PointOfInterestResponse validateResponse(final Response response, final long requestStart, final long roundTripTime)
																		throws TargomoClientException {

		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			return new PointOfInterestResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart);
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}

	private PointOfInterestSummaryResponse validateSummaryResponse(final Response response, final long requestStart, final long roundTripTime)
			throws TargomoClientException {

		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			return new PointOfInterestSummaryResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart);
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}
}

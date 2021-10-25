package com.targomo.client.api.request;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.Format;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.PointOfInterestGravitationResponse;
import com.targomo.client.api.response.PointOfInterestResponse;
import com.targomo.client.api.response.PointOfInterestSummaryResponse;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Supplier;

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

	/**
	 * Execute summary poi reachability request
	 * @return point of interest summary response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public PointOfInterestSummaryResponse getSummary() throws TargomoClientException {
		long requestStart = System.currentTimeMillis();

		Response response = getResponse("/reachability/summary");
		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateSummaryResponse(response, requestStart, roundTripTime);
	}

	/**
	 * Execute request
	 * @return point of interest response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public PointOfInterestResponse getPOIsWithinGeometry() throws TargomoClientException {
		long requestStart = System.currentTimeMillis();

		Response response = getResponse("/reachability");
		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateResponse(response, requestStart, roundTripTime);
	}

	/**
	 * Execute summary poi geometry request
	 * @return point of interest summary response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public PointOfInterestSummaryResponse getPOIsWithinGeometrySummary() throws TargomoClientException {
		long requestStart = System.currentTimeMillis();

		Response response = getResponse("/geometry/summary");
		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateSummaryResponse(response, requestStart, roundTripTime);
	}

	/**
	 * Execute gravitation poi request
	 * @return point of interest gravitation response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public PointOfInterestGravitationResponse getGravitationAnalysis() throws TargomoClientException {
		long requestStart = System.currentTimeMillis();

		Response response = getResponse("/gravitation");
		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateGravitationResponse(response, requestStart, roundTripTime);
	}

	private Response getResponse(String path) throws TargomoClientException {
		WebTarget target = client.target(travelOptions.getPointOfInterestServiceUrl()).path(path)
				.queryParam("key", travelOptions.getServiceKey());

		if (travelOptions.getFormat() == null) travelOptions.setFormat(Format.JSON);
		LOGGER.debug("Executing reachability request to URI: '{}}'", target.getUri());
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
		return validateResponse(response, () -> new PointOfInterestResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart));
	}

	/**
	 * Validate HTTP response and return a PointOfInterestSummaryResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private PointOfInterestSummaryResponse validateSummaryResponse(final Response response, final long requestStart, final long roundTripTime)
			throws TargomoClientException {

		return validateResponse(response, () -> new PointOfInterestSummaryResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart));
	}

	/**
	 * Validate HTTP response and return a PointOfInterestGravitationResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private PointOfInterestGravitationResponse validateGravitationResponse(final Response response, final long requestStart, final long roundTripTime)
			throws TargomoClientException {

		return validateResponse(response, () -> new PointOfInterestGravitationResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart));
	}

	private <T> T validateResponse(Response response, Supplier<T> responseSupplier) throws TargomoClientException {
		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			return responseSupplier.get();
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}
}

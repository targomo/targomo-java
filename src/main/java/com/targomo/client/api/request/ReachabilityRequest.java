package com.targomo.client.api.request;

import com.targomo.client.Constants;
import com.targomo.client.api.exception.TargomoAuthorizationException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoRequestException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.ReachabilityResponse;
import com.targomo.client.api.util.JsonUtil;
import com.targomo.client.api.TravelOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Calculates travel time for each source point to all targets, or -1 if unreachable.
 * Only accepts {@link HttpMethod} POST.
 */
public class ReachabilityRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReachabilityRequest.class);

	private Client client;
	private TravelOptions travelOptions;
	private static final String CALLBACK = "callback";

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}.
	 * @param travelOptions Options to be used
	 */
	public ReachabilityRequest(TravelOptions travelOptions) {

		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public ReachabilityRequest(Client client, TravelOptions travelOptions){

		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * Execute request
	 * @return Reachability response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public ReachabilityResponse get() throws TargomoClientException, TargomoAuthorizationException, TargomoRequestException {

		long requestStart = System.currentTimeMillis();

		WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/reachability")
				.queryParam("cb", CALLBACK)
				.queryParam("key", travelOptions.getServiceKey())
                .queryParam(Constants.INTER_SERVICE_KEY, travelOptions.getInterServiceKey());

		final Entity<String> entity = Entity.entity(RequestConfigurator.getConfig(travelOptions), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug(String.format("Executing reachability request to URI: '%s'", target.getUri()));

		Response response;

		try {

			// Execute POST request
			response = target.request().post(entity);
		}
		// this can happen for example if we are doing a request and restart the corresponding
		// targomo service on the same machine, in case of a fallback we need to try a different host
		// but only once
		catch ( ProcessingException exception ) {

			target = client.target(travelOptions.getFallbackServiceUrl()).path("v1/reachability")
					.queryParam("cb", CALLBACK)
					.queryParam("key", travelOptions.getServiceKey());

			LOGGER.debug(String.format("Executing reachability request to URI: '%s'", target.getUri()));

			// Execute POST request
			response = target.request().post(entity);
		}

		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateResponse(response, requestStart, roundTripTime);
	}

	/**
	 * Validate HTTP response and return a ReachabilityResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private ReachabilityResponse validateResponse(final Response response,
	                                              final long requestStart, final long roundTripTime)
			throws TargomoClientException, TargomoAuthorizationException, TargomoRequestException {
		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			String res = response.readEntity(String.class);
			return new ReachabilityResponse(travelOptions, JsonUtil.parseString(res), requestStart);
		} else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return new ReachabilityResponse(travelOptions, "gateway-time-out", roundTripTime, requestStart);
		} else if (response.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
			throw new TargomoAuthorizationException(response.readEntity(String.class) + "FORBIDDEN", null);
		} else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			throw new TargomoRequestException(response.readEntity(String.class) + "BAD_REQUEST", null);
		} else {
			throw new TargomoClientException(response.readEntity(String.class), null);
		}
	}
}

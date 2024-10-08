package com.targomo.client.api.request;

import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.ReachabilityResponse;
import com.targomo.client.api.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.function.Function;


/**
 * Calculates travel time for each source point to all targets.
 * Only accepts {@link HttpMethod} POST.
 */
public class ReachabilityRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReachabilityRequest.class);

	private final Client client;
	private final TravelOptions travelOptions;
	private static final String CALLBACK = "callback";
	private final MultivaluedMap<String, Object> headers;

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}.
	 * @param travelOptions Options to be used
	 */
	public ReachabilityRequest(TravelOptions travelOptions) {
		this.headers = new MultivaluedHashMap<>();
		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public ReachabilityRequest(Client client, TravelOptions travelOptions){
		this.headers = new MultivaluedHashMap<>();
		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 * @param headers List of custom http headers to be used
	 */
	public ReachabilityRequest(Client client, TravelOptions travelOptions, MultivaluedMap<String, Object> headers){
		this.client	= client;
		this.travelOptions = travelOptions;
		this.headers = headers;
	}

	/**
	 * Execute request
	 * @return Reachability response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public ReachabilityResponse get() throws TargomoClientException, ResponseErrorException {
		return get(Function.identity());
	}

	/**
	 * Execute request
	 * If cached targets are used that are shared among multiple statistics it may be necessary to filter the targets and map their ids.
	 * To improve performance this can be done while parsing the response by passing a mapper/filter function.
	 * @param targetIdMapperFilter a function that maps the target id to a different value or filters targets by returning null.
	 * @return Reachability response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public ReachabilityResponse get(Function<String, String> targetIdMapperFilter) throws TargomoClientException, ResponseErrorException {

		long requestStart = System.currentTimeMillis();

		WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/reachability")
				.queryParam("cb", CALLBACK)
				.queryParam("key", travelOptions.getServiceKey())
				.queryParam("forceRecalculate", travelOptions.isForceRecalculate())
				.queryParam("cacheResult", travelOptions.isCacheResult())
				.queryParam(Constants.INTER_SERVICE_KEY, travelOptions.getInterServiceKey())
				.queryParam(Constants.INTER_SERVICE_REQUEST, travelOptions.getInterServiceRequestType());

		final Entity<String> entity = Entity.entity(RequestConfigurator.getConfig(travelOptions), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug("Executing reachability request to URI: '{}}'", target.getUri());

		// Execute POST request
		Response response = target.request().headers(headers).post(entity);

		return validateResponse(response, requestStart, targetIdMapperFilter);
	}

	/**
	 * Validate HTTP response and return a ReachabilityResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private ReachabilityResponse validateResponse(final Response response, final long requestStart, Function<String, String> targetIdMapperFilter)
			throws TargomoClientException, ResponseErrorException {
		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			String res = response.readEntity(String.class);
			return new ReachabilityResponse(travelOptions, JsonUtil.parseString(res), requestStart, targetIdMapperFilter);
		} else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}
}

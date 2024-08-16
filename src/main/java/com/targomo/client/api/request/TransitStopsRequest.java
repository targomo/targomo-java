package com.targomo.client.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.TransitStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Get all the transit stations reachable from source(s) and the times of the next stops
 * Only accepts {@link HttpMethod} POST.
 */
public class TransitStopsRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransitStopsRequest.class);

	private final Client client;
	private final TravelOptions travelOptions;
	private static final String CALLBACK = "callback";
	private final MultivaluedMap<String, Object> headers;

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}.
	 * @param travelOptions Options to be used
	 */
	public TransitStopsRequest(TravelOptions travelOptions) {
		this.headers = new MultivaluedHashMap<>();
		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public TransitStopsRequest(Client client, TravelOptions travelOptions){
		this.headers = new MultivaluedHashMap<>();
		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public TransitStopsRequest(Client client, TravelOptions travelOptions, MultivaluedMap<String,Object> headers){
		this.client	= client;
		this.travelOptions = travelOptions;
		this.headers = headers;
	}

	public Map<String, List<TransitStation>> get() throws TargomoClientException, JsonProcessingException {
		return get(null);
	}

	/**
	 * Execute request
	 * @return Map keys - source id
	 *             values - list of reachable transit stations
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public Map<String, List<TransitStation>> get(Boolean filterEmptyNextStops) throws TargomoClientException, JsonProcessingException {

		WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/transit/stops")
				.queryParam("cb", CALLBACK)
				.queryParam("key", travelOptions.getServiceKey())
        		.queryParam(Constants.INTER_SERVICE_KEY, travelOptions.getInterServiceKey())
				.queryParam(Constants.INTER_SERVICE_REQUEST, travelOptions.getInterServiceRequestType());
		if(filterEmptyNextStops != null){
			target = target.queryParam("filterEmptyNextStops", filterEmptyNextStops);
		}

		final Entity<String> entity = Entity.entity(RequestConfigurator.getConfig(travelOptions), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug("Executing transit stops request to URI: '{}}'", target.getUri());

		Response response;

		// Execute POST request
		response = target.request().headers(headers).post(entity);

		return validateResponse(response);
	}

	/**
	 * Validate HTTP response and return a List<TransitStopResponse>
	 * @param response HTTP response
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private Map<String, List<TransitStation>> validateResponse(final Response response)
			throws TargomoClientException, JsonProcessingException {
		// compare the HTTP status codes
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			TypeReference<Map<String, List<TransitStation>>> typeRef = new TypeReference<Map<String, List<TransitStation>>>() {};
			return new ObjectMapper().readValue(response.readEntity(String.class), typeRef);
		} else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}
}

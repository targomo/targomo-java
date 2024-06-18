package com.targomo.client.api.request;

import com.targomo.client.Constants;
import com.targomo.client.api.StatisticTravelOptions;
import com.targomo.client.api.enums.EdgeWeightType;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.request.config.JacksonRequestConfigurator;
import com.targomo.client.api.request.enums.StatisticMethod;
import com.targomo.client.api.response.StatisticsGeometryValuesResponse;
import com.targomo.client.api.response.StatisticsResponse;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.function.Supplier;

public class StatisticsRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsRequest.class);
	private static final String VALUES_GEOMETRY = "values/geometry";
	private final Client client;
	private final StatisticTravelOptions travelOptions;
	private final MultivaluedMap<String, Object> headers;

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}
	 * @param travelOptions Options to be used
	 */
	public StatisticsRequest(StatisticTravelOptions travelOptions) {
		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
		this.headers = new MultivaluedHashMap<>();
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public StatisticsRequest(Client client, StatisticTravelOptions travelOptions){
		this.client	= client;
		this.travelOptions = travelOptions;
		this.headers = new MultivaluedHashMap<>();
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 * @param headers List of custom http headers to be used
	 */
	public StatisticsRequest(Client client, StatisticTravelOptions travelOptions, MultivaluedMap<String, Object> headers){
		this.client	= client;
		this.travelOptions = travelOptions;
		this.headers = headers;
	}

	public StatisticsResponse get(StatisticMethod method) throws TargomoClientException {
		return get(method.getPath(), this::validateResponse);
	}


	public StatisticsGeometryValuesResponse getValuesGeometry() throws TargomoClientException {
		return get(VALUES_GEOMETRY, this::validateGeometryValuesResponse);
	}

	/**
	 * @param path the path for the request to be executed
	 * @return Response from the statistics server
	 * @throws TargomoClientException In case of other errors
	 */
	public <T> T get(String path, ResponseValidator<T> responseValidator) throws TargomoClientException {

		long requestStart = System.currentTimeMillis();

		WebTarget target = client.target(this.travelOptions.getStatisticServiceUrl()).path(path)
				.queryParam("key", travelOptions.getServiceKey())
				.queryParam("serviceUrl", travelOptions.getServiceUrl());
		if(travelOptions.getInterServiceKey() != null){
			target = target.queryParam(Constants.INTER_SERVICE_KEY, travelOptions.getInterServiceKey());
		}
		if(travelOptions.getInterServiceRequestType() != null){
			target = target.queryParam(Constants.INTER_SERVICE_REQUEST, travelOptions.getInterServiceRequestType());
		}
		if(VALUES_GEOMETRY.equals(path) && travelOptions.getStatisticIds() != null && travelOptions.getValuesGeometryAggregation() != null){
			for(Short statisticId : travelOptions.getStatisticIds()){
				target = target.queryParam("aggregations", statisticId+"-"+travelOptions.getValuesGeometryAggregation());
			}
		}

		final Entity<String> entity = Entity.entity(JacksonRequestConfigurator.getConfig(travelOptions), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug("Executing statistics request ({}) to URI: '{}'", path, target.getUri());

		Response response;

		// Execute POST request
		response = target.request().headers(headers).post(entity);

		long roundTripTime = System.currentTimeMillis() - requestStart;

		return responseValidator.validateResponse(response, requestStart, roundTripTime);
	}

	private <T> T validateResponse(final Response response, Supplier<T> responseSupplier, Supplier<T> gatewayTimeOutResponseSupplier)
			throws TargomoClientException {

		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {

			// consume the results
			return responseSupplier.get();
		}
		else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return gatewayTimeOutResponseSupplier.get();
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}

	/**
	 * Validate HTTP response and return a StatisticsResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private StatisticsResponse validateResponse(final Response response, final long requestStart, final long roundTripTime)
			throws TargomoClientException {

		return validateResponse(response,
				() -> new StatisticsResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart),
				() -> new StatisticsResponse(travelOptions, "gateway-time-out", roundTripTime, requestStart));
	}

	/**
	 * Validate HTTP response and return a StatisticsGeometryValueResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private StatisticsGeometryValuesResponse validateGeometryValuesResponse(final Response response, final long requestStart, final long roundTripTime)
			throws TargomoClientException {

		return validateResponse(response,
				() -> new StatisticsGeometryValuesResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart),
				() -> new StatisticsGeometryValuesResponse(travelOptions, "gateway-time-out", roundTripTime, requestStart));
	}

	@FunctionalInterface
	public interface ResponseValidator<T> {
		T validateResponse(final Response response, final long requestStart, final long roundTripTime) throws TargomoClientException;
	}

}

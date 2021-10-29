package com.targomo.client.api.request;

import com.targomo.client.api.StatisticTravelOptions;
import com.targomo.client.api.TravelOptions;
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
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.function.Supplier;

public class StatisticsRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsRequest.class);
	private Client client;
	private TravelOptions travelOptions;

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}
	 * @param travelOptions Options to be used
	 */
	public StatisticsRequest(TravelOptions travelOptions) {

		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public StatisticsRequest(Client client, TravelOptions travelOptions){

		this.client	= client;
		this.travelOptions = travelOptions;
	}

	public StatisticsResponse get(StatisticMethod method) throws TargomoClientException {
		return get(method.getPath(), this::validateResponse);
	}


	public StatisticsGeometryValuesResponse getValuesGeometry() throws TargomoClientException {
		return get("values/geometry", this::validateGeometryValuesResponse);
	}

	/**
	 * @param path the path for the request to be executed
	 * @return Response from the statistics server
	 * @throws JSONException In case the returned response is not parsable
	 * @throws TargomoClientException In case of other errors
	 */
	public <T> T get(String path, ResponseValidator<T> responseValidator) throws TargomoClientException {

		long requestStart = System.currentTimeMillis();

		WebTarget target = client.target(this.travelOptions.getStatisticServiceUrl()).path(path)
				.queryParam("key", travelOptions.getServiceKey())
				.queryParam("serviceUrl", travelOptions.getServiceUrl());

		final Entity<String> entity = Entity.entity(JacksonRequestConfigurator.getConfig(travelOptions), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug("Executing statistics request ({}) to URI: '{}'", path, target.getUri());

		Response response;

		try {

			// Execute POST request
			response = target.request().post(entity);
		}
		// this can happen for example if we are doing a request and restart the corresponding
		// targomo service on the same machine, in case of a fallback we need to try a different host
		// but only once
		catch ( ProcessingException exception ) {

			LOGGER.error("Error executing statistics request ({}) to URI: '{}'", path, target.getUri(), exception);

			target = client.target(travelOptions.getFallbackServiceUrl()).path(path)
					.queryParam("key", travelOptions.getServiceKey());

			LOGGER.debug("Executing statistics request ({}) to URI: '{}'", path, target.getUri());

			// Execute POST request
			response = target.request().post(entity);
		}

		long roundTripTime = System.currentTimeMillis() - requestStart;

		return responseValidator.validateResponse(response, requestStart, roundTripTime);
	}

	public static void main(String[] args) throws TargomoClientException {

		StatisticTravelOptions options = new StatisticTravelOptions();
		options.setMaxEdgeWeight(1800);
		options.setEdgeWeightType(EdgeWeightType.TIME);
		options.setTravelType(TravelType.WALK);
		options.addSource(new DefaultSourceCoordinate("1asda", 13.405, 52.52));
		options.setServiceUrl("http://localhost:8081/");
		options.setFallbackServiceUrl("http://localhost:8081/");
		options.setStatisticServiceUrl("http://localhost:8080/");
		options.setServiceKey("uhWrWpUhyZQy8rPfiC7X");
		options.setDate(20150812);
		options.setTime(43200);
		options.setAppendTravelTimes(true);
		options.setStatisticIds(Arrays.asList((short) 0, (short) 1));
		options.setStatisticGroupId(1);

		StatisticsResponse response   = new StatisticsRequest(options).get(StatisticMethod.CHARTS_DEPENDENT);
		LOGGER.info("{}", response.getStatisticResult());
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

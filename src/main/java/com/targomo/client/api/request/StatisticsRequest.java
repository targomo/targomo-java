package com.targomo.client.api.request;

import com.targomo.client.api.StatisticTravelOptions;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.response.StatisticsResponse;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.request.config.JacksonRequestConfigurator;
import com.targomo.client.api.request.enums.StatisticMethod;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.Map;

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

	/**
	 * @param method the method for the request to be executed
	 * @return Response from the statistics server
	 * @throws JSONException In case the returned response is not parsable
	 * @throws TargomoClientException In case of other errors
	 */
	public StatisticsResponse get(StatisticMethod method) throws TargomoClientException, JSONException {

		long requestStart = System.currentTimeMillis();

		WebTarget target = client.target(this.travelOptions.getStatisticServiceUrl()).path(method.getPath())
				.queryParam("key", travelOptions.getServiceKey())
				.queryParam("serviceUrl", travelOptions.getServiceUrl());

		final Entity<String> entity = Entity.entity(JacksonRequestConfigurator.getConfig(travelOptions), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug(String.format("Executing statistics request (%s) to URI: '%s'", method.getPath(), target.getUri()));

		Response response;

		try {

			// Execute POST request
			response = target.request().post(entity);
		}
		// this can happen for example if we are doing a request and restart the corresponding
		// targomo service on the same machine, in case of a fallback we need to try a different host
		// but only once
		catch ( ProcessingException exception ) {

			LOGGER.error(String.format("Executing statistics request (%s) to URI: '%s'", method.getPath(), target.getUri()), exception);

			target = client.target(travelOptions.getFallbackServiceUrl()).path(method.getPath())
					.queryParam("key", travelOptions.getServiceKey());

			LOGGER.debug(String.format("Executing statistics request (%s) to URI: '%s'", method.getPath(), target.getUri()));

			// Execute POST request
			response = target.request().post(entity);
		}

		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateResponse(response, requestStart, roundTripTime);
	}

	public static void main(String[] args) throws TargomoClientException, JSONException {

		StatisticTravelOptions options = new StatisticTravelOptions();
		options.setMaxRoutingTime(1800);
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
		System.out.println(response.getStatisticResult());
	}

	/**
	 * Validate HTTP response and return a ReachabilityResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private StatisticsResponse validateResponse(final Response response, final long requestStart, final long roundTripTime)
			throws TargomoClientException {

		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {

			// consume the results
			return new StatisticsResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart);
		}
		else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return new StatisticsResponse(travelOptions, "gateway-time-out", roundTripTime, requestStart);
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}

	/**
	 * @param sources ID -> Coordinate map of sources to be parsed
	 * @return Sources parsed into JSON
	 * @throws JSONException In case something cannot be parsed
	 */
	private static String parseSources(Map<String,Coordinate> sources) throws JSONException {

		JSONArray sourcesJson = new JSONArray();
		for ( Coordinate src : sources.values() ) {
			sourcesJson.put(new JSONObject()
				.put(Constants.ID, src.getId())
				.put(Constants.Y, src.getY())
				.put(Constants.X, src.getX())
			);
		}

		return sourcesJson.toString();
	}
}

package com.targomo.client.api.request;

import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.request.ssl.SslClientGenerator;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.response.PolygonResponse;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import org.json.JSONObject;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Creates polygons for the source points with specified travel times in minutes.
 * In case of GeoJson output, Polygons will be buffered, simplified and transformed
 * according to the respective values in {@link TravelOptions}.
 * Buffer should be given in meters or in degrees, depending on the output CRS's unit.
 */
public class PolygonRequest {

	private Client client;
	private TravelOptions travelOptions;
	private String method;

	/**
	 * Use a custom client implementation
	 * @param client Client to be used
	 */
	public PolygonRequest(Client client) {
		this.client	= client;
	}

	/**
	 * Use default Client. See {@link SslClientGenerator}
	 */
	public PolygonRequest() {
		this(SslClientGenerator.initClient());
	}

	/**
	 * Use default Client with specified travelOptions
	 * @param travelOptions Travel options parameters
	 */
	public PolygonRequest(TravelOptions travelOptions) {
		this();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use default Client with specified travelOptions and HTTP method
	 * @param travelOptions Travel options parameters
	 * @param method HTTP Method (GET or POST)
	 */
	public PolygonRequest(TravelOptions travelOptions, String method) {
		this();
		this.travelOptions = travelOptions;
		this.method 	   = method;
	}

	/**
	 * Use custom client with specified travelOptions
	 * @param client Client to be used
	 * @param travelOptions Travel options parameters
	 */
	public PolygonRequest(Client client, TravelOptions travelOptions) {
		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 *
	 * @param options Travel options parameters
	 */
	public void setTravelOptions(TravelOptions options) {
		this.travelOptions = options;
	}

	/**
	 * Execute request
	 * @return Polygon response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public PolygonResponse get() throws TargomoClientException {

		long startTimeMillis = System.currentTimeMillis();

		WebTarget request = client.target(travelOptions.getServiceUrl())
				.path("v1/polygon")
				.queryParam("cb", Constants.CALLBACK)
				.queryParam("key", travelOptions.getServiceKey());

		// Execute request
		Response response;
		String config = RequestConfigurator.getConfig(travelOptions);
		if (HttpMethod.GET.equals(method)) {
			request  = request.queryParam("cfg", IOUtil.encode(config));
			response = request.request().get();
		}
		else if (HttpMethod.POST.equals(method)) {
			response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
		} else {
			throw new TargomoClientException("HTTP Method not supported: " + this.method, null);
		}

		// Execution time
		long roundTripTimeMillis = (System.currentTimeMillis() - startTimeMillis);

		// Validate & return
		return validateResponse(response, roundTripTimeMillis);
	}

	/**
	 * Validate HTTP response and return a PolygonResponse
	 * @param response HTTP response
	 * @param roundTripTimeMillis Execution time in milliseconds
	 * @return PolygonResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private PolygonResponse validateResponse(final Response response, final long roundTripTimeMillis)
			throws TargomoClientException {

		// Check HTTP status
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			String resultString = IOUtil.getResultString(response);

			long startParsing = System.currentTimeMillis();
			JSONObject result = JsonUtil.parseString(resultString);
			long parseTime = System.currentTimeMillis() - startParsing;

			// Check response code
			final String responseCode = JsonUtil.getString(result, "code");
			if (Constants.EXCEPTION_ERROR_CODE_NO_ROUTE_FOUND.equals(responseCode)
					|| Constants.EXCEPTION_ERROR_CODE_COULD_NOT_CONNECT_POINT_TO_NETWORK.equals(responseCode)
					|| Constants.EXCEPTION_ERROR_CODE_TRAVEL_TIME_EXCEEDED.equals(responseCode)
					|| Constants.EXCEPTION_ERROR_CODE_UNKNOWN_EXCEPTION.equals(responseCode)) {
				throw new TargomoClientException(result.toString(), null);
			}

			return new PolygonResponse(travelOptions, result,
					JsonUtil.getString(result, "code"),
					JsonUtil.getLong(result, "requestTime"), roundTripTimeMillis, parseTime);
		} else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return new PolygonResponse(travelOptions, new JSONObject(), "gateway-time-out", roundTripTimeMillis, -1);
		} else {
			throw new TargomoClientException("Status: " + response.getStatus() + ": " + response.readEntity(String.class), null);
		}
	}

	/**
	 * Specify HTTP method to be used
	 * @param method HTTP method (GET or POST)
	 */
	public void setMethod(final String method) {
		this.method = method;
	}
}

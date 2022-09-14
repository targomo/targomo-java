package com.targomo.client.api.request;

import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.request.ssl.SslClientGenerator;
import com.targomo.client.api.response.PolygonResponse;
import com.targomo.client.api.response.ResponseCode;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
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
	private MultivaluedMap<String, Object> headers;

	/**
	 * Use a custom client implementation
	 * @param client Client to be used
	 */
	public PolygonRequest(Client client) {
		this.client	= client;
		this.headers = new MultivaluedHashMap<>();
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
		this.headers = new MultivaluedHashMap<>();
	}

	/**
	 * Use custom client with specified travelOptions and the specified HTTP VERB
	 * @param client Client to be used
	 * @param travelOptions Travel options parameters
	 * @param method the HTTP VERB
	 */
	public PolygonRequest(Client client, TravelOptions travelOptions, String method) {
		this.client	= client;
		this.travelOptions = travelOptions;
		this.method = method;
		this.headers = new MultivaluedHashMap<>();
	}

	/**
	 * Use custom client with specified travelOptions and the specified HTTP VERB
	 * @param client Client to be used
	 * @param travelOptions Travel options parameters
	 * @param method the HTTP VERB
	 * @param headers List of custom http headers to be used
	 */
	public PolygonRequest(Client client, TravelOptions travelOptions, String method, MultivaluedMap<String, Object> headers) {
		this.client	= client;
		this.travelOptions = travelOptions;
		this.method = method;
		this.headers = headers;
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
	public PolygonResponse get() throws TargomoClientException, ResponseErrorException {

		long startTimeMillis = System.currentTimeMillis();

		WebTarget request = client.target(travelOptions.getServiceUrl())
				.path("v1/polygon")
				.queryParam("cb", Constants.CALLBACK)
				.queryParam("key", travelOptions.getServiceKey())
				.queryParam("interServiceKey", travelOptions.getInterServiceKey())
				.queryParam("interServiceRequest", travelOptions.getInterServiceRequestType());

		// Execute request
		Response response;
		String config = RequestConfigurator.getConfig(travelOptions);
		if (HttpMethod.GET.equals(method)) {
			request  = request.queryParam("cfg", IOUtil.encode(config));
			response = request.request().headers(headers).get();
		}
		else if (HttpMethod.POST.equals(method)) {
			response = request.request().headers(headers).post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
		} else {
			throw new TargomoClientException("HTTP Method not supported: " + this.method);
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
			throws TargomoClientException, ResponseErrorException {

		// Check HTTP status
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			String resultString = IOUtil.getResultString(response);

			long startParsing = System.currentTimeMillis();
			JSONObject result = JsonUtil.parseString(resultString);
			long parseTime = System.currentTimeMillis() - startParsing;

			// Check response code
			final ResponseCode responseCode = ResponseCode.fromString(JsonUtil.getString(result, "code"));
			final String message = result.has("message") ? JsonUtil.getString(result, "message") : "";

			if (responseCode != ResponseCode.OK) {
				String msg = "Polygon request returned an error";
				if (!StringUtils.isEmpty(message)) {
					msg += ": " + message;
				}
				throw new TargomoClientException(String.format("Status: %s: %s", response.getStatus(), message), response.getStatus());
			}
			return new PolygonResponse(travelOptions, result, responseCode,
					JsonUtil.getLong(result, "requestTime"), roundTripTimeMillis, parseTime);
		} else {
			throw new TargomoClientException(String.format("Status: %s: %s", response.getStatus(), response.readEntity(String.class)), response.getStatus());
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

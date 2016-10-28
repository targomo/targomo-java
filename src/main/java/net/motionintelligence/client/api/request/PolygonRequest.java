package net.motionintelligence.client.api.request;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.request.ssl.JerseySslClientGenerator;
import net.motionintelligence.client.api.response.PolygonResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
		this.client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        this.client.property(ClientProperties.READ_TIMEOUT, 100000);
	}

	/**
	 * Use default Client
	 */
	public PolygonRequest() {
		this(JerseySslClientGenerator.initClient());
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
	 * Use default Client with specified travelOptions and method
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
		this.client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        this.client.property(ClientProperties.READ_TIMEOUT, 100000);
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
	 * @throws Route360ClientException In case of error other than Gateway Timeout
	 */
	public PolygonResponse get() throws Route360ClientException {

		long startTimeMillis = System.currentTimeMillis();

		WebTarget request = client.target(travelOptions.getServiceUrl())
				.path("v1/polygon" + (HttpMethod.POST.equals(method) ? "_post" : ""))
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
			throw new Route360ClientException("HTTP Method not supported: " + this.method, null);
		}

		// Execution time
		long roundTripTimeMillis = (System.currentTimeMillis() - startTimeMillis);

		// Validate & return
		return validateResponse(response, roundTripTimeMillis);
	}

	/**
	 * Validate HTTP response & return a PolygonResponse
	 * @param response HTTP response
	 * @param roundTripTimeMillis Execution time in milliseconds
	 * @return PolygonResponse
	 * @throws Route360ClientException In case of errors other than GatewayTimeout
	 */
	private PolygonResponse validateResponse(final Response response, final long roundTripTimeMillis)
			throws Route360ClientException {

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
				throw new Route360ClientException(result.toString(), null);
			}

			return new PolygonResponse(travelOptions, result,
					JsonUtil.getString(result, "code"),
					JsonUtil.getLong(result, "requestTime"), roundTripTimeMillis, parseTime);
		} else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return new PolygonResponse(travelOptions, new JSONObject(), "gateway-time-out", roundTripTimeMillis, -1);
		} else {
			throw new Route360ClientException("Status: " + response.getStatus() + ": " + response.readEntity(String.class), null);
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

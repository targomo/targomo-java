package com.targomo.client.api.request;

import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.RouteResponse;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import com.targomo.client.api.TravelOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Generates possible route from sources to targets.
 */
public class RouteRequest {

	private Client client;
	private TravelOptions travelOptions;
	private static final String CALLBACK = "callback";

	/**
	 * Use default Client with specified travelOptions
	 * Default client uses {@link ClientBuilder}
	 * @param travelOptions Travel options parameters
	 */
	public RouteRequest(TravelOptions travelOptions) {
		this.client	= ClientBuilder.newClient();

		this.travelOptions = travelOptions;
	}
	/**
	 * Use custom client with specified travelOptions
	 * @param client Client to be used
	 * @param travelOptions Travel options parameters
	 */
	public RouteRequest(Client client, TravelOptions travelOptions) {
		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * Execute request
	 * @return Route response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public RouteResponse get() throws TargomoClientException {
		long requestStart = System.currentTimeMillis();

		WebTarget request = client.target(travelOptions.getServiceUrl()).path("v1/route")
			.queryParam("cb", CALLBACK)
			.queryParam("key", travelOptions.getServiceKey())
			.queryParam("cfg", IOUtil.encode(RequestConfigurator.getConfig(travelOptions)));

		// make the request
		Response response = request.request().get();
		return validateResponse(requestStart, response);
	}

    /**
     * For debugging.
     *
     * @return the request as curl String
     * @throws TargomoClientException when error occurred during parsing of the travel options
     */
	public String toCurl() throws TargomoClientException {
	    String url = travelOptions.getServiceUrl().endsWith("/") ? travelOptions.getServiceUrl() : travelOptions.getServiceUrl() + "/";
	    return "curl -X GET '" +
                url + "v1/route" +
                "?cb=" + CALLBACK +
                "&key=" + travelOptions.getServiceKey() +
                "&cfg=" + IOUtil.encode(RequestConfigurator.getConfig(travelOptions)) + "'";
    }

	/**
	 * Validate HTTP response and return a PolygonResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @return RouteResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private RouteResponse validateResponse(final long requestStart, final Response response) throws TargomoClientException {
		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			JSONObject result = JsonUtil.parseString(IOUtil.getResultString(response));

			return new RouteResponse(travelOptions, JsonUtil.getJsonArray(JsonUtil.getJSONObject(result, "data"), "routes"), JsonUtil.getString(result, "code"),
					result.has("requestTime") ? JsonUtil.getInt(result, "requestTime") : -1);
		} else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return new RouteResponse(travelOptions, new JSONArray(), "gateway-time-out", System.currentTimeMillis() - requestStart);
		} else {
			throw new TargomoClientException(response.readEntity(String.class), null);
		}
	}
}
package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.response.RouteResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;
import org.glassfish.jersey.message.GZipEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class RouteRequest {
	
	private Client client;
	private TravelOptions travelOptions;
	private static final String CALLBACK = "callback";

	/**
	 * Use default Client with specified travelOptions
	 * @param travelOptions Travel options parameters
	 */
	public RouteRequest(TravelOptions travelOptions){
		this.client	= ClientBuilder.newClient();
		client.register(GZipEncoder.class);
		
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
	 * @throws Route360ClientException In case of error other than Gateway Timeout
	 */
	public RouteResponse get() throws Route360ClientException {
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
	 * Validate HTTP response & return a PolygonResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @return RouteResponse
	 * @throws Route360ClientException In case of errors other than GatewayTimeout
	 */
	private RouteResponse validateResponse(final long requestStart, final Response response) throws Route360ClientException {
		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			JSONObject result = JsonUtil.parseString(IOUtil.getResultString(response));

			return new RouteResponse(travelOptions, JsonUtil.getJsonArray(JsonUtil.getJSONObject(result, "data"), "routes"), JsonUtil.getString(result, "code"),
					result.has("requestTime") ? JsonUtil.getInt(result, "requestTime") : -1);
		} else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return new RouteResponse(travelOptions, new JSONArray(), "gateway-time-out", System.currentTimeMillis() - requestStart);
		} else {
			throw new Route360ClientException(response.readEntity(String.class), null);
		}
	}
}

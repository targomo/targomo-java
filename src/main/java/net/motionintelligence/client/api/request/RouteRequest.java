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
	private String callback = "callback";
	
	/**
	 * 
	 * @param travelOptions
	 */
	public RouteRequest(TravelOptions travelOptions){
		
		this.client	= ClientBuilder.newClient();
		client.register(GZipEncoder.class);
		
		this.travelOptions = travelOptions;
	}
	
	public RouteRequest(Client client, TravelOptions travelOptions) {
		
		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * 
	 * @return
	 * @throws Route360ClientException 
	 */
	public RouteResponse get() throws Route360ClientException{
		
		long requestStart = System.currentTimeMillis();
		
		WebTarget request = client.target(travelOptions.getServiceUrl()).path("v1/route")
			.queryParam("cb", callback)
			.queryParam("key", travelOptions.getServiceKey())
			.queryParam("cfg", IOUtil.encode(RequestConfigurator.getConfig(travelOptions)));
		
		// make the request
		Response response = request.request().get();
		
		// compare the HTTP status codes, NOT the route 360 code
		if ( response.getStatus() == Response.Status.OK.getStatusCode() ) {
			
			// consume the results
			JSONObject result = JsonUtil.parseString(response.readEntity(String.class).replace(callback + "(", "").replaceAll("\\)$", ""));
			
			return new RouteResponse(this.travelOptions, JsonUtil.getJsonArray(JsonUtil.getJSONObject(result, "data"), "routes"), JsonUtil.getString(result, "code"), 
					result.has("requestTime") ? JsonUtil.getInt(result, "requestTime") : -1);
		}
		else if ( response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode() ) 
			return new RouteResponse(this.travelOptions, new JSONArray(), "gateway-time-out", System.currentTimeMillis() - requestStart);
		else 
			throw new Route360ClientException(response.readEntity(String.class), null);
	}
}

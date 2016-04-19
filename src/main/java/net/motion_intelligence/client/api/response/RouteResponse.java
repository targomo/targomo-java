package net.motion_intelligence.client.api.response;

import org.json.JSONArray;

import net.motion_intelligence.client.api.TravelOptions;

public class RouteResponse {

	private final String code;
	private final long requestTimeMillis;
	private final TravelOptions travelOptions;
	private final JSONArray routes;

	/**
	 * @param code - route360 status code for the request
	 * @param requestTimeMillis - the milliseconds for server runtime
	 */
	public RouteResponse(TravelOptions travelOptions, JSONArray routes, String code, long requestTimeMillis) {
		this.travelOptions 	   = travelOptions;
		this.code 			   = code;
		this.routes 		   = routes;
		this.requestTimeMillis = requestTimeMillis;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the requestTimeMillis
	 */
	public long getRequestTimeMillis() {
		return requestTimeMillis;
	}

	/**
	 * @return the travelOptions
	 */
	public TravelOptions getTravelOptions() {
		return travelOptions;
	}

	/**
	 * @return the routes
	 */
	public JSONArray getRoutes() {
		return routes;
	}
}

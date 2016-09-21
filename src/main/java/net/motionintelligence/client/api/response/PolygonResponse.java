package net.motionintelligence.client.api.response;

import org.json.JSONObject;

import net.motionintelligence.client.api.TravelOptions;

public class PolygonResponse {

	private final String code;
	private final long requestTimeMillis;
	private final long roundTripTimeMillis;
	private final TravelOptions travelOptions;
	private final JSONObject result;
	private final long parseTime;

	/**
	 * @param code - route360 status code for the request
	 * @param requestTimeMillis - the milliseconds for server runtime
	 */
	public PolygonResponse(TravelOptions travelOptions, JSONObject result, String code, long requestTimeMillis, long roundTripTimeMillis) {
		this.travelOptions 	   	= travelOptions;
		this.code 			   	= code;
		this.result				= result;
		this.requestTimeMillis 	= requestTimeMillis;
		this.roundTripTimeMillis = roundTripTimeMillis;
		this.parseTime 			= -1;
	}
	
	public PolygonResponse(TravelOptions travelOptions, JSONObject result, String code, long requestTimeMillis, long roundTripTimeMillis, long parseTime) {
		this.travelOptions 	   	= travelOptions;
		this.code 			   	= code;
		this.result				= result;
		this.requestTimeMillis 	= requestTimeMillis;
		this.roundTripTimeMillis = roundTripTimeMillis;
		this.parseTime 			= parseTime;
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
	 * @return the roundTripTimeMillis
	 */
	public long getRoundTripTimeMillis() {
		return roundTripTimeMillis;
	}

	public JSONObject getResult() {
		return result;
	}

	public long getParseTime() {
		return parseTime;
	}
}

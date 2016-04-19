package net.motion_intelligence.client.api.response;

import net.motion_intelligence.client.api.TravelOptions;

public class PolygonResponse {

	private final String code;
	private final long requestTimeMillis;
	private final long roundTripTimeMillis;
	private final TravelOptions travelOptions;

	/**
	 * @param code - route360 status code for the request
	 * @param requestTimeMillis - the milliseconds for server runtime
	 */
	public PolygonResponse(TravelOptions travelOptions, String code, long requestTimeMillis, long roundTripTimeMillis) {
		this.travelOptions 	   = travelOptions;
		this.code 			   = code;
		this.requestTimeMillis = requestTimeMillis;
		this.roundTripTimeMillis = roundTripTimeMillis;
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
}

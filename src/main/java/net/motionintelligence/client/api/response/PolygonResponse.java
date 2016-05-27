package net.motionintelligence.client.api.response;

import net.motionintelligence.client.api.TravelOptions;

public class PolygonResponse {

	private final String code;
	private final long requestTimeMillis;
	private final long roundTripTimeMillis;
	private final TravelOptions travelOptions;
	private final String result;

	/**
	 * @param code - route360 status code for the request
	 * @param requestTimeMillis - the milliseconds for server runtime
	 */
	public PolygonResponse(TravelOptions travelOptions, String result, String code, long requestTimeMillis, long roundTripTimeMillis) {
		this.travelOptions 	   	= travelOptions;
		this.code 			   	= code;
		this.result				= result;
		this.requestTimeMillis 	= requestTimeMillis;
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

	public String getResult() {
		return result;
	}
}

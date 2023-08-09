package com.targomo.client.api.response;

import com.targomo.client.api.TravelOptions;
import org.json.JSONObject;

public class PolygonResponse {

	private final ResponseCode code;
	private final long requestTimeMillis;
	private final long roundTripTimeMillis;
	private final TravelOptions travelOptions;
	private final JSONObject result;
	private final long parseTime;

	/**
	 *
	 * @param travelOptions Travel configuration
	 * @param result Response body
	 * @param code targomo status code for the request
	 * @param requestTimeMillis the milliseconds for server runtime
	 * @param roundTripTimeMillis the milliseconds for total roundtrip
	 */
	public PolygonResponse(TravelOptions travelOptions, JSONObject result, ResponseCode code, long requestTimeMillis, long roundTripTimeMillis) {
		this.travelOptions 	   	= travelOptions;
		this.code 			   	= code;
		this.result				= result;
		this.requestTimeMillis 	= requestTimeMillis;
		this.roundTripTimeMillis = roundTripTimeMillis;
		this.parseTime 			= -1;
	}
	
	public PolygonResponse(TravelOptions travelOptions, JSONObject result, ResponseCode code, long requestTimeMillis, long roundTripTimeMillis, long parseTime) {
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
	public ResponseCode getCode() {
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

	/**
	 * Generated polygons in JSON format. <p>
	 * Example: <br>
	 * <code> {
		 "requestTime": "2314",
		 "code": "ok",
		 "data": [{
			 "area": 7408443.013488605,
			 "polygons": [
			 {
				 "area": 7408443.013488605,
				 "travelTime": 300,
				 "outerBoundary": [
					 [
					 1488566,
					 6894821
					 ],
					 [
					 1488565,
					 6894837
					 ],
					 [
					 1488549,
					 6894837
					 ],
					 [
					 1488536,
					 6894837
					 ]
				 ],
				 "innerBoundary": [
					 [
					 1504982,
					 6895249
					 ],
					 [
					 1504987,
					 6895348
					 ],
					 [
					 1504882,
					 6895342
					 ],
					 [
					 1504823,
					 6895341
					 ],
					 [
					 1504729,
					 6895413
					 ]
				 ]
			 }
		 }]
	 } </code> </p>
	 * @return polygon data
	 */
	public JSONObject getResult() {
		return result;
	}

	public long getParseTime() {
		return parseTime;
	}
}

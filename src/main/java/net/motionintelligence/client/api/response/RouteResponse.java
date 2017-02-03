package net.motionintelligence.client.api.response;

import net.motionintelligence.client.api.TravelOptions;
import org.json.JSONArray;

public class RouteResponse {

	private final String code;
	private final long requestTimeMillis;
	private final TravelOptions travelOptions;
	private final JSONArray routes;

	/**
	 *
	 * @param travelOptions Travel configuration
	 * @param routes Response body
	 * @param code route360 status code for the request
	 * @param requestTimeMillis the milliseconds for server runtime
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
	 * Returns generated routes in JSON format.
	 * <p> Example: <br>
	 * <code> [{"travelTime": 483, "length": 4064.2, "target_id": "target0", "source_id": "source0",
	 *   "segments": [{"travelTime": 483,"length": 4064.2324777363283,"endname": "target", "type": "CAR",
	 *                 "startname": "source",
	 *                 "points": [[6890540,1493497,37],[6890662,1493215,39]]}]}] </code> </p>
	 * @return the routes
	 */
	public JSONArray getRoutes() {
		return routes;
	}
}

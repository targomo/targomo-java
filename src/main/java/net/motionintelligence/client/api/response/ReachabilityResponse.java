package net.motionintelligence.client.api.response;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReachabilityResponse {

	private final String code;
	private final long requestTimeMillis;
	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	
	private final Map<String,Integer> travelTimes = new HashMap<>();

	/**
	 * Create a response from JSON results, using given travel options
	 * @param travelOptions travel options, from the request
	 * @param result Travel times in JSON
	 * @param requestStart Start time of execution
	 */
	public ReachabilityResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {
		
		this.travelOptions 	   	  = travelOptions;
		this.code 		 	   	  = JsonUtil.getString(result, "code");
		this.requestTimeMillis 	  = result.has("requestTime") ? JsonUtil.getLong(result, "requestTime") : -1;
		this.totalTimeMillis 	  = System.currentTimeMillis() - requestStart;

		mapResults(result);
	}

	/**
	 * Create a response with custom response code and without results. Can be used in case of errors.
	 * @param travelOptions Travel options used in request
	 * @param code Response code
	 * @param requestTime Execution time in milliseconds
	 * @param requestStart Start time of execution
	 */
	public ReachabilityResponse(TravelOptions travelOptions, String code, long requestTime, long requestStart) {

		this.travelOptions 	   	  = travelOptions;
		this.code 		 	   	  = code;
		this.requestTimeMillis 	  = requestTime;
		this.totalTimeMillis = System.currentTimeMillis() - requestStart;
	}

	/**
	 * Parse results in JSON to travel times map.
	 * @param result resulting JSON
	 */
	public void mapResults(final JSONObject result) {
		JSONArray jsonArray = JsonUtil.getJsonArray(result, "data");
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject target = JsonUtil.getJSONObject(jsonArray, i);
			String trgId = JsonUtil.getString(target, "id");

			this.addTravelTime(trgId, JsonUtil.getInt(target, "travelTime"));
		}
	}

	/**
	 * @param targetId
	 * @param travelTime
	 */
	public void addTravelTime(String targetId, Integer travelTime) {
		
		this.travelTimes.put(targetId, travelTime);
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
	 * Map of source IDs - travel times
	 * @return travel time map
	 */
	public Map<String, Integer> getTravelTimes() {
		return this.travelTimes;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTotalTime() {
		return this.totalTimeMillis;
	}
}

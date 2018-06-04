package com.targomo.client.api.response;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.util.JsonUtil;
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
	private final Map<String,String> closestSourceId = new HashMap<>();

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
			if (target.has("source")) this.addClosestSource(trgId, JsonUtil.getString(target, "source"));
		}
	}

	/**
	 * Add travel time to a target
	 * @param targetId Target ID
	 * @param travelTime Travel time for the target
	 */
	public void addTravelTime(String targetId, Integer travelTime) {
		this.travelTimes.put(targetId, travelTime);
	}

	public void addClosestSource(String targetId, String closestSourceId) {
		this.closestSourceId.put(targetId, closestSourceId);
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
	 * Map of target IDs to the individual closest source ID
	 * @return target id - source id
	 */
	public Map<String, String> getClosestSourceIds(){ return this.closestSourceId; }
	
	/**
	 * Get total time in milliseconds
	 * @return total time
	 */
	public long getTotalTime() {
		return this.totalTimeMillis;
	}

	public String getClosestSourceForTarget(String targetId) {
		return this.closestSourceId.get(targetId);
	}
}

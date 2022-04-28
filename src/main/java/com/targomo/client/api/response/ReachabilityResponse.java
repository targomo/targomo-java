package com.targomo.client.api.response;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ReachabilityResponse {

	private final ResponseCode code;
	private final long requestTimeMillis;
	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	private final String message;

	private final Map<String,Integer> travelTimes = new HashMap<>();
	private final Map<String,String> closestSourceId = new HashMap<>();

	/**
	 * Create a response from JSON results, using given travel options
	 * @param travelOptions travel options, from the request
	 * @param result Travel times in JSON
	 * @param requestStart Start time of execution
	 */
	public ReachabilityResponse(TravelOptions travelOptions, JSONObject result, long requestStart) throws ResponseErrorException {
		this(travelOptions, result, requestStart, Function.identity());
	}

	/**
	 * Create a response from JSON results, using given travel options.
	 * If cached targets are used that are shared among multiple statistics it may be necessary to filter the targets and map their ids.
	 * To improve performance this can be done while parsing the response by passing a mapper/filter function.
	 * @param travelOptions travel options, from the request
	 * @param result Travel times in JSON
	 * @param requestStart Start time of execution
	 * @param targetIdMapperFilter a function that maps the target id to a different value or filters targets by returning null.
	 */
	public ReachabilityResponse(TravelOptions travelOptions, JSONObject result, long requestStart, Function<String, String> targetIdMapperFilter) throws ResponseErrorException {

		this.travelOptions 	   	  = travelOptions;
		this.code 		 	   	  = ResponseCode.fromString(JsonUtil.getString(result, "code"));
		this.requestTimeMillis 	  = result.has("requestTime") ? JsonUtil.getLong(result, "requestTime") : -1;
		this.totalTimeMillis 	  = System.currentTimeMillis() - requestStart;
		this.message              = result.has("message") ? JsonUtil.getString(result, "message") : "";

		// throw an exception in case of an error code
		if (this.code != ResponseCode.OK) {
			String msg = "Reachability request returned an error";
			if (!StringUtils.isEmpty(message)) {
				msg += ": " + message;
			}
			throw new ResponseErrorException(this.code, msg);
		}

		mapResults(result, targetIdMapperFilter);
	}

	/**
	 * Create a response with custom response code and without results. Can be used in case of errors.
	 * @param travelOptions Travel options used in request
	 * @param code Response code
	 * @param requestTime Execution time in milliseconds
	 * @param requestStart Start time of execution
	 */
	public ReachabilityResponse(TravelOptions travelOptions, ResponseCode code, long requestTime, long requestStart) {

		this.travelOptions 	   	  = travelOptions;
		this.code 		 	   	  = code;
		this.requestTimeMillis 	  = requestTime;
		this.totalTimeMillis = System.currentTimeMillis() - requestStart;
		this.message = "";
	}

	public ReachabilityResponse(TravelOptions travelOptions, ResponseCode code, long requestTimeMillis, long totalTimeMillis, String message) {
		this.travelOptions = travelOptions;
		this.code = code;
		this.requestTimeMillis = requestTimeMillis;
		this.totalTimeMillis = totalTimeMillis;
		this.message = message;
	}

	/**
	 * Parse results in JSON to travel times map.
	 * Applies the given function to each target id to modify the id or filter the target.
	 * @param result resulting JSON
	 * @param targetIdMapperFilter a function that maps the target id to a different value or filters targets by returning null.
	 */
	protected void mapResults(final JSONObject result, Function<String, String> targetIdMapperFilter) {
		JSONArray jsonArray = JsonUtil.getJsonArray(result, "data");
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject target = JsonUtil.getJSONObject(jsonArray, i);
			String trgId = JsonUtil.getString(target, "id");
			trgId = targetIdMapperFilter.apply(trgId);

			if (trgId != null) {
				this.addTravelTime(trgId, JsonUtil.getInt(target, "travelTime"));
				if (target.has("source")) this.addClosestSource(trgId, JsonUtil.getString(target, "source"));
			}
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

	public String getMessage() {
		return this.message;
	}

	public String getClosestSourceForTarget(String targetId) {
		return this.closestSourceId.get(targetId);
	}
}

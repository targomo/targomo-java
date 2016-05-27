package net.motionintelligence.client.api.response;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.util.JsonUtil;

public class ReachabilityResponse {

	private final String code;
	private final long requestTimeMillis;
	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	
	private final Map<String,Integer> travelTimes = new HashMap<>();

	/**
	 * @param travelOptions2
	 * @param result
	 * @param totalTimeMillis 
	 */
	public ReachabilityResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {
		
		this.travelOptions 	   	  = travelOptions;
		this.code 		 	   	  = JsonUtil.getString(result, "code");
		this.requestTimeMillis 	  = result.has("requestTime") ? JsonUtil.getLong(result, "requestTime") : -1;
		this.totalTimeMillis = System.currentTimeMillis() - requestStart;
		
		JSONArray jsonArray = JsonUtil.getJsonArray(result, "data");
		for ( int i = 0 ; i < jsonArray.length() ; i++) {
		
			JSONObject target = JsonUtil.getJSONObject(jsonArray, i);
			String trgId      = JsonUtil.getString(target, "id");
			
			this.addTravelTime(trgId, JsonUtil.getInt(target, "travelTime"));
		}
	}

	/**
	 * 
	 * @param travelOptions
	 * @param code
	 * @param requestime
	 */
	public ReachabilityResponse(TravelOptions travelOptions, String code, long requestime, long requestStart) {
		
		this.travelOptions 	   	  = travelOptions;
		this.code 		 	   	  = code;
		this.requestTimeMillis 	  = requestime;
		this.totalTimeMillis = System.currentTimeMillis() - requestStart;
	}

	/**
	 * @param source
	 * @param target
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
	 * 
	 * @return
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

package net.motion_intelligence.client.api.response;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import net.motion_intelligence.client.api.TravelOptions;
import net.motion_intelligence.client.api.geo.Source;
import net.motion_intelligence.client.api.geo.Target;
import net.motion_intelligence.client.api.util.JsonUtil;

public class TimeResponse {

	private final String code;
	private final long requestTimeMillis;
	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	
	private final Map<Source, Map<Target,Integer>> travelTimes = new HashMap<>();

	/**
	 * @param travelOptions2
	 * @param result
	 * @param totalTimeMillis 
	 */
	public TimeResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {
		
		this.travelOptions 	   	  = travelOptions;
		this.code 		 	   	  = JsonUtil.getString(result, "code");
		this.requestTimeMillis 	  = result.has("requestTime") ? JsonUtil.getLong(result, "requestTime") : -1;
		this.totalTimeMillis = System.currentTimeMillis() - requestStart;
		
		JSONArray jsonArray = JsonUtil.getJsonArray(result, "data");
		for ( int i = 0 ; i < jsonArray.length() ; i++) {
		
			JSONObject source = JsonUtil.getJSONObject(jsonArray, i);
			String srcId 	  = JsonUtil.getString(source, "id");
			
			JSONArray targets = JsonUtil.getJsonArray(source, "targets");
			
			for ( int j = 0 ; j < targets.length() ; j++) {
				
				JSONObject target = JsonUtil.getJSONObject(targets, j);
				String trgId      = JsonUtil.getString(target, "id");
				
				this.addTravelTime(travelOptions.getSource(srcId), travelOptions.getTarget(trgId), JsonUtil.getInt(target, "travelTime"));
			}
		}
	}

	/**
	 * 
	 * @param travelOptions
	 * @param code
	 * @param requestime
	 */
	public TimeResponse(TravelOptions travelOptions, String code, long requestime, long requestStart) {
		
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
	public void addTravelTime(Source source, Target target, Integer travelTime) {
		
		this.travelTimes.putIfAbsent(source, new HashMap<>());
		this.travelTimes.get(source).put(target, travelTime);
	}
	
	/**
	 * @param source
	 * @param target
	 * @return null if the source or the target is not available, the travel time otherwise
	 */
	public Integer getTravelTime(Source source, Target target) {
		return this.travelTimes.getOrDefault(source, null).getOrDefault(target, -1);
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
	public Map<Source, Map<Target, Integer>> getTravelTimes() {
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

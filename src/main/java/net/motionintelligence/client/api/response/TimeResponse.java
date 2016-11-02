package net.motionintelligence.client.api.response;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TimeResponse {

	private final String code;
	private final long requestTimeMillis;
	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	
	private final Map<Coordinate, Map<Coordinate,Integer>> travelTimes = new HashMap<>();

	/**
	 * @param travelOptions
	 * @param result
	 * @param requestStart
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
	public void addTravelTime(Coordinate source, Coordinate target, Integer travelTime) {
		
		this.travelTimes.putIfAbsent(source, new HashMap<>());
		this.travelTimes.get(source).put(target, travelTime);
	}
	
	/**
	 * @param source
	 * @param target
	 * @return null if the source or the target is not available, the travel time otherwise
	 */
	public Integer getTravelTime(Coordinate source, Coordinate target) {
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
	 * Get travel times from each source point to each target point.
	 * @return map for sources -> (targets, travel times)
	 */
	public Map<Coordinate, Map<Coordinate, Integer>> getTravelTimes() {
		return this.travelTimes;
	}
	
	/**
	 * @return Total execution time
	 */
	public long getTotalTime() {
		return this.totalTimeMillis;
	}
}

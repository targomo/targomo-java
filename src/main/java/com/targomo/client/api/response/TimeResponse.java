package com.targomo.client.api.response;

import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.pojo.TravelWeight;
import com.targomo.client.api.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TimeResponse {

	private static final TravelWeight EMPTY_TRAVELWEIGHT = new TravelWeight(-1, -1);

	private final String code;
	private final long requestTimeMillis;
	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	
	private final Map<Coordinate, Map<Coordinate,TravelWeight>> travelWeights = new HashMap<>();
	private Map<Coordinate, Map<Coordinate, Integer>> travelTimes = null;
	private Map<Coordinate, Map<Coordinate, Integer>> travelDistances = null;

	/**
	 * Create a response from JSON results.
	 * @param travelOptions Travel options used in request
	 * @param result Travel times in JSON
	 * @param requestStart Start time of execution
	 */
	public TimeResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {
		
		this.travelOptions 	   	  = travelOptions;
		this.code 		 	   	  = JsonUtil.getString(result, "code");
		this.requestTimeMillis 	  = result.has("requestTime") ? JsonUtil.getLong(result, "requestTime") : -1;
		this.totalTimeMillis = System.currentTimeMillis() - requestStart;

		mapResults(result);
	}

	/**
	 * Create a response with custom response code and without results. Can be used in case of errors.
	 * @param travelOptions Travel options used in request
	 * @param code Response code
	 * @param requestTime Execution time in milliseconds
	 * @param requestStart Start time of execution
	 */
	public TimeResponse(TravelOptions travelOptions, String code, long requestTime, long requestStart) {

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
		if (travelOptions == null)
			throw new TargomoClientRuntimeException("Unsupported call");

		mapResults(travelOptions, result);
	}

	/**
	 * Parse results in JSON to travel times map.
	 * @param travelOptions options used in the request
	 * @param result resulting JSON
	 */
	public void mapResults(final TravelOptions travelOptions, final JSONObject result) {
		JSONArray jsonArray = JsonUtil.getJsonArray(result, "data");
		for (int i = 0 ; i < jsonArray.length(); i++) {
			JSONObject source = JsonUtil.getJSONObject(jsonArray, i);
			String srcId = JsonUtil.getString(source, "id");
			JSONArray targets = JsonUtil.getJsonArray(source, "targets");

			this.travelWeights.putIfAbsent(travelOptions.getSource(srcId), new HashMap<>());

			for (int j = 0; j < targets.length(); j++) {
				JSONObject target = JsonUtil.getJSONObject(targets, j);
				String trgId = JsonUtil.getString(target, "id");

				addTravelWeight(travelOptions.getSource(srcId), travelOptions.getTarget(trgId),
						JsonUtil.getInt(target, "travelTime"), JsonUtil.getInt(target, "length"));
			}
		}
	}

	/**
	 * @param source Source coordinate
	 * @param target Target coordinate
	 * @param travelTime Travel time to be added
	 * @param length Travel distance to be added
	 */
	public void addTravelWeight(Coordinate source, Coordinate target, Integer travelTime, Integer length) {
		
		this.travelWeights.putIfAbsent(source, new HashMap<>());
		this.travelWeights.get(source).put(target, new TravelWeight(length, travelTime));
	}

	/**
	 * @param source Source coordinate
	 * @param target Target coordinate
	 * @return null if the source or the target is not available, the travel time otherwise
	 */
	public Integer getTravelTime(Coordinate source, Coordinate target) {
		return this.getTravelWeight(source, target).getTravelTime();
	}

	/**
	 * @param source Source coordinate
	 * @param target Target coordinate
	 * @return null if the source or the target is not available, the travel time otherwise
	 */
	public Integer getLength(Coordinate source, Coordinate target) {
		return this.getTravelWeight(source, target).getTravelDistance();
	}

	/**
	 * @param source Source coordinate
	 * @param target Target coordinate
	 * @return null if the source or the target is not available, the travel weight otherwise
	 */
	public TravelWeight getTravelWeight(Coordinate source, Coordinate target) {
		return this.travelWeights.getOrDefault(source, Collections.emptyMap()).getOrDefault(target, EMPTY_TRAVELWEIGHT);
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
	 * Get travel weights from each source point to each target point.
	 * @return map from each source to (targets, travel times)
	 */
	public Map<Coordinate, Map<Coordinate, TravelWeight>> getTravelWeights() {
		return this.travelWeights;
	}

	/**
	 * Get travel times from each source point to each target point.
	 * @return map from each source to (targets, travel times)
	 */
	public Map<Coordinate, Map<Coordinate, Integer>> getTravelTimes() {

		if (travelTimes == null) {
			travelTimes = new HashMap<>();

			this.travelWeights.entrySet().forEach(entry ->
					travelTimes.put(
							entry.getKey(),
							entry.getValue().entrySet().stream()
									.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getTravelTime())))
			);
		}
		return travelTimes;
	}

	/**
	 * Get travel distances from each source point to each target point.
	 * @return map from each source to (targets, lengths)
	 */
	public Map<Coordinate, Map<Coordinate, Integer>> getLengths() {

		if (travelDistances == null) {
			travelDistances = new HashMap<>();

			this.travelWeights.entrySet().forEach(entry ->
					travelDistances.put(
							entry.getKey(),
							entry.getValue().entrySet().stream()
									.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getTravelDistance())))
			);

		}
		return travelDistances;
	}

	/**
	 * @return Total execution time
	 */
	public long getTotalTime() {
		return this.totalTimeMillis;
	}
}

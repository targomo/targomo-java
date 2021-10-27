package com.targomo.client.api.response;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.response.statistics.StatisticGeometryValueResult;
import com.targomo.client.api.util.JsonUtil;
import org.json.JSONObject;

public class StatisticsGeometryValueResponse {

	private final long requestTimeMillis;
	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	private final StatisticGeometryValueResult statisticGeometryValueResult;

	/**
	 *
	 * @param travelOptions Travel configuration
	 * @param result Response body
	 * @param requestStart Start time of request in milliseconds
	 */
	public StatisticsGeometryValueResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {

		this.travelOptions 	   	  = travelOptions;
		this.requestTimeMillis 	  = result.has("requestTime") ? JsonUtil.getLong(result, "requestTime") : -1;
		this.totalTimeMillis 	  = System.currentTimeMillis() - requestStart;

		this.statisticGeometryValueResult 	  = new StatisticGeometryValueResult(travelOptions, result);
	}

	/**
	 *
	 * @param travelOptions Travel configuration
	 * @param string Error message, if any
	 * @param roundTripTime Total roundtrip time
	 * @param requestStart Start time of request in milliseconds
	 */
	public StatisticsGeometryValueResponse(TravelOptions travelOptions, String string, long roundTripTime, long requestStart) {

		this.travelOptions 	   	  = travelOptions;
		this.requestTimeMillis 	  = roundTripTime;
		this.totalTimeMillis 	  = System.currentTimeMillis() - requestStart;
		this.statisticGeometryValueResult 	  = null;
	}
	
	/**
	 * @return the requestTimeMillis
	 */
	public long getRequestTimeMillis() {
		return requestTimeMillis;
	}

	/**
	 * @return the totalTimeMillis
	 */
	public long getTotalTimeMillis() {
		return totalTimeMillis;
	}

	/**
	 * @return the travelOptions
	 */
	public TravelOptions getTravelOptions() {
		return travelOptions;
	}

	/**
	 * @return the statisticGeometryValueResult
	 */
	public StatisticGeometryValueResult getStatisticGeometryValueResult() {
		return statisticGeometryValueResult;
	}
}

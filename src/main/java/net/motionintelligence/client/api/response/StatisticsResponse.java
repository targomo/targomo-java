package net.motionintelligence.client.api.response;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.response.statistics.StatisticResult;
import net.motionintelligence.client.api.util.JsonUtil;

public class StatisticsResponse {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsResponse.class);

	private final long requestTimeMillis;
	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	private final StatisticResult statisticResult;
	
	/**
	 * 
	 * @param travelOptions
	 * @param result
	 * @param requestStart
	 */
	public StatisticsResponse(TravelOptions travelOptions, JSONObject result, long requestStart) {
		
		this.travelOptions 	   	  = travelOptions;
		this.requestTimeMillis 	  = result.has("requestTime") ? JsonUtil.getLong(result, "requestTime") : -1;
		this.totalTimeMillis 	  = System.currentTimeMillis() - requestStart;
		
		this.statisticResult 	  = new StatisticResult(travelOptions, result);
	}
	
	/**
	 * 
	 * @param travelOptions
	 * @param string
	 * @param roundTripTime
	 * @param requestStart
	 */
	public StatisticsResponse(TravelOptions travelOptions, String string, long roundTripTime, long requestStart) {
		
		this.travelOptions 	   	  = travelOptions;
		this.requestTimeMillis 	  = roundTripTime;
		this.totalTimeMillis 	  = System.currentTimeMillis() - requestStart;
		this.statisticResult 	  = null;
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
	 * @return the statisticResult
	 */
	public StatisticResult getStatisticResult() {
		return statisticResult;
	}
}

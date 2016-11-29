package net.motionintelligence.client.api.response;

import java.util.HashMap;
import java.util.Map;

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
		
		this.statisticResult 	  = new StatisticResult(parseReachablePeopleResult(result));
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
	 * 
	 * @param result
	 * @return
	 */
	public Map<Integer, Map<Integer, Double>> parseReachablePeopleResult(JSONObject result)  {
		
		long start = System.currentTimeMillis();
		Map<Integer, Map<Integer, Double>> reachablePersonsByStatistic = new HashMap<>();
		
		JSONObject results = JsonUtil.getJSONObject(result, "statistics");
		
		for ( Integer statistic : JsonUtil.getSortedIntKeySet(results) ) {
		
			Map<Integer, Double> reachablePersons = new HashMap<>();
			this.travelOptions.getTravelTimes().forEach(time -> reachablePersons.put(time, 0D));
			
			JSONObject secondsToResult = JsonUtil.getJSONObject(results, statistic + "");
			
			for ( Integer currentTravelTime : JsonUtil.getSortedIntKeySet(secondsToResult) ) 
				for ( Integer time : this.travelOptions.getTravelTimes() )
					if ( Integer.valueOf(currentTravelTime) <= time ) 
						reachablePersons.put(time, reachablePersons.get(time) + JsonUtil.getDouble(secondsToResult, currentTravelTime + ""));
			
			reachablePersonsByStatistic.put(statistic, reachablePersons);
		}
		
		LOGGER.info(String.format("It took %sms to parse the population results!", System.currentTimeMillis() - start));
		
		return reachablePersonsByStatistic;
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

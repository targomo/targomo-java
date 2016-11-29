package net.motionintelligence.client.api.response.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.util.JsonUtil;

public class StatisticResult {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticResult.class);

	private final Map<Integer, Map<Integer, Double>> statistics;
	private final Map<Integer, Integer> reachableTargets;
	private final Map<Integer, Integer> targetTravelTimes;
	private final TravelOptions options;

	/**
	 * 
	 * @param jsonResult
	 */
	public StatisticResult(TravelOptions options, JSONObject jsonResult) {
		
		this.options		   = options;
		this.statistics 	   = parseReachableStatisticsResult(jsonResult);
		this.reachableTargets  = parseReachableTargetsResult(jsonResult);
		this.targetTravelTimes = parseTargetTravelTimes(jsonResult);
	}
	
	/**
	 * 
	 * @param jsonResult
	 * @return
	 */
	private Map<Integer, Integer> parseReachableTargetsResult(JSONObject jsonResult) {
		
		Map<Integer, Integer> reachableTargets = new TreeMap<>();
		
		JSONObject minuteToReachableTargets = JsonUtil.getJSONObject(jsonResult, "reachableTargets");
		
		for ( Integer minute : JsonUtil.getSortedIntKeySet(minuteToReachableTargets) )
			if ( minute <= this.options.getMaxRoutingTime() )
				reachableTargets.put(minute, JsonUtil.getInt(minuteToReachableTargets, minute + ""));
			
		return reachableTargets;
	}

	/**
	 * 
	 * @param jsonResult
	 * @return
	 */
	private Map<Integer, Integer> parseTargetTravelTimes(JSONObject jsonResult) {

		Map<Integer, Integer> targetTravelTimes = new TreeMap<>();
		
		JSONObject targetToMinute = JsonUtil.getJSONObject(jsonResult, "targetTravelTimes");
		
		for ( Integer targetId : JsonUtil.getSortedIntKeySet(targetToMinute) ) 
			if ( JsonUtil.getInt(targetToMinute, targetId + "") <= this.options.getMaxRoutingTime() )
				targetTravelTimes.put(targetId, JsonUtil.getInt(targetToMinute, targetId + ""));
			
		return targetTravelTimes;
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	public Map<Integer, Map<Integer, Double>> parseReachableStatisticsResult(JSONObject result)  {
		
		long start = System.currentTimeMillis();
		Map<Integer, Map<Integer, Double>> reachablePersonsByStatistic = new HashMap<>();
		
		JSONObject statistics = JsonUtil.getJSONObject(result, "statistics");
		
		for ( Integer statistic : JsonUtil.getSortedIntKeySet(statistics) ) {
		
			JSONObject minuteToValues 			  = JsonUtil.getJSONObject(statistics, statistic + "");
			Map<Integer, Double> reachablePersons = new TreeMap<>();
			
			for ( Integer minute : JsonUtil.getSortedIntKeySet(minuteToValues)) 
				if ( minute <= this.options.getMaxRoutingTime() )
					reachablePersons.put(minute, JsonUtil.getDouble(minuteToValues, minute + ""));
			
			reachablePersonsByStatistic.put(statistic, reachablePersons);
		}
		
		LOGGER.info(String.format("It took %sms to parse the population results!", System.currentTimeMillis() - start));
		
		return reachablePersonsByStatistic;
	}

	/**
	 * @return
	 */
	public Map<Integer, Map<Integer, Double>> getStatistics() {
		return statistics;
	}
	
	/**
	 * @return the reachableTargets
	 */
	public Map<Integer, Integer> getReachableTargets() {
		return reachableTargets;
	}

	/**
	 * @return the targetTravelTimes
	 */
	public Map<Integer, Integer> getTargetTravelTimes() {
		return targetTravelTimes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("\n  statistics: \n");
		
		for ( Integer statisticId : this.statistics.keySet() ) {

			builder.append("    " + statisticId + "\n");
			builder.append("    ---------------\n");
			for ( Map.Entry<Integer, Double> entry : this.statistics.get(statisticId).entrySet() )
				builder.append("      " + String.format("%6d", entry.getKey()) + ":" + String.format("%14.2f", entry.getValue()) + "\n");
		}
		
		builder.append("\n  reachableTargets: \n");
		builder.append("  ---------------\n");
		for ( Map.Entry<Integer, Integer> entry : this.reachableTargets.entrySet() )
			builder.append("    " + String.format("%6d", entry.getKey()) + ":" + String.format("%6d", entry.getValue()) + "\n");
		
		builder.append("\n  targetTravelTimes: \n");
		builder.append("  ---------------\n");
		for ( Map.Entry<Integer, Integer> entry : this.targetTravelTimes.entrySet() )
			builder.append("    " + String.format("%6d", entry.getKey()) + ":" + String.format("%6d", entry.getValue()) + "\n");
		
		builder.append("}\n");
		return builder.toString();
	}

}

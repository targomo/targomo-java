package com.targomo.client.api.response.statistics;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.util.JsonUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;

public class StatisticResult {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticResult.class);

	private final Map<Integer, Map<Integer, Double>> statistics;
	private final Map<Integer, Integer> reachableTargets;
	private final Map<Integer, Integer> targetTravelTimes;
	private final TravelOptions options;

	/**
	 * @param options Travel configuration
	 * @param jsonResult Result as json object
	 */
	public StatisticResult(TravelOptions options, JSONObject jsonResult) {
		
		this.options		   = options;
		this.statistics 	   = jsonResult.has("statistics") ? parseReachableStatisticsResult(jsonResult) : Collections.emptyMap();
		this.reachableTargets  = jsonResult.has("reachableTargets") ? parseReachableTargetsResult(jsonResult) : Collections.emptyMap();
		this.targetTravelTimes = jsonResult.has("targetTravelTimes") ? parseTargetTravelTimes(jsonResult) : Collections.emptyMap();
	}
	
	/**
	 * 
	 * @param jsonResult result as json object
	 * @return Map of reachable targets
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
	 * @param jsonResult result as json object
	 * @return Map of target travel times
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
	 * @param result result as json object
	 * @return Map of targets with reachability and statistics
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
	 * This method returns the aggragtion value for all values for the
	 * specified statistic and the given minute (starting from 0). So you can use this
	 * method to generate the sum or the average of the reachable "targets"
	 * for a statistic and minute.
	 *
	 * @param statisticId the id of the statistic
	 * @param endMinute - the minute to which we want to aggregate (inclusive)
	 * @param aggregatorFunction the function that we want to use to aggregate the found values
	 *
	 * @return the result of the aggregation
	 *
	 * @throws NullPointerException if no statistics have been set or there are no
	 * 	values for the current statistic id
	 */
	public OptionalDouble getAggregationToMinute(Integer statisticId, Integer endMinute, ToDoubleFunction<DoubleStream> aggregatorFunction) {
		return getAggregationToMinute(statisticId, 0, endMinute, aggregatorFunction);
	}

	/**
	 * This method returns the aggragtion value for all values for the
	 * specified statistic and the given minute (starting from 0). So you can use this
	 * method to generate the sum or the average of the reachable "targets"
	 * for a statistic and minute.
	 *
	 * @param statisticId the id of the statistic
	 * @param startMinute - the minute of the interval to start from (inclusive)
	 * @param endMinute - the minute to which we want to aggregate (inclusive)
	 * @param aggregatorFunction the function that we want to use to aggregate the found values
	 *
	 * @return the result of the aggregation

	 * @throws NullPointerException if no statistics have been set or there are no
	 * 	values for the current statistic id
	 */
	public OptionalDouble getAggregationToMinute(Integer statisticId, Integer startMinute, Integer endMinute, ToDoubleFunction<DoubleStream> aggregatorFunction) {

		Function<DoubleStream,OptionalDouble> converter = stream -> OptionalDouble.of(aggregatorFunction.applyAsDouble(stream));
		return getAggToMinute(statisticId, startMinute, endMinute, converter);
	}

	/**
	 * Private helper function to make it possible to also use aggregator
	 * functions which only return a OptionalDouble
	 *
	 * @param statisticId - the id of the statistic of a group
	 * @param startMinute - the minute of the interval to start from (inclusive)
	 * @param endMinute   - the minute of the interval to end at (inclusive)
	 * @param aggregatorFunction - the aggeragtion function
	 *
	 * @return the result of the aggregation
	 *
	 * @throws NullPointerException if no statistics have been set or there are no
	 * 	values for the current statistic id
	 */
	private OptionalDouble getAggToMinute(Integer statisticId, Integer startMinute, Integer endMinute, Function<DoubleStream,OptionalDouble> aggregatorFunction) {

		return aggregatorFunction.apply(
				this.statistics.get(statisticId).entrySet()
						.parallelStream()
						.filter(entry -> startMinute <= entry.getKey() && entry.getKey() <= endMinute)
						.mapToDouble(Map.Entry::getValue));
	}

	public Map<Integer, Map<Integer, Double>> getStatistics() {
		return statistics;
	}

	public Map<Integer, Integer> getReachableTargets() {
		return reachableTargets;
	}

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

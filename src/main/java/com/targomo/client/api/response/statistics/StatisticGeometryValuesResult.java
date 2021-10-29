package com.targomo.client.api.response.statistics;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StatisticGeometryValuesResult {
	@Getter
	private final Map<String, StatisticsGeometryValue> results;
	@Getter
	private final TravelOptions options;

	/**
	 * @param options Travel configuration
	 * @param jsonResult Result as json object
	 */
	public StatisticGeometryValuesResult(TravelOptions options, JSONObject jsonResult) {
		this.options	= options;
		this.results 	= parseStatisticsGeometryValues(jsonResult);
	}
	
	/**
	 * 
	 * @param jsonResult result as json object
	 * @return Map of reachable targets
	 */
	private Map<String, StatisticsGeometryValue> parseStatisticsGeometryValues(JSONObject jsonResult) {
		
		Map<String, StatisticsGeometryValue> values = new HashMap<>();

		final Iterator<String> keysIterator = jsonResult.keys();
		while ( keysIterator.hasNext() ) {

			final String key = keysIterator.next();
			final JSONObject obj = JsonUtil.getJSONObject(jsonResult, key);

			values.put(key, new StatisticsGeometryValue(JsonUtil.getString(obj, "aggregation"),
					JsonUtil.getInt(obj, "statisticId").shortValue(),
					JsonUtil.getDouble(obj, "value")));
		}
			
		return values;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("\n  results: \n");
		builder.append("    ---------------\n");
		
		for ( Map.Entry<String, StatisticsGeometryValue> result : this.results.entrySet() ) {

			builder.append("    " + result.getKey() + ":" + String.format(" { aggregation: %s, statisticsID: %o, value: %f }",
					result.getValue().getAggregation(),
					result.getValue().getStatisticsID(),
					result.getValue().getValue()) + "\n");
			builder.append("    ---------------\n");
		}

		builder.append("}\n");
		return builder.toString();
	}

	@AllArgsConstructor
	@Getter
	public static class StatisticsGeometryValue{
		private final String aggregation;
		private final short statisticsID;
		private final double value;
	}

}

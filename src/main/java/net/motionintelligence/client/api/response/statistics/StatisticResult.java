package net.motionintelligence.client.api.response.statistics;

import java.util.Map;

public class StatisticResult {

	private final Map<Integer, Map<Integer, Double>> result;

	/**
	 * 
	 * @param result
	 */
	public StatisticResult(Map<Integer, Map<Integer, Double>> result) {
		this.result = result;
	}

	/**
	 * @return
	 */
	public Map<Integer, Map<Integer, Double>> getResult() {
		return result;
	}
}

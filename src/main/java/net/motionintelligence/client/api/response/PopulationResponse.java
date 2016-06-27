package net.motionintelligence.client.api.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.response.population.TravelTypeStatistic;
import net.motionintelligence.client.api.util.JsonUtil;

public class PopulationResponse {

	private final long totalTimeMillis;
	private final TravelOptions travelOptions;
	private List<TravelTypeStatistic> statistics = new ArrayList<>();
	
	/**
	 * @param travelOptions2
	 * @param result
	 * @param totalTimeMillis 
	 * @throws JSONException 
	 */
	public PopulationResponse(TravelOptions travelOptions, JSONArray result, long requestStart) throws JSONException {
		
		this.travelOptions 	   	  = travelOptions;
		this.totalTimeMillis      = System.currentTimeMillis() - requestStart;
		
		// [{"statistic":"population_total","travelType":"transit","values": [133.0,840.0,397.0,603.0,190 ... 58536.0,9593.0,61038.0,11465.0,127.0]}]
		
		for ( int i = 0 ; i < result.length() ; i++) {
		
			JSONObject travelTypeStatistic = JsonUtil.getJSONObject(result, i);
			
			List<Double> newValues = new ArrayList<>();
			JSONArray values 	   = travelTypeStatistic.getJSONArray("values");
			for ( int j = 0 ; j < values.length() ; j++) 
				newValues.add(values.getDouble(j));
			
			TravelTypeStatistic tts = new TravelTypeStatistic();
			tts.setStatistic(travelTypeStatistic.getString("statistic"));
			tts.setTravelType(TravelType.parse(travelTypeStatistic.getString("travelType")));
			tts.setValues(newValues);
			
			this.getStatistics().add(tts);
		}
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
	public long getTotalTime() {
		return this.totalTimeMillis;
	}

	/**
	 * @return the statistics
	 */
	public List<TravelTypeStatistic> getStatistics() {
		return statistics;
	}
}

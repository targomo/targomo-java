package net.motionintelligence.client.api.response.population;

import java.util.ArrayList;
import java.util.List;

import net.motionintelligence.client.api.enums.TravelType;

/**
 * A TravelTypeStatistic.
 */
public class TravelTypeStatistic {

    private List<Integer> values = new ArrayList<>();
    private String statistic;
    private TravelType travelType;
    
	/**
	 * @return the statistic
	 */
	public String getStatistic() {
		return statistic;
	}

	/**
	 * @param statistic the statistic to set
	 */
	public void setStatistic(String statistic) {
		this.statistic = statistic;
	}

	/**
	 * 
	 */
	public TravelTypeStatistic(){}
    
	/**
	 * 
	 * @return
	 */
    public TravelType getTravelType() {
        return travelType;
    }

    /**
     * 
     * @param travelType
     */
    public void setTravelType(TravelType travelType) {
        this.travelType = travelType;
    }

    /**
     * 
     * @param minute
     * @return
     */
    public double getValueToMinute(int minute) {
    	return this.values.subList(0, Math.min(this.values.size(), minute)).stream().mapToDouble(Integer::intValue).sum();
    }
    
    /**
     * @return the values
     */
    public List<Integer> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List<Integer> values) {
        this.values = values;
    }
}

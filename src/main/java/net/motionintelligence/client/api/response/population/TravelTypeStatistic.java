package net.motionintelligence.client.api.response.population;

import java.util.ArrayList;
import java.util.List;

import net.motionintelligence.client.api.enums.TravelType;

/**
 * A TravelTypeStatistic.
 */
public class TravelTypeStatistic {

	private List<Double> values = new ArrayList<>();
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
    	
    	Double reduce = this.values.subList(0, Math.min(this.values.size(), minute)).stream().reduce(0.0, Double::sum);
    	
    	return reduce;
    }
    
    /**
     * @return the values
     */
    public List<Double> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List<Double> values) {
        this.values = values;
    }
    
    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TravelTypeStatistic {\n\tvalues: " + values + "\n\tstatistic: " + statistic + "\n\ttravelType: " + travelType + "\n}";
	}
}

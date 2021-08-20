package com.targomo.client.api.geo;

import com.targomo.client.api.pojo.AggregationInputParameters;

public abstract class AbstractLocation {

    protected String id;
    protected AggregationInputParameters aggregationInputParameters;

    // needed for jackson
    public AbstractLocation(){}


    public AbstractLocation(String id, AggregationInputParameters aggregationInputParameters){
        this.id = id;
        this.aggregationInputParameters = aggregationInputParameters;
    }

    /**
     * Get the ID associated with the location.
     * @return Location ID
     */
    public String getId() {
        return id;
    }

    /**
     * Assign an ID to the location
     * @param id ID to be assigned
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get the aggregation input parameters of a location
     * @return Location Properties
     */
    public AggregationInputParameters getAggregationInputParameters() { return this.aggregationInputParameters; }

    /**
     * Assign aggregation input parameters to a location
     * @param aggregationInputParameters aggregation input parameters to be assigned
     */
    public void setAggregationInputParameters(final AggregationInputParameters aggregationInputParameters){ this.aggregationInputParameters = aggregationInputParameters; }

}

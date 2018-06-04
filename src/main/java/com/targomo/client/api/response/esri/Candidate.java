package com.targomo.client.api.response.esri;

import com.targomo.client.api.geo.DefaultTargetCoordinate;

/**
 * Geocoding candidate for a requested address. It is immutable with a private constructor since it is only
 * meant to be created from a json object (as a REST response).
 */
public class Candidate {
    private final double score;
    private final DefaultTargetCoordinate location;

    /**
     * private - not used since this is a POJO only created from a json String
     */
    private Candidate(double score, DefaultTargetCoordinate df){
        this.score = score;
        this.location = df;
    }

    /**
     * Return the Score of the candidate. The higher the score the more confidence into the candidate as the correct
     * representative. Maximum score is 100.0; Minimum 0.0.
     *
     * @return the score of the candidate
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Returns the geocode as {@link DefaultTargetCoordinate} according to the spatial reference from the request.
     * Default reference is "EPSG 4326" (.i.e. if not otherwise specified)
     * @return the geo location of the candidate
     */
    public DefaultTargetCoordinate getLocation(){
        return this.location;
    }
}

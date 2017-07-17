package net.motionintelligence.client.api.response;

import net.motionintelligence.client.api.geo.DefaultTargetCoordinate;
import net.motionintelligence.client.api.util.POJOUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Response of the ESRI geocoding REST service for a requested address. It is immutable with a private constructor
 * since it is only meant to be created from the returned json object. This is not the complete response from the
 * REST service - only relevant information is captured, the remainder is discarded.
 *
 * Created by David on 17.07.2017.
 */
public class GeocodingResponse implements Iterable<GeocodingResponse.Candidate>{

    private final List<Candidate> candidates;

    private GeocodingResponse(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public DefaultTargetCoordinate getRepresentativeGeocodeOfRequest() {
        Candidate mostLikelyCandidate = this.candidates.get(0);
        return mostLikelyCandidate.getLocation();
    }

    /**
     * Pretty prints the {@link GeocodingResponse}, i.e. with indention and line breaks.
     * @return
     */
    @Override
    public String toString() {
        return POJOUtil.prettyPrintPOJO(this);
    }

    @Override
    public Iterator<Candidate> iterator() {
        return candidates.iterator();
    }

    /**
     * Geocoding candidate for a requested address. It is immutable with a private constructor since it is only
     * meant to be created from a json object (as a REST response).
     */
    public class Candidate {
        private final double score;
        private final DefaultTargetCoordinate location;

        private Candidate(double score, DefaultTargetCoordinate df){
            this.score = score;
            this.location = df;
        }

        public double getScore() {
            return this.score;
        }

        public DefaultTargetCoordinate getLocation(){
            return this.location;
        }
    }
}

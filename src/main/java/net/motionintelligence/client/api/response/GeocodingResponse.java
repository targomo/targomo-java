package net.motionintelligence.client.api.response;

import net.motionintelligence.client.api.geo.DefaultTargetCoordinate;
import net.motionintelligence.client.api.util.POJOUtil;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Response of the ESRI geocoding REST service for a requested address. It is immutable with a private constructor
 * since it is only meant to be created from the returned json object.
 *
 * Note: This is not the complete response from the REST service - only relevant information is captured, the remainder
 * is discarded.
 *
 * Created by David on 17.07.2017.
 */
public class GeocodingResponse implements Iterable<GeocodingResponse.Candidate>{

    private final List<Candidate> candidates;

    private GeocodingResponse(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    /**
     * @return the first (and thus best) candidate coordinates contained in this geocoding response.
     *
     * @throws NoSuchElementException when response contains no candidates
     */
    public DefaultTargetCoordinate getRepresentativeGeocodeOfRequest() {
        if( this.candidates.isEmpty() )
            throw new NoSuchElementException("This response does not contain a coordination candidate.");
        Candidate mostLikelyCandidate = this.candidates.get(0);
        return mostLikelyCandidate.getLocation();
    }

    /**
     * @return the first (and thus best) candidate contained in this geocoding response.
     *
     * @throws NoSuchElementException when response contains no candidates
     */
    public Candidate getRepresentativeCandidate() {
        if( this.candidates.isEmpty() )
            throw new NoSuchElementException("This response does not contain a candidate.");
        return this.candidates.get(0);
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
     * @return true if empty; false otherwise
     */
    public boolean isEmpty() {
        return candidates.isEmpty();
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

        /**
         * Return the Score of the candidate. The higher the score the more confidence into the candidate as the correct
         * representative. Maximum score is 100.0; Minimum 0.0.
         *
         * @return
         */
        public double getScore() {
            return this.score;
        }

        /**
         * Returns the geocode as {@link DefaultTargetCoordinate} according to the spatial reference from the request.
         * Default reference is "EPSG 4326" (.i.e. if not otherwise specified)
         * @return
         */
        public DefaultTargetCoordinate getLocation(){
            return this.location;
        }
    }
}

package com.targomo.client.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import com.targomo.client.api.response.esri.Candidate;
import com.targomo.client.api.response.esri.ErrorDescription;
import com.targomo.client.api.util.POJOUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Response of the ESRI geocoding REST service for a requested address. There are two different possible response types:
 * <ul>
 *   <li>(1) the response was an error message (<code>wasErrorResponse()==true</code>), i.e. error value is set -
 *           see {@link ErrorDescription}</li>
 *   <li>(2) the request was successful (<code>wasErrorResponse()==false</code>) the <code>candidates</code> value is set
 * </ul>
 *
 * <p>
 * Note: It is immutable with a private constructor since it is only meant to be created from the returned json object.<br>
 * Note: This is not the complete response from the REST service - only relevant information is captured, the remainder
 * is discarded.
 * </p>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingResponse implements Iterable<Candidate>{
    private static final ObjectMapper JSON_PARSER   = new ObjectMapper();

    private final List<Candidate> candidates;
    private final ErrorDescription error;
    private final String completeJsonResponse;

    /**
     * private - not used since this is a POJO only created from a json String
     */
    public GeocodingResponse(@JsonProperty("candidates") List<Candidate> candidates, @JsonProperty("error") ErrorDescription error, @JsonProperty("jsonString") String jsonString) {
        this.completeJsonResponse = jsonString;
        this.error = error;
        this.candidates = candidates;
    }

    /**
     * Creates the response from a JSON String
     *
     * @param jsonString to be parsed
     * @return the resulting POJO GeocodingResponse
     */
    public static GeocodingResponse createFromJson(String jsonString) {
        GeocodingResponse ret = null;

        try {
            ret = JSON_PARSER.readValue(jsonString, GeocodingResponse.class);
        } catch (IOException e) {
            throw new TargomoClientRuntimeException(e.getMessage(), e);
        }
        return new GeocodingResponse(ret.candidates, ret.error, jsonString);
    }

    /**
     * @return if an error occurred, the {@link ErrorDescription} is returned
     */
    public ErrorDescription getError() {
        return error;
    }

    /**
     * @return <code>true</code> if error was recorded; <code>false</code> otherwise
     */
    public boolean wasErrorResponse() {
        return error != null;
    }

    /**
     * @return the complete JSON response as String
     */
    public String getCompleteJsonResponseAsString() {
        return completeJsonResponse;
    }

    /**
     * @return the first (and thus best) candidate coordinates contained in this geocoding response.
     *
     * @throws NoSuchElementException when response contains no candidates
     */
    public DefaultTargetCoordinate getRepresentativeGeocodeOfRequest() {
        if( this.candidates == null || this.candidates.isEmpty() )
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
        if( this.candidates == null || this.candidates.isEmpty() )
            throw new NoSuchElementException("This response does not contain a candidate.");
        return this.candidates.get(0);
    }

    /**
     * Pretty prints the {@link GeocodingResponse}, i.e. with indention and line breaks.
     * @return the pretty print String of the response
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
        return this.candidates == null || this.candidates.isEmpty();
    }


}

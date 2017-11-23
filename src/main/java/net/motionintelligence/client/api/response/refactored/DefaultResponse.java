package net.motionintelligence.client.api.response.refactored;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.motionintelligence.client.api.TravelOptions;

/**
 *
 * @param <O> this is the type of the data that ou want to be visible from outside the response
 * @param <I> this is the type of data that Jackson should create and from which you parse the data of type <O>, e.g. usually Map<String,Object> for an object
 */
public abstract class DefaultResponse<O,I> {

    //Json parsed data
    private String code;
    private long requestTimeMillis;
    private String message;
    protected O data;

    //other parameters
    private long parseTimeMillis;
    private long roundTripTimeMillis;
    private TravelOptions travelOptions;

    public void setExtraParameters(TravelOptions travelOptions, long roundTripTimeMillis, long parseTimeMillis) {
        this.travelOptions 	   	 = travelOptions;
        this.roundTripTimeMillis = roundTripTimeMillis;
        this.parseTimeMillis 	 = parseTimeMillis;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("requestTime")
    public void setRequestTimeMillis(long requestTimeMillis) {
        this.requestTimeMillis = requestTimeMillis;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(I data){
        this.data = parseData(data);
    }

    protected abstract O parseData(I jacksonData);

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the requestTimeMillis
     */
    public long getRequestTimeMillis() {
        return requestTimeMillis;
    }

    public String getMessage() {
        return message;
    }

    public O getData() {
        return this.data;
    }

    public long getParseTimeMillis() {
        return parseTimeMillis;
    }

    /**
     * @return the travelOptions
     */
    public TravelOptions getTravelOptions() {
        return travelOptions;
    }

    /**
     * @return the roundTripTimeMillis
     */
    public long getRoundTripTimeMillis() {
        return roundTripTimeMillis;
    }
}

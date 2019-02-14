package com.targomo.client.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientException;

/**
 *
 * @param <O> this is the type of the data that ou want to be visible from outside the response
 * @param <I> this is the type of data that Jackson should create and from which you parse the data of type O
 *           , e.g. usually {@link java.util.Map} from {@link String} to {@link Object} for an object
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

    public static <R extends DefaultResponse> R createGatewayTimeoutResponse(Class<R> clazz) throws TargomoClientException {
        try {
            R gateWayTimeoutResponse = clazz.newInstance();
            gateWayTimeoutResponse.setCode("gateway-time-out");
            gateWayTimeoutResponse.setMessage("");
            gateWayTimeoutResponse.setRequestTimeMillis(-1);
            return gateWayTimeoutResponse;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new TargomoClientException("Response Instantiation failed with error", e);
        }
    }

    public void finishDeserialization(TravelOptions travelOptions, long roundTripTimeMillis, long parseTimeMillis) {
        this.travelOptions 	   	 = travelOptions;
        this.roundTripTimeMillis = roundTripTimeMillis;
        this.parseTimeMillis 	 = parseTimeMillis;
    }

    void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("requestTime")
    void setRequestTimeMillis(long requestTimeMillis) {
        this.requestTimeMillis = requestTimeMillis;
    }

    void setMessage(String message) {
        this.message = message;
    }

    //private so only json parser will use it
    private void setData(I data){
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

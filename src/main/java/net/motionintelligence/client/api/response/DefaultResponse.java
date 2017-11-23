package net.motionintelligence.client.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.motionintelligence.client.api.TravelOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class DefaultResponse<O> {

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

    public void setData(O data) {
        this.data = data;
    }

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
        return data;
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

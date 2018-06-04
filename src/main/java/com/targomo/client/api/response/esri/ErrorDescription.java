package com.targomo.client.api.response.esri;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.api.response.GeocodingResponse;

import java.util.List;

/**
 * The POJO for an error message reponse sent from an ESRI service (see
 * {@link GeocodingResponse} and {@link AuthenticationResponse})
 */
public class ErrorDescription {

    private final Integer code;
    private final String error;
    @JsonProperty("error_description")
    private final String errorDescription;
    private final String message;
    private final List<String> details;

    /**
     * private - not used since this is a POJO only created from a json String
     */
    private ErrorDescription(Integer code, String error, String errorDescription, String message, List<String> details) {
        this.code = code;
        this.error = error;
        this.errorDescription = errorDescription;
        this.message = message;
        this.details = details;
    }

    /**
     * @return the ESRI error code (not the http status code of the response)
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the error name
     */
    public String getError() {
        return error;
    }

    /**
     * @return the error description as String
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * @return the error details
     */
    public List<String> getDetails() {
        return details;
    }
}

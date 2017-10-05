package net.motionintelligence.client.api.response.esri;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * TODO
 */
public class ErrorDescription {

    private final Integer code;
    private final String error;
    @JsonProperty("error_description")
    private final String errorDescription;
    private final String message;
    private final List<String> details;

    private ErrorDescription(Integer code, String error, String errorDescription, String message, List<String> details) {
        this.code = code;
        this.error = error;
        this.errorDescription = errorDescription;
        this.message = message;
        this.details = details;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}

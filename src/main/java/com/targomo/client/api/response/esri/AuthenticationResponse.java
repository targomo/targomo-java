package com.targomo.client.api.response.esri;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The POJO for the response from the ESRI authentication. There are two different possible response types:
 * <ul>
 *   <li>(1) the response was an error message (<code>wasErrorResponse()==true</code>), i.e. error value is set -
 *           see {@link ErrorDescription}</li>
 *   <li>(2) the request was successful (<code>wasErrorResponse()==false</code>) and values for <code>accessToken</code>
 *           and <code>expiresIn</code> are set</li>
 * </ul>
 */
public class AuthenticationResponse {
    private final String accessToken;
    private final Integer expiresIn;
    private final ErrorDescription error;

    /**
     * private - not used since this is a POJO only created from a json String
     */
    @JsonCreator
    public AuthenticationResponse(@JsonProperty("access_token") String accessToken, @JsonProperty("expires_in") Integer expiresIn,
                                      @JsonProperty("error") ErrorDescription error) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.error = error;
    }

    /**
     * @return the retrieved access token (may be null if an error occurred during request)
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @return time validity for token in seconds (may be null if an error occurred during request)
     */
    public Integer getExpiresIn() {
        return expiresIn;
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
}

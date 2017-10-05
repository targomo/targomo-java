package net.motionintelligence.client.api.response.esri;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO
 */
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("expires_in")
    private final Integer expiresIn;
    private final ErrorDescription error;

    private AuthenticationResponse(String accessToken, Integer expiresIn, ErrorDescription error) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.error = error;
    }

    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @return time validity for token in seconds
     */
    public Integer getExpiresIn() {
        return expiresIn;
    }

    public ErrorDescription getError() {
        return error;
    }

    public boolean wasErrorResponse() {
        return error != null;
    }
}

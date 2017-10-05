package net.motionintelligence.client.api.request.esri;

import javax.annotation.Nonnull;

/**
 * TODO
 */
public class ESRIAthenticationDetails {

    private static final Integer MAX_TOKEN_EXPIRATION = 20160; //20160 minutes = 14 days - that's the maximum according to
    // https://developers.arcgis.com/rest/geocode/api-reference/geocoding-authenticate-a-request.htm

    private final String clientID;
    private final String clientSecret;
    private final Integer tokenExpirationInMinutes;

    public ESRIAthenticationDetails(@Nonnull String clientID,@Nonnull String clientSecret) {
        this(clientID,clientSecret,MAX_TOKEN_EXPIRATION);
    }

    public ESRIAthenticationDetails(@Nonnull String clientID,@Nonnull String clientSecret,@Nonnull Integer tokenExpirationInMinutes) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.tokenExpirationInMinutes = tokenExpirationInMinutes;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Integer getTokenExpirationInMinutes() {
        return tokenExpirationInMinutes;
    }
}

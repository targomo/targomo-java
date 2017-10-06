package net.motionintelligence.client.api.request.esri;

import javax.annotation.Nonnull;

/**
 * Immutable Class to specify the user credentials for an ESRI account - this may be required for using
 * {@link net.motionintelligence.client.api.request.GeocodingRequest} (which uses the ESRI service)
 */
public class ESRIAuthenticationDetails {

    private static final Integer MAX_TOKEN_EXPIRATION = 20160; //20160 minutes = 14 days - that's the maximum according
    // to https://developers.arcgis.com/rest/geocode/api-reference/geocoding-authenticate-a-request.htm

    private final String clientID;
    private final String clientSecret;
    private final Integer tokenExpirationInMinutes;

    /**
     * Creates an authentication specification. The value for token expiration is set to the default value of
     * 20160 minutes (=14 days).
     *
     * @see <url>https://developers.arcgis.com/rest/geocode/api-reference/geocoding-authenticate-a-request.htm</url>
     *
     * @param clientID - look into link for details how to get your ESRI client id
     * @param clientSecret - look into link for details how to get your ESRI client secret
     */
    public ESRIAuthenticationDetails(@Nonnull String clientID, @Nonnull String clientSecret) {
        this(clientID,clientSecret,MAX_TOKEN_EXPIRATION);
    }

    /**
     * Creates an authentication specification.
     *
     * @see <url>https://developers.arcgis.com/rest/geocode/api-reference/geocoding-authenticate-a-request.htm</url>
     *
     * @param clientID - look into link for details how to get your ESRI client id
     * @param clientSecret - look into link for details how to get your ESRI client secret
     * @param tokenExpirationInMinutes - determines how long a token will be valid - min: 1 minute; max: 20160 minutes
     *                                 (=14 days)
     */
    public ESRIAuthenticationDetails(@Nonnull String clientID, @Nonnull String clientSecret,
                                     @Nonnull Integer tokenExpirationInMinutes) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        if(tokenExpirationInMinutes < 1)
            throw new IllegalArgumentException("tokenExpirationInMinutes has to be a positive number, but was "
                    + tokenExpirationInMinutes);
        this.tokenExpirationInMinutes = tokenExpirationInMinutes;
    }

    /**
     * @return the specified clientID of this authentication credentials
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * @return the specified clientSecret of this authentication credentials
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * @return the specified tokenExpirationInMinutes of this authentication credentials
     */
    public Integer getTokenExpirationInMinutes() {
        return tokenExpirationInMinutes;
    }
}

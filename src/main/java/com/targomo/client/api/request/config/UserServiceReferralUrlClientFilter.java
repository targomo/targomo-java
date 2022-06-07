package com.targomo.client.api.request.config;

/**
 * Same as the {@link UserServiceEnvClientFilter} but for the refer header
 *
 * @author Mario Freitas
 */
public class UserServiceReferralUrlClientFilter extends RequestHeaderFilter{

    private static final String REFERER = "referer";

    public UserServiceReferralUrlClientFilter(String value) {
        super(REFERER, value);
    }

}

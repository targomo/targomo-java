package com.targomo.client.api.request.config;

/**
 * @author Mario Freitas
 */
public class UserServiceEnvClientFilter extends RequestHeaderFilter{

    private static final String TARGOMO_ENVIRONMENT = "x-targomo-environment";

    public UserServiceEnvClientFilter(String value) {
        super(TARGOMO_ENVIRONMENT, value);
    }

}

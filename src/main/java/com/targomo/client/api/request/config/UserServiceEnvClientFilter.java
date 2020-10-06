package com.targomo.client.api.request.config;

/**
 * ClientRequestFilter implementation for x-targomo-environment headers
 * How to use it: just register this component to one Jersey Client
 *
 * E.g.:
 * Client client = ClientBuilder.newClient();
 * client.register(new UserServiceEnvClientFilter("staging"));
 *
 * @author Mario Freitas
 */
public class UserServiceEnvClientFilter extends RequestHeaderFilter{

    private static final String TARGOMO_ENVIRONMENT = "x-targomo-environment";

    public UserServiceEnvClientFilter(String value) {
        super(TARGOMO_ENVIRONMENT, value);
    }

}

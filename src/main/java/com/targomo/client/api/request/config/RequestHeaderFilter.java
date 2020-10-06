package com.targomo.client.api.request.config;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

/**
 * @author Mario Freitas
 */
public abstract class RequestHeaderFilter implements ClientRequestFilter {

    private String value;
    private String key;

    protected RequestHeaderFilter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add(key, value);
    }
}

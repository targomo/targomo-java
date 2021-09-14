package com.targomo.client.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.targomo.client.api.exception.TargomoClientRuntimeException;

import java.util.stream.Stream;

/**
 * Response codes (of responses with status code 200) from the Targomo API.
 */
public enum ResponseCode {

    OK("ok"),
    COULD_NOT_CONNECT_POLYGON_TO_NETWORK("could-not-connect-polygon-to-network"),
    COULD_NOT_CONNECT_POINT_TO_NETWORK("could-not-connect-point-to-network"),
    NO_ROUTE_FOUND("no-route-found"),
    RAVEL_TIME_EXCEEDED("travel-time-exceeded"),
    UNKNOWN_EXCEPTION("unknown-exception");

    private final String code;

    ResponseCode(String code) {
        this.code = code;
    }

    @JsonCreator
    public static ResponseCode fromString(String code) {
        return code == null ? null : Stream.of(ResponseCode.values())
                .filter(enu -> enu.code.equalsIgnoreCase(code)).findFirst()
                .orElseThrow(() -> new TargomoClientRuntimeException("Unsupported response code"));
    }
}

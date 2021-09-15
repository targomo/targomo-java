package com.targomo.client.api.exception;

import com.targomo.client.api.response.ResponseCode;
import lombok.Getter;

public class ResponseErrorException extends Exception {

    @Getter
    private final ResponseCode errorCode;

    public ResponseErrorException(ResponseCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}

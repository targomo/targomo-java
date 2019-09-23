package com.targomo.client.api.exception;

public class TargomoAuthorizationException extends Exception {

    private static final long serialVersionUID = -9062046076323831446L;

    public TargomoAuthorizationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public TargomoAuthorizationException(String message) {
        super(message);
    }
}

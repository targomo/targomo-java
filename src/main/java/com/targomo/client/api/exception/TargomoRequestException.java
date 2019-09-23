package com.targomo.client.api.exception;

public class TargomoRequestException extends Exception {
    
    private static final long serialVersionUID = -781293582565330921L;

    public TargomoRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public TargomoRequestException(String message) {
        super(message);
    }
}

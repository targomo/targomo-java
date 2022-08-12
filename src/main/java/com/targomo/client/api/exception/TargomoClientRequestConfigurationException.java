package com.targomo.client.api.exception;

/**
 * Exception thrown when there is an error in the definition of a request configuration object
 */
public class TargomoClientRequestConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param error
     * @param e
     */
    public TargomoClientRequestConfigurationException(String error, Exception e) {
        super(error, e);
    }

    public TargomoClientRequestConfigurationException(String error) {
        super(error);
    }

}

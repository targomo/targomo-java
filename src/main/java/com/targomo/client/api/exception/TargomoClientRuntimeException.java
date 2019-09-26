package com.targomo.client.api.exception;

/**
 * Excpetion thrown when there is a runtime exception in a request to a Targomo API
 */
public class TargomoClientRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7860204374116258864L;

	/**
	 * @param message Description of the exception
	 * @param throwable cause of the exception
	 */
	public TargomoClientRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param message Description of the exception
	 */
	public TargomoClientRuntimeException(String message) {
		super(message);
	}
}

package com.targomo.client.api.exception;

public class TargomoClientRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7860204374116258864L;

	public TargomoClientRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TargomoClientRuntimeException(String message) {
		super(message);
	}
}

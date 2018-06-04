package com.targomo.client.api.exception;

public class TargomoClientRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 748077356312331362L;

	public TargomoClientRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TargomoClientRuntimeException(String message) {
		super(message);
	}
}

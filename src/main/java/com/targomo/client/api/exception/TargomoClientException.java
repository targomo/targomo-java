package com.targomo.client.api.exception;

public class TargomoClientException extends Exception {

	private static final long serialVersionUID = 748077356312331362L;

	public TargomoClientException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TargomoClientException(String message) {
		super(message);
	}
}

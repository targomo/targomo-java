package com.targomo.client.api.exception;

public class TargomoClientException extends Exception {

	private static final long serialVersionUID = 748077356312331362L;
	private int httpStatus;

	public TargomoClientException(String message, Throwable throwable) {
		super(message, throwable);
		httpStatus = 0;
	}

	public TargomoClientException(String message) {
		super(message);
		httpStatus = 0;
	}

	public TargomoClientException(String message, Throwable throwable, int httpStatus) {
		super(message, throwable);
		this.httpStatus = httpStatus;
	}

	public TargomoClientException(String message, int httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public boolean hasHttpStatus() {
		return httpStatus != 0;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int status) {
		httpStatus = status;
	}
}

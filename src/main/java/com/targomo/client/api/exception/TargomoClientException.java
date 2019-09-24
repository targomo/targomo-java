package com.targomo.client.api.exception;

import com.targomo.client.Constants;
import lombok.Getter;

public class TargomoClientException extends Exception {

	private static final long serialVersionUID = 748077356312331362L;
	
	@Getter
	private int httpStatusCode;

	public TargomoClientException(String message, Throwable throwable) {
		this(message, throwable, Constants.BAD_GATEWAY_CODE);
	}

	public TargomoClientException(String message) {
		this(message, Constants.BAD_GATEWAY_CODE);
	}

	public TargomoClientException(String message, Throwable throwable, int httpStatusCode) {
		super(message, throwable);
		this.httpStatusCode = httpStatusCode;
	}

	public TargomoClientException(String message, int httpStatusCode) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}

	public boolean hasHttpStatusCode() {
		return httpStatusCode != 0;
	}
}

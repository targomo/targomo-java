package com.targomo.client.api.exception;

import com.targomo.client.Constants;
import lombok.Getter;

/**
 * Excpetion thrown when there is an exception in a request to a Targomo API
 */
public class TargomoClientException extends Exception implements ExceptionCode {

	private static final long serialVersionUID = 748077356312331362L;

	@Getter //By default the error for This is exception is translated as a `BAD_GATEWAY` response
	private int httpStatusCode = Constants.BAD_GATEWAY_CODE;

	/**
	 * Uses default httpStatusCode and will be translated into a `BAD_GATEWAY` response
	 * @param message Description of the exception
	 * @param throwable cause of the exception
	 */
	public TargomoClientException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Uses default httpStatusCode and will be translated into a `BAD_GATEWAY` response
	 * @param message Description of the exception
	 */
	public TargomoClientException(String message) {
		super(message);
	}

	/**
	 * @param message Description of the exception
	 * @param throwable cause of the exception
	 * @param httpStatusCode The exception will be translated into the response type that matches this code.
	 *                       Invalid codes will result in `BAD_GATEWAY` responses
	 */
	public TargomoClientException(String message, Throwable throwable, int httpStatusCode) {
		super(message, throwable);
		this.httpStatusCode = httpStatusCode;
	}

	/**
	 * @param message Description of the exception
	 * @param httpStatusCode The exception will be translated into the response type that matches this code.
	 *                       Invalid codes will result in `BAD_GATEWAY` responses
	 */
	public TargomoClientException(String message, int httpStatusCode) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}

	@Override
	public Integer getCode() {
		return httpStatusCode;
	}
}

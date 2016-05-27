package net.motionintelligence.client.api.exception;

public class Route360ClientRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 748077356312331362L;

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public Route360ClientRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * 
	 * @param message
	 */
	public Route360ClientRuntimeException(String message) {
		super(message);
	}
}

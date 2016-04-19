package net.motion_intelligence.client.api.exception;

public class Route360ClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 748077356312331362L;

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public Route360ClientException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * 
	 * @param message
	 */
	public Route360ClientException(String message) {
		super(message);
	}
}

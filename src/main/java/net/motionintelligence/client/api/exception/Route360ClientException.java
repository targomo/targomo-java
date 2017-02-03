package net.motionintelligence.client.api.exception;

public class Route360ClientException extends Exception {

	private static final long serialVersionUID = 748077356312331362L;

	public Route360ClientException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public Route360ClientException(String message) {
		super(message);
	}
}

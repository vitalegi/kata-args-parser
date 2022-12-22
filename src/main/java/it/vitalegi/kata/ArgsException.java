package it.vitalegi.kata;

public class ArgsException extends RuntimeException {

	public ArgsException() {
		super();
	}

	public ArgsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ArgsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArgsException(String message) {
		super(message);
	}

	public ArgsException(Throwable cause) {
		super(cause);
	}

}

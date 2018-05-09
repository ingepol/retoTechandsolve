package com.mudanza.exception;

public class PreconditionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PreconditionException(String message) {
        super(message);
    }

    public PreconditionException(String message, Throwable cause) {
        super(message, cause);
    }
}

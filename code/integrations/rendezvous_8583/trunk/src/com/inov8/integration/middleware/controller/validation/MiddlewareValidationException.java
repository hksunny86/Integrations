package com.inov8.integration.middleware.controller.validation;

public class MiddlewareValidationException extends RuntimeException {

	private static final long serialVersionUID = -2714364496807971467L;

	public MiddlewareValidationException(String message) {
		super(message);
	}

	public MiddlewareValidationException(Throwable throwable) {
		super(throwable);
	}

	public MiddlewareValidationException(Throwable throwable, String message) {
		super(message, throwable);
	}
}

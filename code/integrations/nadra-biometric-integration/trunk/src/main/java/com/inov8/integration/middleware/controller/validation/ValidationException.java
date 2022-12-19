package com.inov8.integration.middleware.controller.validation;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = -3737393730184351201L;

	public ValidationException(String msg) {
		super(msg);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public synchronized Throwable getCause() {
		return super.getCause();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
}

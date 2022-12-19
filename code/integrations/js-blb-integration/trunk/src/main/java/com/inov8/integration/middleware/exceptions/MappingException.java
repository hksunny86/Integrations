package com.inov8.integration.middleware.exceptions;

public class MappingException extends RuntimeException {

	private static final long serialVersionUID = -2714364496807971467L;

	public MappingException(String message) {
		super(message);
	}

	public MappingException(Throwable throwable) {
		super(throwable);
	}

	public MappingException(Throwable throwable, String message) {
		super(message, throwable);
	}
}

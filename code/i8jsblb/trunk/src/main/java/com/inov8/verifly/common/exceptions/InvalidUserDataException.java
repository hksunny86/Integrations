package com.inov8.verifly.common.exceptions;

public class InvalidUserDataException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4055851352407372149L;

	public InvalidUserDataException(String message) 
	{
		super(message);		
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public InvalidUserDataException(String message, Throwable throwable) 
	{
		super(message, throwable);		
	}
}

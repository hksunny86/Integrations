package com.inov8.verifly.common.exceptions;

public class InvalidPinException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5471249692905328191L;

	public InvalidPinException(String message) 
	{
		super(message);		
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public InvalidPinException(String message, Throwable throwable) 
	{
		super(message, throwable);		
	}

}

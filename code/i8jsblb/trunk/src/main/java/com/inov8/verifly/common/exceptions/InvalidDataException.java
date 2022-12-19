package com.inov8.verifly.common.exceptions;

public class InvalidDataException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8217473961220706161L;

	public InvalidDataException(String message) 
	{
		super(message);		
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public InvalidDataException(String message, Throwable throwable) 
	{
		super(message, throwable);		
	}
}

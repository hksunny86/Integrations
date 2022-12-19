package com.inov8.microbank.common.exception;



public class EmailServiceSendFailureException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3365119199575759988L;

	public EmailServiceSendFailureException( String message )
	{
		super( message );		
	}
	public EmailServiceSendFailureException( String message, Throwable ex )
	{
		super( message,ex );		
	}
	
	public EmailServiceSendFailureException( String message, long errorCode )
	{
		super( message );		
	}
}

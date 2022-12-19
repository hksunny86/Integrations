package com.inov8.microbank.common.exception;



public class ConnectionFailureException extends IntegrationException
{
	public ConnectionFailureException( String message )
	{
		super( message );		
	}
	public ConnectionFailureException( String message, Throwable ex )
	{
		super( message,ex );		
	}
	
	public ConnectionFailureException( String message, long errorCode )
	{
		super( message );		
	}
}

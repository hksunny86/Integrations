package com.inov8.microbank.common.exception;

import com.inov8.framework.common.exception.FrameworkCheckedException;


public class IntegrationException extends FrameworkCheckedException
{
	public String failureReason;
	
	
	public String getFailureReason()
	{
		return failureReason;
	}
	
	public void setFailureReason(String failureReason)
	{
		this.failureReason = failureReason;
	}
	public IntegrationException( String message )
	{
		super( message );		
	}
	public IntegrationException( String message, Throwable ex )
	{
		super( message,ex );		
	}
	
	public IntegrationException( String message, long errorCode )
	{
		super( message );		
	}
	
	
	
	
}

/**
 * 
 */
package com.inov8.microbank.common.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Jawwad Farooq
 *
 */
public class WorkFlowException extends NestedRuntimeException {

	/**
	 * @param message
	 */
	private String errorCode;
	public WorkFlowException(String message)
	{
		super(message);		
	}

	public WorkFlowException(String message, String errorCode) {
		this(message, errorCode, null);
	}

	public WorkFlowException(String message, String errorCode, Throwable throwable) {
		super(message, throwable);
		this.errorCode = errorCode;
	}
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public WorkFlowException(String message, Throwable throwable) 
	{
		super(message, throwable);		
	}

}

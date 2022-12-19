/**
 * 
 */
package com.inov8.microbank.common.exception;

/**
 * @author Jawwad Farooq
 *
 */
public class TransactionAmountZeroException extends Exception 
{
	/**
	 * @param message
	 */
	public TransactionAmountZeroException(String message) 
	{
		super(message);
	}
}

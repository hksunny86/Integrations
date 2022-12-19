/**
 * 
 */
package com.inov8.microbank.mfs.jme.messaging.parser;

/**
 * @author imran.sarwar
 *
 */
@SuppressWarnings("serial")
public class ParsingException extends RuntimeException
{

	/**
	 * 
	 */
	public ParsingException()
	{
		super();
	}

	/**
	 * @param message
	 */
	public ParsingException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ParsingException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ParsingException(Throwable cause)
	{
		super(cause);
	}

}

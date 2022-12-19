package com.inov8.microbank.common.exception;

import com.inov8.framework.common.exception.FrameworkCheckedException;

public class ImplementationNotSupportedException extends FrameworkCheckedException
{
	private static final long serialVersionUID = 1L;

	public ImplementationNotSupportedException(String errorMessage)
	{
		super(errorMessage);
	}
}

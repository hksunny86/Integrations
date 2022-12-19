package com.inov8.microbank.common.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;

public class WorkFlowExceptionTranslator extends FrameworkExceptionTranslator
{

	private final Log logger = LogFactory.getLog(getClass());

	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public WorkFlowExceptionTranslator()
	{
		super();
	}

	/**
	 *
	 * @param exception Exception
	 * @return EMRCheckedException
	 */
	public WorkFlowException translateWorkFlowException(Exception exception, int actionType)
	{

		WorkFlowException workflowException = null;
		// If Use Case specific exception then made it checked and throw to client
		// Note: All use case specific exceptions are runtime exceptions.

		logger.error(exception);
		exception.printStackTrace();
		if (exception instanceof WorkFlowException)
		{
			String message = null;
			try
			{
				message = this.messageSource.getMessage(exception.getMessage(), null, null);
			}
			catch (NoSuchMessageException e)
			{
				if(logger.isDebugEnabled())
					logger.debug("\n\n\n\n***Exception might already have been translated***\n\n\n\n");
				workflowException = new WorkFlowException(exception.getMessage());
				
			}
//			System.out.println(message);
			if(null != message && !"".equals(message))
			{
				workflowException = new WorkFlowException(message); // exception message to be set to the one taken from resource bundle
			}

		}
		else if (exception instanceof NullPointerException)
		{
			String message = this.messageSource.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR, null,
					null);
//			System.out.println(message);
			workflowException = new WorkFlowException(message); // exception message to be set to the one taken from resource bundle
		}
		else
		{
			workflowException = new WorkFlowException(exception.getMessage());
		}
		return workflowException;
	}

}

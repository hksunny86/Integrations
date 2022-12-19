package com.inov8.microbank.server.messaging.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;

public class AuditLogListener implements MessageListener
{
	private FailureLogManager auditLogModule;
	protected static Log logger	= LogFactory.getLog(AuditLogListener.class);
	
	

	public void onMessage(Message message)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside onMessage of AuditLogListener");
		}
		if (message instanceof ObjectMessage)
		{
			
			try
			{
				
				
				Object obj = ((ObjectMessage) message).getObject();
				if (null != obj)
				{
					if (obj instanceof AuditLogModel)
					{
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						baseWrapper.setBasePersistableModel((AuditLogModel)obj);
						auditLogModule.auditLogRequiresNewTransaction(baseWrapper);
					}
				
			     }
			}
			catch (FrameworkCheckedException ex)
			{
				logger.error("Exception occured while insertion in auditLog"+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				throw new RuntimeException(ex.getMessage(), ex);
			}
			catch (Exception ex)
			{
				logger.error("Exception occured while insertion in auditLog"+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				throw new RuntimeException(ex.getMessage(), ex);
			}
		}
		
		
	}



	



	public void setAuditLogModule(FailureLogManager auditLogModule)
	{
		this.auditLogModule = auditLogModule;
	}
	
	

	

}

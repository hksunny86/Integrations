package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.notificationmessagemodule.NotificationMessageManager;



public class NotificationMessageFacadeImpl implements NotificationMessageFacade 
{
		
	private NotificationMessageManager notificationMessageManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public SearchBaseWrapper viewNotificationMessages(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		try
	    {
	      
			this.notificationMessageManager.viewNotificationMessages(searchBaseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return searchBaseWrapper;
	}

	public void setNotificationMessageManager(
			NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	

	

	


}

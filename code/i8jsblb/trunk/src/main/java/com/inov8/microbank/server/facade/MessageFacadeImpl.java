package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class MessageFacadeImpl
    implements MessageFacade
{
  

private NotificationMessageManager notificationMessageManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  public SearchBaseWrapper loadNotificationMessage(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.notificationMessageManager.loadNotificationMessage(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;
}

  public BaseWrapper loadNotificationMessage(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.notificationMessageManager.loadNotificationMessage(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
 }

 return baseWrapper;

}

  public BaseWrapper updateNotificationMessage(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	  try
	    {
	      this.notificationMessageManager.updateNotificationMessage(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
	 }

	 return baseWrapper;
	}
  
  
  
  public void setNotificationMessageManager(NotificationMessageManager
                                            notificationMessageManager)
  {
    this.notificationMessageManager = notificationMessageManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

}

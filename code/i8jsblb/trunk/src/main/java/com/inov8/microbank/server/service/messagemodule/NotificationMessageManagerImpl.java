package com.inov8.microbank.server.service.messagemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.server.dao.messagemodule.NotificationMessageDAO;

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
public class NotificationMessageManagerImpl
    implements NotificationMessageManager
{
  private NotificationMessageDAO notificationMessageDAO;

  public SearchBaseWrapper loadNotificationMessage(SearchBaseWrapper searchBaseWrapper)
 {
   NotificationMessageModel notificationMessageModel = this.
       notificationMessageDAO.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                        getPrimaryKey());
   searchBaseWrapper.setBasePersistableModel(notificationMessageModel);
   return searchBaseWrapper;
 }

 public BaseWrapper loadNotificationMessage(BaseWrapper
                                           baseWrapper)
 {
   NotificationMessageModel notificationMessageModel = this.notificationMessageDAO.
       findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
   baseWrapper.setBasePersistableModel(notificationMessageModel);
   return baseWrapper;
 }

  
 public BaseWrapper updateNotificationMessage(BaseWrapper baseWrapper) throws FrameworkCheckedException 
 {
	 
	 NotificationMessageModel notificationMessageModel=this.notificationMessageDAO.saveOrUpdate((NotificationMessageModel)baseWrapper.getBasePersistableModel());
	 baseWrapper.setBasePersistableModel(notificationMessageModel);
	 return baseWrapper;
	
}

public void setNotificationMessageDAO(NotificationMessageDAO
                                        notificationMessageDAO)
  {
    this.notificationMessageDAO = notificationMessageDAO;
  }

}

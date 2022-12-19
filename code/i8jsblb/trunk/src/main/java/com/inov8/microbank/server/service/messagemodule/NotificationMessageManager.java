package com.inov8.microbank.server.service.messagemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

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
public interface NotificationMessageManager
{

  public SearchBaseWrapper loadNotificationMessage(SearchBaseWrapper   searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadNotificationMessage(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper updateNotificationMessage(BaseWrapper  baseWrapper) throws
  FrameworkCheckedException;



}

package com.inov8.microbank.server.service.notificationmessagemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface NotificationMessageManager {


	public SearchBaseWrapper viewNotificationMessages(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;


}

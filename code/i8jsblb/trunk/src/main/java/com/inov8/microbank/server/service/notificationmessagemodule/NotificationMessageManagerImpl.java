package com.inov8.microbank.server.service.notificationmessagemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.notificationmessagemodule.NotificationMessageListViewModel;
import com.inov8.microbank.server.dao.messagemodule.NotificationMessageDAO;
import com.inov8.microbank.server.dao.messagemodule.NotificationMessageListViewDAO;


public class NotificationMessageManagerImpl implements NotificationMessageManager {

				
		private NotificationMessageDAO notificationMessageDAO;
		private NotificationMessageListViewDAO notificationMessageListViewDAO;
	public SearchBaseWrapper viewNotificationMessages(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		
		
		CustomList<NotificationMessageListViewModel> list = this.notificationMessageListViewDAO
		.findByExample((NotificationMessageListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
					searchBaseWrapper.setCustomList(list);
				
		return searchBaseWrapper;
	}
	public void setNotificationMessageDAO(
			NotificationMessageDAO notificationMessageDAO) {
		this.notificationMessageDAO = notificationMessageDAO;
	}
	public void setNotificationMessageListViewDAO(
			NotificationMessageListViewDAO notificationMessageListViewDAO) {
		this.notificationMessageListViewDAO = notificationMessageListViewDAO;
	}
    

}

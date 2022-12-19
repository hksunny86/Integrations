package com.inov8.microbank.server.dao.messagemodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.notificationmessagemodule.NotificationMessageListViewModel;
import com.inov8.microbank.server.dao.messagemodule.NotificationMessageListViewDAO;

public class NotificationMessageListViewHibernateDAO extends 
             BaseHibernateDAO<NotificationMessageListViewModel, Long, NotificationMessageListViewDAO>
             implements NotificationMessageListViewDAO
{

}

package com.inov8.microbank.server.dao.portal.usermanagementmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementListViewModel;
import com.inov8.microbank.server.dao.portal.usermanagementmodule.UserManagementListViewDAO;

public class UserManagementListViewHibernateDAO extends 
    BaseHibernateDAO<UserManagementListViewModel, Long, UserManagementListViewDAO>
    implements UserManagementListViewDAO
{

}

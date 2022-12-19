package com.inov8.microbank.server.dao.actionlogmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.actionlogmodule.ActionLogListViewModel;
import com.inov8.microbank.server.dao.actionlogmodule.ActionLogListViewDAO;

public class ActionLogListViewHibernateDAO extends BaseHibernateDAO<ActionLogListViewModel,Long,ActionLogListViewDAO>
   implements ActionLogListViewDAO{

}

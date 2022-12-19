package com.inov8.microbank.server.dao.commandmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.commandmodule.CommandListViewModel;
import com.inov8.microbank.server.dao.commandmodule.CommandListViewDAO;

public class CommandListViewHibernateDAO extends BaseHibernateDAO<CommandListViewModel,Long,CommandListViewDAO> 
   implements CommandListViewDAO{

}

package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommandModel;
import com.inov8.microbank.server.dao.mfsmodule.CommandDAO;

public class CommandHibernateDAO 
	extends BaseHibernateDAO<CommandModel, Long, CommandDAO>
	implements CommandDAO
{

}

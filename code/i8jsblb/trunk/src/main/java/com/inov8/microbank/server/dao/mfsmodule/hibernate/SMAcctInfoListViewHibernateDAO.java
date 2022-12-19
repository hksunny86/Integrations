package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.smartmoneymodule.SmAcctInfoListViewModel;
import com.inov8.microbank.server.dao.mfsmodule.SMAcctInfoListViewDAO;

public class SMAcctInfoListViewHibernateDAO 
	extends BaseHibernateDAO<SmAcctInfoListViewModel, Long,SMAcctInfoListViewDAO>
	implements SMAcctInfoListViewDAO
{

}

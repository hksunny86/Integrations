package com.inov8.microbank.server.dao.commissionstakeholdermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.commissionmodule.CommStakeholderListViewModel;
import com.inov8.microbank.server.dao.commissionstakeholdermodule.CommissionStakeholderListViewDAO;

public class CommissionStakeholderListViewHibernateDAO 
	extends BaseHibernateDAO<CommStakeholderListViewModel, Long,CommissionStakeholderListViewDAO>
	implements CommissionStakeholderListViewDAO
{

}

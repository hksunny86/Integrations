package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.transactionmodule.TransactionListViewModel;
import com.inov8.microbank.server.dao.mfsmodule.TransactionListViewDAO;

public class TransactionListViewHibernateDAO 
	extends BaseHibernateDAO<TransactionListViewModel, Long, TransactionListViewDAO>
	implements TransactionListViewDAO
{

}

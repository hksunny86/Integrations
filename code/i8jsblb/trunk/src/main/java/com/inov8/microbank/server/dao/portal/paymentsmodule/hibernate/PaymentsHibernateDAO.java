package com.inov8.microbank.server.dao.portal.paymentsmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.paymentsmodule.PaymentsListViewModel;
import com.inov8.microbank.server.dao.portal.paymentsmodule.PaymentsListViewDAO;




public class PaymentsHibernateDAO extends BaseHibernateDAO<PaymentsListViewModel, Long, PaymentsListViewDAO> implements PaymentsListViewDAO {
	
	public PaymentsHibernateDAO()
	{
		
	}

}

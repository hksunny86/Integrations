package com.inov8.microbank.server.dao.transactionmodule.hibernate;

import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.transactionmodule.SalesSummaryListViewModel;
import com.inov8.microbank.server.dao.transactionmodule.SalesSummaryListViewDAO;

public class SalesSummaryListViewHibernateDAO 
extends BaseHibernateDAO<SalesSummaryListViewModel, Long, SalesSummaryListViewDAO>
implements SalesSummaryListViewDAO
{
	
	public List<SalesSummaryListViewModel> getSalesSummary( BaseWrapper baseWrapper )
	{
		SalesSummaryListViewModel salesSummary = (SalesSummaryListViewModel)baseWrapper.getBasePersistableModel() ;
		String hql = " FROM SalesSummaryListViewModel ssm WHERE ssm.appUserId = ? AND ssm.transactionDate = ? " ;
		
		return this.getHibernateTemplate().find(hql, new Object[]{salesSummary.getAppUserId(),salesSummary.getTransactionDate()}) ;		
	}

}

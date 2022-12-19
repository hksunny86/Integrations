package com.inov8.microbank.server.dao.salesmodule;

import java.util.List;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.salesmodule.SalesTeamComissionViewModel;
import org.hibernate.criterion.Restrictions;

public class SalesTeamCommissionHibernateDAO extends BaseHibernateDAO<SalesTeamComissionViewModel, Long, SalesTeamCommissionDAO> implements SalesTeamCommissionDAO {
	
	public SalesTeamComissionViewModel loadSalesTeamComissionView(String transactionCode){
		SalesTeamComissionViewModel salesTeamComissionViewModel = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" FROM SalesTeamComissionViewModel ");
		queryString.append(" WHERE transactionId = :transactionCode");
		
		Object[] values = { transactionCode };
		String[] paramNames = { "transactionCode" };
		
		try {			
			List<SalesTeamComissionViewModel> list = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
			if(list != null){
				if(list.get(0) != null)
					salesTeamComissionViewModel = list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return salesTeamComissionViewModel;
	}

	@Override
	public CustomList<SalesTeamComissionViewModel> loadSalesTeamCommissionViewByCriteria(String transactionCode)
	{
		return super.findByCriteria(Restrictions.eq("transactionId",transactionCode));
	}

}

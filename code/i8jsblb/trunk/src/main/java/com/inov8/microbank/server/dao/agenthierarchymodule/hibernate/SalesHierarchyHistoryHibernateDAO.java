package com.inov8.microbank.server.dao.agenthierarchymodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyHistoryModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.SalesHierarchyHistoryDAO;

public class SalesHierarchyHistoryHibernateDAO extends BaseHibernateDAO<SalesHierarchyHistoryModel, Long, SalesHierarchyHistoryDAO>
implements SalesHierarchyHistoryDAO
{
	
	@Override
	public List<SalesHierarchyHistoryModel> findSaleUserHistoryByBankUserId(Long bankUserId) throws FrameworkCheckedException
	{
		List<SalesHierarchyHistoryModel> salesHierarchyHistoryModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SalesHierarchyHistoryModel.class );
        detachedCriteria.add(Restrictions.eq("relationOldBankUserAppUserModel.appUserId", bankUserId));
        detachedCriteria.addOrder(Order.desc("createdOn"));
        salesHierarchyHistoryModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
        
        return salesHierarchyHistoryModelList;
	}
		
}

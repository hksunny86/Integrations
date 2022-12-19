package com.inov8.microbank.server.dao.agenthierarchymodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.SalesHierarchyDAO;

public class SalesHierarchyHibernateDAO extends BaseHibernateDAO<SalesHierarchyModel, Long, SalesHierarchyDAO>
implements SalesHierarchyDAO
{
	
	
	public List<SalesHierarchyModel> findSaleUsersByUltimateParentSaleUserId(Long ultimateParentSaleUserId) throws FrameworkCheckedException
	{
		List<SalesHierarchyModel> salesHierarchyModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SalesHierarchyModel.class );
        detachedCriteria.add(Restrictions.eq("relationUltimateParentBankUserAppUserModel.appUserId", ultimateParentSaleUserId));
        detachedCriteria.addOrder(Order.asc("salesHierarchyId"));
        salesHierarchyModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return salesHierarchyModelList;
	}
	
	public SalesHierarchyModel findSaleUserByBankUserId(Long bankUserId) throws FrameworkCheckedException
	{
		List<SalesHierarchyModel> salesHierarchyModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SalesHierarchyModel.class );
        detachedCriteria.add(Restrictions.eq("relationBankUserAppUserModel.appUserId", bankUserId));
        salesHierarchyModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
        if(salesHierarchyModelList.size()>0)
        	return salesHierarchyModelList.get(0);
        return null;
	}
	
	public List<SalesHierarchyModel> findUltimateSaleUsers() throws FrameworkCheckedException
	{
		List<SalesHierarchyModel> salesHierarchyModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SalesHierarchyModel.class );
        detachedCriteria.add(Restrictions.isNull("relationUltimateParentBankUserAppUserModel.appUserId"));
        salesHierarchyModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return salesHierarchyModelList;
	}
}

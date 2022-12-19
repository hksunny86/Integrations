package com.inov8.microbank.server.dao.portal.ola.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 20, 2013 7:40:48 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */

public class OlaCustomerAccountTypeHibernateDao extends BaseHibernateDAO<OlaCustomerAccountTypeModel, Long, OlaCustomerAccountTypeDao> implements OlaCustomerAccountTypeDao
{

	@SuppressWarnings("unchecked")
	@Override
	public List<OlaCustomerAccountTypeModel> loadCustomerACTypes(Long[] typesId) throws FrameworkCheckedException {
		DetachedCriteria criteria = DetachedCriteria.forClass(OlaCustomerAccountTypeModel.class);
		criteria.add(Restrictions.in("customerAccountTypeId", typesId));
		List<OlaCustomerAccountTypeModel> list = this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OlaCustomerAccountTypeModel> searchParentOlaCustomerAccountTypes(Long... customerAccountTypeIdsToExclude)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(OlaCustomerAccountTypeModel.class);
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		criteria.add(Restrictions.eq("isCustomerAccountType", Boolean.FALSE));
		criteria.add(Restrictions.isNull("parentOlaCustomerAccountTypeModel"));
		criteria.add(Restrictions.not(Restrictions.in("customerAccountTypeId", customerAccountTypeIdsToExclude)));
		List<OlaCustomerAccountTypeModel> list = this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OlaCustomerAccountTypeModel> searchSubtypesAndLimits(Long parentAccountTypeId)
	{
		List<OlaCustomerAccountTypeModel> list = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(OlaCustomerAccountTypeModel.class);
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		criteria.add(Restrictions.eq("parentOlaCustomerAccountTypeModel.customerAccountTypeId", parentAccountTypeId));
		criteria.setFetchMode("customerAccountTypeIdLimitModelList", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		list = this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}

	@Override
	public List<OlaCustomerAccountTypeModel> loadHandlerACTypes()
			throws FrameworkCheckedException {
		DetachedCriteria criteria = DetachedCriteria.forClass(OlaCustomerAccountTypeModel.class);
		criteria.add(Restrictions.isNotNull("parentOlaCustomerAccountTypeModel.customerAccountTypeId"));
		criteria.add(Restrictions.eq("isCustomerAccountType", false));
		return this.getHibernateTemplate().findByCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OlaCustomerAccountTypeModel> getAllActiveCustomerAccountTypes()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(OlaCustomerAccountTypeModel.class);
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		criteria.add(Restrictions.eq("isCustomerAccountType", Boolean.FALSE));
		List<OlaCustomerAccountTypeModel> list = this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}

	@Override
	public List<OlaCustomerAccountTypeModel> getParentOlaCustomerAccountTypes(
			Long[] accountTypeIds) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OlaCustomerAccountTypeModel.class);
		criteria.add(Restrictions.in("customerAccountTypeId",accountTypeIds));
		List<OlaCustomerAccountTypeModel> list = this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
}

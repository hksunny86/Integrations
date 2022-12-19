package com.inov8.microbank.server.dao.productmodule.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductLimitRuleModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.dao.productmodule.ProductLimitRuleDAO;

/**
 * <p>
 * Title: Microbank
 * </p>
 * 
 * <p>
 * Description: Backend Application for POS terminals.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Inov8 Limited
 * </p>
 * 
 * @author Muhammad Atif Hussain
 * @version 1.0
 */

public class ProductLimitRuleHibernateDAO extends
		BaseHibernateDAO<ProductLimitRuleModel, Long, ProductLimitRuleDAO>
		implements ProductLimitRuleDAO {

	/**
	 * @author AtifHu
	 */
	@Override
	public void createOrUpdateProductLimitRule(
			Collection<ProductLimitRuleModel> productLimitRuleModelList)
			throws FrameworkCheckedException {
		getHibernateTemplate().saveOrUpdateAll(productLimitRuleModelList);
		getHibernateTemplate().flush();
	}

	/**
	 * @author AtifHu
	 */
	@Override
	public Collection<ProductLimitRuleModel> loadProductLimitRules(
			long productId) throws FrameworkCheckedException {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(ProductLimitRuleModel.class);
		criteria.add(Restrictions.eq("productIdModel.productId", productId));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<ProductLimitRuleModel> loadProductLimitRulesByServiceOpId(String hql, Object[] parameterList) throws FrameworkCheckedException {
		List<ProductLimitRuleModel> list = getHibernateTemplate().find(hql,parameterList);
		return list;
	}

	@Override
	public void deleteProductLimitRule(long productId)
			throws FrameworkCheckedException {
		String hql = "DELETE FROM ProductLimitRuleModel"
				+ " WHERE productIdModel.productId =?";
		this.getHibernateTemplate().bulkUpdate(hql,productId);
	}
}

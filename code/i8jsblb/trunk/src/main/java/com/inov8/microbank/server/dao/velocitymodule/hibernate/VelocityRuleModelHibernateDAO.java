package com.inov8.microbank.server.dao.velocitymodule.hibernate;
/*
 * Author : Hassan javaid
 * Date :   9/2/2014
 * 
 *
 */
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.VelocityRuleModel;
import com.inov8.microbank.server.dao.velocitymodule.VelocityRuleModelDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class VelocityRuleModelHibernateDAO extends BaseHibernateDAO<VelocityRuleModel, Long, VelocityRuleModelDAO> implements VelocityRuleModelDAO {

	private JdbcTemplate jdbcTemplate;

	@Override
	public List<VelocityRuleModel> findByCriteria(VelocityRuleModel velocityRuleModel) {
	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(VelocityRuleModel.class);
		
		if(null == velocityRuleModel.getProductId())
			detachedCriteria.add(Restrictions.isNull("relationProductIdProductModel.productId"));
		else
			detachedCriteria.add(Restrictions.eq("relationProductIdProductModel.productId", velocityRuleModel.getProductId()));
		
		if(null == velocityRuleModel.getDeviceTypeId())
			detachedCriteria.add(Restrictions.isNull("relationDeviceTypeIdDeviceTypeModel.deviceTypeId"));
		else
			detachedCriteria.add(Restrictions.eq("relationDeviceTypeIdDeviceTypeModel.deviceTypeId", velocityRuleModel.getDeviceTypeId()));
		
		if(null == velocityRuleModel.getSegmentId())
			detachedCriteria.add(Restrictions.isNull("relationSegmentIdSegmentModel.segmentId"));
		else
			detachedCriteria.add(Restrictions.eq("relationSegmentIdSegmentModel.segmentId", velocityRuleModel.getSegmentId()));
		
		if(null == velocityRuleModel.getDistributorId())
			detachedCriteria.add(Restrictions.isNull("relationDistributorIdDistributorModel.distributorId"));
		else
			detachedCriteria.add(Restrictions.eq("relationDistributorIdDistributorModel.distributorId", velocityRuleModel.getDistributorId()));
		
		if(null == velocityRuleModel.getDistributorLevelId())
			detachedCriteria.add(Restrictions.isNull("relationDistributorLevelIdDistributorLevelModel.distributorLevelId"));
		else
			detachedCriteria.add(Restrictions.eq("relationDistributorLevelIdDistributorLevelModel.distributorLevelId", velocityRuleModel.getDistributorLevelId()));
		
		
		if(null == velocityRuleModel.getLimitTypeId())
			detachedCriteria.add(Restrictions.isNull("relationLimitTypeIdLimitTypeModel.limitTypeId"));
		else
			detachedCriteria.add(Restrictions.eq("relationLimitTypeIdLimitTypeModel.limitTypeId", velocityRuleModel.getLimitTypeId()));

		//Account Type Limit
		if(null == velocityRuleModel.getCustomerAccountTypeId())
			detachedCriteria.add(Restrictions.isNull("relationCustomerAccountTypeIdOlaCustomerAccountTypeModel.customerAccountTypeId"));
		else
			detachedCriteria.add(Restrictions.eq("relationCustomerAccountTypeIdOlaCustomerAccountTypeModel.customerAccountTypeId",
					velocityRuleModel.getCustomerAccountTypeId()));

		detachedCriteria.add(Restrictions.eq("isActive", velocityRuleModel.getIsActive()));

			return getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	@Override
	public String update(VelocityRuleModel velocityRuleModel) {
		Long velocityRuleId = velocityRuleModel.getVelocityRuleId();
		String query= "UPDATE VELOCITY_RULE SET IS_ACTIVE = 0 WHERE VELOCITY_RULE_ID = " +velocityRuleId;
		logger.info("Query to Update velocity rule table :: " + query.toString() );

		String result = String.valueOf(jdbcTemplate.update(query));

//		getHibernateTemplate().update(velocityRuleModel);

		return result;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}
}

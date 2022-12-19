package com.inov8.microbank.server.dao.velocitymodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.velocitymodule.VelocityRuleViewModel;
import com.inov8.microbank.server.dao.velocitymodule.VelocityRuleViewDAO;

public class VelocityRuleViewHibernateDAO extends BaseHibernateDAO<VelocityRuleViewModel, Long, VelocityRuleViewDAO> implements VelocityRuleViewDAO {
	
	public List<VelocityRuleViewModel>  loadVelocityRules(VelocityRuleViewModel velocityRuleViewModel) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( VelocityRuleViewModel.class );
		
		if(null != velocityRuleViewModel.getProductId()){
			if(velocityRuleViewModel.getProductId().longValue() == -1){
				detachedCriteria.add(Restrictions.isNull("productId"));
			}else{
				detachedCriteria.add(Restrictions.or(
						Restrictions.eq("productId", velocityRuleViewModel.getProductId()),
						Restrictions.isNull("productId")));
			}
		}
		
		if(null != velocityRuleViewModel.getDeviceTypeId()){
			if(velocityRuleViewModel.getDeviceTypeId().longValue() == -1){
				detachedCriteria.add(Restrictions.isNull("deviceTypeId"));
			}else {
				detachedCriteria.add(Restrictions.or(
						Restrictions.eq("deviceTypeId", velocityRuleViewModel.getDeviceTypeId()),
						Restrictions.isNull("deviceTypeId")));
			}
		}

		if(null != velocityRuleViewModel.getSegmentId()){
			if(velocityRuleViewModel.getSegmentId().longValue() == -1){
				detachedCriteria.add(Restrictions.isNull("segmentId"));
			}else {
				detachedCriteria.add(Restrictions.or(
						Restrictions.eq("segmentId", velocityRuleViewModel.getSegmentId()),
						Restrictions.isNull("segmentId")));
			}
		}

		if(null != velocityRuleViewModel.getDistributorId()){
			if(velocityRuleViewModel.getDistributorId().longValue() == -1){
				detachedCriteria.add(Restrictions.isNull("distributorId"));
			}else {
				detachedCriteria.add(Restrictions.or(
						Restrictions.eq("distributorId", velocityRuleViewModel.getDistributorId()),
						Restrictions.isNull("distributorId")));
			}
		}
		
		if(null != velocityRuleViewModel.getAgentType()){
			if(velocityRuleViewModel.getAgentType().longValue() == -1) {
				detachedCriteria.add(Restrictions.isNull("agentType"));
			}else{
				detachedCriteria.add(Restrictions.or(
						Restrictions.eq("agentType", velocityRuleViewModel.getAgentType()),
						Restrictions.isNull("agentType")));
			}
		}
		//Account Type Limit
		if(null != velocityRuleViewModel.getCustomerAccountTypeId()){
			if(velocityRuleViewModel.getCustomerAccountTypeId().longValue() == -1) {
				detachedCriteria.add(Restrictions.isNull("customerAccountTypeId"));
			}else{
				detachedCriteria.add(Restrictions.or(
						Restrictions.eq("customerAccountTypeId", velocityRuleViewModel.getCustomerAccountTypeId()),
						Restrictions.isNull("customerAccountTypeId")));
			}
		}

        if(null != velocityRuleViewModel.getCustomerId()){
            if(velocityRuleViewModel.getCustomerId().longValue() == -1) {
                detachedCriteria.add(Restrictions.isNull("customerId"));
            }else{
                detachedCriteria.add(Restrictions.or(
                        Restrictions.eq("customerId", velocityRuleViewModel.getCustomerId()),
                        Restrictions.isNull("customerId")));
            }
        }
		detachedCriteria.add(Restrictions.eq("isActive", Boolean.TRUE));

		List<VelocityRuleViewModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
	
		return list;
		
	}
}
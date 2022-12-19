package com.inov8.microbank.server.dao.operatinghoursmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.OperatingHoursRuleModel;


import com.inov8.microbank.server.dao.operatinghoursmodule.OperatingHoursRuleModelDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class OperatingHoursRuleModelHibernateDAO extends BaseHibernateDAO<OperatingHoursRuleModel,Long, OperatingHoursRuleModelDAO> implements OperatingHoursRuleModelDAO{
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<OperatingHoursRuleModel> findByCriteria(OperatingHoursRuleModel operatingHoursRuleModel) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OperatingHoursRuleModel.class);

        if(null == operatingHoursRuleModel.getProductId())
            detachedCriteria.add(Restrictions.isNull("relationProductIdProductModel.productId"));
        else
            detachedCriteria.add(Restrictions.eq("relationProductIdProductModel.productId", operatingHoursRuleModel.getProductId()));

        if(null == operatingHoursRuleModel.getDeviceTypeId())
            detachedCriteria.add(Restrictions.isNull("relationDeviceTypeIdDeviceTypeModel.deviceTypeId"));
        else
            detachedCriteria.add(Restrictions.eq("relationDeviceTypeIdDeviceTypeModel.deviceTypeId", operatingHoursRuleModel.getDeviceTypeId()));

        if(null == operatingHoursRuleModel.getSegmentId())
            detachedCriteria.add(Restrictions.isNull("relationSegmentIdSegmentModel.segmentId"));
        else
            detachedCriteria.add(Restrictions.eq("relationSegmentIdSegmentModel.segmentId", operatingHoursRuleModel.getSegmentId()));

        if(null == operatingHoursRuleModel.getDistributorId())
            detachedCriteria.add(Restrictions.isNull("relationDistributorIdDistributorModel.distributorId"));
        else
            detachedCriteria.add(Restrictions.eq("relationDistributorIdDistributorModel.distributorId", operatingHoursRuleModel.getDistributorId()));

        if(null == operatingHoursRuleModel.getDistributorLevelId())
            detachedCriteria.add(Restrictions.isNull("relationDistributorLevelIdDistributorLevelModel.distributorLevelId"));
        else
            detachedCriteria.add(Restrictions.eq("relationDistributorLevelIdDistributorLevelModel.distributorLevelId", operatingHoursRuleModel.getDistributorLevelId()));


        /*if(null == operatingHoursRuleModel.getLimitTypeId())
            detachedCriteria.add(Restrictions.isNull("relationLimitTypeIdLimitTypeModel.limitTypeId"));
        else
            detachedCriteria.add(Restrictions.eq("relationLimitTypeIdLimitTypeModel.limitTypeId", velocityRuleModel.getLimitTypeId()));
*/
        //Account Type Limit
        if(null == operatingHoursRuleModel.getCustomerAccountTypeId())
            detachedCriteria.add(Restrictions.isNull("relationCustomerAccountTypeIdOlaCustomerAccountTypeModel.customerAccountTypeId"));
        else
            detachedCriteria.add(Restrictions.eq("relationCustomerAccountTypeIdOlaCustomerAccountTypeModel.customerAccountTypeId",
                    operatingHoursRuleModel.getCustomerAccountTypeId()));

        detachedCriteria.add(Restrictions.eq("isActive", operatingHoursRuleModel.getIsActive()));

        return getHibernateTemplate().findByCriteria(detachedCriteria);
    }

    @Override
    public List<OperatingHoursRuleModel> loadOperatingHoursRules(OperatingHoursRuleModel operatingHoursRuleModel) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( OperatingHoursRuleModel.class );

        if(null != operatingHoursRuleModel.getProductId()){
            if(operatingHoursRuleModel.getProductId().longValue() == -1){
                detachedCriteria.add(Restrictions.isNull("relationProductIdProductModel.productId"));
            }else{
                detachedCriteria.add(Restrictions.or(
                        Restrictions.eq("relationProductIdProductModel.productId", operatingHoursRuleModel.getProductId()),
                        Restrictions.isNull("relationProductIdProductModel.productId")));
            }
        }

        /*f(null != operatingHoursRuleModel.getDeviceTypeId()){
            if(operatingHoursRuleModel.getDeviceTypeId().longValue() == -1){
                detachedCriteria.add(Restrictions.isNull("deviceTypeId"));
            }else {
                detachedCriteria.add(Restrictions.or(
                        Restrictions.eq("deviceTypeId", operatingHoursRuleModel.getDeviceTypeId()),
                        Restrictions.isNull("deviceTypeId")));
            }
        }

        if(null != operatingHoursRuleModel.getSegmentId()){
            if(operatingHoursRuleModel.getSegmentId().longValue() == -1){
                detachedCriteria.add(Restrictions.isNull("segmentId"));
            }else {
                detachedCriteria.add(Restrictions.or(
                        Restrictions.eq("segmentId", operatingHoursRuleModel.getSegmentId()),
                        Restrictions.isNull("segmentId")));
            }
        }

        if(null != operatingHoursRuleModel.getDistributorId()){
            if(operatingHoursRuleModel.getDistributorId().longValue() == -1){
                detachedCriteria.add(Restrictions.isNull("distributorId"));
            }else {
                detachedCriteria.add(Restrictions.or(
                        Restrictions.eq("distributorId", operatingHoursRuleModel.getDistributorId()),
                        Restrictions.isNull("distributorId")));
            }
        }*/

/*        if(null != operatingHoursRuleModel.getAgentType()){
            if(velocityRuleViewModel.getAgentType().longValue() == -1) {
                detachedCriteria.add(Restrictions.isNull("agentType"));
            }else{
                detachedCriteria.add(Restrictions.or(
                        Restrictions.eq("agentType", velocityRuleViewModel.getAgentType()),
                        Restrictions.isNull("agentType")));
            }
        }*/
        //Account Type Limit
       /* if(null != operatingHoursRuleModel.getCustomerAccountTypeId()){
            if(operatingHoursRuleModel.getCustomerAccountTypeId().longValue() == -1) {
                detachedCriteria.add(Restrictions.isNull("customerAccountTypeId"));
            }else{
                detachedCriteria.add(Restrictions.or(
                        Restrictions.eq("customerAccountTypeId", operatingHoursRuleModel.getCustomerAccountTypeId()),
                        Restrictions.isNull("customerAccountTypeId")));
            }
        }*/

        /*if(null != operatingHoursRuleModel.getCustomerId()){
            if(velocityRuleViewModel.getCustomerId().longValue() == -1) {
                detachedCriteria.add(Restrictions.isNull("customerId"));
            }else{
                detachedCriteria.add(Restrictions.or(
                        Restrictions.eq("customerId", velocityRuleViewModel.getCustomerId()),
                        Restrictions.isNull("customerId")));
            }
        }*/
        detachedCriteria.add(Restrictions.eq("isActive", Boolean.TRUE));

        List<OperatingHoursRuleModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        return  list;
    }

    @Override
    public String update(OperatingHoursRuleModel operatingHoursRuleModel) {
        Long operatingHourRuleId = operatingHoursRuleModel.getOperatingHoursRuleId();
        String query= "UPDATE OPERATING_HOURS_RULE SET IS_ACTIVE = 0 WHERE OPERATING_HOURS_RULE_ID = " +operatingHourRuleId;
        logger.info("Query to Update operating hours rule table :: " + query.toString() );

        String result = String.valueOf(jdbcTemplate.update(query));
        return  result;

    }
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }
}

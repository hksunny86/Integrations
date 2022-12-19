package com.inov8.ola.server.dao.limit.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.ola.server.dao.limit.LimitRuleDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class LimitRuleHibernateDAO  extends BaseHibernateDAO<LimitRuleModel, Long, LimitRuleDAO> implements LimitRuleDAO  {

	private JdbcTemplate jdbcTemplate;
	
	public boolean isLimitApplicable(LimitRuleModel limitRuleModel) {
		boolean result = true;
		logger.info("The Start of [isLimitApplicable()] for Product " + limitRuleModel.getProductId() + " at the time :: "+ new Date());
		StringBuilder stringBuilder = new StringBuilder(500);
		stringBuilder.append("SELECT * FROM LIMIT_RULE WHERE IS_ACTIVE = " + 1);

		if(null != limitRuleModel.getProductId())
			stringBuilder.append(" AND (PRODUCT_ID IS NULL OR PRODUCT_ID = ").append(limitRuleModel.getProductId()).append(")");
		
		if(null != limitRuleModel.getSegmentId())
			stringBuilder.append(" AND (SEGMENT_ID IS NULL OR SEGMENT_ID = ").append(limitRuleModel.getSegmentId()).append(")");

		if(null != limitRuleModel.getAccountTypeId())
			stringBuilder.append(" AND (ACCOUNT_TYPE_ID IS NULL OR ACCOUNT_TYPE_ID = ").append(limitRuleModel.getAccountTypeId()).append(")");

		logger.info("Query for LimitRuleModel :: " + stringBuilder.toString());

		List<LimitRuleModel> list = (List<LimitRuleModel>) jdbcTemplate.query(stringBuilder.toString(),new LimitRuleModel());
		if(list != null && list.size() > 0){
			result = false;
		}
		return result;
	}

	@Override
	public int findByCriteria(LimitRuleModel limitRuleModel) {

		logger.info("The Start of [findByCriteria()] for Product " + limitRuleModel.getProductId() + " at the time :: "+ new Date());
		StringBuilder stringBuilder = new StringBuilder(500);
		stringBuilder.append("SELECT COUNT(*) FROM LIMIT_RULE WHERE LIMIT_RULE_ID IS NOT NULL ");

		if(null != limitRuleModel.getProductId())
			stringBuilder.append(" AND PRODUCT_ID = ").append(limitRuleModel.getProductId());
		else
			stringBuilder.append(" AND PRODUCT_ID IS NULL");

		if(null != limitRuleModel.getSegmentId())
			stringBuilder.append(" AND SEGMENT_ID = ").append(limitRuleModel.getSegmentId());
		else
			stringBuilder.append(" AND SEGMENT_ID IS NULL ");

		if(null != limitRuleModel.getAccountTypeId())
			stringBuilder.append(" AND ACCOUNT_TYPE_ID = ").append(limitRuleModel.getAccountTypeId());
		else
			stringBuilder.append(" AND ACCOUNT_TYPE_ID IS NULL ");

		logger.info("Query for LimitRuleModel.findByCriteria() :: " + stringBuilder.toString());

		int size = (int) jdbcTemplate.queryForInt(stringBuilder.toString());

		logger.info("Result Found for [isLimitApplicable()] for Product " + limitRuleModel.getProductId() + " at the time :: "+ new Date());

		return size;
	}

	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
}

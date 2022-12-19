package com.inov8.ola.server.dao.limit.hibernate;

/** 	
 * @author 					Usman Ashraf
 * Project Name: 			OLA
 * Creation Date: 			April 2012  			
 * Description:				
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.ola.server.dao.limit.LimitDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class LimitHibernateDAO  extends
BaseHibernateDAO<LimitModel, Long, LimitDAO>
implements LimitDAO  {

	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	public List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId)throws FrameworkCheckedException{
		logger.info("Start of LimitHibernateDAO.getLimitsByCustomerAccountType() at Time :: " + new Date());
		StringBuilder stringBuilder = new StringBuilder(400);
		stringBuilder.append("SELECT * FROM LIMIT ").append("WHERE ");
		stringBuilder.append("CUSTOMER_ACCOUNT_TYPE_ID = ").append(customerAccountTypeId);
		logger.info("Query for LimitHibernateDAO.getLimitByTransactionType() :: " + stringBuilder.toString());
		List<LimitModel> resultList = (List<LimitModel>) jdbcTemplate.query(stringBuilder.toString(),new LimitModel());
		logger.info("End of LimitHibernateDAO.getLimitsByCustomerAccountType() at Time :: " + new Date());
		return resultList;
	}

	@Override
	public LimitModel getLimitByTransactionType(Long transactionTypeId, Long limitTypeId,Long customerAccountTypeId) throws FrameworkCheckedException {
		logger.info("Start of LimitHibernateDAO.getLimitByTransactionType() at Time :: " + new Date());
		StringBuilder stringBuilder = new StringBuilder(400);
		stringBuilder.append("SELECT * FROM LIMIT ").append("WHERE ");
		stringBuilder.append("TRANSACTION_TYPE_ID = ").append(transactionTypeId);
		stringBuilder.append(" AND ").append("LIMIT_TYPE_ID = ").append(limitTypeId);
		stringBuilder.append(" AND ").append("CUSTOMER_ACCOUNT_TYPE_ID = ").append(limitTypeId);
		logger.info("Query for LimitHibernateDAO.getLimitByTransactionType() :: " + stringBuilder.toString());
		LimitModel model = (LimitModel) jdbcTemplate.queryForObject(stringBuilder.toString(),new LimitModel());
		logger.info("End of LimitHibernateDAO.getLimitByTransactionType() at Time :: " + new Date());
		return model;
	}

	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

}

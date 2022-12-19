package com.inov8.ola.server.dao.limit.hibernate;

/** 	
 * @author 					Usman Ashraf
 * Project Name: 			OLA
 * Creation Date: 			April 2012  			
 * Description:				
 */


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.BlinkDefaultLimitModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.ola.server.dao.limit.BlinkDefaultLimitDAO;
import com.inov8.ola.server.dao.limit.LimitDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;


public class BlinkDefaultLimitHibernateDAO extends
BaseHibernateDAO<BlinkDefaultLimitModel, Long, BlinkDefaultLimitDAO>
implements BlinkDefaultLimitDAO {

	private JdbcTemplate jdbcTemplate;
	


	@Override
	public BlinkDefaultLimitModel getLimitByTransactionType(Long customerAccountTypeId, Long limitTypeId,Long transactionTypeId) throws FrameworkCheckedException {
		logger.info("Start of BlinkDefaultLimitHibernateDAO.getLimitByTransactionType() at Time :: " + new Date());
		StringBuilder stringBuilder = new StringBuilder(400);
		stringBuilder.append("SELECT * FROM BLINK_DEFAULT_LIMIT ").append("WHERE ");
		stringBuilder.append("TRANSACTION_TYPE_ID = ").append(transactionTypeId);
		stringBuilder.append(" AND ").append("LIMIT_TYPE_ID = ").append(limitTypeId);
		stringBuilder.append(" AND ").append("CUSTOMER_ACCOUNT_TYPE_ID = ").append(customerAccountTypeId);
		logger.info("Query for BlinkDefaultLimitHibernateDAO.getLimitByTransactionType() :: " + stringBuilder.toString());
		BlinkDefaultLimitModel model = (BlinkDefaultLimitModel) jdbcTemplate.queryForObject(stringBuilder.toString(),new BlinkDefaultLimitModel());
		logger.info("End of BinkDefaultLimitHibernateDAO.getLimitByTransactionType() at Time :: " + new Date());
		return model;
	}

	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

}

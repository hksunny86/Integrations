package com.inov8.microbank.server.dao.messagingmodule.hibernate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.MiddlewareRetryAdviceModel;
import com.inov8.microbank.server.dao.messagingmodule.MiddlewareRetryAdviceDAO;

public class MiddlewareRetryAdviceHibernateDAO extends BaseHibernateDAO<MiddlewareRetryAdviceModel, Long, MiddlewareRetryAdviceDAO> implements MiddlewareRetryAdviceDAO
{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public void updateRetryAdviceReportStatus(Long trxCodeId) throws FrameworkCheckedException {
		String query = "update middleware_retry_advice_report set status = ? where transaction_code_id=? and middleware_retry_adv_rprt_id = (select max( middleware_retry_adv_rprt_id) from middleware_retry_advice_report where transaction_code_id=?)";
		Object[] args = {"Successful", trxCodeId, trxCodeId};
		try{	
			jdbcTemplate.update(query, args);
		}catch(Exception ex){
			throw new FrameworkCheckedException(ex.getMessage(),ex);
		}
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}

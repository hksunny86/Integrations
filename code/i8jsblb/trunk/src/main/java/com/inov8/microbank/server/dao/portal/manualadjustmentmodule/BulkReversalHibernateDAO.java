package com.inov8.microbank.server.dao.portal.manualadjustmentmodule;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class BulkReversalHibernateDAO
extends BaseHibernateDAO<BulkAutoReversalModel, Long, BulkReversalDAO>
implements BulkReversalDAO{
	
	private final static Log logger = LogFactory.getLog(BulkReversalHibernateDAO.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
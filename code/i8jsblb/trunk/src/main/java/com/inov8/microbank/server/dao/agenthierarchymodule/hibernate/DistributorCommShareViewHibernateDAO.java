package com.inov8.microbank.server.dao.agenthierarchymodule.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommShareViewModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.DistributorCommShareViewDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Rashid Mahmood
 * @version 1.0
 */

public class DistributorCommShareViewHibernateDAO
    extends BaseHibernateDAO<DistributorCommShareViewModel, Long, DistributorCommShareViewDAO>
    implements DistributorCommShareViewDAO
{
	private JdbcTemplate jdbcTemplate;
	
	public List<DistributorCommShareViewModel> loadDistributorCommissionSharesList(DistributorCommShareViewModel commShareViewModel){
		String sql = "SELECT * FROM DISTRIBUTOR_COM_SHARE_VIEW WHERE APP_USER_ID = " + commShareViewModel.getAppUserId();
		if(commShareViewModel.getProductId() != null)
			sql += " AND PRODUCT_ID = " + commShareViewModel.getProductId();

		logger.info("Loading DistributorCommShares with SQL: " + sql);
		List<DistributorCommShareViewModel> list = (List<DistributorCommShareViewModel>) jdbcTemplate.query(sql,new DistributorCommShareViewModel());
		logger.info("Record Fetched DistributorCommShareViewModel.loadDistributorCommissionSharesList() at Time :: " + new Date());
		return list;
	}

	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
}

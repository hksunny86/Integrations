package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionRateDefaultModel;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateDefaultDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CommissionRateDefaultHibernateDAO extends BaseHibernateDAO<CommissionRateDefaultModel, Long, CommissionRateDefaultDAO>
		implements CommissionRateDefaultDAO {

	private JdbcTemplate jdbcTemplate;
	@SuppressWarnings("unchecked")
	public List<CommissionRateDefaultModel> loadDefaultCommissionRateList(CommissionRateDefaultModel defaultRateModel){
		logger.info("[CommissionRateDefaultHibernateDAO.loadDefaultCommissionRateList] Loading default rates with Start Time :: " + new Date());

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT * FROM PRODUCT_CHARGES_DEFAULT WHERE PRODUCT_ID = " + defaultRateModel.getProductId());

		logger.info("[CommissionRateDefaultHibernateDAO.loadDefaultCommissionRateList] Loading default rates with Query " + stringBuilder.toString());

		List<CommissionRateDefaultModel> list = (List<CommissionRateDefaultModel>)jdbcTemplate.query(stringBuilder.toString(),new CommissionRateDefaultModel());

		logger.info("[CommissionRateDefaultHibernateDAO.loadDefaultCommissionRateList] Loading default rates with End Time :: " + new Date());

		return list;
	}
	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
}

package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.tax.dao.FEDRuleDAO;
import com.inov8.microbank.tax.model.FEDRuleModel;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class FEDRuleHibernateDAO extends 
    BaseHibernateDAO<FEDRuleModel, Long, FEDRuleDAO>
    implements FEDRuleDAO
{

	private JdbcTemplate jdbcTemplate;

	@Override
	public void deleteFedRules() throws FrameworkCheckedException{
		String qry = "delete from FEDRuleModel fed";
		super.getHibernateTemplate().bulkUpdate(qry);
	}

	@Override
	public List<FEDRuleModel> loadAllActiveRulesByServiceId(FEDRuleModel model) throws FrameworkCheckedException {
		Long isActive = 1L;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT * FROM FED_RULE WHERE IS_ACTIVE = " + isActive + " AND SERVICE_ID = " + model.getServiceId());
		return (List<FEDRuleModel>) jdbcTemplate.query(stringBuilder.toString(),new FEDRuleModel());
	}

	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

}

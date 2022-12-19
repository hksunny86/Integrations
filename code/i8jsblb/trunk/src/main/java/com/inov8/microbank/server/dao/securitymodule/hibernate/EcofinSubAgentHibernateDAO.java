package com.inov8.microbank.server.dao.securitymodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.fonepay.model.EcofinSubAgentModel;
import com.inov8.microbank.server.dao.securitymodule.EcofinSubAgentDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;


@SuppressWarnings({ "unchecked"})
public class EcofinSubAgentHibernateDAO
		extends BaseHibernateDAO<EcofinSubAgentModel, Long, EcofinSubAgentDAO>
		implements EcofinSubAgentDAO
{

	private JdbcTemplate jdbcTemplate;

	protected static Log LOGGER = LogFactory.getLog(EcofinSubAgentHibernateDAO.class);

	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public EcofinSubAgentModel loadEcofinSubAgentbyId(Long ecofinSubAgentIds) {

		List<EcofinSubAgentModel> users = this.getHibernateTemplate().findByNamedParam("select au from EcofinSubAgentModel au where au.ecofinSubAgentId in ( :ecofinSubAgentIds )", "ecofinSubAgentIds", ecofinSubAgentIds);
		return users.get(0);

//		String hql = "select distinct au from EcofinSubAgentModel au where au.ecofinSubAgentId = 102955";
//		List<EcofinSubAgentModel> users = getHibernateTemplate().find(hql);
//		EcofinSubAgentModel ecofinSubAgentModel =null;
//		ecofinSubAgentModel = users.get(0);
//		return ecofinSubAgentModel;
	}
}

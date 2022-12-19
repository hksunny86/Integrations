package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CustomerRemitterModel;
import com.inov8.microbank.server.dao.customermodule.CustomerRemitterDAO;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 20018</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Abu Turab
 * @version 1.0
 *
 */

public class CustomerRemitterHibernateDAO
    extends
    BaseHibernateDAO<CustomerRemitterModel, Long, CustomerRemitterDAO>
    implements
    CustomerRemitterDAO
{
	private JdbcTemplate jdbcTemplate;




	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<CustomerRemitterModel> getActiveCustomerRemitterModelByCustomerId(Long customerId) throws FrameworkCheckedException {
		Map<String, Object> criterionMap = new HashMap<String, Object>(0);
		criterionMap.put("isActive", 1L);
		criterionMap.put("relationCustomerIdCustomerModel.customerId", customerId);

		Criterion criterion = Restrictions.allEq(criterionMap);

		CustomList<CustomerRemitterModel> customList = super.findByCriteria(criterion);

		if(customList != null && !customList.getResultsetList().isEmpty()) {
			List<CustomerRemitterModel> list = customList.getResultsetList();
			return list;
		}

		return null;
	}
}

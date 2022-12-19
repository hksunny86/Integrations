package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CustomerFundSourceModel;
import com.inov8.microbank.server.dao.customermodule.CustomerFundSourceDAO;

/**
 * <p>
 * Title: Microbank
 * Copyright: Copyright (c) 2006
 * Company: Inov8 Ltd
 * @author Rizwan Munir
 */

public class CustomerFundSourceHibernateDAO extends
		BaseHibernateDAO<CustomerFundSourceModel, Long, CustomerFundSourceDAO>
		implements CustomerFundSourceDAO {
}

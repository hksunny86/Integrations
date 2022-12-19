package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.BusinessDetailModel;
import com.inov8.microbank.server.dao.customermodule.BusinessDetailDAO;

/**
 * @author Soofiafa
 * 
 */

public class BusinessDetailHibernateDAO extends
		BaseHibernateDAO<BusinessDetailModel, Long, BusinessDetailDAO>
		implements BusinessDetailDAO {
}

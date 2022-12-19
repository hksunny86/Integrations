package com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.hibernate;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.LinkPaymentModeDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;

public class LinkPaymentModeHibernateDAO extends BaseHibernateDAO<SmartMoneyAccountModel, Long, SmartMoneyAccountDAO>
implements LinkPaymentModeDAO 


{

	@Override
	public SmartMoneyAccountModel saveOrUpdate(SmartMoneyAccountModel smartMoneyAccountModel) throws DataAccessException {
		super.saveOrUpdate(smartMoneyAccountModel);
		this.getHibernateTemplate().flush();
		return smartMoneyAccountModel ;
	}

}

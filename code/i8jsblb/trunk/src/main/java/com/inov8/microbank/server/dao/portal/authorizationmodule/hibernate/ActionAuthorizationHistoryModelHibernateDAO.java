package com.inov8.microbank.server.dao.portal.authorizationmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.server.dao.portal.authorizationmodule.ActionAuthorizationHistoryModelDAO;

public class ActionAuthorizationHistoryModelHibernateDAO extends
		BaseHibernateDAO<ActionAuthorizationHistoryModel,Long, ActionAuthorizationHistoryModelDAO> implements ActionAuthorizationHistoryModelDAO  {
}

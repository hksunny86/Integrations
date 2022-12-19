package com.inov8.microbank.server.dao.actionlogmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.server.dao.actionlogmodule.ActionLogDAO;
import com.sun.xml.wss.saml.Action;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import java.util.List;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public class ActionLogHibernateDAO 
	extends BaseHibernateDAO<ActionLogModel, Long, ActionLogDAO>
	implements ActionLogDAO
{
	@Override
	public ActionLogModel getActionLogModelByActionAuthId(Long actionAuthId) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = null;
		String hql = "FROM ActionLogModel alm WHERE alm.actionAuthorizationId = " + actionAuthId;
		List<ActionLogModel> list = this.getHibernateTemplate().find(hql) ;
		if(list!= null && !list.isEmpty())
			actionLogModel = list.get(0);
		this.getHibernateTemplate().flush();
		/*SessionFactoryUtils.releaseSession(session, getSessionFactory());*/
		return actionLogModel;
	}

	@Override
	public ActionLogModel findByPrimaryKey(ActionLogModel actionLogModel) {
		return actionLogModel;
	}
}

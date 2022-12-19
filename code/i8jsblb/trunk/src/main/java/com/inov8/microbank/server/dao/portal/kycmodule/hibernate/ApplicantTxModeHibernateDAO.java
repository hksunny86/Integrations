/**
 * 
 */
package com.inov8.microbank.server.dao.portal.kycmodule.hibernate;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ApplicantTxModeModel;
import com.inov8.microbank.server.dao.portal.kycmodule.ApplicantTxModeDAO;

/**
 * @author basit.mehr
 *
 */
public class ApplicantTxModeHibernateDAO 
extends BaseHibernateDAO<ApplicantTxModeModel, Long,ApplicantTxModeDAO>
implements ApplicantTxModeDAO
{

	@Override
	public void deleteApplicantTransactionMode(SearchBaseWrapper searchWrapper){
		Session session = this.getSession();
		ApplicantTxModeModel applicantTxModeModel = (ApplicantTxModeModel) searchWrapper.getBasePersistableModel();
		String hql = "delete ApplicantTxModeModel atm where atm.initialAppFormNo='"+applicantTxModeModel.getInitialAppFormNo()+"'";
		session.createQuery(hql).executeUpdate();
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
	}
}

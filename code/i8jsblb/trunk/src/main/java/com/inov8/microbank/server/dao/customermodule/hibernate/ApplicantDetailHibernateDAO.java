package com.inov8.microbank.server.dao.customermodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.server.dao.customermodule.ApplicantDetailDAO;

/**
 * @author Soofiafa
 * 
 */ 

public class ApplicantDetailHibernateDAO extends
		BaseHibernateDAO<ApplicantDetailModel, Long, ApplicantDetailDAO>
		implements ApplicantDetailDAO {
	@Override
	public boolean isIdDocumentNumberAleardyExist(Long retailerContactId, Long idDocumentType, String idDocumentNumber)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(ApplicantDetailModel.class);
		criteria.add( Restrictions.ne("retailerContactModel.retailerContactId", retailerContactId));
		criteria.add( Restrictions.eq("idType",idDocumentType));
		criteria.add( Restrictions.eq("idNumber",idDocumentNumber));
		List<ApplicantDetailModel> list = getHibernateTemplate().findByCriteria(criteria);
		
		return list.size()>0 ? true:false;
	}
}

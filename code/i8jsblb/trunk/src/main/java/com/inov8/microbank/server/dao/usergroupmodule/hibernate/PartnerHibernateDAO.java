package com.inov8.microbank.server.dao.usergroupmodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerDAO;

public class PartnerHibernateDAO extends BaseHibernateDAO<PartnerModel,Long,PartnerDAO> implements 
PartnerDAO{

	
	public List<PartnerModel> findPartnerByRetailerId(long retailerId) throws FrameworkCheckedException
	{
		List<PartnerModel> partnerModelModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PartnerModel.class);
        detachedCriteria.add( Restrictions.eq("relationRetailerIdRetailerModel.retailerId", retailerId));
        partnerModelModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return partnerModelModelList;
	}
	
}

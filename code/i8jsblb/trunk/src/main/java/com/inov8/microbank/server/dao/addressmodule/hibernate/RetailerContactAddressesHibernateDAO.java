package com.inov8.microbank.server.dao.addressmodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.util.AddressTypeConstants;
import com.inov8.microbank.common.util.ApplicantTypeConstants;
import com.inov8.microbank.server.dao.addressmodule.RetailerContactAddressesDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: </p>
 *
 * @author Usman Ashraf
 * @version 1.0
 */


public class RetailerContactAddressesHibernateDAO
    extends BaseHibernateDAO<RetailerContactAddressesModel, Long, RetailerContactAddressesDAO>
    implements RetailerContactAddressesDAO
{
	
	public RetailerContactAddressesModel findByRetailerContractId(long retailerContactId) throws FrameworkCheckedException
	{
		RetailerContactAddressesModel retailerContactAddressesModel = null;
		List<RetailerContactAddressesModel> list = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RetailerContactAddressesModel.class);
        detachedCriteria.add(Restrictions.eq("relationRetailerContactIdRetailerContactModel.retailerContactId", retailerContactId));
        list = getHibernateTemplate().findByCriteria(detachedCriteria );
        if(list != null && list.size() > 0)
        {
        	retailerContactAddressesModel = list.get(0);
        }
		return retailerContactAddressesModel;
	}
	
	public RetailerContactAddressesModel findBusinessTypeRetailerContactAddress(long retailerContactId) throws FrameworkCheckedException
	{
		RetailerContactAddressesModel retailerContactAddressesModel = null;
		List<RetailerContactAddressesModel> list = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RetailerContactAddressesModel.class);
        detachedCriteria.add(Restrictions.eq("relationRetailerContactIdRetailerContactModel.retailerContactId", retailerContactId));
        detachedCriteria.add(Restrictions.eq("relationAddressTypeIdAddressTypeModel.addressTypeId", AddressTypeConstants.BUSINESS_ADDRESS));
        detachedCriteria.add(Restrictions.eq("applicantTypeId", ApplicantTypeConstants.APPLICANT_TYPE_1));
        list = getHibernateTemplate().findByCriteria(detachedCriteria );
        if(list != null && list.size() > 0)
        {
        	retailerContactAddressesModel = list.get(0);
        }
		return retailerContactAddressesModel;
	}
	
	@Override
	public RetailerContactAddressesModel findRetailerContactAddress(Long retailerContactId, Long addressType, Long applicantDetailId) throws FrameworkCheckedException
	{
		RetailerContactAddressesModel retailerContactAddressesModel = null;
		List<RetailerContactAddressesModel> list = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RetailerContactAddressesModel.class);
        detachedCriteria.add(Restrictions.eq("relationRetailerContactIdRetailerContactModel.retailerContactId", retailerContactId));
        detachedCriteria.add(Restrictions.eq("relationAddressTypeIdAddressTypeModel.addressTypeId", addressType));
        detachedCriteria.add(Restrictions.eq("applicantDetailId", applicantDetailId));
        list = getHibernateTemplate().findByCriteria(detachedCriteria );
        if(list != null && list.size() > 0)
        {
        	retailerContactAddressesModel = list.get(0);
        }
		return retailerContactAddressesModel;
	}
}

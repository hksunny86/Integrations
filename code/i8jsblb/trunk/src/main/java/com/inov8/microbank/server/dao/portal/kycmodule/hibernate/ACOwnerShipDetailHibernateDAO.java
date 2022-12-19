/**
 * 
 */
package com.inov8.microbank.server.dao.portal.kycmodule.hibernate;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ACOwnershipDetailModel;
import com.inov8.microbank.server.dao.portal.kycmodule.ACOwnerShipDAO;

/**
 * @author Abu Turab
 *
 */
public class ACOwnerShipDetailHibernateDAO 
extends BaseHibernateDAO<ACOwnershipDetailModel, Long,ACOwnerShipDAO>
implements ACOwnerShipDAO
{

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.portal.kycmodule.ACOwnerShipDAO#loadAccountOwnerShipsByCustomerId(com.inov8.microbank.common.model.ACOwnershipDetailModel)
	 */
	@Override
	public List<ACOwnershipDetailModel> loadAccountOwnerShipsByCustomerId(ACOwnershipDetailModel ownerShipDetailModel)
	throws FrameworkCheckedException{
		String hql = "from ACOwnershipDetailModel os where os.relationCustomerIdCustomerModel.customerId=:customerId and os.isDeleted=:flagVal";
		String[] params = {"customerId","flagVal"};
		Object[] values = {ownerShipDetailModel.getCustomerId(), Boolean.FALSE};
		List<ACOwnershipDetailModel> list =getHibernateTemplate().findByNamedParam(hql, params, values);
		
	return list;
	}
	
	@Override
	public List<ACOwnershipDetailModel> loadAccountOwnerShipsByRetailerContactId(ACOwnershipDetailModel ownerShipDetailModel)
	throws FrameworkCheckedException{
		String hql = "from ACOwnershipDetailModel os where os.relationRetailerContactIdRetailerContactModel.retailerContactId=:retailerContactId and os.isDeleted=:flagVal";
		String[] params = {"retailerContactId","flagVal"};
		Object[] values = {ownerShipDetailModel.getRetailerContactId(), Boolean.FALSE};
		List<ACOwnershipDetailModel> list =getHibernateTemplate().findByNamedParam(hql, params, values);
		
	return list;
	}
	
}

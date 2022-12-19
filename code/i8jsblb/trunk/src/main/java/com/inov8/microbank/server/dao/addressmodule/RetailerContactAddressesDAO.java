package com.inov8.microbank.server.dao.addressmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;

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



public interface RetailerContactAddressesDAO
    extends BaseDAO<RetailerContactAddressesModel, Long>
{
	public RetailerContactAddressesModel findBusinessTypeRetailerContactAddress(long retailerContactId) throws FrameworkCheckedException;
	public RetailerContactAddressesModel findByRetailerContractId(long retailerContactId) throws FrameworkCheckedException;
	RetailerContactAddressesModel findRetailerContactAddress(Long retailerContactId, Long addressType, Long applicantDetailId)
			throws FrameworkCheckedException;
}

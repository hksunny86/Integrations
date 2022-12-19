package com.inov8.microbank.server.dao.portal.ola;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 20, 2013 7:37:37 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface OlaCustomerAccountTypeDao extends BaseDAO<OlaCustomerAccountTypeModel, Long>
{
	public List<OlaCustomerAccountTypeModel> loadCustomerACTypes(Long[] typesId) throws FrameworkCheckedException;

	List<OlaCustomerAccountTypeModel> searchParentOlaCustomerAccountTypes(Long... customerAccountTypeIdsToExclude);

	List<OlaCustomerAccountTypeModel> searchSubtypesAndLimits(Long parentAccountTypeId);

	public List<OlaCustomerAccountTypeModel> loadHandlerACTypes() throws FrameworkCheckedException;

	/* added by atif hussain*/
	List<OlaCustomerAccountTypeModel> getAllActiveCustomerAccountTypes();
	List<OlaCustomerAccountTypeModel> getParentOlaCustomerAccountTypes(Long[] accountTypeIds);
	
}

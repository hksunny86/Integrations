package com.inov8.ola.server.dao.limit;

/** 	
 * @author 					Usman Ashraf
 * Project Name: 			OLA
 * Creation Date: 			April 2012  			
 * Description:				
 */


import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.LimitModel;


public interface LimitDAO extends BaseDAO<LimitModel, Long> {

	public List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId)throws FrameworkCheckedException;

	LimitModel getLimitByTransactionType(Long transactionTypeId, Long limitTypeId,Long customerAccountTypeId) throws FrameworkCheckedException;
}

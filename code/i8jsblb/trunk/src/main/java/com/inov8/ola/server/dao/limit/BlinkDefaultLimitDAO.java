package com.inov8.ola.server.dao.limit;

/** 	
 * @author 					Usman Ashraf
 * Project Name: 			OLA
 * Creation Date: 			April 2012  			
 * Description:				
 */


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.BlinkDefaultLimitModel;
import com.inov8.integration.common.model.LimitModel;

import java.util.List;


public interface BlinkDefaultLimitDAO extends BaseDAO<BlinkDefaultLimitModel, Long> {

	BlinkDefaultLimitModel getLimitByTransactionType(Long customerAccountTypeId, Long limitTypeId, Long transactionTypeId) throws FrameworkCheckedException;
}

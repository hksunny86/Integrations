package com.inov8.microbank.server.service.AllpayModule;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			Microbank
 * Creation Date: 			December 2008  			
 * Description:				
 */

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface AllpayCommissionTransactionManager
{

	BaseWrapper loadCommissionTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	BaseWrapper updateCommissionTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

}

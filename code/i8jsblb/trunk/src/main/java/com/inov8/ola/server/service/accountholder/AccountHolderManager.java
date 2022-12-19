package com.inov8.ola.server.service.accountholder;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */



import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;


public interface AccountHolderManager {
	
	BaseWrapper saveAccountHolder(BaseWrapper baseWrapper) throws Exception;
	
	BaseWrapper loadAccountHolder(BaseWrapper baseWrapper) throws Exception;
	
	void updateCnicAndMobileNo(String oldCnic, String newCnic, String newMobileNo) throws FrameworkCheckedException;

	int updateAccountHolderModelToCloseAccount(AccountHolderModel accountHolderModelModel) throws  FrameworkCheckedException;
}

package com.inov8.ola.server.facade;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.accountholder.AccountHolderManager;


public interface AccountFacade
    extends AccountHolderManager,AccountManager
{
    Long getAccountIdByCustomerAccountType(String cnic, Long customerAccountTypeId);
}

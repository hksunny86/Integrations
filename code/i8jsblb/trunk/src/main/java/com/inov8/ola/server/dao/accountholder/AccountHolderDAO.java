package com.inov8.ola.server.dao.accountholder;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.AccountHolderModel;


public interface AccountHolderDAO extends BaseDAO<AccountHolderModel, Long> {
	void updateCnicAndMobileNo(String oldCnic, String newCnic, String newMobileNo);

	void updateMobileNo(String cnic, String mobileNo);

	public List<AccountHolderModel> getAccountHolderModelListByAccountIds(List<Long> accountIds);

	public void updateCnic(String cnic, String mobileNo);

	int updateAccountHolderModelToCloseAccount(AccountHolderModel accountHolderModelModel) throws FrameworkCheckedException;
}

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
import com.inov8.ola.server.dao.accountholder.AccountHolderDAO;


public class AccountHolderManagerImpl implements AccountHolderManager 
{

	private  AccountHolderDAO  accountHolderDAO;
	
	
	public BaseWrapper loadAccountHolder(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		AccountHolderModel accountHolderModel = (AccountHolderModel)baseWrapper.getBasePersistableModel() ;		
		accountHolderModel = this.accountHolderDAO.findByPrimaryKey(accountHolderModel.getAccountHolderId()) ;		
		baseWrapper.setBasePersistableModel(accountHolderModel) ;
		
		return baseWrapper;	
	}
	

	public BaseWrapper saveAccountHolder(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		AccountHolderModel accountHolderModel = (AccountHolderModel)baseWrapper.getBasePersistableModel() ;		
		accountHolderModel = this.accountHolderDAO.saveOrUpdate(accountHolderModel) ;		
		baseWrapper.setBasePersistableModel(accountHolderModel) ;
		
		return baseWrapper;
	}

	@Override
	public void updateCnicAndMobileNo(String oldCnic, String newCnic, String newMobileNo) throws FrameworkCheckedException
	{
		accountHolderDAO.updateCnicAndMobileNo(oldCnic, newCnic, newMobileNo);
	}

	@Override
	public int updateAccountHolderModelToCloseAccount(AccountHolderModel accountHolderModelModel) throws FrameworkCheckedException {
		return accountHolderDAO.updateAccountHolderModelToCloseAccount(accountHolderModelModel);
	}


	public void setAccountHolderDAO(AccountHolderDAO accountHolderDAO)
	{
		this.accountHolderDAO = accountHolderDAO;
	}




	
	



}



package com.inov8.ola.server.facade;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.AccountsWithStatsListViewModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.accountholder.AccountHolderManager;



public class AccountFacadeImpl implements AccountFacade
{
		
	private AccountManager accountManager;
	private AccountHolderManager accountHolderManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public OLAVO creditTransferRequiresNewTransaction(OLAVO olaVO) throws Exception
	{
		try
	    {
			olaVO = this.accountManager.creditTransferRequiresNewTransaction(olaVO);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	    return olaVO;		
	}

	public OLAVO makeTransferFunds(OLAVO olaVO) throws Exception{
		try{
			olaVO = this.accountManager.makeTransferFunds(olaVO);
	    }catch (Exception ex){
		    throw ex ;
	    }
	    
	    return olaVO;		
	}

	public OLAVO makeCreditTransfer(OLAVO olaVO) throws Exception
	{
		try
	    {
			olaVO = this.accountManager.makeCreditTransfer(olaVO);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	    return olaVO;		
	}
	
	
	public OLAVO makeTx(OLAVO olaVO) throws Exception
	{
		try
	    {
			olaVO = this.accountManager.makeTx(olaVO);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	    return olaVO;		
	}
	
	public OLAVO makeTxRequiresNewTransaction(OLAVO olaVO) throws Exception
	{
		try
	    {
			olaVO = this.accountManager.makeTxRequiresNewTransaction(olaVO);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	    return olaVO;		
	}
	
	public OLAVO makeTxFori8Commission(OLAVO olaVO) throws Exception
	{
		try
	    {
			olaVO = this.accountManager.makeTxFori8Commission(olaVO);
	    }
	    catch (Exception ex)
	    {

		    throw ex ;
	    }
	    
	    return olaVO;		
	}
	
	
	public BaseWrapper updateAccount(BaseWrapper baseWrapper) throws Exception
	{
		try
	    {
			baseWrapper = this.accountManager.updateAccount(baseWrapper);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	    return baseWrapper;
	}
	
	public BaseWrapper loadAccountByPK(BaseWrapper baseWrapper) throws Exception
	{
		try
	    {
			baseWrapper = this.accountManager.loadAccountByPK(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw ex ;
	    }
	    
	    return baseWrapper;
	}

	@Override
	public AccountModel getAccountModelByCNICAndMobile(String cnic, String mobileNo) {
		return accountManager.getAccountModelByCNICAndMobile(cnic,mobileNo);
	}

	public BaseWrapper loadAccount(BaseWrapper baseWrapper) throws Exception
	{
		try
	    {
			baseWrapper = this.accountManager.loadAccount(baseWrapper);
	    }
	    catch (Exception ex)
	    {

	      throw ex;
	    }
	    
	    return baseWrapper;
	}
	
	
	public SearchBaseWrapper getAllAccounts(SearchBaseWrapper searchBaseWrapper) throws Exception
	{
		try
	    {
			searchBaseWrapper = this.accountManager.getAllAccounts(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {

		    throw ex ;	      
	    }
	    
	    return searchBaseWrapper;
	}
	
	public List<AccountsWithStatsListViewModel> getAllAccountsWithStats(Date date) throws Exception
	{
		List<AccountsWithStatsListViewModel> list;
		
		try
	    {
			list = this.accountManager.getAllAccountsWithStats(date);
	    }
	    catch (Exception ex)
	    {

		    throw ex ;	      
	    }
	    
	    return list;
	}
	
	
	public List<Object> getAccountStatsWithRange(Date startDate, Date endDate) throws Exception
	{
		List<Object> list;
		
		try
	    {
			list = this.accountManager.getAccountStatsWithRange( startDate, endDate );
	    }
	    catch (Exception ex)
	    {

		    throw ex ;	      
	    }
	    
	    return list;
	}
	
	
	
	public BaseWrapper loadAccountHolder(BaseWrapper baseWrapper) throws Exception
	{
		try
	    {
			baseWrapper = this.accountHolderManager.loadAccountHolder(baseWrapper);
	    }
	    catch (Exception ex)
	    {

		    throw ex ;
	    }
	    
	    return baseWrapper;
	}
	

	public BaseWrapper saveAccountHolder(BaseWrapper baseWrapper) throws Exception
	{
		try
	    {
			baseWrapper = this.accountHolderManager.saveAccountHolder(baseWrapper);
	    }
	    catch (Exception ex)
	    {

		    throw ex ;	  
	    }
	    
	    return baseWrapper;
	}
	public OLAVO verifyCreditLimits(OLAVO olaVO) throws Exception{
		OLAVO result = null;
		try{
			result = this.accountManager.verifyCreditLimits(olaVO);
		}catch(Exception ex){
			throw ex;
		}
		return result;
		
	}
	public OLAVO verifyDebitLimits(OLAVO olaVO) throws Exception{
		OLAVO result = null;
		try{
			result = this.accountManager.verifyDebitLimits(olaVO);
		}catch(Exception ex){
			throw ex;
		}
		return result;
		
	}
	
	public OLAVO verifyWalkinCustomerThroughputLimits(OLAVO olaVO) throws Exception {
		OLAVO olavo = null;
		try
	    {
			olavo = this.accountManager.verifyWalkinCustomerThroughputLimits(olaVO);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	    return olavo;
	}

	public OLAVO saveWalkinCustomerLedgerEntry(OLAVO olaVO) throws Exception{
		OLAVO olavo = null;
		try
	    {
			olavo = this.accountManager.saveWalkinCustomerLedgerEntry(olaVO);
	    }
	    catch (Exception ex)
	    {

		    throw ex ;
	    }
	    
	    return olavo;
	}

	public AccountModel getAccountModelByCNIC(String cnic) throws Exception{
		AccountModel accountModel = null;
		try
	    {
			accountModel = this.accountManager.getAccountModelByCNIC(cnic);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	    return accountModel;
	}


	@Override
	public OLAVO makeLedgerRollbackEntriesRequiresNewTransaction(OLAVO olavo) throws Exception {
		
		try
	    {
			olavo = this.accountManager.makeLedgerRollbackEntriesRequiresNewTransaction(olavo);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	    return olavo;
	}

	@Override
	public List<LabelValueBean> loadAccountIdsAndTitles( Long olaCustomerAccountTypeId ) throws FrameworkCheckedException
	{
	    try
        {
            return accountManager.loadAccountIdsAndTitles( olaCustomerAccountTypeId );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public OlaCustomerAccountTypeModel findAccountTypeById(Long accountTypeId) throws FrameworkCheckedException
	{
		try
        {
            return accountManager.findAccountTypeById(accountTypeId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION );
        }
	}

	@Override
	public SearchBaseWrapper searchOlaCustomerAccountTypes(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
	    try
        {
            return accountManager.searchOlaCustomerAccountTypes(searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public List<OlaCustomerAccountTypeModel> searchParentOlaCustomerAccountTypes(Long... customerAccountTypeIdsToExclude) throws FrameworkCheckedException
	{
		try
        {
            return accountManager.searchParentOlaCustomerAccountTypes(customerAccountTypeIdsToExclude);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public boolean isAssociatedWithAgentCustomerOrHandler(Long accountTypeId, Long parentAccountTypeId, boolean isCustomerAccountType) throws FrameworkCheckedException
	{
		try
        {
            return accountManager.isAssociatedWithAgentCustomerOrHandler(accountTypeId, parentAccountTypeId, isCustomerAccountType);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public List<OlaCustomerAccountTypeModel> searchSubtypesAndLimits(Long parentAccountTypeId) throws FrameworkCheckedException
	{
		try
        {
            return accountManager.searchSubtypesAndLimits(parentAccountTypeId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public boolean hasActiveAccountSubtypes(Long parentAccountTypeId) throws FrameworkCheckedException
	{
		try
        {
            return accountManager.hasActiveAccountSubtypes(parentAccountTypeId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public int countByExample(OlaCustomerAccountTypeModel model, ExampleConfigHolderModel exampleConfigHolderModel) throws FrameworkCheckedException
	{
		try
        {
            return accountManager.countByExample(model, exampleConfigHolderModel);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public BaseWrapper saveOlaCustomerAccountType( BaseWrapper baseWrapper ) throws FrameworkCheckedException
	{
	    try
        {
            return accountManager.saveOlaCustomerAccountType( baseWrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.INSERT_ACTION );
        }
	}

	@Override
	public void updateCnicAndMobileNo(String oldCnic, String newCnic, String newMobileNo) throws FrameworkCheckedException
	{
		try
        {
            accountHolderManager.updateCnicAndMobileNo( oldCnic, newCnic, newMobileNo );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}

	@Override
	public int updateAccountHolderModelToCloseAccount(AccountHolderModel accountHolderModelModel) throws FrameworkCheckedException {
		return accountHolderManager.updateAccountHolderModelToCloseAccount(accountHolderModelModel);
	}

	public void setAccountHolderManager(AccountHolderManager accountHolderManager)
	{
		this.accountHolderManager = accountHolderManager;
	}

	
	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	
	public void setAccountManager(AccountManager accountManager)
	{
		this.accountManager = accountManager;
	}

	@Override
	public boolean markLedgerEntriesRequiresNewTransaction(Long ledgerId) throws Exception {
		boolean result = false;
		try{
			result = this.accountManager.markLedgerEntriesRequiresNewTransaction(ledgerId);
	    }catch (Exception ex){
		    throw ex ;
	    }
	    return result;
	}

	@Override
	public SearchBaseWrapper searchAccount(SearchBaseWrapper searchBaseWrapper)
			throws Exception {
		try
		{
			return accountManager.searchAccount(searchBaseWrapper);
		}
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
	}

	@Override
	public SearchBaseWrapper getAllHanlderAccountTypes()
			throws FrameworkCheckedException {
		try
		{
			return accountManager.getAllHanlderAccountTypes();
		}
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
	}
	@Override
	public SearchBaseWrapper getAllActiveCustomerAccountTypes()
			throws FrameworkCheckedException {
		try
		{
			return accountManager.getAllActiveCustomerAccountTypes();
		}
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
	}
	@Override
	public SearchBaseWrapper searchAccountTypeView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try
		{
			return accountManager.searchAccountTypeView(wrapper);
		}
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public SearchBaseWrapper getParentOlaCustomerAccountTypes(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try
		{
			return accountManager.getParentOlaCustomerAccountTypes(searchBaseWrapper);
		}
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public List<OlaCustomerAccountTypeModel> getAllActiveCustomerAccountTypesGrouped()
			throws FrameworkCheckedException {
		try
		{
			return accountManager.getAllActiveCustomerAccountTypesGrouped();
		}
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public void saveFailedOLACreditDebit(OLAVO olavo) throws FrameworkCheckedException {

		try
	    {
			this.accountManager.saveFailedOLACreditDebit(olavo);
	    }
	    catch (Exception ex)
	    {
		    throw ex ;
	    }
	    
	
		
	}
	
	@Override
	public List<AccountModel> getAccountModelListByCNICs(List<String> cnicList)
			throws FrameworkCheckedException {
		
		try
		{
			return accountManager.getAccountModelListByCNICs(cnicList);
		}
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public Map<String, Object> getAllLimits(Date transactionDateTime, Long customerAccountTypeId, Long handlerId, String cnic, Long limitType) throws FrameworkCheckedException
	{
		return accountManager.getAllLimits(transactionDateTime,customerAccountTypeId,handlerId,cnic,limitType );
	}

	@Override
	public SearchBaseWrapper getAllAgentAccountTypes()throws FrameworkCheckedException {
		try
		{
			return accountManager.getAllAgentAccountTypes();
		}
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public void makeOLACreditDebitViaQueue(OLAVO olavo) throws FrameworkCheckedException {
		try{
            accountManager.makeOLACreditDebitViaQueue(olavo);
        }catch(Exception e){
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}

	@Override
	public void makeWalletOLACreditDebitViaQueue(OLAVO olavo) throws FrameworkCheckedException {
		try{
			accountManager.makeWalletOLACreditDebitViaQueue(olavo);
		}catch(Exception e){
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.UPDATE_ACTION );
		}
	}

	@Override
	public BaseWrapper loadTaggedAgentAccount(BaseWrapper baseWrapper)
			throws Exception {
		try
	    {
			baseWrapper = this.accountManager.loadTaggedAgentAccount(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw ex;
	    }
	    
	    return baseWrapper;
	}

	@Override
	public Long getAccountIdByCustomerAccountType(String cnic, Long customerAccountTypeId) {
		return accountManager.getAccountIdByCustomerAccountType(cnic,customerAccountTypeId);
	}

	@Override
	public AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic, Long customerAccountTypeId, Long statusId) {
		return this.accountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(cnic,customerAccountTypeId,statusId);
	}

	@Override
	public AccountModel getLastClosedAccount(String cnic, Long customerAccountTypeId) {
		return accountManager.getLastClosedAccount(cnic,customerAccountTypeId);
	}
}


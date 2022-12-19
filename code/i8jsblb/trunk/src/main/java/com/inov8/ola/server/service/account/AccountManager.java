package com.inov8.ola.server.service.account;

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
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.AccountsWithStatsListViewModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.ola.integration.vo.OLAVO;


public interface AccountManager 
{

	public AccountModel getAccountModelByCNICAndMobile(String cnic,String mobileNo);
	BaseWrapper loadAccount(BaseWrapper baseWrapper) throws Exception;
	BaseWrapper loadTaggedAgentAccount(BaseWrapper baseWrapper)  throws Exception;
	
	SearchBaseWrapper searchAccount(SearchBaseWrapper searchBaseWrapper) throws Exception;
	
	BaseWrapper updateAccount(BaseWrapper baseWrapper) throws Exception;
	
	SearchBaseWrapper getAllAccounts(SearchBaseWrapper searchBaseWrapper) throws Exception;
	
	BaseWrapper loadAccountByPK(BaseWrapper baseWrapper) throws Exception;
	
	public OLAVO creditTransferRequiresNewTransaction(OLAVO olaVO) throws Exception;
	public OLAVO makeCreditTransfer(OLAVO olaVO) throws Exception;
	
	public boolean markLedgerEntriesRequiresNewTransaction(Long ledgerId) throws Exception;
	
	OLAVO makeTx(OLAVO olaVO) throws Exception;
	OLAVO makeTxRequiresNewTransaction(OLAVO olaVO) throws Exception;
	
	OLAVO makeTxFori8Commission(OLAVO olaVO) throws Exception;
	
	List<AccountsWithStatsListViewModel> getAllAccountsWithStats(Date date) throws Exception;
	List<Object> getAccountStatsWithRange( Date startDate, Date endDate ) throws Exception;
	OLAVO verifyCreditLimits(OLAVO olaVO) throws Exception;
	OLAVO verifyDebitLimits(OLAVO olaVO) throws Exception;
	OLAVO verifyWalkinCustomerThroughputLimits(OLAVO olaVO) throws Exception;
	public OLAVO saveWalkinCustomerLedgerEntry(OLAVO olaVO) throws Exception;
	public AccountModel getAccountModelByCNIC(String cnic)throws Exception;
	
	public OLAVO makeLedgerRollbackEntriesRequiresNewTransaction(OLAVO olavo) throws Exception;

	List<LabelValueBean> loadAccountIdsAndTitles( Long olaCustomerAccountTypeId ) throws FrameworkCheckedException;

	OlaCustomerAccountTypeModel findAccountTypeById(Long accountTypeId) throws FrameworkCheckedException;

	SearchBaseWrapper searchOlaCustomerAccountTypes( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

	List<OlaCustomerAccountTypeModel> searchParentOlaCustomerAccountTypes(Long... customerAccountTypeIdsToExclude ) throws FrameworkCheckedException;

	List<OlaCustomerAccountTypeModel> searchSubtypesAndLimits(Long parentAccountTypeId) throws FrameworkCheckedException;

	boolean hasActiveAccountSubtypes(Long parentAccountTypeId) throws FrameworkCheckedException;

	boolean isAssociatedWithAgentCustomerOrHandler(Long accountTypeId, Long parentAccountTypeId, boolean isCustomerAccountType) throws FrameworkCheckedException;

	int countByExample(OlaCustomerAccountTypeModel model, ExampleConfigHolderModel exampleConfigHolderModel) throws FrameworkCheckedException;

	BaseWrapper saveOlaCustomerAccountType( BaseWrapper baseWrapper ) throws FrameworkCheckedException;
	public OLAVO makeTransferFunds(OLAVO olaVO) throws Exception;
	
	SearchBaseWrapper getAllHanlderAccountTypes() throws FrameworkCheckedException;
	/*added by atif hussain*/
	SearchBaseWrapper getAllActiveCustomerAccountTypes()throws FrameworkCheckedException;
	SearchBaseWrapper searchAccountTypeView(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	SearchBaseWrapper getParentOlaCustomerAccountTypes(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException;
	
	List<OlaCustomerAccountTypeModel> getAllActiveCustomerAccountTypesGrouped() throws FrameworkCheckedException;

	public List<AccountModel> getAccountModelListByCNICs(List<String> cnicList)
			throws FrameworkCheckedException;

	public Map<String, Object> getAllLimits(Date transactionDateTime, Long customerAccountTypeId, Long handlerId, String cnic, Long limitType)throws FrameworkCheckedException;
	SearchBaseWrapper getAllAgentAccountTypes() throws FrameworkCheckedException;
	void saveFailedOLACreditDebit(OLAVO olaVO) throws FrameworkCheckedException;
	void makeOLACreditDebitViaQueue(OLAVO olavo) throws FrameworkCheckedException;
	void makeWalletOLACreditDebitViaQueue(OLAVO olavo) throws FrameworkCheckedException;

	Long getAccountIdByCustomerAccountType(String cnic,Long customerAccountTypeId);

	public AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic,Long customerAccountTypeId,Long statusId);

	AccountModel getLastClosedAccount(String cnic, Long customerAccountTypeId);
}

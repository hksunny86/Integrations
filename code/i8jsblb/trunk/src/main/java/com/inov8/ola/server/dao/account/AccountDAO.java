package com.inov8.ola.server.dao.account;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.AccountsWithStatsListViewModel;
import com.inov8.microbank.common.util.LabelValueBean;


public interface AccountDAO extends BaseDAO<AccountModel, Long> 
{
	public List<AccountModel> getAllAccounts( SearchBaseWrapper searchBaseWrapper ); 
	public List<AccountsWithStatsListViewModel> getAccountBalanceStats( Date date );
	public List<Object> getAccountStatsWithRange( Date startDate, Date endDate );
	public BaseWrapper loadAccountAndLock(BaseWrapper baseWrapper);
	public BaseWrapper loadAccountAndLock(Long PK);
	public AccountModel getAccountModelByCNIC(String cnic);
	public AccountModel getAccountModelByCNICAndMobile(String cnic,String mobileNo);
	public List<LabelValueBean> loadAccountIdsAndTitles(Long olaCustomerAccountTypeId);
	int updateAccountBalanceByAccountId(Long accountId, Double amount, Long ledgerId, boolean isCredit);
	public List<AccountModel> getAccountModelListByCNICs(List<String> cnicList);
	public Long getAccountIdByCnic(String cnic, Long accountTypeId);

	Long getAccountIdByCustomerAccountType(String cnic, Long customerAccountTypeId);

	public AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic,Long customerAccountTypeId,Long statusId);

	AccountModel getLastClosedAccountModel(String cnic, Long customerAccountTypeId);

	int updateAccountModelToCloseAccount(AccountModel accountModel) throws FrameworkCheckedException;
}

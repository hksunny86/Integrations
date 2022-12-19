package com.inov8.microbank.webapp.action.portal.reports.finance;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.portal.bbcustomers.BbCustomerAccountsViewModel;
import com.inov8.microbank.common.model.portal.ola.BBSettlementAccountsViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.CryptographyType;
import com.inov8.microbank.server.facade.portal.financereportsmodule.FinanceReportsFacacde;
import com.inov8.ola.util.AccountConstants;
import com.inov8.ola.util.EncryptionUtil;

public class SettlementAccountReportViewController extends BaseSearchController{
	
	//Autowired
    private FinanceReportsFacacde financeReportsFacacde;
	
	public SettlementAccountReportViewController() {
	    super.setFilterSearchCommandClass(BBSettlementAccountsViewModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest req, LinkedHashMap<String, SortingOrder> sortingOrder) throws Exception {
		
		BBSettlementAccountsViewModel modelToSearch = new BBSettlementAccountsViewModel();
		
		BeanUtils.copyProperties(modelToSearch,  (BBSettlementAccountsViewModel)model);
		modelToSearch.setAccountId(null); // reset default value from zero to null
		modelToSearch.setAccountHolderId(null); // reset default value from zero to null
		modelToSearch.setProductId(null);
		modelToSearch.setAccountTypeId(null);
		
		String searchAccountNumber = modelToSearch.getAccountNumber();
        if( !GenericValidator.isBlankOrNull(searchAccountNumber) && !AccountConstants.BULK_PAYMENT_SUNDRY_OLA_ACCOUNT_NUMBER.equals(searchAccountNumber) ){
            modelToSearch.setAccountNumber( EncryptionUtil.encryptAccountNo( modelToSearch.getAccountNumber() ) );
        }
//        EncryptionUtil.docryptFields( CryptographyType.ENCRYPT, modelToSearch, "cnic", "dob" );

		if(sortingOrder.isEmpty()){
        	sortingOrder.put("accountNumber", SortingOrder.ASC);
        }
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setSortingOrderMap(sortingOrder);
		searchBaseWrapper.setBasePersistableModel(modelToSearch);
		searchBaseWrapper = this.financeReportsFacacde.searchBBSettlementAccountsReport(searchBaseWrapper);
		List<BBSettlementAccountsViewModel> list = searchBaseWrapper.getCustomList().getResultsetList();
		
		for( BBSettlementAccountsViewModel bbSettlementAccountsViewModel : list ){
            try{
                String accountNumber = bbSettlementAccountsViewModel.getAccountNumber();
                if( !GenericValidator.isBlankOrNull(accountNumber) && !AccountConstants.BULK_PAYMENT_SUNDRY_OLA_ACCOUNT_NUMBER.equals(accountNumber) ){
                	bbSettlementAccountsViewModel.setAccountNumber( EncryptionUtil.decryptAccountNo( accountNumber ) ) ;
                }
            }catch( Exception e ){
                log.error( e.getMessage(), e );
            }
            
            try{
            	if(AccountConstants.BULK_PAYMENT_SUNDRY_OLA_ACCOUNT_ID != bbSettlementAccountsViewModel.getAccountId().longValue()){
            		EncryptionUtil.docryptFields( CryptographyType.DECRYPT, bbSettlementAccountsViewModel, "balance");
				}
        		
            	String formattedBalance = CommonUtils.formatAmount(Double.parseDouble(bbSettlementAccountsViewModel.getBalance()));
        		bbSettlementAccountsViewModel.setBalance(formattedBalance);
        		
            }catch( Exception e ){
                log.error( e.getMessage(), e );
                bbSettlementAccountsViewModel.setBalance("Invalid Balance");
            }

            
		}
		
//		AppUserModel user = UserUtils.getCurrentUser();
//		req.setAttribute("currentAppUserId", user.getAppUserId());
		ModelAndView modelAndView = new ModelAndView(getSearchView(), "settlementAccountsList", list);
		return modelAndView;
	}

	public void setFinanceReportsFacacde( FinanceReportsFacacde financeReportsFacacde ) {
        this.financeReportsFacacde = financeReportsFacacde;
    }

    
}

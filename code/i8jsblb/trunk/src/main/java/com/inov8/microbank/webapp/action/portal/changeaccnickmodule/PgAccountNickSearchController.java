package com.inov8.microbank.webapp.action.portal.changeaccnickmodule;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.portal.changeaccnickmodule.ChangeAccountNickListViewModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;

public class PgAccountNickSearchController extends BaseSearchController{
	private LinkPaymentModeManager	linkPaymentModeManager;
	private FinancialIntegrationManager financialIntegrationManager;
	
	public PgAccountNickSearchController() {
	    super.setFilterSearchCommandClass(ChangeAccountNickListViewModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, 
			                        Object model, 
			                        HttpServletRequest req, 
			                        LinkedHashMap<String, SortingOrder> sortingOrder) 
	throws Exception {
		ChangeAccountNickListViewModel changeAccountNickListViewModel = (ChangeAccountNickListViewModel)model; 
		Long bankId = UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId();
		
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		BankModel bankModel = new BankModel();
		bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
		boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
		req.setAttribute("veriflyRequired", veriflyRequired);
		
		Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(req, "appUserId")));
		changeAccountNickListViewModel.setBankId(bankId);
		changeAccountNickListViewModel.setAppUserId(appUserId);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        if(sortingOrder.isEmpty()){
        	sortingOrder.put("accountNick", SortingOrder.ASC);
        }

		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		
		searchBaseWrapper.setSortingOrderMap(sortingOrder);
		searchBaseWrapper.setBasePersistableModel(changeAccountNickListViewModel);
		searchBaseWrapper = this.linkPaymentModeManager.searchAccounts(searchBaseWrapper);
		List list = searchBaseWrapper.getCustomList().getResultsetList();
		ModelAndView modelAndView = new ModelAndView(getSearchView(), "accountsNicksList",
				                        searchBaseWrapper.getCustomList().getResultsetList());
		return modelAndView;
	}

	/**
	 * @param linkPaymentModeManager the linkPaymentModeManager to set
	 */
	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}
	

}

package com.inov8.microbank.webapp.action.allpaymodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AllpayForgotVeriflyPinViewModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.allpaymodule.AllpayForgotVeriflyPinManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Forgot Verifly pin</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Basit Mehr
 * @version 1.0
 */

public class AllpayForgotBankPinSearchController extends BaseFormSearchController {

    private AllpayForgotVeriflyPinManager allpayForgotVeriflyPinManager;
    private ReferenceDataManager referenceDataManager;
    private LinkPaymentModeManager linkPaymentModeManager;
    private FinancialIntegrationManager financialIntegrationManager;
	
	public AllpayForgotBankPinSearchController() {
        super.setCommandName("forgotVeriflyPinViewModel");
        super.setCommandClass(AllpayForgotVeriflyPinViewModel.class);
	}


	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		// TODO Auto-generated method stub
		Map referenceMap = new HashMap();
		return referenceMap;
	}


    protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object object,
            PagingHelperModel pagingHelperModel,
            LinkedHashMap linkedHashMap) throws
       Exception {	
    	BaseWrapper baseWrapperBank = new BaseWrapperImpl();
    	BankModel bankModel = new BankModel();
    	bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
    	baseWrapperBank.setBasePersistableModel(bankModel);
    	AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
    	boolean veriflyRequired = true;
    	try{
    		veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
    	}   
    	catch(Exception ex){
    		ex.printStackTrace();
    	}   
    	httpServletRequest.setAttribute("veriflyRequired", veriflyRequired);
    	BaseWrapper baseWrapper = new BaseWrapperImpl();
    	
        AllpayForgotVeriflyPinViewModel allpayForgotVeriflyPinViewModel = (
        		AllpayForgotVeriflyPinViewModel) object;
        
    	String eappUserId = ServletRequestUtils.getStringParameter(httpServletRequest, "fromAppUserId");
    	Long appUserId = null;
    	UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
    	if(eappUserId != null && eappUserId.trim().length() > 0){
    		appUserId = new Long(EncryptionUtil.decryptWithDES(eappUserId));
    		
    		userInfoListViewModel.setAppUserId(appUserId);
    		baseWrapper.setBasePersistableModel(userInfoListViewModel);
    		baseWrapper = linkPaymentModeManager.loadUserInfoListViewByPrimaryKey(baseWrapper);
    		userInfoListViewModel = (UserInfoListViewModel)baseWrapper.getBasePersistableModel();
    		
    		allpayForgotVeriflyPinViewModel.setMfsId(userInfoListViewModel.getUserId());
    	}
    	
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        
        allpayForgotVeriflyPinViewModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
        
        searchBaseWrapper.setBasePersistableModel(allpayForgotVeriflyPinViewModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        if(linkedHashMap.isEmpty()){
            linkedHashMap.put("firstName", SortingOrder.ASC);
            linkedHashMap.put("lastName", SortingOrder.ASC);
        }
        
        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        searchBaseWrapper = this.allpayForgotVeriflyPinManager.searchForgotVeriflyPin(
                searchBaseWrapper);
        
        httpServletRequest.setAttribute("fromAppUserId", eappUserId);
        if(userInfoListViewModel != null){
        	allpayForgotVeriflyPinViewModel.setFirstName(userInfoListViewModel.getFirstName());
        	allpayForgotVeriflyPinViewModel.setLastName(userInfoListViewModel.getLastName());
        	allpayForgotVeriflyPinViewModel.setNic(userInfoListViewModel.getNic());
        	userInfoListViewModel = null;
        }
        
        return new ModelAndView(getSuccessView(),
                                "forgotVeriflyPinViewModelList",
                                searchBaseWrapper.getCustomList().
                                getResultsetList());
	}



	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}


	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}


	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}


	public AllpayForgotVeriflyPinManager getAllpayForgotVeriflyPinManager() {
		return allpayForgotVeriflyPinManager;
	}


	public void setAllpayForgotVeriflyPinManager(AllpayForgotVeriflyPinManager allpayForgotVeriflyPinManager) {
		this.allpayForgotVeriflyPinManager = allpayForgotVeriflyPinManager;
	}

}

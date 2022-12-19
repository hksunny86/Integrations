package com.inov8.microbank.webapp.action.portal.delinkrelinkpaymentmode;

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
import com.inov8.microbank.common.model.portal.delinkrelinkpaymentmodemodule.DelinkRelinkPaymentModeVieModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.delinkrelinkpaymentmode.DelinkRelinkPaymentModeManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;

public class DelinkRelinkPaymentModeController extends BaseFormSearchController {

    private DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager;
    private ReferenceDataManager referenceDataManager;
    private LinkPaymentModeManager linkPaymentModeManager;
	
	public DelinkRelinkPaymentModeController() {
		// TODO Auto-generated constructor stub
        super.setCommandName("delinkRelinkPaymentModeVieModel");
        super.setCommandClass(DelinkRelinkPaymentModeVieModel.class);
	}
	
	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		// TODO Auto-generated method stub

		Map<String,Object> referenceMap = new HashMap<>();
		return referenceMap;
	}

    protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object object,
            PagingHelperModel pagingHelperModel,
            LinkedHashMap<String,SortingOrder> linkedHashMap) throws
       Exception {	

        DelinkRelinkPaymentModeVieModel delinkRelinkPaymentModeVieModel = (
        		DelinkRelinkPaymentModeVieModel) object;
    	
    	BaseWrapper baseWrapper = new BaseWrapperImpl();
    	String eappUserId = ServletRequestUtils.getStringParameter(httpServletRequest, "fromAppUserId");
    	Long appUserId = null;
    	UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
    	if(eappUserId != null && eappUserId.trim().length() > 0 ){
    		appUserId = new Long(EncryptionUtil.decryptWithDES(eappUserId));
    		
    		userInfoListViewModel.setAppUserId(appUserId);
    		baseWrapper.setBasePersistableModel(userInfoListViewModel);
    		baseWrapper = linkPaymentModeManager.loadUserInfoListViewByPrimaryKey(baseWrapper);
    		userInfoListViewModel = (UserInfoListViewModel)baseWrapper.getBasePersistableModel();
    		
    		delinkRelinkPaymentModeVieModel.setMfsId(userInfoListViewModel.getUserId());
    	}    	
    	
    	
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        delinkRelinkPaymentModeVieModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
        searchBaseWrapper.setBasePersistableModel(delinkRelinkPaymentModeVieModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);        
       
        if(linkedHashMap.isEmpty()){
        	linkedHashMap.put("accountNick", SortingOrder.ASC);
        }
        
        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        searchBaseWrapper = this.delinkRelinkPaymentModeManager.searchDelinkRelinkPaymentMode(
                searchBaseWrapper);

        httpServletRequest.setAttribute("fromAppUserId", eappUserId);
        
        if(userInfoListViewModel != null){
        	delinkRelinkPaymentModeVieModel.setFirstName(userInfoListViewModel.getFirstName());
        	delinkRelinkPaymentModeVieModel.setLastName(userInfoListViewModel.getLastName());
        	delinkRelinkPaymentModeVieModel.setNic(userInfoListViewModel.getNic());
        	userInfoListViewModel = null;
        }        
        
        
        return new ModelAndView(getSuccessView(),
                                "delinkRelinkPaymentModeModelList",
                                searchBaseWrapper.getCustomList().
                                getResultsetList());
	}

	public void setDelinkRelinkPaymentModeManager(
			DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager) {
		this.delinkRelinkPaymentModeManager = delinkRelinkPaymentModeManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}	
	

}

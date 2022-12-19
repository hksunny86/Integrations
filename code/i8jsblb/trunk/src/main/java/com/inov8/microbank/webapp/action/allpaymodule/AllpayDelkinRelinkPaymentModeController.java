package com.inov8.microbank.webapp.action.allpaymodule;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AllpayDeReLinkListViewModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.allpaymodule.AllpayDelinkRelinkPaymentModeManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.verifly.common.des.EncryptionHandler;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllpayDelkinRelinkPaymentModeController extends BaseFormSearchController {

    private AllpayDelinkRelinkPaymentModeManager allpayDelinkRelinkPaymentModeManager;
    private ReferenceDataManager referenceDataManager;
    private LinkPaymentModeManager linkPaymentModeManager;
    //Autowired
	private EncryptionHandler encryptionHandler;

	public AllpayDelkinRelinkPaymentModeController() {
		// TODO Auto-generated constructor stub
        super.setCommandName("delinkRelinkPaymentModeVieModel");
        super.setCommandClass(AllpayDeReLinkListViewModel.class);
	}

	private List<AllpayDeReLinkListViewModel> decryptAccountNumbers( List<AllpayDeReLinkListViewModel> allpayDeReLinkListViewModels )
	{
		for( AllpayDeReLinkListViewModel allpayDeReLinkListViewModel : allpayDeReLinkListViewModels )
		{
			String encryptedAccountNo = allpayDeReLinkListViewModel.getAccountNo();
			if( encryptedAccountNo != null )
			{
				try
				{
					allpayDeReLinkListViewModel.setAccountNo( encryptionHandler.decrypt(encryptedAccountNo) );
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		}
		return allpayDeReLinkListViewModels;
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

    	AllpayDeReLinkListViewModel allpayDeReLinkListViewModel = (
    			AllpayDeReLinkListViewModel) object;
    	
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
    		
    		allpayDeReLinkListViewModel.setMfsId(userInfoListViewModel.getUserId());
    	}    	
    	
    	
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        allpayDeReLinkListViewModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
        searchBaseWrapper.setBasePersistableModel(allpayDeReLinkListViewModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);        
       
        if(linkedHashMap.isEmpty()){
        	linkedHashMap.put("accountNick", SortingOrder.ASC);
        }
        
        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        searchBaseWrapper = this.allpayDelinkRelinkPaymentModeManager.searchDelinkRelinkPaymentMode(
                searchBaseWrapper);

        httpServletRequest.setAttribute("fromAppUserId", eappUserId);
        
        if(userInfoListViewModel != null){
        	allpayDeReLinkListViewModel.setFirstName(userInfoListViewModel.getFirstName());
        	allpayDeReLinkListViewModel.setLastName(userInfoListViewModel.getLastName());
        	allpayDeReLinkListViewModel.setNic(userInfoListViewModel.getNic());
        	userInfoListViewModel = null;
        }        
        
        List<AllpayDeReLinkListViewModel> allpayDeReLinkListViewModels = (List<AllpayDeReLinkListViewModel>) searchBaseWrapper.getCustomList().getResultsetList();
        //allpayDeReLinkListViewModels = decryptAccountNumbers( allpayDeReLinkListViewModels );
        return new ModelAndView(getSuccessView(), "delinkRelinkPaymentModeModelList", allpayDeReLinkListViewModels);
	}

	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}

	public AllpayDelinkRelinkPaymentModeManager getAllpayDelinkRelinkPaymentModeManager() {
		return allpayDelinkRelinkPaymentModeManager;
	}

	public void setAllpayDelinkRelinkPaymentModeManager(AllpayDelinkRelinkPaymentModeManager allpayDelinkRelinkPaymentModeManager) {
		this.allpayDelinkRelinkPaymentModeManager = allpayDelinkRelinkPaymentModeManager;
	}

	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}	

}

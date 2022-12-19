package com.inov8.microbank.webapp.action.portal.opconcernsmodule;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.portal.concernmodule.AppUserConcernPartnerViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class OpConcernRulesController extends AbstractController{
    private ConcernManager concernManager;
	private ReferenceDataManager referenceDataManager;    
    

	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
	{	
		AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = new AppUserConcernPartnerViewModel();
		appUserConcernPartnerViewModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(appUserConcernPartnerViewModel);
		baseWrapper = concernManager.searchAppUserConcernPartnerViewByPrimaryKey(baseWrapper);
		appUserConcernPartnerViewModel = (AppUserConcernPartnerViewModel)baseWrapper.getBasePersistableModel();
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.putObject("partnerId", appUserConcernPartnerViewModel.getConcernPartnerId());
		searchBaseWrapper = concernManager.searchAllOtherPartners(searchBaseWrapper);
		CustomList clist = searchBaseWrapper.getCustomList();
		
		Map partnerMap = new HashMap();
//		
//		ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
//		concernPartnerModel.setActive(true);
//	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
//	    		concernPartnerModel, "name", SortingOrder.ASC);
//		
//	    referenceDataWrapper.setBasePersistableModel(concernPartnerModel);
//	    try
//	    {
//	      referenceDataManager.getReferenceData(referenceDataWrapper);
//	    }
//	    catch (Exception e)
//	    {
//
//	    }
//	    List<ConcernPartnerModel> concernPartnerList = null;
//	    if (referenceDataWrapper.getReferenceDataList() != null)
//	    {
//	    	concernPartnerList = referenceDataWrapper.getReferenceDataList();
//	    }
	    partnerMap.put("concernPartnerList", clist.getResultsetList());		
		
		
		return new ModelAndView("p_opconcernrules",partnerMap);
	}


	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
}

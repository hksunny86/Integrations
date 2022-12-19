package com.inov8.microbank.webapp.action.portal.opconcernsmodule;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.portal.concernmodule.AppUserConcernPartnerViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernPartnersRuleViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class OpConcernRulesFormController extends AdvanceFormController{

	private ConcernManager concernManager;
	private ReferenceDataManager referenceDataManager;

	public OpConcernRulesFormController() {
		setCommandName("concernPartnersRuleViewModel");
	    setCommandClass(ConcernPartnersRuleViewModel.class);
	}


	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

		
		return new ConcernPartnersRuleViewModel();
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception {
		
		String ruleForm = ServletRequestUtils.getStringParameter(request, "ruleForm");
		Map<String,Object> referenceDataMap = new HashMap<String,Object>();
		if(ruleForm == null || ruleForm.trim().length() < 1  ){
			Long partnerId = ServletRequestUtils.getLongParameter(request, "partnerId");
			
			ConcernPartnersRuleViewModel concernPartnersRuleViewModel = new ConcernPartnersRuleViewModel();
			concernPartnersRuleViewModel.setPartnerId(partnerId);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(concernPartnersRuleViewModel);
			concernManager.createPartnerAssociationReferenceData(baseWrapper);
			
		
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(concernPartnersRuleViewModel);
			searchBaseWrapper = concernManager.loadConcernPartnerRules(searchBaseWrapper);
			CustomList<ConcernPartnersRuleViewModel> partnerList = searchBaseWrapper.getCustomList();
			
			baseWrapper = new BaseWrapperImpl();
			ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
			concernPartnerModel.setConcernPartnerId(partnerId);
			baseWrapper.setBasePersistableModel(concernPartnerModel);
			baseWrapper = concernManager.loadConcernPartnerByPrimaryKey(baseWrapper);
			concernPartnerModel = (ConcernPartnerModel)baseWrapper.getBasePersistableModel();
			
			request.setAttribute("partnerName", concernPartnerModel.getName());
			request.setAttribute("partnerList1", partnerList.getResultsetList());
			
			//load current user partner id
			AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = new AppUserConcernPartnerViewModel();
			appUserConcernPartnerViewModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(appUserConcernPartnerViewModel);
			baseWrapper = concernManager.searchAppUserConcernPartnerViewByPrimaryKey(baseWrapper);
			appUserConcernPartnerViewModel = (AppUserConcernPartnerViewModel)baseWrapper.getBasePersistableModel();
			
			request.setAttribute("currentPartnerId", appUserConcernPartnerViewModel.getConcernPartnerId());
			
			referenceDataMap.put("partnerList", partnerList);
		}
		
		return referenceDataMap;
	}
    
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		return new ModelAndView(new RedirectView("p_opconcernrules.html?actionId=2"));	
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		
		ConcernPartnersRuleViewModel concernPartnersRuleViewModel = (ConcernPartnersRuleViewModel)obj;
		String partnersAssociation[] = req.getParameterValues("isPartnerAssociated");
		String partnerId = req.getParameter("apartnerId");

		
		BaseWrapper baseWrapper = new BaseWrapperImpl(); 
		baseWrapper.putObject("partnersAssociation", partnersAssociation);
		baseWrapper.putObject("partnerId", partnerId);
		String msg = "";
		try{
			concernManager.updatePartnerAssociationsRole(baseWrapper);
			msg = "Record is saved successfully.";
		}catch(Exception e){
			 msg = "An error occurred while trying to associate partners. Please try again.";			
			e.printStackTrace();
		}
	

		 saveMessage(req, msg);
		// return super.showForm(request, response, errors);
		 return new ModelAndView(new RedirectView("p_opconcernrules.html?actionId=2"));		
	}


	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
    
	
}

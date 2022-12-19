package com.inov8.microbank.webapp.action.portal.concernmodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ConcernCategoryModel;
import com.inov8.microbank.common.model.ConcernModel;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.ConcernPriorityModel;
import com.inov8.microbank.common.model.portal.concernmodule.AppUserConcernPartnerViewModel;
import com.inov8.microbank.common.util.ConcernsConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class CreateConcernFormController extends AdvanceFormController{
    
	private ConcernManager concernManager;
	private ReferenceDataManager referenceDataManager;
	
	public CreateConcernFormController() {
		setCommandName("concernModel");
		setCommandClass(ConcernModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		ConcernModel concernModel = new ConcernModel();
		return concernModel;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {
		/** 
	     * code fragment to load reference data for ConcernPartner Type
	     */
	    ConcernCategoryModel concernCategoryModel = new ConcernCategoryModel();
	    concernCategoryModel.setActive(true);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernCategoryModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ConcernCategoryModel> concernCategoryModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	concernCategoryModelList = referenceDataWrapper.getReferenceDataList();
	    } 
	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("concernCategoryModelList", concernCategoryModelList);

	    /**
	     * code fragment to load reference data for Concern Priority
	     */
	    ConcernPriorityModel concernPriorityModel = new ConcernPriorityModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernPriorityModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ConcernPriorityModel> concernPriorityModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	concernPriorityModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("concernPriorityModelList", concernPriorityModelList);
	    
	    /**
	     * code fragment to load reference data for Concern Partner
	     */
	    ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernPartnerModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ConcernPartnerModel> concernPartnerModelList = null;
	    
	    BaseWrapper baseWrapper = new BaseWrapperImpl();
	    AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = new AppUserConcernPartnerViewModel();
	    appUserConcernPartnerViewModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
	    baseWrapper.setBasePersistableModel(appUserConcernPartnerViewModel);
	    baseWrapper = this.concernManager.searchAppUserConcernPartnerViewByPrimaryKey(baseWrapper);
	    appUserConcernPartnerViewModel = (AppUserConcernPartnerViewModel)baseWrapper.getBasePersistableModel();
	    
	    baseWrapper.putObject(ConcernsConstants.KEY_PARTNER_ID, appUserConcernPartnerViewModel.getConcernPartnerId());
	    baseWrapper = this.concernManager.getPartnerPartners(baseWrapper);
	    CustomList<ConcernPartnerModel> list = (CustomList)baseWrapper.getObject("clist");
	    
	    
	    referenceDataMap.put("concernPartnerModelList", list.getResultsetList());

	    return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		System.out.println("In the onCreate");
		Date nowDate = new Date();
		ConcernModel concernModel = (ConcernModel)model;
		
		AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = new AppUserConcernPartnerViewModel();
		appUserConcernPartnerViewModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(appUserConcernPartnerViewModel);
		this.concernManager.searchConcernPartner();    ////// -------------------------
		baseWrapper = this.concernManager.searchAppUserConcernPartnerViewByPrimaryKey(baseWrapper);
		appUserConcernPartnerViewModel = (AppUserConcernPartnerViewModel)baseWrapper.getBasePersistableModel();
		concernModel.setInitiatorPartnerId(appUserConcernPartnerViewModel.getConcernPartnerId());
		concernModel.setConcernStatusId(ConcernsConstants.STATUS_OPEN);
		concernModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		concernModel.setCreatedOn(nowDate);
		concernModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		concernModel.setUpdatedOn(nowDate);
		concernModel.setActive(true);
		baseWrapper.setBasePersistableModel(concernModel);
		try{
			baseWrapper = this.concernManager.createConcern(baseWrapper);
			concernModel = (ConcernModel)baseWrapper.getBasePersistableModel();
			this.saveMessage(req, super.getText("concern.concernRaiseSuccessfull", new Object[]{baseWrapper.getObject("recepientPartnerName"),concernModel.getConcernCode()}, req.getLocale()));
		}catch(FrameworkCheckedException fce){
			super.saveMessage(req, super.getText("concern.concernRaiseFailure", req.getLocale()));
			super.showForm(req, res, errors);
			fce.printStackTrace();
		}catch(Exception fce){
			super.saveMessage(req, MessageUtil.getMessage("6075"));
			super.showForm(req, res, errors);
			fce.printStackTrace();
		}
		
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_myconcerns.html?actionId=2&_formSubmit=true"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}

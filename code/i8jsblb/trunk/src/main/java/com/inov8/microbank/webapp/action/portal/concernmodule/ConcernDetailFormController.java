package com.inov8.microbank.webapp.action.portal.concernmodule;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ConcernModel;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.portal.concernmodule.AppUserConcernPartnerViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernHistoryListViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernListViewModel;
import com.inov8.microbank.common.util.ConcernsConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class ConcernDetailFormController extends AdvanceFormController{
    
	private ConcernManager concernManager;
    private ReferenceDataManager referenceDataManager;
	
    public ConcernDetailFormController() {
		setCommandName("concernListViewModel");
		setCommandClass(ConcernListViewModel.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
	   Long concernId = ServletRequestUtils.getLongParameter(req,"concernId");   
	   Long id = null;
	   BaseWrapper baseWrapper = new BaseWrapperImpl();
	   ConcernListViewModel concernListViewModel = new ConcernListViewModel();
	   if (null != concernId )
	   {
//	       id = new Long(EncryptionUtil.decryptWithDES(eIssueId));  
	       
	      concernListViewModel.setConcernId(concernId);
	      baseWrapper.setBasePersistableModel(concernListViewModel);
	      baseWrapper = this.concernManager.loadConcern(baseWrapper);
	      concernListViewModel = (ConcernListViewModel)baseWrapper.getBasePersistableModel(); 
//	       httpServletRequest.setAttribute("transactionCode", ServletRequestUtils.getStringParameter(httpServletRequest, "transactionCode"));
	    }
	    return concernListViewModel;
	}
	
	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {
	  	String concernCode  = ServletRequestUtils.getStringParameter(req, "concernCode");
	  	String concernPage = ServletRequestUtils.getStringParameter(req, ConcernsConstants.KEY_CONCERN_PAGE);

		Map referenceDataMap = new HashMap();
		ConcernHistoryListViewModel concernHistoryListViewModel = new ConcernHistoryListViewModel();
		concernHistoryListViewModel.setConcernCode(concernCode);
		
		AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = new AppUserConcernPartnerViewModel();
		appUserConcernPartnerViewModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(appUserConcernPartnerViewModel);
		this.concernManager.searchAppUserConcernPartnerViewByPrimaryKey(baseWrapper);
		appUserConcernPartnerViewModel = (AppUserConcernPartnerViewModel)baseWrapper.getBasePersistableModel();
		if(concernPage.equals(ConcernsConstants.PAGE_MY_CONCERN)){
			concernHistoryListViewModel.setInitiatorPartnerId(appUserConcernPartnerViewModel.getConcernPartnerId());
		}
		else{
			concernHistoryListViewModel.setRecipientPartnerId(appUserConcernPartnerViewModel.getConcernPartnerId());
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		LinkedHashMap sortingOrder = new LinkedHashMap<String,SortingOrder>();
      	sortingOrder.put("historyUpdatedOn", SortingOrder.DESC);

		searchBaseWrapper.setSortingOrderMap(sortingOrder);
		searchBaseWrapper.setBasePersistableModel(concernHistoryListViewModel);
		searchBaseWrapper = this.concernManager.searchConcernsHistory(searchBaseWrapper);
		List concernHistoryList = searchBaseWrapper.getCustomList().getResultsetList();
	    
	    referenceDataMap.put("concernHistoryList", concernHistoryList);
	    

	    //load raised to concern pertners data
		ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernPartnerModel, "name", SortingOrder.ASC);
		
		referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernPartnerModel);
		
	    referenceDataWrapper.setBasePersistableModel(concernPartnerModel);
	    try
	    {
	      referenceDataManager.getReferenceData(referenceDataWrapper);
	    }
	    catch (Exception e)
	    {

	    }
	    List<ConcernPartnerModel> concernPartnerList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	concernPartnerList = referenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("concernPartnerList", concernPartnerList);
	    
	    
        return referenceDataMap;

	}
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		
		return null;
	}
	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		String concernPage = ServletRequestUtils.getStringParameter(req, ConcernsConstants.KEY_CONCERN_PAGE);
		Date nowDate = new Date();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		ConcernListViewModel concernListViewModel = (ConcernListViewModel)model;
		ConcernModel concernModel = new ConcernModel();
		concernModel.setPrimaryKey(concernListViewModel.getConcernId());
		baseWrapper.setBasePersistableModel(concernModel);
		baseWrapper = this.concernManager.loadConcernModel(baseWrapper);
		concernModel = (ConcernModel)baseWrapper.getBasePersistableModel();
		concernModel.setComments(concernListViewModel.getComments());
		String msgSuccess, msgFailure;
		if(concernListViewModel.getTitle().equals(ConcernsConstants.ACTION_RAISE_AGAIN)){
			concernModel.setConcernStatusId(ConcernsConstants.STATUS_INPROCESS);
			msgFailure = super.getText("concern.concernRaiseAgainFailure", req.getLocale());
			msgSuccess = super.getText("concern.concernRaiseAgainSuccessfull", new Object[]{concernModel.getConcernCode(), concernModel.getRecipientPartnerIdConcernPartnerModel().getName()},req.getLocale());
		}
		else if(concernListViewModel.getTitle().equals(ConcernsConstants.ACTION_RESOLVE)){
			concernModel.setConcernStatusId(ConcernsConstants.STATUS_CLOSED);
			msgFailure = super.getText("concern.concernResolveFailure", req.getLocale());
			msgSuccess = super.getText("concern.concernResolveSuccessfull", new Object[]{concernModel.getConcernCode(), concernModel.getRecipientPartnerIdConcernPartnerModel().getName()},req.getLocale());
		}
		else{
			concernModel.setConcernStatusId(ConcernsConstants.STATUS_REPLIED);
			msgFailure = super.getText("concern.concernReplyFailure", req.getLocale());
			msgSuccess = super.getText("concern.concernReplySuccessfull", new Object[]{concernModel.getConcernCode(), concernModel.getInitiatorPartnerIdConcernPartnerModel().getName()},req.getLocale());;
		}
		concernModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		concernModel.setUpdatedOn(nowDate);
		baseWrapper.setBasePersistableModel(concernModel);
		try{
			baseWrapper = this.concernManager.updateConcernModel(baseWrapper);
		}catch(FrameworkCheckedException fce){
			this.saveMessage(req, msgFailure);
			return new ModelAndView(new RedirectView("p_concerndetailform.html?actionId=1&concerdCode="+concernListViewModel.getConcernCode()+"&concernId="+concernListViewModel.getConcernId()+"&concernPage="+concernPage));
		}
		catch(Exception fce){
			this.saveMessage(req, MessageUtil.getMessage("6075"));
			return new ModelAndView(new RedirectView("p_concerndetailform.html?actionId=1&concerdCode="+concernListViewModel.getConcernCode()+"&concernId="+concernListViewModel.getConcernId()+"&concernPage="+concernPage));
		}
		
		this.saveMessage(req, msgSuccess);
		return new ModelAndView(new RedirectView("p_concerndetailform.html?actionId=2&concernCode="+concernModel.getConcernCode()+"&concernId="+concernModel.getConcernId()+"&concernPage="+concernPage));
	}
	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
    
}

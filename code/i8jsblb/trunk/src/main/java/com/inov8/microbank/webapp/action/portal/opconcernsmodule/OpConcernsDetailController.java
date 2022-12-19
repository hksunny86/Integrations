package com.inov8.microbank.webapp.action.portal.opconcernsmodule;

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

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ConcernModel;
import com.inov8.microbank.common.model.portal.concernmodule.AppUserConcernPartnerViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernInActPartnerViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernListViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernOpHistoryListViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernOpRaisedToListViewModel;
import com.inov8.microbank.common.util.ConcernsConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class OpConcernsDetailController extends AdvanceFormController
{
  private ConcernManager concernManager;
  private ReferenceDataManager referenceDataManager;
  
  public OpConcernsDetailController()
  {
    setCommandName("concernListViewModel");
    setCommandClass(ConcernListViewModel.class);
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
	  	String concernCode  = ServletRequestUtils.getStringParameter(httpServletRequest, "concernCode");

		Map referenceDataMap = new HashMap();
		ConcernOpHistoryListViewModel concernOpHistoryListViewModel = new ConcernOpHistoryListViewModel();
		concernOpHistoryListViewModel.setConcernCode(concernCode);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		LinkedHashMap sortingOrder = new LinkedHashMap<String,SortingOrder>();
      	sortingOrder.put("historyUpdatedOn", SortingOrder.DESC);

		searchBaseWrapper.setSortingOrderMap(sortingOrder);
		searchBaseWrapper.setBasePersistableModel(concernOpHistoryListViewModel);
		searchBaseWrapper = this.concernManager.searchOpConcernsHistory(searchBaseWrapper);
		List concernHistoryList = searchBaseWrapper.getCustomList().getResultsetList();
	    
	    referenceDataMap.put("concernHistoryList", concernHistoryList);
	    

	    //load partner data
	    AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = new AppUserConcernPartnerViewModel();
	    appUserConcernPartnerViewModel.setAppUserId( UserUtils.getCurrentUser().getAppUserId());
	    BaseWrapper baseWrapper = new BaseWrapperImpl();
	    baseWrapper.setBasePersistableModel(appUserConcernPartnerViewModel);
	    
	    baseWrapper = concernManager.searchAppUserConcernPartnerViewByPrimaryKey(baseWrapper);
	    appUserConcernPartnerViewModel = (AppUserConcernPartnerViewModel)baseWrapper.getBasePersistableModel();
	    
	    searchBaseWrapper = new SearchBaseWrapperImpl();
	    ConcernOpRaisedToListViewModel concernOpRaisedToListViewModel = new ConcernOpRaisedToListViewModel();
	    concernOpRaisedToListViewModel.setConcernCode(concernCode);
	    searchBaseWrapper.setBasePersistableModel(concernOpRaisedToListViewModel);
	    searchBaseWrapper = concernManager.searchOpPartners(searchBaseWrapper );
	    
	    CustomList clist = searchBaseWrapper.getCustomList();
	    List partnerList = clist.getResultsetList();
	    
	    referenceDataMap.put("concernPartnerList", partnerList);
	    httpServletRequest.setAttribute("attribCurrentUserPartnerId", appUserConcernPartnerViewModel.getConcernPartnerId());
	    
    return referenceDataMap;
  }

  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
	Long concernId = ServletRequestUtils.getLongParameter(httpServletRequest, "concernId"); 
	String concernCode  = ServletRequestUtils.getStringParameter(httpServletRequest, "concernCode");
    Long id = null;

    BaseWrapper baseWrapper = new BaseWrapperImpl();
    ConcernListViewModel concernListViewModel = new ConcernListViewModel();
     if (null != concernId )
     {
//       id = new Long(EncryptionUtil.decryptWithDES(eIssueId));  
       
       concernListViewModel.setConcernId(concernId);
       baseWrapper.setBasePersistableModel(concernListViewModel);
       baseWrapper = this.concernManager.loadConcern(baseWrapper);
       concernListViewModel = (ConcernListViewModel)baseWrapper.getBasePersistableModel(); 
       
 //      if(concernListViewModel.getConcernStatusId().longValue() == ConcernsConstants.STATUS_REPLIED.longValue()){
	       // check if reply action required or not
	       baseWrapper = new BaseWrapperImpl();
	       baseWrapper.putObject(ConcernsConstants.KEY_CONCERN_CODE, concernCode);
	       baseWrapper = this.concernManager.findIndirectActiveConcern(baseWrapper);
	       CustomList<ConcernInActPartnerViewModel> clist = (CustomList)baseWrapper.getObject("clist");
	       List <ConcernInActPartnerViewModel>list = clist.getResultsetList();
	       if(!list.isEmpty()){
	       	ConcernInActPartnerViewModel concernInActPartnerViewModel = list.get(0);
	       	httpServletRequest.setAttribute("activePartnerName", concernInActPartnerViewModel.getRecipientPartnerName());
	       	httpServletRequest.setAttribute("activePartnerStatusId", concernInActPartnerViewModel.getConcernStatusId());
	       }
//       }

     }
       return concernListViewModel;
  }

  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
                               bindException);
  }

  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
                               bindException);
  }

  /**
  *
  * @param request HttpServletRequest
  * @param response HttpServletResponse
  * @param command Object
  * @param errors BindException
  * @return ModelAndView
  * @throws Exception
  */
 private ModelAndView createOrUpdate(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object command,
                                     BindException errors) throws Exception
 {
	 String newComments = ServletRequestUtils.getStringParameter(request, "newComments");
	 String localAction = ServletRequestUtils.getStringParameter(request, "localAction");
	 String paramRecipientPartnerId = ServletRequestUtils.getStringParameter(request, "paramRecipientPartnerId");
	 String paramInitiatorPartnerId = ServletRequestUtils.getStringParameter(request, "paramInitiatorPartnerId");
	 
	 BaseWrapper baseWrapper = new BaseWrapperImpl();
	 ConcernListViewModel tmpconcernListViewModel = (ConcernListViewModel) command;
	 
	 ConcernModel concernModel = new ConcernModel();
	 concernModel.setComments(newComments);
	 concernModel.setConcernCode(tmpconcernListViewModel.getConcernCode());
	 String msg = "";
	 
	 //send reply to concern creator
	 if(ConcernsConstants.ACTION_SEND_TO_CREATOR.equals(localAction)){
		 concernModel.setConcernStatusId(ConcernsConstants.STATUS_REPLIED);
		 
		 baseWrapper.setBasePersistableModel(concernModel);
		 baseWrapper.putObject(ConcernsConstants.KEY_IS_CREATOR, "true");
		 try{		 
			 concernManager.updateConcernForReply(baseWrapper);
			 msg = super.getText("concern.sendToCreatorSuccessfull", new Object[]{concernModel.getConcernCode(),baseWrapper.getObject("initiatorPartnerName")}, request.getLocale());
		 }catch(Exception e){
			msg = super.getText("concern.sendToCreatorFailure", request.getLocale());
			e.printStackTrace();
		 }
		 //forward concern to selected partner
	 }else 	if(ConcernsConstants.ACTION_SEND_TO_RESOLVER.equals(localAction)){

		 concernModel.setRecipientPartnerId(tmpconcernListViewModel.getRecipientPartnerId());
		 concernModel.setTitle(tmpconcernListViewModel.getTitle());
		 concernModel.setConcernStatusId(ConcernsConstants.STATUS_OPEN);
		 
		 baseWrapper.setBasePersistableModel(concernModel);
		 try{ 
			 baseWrapper = concernManager.updateConcernForResolver(baseWrapper);  
			 String isRaisedAgain = (String)baseWrapper.getObject("isRaisedAgain");
			 if("true".equals(isRaisedAgain)){
				 msg = super.getText("concern.sendAgainToResolvingPartnerSuccessfull", new Object[]{concernModel.getConcernCode(),baseWrapper.getObject("recepientPartnerName")}, request.getLocale());
			 }else{	 
				 msg = super.getText("concern.sendToResolvingPartnerSuccessfull", new Object[]{concernModel.getConcernCode(),baseWrapper.getObject("recepientPartnerName")}, request.getLocale());
			 }
		 
		 
		 }catch(Exception e){
			 msg = super.getText("concern.sendToResolvingPartnerFailure", request.getLocale());
		 }
		 //Raised again to current active partner
	  }else	if(ConcernsConstants.ACTION_RAISE_AGAIN.equals(localAction)){
		 concernModel.setConcernStatusId(ConcernsConstants.STATUS_INPROCESS);
		 baseWrapper.setBasePersistableModel(concernModel);
		 try{
			 concernManager.updateConcernForReply(baseWrapper); 
			 msg = super.getText("concern.concernRaiseAgainSuccessfull", new Object[]{concernModel.getConcernCode(),baseWrapper.getObject("recepientPartnerName")}, request.getLocale());
		 }catch(Exception e){
			 msg = super.getText("concern.concernRaiseAgainFailure", request.getLocale());
		 }
	 }
	 

	 saveMessage(request, msg);
	// return super.showForm(request, response, errors);
	 return new ModelAndView(new RedirectView("p_opconcernsdetail.html?actionId=2&concernCode="+tmpconcernListViewModel.getConcernCode()+"&concernId="+tmpconcernListViewModel.getConcernId()+"&paramInitiatorPartnerId="+paramInitiatorPartnerId+"&paramRecipientPartnerId="+paramRecipientPartnerId ));
 }

public void setConcernManager(ConcernManager concernManager) {
	this.concernManager = concernManager;
}

public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
	this.referenceDataManager = referenceDataManager;
}

 

}

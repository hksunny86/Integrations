package com.inov8.microbank.webapp.action.retailermodule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementListViewModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;
import com.inov8.microbank.common.util.CryptographyType;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.util.EncryptionUtil;

public class SearchTaggedAgentsController extends BaseSearchController{

	protected final transient Log log = LogFactory.getLog(getClass());
	private PortalOlaFacade portalOlaFacade;
	//AppUserManager appUserManager;
	AgentTaggingManager agentTaggingManager;
	AppUserManager appUserManager;
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public SearchTaggedAgentsController() {
	    super.setFilterSearchCommandClass(TaggedAgentsListViewModel.class);
	}
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object model, HttpServletRequest request,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		Map<String,Object> detailMap = new HashMap<>();
		Long pID= ServletRequestUtils.getLongParameter(request, "pID");
		String CDATE= ServletRequestUtils.getStringParameter(request, "cDate");
		String EDATE= ServletRequestUtils.getStringParameter(request, "eDate");
		request.setAttribute("cDate", CDATE);
		request.setAttribute("eDate", EDATE);
		TaggedAgentsListViewModel tAgentsListViewModel = new TaggedAgentsListViewModel();
		AgentTaggingViewModel parentAgentDetail = new AgentTaggingViewModel();
		List<TaggedAgentsListViewModel> resultList = new ArrayList<>();
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		
		if(pID !=null){
			parentAgentDetail.setPk(pID);
			sBaseWrapper.setBasePersistableModel(parentAgentDetail);
			sBaseWrapper =  portalOlaFacade.searchTaggingParentDetail(sBaseWrapper);
			if(sBaseWrapper.getBasePersistableModel() !=null){
			parentAgentDetail = (AgentTaggingViewModel) sBaseWrapper.getBasePersistableModel();
			detailMap.put("parentAgentDetail",parentAgentDetail);
			}
			
		   tAgentsListViewModel.setParentId(pID);
		   SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		   searchBaseWrapper.setBasePersistableModel(tAgentsListViewModel);
		  
		  searchBaseWrapper.setPagingHelperModel(pagingHelperModel); 
		  String eDate=null;
		  if(EDATE!=null || EDATE!="")
		  {
		  eDate = EDATE.substring(0, EDATE.length()-1).trim();
		  }
		  Date SDate=null ;
		  Date EDate=null ;
		  if(CDATE != ""){
	  SDate = PortalDateUtils.parseStringAsDate(CDATE, PortalDateUtils.SHORT_DATE_TIME_FORMAT);
	  SDate.setHours(0);
	 
		  }
		  if(eDate.length() > 0){
	   EDate = PortalDateUtils.parseStringAsDate(EDATE, PortalDateUtils.SHORT_DATE_TIME_FORMAT);
	   EDate.setHours(23);
	   EDate.setMinutes(59);
	   EDate.setSeconds(59);
		  }
		  
	  DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("abc",SDate,EDate);

           searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
		  
		   searchBaseWrapper= this.portalOlaFacade.searchTaggedAgents(searchBaseWrapper);
		   if(searchBaseWrapper.getCustomList()!=null && searchBaseWrapper.getCustomList().getResultsetList()!=null) {
			   resultList = searchBaseWrapper.getCustomList().getResultsetList();
			   if(resultList.size()>0){
		        decryptTaggedAgentsListViewModels( resultList );
		        detailMap.put("taggedAgentsList",resultList);
			   }
		   }
		}
		return new ModelAndView("searchTaggedAgents",detailMap);
	}
	
	 private void decryptTaggedAgentsListViewModels(List<TaggedAgentsListViewModel> taggedAgentsListViewModels)
	    {
	        for( TaggedAgentsListViewModel taggedAgentsListViewModel : taggedAgentsListViewModels )
	        {
	        	
	        	try
	        	{
	        		AppUserModel appUserModel = new AppUserModel();
	        		Long appUserTypeId = null;
		        	BaseWrapper bWrapper = new BaseWrapperImpl();
		        	if(taggedAgentsListViewModel.getPk() !=null){
		        		appUserModel.setAppUserId(taggedAgentsListViewModel.getPk());
		        		bWrapper.setBasePersistableModel(appUserModel);
		        		bWrapper = appUserManager.loadAppUser(bWrapper);
		        		
		        		if(bWrapper.getBasePersistableModel()!=null)
		        		{
		        			appUserModel = (AppUserModel) bWrapper.getBasePersistableModel();
		        			if(appUserModel.getAppUserTypeId()!=null)
		        			{
		        				appUserTypeId = appUserModel.getAppUserTypeId();
		        			}
		        			
		        		}
		        	}
            		EncryptionUtil.docryptFields( CryptographyType.DECRYPT, taggedAgentsListViewModel, "balance");
            		if(appUserTypeId == UserTypeConstantsInterface.HANDLER.longValue()){
            			
            			taggedAgentsListViewModel.setBalance("");
				
	        	}
               
	        	} 
            catch( Exception e )
            {
                log.error( e.getMessage(), e );
            }
	        	
	        }
	    }
	
	public void setPortalOlaFacade(PortalOlaFacade portalOlaFacade) {
		this.portalOlaFacade = portalOlaFacade;
	}

	public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
		this.agentTaggingManager = agentTaggingManager;
	}

}

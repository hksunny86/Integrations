package com.inov8.microbank.webapp.action.retailermodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class SearchAgentGroupController extends BaseFormSearchController {


	AgentTaggingManager agentTaggingManager;

	
	public SearchAgentGroupController() {
		setCommandName("agentTaggingViewModel");
	    setCommandClass(AgentTaggingViewModel.class);
	}
	
	
	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		Map referenceDataMap = new HashMap();
		AgentTaggingViewModel agentGroupViewModel = new AgentTaggingViewModel();
		List<AgentTaggingViewModel> agentGroupList = agentTaggingManager.getAllGroupTitle();
		
		referenceDataMap.put("agentGroupList",agentGroupList);
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, PagingHelperModel arg3,
			LinkedHashMap<String, SortingOrder> arg4) throws Exception {
		              		
		
		        String cDate = ServletRequestUtils.getStringParameter(arg0, "startDate");
		        String eDate = ServletRequestUtils.getStringParameter(arg0, "endDate");
		        arg0.setAttribute("CDATE", cDate);
		        arg0.setAttribute("EDATE", eDate);
		        List<AgentTaggingViewModel> resultList = new ArrayList();
		        AgentTaggingViewModel atViewModel = (AgentTaggingViewModel)arg2;
		        AgentTaggingViewModel agentTaggingViewModel = new AgentTaggingViewModel();
		        agentTaggingViewModel.setCnic(atViewModel.getCnic());
		        agentTaggingViewModel.setGroupTitle(atViewModel.getGroupTitle());
		        agentTaggingViewModel.setParrentId(atViewModel.getParrentId());
		        agentTaggingViewModel.setMobileNumber(atViewModel.getMobileNumber());
		        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		        searchBaseWrapper.setBasePersistableModel(agentTaggingViewModel);
		        searchBaseWrapper.setPagingHelperModel(arg3);
		        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", atViewModel.getStartDate(),atViewModel.getEndDate());
                searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
                
		        searchBaseWrapper = this.agentTaggingManager.sAgentTaggingViewModel(searchBaseWrapper);
		        if(searchBaseWrapper.getCustomList() !=null && searchBaseWrapper.getCustomList().getResultsetList() !=null)
		        {
		        	if(searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
		        	{
		        		resultList= searchBaseWrapper.getCustomList().getResultsetList();
		        		 return new ModelAndView(getSuccessView(), "agentTaggingGroupList",resultList);
		        	}
		        }
		       
		        	return new ModelAndView(getSuccessView(), "agentTaggingGroupList",
		                    resultList);        
	}
	
	public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
		this.agentTaggingManager = agentTaggingManager;
	}

}

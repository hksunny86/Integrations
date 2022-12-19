package com.inov8.microbank.webapp.action.agentgroup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.AgentClosingBalanceViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;

public class AgentGroupViewController extends BaseFormSearchController {
    
	@Autowired
    private AgentTaggingManager agentTaggingManager;

	public AgentGroupViewController() {
		 super.setCommandName("agentTaggingViewModel");
		 super.setCommandClass( AgentTaggingViewModel.class );
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	
	@Override
	protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception {
		
		Map<String,Object> referenceDataMap = new HashMap<String,Object>( );
	    return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap ) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

        AgentTaggingViewModel agentTaggingViewModel = (AgentTaggingViewModel) model;
		
		List<AgentTaggingViewModel> list = agentTaggingManager.searchAgentTaggingViewModel(agentTaggingViewModel);
		
		pagingHelperModel.setTotalRecordsCount(list.size());
				
		String successView = "p_searchAgentGroup";		
		
		return new ModelAndView(successView, "agentTaggingViewModelList", list );
	}

	public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
        this.agentTaggingManager = agentTaggingManager;
    }

}

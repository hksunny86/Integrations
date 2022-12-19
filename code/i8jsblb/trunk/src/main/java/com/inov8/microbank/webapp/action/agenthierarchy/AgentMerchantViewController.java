/**
 * 
 */
package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AgentMerchantDetailViewModel;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;

public class AgentMerchantViewController extends BaseFormSearchController
{
	private MfsAccountFacade mfsAccountFacade;

	public AgentMerchantViewController()
	{
		setCommandName("agentMerchantDetailViewModel");
		setCommandClass(AgentMerchantDetailViewModel.class);
	}

	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String, Object> refDataMap = new HashMap<String, Object>(2);
		return refDataMap;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object command, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		
		AgentMerchantDetailViewModel agentMerchantDetailViewModel = (AgentMerchantDetailViewModel) command;
		
		searchBaseWrapper.setBasePersistableModel(agentMerchantDetailViewModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn", agentMerchantDetailViewModel.getStartDate(),
				agentMerchantDetailViewModel.getEndDate());
		
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		if (sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = mfsAccountFacade.searchAgentMerchant(searchBaseWrapper);
		CustomList<AgentMerchantDetailViewModel> customList = searchBaseWrapper.getCustomList();
		List<AgentMerchantDetailViewModel> agentMerchantDetailModelList = null;
		if (customList != null)
		{
			agentMerchantDetailModelList = customList.getResultsetList();
		}
		return new ModelAndView(getFormView(), "agentMerchantModelList", agentMerchantDetailModelList);
	}
	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade)
	{
		this.mfsAccountFacade = mfsAccountFacade;
	}
}

package com.inov8.microbank.webapp.action.commissionstakeholdermodule;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.commissionmodule.CommStakeholderListViewModel;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public class CommissionStakeholderSearchController
	extends BaseSearchController
{
	
	private CommissionStakeholderManager commissionStakeholderManager;

	public CommissionStakeholderSearchController()
	{
		super.setFilterSearchCommandClass(CommStakeholderListViewModel.class);
	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request,HttpServletResponse response,
			Boolean activate) throws Exception
	{
		return null;
	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		return null;
	}

	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,Object model, HttpServletRequest request,
			LinkedHashMap sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		CommStakeholderListViewModel commStakeholderListViewModel = (CommStakeholderListViewModel) model;
		searchBaseWrapper.setBasePersistableModel(commStakeholderListViewModel);
		if( sortingOrderMap.isEmpty() )
			sortingOrderMap.put("commissionStakeholderName", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.commissionStakeholderManager.searchCommissionStakeholder(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "commissionStakeholderList",
				searchBaseWrapper.getCustomList().getResultsetList());
	}

	public void setCommissionStakeholderManager(CommissionStakeholderManager commissionStakeholderManager)
	{
		this.commissionStakeholderManager = commissionStakeholderManager;
	}
	
}

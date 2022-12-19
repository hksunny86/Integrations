package com.inov8.microbank.webapp.action.stakeholdermodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.stakeholdermodule.StakehldAcctMapListViewModel;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;

/**
 * @Date   19-11-2014
 * @author Hassan javaid
 * @version 1.0
 */

public class StakeHolderAccountsSearchController extends BaseSearchController
{
	
	private ReferenceDataManager referenceDataManager;
	private StakeholderBankInfoManager stakeholderBankInfoManager;

	public ReferenceDataManager getReferenceDataManager()
	{
		return referenceDataManager;
	}
	
	public StakeHolderAccountsSearchController() {
		super.setFilterSearchCommandClass(StakehldAcctMapListViewModel.class);
	}
	
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object model, HttpServletRequest request,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();		
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		StakehldAcctMapListViewModel stakehldAcctMapListViewModel = (StakehldAcctMapListViewModel)model;
		
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("blbAccTitle", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setBasePersistableModel(stakehldAcctMapListViewModel);
		searchBaseWrapper = this.stakeholderBankInfoManager.getStakehldAcctMappingList(searchBaseWrapper);
		
		return new ModelAndView(super.getSearchView(), "stakehldAcctMapList", searchBaseWrapper.getCustomList().getResultsetList());
	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
	
	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
}


package com.inov8.microbank.webapp.action.commissionstakeholdermodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.commissionmodule.CommShAcctsListViewModel;
import com.inov8.microbank.common.model.commissionmodule.ExtendedCommShAcctsListViewModel;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: </p>
 *
 * @author Fahad Tariq
 * @version 1.0
 */

public class CommissionStakeholderAccountsSearchController extends BaseFormSearchController
{
	
	private ReferenceDataManager referenceDataManager;
	private CommissionStakeholderManager commissionStakeholderManager;

	public ReferenceDataManager getReferenceDataManager()
	{
		return referenceDataManager;
	}
	
	public CommissionStakeholderAccountsSearchController() {
		setCommandName("extendedCommShAcctsListViewModel");
		setCommandClass(ExtendedCommShAcctsListViewModel.class);
	}
	
	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		
		CommissionStakeholderModel commissionStakeholderModel = new CommissionStakeholderModel();
		ReferenceDataWrapper  referenceDataWrapper = new ReferenceDataWrapperImpl(
				commissionStakeholderModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(commissionStakeholderModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<CommissionStakeholderModel> commissionStakeholderModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
	    {
			commissionStakeholderModelList = referenceDataWrapper.
	          getReferenceDataList();
			
			/*
			ArrayList<CommissionStakeholderModel> toBeRemovedList = new ArrayList<CommissionStakeholderModel>();
			
			CommissionStakeholderModel cshm = null;
			// To remove Agent Entry
			for(int i=0; i<commissionStakeholderModelList.size() ; i++){
				cshm = commissionStakeholderModelList.get(i);
				if(cshm.getName().contains("Agent") == true){
					toBeRemovedList.add(commissionStakeholderModelList.get(i));
				}
			}
			
			for(int j=0; j<toBeRemovedList.size(); j++){
				commissionStakeholderModelList.remove(toBeRemovedList.get(j));
			}
			*/
	    }
		Map referenceDataMap = new HashMap();
	    referenceDataMap.put("commissionStakeholderModelList",
	    		commissionStakeholderModelList);
		
	    return referenceDataMap;
	    
	}
	
	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		// TODO Auto-generated method stub
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		//TransactionDetInfoListViewModel transactionDetInfoListViewModel = (TransactionDetInfoListViewModel) object;
		ExtendedCommShAcctsListViewModel commShAcctsModel = (ExtendedCommShAcctsListViewModel)object;
		if(ServletRequestUtils.getLongParameter(request, "commStakeHolderId",-1L) != -1L)
		{
			commShAcctsModel.setShId(ServletRequestUtils.getLongParameter(request, "commStakeHolderId"));
		}
		
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("accountTitle", SortingOrder.ASC);
		}

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setBasePersistableModel((CommShAcctsListViewModel)commShAcctsModel);
		searchBaseWrapper = this.commissionStakeholderManager.searchCommissionStakeholderAccounts(searchBaseWrapper);
		
		List<CommShAcctsListViewModel> list = new ArrayList<CommShAcctsListViewModel>();
		if(searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null){			
			for (int i=0 ; i<searchBaseWrapper.getCustomList().getResultsetList().size(); i++){
				CommShAcctsListViewModel commShAcctsListViewModel = (CommShAcctsListViewModel)searchBaseWrapper.getCustomList().getResultsetList().get(i);
				if(commShAcctsListViewModel.getBankId() == 50050L){
					commShAcctsListViewModel.setAccountType("BB Account");
				}else if(commShAcctsListViewModel.getBankId() == 50110L){
					commShAcctsListViewModel.setAccountType("Core Account");
				} 
				list.add(commShAcctsListViewModel);
			}
		}
		CustomList<CommShAcctsListViewModel> customList = new CustomList<>(list);
//		pagingHelperModel.setTotalRecordsCount(customList.getResultsetList().size());
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setCustomList(customList);
		return new ModelAndView(super.getSuccessView(), "extendedCommShAcctsListViewModelList", searchBaseWrapper.getCustomList()
				.getResultsetList());

	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
	public CommissionStakeholderManager getCommissionStakeholderManager() {
		return commissionStakeholderManager;
	}
	public void setCommissionStakeholderManager(
			CommissionStakeholderManager commissionStakeholderManager) {
		this.commissionStakeholderManager = commissionStakeholderManager;
	}


}

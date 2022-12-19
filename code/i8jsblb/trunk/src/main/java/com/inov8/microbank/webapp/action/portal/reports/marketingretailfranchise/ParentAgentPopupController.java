package com.inov8.microbank.webapp.action.portal.reports.marketingretailfranchise;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.DistributorLvlRetContactViewModel;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

public class ParentAgentPopupController extends BaseSearchController{
	
	private DistributorLevelManager distributorLevelManager;
	
	public ParentAgentPopupController() {
	    super.setFilterSearchCommandClass(DistributorLvlRetContactViewModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, 
			                        Object model, 
			                        HttpServletRequest req, 
			                        LinkedHashMap<String, SortingOrder> sortingOrder) 
	throws Exception {
		DistributorLvlRetContactViewModel distributorLvlRetContactViewModel = (DistributorLvlRetContactViewModel)model; 
		Long distributorLevelId=ServletRequestUtils.getLongParameter(req, "agentLevelId");
		Long retailerId=ServletRequestUtils.getLongParameter(req, "retailerId",0);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.putObject("retailerId", retailerId);
		searchBaseWrapper.putObject("distributorId", distributorLevelId);

        if(sortingOrder.isEmpty()){
        	sortingOrder.put("parentAgentId", SortingOrder.ASC);
        }
        
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);	
		searchBaseWrapper.setSortingOrderMap(sortingOrder);
		
//		distributorLvlRetContactViewModel.setDistributorLevelId(distributorLevelId);
		searchBaseWrapper.setBasePersistableModel(distributorLvlRetContactViewModel);
		List<DistributorLvlRetContactViewModel> list =null;
		try {
			searchBaseWrapper = this.distributorLevelManager.getParentAgentsBydistributorLevelId(searchBaseWrapper);
			list = searchBaseWrapper.getCustomList().getResultsetList();
		} catch (FrameworkCheckedException e) {
			
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getMessage());
		}
			
		ModelAndView modelAndView = new ModelAndView(getSearchView(), "parentAgentsList",list);
		return modelAndView;
	}

	public void setDistributorLevelManager(
			DistributorLevelManager distributorLevelManager) {
		this.distributorLevelManager = distributorLevelManager;
	}

}

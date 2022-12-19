package com.inov8.microbank.webapp.action.stakeholdermodule;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.stakeholdermodule.StakehldBankInfoListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;

public class StakeholderBankInfoSearchController extends BaseSearchController {

	 private StakeholderBankInfoManager stakeholderBankInfoManager;
	
	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
	public StakeholderBankInfoSearchController()
	  {
	    super.setFilterSearchCommandClass(StakehldBankInfoListViewModel.class);
	  }
	
	protected ModelAndView onToggleActivate(HttpServletRequest request,
            HttpServletResponse response,Boolean activate) throws
Exception
{
Long id = ServletRequestUtils.getLongParameter(request,
                         "stakeholderBankInfoId");

if (null != id)
{
if (log.isDebugEnabled())
{
log.debug("id is not null....retrieving object from DB and then updating it");
}

BaseWrapper baseWrapper = new BaseWrapperImpl();
SearchBaseWrapper searchBaseWrapper =new SearchBaseWrapperImpl();
StakeholderBankInfoModel stakeholderBankInfoModel =new StakeholderBankInfoModel();
stakeholderBankInfoModel.setStakeholderBankInfoId(id);

searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(
		searchBaseWrapper);
//Set the active flag
stakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
stakeholderBankInfoModel.setActive(activate);
stakeholderBankInfoModel.setUpdatedOn(new Date());
stakeholderBankInfoModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
baseWrapper.setBasePersistableModel(stakeholderBankInfoModel);

try{
this.stakeholderBankInfoManager.createOrUpdateStakeholderBankInfo(baseWrapper);
}catch(FrameworkCheckedException fce){
this.saveMessage(request, "Record could not be updated.");
ModelAndView modelAndView = new ModelAndView(new RedirectView(
getSearchView() + ".html"));

return modelAndView;
}

}
ModelAndView modelAndView = new ModelAndView(new RedirectView(
getSearchView() + ".html"));

return modelAndView;

}

	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	    StakehldBankInfoListViewModel stakehldBankInfoListViewModel = (StakehldBankInfoListViewModel) model;
	    searchBaseWrapper.setBasePersistableModel(stakehldBankInfoListViewModel);
	    if(sortingOrderMap.isEmpty())
	        sortingOrderMap.put("name", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.stakeholderBankInfoManager.searchStakeHolderBankInfo(searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "stakeholderBankInfoModelList",
	                            searchBaseWrapper.getCustomList().
	                            getResultsetList());
	}

}

package com.inov8.microbank.webapp.action.portal.chargebackmodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.portal.chargebackmodule.ChargebackListViewModel;
import com.inov8.microbank.server.service.portal.chargebackmodule.ChargebackManager;

public class CustomerChargebackListController extends BaseSearchController
{
  private ChargebackManager chargebackManager;

  public CustomerChargebackListController(){
	super.setFilterSearchCommandClass(ChargebackListViewModel.class);
  }

  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model,
  		HttpServletRequest req, LinkedHashMap<String, SortingOrder> sortingOrder)
  		throws Exception {
  	
//	  ExtendedChargebackListViewModel extendedChargebackListViewModel = (ExtendedChargebackListViewModel) model;

	  String mfsId = ServletRequestUtils.getStringParameter(req, "searchKey");
	  if (null == mfsId || mfsId.trim().length() == 0){
		  mfsId = " ";
	  }

	  ChargebackListViewModel chargebackListViewModel = (ChargebackListViewModel) model;
//	  chargebackListViewModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
	  chargebackListViewModel.setMfsId(mfsId);
	  chargebackListViewModel.setRecipientMfsId(mfsId);
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	  searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	  searchBaseWrapper.setBasePersistableModel((ChargebackListViewModel)chargebackListViewModel);
	  
//	  DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
//			  "createdOn", extendedChargebackListViewModel.getStartDate(),
//	          extendedChargebackListViewModel.getEndDate());
//	  searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
	  if(sortingOrder.isEmpty()){
		  sortingOrder.put("createdOn", SortingOrder.DESC);
	  }
	  searchBaseWrapper.setSortingOrderMap(sortingOrder);
	  //searchBaseWrapper = this.chargebackManager.searchChargebackTransaction(searchBaseWrapper);
		
	  CustomList<ChargebackListViewModel> customList = this.chargebackManager.getChargebackTransactions(searchBaseWrapper);
	  searchBaseWrapper.setCustomList(customList);
	  pagingHelperModel.setTotalRecordsCount(customList.getResultsetList().size());

	  
	  ModelAndView modelAndView = new ModelAndView( getSearchView(), "transactionDetailModelList",
			  customList.getResultsetList() );
	  return modelAndView;
	
  }

  public void setChargebackManager(ChargebackManager chargebackManager) {
	  this.chargebackManager = chargebackManager;
  }

}

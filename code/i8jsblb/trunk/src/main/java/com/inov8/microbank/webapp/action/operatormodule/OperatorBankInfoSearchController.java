package com.inov8.microbank.webapp.action.operatormodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.operatorbankmodule.OperatorBankInfoListViewModel;
import com.inov8.microbank.server.service.operatormodule.OperatorBankInfoManager;


public class OperatorBankInfoSearchController extends BaseSearchController {
	private OperatorBankInfoManager operatorBankInfoManager ;

	public OperatorBankInfoSearchController()
	{
	  super.setFilterSearchCommandClass(OperatorBankInfoListViewModel.class);
	}

	public void setOperatorBankInfoManager(
			OperatorBankInfoManager operatorBankInfoManager) {
		this.operatorBankInfoManager = operatorBankInfoManager;
	}
		@Override
		protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		    OperatorBankInfoListViewModel 
		    operatorBankInfoListViewModel = (OperatorBankInfoListViewModel) model;
		    searchBaseWrapper.setBasePersistableModel(operatorBankInfoListViewModel);
		    if(sortingOrderMap.isEmpty())
		        sortingOrderMap.put("name", SortingOrder.ASC);
		    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		    searchBaseWrapper = this.operatorBankInfoManager.searchOperatorBankInfo(
		        searchBaseWrapper);

		    return new ModelAndView(super.getSearchView(), "operatorBankInfoModelList",
		                            searchBaseWrapper.getCustomList().getResultsetList());

		}

}

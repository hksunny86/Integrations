package com.inov8.microbank.webapp.action.portal.reportsmodule;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.smartmoneymodule.LinkedAccountsListViewModel;
import com.inov8.microbank.common.model.smartmoneymodule.LinkedAccountsListViewModelExtended;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.reportsmodule.LinkedAccountsReportManager;

public class LinkedAccountsReportController extends BaseFormSearchController{

	private LinkedAccountsReportManager linkedAccountsReportManager;
	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public LinkedAccountsReportController (){
		super.setCommandClass(LinkedAccountsListViewModelExtended.class);
		super.setCommandName("linkedAccountsListViewModelExtended");
	}
	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> arg4) throws Exception {
		//change this code for linkedaccountslistviewmodelextended;
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		LinkedAccountsListViewModelExtended linkedAccountsListViewModelExtended = (LinkedAccountsListViewModelExtended)object;
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", linkedAccountsListViewModelExtended.getFromDate(),
				linkedAccountsListViewModelExtended.getToDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		linkedAccountsListViewModelExtended.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		searchBaseWrapper.setBasePersistableModel((LinkedAccountsListViewModel)linkedAccountsListViewModelExtended);
		searchBaseWrapper = this.linkedAccountsReportManager.searchAccounts(searchBaseWrapper);
		return new ModelAndView(super.getSuccessView(),
				"linkedAccountsList", searchBaseWrapper.getCustomList()
						.getResultsetList());
	}

	public LinkedAccountsReportManager getLinkedAccountsReportManager() {
		return linkedAccountsReportManager;
	}

	public void setLinkedAccountsReportManager(
			LinkedAccountsReportManager linkedAccountsReportManager) {
		this.linkedAccountsReportManager = linkedAccountsReportManager;
	}

}

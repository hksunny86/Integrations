package com.inov8.microbank.server.service.reportsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.smartmoneymodule.LinkedAccountsListViewModel;
import com.inov8.microbank.server.dao.smartmoneymodule.LinkedAccountsListViewDAO;

public class LinkedAccountsReportManagerImpl implements
		LinkedAccountsReportManager {
	private LinkedAccountsListViewDAO linkedAccountsListViewDAO;
	public SearchBaseWrapper searchAccounts(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		CustomList<LinkedAccountsListViewModel>
	      list = this.linkedAccountsListViewDAO.findByExample( (
	          LinkedAccountsListViewModel)
	          searchBaseWrapper.
	          getBasePersistableModel(),
	          searchBaseWrapper.
	          getPagingHelperModel(),
	          searchBaseWrapper.
	          getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;
	}
	public LinkedAccountsListViewDAO getLinkedAccountsListViewDAO() {
		return linkedAccountsListViewDAO;
	}
	public void setLinkedAccountsListViewDAO(
			LinkedAccountsListViewDAO linkedAccountsListViewDAO) {
		this.linkedAccountsListViewDAO = linkedAccountsListViewDAO;
	}

}

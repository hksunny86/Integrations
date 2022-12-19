package com.inov8.microbank.server.service.mobilehistorymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.mobilehistorymodule.MobileHistoryListViewModel;
import com.inov8.microbank.server.dao.mobilehistorymodule.MobileHistoryListViewDAO;

public class MobileHistoryListViewManagerImpl implements MobileHistoryListViewManager {

	private MobileHistoryListViewDAO mobileHistoryListViewDAO;
	public SearchBaseWrapper searchMobileHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		CustomList<MobileHistoryListViewModel> list = this.mobileHistoryListViewDAO
		.findByExample((MobileHistoryListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
					searchBaseWrapper.setCustomList(list);
		
		return  searchBaseWrapper;
	}
	public void setMobileHistoryListViewDAO(
			MobileHistoryListViewDAO mobileHistoryListViewDAO) {
		this.mobileHistoryListViewDAO = mobileHistoryListViewDAO;
	}

}

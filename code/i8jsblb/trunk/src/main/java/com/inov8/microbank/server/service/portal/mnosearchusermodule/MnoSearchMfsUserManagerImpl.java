package com.inov8.microbank.server.service.portal.mnosearchusermodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.mnosearchusermodule.MnoSearchMfsUsrListViewModel;
import com.inov8.microbank.server.dao.portal.mnosearchusermodule.MnoSearchMfsUsrListViewDAO;

public class MnoSearchMfsUserManagerImpl implements MnoSearchMfsUserManager{
	
	private MnoSearchMfsUsrListViewDAO mnoSearchMfsUsrListViewDAO;
	
	public void setMnoSearchMfsUsrListViewDAO(
			MnoSearchMfsUsrListViewDAO mnoSearchMfsUsrListViewDAO) {
		this.mnoSearchMfsUsrListViewDAO = mnoSearchMfsUsrListViewDAO;
	}

	public SearchBaseWrapper searchMfsUser(
			SearchBaseWrapper searchBaseWrapper) {
		CustomList<MnoSearchMfsUsrListViewModel> list = this.mnoSearchMfsUsrListViewDAO
				.findByExample((MnoSearchMfsUsrListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
}

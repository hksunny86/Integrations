package com.inov8.microbank.server.service.portal.chargebackmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.chargebackmodule.ChargebackListViewModel;
import com.inov8.microbank.server.dao.portal.chargebackmodule.ChargebackListViewDAO;

public class ChargebackManagerImpl implements ChargebackManager {
	
	private ChargebackListViewDAO chargebackListViewDAO; 

	public CustomList<ChargebackListViewModel> getChargebackTransactions(SearchBaseWrapper wrapper) throws Exception {
		return this.chargebackListViewDAO.getChargeBackTransactions(wrapper);
	}
	
	public SearchBaseWrapper searchChargebackTransaction(SearchBaseWrapper searchBaseWrapper) throws Exception {

		CustomList<ChargebackListViewModel>
	      list = this.chargebackListViewDAO.findByExample((
	          ChargebackListViewModel)
	          searchBaseWrapper.
	          getBasePersistableModel(),
	          searchBaseWrapper.
	          getPagingHelperModel(),
	          searchBaseWrapper.
	          getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());



	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;
	}



	public void setChargebackListViewDAO(ChargebackListViewDAO chargebackListViewDAO) {
		this.chargebackListViewDAO = chargebackListViewDAO;
	}



	

}

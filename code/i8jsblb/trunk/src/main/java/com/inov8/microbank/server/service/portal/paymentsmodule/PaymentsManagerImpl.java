package com.inov8.microbank.server.service.portal.paymentsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.paymentsmodule.PaymentsListViewModel;
import com.inov8.microbank.server.dao.portal.paymentsmodule.PaymentsListViewDAO;

public class PaymentsManagerImpl implements PaymentsManager {
	
	PaymentsListViewDAO paymentsListViewDAO; 

	public SearchBaseWrapper searchPayments(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException{
		 
		CustomList<PaymentsListViewModel>
        list = this.paymentsListViewDAO.findByExample( (PaymentsListViewModel)
                                                  searchBaseWrapper.
                                                  getBasePersistableModel(),
                                                  searchBaseWrapper.
                                                  getPagingHelperModel(),
                                                  searchBaseWrapper.
                                                  getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
	}

	public void setPaymentsListViewDAO(PaymentsListViewDAO paymentsListViewDAO) {
		this.paymentsListViewDAO = paymentsListViewDAO;
	}

}

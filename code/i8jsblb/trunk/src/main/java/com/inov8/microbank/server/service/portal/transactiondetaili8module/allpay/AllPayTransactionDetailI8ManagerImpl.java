package com.inov8.microbank.server.service.portal.transactiondetaili8module.allpay;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AllpayTransDetListModel;
import com.inov8.microbank.server.dao.portal.transactiondetaili8module.allpay.AllPayTransactionDetailPortalListViewDAO;

public class AllPayTransactionDetailI8ManagerImpl implements AllPayTransactionDetailI8Manager
{
	private AllPayTransactionDetailPortalListViewDAO allpayTransactionDetailPortalListViewDAO; 
	
	public SearchBaseWrapper searchTransactionDetailForI8(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		CustomList<AllpayTransDetListModel>
		list = this.allpayTransactionDetailPortalListViewDAO.findByExample( (AllpayTransDetListModel)
				searchBaseWrapper.
				getBasePersistableModel(),
				searchBaseWrapper.
				getPagingHelperModel(),
				searchBaseWrapper.
				getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		if(list != null)
		{
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}

	public AllPayTransactionDetailPortalListViewDAO getAllpayTransactionDetailPortalListViewDAO() {
		return allpayTransactionDetailPortalListViewDAO;
	}

	public void setAllpayTransactionDetailPortalListViewDAO(
			AllPayTransactionDetailPortalListViewDAO allpayTransactionDetailPortalListViewDAO) {
		this.allpayTransactionDetailPortalListViewDAO = allpayTransactionDetailPortalListViewDAO;
	}

	

	

}

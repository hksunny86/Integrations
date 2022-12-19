package com.inov8.microbank.server.dao.transactiondetailinfomodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel;

public interface AllPayTransactionInfoListViewDAO extends BaseDAO<AllpayTransInfoListViewModel, Long> {

	
	public CustomList<AllpayTransInfoListViewModel> searchAllpayTransInfoListViewModel(SearchBaseWrapper searchBaseWrapper, String mfsId) throws FrameworkCheckedException;
	public List<TransactionDetailPortalListModel> getMiniStatementTransactionList(ExtendedTransactionDetailPortalListModel model, Integer fetchSize) throws FrameworkCheckedException;
}

package com.inov8.microbank.server.dao.portal.transactiondetaili8module;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import org.springframework.dao.DataAccessException;

import java.util.LinkedHashMap;
import java.util.List;

public interface TransactionDetailPortalListViewDAO
extends BaseDAO<TransactionDetailPortalListModel, Long>
{

	 CustomList<TransactionDetailPortalListModel> findByExampleP2PCashTransfer(TransactionDetailPortalListModel exampleInstance, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel, String... excludeProperty) throws DataAccessException;
	 List<TransactionDetailPortalListModel> fetchDateWiseSummary( SearchBaseWrapper searchBaseWrapper ) throws DataAccessException;
	SearchBaseWrapper findSenderRedeemTransactionDetail(
			SearchBaseWrapper searchBaseWrapper);
	CustomList<TransactionDetailPortalListModel> searchTransactionDetailForI8(SearchBaseWrapper searchBaseWrapper, MnoModel mnoModel) throws FrameworkCheckedException;
}

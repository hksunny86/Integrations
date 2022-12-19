package com.inov8.microbank.server.service.portal.transactiondetaili8module;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.SettlementTransactionViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.DateWiseTxSummaryModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;

public interface TransactionDetailI8Manager
{
	SearchBaseWrapper searchTransactionDetailForI8(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	SearchBaseWrapper searchP2PTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	List<TransactionDetailPortalListModel> fetchDateWiseSummary( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
	List<DateWiseTxSummaryModel> loadDateWiseSummary( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
	public SearchBaseWrapper searchOFSettlementTransaction(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	public SearchBaseWrapper searchOFSettlementTransactionDetail(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	public SearchBaseWrapper searchOFSettlementTransactionSummary(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	CustomList searchTransactionDetailForI8(
			TransactionDetailPortalListModel transactionDetailPortalListModel)
			throws FrameworkCheckedException;
	CustomList<SettlementTransactionViewModel> searchOFSettlementTransactions(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	public String createZipFile(SearchBaseWrapper searchBaseWrapper) throws Exception;
	public SearchBaseWrapper searchSenderRedeemTransactionReportDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	public SearchBaseWrapper searchBispTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException ;
}

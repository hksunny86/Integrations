package com.inov8.microbank.server.facade.portal.transactiondetaili8module;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.SettlementTransactionViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.DateWiseTxSummaryModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;

public class TransactionDetailI8FacadeImpl implements TransactionDetailI8Facade {

	private TransactionDetailI8Manager transactionDetailI8Manager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchTransactionDetailForI8(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return transactionDetailI8Manager
					.searchTransactionDetailForI8(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public SearchBaseWrapper searchP2PTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			return transactionDetailI8Manager
					.searchP2PTransactionDetail(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	@Override
	public List<TransactionDetailPortalListModel> fetchDateWiseSummary( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
	{
	    List<TransactionDetailPortalListModel> list = null;
	    try
        {
            list = transactionDetailI8Manager.fetchDateWiseSummary( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
        return list;
	}

	@Override
	public List<DateWiseTxSummaryModel> loadDateWiseSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	    List<DateWiseTxSummaryModel> list = null;
	    try
        {
            list = transactionDetailI8Manager.loadDateWiseSummary( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
        return list;
	}
	
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setTransactionDetailI8Manager(
			TransactionDetailI8Manager transactionDetailI8Manager) {
		this.transactionDetailI8Manager = transactionDetailI8Manager;
	}

	@Override
	public SearchBaseWrapper searchOFSettlementTransaction(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		return this.transactionDetailI8Manager.searchOFSettlementTransaction(searchBaseWrapper);
	}

	@Override
	public SearchBaseWrapper searchOFSettlementTransactionDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		return this.transactionDetailI8Manager.searchOFSettlementTransactionDetail(searchBaseWrapper);
	}

	@Override
	public SearchBaseWrapper searchOFSettlementTransactionSummary(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		return this.transactionDetailI8Manager.searchOFSettlementTransactionSummary(searchBaseWrapper);
	}

	@Override
	public CustomList searchTransactionDetailForI8(
			TransactionDetailPortalListModel transactionDetailPortalListModel)
			throws FrameworkCheckedException {
		return this.transactionDetailI8Manager.searchTransactionDetailForI8(transactionDetailPortalListModel);
	}
	

	@Override
	public CustomList<SettlementTransactionViewModel> searchOFSettlementTransactions(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		return transactionDetailI8Manager.searchOFSettlementTransactions(searchBaseWrapper);
	}

	@Override
	public String createZipFile(SearchBaseWrapper searchBaseWrapper) throws Exception {

		return transactionDetailI8Manager.createZipFile(searchBaseWrapper);
	}

	@Override
	public SearchBaseWrapper searchSenderRedeemTransactionReportDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		 
			try {
				return transactionDetailI8Manager.searchSenderRedeemTransactionReportDetail(searchBaseWrapper);
			} catch (Exception ex) {
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.FIND_ACTION);
			}
	}

	@Override
	public SearchBaseWrapper searchBispTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return transactionDetailI8Manager.searchBispTransactionDetail(searchBaseWrapper);
	}

}

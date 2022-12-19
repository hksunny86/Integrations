package com.inov8.microbank.server.service.portal.transactiondetail;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.CashTransactionViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.SenderRedeemViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionCashReversalViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionSummaryTextViewModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public interface TransactionDetailManager


{
   SearchBaseWrapper searchTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;
   
   SearchBaseWrapper searchTransactionDetail(SearchBaseWrapper searchBaseWrapper, String mfsId) throws FrameworkCheckedException;

  SearchBaseWrapper loadTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createOrUpdateIssue(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  BaseWrapper createOrUpdateTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadTransaction(BaseWrapper baseWrapper) throws
        FrameworkCheckedException;

  List<TransactionSummaryTextViewModel> populateBBTransactionSummary(String csvTransIDs) throws
  		FrameworkCheckedException;
  
  SearchBaseWrapper searchMiniTransactionViewList(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

  CustomList<TransactionAllViewModel> searchTransactionAllView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

  List<?> findWalkInCustomerTransactions( String cnic,TransactionAllViewModel transactionAllViewModel, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap,DateRangeHolderModel dateRangeHolderModel );

  List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids ) throws DataAccessException;

  CustomList<TransactionCashReversalViewModel> searchTransactionCashReversalView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
  
  CustomList<SenderRedeemViewModel> searchSenderRedeemView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
  CustomList<CashTransactionViewModel> searchCashTransactionView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

  BaseWrapper modifyPINSentMiniTransToExpired(BaseWrapper baseWrapper) throws FrameworkCheckedException;  
  MiniTransactionModel LoadMiniTransactionModel(String transactionCode)throws FrameworkCheckedException;
  BaseWrapper updateMiniTransactionModel(BaseWrapper baseWrapper)throws FrameworkCheckedException;
  SearchBaseWrapper searchTransactionCodeHtrViewModel(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException; 
}

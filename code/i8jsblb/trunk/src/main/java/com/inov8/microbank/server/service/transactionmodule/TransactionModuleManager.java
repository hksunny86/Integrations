package com.inov8.microbank.server.service.transactionmodule;

import java.util.Collection;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface TransactionModuleManager
{

 public BaseWrapper loadTransactionCodeByCode(BaseWrapper baseWrapper) throws FrameworkCheckedException;

 public WorkFlowWrapper generateTransactionCodeRequiresNewTransaction(WorkFlowWrapper
                                                                              workFlowWrapper) throws
         FrameworkCheckedException;

 public WorkFlowWrapper generateTransactionObject(WorkFlowWrapper
                                                          workFlowWrapper) throws
         FrameworkCheckedException;

 public void saveTransaction(WorkFlowWrapper workFlowWrapper) throws
         FrameworkCheckedException;

 public void transactionRequiresNewTransaction(WorkFlowWrapper workFlowWrapper) throws
         FrameworkCheckedException;

 public void bookMeTransactionRequiresNewTransaction(WorkFlowWrapper workFlowWrapper) throws
         FrameworkCheckedException;

 public void updateTransactionCode(WorkFlowWrapper workFlowWrapper) throws
         FrameworkCheckedException;

 public BaseWrapper updateTransaction(BaseWrapper baseWrapper) throws
         FrameworkCheckedException;

 public SearchBaseWrapper loadTransaction(SearchBaseWrapper searchBaseWrapper)
         throws FrameworkCheckedException;

 public BaseWrapper loadTransactionCode(BaseWrapper baseWrapper)
         throws FrameworkCheckedException;

 public BaseWrapper updateTransactionCode(BaseWrapper baseWrapper)
         throws FrameworkCheckedException;

 public SearchBaseWrapper loadTransactionByTransactionCode(SearchBaseWrapper searchBaseWrapper)
         throws FrameworkCheckedException;

 public BaseWrapper saveTransactionModel(BaseWrapper wrapper) throws
         FrameworkCheckedException;

 public BaseWrapper failTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

 public List getTransactionsByCriteria(Long distributorId, Long productId, Boolean isSettled, Boolean isPosted);

 public BaseWrapper saveTransactionSummaryModel(BaseWrapper wrapper) throws FrameworkCheckedException;
 public void saveUpdateAll(Collection<TransactionModel> collection);
 public List<Object[]> getDonationTransactionList(Long trnsactionType, Long supProcessingStatusId, Long serviceId) throws FrameworkCheckedException;

 public Integer updateTransactionProcessingStatus(Long transactionProcessingStatus, List<Long> transactionId);

 public BaseWrapper saveMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

 public BaseWrapper updateP2PTxDetails(BaseWrapper baseWrapper) throws FrameworkCheckedException;

//  public BaseWrapper updateP2PTxDetailsActionAuth(BaseWrapper baseWrapper) throws FrameworkCheckedException;

 public SearchBaseWrapper loadP2PUpdateHistory(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

 void makeWalkinCustomer(String walkInCNIC, String walkInMobileNumber) throws FrameworkCheckedException;

 public WorkFlowWrapper createTransactionModel(WorkFlowWrapper wrapper) throws FrameworkCheckedException;
 MiniTransactionModel loadMiniTransactionModelByTransactionCode(String transactionCode);
 MiniTransactionModel updateMiniTransactionRequiresNewTransaction(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException;
 BaseWrapper loadAndLockMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;
 BaseWrapper updateMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

}

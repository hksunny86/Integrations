package com.inov8.microbank.webapp.action.allpayweb;

import javax.servlet.http.HttpServletRequest;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

public interface AgentWebManager {
	
	public final String BEAN_NAME = "agentWebManager";
	public final String CASH_WITHDRAW = "Cash Withdrawal";
	public final String BULK_PAYMENT_WITHDRAW = "Bulk Payment Withdrawal";
	public final String ACCOUNT_TO_CASH = "Account to Cash";
	public final String CASH_TO_CASH = "Cash Transfer";
	public final String TRANSFER_IN = "Transfer In";
	public final String TRANSACTION_TYPE = "TRANSACTION_TYPE";
	
	public Boolean initTransactionParams(HttpServletRequest request, Boolean isPrintRequest, TransactionCodeModel _transactionCodeModel);
	public Double getCommissionRate(ProductModel productModel);
	public Double getCommissionAmount(WorkFlowWrapper _workFlowWrapper);
	public TransactionModel loadTransactionModel(TransactionCodeModel transactionCodeModel);
	public TransactionCodeModel loadTransactionCodeByCode(String transactionId);
	public TransactionDetailModel loadTransactionDetailModel(TransactionCodeModel transactionCodeModel, HttpServletRequest request);
	public void updateCashDepositRequestParameter(String transactionCode, HttpServletRequest request) throws FrameworkCheckedException;
	public void updateBillPaymentRequestParameter(String transactionCode, HttpServletRequest request) throws FrameworkCheckedException;
	public MiniTransactionModel loadMiniTransactionModelByTrId(TransactionCodeModel transactionCodeModel, HttpServletRequest request);
	public void setTransactionParameter(String transactionId, HttpServletRequest request, MiniTransactionModel miniTransactionModel, TransactionDetailMasterModel transactionDetailMasterModel) throws FrameworkCheckedException;
	//public Boolean checkProductLimit(Long productId, Double amount, HttpServletRequest request);
	public ProductModel getProductModel(Long productId);
	public Boolean isInclusiveTransaction(Long transactionDetailId);
}
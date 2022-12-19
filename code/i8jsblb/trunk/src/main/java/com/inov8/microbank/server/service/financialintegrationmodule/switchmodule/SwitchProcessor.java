package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.IntegrationModuleConstants;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.TransactionConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.thoughtworks.xstream.XStream;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: inov8 Limited
 * </p>
 * 
 * @author Jawwad Farooq
 * @version 1.0
 */
public abstract class SwitchProcessor implements Switch {
	private FailureLogManager auditLogModuleFacade;
	private PostedTransactionReportFacade postedTransactionReportFacade;
	private XStream xstream;

	public BaseWrapper createOrUpdatePostedTransactionBeforeCall(SwitchWrapper switchWrapper, Long intgTransactionType) throws FrameworkCheckedException
	{
		if(intgTransactionType == null)
			intgTransactionType = IntgTransactionTypeConstantsInterface.AGENT_IBFT;
		PostedTransactionReportModel postedTransactionReportModel = new PostedTransactionReportModel();
		System.out.println("Inside SwitchProcessor.createOrUpdatePostedTransactionBeforeCall()...");

		//	1.		Integration Transaction Type Id
		postedTransactionReportModel.setIntgTransactionTypeId(intgTransactionType);
		//---------------------------------------------------------------------------------------------------------------
		// 	2. 		Transaction Code Id
		TransactionCodeModel transactionCodeModel = switchWrapper.getWorkFlowWrapper().getTransactionCodeModel();
		if(transactionCodeModel != null)
		{
//			System.out.println("Inside SwitchProcessor - TxCodeId : " + transactionCodeModel.getTransactionCodeId());
			postedTransactionReportModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
			if(transactionCodeModel.getTransactionCodeId() == null){
				postedTransactionReportModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getTransactionCodeId());
			}
		}
		//---------------------------------------------------------------------------------------------------------------
		//	3. 		Product Id
		ProductModel productModel = switchWrapper.getWorkFlowWrapper().getProductModel();
		if(productModel != null)
		{
			postedTransactionReportModel.setProductId(productModel.getProductId());
		}
		//---------------------------------------------------------------------------------------------------------------
		//	4. 		Amount
		if(intgTransactionType.equals(IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE))
		{
			postedTransactionReportModel.setAmount(switchWrapper.getTransactionAmount());
		}
		else if(intgTransactionType.equals(IntgTransactionTypeConstantsInterface.DEBIT_OLA) || intgTransactionType.equals(IntgTransactionTypeConstantsInterface.CREDIT_OLA))
		{
			postedTransactionReportModel.setAmount(switchWrapper.getOlavo().getBalance());
		}
		else if(intgTransactionType.equals(IntgTransactionTypeConstantsInterface.DEBIT_CREDIT_OLA)
				|| intgTransactionType.equals(IntgTransactionTypeConstantsInterface.DEBIT_CARD))
		{
			postedTransactionReportModel.setAmount(switchWrapper.getOlavo().getBalance());
		} else if(intgTransactionType.equals((IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE)) ||
				intgTransactionType.equals((IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL)) ||
				intgTransactionType.equals((IntgTransactionTypeConstantsInterface.TRANSFER_OUT_CORE)) ||
				intgTransactionType.equals((IntgTransactionTypeConstantsInterface.AGENT_IBFT))) {

			Double amount =  Double.parseDouble(switchWrapper.getMiddlewareIntegrationMessageVO().getTransactionAmount());
			postedTransactionReportModel.setAmount(amount);

			if(switchWrapper.getMiddlewareIntegrationMessageVO() != null) {
				postedTransactionReportModel.setResponseCode(switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode());
				postedTransactionReportModel.setSystemTraceAuditNumber(switchWrapper.getMiddlewareIntegrationMessageVO().getStan());
			}
		} else
		{
			if(switchWrapper.getAmountPaid() != null && !switchWrapper.getAmountPaid().trim().equals(""))
			{
				Double amount =  Double.parseDouble(switchWrapper.getAmountPaid());
				postedTransactionReportModel.setAmount(amount);
			}
		}
		//---------------------------------------------------------------------------------------------------------------
		//	5. & 6.		From Account & To Account
		if(intgTransactionType.equals(IntgTransactionTypeConstantsInterface.DEBIT_OLA) ||
			intgTransactionType.equals(IntgTransactionTypeConstantsInterface.CHECK_BALANCE_OLA) ||
			intgTransactionType.equals(IntgTransactionTypeConstantsInterface.LEDGER_OLA) ||
			intgTransactionType.equals(IntgTransactionTypeConstantsInterface.ROLL_BACK_OLA))
		{
			if(switchWrapper.getAccountInfoModel() != null)
			{
				postedTransactionReportModel.setFromAccount(switchWrapper.getAccountInfoModel().getAccountNo());
			}
		}
		else if(intgTransactionType.equals(IntgTransactionTypeConstantsInterface.CREDIT_OLA))
		{
			postedTransactionReportModel.setToAccount(switchWrapper.getOlavo().getPayingAccNo());
		}
		else if(intgTransactionType.equals(IntgTransactionTypeConstantsInterface.DEBIT_CREDIT_OLA))
		{
			postedTransactionReportModel.setFromAccount(switchWrapper.getOlavo().getPayingAccNo());
			postedTransactionReportModel.setToAccount(switchWrapper.getOlavo().getReceivingAccNo());
		}
		else if(intgTransactionType.equals(IntgTransactionTypeConstantsInterface.DEBIT_CARD))
		{
			postedTransactionReportModel.setFromAccount(switchWrapper.getFromAccountNo());
			postedTransactionReportModel.setToAccount(switchWrapper.getToAccountNo());
			postedTransactionReportModel.setAmount(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
			postedTransactionReportModel.setRefCode((String) switchWrapper.getWorkFlowWrapper().getObject("RRN"));
			//postedTransactionReportModel.setSystemTraceAuditNumber(switchWrapper.);
		}
		else if(intgTransactionType.equals((IntgTransactionTypeConstantsInterface.CHECK_BALANCE_PHOENIX)) ||
			intgTransactionType.equals((IntgTransactionTypeConstantsInterface.BILL_INQUIRY_PHOENIX)) ||
			intgTransactionType.equals((IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX)) ||
			intgTransactionType.equals((IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE)) ||
			intgTransactionType.equals((IntgTransactionTypeConstantsInterface.TITLE_FETCH_PHOENIX)) ||
			intgTransactionType.equals((IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE)) ||
			intgTransactionType.equals((IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL)) ||
				intgTransactionType.equals((IntgTransactionTypeConstantsInterface.TRANSFER_OUT_CORE)) ||
				switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.VC_TRANSFER_PRODUCT) ||
				intgTransactionType.equals((IntgTransactionTypeConstantsInterface.AGENT_IBFT)))
		{
			postedTransactionReportModel.setFromAccount(switchWrapper.getFromAccountNo());
			postedTransactionReportModel.setToAccount(switchWrapper.getToAccountNo());

			if(switchWrapper.getMiddlewareIntegrationMessageVO() != null) {
				postedTransactionReportModel.setResponseCode(switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode());
				postedTransactionReportModel.setSystemTraceAuditNumber(switchWrapper.getMiddlewareIntegrationMessageVO().getStan());
			}
		}
		//---------------------------------------------------------------------------------------------------------------
		//	7. 		Consumer Number
		postedTransactionReportModel.setConsumerNo(switchWrapper.getConsumerNumber());
		//---------------------------------------------------------------------------------------------------------------
		//	8.		Audit Fields		
		postedTransactionReportModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		postedTransactionReportModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		postedTransactionReportModel.setCreatedOn(new Date());
		postedTransactionReportModel.setUpdatedOn(new Date());
		//---------------------------------------------------------------------------------------------------------------
		//	9.		Ref Code(RRN) & STAN (in case of IBFT)
		if(intgTransactionType.equals(IntgTransactionTypeConstantsInterface.DEBIT_CREDIT_OLA)
				|| intgTransactionType.equals(IntgTransactionTypeConstantsInterface.DEBIT_CARD)){
			String stan = (String) switchWrapper.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_STAN);
			String rrn = (String) switchWrapper.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_RRN);
			if(!StringUtil.isNullOrEmpty(stan)){
				postedTransactionReportModel.setSystemTraceAuditNumber(stan);
			}
			if(!StringUtil.isNullOrEmpty(rrn)){
				postedTransactionReportModel.setRefCode(rrn);
			}
		}


		if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().
				getProductId().equals(ProductConstantsInterface.VC_TRANSFER_PRODUCT)){
			postedTransactionReportModel.setAmount(switchWrapper.getWorkFlowWrapper().getTransactionAmount());
			postedTransactionReportModel.setFromAccount(switchWrapper.getFromAccountNo());
			postedTransactionReportModel.setToAccount(switchWrapper.getToAccountNo());
			if(switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode().equals("I8SB-200")) {
				postedTransactionReportModel.setResponseCode("00");
			}
			else{
				postedTransactionReportModel.setResponseCode(switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode());
			}
			postedTransactionReportModel.setSystemTraceAuditNumber(switchWrapper.getMiddlewareIntegrationMessageVO().getStan());
		}

		if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && (switchWrapper.getWorkFlowWrapper().getProductModel().
				getProductId().equals(ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US) ||
				switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL))) {
			if (switchWrapper.getMiddlewareIntegrationMessageVO() != null) {
				postedTransactionReportModel.setAmount(Double.valueOf(switchWrapper.getMiddlewareIntegrationMessageVO().getTransactionAmount()));
			}
		}

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(postedTransactionReportModel);
		baseWrapper = postedTransactionReportFacade.createOrUpdatePostedTransactionRequiresNewTransaction(baseWrapper);
		
		//Transaction Detail Master updates
		populateTransactionDetailMasterForPostedTransaction(switchWrapper.getWorkFlowWrapper(), postedTransactionReportModel);
		
		
		return baseWrapper;
	}
	
	public BaseWrapper createOrUpdatePostedTransactionAfterCall(SwitchWrapper switchWrapper, BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		PostedTransactionReportModel postedTransactionReportModel = (PostedTransactionReportModel)baseWrapper.getBasePersistableModel();
	
		if(postedTransactionReportModel.getIntgTransactionTypeId().equals(IntgTransactionTypeConstantsInterface.CHECK_BALANCE_PHOENIX) || 
				postedTransactionReportModel.getIntgTransactionTypeId().equals(IntgTransactionTypeConstantsInterface.CHECK_BALANCE_OLA))
		{
			postedTransactionReportModel.setAmount(switchWrapper.getBalance());
		}
		
		if(postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.CHECK_BALANCE_PHOENIX)) || 
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.BILL_INQUIRY_PHOENIX)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.BILL_PAYMENT_NADRA)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.TITLE_FETCH_PHOENIX)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.TRANSFER_OUT_CORE)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.AGENT_IBFT)))
		{
			postedTransactionReportModel.setRefCode(switchWrapper.getMiddlewareIntegrationMessageVO().getRetrievalReferenceNumber());
			if(switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode().equals("I8SB-200") &&
					switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.VC_TRANSFER_PRODUCT)){
				postedTransactionReportModel.setResponseCode("00");
			}
			else {
				postedTransactionReportModel.setResponseCode(switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode());
			}
			postedTransactionReportModel.setSystemTraceAuditNumber(switchWrapper.getMiddlewareIntegrationMessageVO().getStan());
		}
		else if(postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.CHECK_BALANCE_OLA)) || 
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.LEDGER_OLA)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.DEBIT_OLA)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.CREDIT_OLA)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.DEBIT_CREDIT_OLA)) ||
				postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.ROLL_BACK_OLA))
				|| postedTransactionReportModel.getIntgTransactionTypeId().equals((IntgTransactionTypeConstantsInterface.DEBIT_CARD)))
		{
			// In case of IBFT(DEBIT_CREDIT_OLA), Ref Code already populated (with RRN) so don't change
			if(StringUtil.isNullOrEmpty(postedTransactionReportModel.getRefCode())){
				postedTransactionReportModel.setRefCode(switchWrapper.getOlavo().getAuthCode());
			}
			postedTransactionReportModel.setResponseCode(switchWrapper.getOlavo().getResponseCode());
		}
		
		if(switchWrapper != null && switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getProductModel() != null
				&& switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() != null 
				&& switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue() == ProductConstantsInterface.MANUAL_ADJUSTMENT.longValue()) {
			
			postedTransactionReportModel.setProduct(switchWrapper.getWorkFlowWrapper().getProductModel());
			postedTransactionReportModel.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
			
			if(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() !=null && switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId() != null) {

				
				postedTransactionReportModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
			}
		}
		
		postedTransactionReportModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		postedTransactionReportModel.setUpdatedOn(new Date());
		
		baseWrapper.setBasePersistableModel(postedTransactionReportModel);
		
		baseWrapper = postedTransactionReportFacade.createOrUpdatePostedTransactionRequiresNewTransaction(baseWrapper);
		
		//Transaction Detail Master updates
		populateTransactionDetailMasterForPostedTransaction(switchWrapper.getWorkFlowWrapper(), postedTransactionReportModel);
				
		return baseWrapper;
	}
	
	private void populateTransactionDetailMasterForPostedTransaction(WorkFlowWrapper workflowWrapper, PostedTransactionReportModel postedTransactionReportModel){
		
		TransactionDetailMasterModel txDetailMaster = workflowWrapper.getTransactionDetailMasterModel();
		if(	postedTransactionReportModel.getRefCode() != null
				&&  (postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX
						|| postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.TITLE_FETCH_PHOENIX
						|| postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.CHECK_BALANCE_PHOENIX
						|| postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.BILL_INQUIRY_PHOENIX
						|| postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE
						|| postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.CREDIT_OLA
						|| postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE
				|| postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.AGENT_IBFT
					)
				){
			
			if(txDetailMaster != null){
				
				//Intg Transaction Type ID = 1
				if(postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX){
					
					if (txDetailMaster.getBillPaymentRrn() == null) { //setting RRN for the first time.
						txDetailMaster.setBillPaymentRrn(postedTransactionReportModel.getRefCode());
					}else{ //append with existing RRN values 
						txDetailMaster.setBillPaymentRrn(txDetailMaster.getBillPaymentRrn() + "," + postedTransactionReportModel.getRefCode());
					}
					
				}else if(postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.TITLE_FETCH_PHOENIX){
					//Intg Transaction Type ID = 2		
					if (txDetailMaster.getTitleFetchRrn() == null) { //setting RRN for the first time.
						txDetailMaster.setTitleFetchRrn(postedTransactionReportModel.getRefCode());
					}else{ //append with existing RRN values 
						txDetailMaster.setTitleFetchRrn(txDetailMaster.getTitleFetchRrn() + "," + postedTransactionReportModel.getRefCode());
					}
					
				}else if(postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE){
					//Intg Transaction Type ID = 3		
					if (txDetailMaster.getFundTransferRrn() == null) { //setting RRN for the first time.
						txDetailMaster.setFundTransferRrn(postedTransactionReportModel.getRefCode());
					}else{ //append with existing RRN values 
						txDetailMaster.setFundTransferRrn(txDetailMaster.getFundTransferRrn() + "," + postedTransactionReportModel.getRefCode());
					}
					
				}else if(postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.BILL_INQUIRY_PHOENIX){
					//Intg Transaction Type ID = 4		
					if (txDetailMaster.getBillInquiryRrn() == null) { //setting RRN for the first time.
						txDetailMaster.setBillInquiryRrn(postedTransactionReportModel.getRefCode());
					}else{ //append with existing RRN values 
						txDetailMaster.setBillInquiryRrn(txDetailMaster.getBillInquiryRrn() + "," + postedTransactionReportModel.getRefCode());
					}
					
				}else if(postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.CHECK_BALANCE_PHOENIX){
					//Intg Transaction Type ID = 5		
					if (txDetailMaster.getCheckBalanceRrn() == null) { //setting RRN for the first time.
						txDetailMaster.setCheckBalanceRrn(postedTransactionReportModel.getRefCode());
					}else{ //append with existing RRN values 
						txDetailMaster.setCheckBalanceRrn(txDetailMaster.getCheckBalanceRrn() + "," + postedTransactionReportModel.getRefCode());
					}
					
				}else if(postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.CREDIT_OLA){
					//Intg Transaction Type ID = 9	
					if(txDetailMaster.getProductId() != null && txDetailMaster.getProductId().longValue() == ProductConstantsInterface.BULK_DISBURSEMENT.longValue()){
						txDetailMaster.setAuthorizationCode(postedTransactionReportModel.getRefCode());
						txDetailMaster.setRecipientAccountNo(postedTransactionReportModel.getToAccount());
					}
					
				} else if(postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE ||
						postedTransactionReportModel.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.AGENT_IBFT) {
					//Intg Transaction Type ID = 9
					txDetailMaster.setProductId(workflowWrapper.getProductModel().getProductId());
					txDetailMaster.setProductName(workflowWrapper.getProductModel().getName());
					
					if(workflowWrapper.getUserDeviceAccountModel() != null) {
						txDetailMaster.setAgent1Id(workflowWrapper.getUserDeviceAccountModel().getUserId());
					}
							
					if (txDetailMaster.getFundTransferRrn() == null) { //setting RRN for the first time.
						txDetailMaster.setFundTransferRrn(postedTransactionReportModel.getRefCode());
					}else{ //append with existing RRN values 
						txDetailMaster.setFundTransferRrn(txDetailMaster.getFundTransferRrn() + "," + postedTransactionReportModel.getRefCode());
					}					
				}
			}
		}
	}
	
	
	public AuditLogModel auditLogBeforeCall(SwitchWrapper switchWrapper, String inputParam) throws WorkFlowException {
		AuditLogModel auditLogModel = new AuditLogModel();
		auditLogModel.setTransactionStartTime(new Timestamp(System.currentTimeMillis()));
		auditLogModel.setIntegrationModuleId(IntegrationModuleConstants.SWITCH_MODULE);

		if (switchWrapper.getTransactionTransactionModel() != null
				&& switchWrapper.getTransactionTransactionModel().getTransactionCodeIdTransactionCodeModel() != null)
			auditLogModel
					.setTransactionCodeId(switchWrapper.getTransactionTransactionModel().getTransactionCodeIdTransactionCodeModel().getTransactionCodeId());

		auditLogModel.setInputParam(inputParam);
		auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

		if (ThreadLocalActionLog.getActionLogId() == null) {
			auditLogModel.setActionLogId(1l);
		}

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(auditLogModel);
		try {
			auditLogModel = (AuditLogModel) this.auditLogModuleFacade.auditLogRequiresNewTransaction(baseWrapper).getBasePersistableModel();
		} catch (FrameworkCheckedException ex) {
			throw new WorkFlowException(ex.getMessage(), ex);
		}

		return auditLogModel;
	}

	public void auditLogAfterCall(AuditLogModel auditLogModel, String outputParam) throws WorkFlowException {
		auditLogModel.setTransactionEndTime(new Timestamp(System.currentTimeMillis()));
		BaseWrapper baseWrapper = new BaseWrapperImpl();

		auditLogModel.setOutputParam(outputParam);
		baseWrapper.setBasePersistableModel(auditLogModel);

		try {
			this.auditLogModuleFacade.auditLogRequiresNewTransaction(baseWrapper);
		} catch (FrameworkCheckedException ex) {
			throw new WorkFlowException(ex.getMessage(), ex);
		}
	}

	public void setAuditLogModuleFacade(FailureLogManager auditLogModuleFacade) {
		this.auditLogModuleFacade = auditLogModuleFacade;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

	public SwitchWrapper customerAccountRelationshipInquiry(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper miniStatement(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public String getMessageFromResource(String responseCode) {
		String message = null;

		try {
			if (responseCode != null && responseCode.length() > 0) {
				message = MessageUtil.getMessage(responseCode);
				if (message != null && message.length() > 0) {
					return message;
				} else {
					message = MessageUtil.getMessage(TransactionConstantsInterface.GENERIC_ERROR_MESSAGE);
				}
			} else {
				message = MessageUtil.getMessage(TransactionConstantsInterface.REQUEST_PROCESSING_FAILED);
			}
		} catch (Exception ex) {
			message = MessageUtil.getMessage(TransactionConstantsInterface.GENERIC_ERROR_MESSAGE);
		}

		return message;
	}

	// *********** ASKARI PHOENIX INTEGRATION
	// ****************************************
	public SwitchWrapper billInquiry(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper billPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}
	
	public SwitchWrapper payBill(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	// *********** ASKARI PHOENIX INTEGRATION
	// ****************************************
	public SwitchWrapper titleFetch(SwitchWrapper transactions) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper customerProfile(SwitchWrapper accountInfo) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper changeDeliveryChannel(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper generatePIN(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper changePIN(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper getAllOlaAccounts(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper debitCreditAccount(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}
	
	public SwitchWrapper debitAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}
	
	public SwitchWrapper creditAccountAdvice(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper createAccount(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper updateAccountInfo(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper getAccountInfo(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public Map<Long, String> getStatusCodes() throws Exception {
		throw new FrameworkCheckedException("Transaction Not Supported...");

	}

	public SwitchWrapper creditTransfer(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper debit(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Debit facility is not Supported...");
	}

	public SwitchWrapper settleCommission(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper settleInov8Commission(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper getAllAccountsWithStats(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper getAllAccountsStatsWithRange(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}
	public SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper wrapper) throws  WorkFlowException,FrameworkCheckedException,Exception{
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	public SwitchWrapper saveWalkinCustomerLedgerEntry(SwitchWrapper wrapper) throws  WorkFlowException,FrameworkCheckedException,Exception{
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}

	  public SwitchWrapper getPhoenixTransactions(SwitchWrapper switchWrapper) throws Exception{
			throw new FrameworkCheckedException("Transaction Not Supported...");
	  }
	  
	  public SwitchWrapper rollbackWalkinCustomer(SwitchWrapper wrapper) throws  WorkFlowException{
		  throw new WorkFlowException("Transaction Not Supported...");
	  }

	  public SwitchWrapper updateLedger(SwitchWrapper switchWrapper) throws  WorkFlowException,FrameworkCheckedException{
		  throw new WorkFlowException("Transaction Not Supported...");
		}
	  public void setPostedTransactionReportFacade(
				PostedTransactionReportFacade postedTransactionReportFacade) {
			this.postedTransactionReportFacade = postedTransactionReportFacade;
		}
	  public SwitchWrapper reverseFundTransfer(SwitchWrapper switchWrapper) throws Exception{
			throw new FrameworkCheckedException("Transaction Not Supported...");
	  }
		public SwitchWrapper changeAccountDetails(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
			throw new FrameworkCheckedException("Transaction Not Supported...");
		}

		public SwitchWrapper creditCardBillPayment(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
			throw new FrameworkCheckedException("Transaction Not Supported...");
		}
		
		public SwitchWrapper getAccountTitle(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
			throw new FrameworkCheckedException("Transaction Not Supported...");
		}

	public SwitchWrapper sendReversalAdvice(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}
	
	public SwitchWrapper sendCreditAdvice(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}
	public SwitchWrapper pushBillPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}
	
	public SwitchWrapper verifyLimits(SwitchWrapper switchWrapper) throws Exception {
		throw new FrameworkCheckedException("Transaction Not Supported...");
	}
	
}

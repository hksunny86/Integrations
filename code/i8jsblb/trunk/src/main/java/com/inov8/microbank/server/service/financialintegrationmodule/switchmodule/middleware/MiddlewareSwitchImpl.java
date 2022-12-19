package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.middleware;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.remoting.RemoteAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.controller.MiddlewareSwitchController;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchProcessor;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.thoughtworks.xstream.XStream;
import org.springframework.web.bind.ServletRequestUtils;

public class MiddlewareSwitchImpl extends SwitchProcessor {

	protected static Log logger = LogFactory.getLog(MiddlewareSwitchImpl.class);
	protected ApplicationContext ctx;
	private MessageSource messageSource;
	private TransactionReversalManager transactionReversalManager;
	private JdbcTemplate jdbcTemplate;
	private StakeholderBankInfoManager stakeholderBankInfoManager;

	private SettlementManager settlementManager;

	private ESBAdapter esbAdapter;

	public MiddlewareSwitchImpl(ApplicationContext ctx) {
		this.ctx = ctx;
		transactionReversalManager = (TransactionReversalManager) ctx.getBean("transactionReversalManager");
		settlementManager = (SettlementManager) ctx.getBean("settlementManager");
		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		esbAdapter = (ESBAdapter) ctx.getBean("esbAdapter");
		stakeholderBankInfoManager = (StakeholderBankInfoManager) ctx.getBean("stakeholderBankInfoManager");

	}

	public MiddlewareSwitchImpl() {}


	ActionLogManager actionLogManager = null;

	protected Boolean logActionLogModel() {

		if(actionLogManager == null) {
			actionLogManager = (ActionLogManager) ctx.getBean("actionLogManager");
		}

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setAppUserIdAppUserModel(ThreadLocalAppUser.getAppUserModel());
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));

		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();

		baseWrapperActionLog.setBasePersistableModel(actionLogModel);

		try {

			baseWrapperActionLog = actionLogManager.createOrUpdateActionLog(baseWrapperActionLog);

		} catch (FrameworkCheckedException e) {
			logger.error(e.getLocalizedMessage());
		}

		if(baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {

			actionLogModel = (ActionLogModel) baseWrapperActionLog.getBasePersistableModel();

			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}


	@Override
	public SwitchWrapper debitCreditAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		logger.info("Inside debitCreditAccount()");

		String fromAccount = switchWrapper.getFromAccountNo();
		String fromAccountBB = switchWrapper.getFromAccountBB();
		String toAccount = switchWrapper.getToAccountNo();
		String toAccountBB = switchWrapper.getToAccountBB();

		Double transactionAmount = switchWrapper.getTransactionAmount();

		MiddlewareMessageVO middlewareMessageDebitVO = null;

		logger.info("Inside debitCreditAccount()"+
				"debitAccount fromAccount RDV : "+fromAccount+
				"debitAccount toAccount BLB : "+toAccountBB+
				"debitAccount transactionAmount "+transactionAmount);

		switchWrapper.setFromAccountNo(fromAccount);
		switchWrapper.setToAccountNo(toAccountBB);

		switchWrapper = this.debitAccount(switchWrapper);

		if(switchWrapper != null && switchWrapper.getMiddlewareIntegrationMessageVO() != null) {
			middlewareMessageDebitVO = switchWrapper.getMiddlewareIntegrationMessageVO();
			switchWrapper.putObject("middlewareMessageDebitVO", middlewareMessageDebitVO);
		}


		if(middlewareMessageDebitVO != null) {

			if(middlewareMessageDebitVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

				switchWrapper.setFromAccountNo(fromAccountBB);
				switchWrapper.setToAccountNo(toAccount);
				switchWrapper.setTransactionAmount(transactionAmount);

				logger.info("Inside debitCreditAccount()"+
						"creditAccountAdvice fromAccount BLB : "+fromAccountBB+
						"creditAccountAdvice toAccount RDV : "+toAccount+
						"creditAccountAdvice transactionAmount "+transactionAmount);

				switchWrapper = this.creditAccountAdvice(switchWrapper);
			}

		} else {
			logger.error("RDV switch down..escalating the exception");
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
		}

		if(middlewareMessageDebitVO != null && !ResponseCodeEnum.PROCESSED_OK.getValue().equals(middlewareMessageDebitVO.getResponseCode())) {

			throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageDebitVO.getResponseCode()));
		}

		return switchWrapper;
	}

	@Override
	public SwitchWrapper pushBillPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		Date billPaymentDateTime = new Date();

		MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
		middlewareAdviceVO.setAccountNo1(switchWrapper.getFromAccountNo());
		middlewareAdviceVO.setCompnayCode(switchWrapper.getUtilityCompanyId());
		middlewareAdviceVO.setCnicNo(switchWrapper.getSenderCNIC());
		middlewareAdviceVO.setConsumerNo(switchWrapper.getConsumerNumber());
		middlewareAdviceVO.setBillCategoryId(switchWrapper.getUtilityCompanyCategoryId());
		middlewareAdviceVO.setBillAggregator("01");
		middlewareAdviceVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
		middlewareAdviceVO.setIsBillPaymentRequest(Boolean.TRUE);
		middlewareAdviceVO.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX);
		middlewareAdviceVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
		middlewareAdviceVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
		middlewareAdviceVO.getDataMap().put("ACTION_LOG_ID", ThreadLocalActionLog.getActionLogId());
		middlewareAdviceVO.getDataMap().put("TRANSACTION_ID", switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionId());
		middlewareAdviceVO.setStan(this.getSysTraceAuditNum());
		middlewareAdviceVO.setRetrievalReferenceNumber(this.getRetrievalReferenceNumber(middlewareAdviceVO.getStan()));
		middlewareAdviceVO.setDateTimeLocalTransaction(billPaymentDateTime);
		middlewareAdviceVO.setTransmissionTime(billPaymentDateTime);
		middlewareAdviceVO.setRequestTime(billPaymentDateTime);
		middlewareAdviceVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
		middlewareAdviceVO.setUbpStan(this.getBillPaymentField127(middlewareAdviceVO.getStan(), billPaymentDateTime));

		transactionReversalManager.makeCoreCreditAdvice(middlewareAdviceVO);

		return switchWrapper;
	}



	@Override
	public SwitchWrapper billPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		MiddlewareMessageVO middlewareMessageVO = null;
		BaseWrapper baseWrapper = null;
		AuditLogModel auditLogModel = null;
		try {
			middlewareMessageVO = switchWrapper.getMiddlewareIntegrationMessageVO();
			auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
			baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX);
			auditLogModel.setInputParam(CommonUtils.getJSON(middlewareMessageVO));
			auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

			MiddlewareSwitchController middlewareSwitchController = (MiddlewareSwitchController) getIntegrationSwitch(switchWrapper);

			if (middlewareSwitchController == null) {
				logger.error("Phoenix switch down..escalating the exception");
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}

			logger.info("\nSending Bill Payment Request"+"\n"+
					"A/C # 1 "+middlewareMessageVO.getAccountNo1()+"\n"+
					"CNIC #  "+middlewareMessageVO.getCnicNo()+"\n"+
					"Bill #  "+middlewareMessageVO.getConsumerNo()+"\n"+
					"C Code  "+middlewareMessageVO.getCompnayCode()+"\n"+
					"Ct Code "+middlewareMessageVO.getBillCategoryId()+"\n"+
					"Amount  "+middlewareMessageVO.getTransactionAmount()+"\n"+
					"STAN/RRN "+middlewareMessageVO.getStan() +" / "+middlewareMessageVO.getRetrievalReferenceNumber()+"\n"+
					"STAN 127 "+middlewareMessageVO.getUbpSTAN()+"\n"+
					"MW URL  "+middlewareSwitchController.toString());

			String sysTraceAuditNum = middlewareMessageVO.getStan();

			auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
			auditLogModel.setIntegrationPartnerIdentifier(middlewareMessageVO.getStan());

			switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

			java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE.MMMMM.dd.yyyy.hh:mm:ss");

			logger.info("\n\n@@@\nSENDING BILL PAYMENT TO RDV WITH DATE "+simpleDateFormat.format(middlewareMessageVO.getRequestTime())
					+ "\nDE12/DE13 = "+simpleDateFormat.format(middlewareMessageVO.getDateLocalTransaction()) +" / "+simpleDateFormat.format(middlewareMessageVO.getTimeLocalTransaction())+")"
					+ "\nSTAN/RRN= "+middlewareMessageVO.getStan() +" / "+middlewareMessageVO.getRetrievalReferenceNumber());

			middlewareMessageVO = middlewareSwitchController.billPayment(middlewareMessageVO);

			switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

			if(middlewareMessageVO != null) {

				logger.info("\n\n@@@\nRECEVIED BILL PAYMENT FROM RDV WITH DATE "+simpleDateFormat.format(middlewareMessageVO.getRequestTime())
						+ "\nDE12/DE13 = "+simpleDateFormat.format(middlewareMessageVO.getDateLocalTransaction()) +" / "+simpleDateFormat.format(middlewareMessageVO.getTimeLocalTransaction())+" \nStan RDV/i8 = "+middlewareMessageVO.getStan()+"/"+sysTraceAuditNum
						+ "\nSTAN/RRN= "+middlewareMessageVO.getStan() +" / "+middlewareMessageVO.getRetrievalReferenceNumber());

				if(StringUtil.isNullOrEmpty(middlewareMessageVO.getStan()) || StringUtil.isNullOrEmpty(sysTraceAuditNum)) {

					logger.error("\nInvalid System Trace Audit Number Returned "+middlewareMessageVO.getStan());

				} else if(!sysTraceAuditNum.equalsIgnoreCase(middlewareMessageVO.getStan())) {

					logger.error("\nMismatched System Trace Audit Number "+middlewareMessageVO.getStan());
				}

				logger.info("@\nRDV Response Code : "+middlewareMessageVO.getResponseCode() +" -> " + this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
				switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
			} else {
				logger.error("RDV switch down..escalating the exception");
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}

			if(middlewareMessageVO != null && !ResponseCodeEnum.PROCESSED_OK.getValue().equals(middlewareMessageVO.getResponseCode())) {

				throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()), middlewareMessageVO.getResponseCode());

			} else if(ResponseCodeEnum.PROCESSED_OK.getValue().equals(middlewareMessageVO.getResponseCode())
					&& !StringUtil.isNullOrEmpty(middlewareMessageVO.getBillAggregator())) {

				String query = "UPDATE TRANSACTION_DETAIL_MASTER TDM SET TDM.BILL_AGGREGATOR = ? WHERE TDM.TRANSACTION_CODE = ?";
				String[] param = {middlewareMessageVO.getBillAggregator(), middlewareMessageVO.getMicrobankTransactionCode()};
				jdbcTemplate.update(query, param);

				//Day-end Settlement of Bill Payment Core Advice (RDV) hit
				Long trxId = -1L;
				if(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionId() != null ){
					trxId = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionId();
				}
				/*	// Moved to TransactionProcessor
				prepareAndSaveSettlementTransactionRDV(trxId,
						switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
						switchWrapper.getTransactionAmount(),
						PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
						PoolAccountConstantsInterface.OF_SETTLEMENT_UBP_POOL_ACCOUNT);
				*/
			}

		} catch (Exception ex) {
			logger.error("Exception occured in MiddlewareSwitchImpl.billPayment() " + ex);
			auditLogModel.setCustomField1(PhoenixConstantsInterface.FAILURE);
			auditLogModel.setCustomField2(ex.getMessage());

			if (ex instanceof WorkFlowException)
				throw (WorkFlowException) ex;
			else if (ex instanceof IOException)
				throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			else if (ex instanceof RemoteAccessException || ex instanceof ConnectException) {

				if(middlewareMessageVO != null) {
					middlewareMessageVO.setResponseCode("404");
					throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
				} else {
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				}
			} else
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);

		} finally {

			if (middlewareMessageVO != null) {

				logger.info("@\nRDV Response Code : "+middlewareMessageVO.getResponseCode() +" -> " + this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));

				this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(middlewareMessageVO));
				if(baseWrapper != null){
					createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
				}
			}
		}

		logger.debug("Ending billPayment method of MiddlewareSwitchImpl");

		return switchWrapper;
	}



	@Override
	public SwitchWrapper debitAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");

		Long intgTransactionTypeId = switchWrapper.getIntgTransactionTypeId();
		if(intgTransactionTypeId == null)
			intgTransactionTypeId = IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE;

		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, intgTransactionTypeId);
		MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();

		if (switchWrapper != null) {

			try {

				auditLogModel.setInputParam(CommonUtils.getJSON(middlewareMessageVO));
				auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

				MiddlewareSwitchController middlewareSwitchController = (MiddlewareSwitchController) getIntegrationSwitch(switchWrapper);

				if (middlewareSwitchController == null) {
					logger.error("Phoenix switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				middlewareMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());	//T24 Core Account
				middlewareMessageVO.setAccountNo2(switchWrapper.getToAccountNo());		//BB Account (would not entertain TBD)
				middlewareMessageVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
				middlewareMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
				middlewareMessageVO.setTransmissionTime(new Date());

				switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

				Integer tryNumber = 1;
				logger.info("\n@RDV F.T for Transaction Code "+middlewareMessageVO.getMicrobankTransactionCode() + " Try # "+tryNumber);
				//

				if(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().toString().equals(MessageUtil.getMessage("SalaryDisbursementProductId"))) {//set product Id of Salary Disbursement
					I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareFundsTransferForSalaryDisbursement
							(I8SBConstants.RequestType_InterBankFundTransfer);

					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

					requestVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
					requestVO.setAccountId1(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",switchWrapper.getFromAccountNo()));
					requestVO.setAccountId2(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",switchWrapper.getToAccountNo()));
					requestVO.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
					requestVO.setBankId(String.valueOf(CommissionConstantsInterface.BANK_ID));
					requestVO.setReserved1("1");
					requestVO.setReserved2("BLBSalDisb-");

					SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();

					i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
					requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
					I8SBSwitchControllerResponseVO responseVO = requestVO.getI8SBSwitchControllerResponseVO();
					i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
					responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
					ESBAdapter.processI8sbResponseCode(responseVO, false);
					if (!responseVO.getResponseCode().equals("I8SB-200")) {
						throw new Exception("Error in Funds Transfer");
					}

					if(!StringUtil.isNullOrEmpty(responseVO.getResponseCode()) &&
							(responseVO.getResponseCode().trim().equals("404") || responseVO.getResponseCode().trim().equals("220"))) {

						++tryNumber;

						logger.info("\nResponse Code # "+responseVO.getResponseCode()+"-"+
								responseVO.getSTAN()+"-"+
								responseVO.getRrn()+"-"+
								responseVO.getTransmissionDateAndTime()+
								"\n@RDV Reversal Advice for Transaction Code "+middlewareMessageVO.getMicrobankTransactionCode() + " Try # "+tryNumber);

						middlewareMessageVO.setReversalSTAN(responseVO.getSTAN());

						if(responseVO.getTransmissionDateAndTime() != null) {

							String requestTime = new java.text.SimpleDateFormat("MMddhhmmss").format(responseVO.getTransmissionDateAndTime());

							middlewareMessageVO.setReversalRequestTime(requestTime);
						}

						MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
						middlewareAdviceVO.setIsCreditAdvice(false);
						middlewareAdviceVO.setAccountNo1(responseVO.getAccountId1());
						middlewareAdviceVO.setAccountNo2(responseVO.getAccountId2());
						middlewareAdviceVO.setTransactionAmount(responseVO.getAmount());
						middlewareAdviceVO.setMicrobankTransactionCode(middlewareMessageVO.getMicrobankTransactionCode());
						middlewareAdviceVO.setTransmissionTime(dateFormat.parse(responseVO.getTransmissionDateAndTime()));
						middlewareAdviceVO.setRequestTime(dateFormat.parse(responseVO.getTransmissionDateAndTime()));
						middlewareAdviceVO.setStan(responseVO.getSTAN());
						middlewareAdviceVO.setReversalSTAN(responseVO.getSTAN());
						middlewareAdviceVO.setReversalRequestTime(responseVO.getTransmissionDateAndTime());
						switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

						middlewareAdviceVO.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL);
						middlewareAdviceVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
						//Product model is null in case of manual adjustment
						if(switchWrapper.getWorkFlowWrapper().getProductModel() != null){
							middlewareAdviceVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
						}

						if( switchWrapper.getTransactionTransactionModel() != null && switchWrapper.getTransactionTransactionModel().getTransactionId() != null){
							middlewareAdviceVO.getDataMap().put("TRANSACTION_ID", switchWrapper.getTransactionTransactionModel().getTransactionId());
						}

						transactionReversalManager.sendCoreReversalRequiresNewTransaction(middlewareAdviceVO);

						throw new WorkFlowException(this.getResponseCodeDetail(responseVO.getResponseCode()));
					}

					if (responseVO != null && !responseVO.getResponseCode().equals("I8SB-200")) {

						throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
					}

					auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
					auditLogModel.setIntegrationPartnerIdentifier(responseVO.getSTAN());
					middlewareMessageVO.setResponseCode(responseVO.getResponseCode());
					middlewareMessageVO.setStan(responseVO.getSTAN());
					switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
				}
				else {
					middlewareMessageVO = middlewareSwitchController.fundTransfer(middlewareMessageVO);
//					middlewareMessageVO.setResponseCode("220");

					if (middlewareMessageVO != null) {
						logger.info("@\nRDV Response Code : " + middlewareMessageVO.getResponseCode() + " -> " + this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
						switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
					} else {
						logger.error("RDV switch down..escalating the exception");
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
					}
//Sheheryaar
					if (!StringUtil.isNullOrEmpty(middlewareMessageVO.getResponseCode()) &&
							(middlewareMessageVO.getResponseCode().trim().equals("404") || middlewareMessageVO.getResponseCode().trim().equals("220"))) {

						++tryNumber;

						logger.info("\nResponse Code # " + middlewareMessageVO.getResponseCode() + "-" +
								middlewareMessageVO.getStan() + "-" +
								middlewareMessageVO.getRetrievalReferenceNumber() + "-" +
								middlewareMessageVO.getRequestTime() +
								"\n@RDV Reversal Advice for Transaction Code " + middlewareMessageVO.getMicrobankTransactionCode() + " Try # " + tryNumber);

						middlewareMessageVO.setReversalSTAN(middlewareMessageVO.getStan());

						if (middlewareMessageVO.getRequestTime() != null) {

							String requestTime = new java.text.SimpleDateFormat("MMddhhmmss").format(middlewareMessageVO.getRequestTime());

							middlewareMessageVO.setReversalRequestTime(requestTime);
						}

						MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
						middlewareAdviceVO.setIsCreditAdvice(false);
						middlewareAdviceVO.setAccountNo1(middlewareMessageVO.getAccountNo1());
						middlewareAdviceVO.setAccountNo2(middlewareMessageVO.getAccountNo2());
						middlewareAdviceVO.setTransactionAmount(middlewareMessageVO.getTransactionAmount());
						middlewareAdviceVO.setMicrobankTransactionCode(middlewareMessageVO.getMicrobankTransactionCode());
						middlewareAdviceVO.setTransmissionTime(middlewareMessageVO.getTransmissionTime());
						middlewareAdviceVO.setRequestTime(middlewareMessageVO.getRequestTime());
						middlewareAdviceVO.setStan(middlewareMessageVO.getStan());
						middlewareAdviceVO.setReversalSTAN(middlewareMessageVO.getReversalSTAN());
						middlewareAdviceVO.setReversalRequestTime(middlewareMessageVO.getReversalRequestTime());

						middlewareAdviceVO.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL);
						middlewareAdviceVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
						//Product model is null in case of manual adjustment
						if (switchWrapper.getWorkFlowWrapper().getProductModel() != null) {
							middlewareAdviceVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
						}

						if (switchWrapper.getTransactionTransactionModel() != null && switchWrapper.getTransactionTransactionModel().getTransactionId() != null) {
							middlewareAdviceVO.getDataMap().put("TRANSACTION_ID", switchWrapper.getTransactionTransactionModel().getTransactionId());
						}

						transactionReversalManager.sendCoreReversalRequiresNewTransaction(middlewareAdviceVO);

						throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));

					}

					if (middlewareMessageVO != null && !ResponseCodeEnum.PROCESSED_OK.getValue().equals(middlewareMessageVO.getResponseCode())) {

						throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
					}

					auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
					auditLogModel.setIntegrationPartnerIdentifier(middlewareMessageVO.getStan());
				}

			} catch (Exception ex) {
				logger.error("Exception occured in MiddlewareSwitchImpl.debitAccount() :: " + ex);
				auditLogModel.setCustomField1(PhoenixConstantsInterface.FAILURE);
				auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException || ex instanceof ConnectException) {
					if(middlewareMessageVO != null) {
						middlewareMessageVO.setResponseCode("404");
						throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
					} else {
						throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
					}
				}
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);

			} finally {

				if (middlewareMessageVO != null) {

					this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(middlewareMessageVO));
					if(baseWrapper != null){
						createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
					}
				}

				switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
			}
		}

		return switchWrapper;
	}

	/**
	 * To be used from CoreAdveiceListener for Transfer IN reversal advice
	 *
	 * @param switchWrapper
	 * @return
	 * @throws WorkFlowException
	 * @throws FrameworkCheckedException
	 */
	public SwitchWrapper sendReversalAdvice(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		MiddlewareMessageVO middlewareMessageVOReversal = null;
		BaseWrapper baseWrapperReversal = null;
		MiddlewareMessageVO middlewareMessageVO = switchWrapper.getMiddlewareIntegrationMessageVO();

		MiddlewareSwitchController middlewareSwitchController = (MiddlewareSwitchController) getIntegrationSwitch(switchWrapper);

		if (middlewareSwitchController == null) {
			logger.error("Phoenix switch down..escalating the exception");
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
		}

		try{

			baseWrapperReversal = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL);

			middlewareMessageVOReversal = middlewareSwitchController.acquirerReversalAdvice(middlewareMessageVO);

			logger.info("Response Code of Reversal Advice(Tx Code: "+middlewareMessageVOReversal.getMicrobankTransactionCode()+"): " + middlewareMessageVOReversal.getResponseCode());
			switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVOReversal);

			if(middlewareMessageVOReversal != null && !ResponseCodeEnum.PROCESSED_OK.getValue().equals(middlewareMessageVOReversal.getResponseCode())) {
				logger.error("Reversal Failed due to\n");
				throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
			}


		} catch (Exception ex) {
			logger.error("Exception occured in MiddlewareSwitchImpl.sendReversalAdvice.... " + ex);
			ex.printStackTrace();

			if (ex instanceof WorkFlowException)
				throw (WorkFlowException) ex;
			else if (ex instanceof IOException)
				throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			else if (ex instanceof RemoteAccessException || ex instanceof ConnectException) {
				if(middlewareMessageVO != null) {
					middlewareMessageVO.setResponseCode("404");
					throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
				} else {
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				}
			}
			else
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
		} finally {

			if (middlewareMessageVOReversal != null) {

//				this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream.toXML(middlewareMessageVO),
//						XPathConstants.PhoenixAuditLogInputParamLocationSteps));
				if(baseWrapperReversal != null){
					createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapperReversal);
				}
			}

			switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
		}

		return switchWrapper;

	}

	@Override
	public SwitchWrapper creditAccountAdvice(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		Date creditAccountAdviceDateTime = new Date();
		ProductModel productModel = switchWrapper.getWorkFlowWrapper().getProductModel();
		Long productId = null;
		MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
		middlewareAdviceVO.setIsCreditAdvice(true);
		if(productModel != null) {
			productId = productModel.getProductId();
			middlewareAdviceVO.setProductId(productModel.getProductId());
		}
			if(productId != null && ProductConstantsInterface.RELIEF_FUND_PRODUCT.equals(productId)) {
			StakeholderBankInfoModel stakeholderBankInfoModel1 = stakeholderBankInfoManager.loadStakeholderBankInfoModel
					(PoolAccountConstantsInterface.DONATION_POOL_T24_ACCOUNT_ID);
			middlewareAdviceVO.setAccountNo1(stakeholderBankInfoModel1.getAccountNo());
		}else {
			middlewareAdviceVO.setAccountNo1(switchWrapper.getFromAccountNo());

		}
		middlewareAdviceVO.setAccountNo2(switchWrapper.getToAccountNo());
		middlewareAdviceVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
		if(switchWrapper.getMiddlewareIntegrationMessageVO() != null)
			middlewareAdviceVO.setMicrobankTransactionCode(switchWrapper.getMiddlewareIntegrationMessageVO().getMicrobankTransactionCode());
		middlewareAdviceVO.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE);
		if(switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null)
		{
			middlewareAdviceVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
			middlewareAdviceVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
		}
		middlewareAdviceVO.setStan(this.getSysTraceAuditNum());
		middlewareAdviceVO.setRetrievalReferenceNumber(this.getRetrievalReferenceNumber(middlewareAdviceVO.getStan()));
		middlewareAdviceVO.setRequestTime(creditAccountAdviceDateTime);
		middlewareAdviceVO.setDateTimeLocalTransaction(creditAccountAdviceDateTime);
		middlewareAdviceVO.setTransmissionTime(creditAccountAdviceDateTime);

		//Product model is null in case of manual adjustment
//
//		if(productModel != null){
//			productId = productModel.getProductId();
//			middlewareAdviceVO.setProductId(productModel.getProductId());
//		}

		if(productId != null && ProductConstantsInterface.AGENT_BB_TO_IBFT.equals(productId) || ProductConstantsInterface.CUSTOMER_BB_TO_IBFT.equals(productId)){
			WorkFlowWrapper wrapper = switchWrapper.getWorkFlowWrapper();
			CustomerAccount customerAccount = wrapper.getCustomerAccount();
			middlewareAdviceVO.setAccountTitle(customerAccount.getTitleOfTheAccount());
			middlewareAdviceVO.setSenderBankImd(customerAccount.getFromBankImd());
			middlewareAdviceVO.setAccountNo1(wrapper.getAppUserModel().getMobileNo());
			middlewareAdviceVO.setBankIMD(customerAccount.getToBankImd());
			middlewareAdviceVO.setCrDr(customerAccount.getTransactionType());
			middlewareAdviceVO.setBeneIBAN(customerAccount.getBenificieryIBAN());
			middlewareAdviceVO.setBeneBankName(customerAccount.getBankName());
			middlewareAdviceVO.setBeneBranchName(customerAccount.getBranchName());
			middlewareAdviceVO.setSenderIBAN("PK15JSBL" + wrapper.getAppUserModel().getMobileNo() + "00000");
			middlewareAdviceVO.setSenderName(wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName());
			if(wrapper.getRetailerContactModel()!=null) {
				middlewareAdviceVO.setCardAcceptorNameAndLocation(wrapper.getAppUserModel().getFirstName()
						+ " " + wrapper.getAppUserModel().getLastName() + wrapper.getRetailerContactModel().getAreaName());
				middlewareAdviceVO.setAgentId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
			}
			middlewareAdviceVO.setPAN(wrapper.getAppUserModel().getNic());
			middlewareAdviceVO.setCnicNo(wrapper.getAppUserModel().getNic());
			middlewareAdviceVO.setPurposeOfPayment((String)wrapper.getObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE));
		}

		if( switchWrapper.getTransactionTransactionModel() != null && switchWrapper.getTransactionTransactionModel().getTransactionId() != null){
			middlewareAdviceVO.getDataMap().put("TRANSACTION_ID", switchWrapper.getTransactionTransactionModel().getTransactionId());
		}

		transactionReversalManager.makeCoreCreditAdvice(middlewareAdviceVO);

		return switchWrapper;
	}

	/**
	 * used from CoreAdveiceListener for RDV Credit Account advice
	 *
	 * @param switchWrapper
	 * @return
	 * @throws WorkFlowException
	 * @throws FrameworkCheckedException
	 */
	public SwitchWrapper sendCreditAdvice(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, switchWrapper.getIntgTransactionTypeId());
		MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
		if(switchWrapper.getObject("IBFT_VO") != null)
			middlewareMessageVO = (MiddlewareMessageVO) switchWrapper.getObject("IBFT_VO");

		if (switchWrapper != null) {

			try {

				auditLogModel.setInputParam(CommonUtils.getJSON(middlewareMessageVO));

				if (ThreadLocalActionLog.getActionLogId() == null) {
					auditLogModel.setActionLogId(1l);
				}else{
					auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
				}

				MiddlewareSwitchController middlewareSwitchController = (MiddlewareSwitchController) getIntegrationSwitch(switchWrapper);

				if (middlewareSwitchController == null) {
					logger.error("Phoenix switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				String STAN = null;
				String RRN = null;

				if(switchWrapper.getMiddlewareIntegrationMessageVO() != null) {

					STAN = switchWrapper.getMiddlewareIntegrationMessageVO().getStan();
					RRN = switchWrapper.getMiddlewareIntegrationMessageVO().getRetrievalReferenceNumber();
				}

				middlewareMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());	//BB Account (would not entertain TBD)
				middlewareMessageVO.setAccountNo2(switchWrapper.getToAccountNo());		//T24 Core Account
				middlewareMessageVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
				middlewareMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
				middlewareMessageVO.setStan(STAN);
				middlewareMessageVO.setTransmissionTime(switchWrapper.getMiddlewareIntegrationMessageVO().getTransmissionTime());
				middlewareMessageVO.setRequestTime(switchWrapper.getMiddlewareIntegrationMessageVO().getRequestTime());
				middlewareMessageVO.setDateLocalTransaction(switchWrapper.getMiddlewareIntegrationMessageVO().getDateLocalTransaction());
				middlewareMessageVO.setTimeLocalTransaction(switchWrapper.getMiddlewareIntegrationMessageVO().getTimeLocalTransaction());
				middlewareMessageVO.setRetrievalReferenceNumber(RRN);

				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(middlewareMessageVO.getStan());

				switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
				logger.error("[MiddlewareSwitchImpl.creditAccountAdvice] About to hit T24 integration. Transaction ID: " + middlewareMessageVO.getMicrobankTransactionCode());

				java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE.MMMMM.dd.yyyy.hh:mm:ss");

				if(middlewareMessageVO.getRequestTime() != null) {
					if(middlewareMessageVO.getDateLocalTransaction()  == null)
						middlewareMessageVO.setDateLocalTransaction(new Date());
					if(middlewareMessageVO.getTimeLocalTransaction() == null)
						middlewareMessageVO.setTimeLocalTransaction(new Date());
					logger.info("\n\n@@@\nSENDING CREDIT ADVICE TO RDV WITH DATE "+simpleDateFormat.format(middlewareMessageVO.getRequestTime())
							+ "\nDE12/DE13 = "+simpleDateFormat.format(middlewareMessageVO.getDateLocalTransaction()) +" / "+simpleDateFormat.format(middlewareMessageVO.getTimeLocalTransaction())+")"
							+ "\nSTAN/RRN= "+STAN +" / "+RRN);
				}
				ProductModel productModel = null;
				if(switchWrapper.getWorkFlowWrapper() != null)
					productModel = switchWrapper.getWorkFlowWrapper().getProductModel();
				if(productModel != null && productModel.getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT) || productModel.getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT))
				{
					logger.info("Performing IBFT Advice in MiddlewareSwitchImpl.sendCreditAdvice()");
					middlewareMessageVO.setStan(STAN);
					middlewareMessageVO.setRetrievalReferenceNumber(RRN);
					middlewareMessageVO.setRequestTime(new Date());
					middlewareMessageVO = (MiddlewareMessageVO) middlewareSwitchController.ibftAdvice(middlewareMessageVO);
					//IBFT Advice Mock
//                    middlewareMessageVO.setAccountTitle("Sheheryaar Nawaz");
//                    middlewareMessageVO.setResponseCode("00");
				}
				else
					middlewareMessageVO = middlewareSwitchController.fundTransferAdvice(middlewareMessageVO);

				if(middlewareMessageVO != null) {

					if(middlewareMessageVO.getRequestTime() != null) {
						logger.info("\n\n@@@\nRECEVIED CREDIT ADVICE TO RDV WITH DATE "+simpleDateFormat.format(middlewareMessageVO.getRequestTime())
								+ "\nDE12/DE13 = "+simpleDateFormat.format(middlewareMessageVO.getDateLocalTransaction()) +" / "+simpleDateFormat.format(middlewareMessageVO.getTimeLocalTransaction())+")"
								+ "\nSTAN/RRN= "+middlewareMessageVO.getStan() +" / "+middlewareMessageVO.getRetrievalReferenceNumber());
					}

					logger.info("@\nRDV STAN "+middlewareMessageVO.getStan()+" i8 STAN "+STAN);

					if(StringUtil.isNullOrEmpty(middlewareMessageVO.getStan()) || StringUtil.isNullOrEmpty(STAN)) {

						logger.error("\nInvalid System Trace Audit Number Returned "+middlewareMessageVO.getStan());

					} else if(!STAN.equalsIgnoreCase(middlewareMessageVO.getStan())) {

						logger.error("\nMismatched System Trace Audit Number "+middlewareMessageVO.getStan());
					}

					logger.info("@\nRDV Response Code : "+middlewareMessageVO.getResponseCode() +" -> " + this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));

					switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

				} else {
					logger.error("RDV switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				if(middlewareMessageVO != null && !ResponseCodeEnum.PROCESSED_OK.getValue().equals(middlewareMessageVO.getResponseCode())) {

					throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));

				}else{
				/*	// Moved to TransactionProcessor
					//Day-end Settlement of credit Core Advice (RDV) hit
					Long trxId = -1L;
					if(switchWrapper.getWorkFlowWrapper().getTransactionModel() != null
							&& switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionId() != null ){

						trxId = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionId();
					}

					this.prepareAndSaveSettlementTransactionRDV(trxId,
							switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
							switchWrapper.getTransactionAmount(),
							PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
							PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT);
				*/
				}

			} catch (Exception ex) {
				logger.error("Exception occured in MiddlewareSwitchImpl.... " + ex);
				auditLogModel.setCustomField1(PhoenixConstantsInterface.FAILURE);
				auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException || ex instanceof ConnectException) {
					if(middlewareMessageVO != null) {
						middlewareMessageVO.setResponseCode("404");
						throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
					} else {
						throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
					}
				}
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);

			} finally {

				//set middlewareMessageVO at first so that it could be used for posted transaction report
				switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

				if (middlewareMessageVO != null) {
					this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(middlewareMessageVO));
					if(baseWrapper != null){
						createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
					}
				}
			}

		}
		return switchWrapper;
	}


	private void prepareAndSaveSettlementTransactionRDV(Long transactionId,Long productId,Double amount,Long fromAccountInfoId, Long toAccountInfoId) throws Exception{

		SettlementTransactionModel settlementModel = new SettlementTransactionModel();
		settlementModel.setTransactionID(transactionId);
		settlementModel.setProductID(productId);
		settlementModel.setCreatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		settlementModel.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		settlementModel.setCreatedOn(new Date());
		settlementModel.setUpdatedOn(new Date());
		settlementModel.setStatus(0L);
		settlementModel.setFromBankInfoID(fromAccountInfoId);
		settlementModel.setToBankInfoID(toAccountInfoId);
		settlementModel.setAmount(amount);

		this.settlementManager.saveSettlementTransactionModel(settlementModel);

	}


	@Override
	public SwitchWrapper billInquiry(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		if (logger.isDebugEnabled()) {
			logger.debug("Inside billInquiry method of MiddlewareSwitchImpl");
		}

		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");

		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.BILL_INQUIRY_PHOENIX);

		MiddlewareMessageVO middlewareMessageVO = null;

		if (switchWrapper != null) {

			try {

				middlewareMessageVO = new MiddlewareMessageVO();
				middlewareMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());
				middlewareMessageVO.setAccountNo2(switchWrapper.getToAccountNo());
				middlewareMessageVO.setCnicNo(switchWrapper.getSenderCNIC());
				middlewareMessageVO.setConsumerNo(switchWrapper.getConsumerNumber());

				auditLogModel.setInputParam(CommonUtils.getJSON(middlewareMessageVO));
				auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

				MiddlewareSwitchController middlewareSwitchController = (MiddlewareSwitchController) getIntegrationSwitch(switchWrapper);

				if (switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null){
					middlewareMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
				}

				if (middlewareSwitchController == null) {
					logger.error("RDV switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				logger.info("[RDV.billInquiry] Hitting RDV for Bill Inquiry.. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				long startTime = Calendar.getInstance().getTimeInMillis();

				middlewareMessageVO = (MiddlewareMessageVO) middlewareSwitchController.billInquiry(middlewareMessageVO);

				long timeConsumed = Calendar.getInstance().getTimeInMillis() - startTime;

				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);

				if (middlewareMessageVO != null) {

					if(timeConsumed == 0l){//to avoid divide by zero exception..
						timeConsumed = 1l;
					}

					logger.info("@\nRDV Response Code : "+middlewareMessageVO.getResponseCode() +" -> " + this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));

					switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
				}

			} catch (Exception ex) {

				if(middlewareMessageVO != null) {
					logger.info("@\nRDV Response Code : "+middlewareMessageVO.getResponseCode() +" -> " + this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
				}
				logger.error("Error in MiddlewareSwitchImpl.billInquiry() :: " + ex );
				auditLogModel.setCustomField1(PhoenixConstantsInterface.FAILURE);
				auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException || ex instanceof ConnectException) {
					if(middlewareMessageVO != null) {
						middlewareMessageVO.setResponseCode("404");
						throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
					} else {
						throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
					}
				}
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			} finally {

				if (middlewareMessageVO != null) {

					this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(middlewareMessageVO));

					createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
				}

			}
			String responseCode = middlewareMessageVO.getResponseCode();

			if (responseCode == null) {
				logger.error("PHOENIX is down....."
						+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
			} else {
				if (responseCode.equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
					switchWrapper.setBillingMonth(middlewareMessageVO.getBillDueDate().toString());
					switchWrapper.setDueDatePayableAmount(middlewareMessageVO.getTransactionAmount());
					switchWrapper.setPaymentDueDate(middlewareMessageVO.getBillDueDate().toString());
					switchWrapper.setBillStatus(middlewareMessageVO.getResponseCode());
				} else {
					throw new WorkFlowException("error");
				}
			}

			logger.info("Ending billInquiry method of MiddlewareSwitchImpl");
		}
		return switchWrapper;
	}


	private CustomerAccount getCustomerAccount(SwitchWrapper switchWrapper) {

		String coreAccountNumber = null;

		CustomerAccount customerAccount = switchWrapper.getCustomerAccount();

		if (switchWrapper != null) {

			customerAccount = switchWrapper.getCustomerAccount();

			if(customerAccount != null && !StringUtil.isNullOrEmpty(customerAccount.getNumber())) {

				coreAccountNumber = switchWrapper.getCustomerAccount().getNumber();

			} else if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()
					&& StringUtil.isNullOrEmpty(coreAccountNumber) && switchWrapper.getAccountInfoModel() != null &&
					!StringUtil.isNullOrEmpty(switchWrapper.getAccountInfoModel().getAccountNo())) {

				coreAccountNumber = switchWrapper.getAccountInfoModel().getAccountNo();
				customerAccount = new CustomerAccount();

			} else {
				customerAccount = new CustomerAccount();
			}

			customerAccount.setNumber(coreAccountNumber);
			switchWrapper.setCustomerAccount(customerAccount);
		}


		return customerAccount;
	}


	public SwitchWrapper titleFetch(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		if (logger.isDebugEnabled()) {
			logger.debug("Inside titleFetch method of MiddlewareSwitchImpl");
		}

		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.TITLE_FETCH_PHOENIX);
		MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();

		CustomerAccount customerAccount = getCustomerAccount(switchWrapper);
		ProductModel productModel = null;
		if(switchWrapper.getWorkFlowWrapper() != null)
			productModel = switchWrapper.getWorkFlowWrapper().getProductModel();

		String coreAccountNumber = customerAccount.getNumber();

		try {

			middlewareMessageVO = new MiddlewareMessageVO();
			middlewareMessageVO.setAccountNo1(coreAccountNumber);

			auditLogModel.setInputParam(CommonUtils.getJSON(middlewareMessageVO));
			auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

			MiddlewareSwitchController middlewareSwitchController = (MiddlewareSwitchController) getIntegrationSwitch(switchWrapper);
			//
			if(productModel != null && (productModel.getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT) || productModel.getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)))
			{
				logger.info("Performing IBFT Title Fetch MiddlewareSwitchImpl.ibftTitleFetch()");
				WorkFlowWrapper wrapper = switchWrapper.getWorkFlowWrapper();
				BBToCoreVO bbVo = (BBToCoreVO) switchWrapper.getWorkFlowWrapper().getProductVO();
				if(wrapper.getAppUserModel()!=null) {
					middlewareMessageVO.setAccountNo1(wrapper.getAppUserModel().getMobileNo());
					middlewareMessageVO.setCnicNo(wrapper.getAppUserModel().getNic());
					middlewareMessageVO.setTransactionAmount(switchWrapper.getWorkFlowWrapper().getTransactionAmount().toString());
				}
				else
				{
					middlewareMessageVO.setAccountNo1(ThreadLocalAppUser.getAppUserModel().getMobileNo());
					middlewareMessageVO.setCnicNo(ThreadLocalAppUser.getAppUserModel().getNic());
					middlewareMessageVO.setTransactionAmount(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount().toString());
				}
				middlewareMessageVO.setAccountNo2(coreAccountNumber);
				middlewareMessageVO.setSenderBankImd(customerAccount.getFromBankImd());
				middlewareMessageVO.setBankIMD(customerAccount.getToBankImd());
				middlewareMessageVO.setPurposeOfPayment((String)wrapper.getObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE));
				middlewareMessageVO.setRequestTime(new Date());
				//Transaction Purpose Code
				middlewareMessageVO = (MiddlewareMessageVO) middlewareSwitchController.ibftTitleFetch(middlewareMessageVO);
				//IBFT Mock Code Start
//				middlewareMessageVO.setAccountTitle("Sheheryaar Nawaz");
//				middlewareMessageVO.setResponseCode("00");
//				middlewareMessageVO.setAccountBranchName("Lahore");
//				middlewareMessageVO.setAccountBankName("HBL");
//				middlewareMessageVO.setBenificieryIBAN("PK15HBL0321123456700000");
//				middlewareMessageVO.setCrdr("Cr");
				//IBFT Mock Code End
			}
			else
				middlewareMessageVO = (MiddlewareMessageVO) middlewareSwitchController.titleFetch(middlewareMessageVO);
			//IBFT Mock Code Start
//				middlewareMessageVO.setAccountTitle("Abubakar Farooque");
//				middlewareMessageVO.setResponseCode("00");
//				middlewareMessageVO.setAccountBranchName("Lahore");
//				middlewareMessageVO.setAccountBankName("HBL");
//				middlewareMessageVO.setBenificieryIBAN("PK15HBL0321123456700000");
//				middlewareMessageVO.setCrdr("Cr");
			//IBFT Mock Code End

			auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
			auditLogModel.setIntegrationPartnerIdentifier(" ");

			if (middlewareMessageVO != null) {

				middlewareMessageVO.setAccountNo1(coreAccountNumber);

				logger.info("@\n RDV Title Fetched '" + middlewareMessageVO.getAccountTitle() +"' for Account Number : "+middlewareMessageVO.getAccountNo1());

				logger.info("@\nRDV Response Code : "+middlewareMessageVO.getResponseCode() +" --> " + this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));

				switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

				if(ResponseCodeEnum.PROCESSED_OK.getValue().equals(middlewareMessageVO.getResponseCode())) {

					if (!StringUtil.isNullOrEmpty(middlewareMessageVO.getAccountTitle())) {

						customerAccount.setTitleOfTheAccount(middlewareMessageVO.getAccountTitle());
						customerAccount.setNumber(middlewareMessageVO.getAccountNo1());
						//
						customerAccount.setBankName(middlewareMessageVO.getAccountBankName());
						customerAccount.setBranchName(middlewareMessageVO.getAccountBranchName());
						customerAccount.setBenificieryIBAN(middlewareMessageVO.getBenificieryIBAN());
						customerAccount.setTransactionType(middlewareMessageVO.getCrdr());
						//
						switchWrapper.setCustomerAccount(customerAccount);
						switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

					} else {

						throw new WorkFlowException("Transaction Failed, Please Check Account Status");
					}
				} else {

					throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
				}
			} else {

				throw new WorkFlowException("Transaction Failed, Please Check Account Status");
			}

		} catch (Exception ex) {
			logger.error("Exception occured in MiddlewareSwitchImpl.titleFetch() :: " + ex);
			auditLogModel.setCustomField1(PhoenixConstantsInterface.FAILURE);
			auditLogModel.setCustomField2(ex.getMessage());
			if (ex instanceof WorkFlowException) {
				throw (WorkFlowException) ex;
			} else if(ex instanceof RemoteAccessException || ex instanceof ConnectException)  {

				if(middlewareMessageVO != null) {
					middlewareMessageVO.setResponseCode("404");
					throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
				} else {
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				}
			}
			else {
				throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			}

		} finally {

			if(baseWrapper != null) {

				if(switchWrapper.getMiddlewareIntegrationMessageVO() == null) {
					switchWrapper.setMiddlewareIntegrationMessageVO(new MiddlewareMessageVO());
				}

				createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
			}
		}
		logger.info("Ending titleFetch method of MiddlewareSwitchImpl");

		return switchWrapper;
	}



	protected Object getIntegrationSwitch(SwitchWrapper switchWrapper) {

		Object integrationSwitch = null;

		if (logger.isDebugEnabled()) {
			logger.debug("Inside getIntegrationSwitch method of MiddlewareSwitchImpl");
		}

		String switchUrl = null;

		try {

			if(switchWrapper.getSwitchSwitchModel() != null && switchWrapper.getSwitchSwitchModel().getUrl() != null) {
				//
				switchUrl = switchWrapper.getSwitchSwitchModel().getUrl();

			} else if(switchWrapper.getWorkFlowWrapper().getProductModel()!= null &&
					switchWrapper.getWorkFlowWrapper().getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel() != null &&
					switchWrapper.getWorkFlowWrapper().getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl() != null) {

				switchUrl = switchWrapper.getWorkFlowWrapper().getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl();

			}

//			switchUrl = "http://127.0.0.1:8181/middleware-integration/ws/middlewareswitch";
			logger.info("Switch Url = "+switchUrl);

			integrationSwitch = HttpInvokerUtil.getHttpInvokerFactoryBean(MiddlewareSwitchController.class, switchUrl);

		} catch (Exception ex) {

			logger.error("Exception occured Inside getIntegrationSwitch .. " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));

		}

		return integrationSwitch;
	}


	@Override
	public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.CHECK_BALANCE_PHOENIX);

		MiddlewareSwitchController middlewareSwitchController = (MiddlewareSwitchController) getIntegrationSwitch(switchWrapper);

		MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
		middlewareMessageVO.setAccountNo1(switchWrapper.getAccountInfoModel().getAccountNo());

		try {

			middlewareMessageVO = (MiddlewareMessageVO) middlewareSwitchController.accountBalanceInquiry(middlewareMessageVO);

			if(middlewareMessageVO != null) {

				logger.info("@\nRDV Response Code : "+middlewareMessageVO.getResponseCode() +" -> " + this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));

				switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

				if(!StringUtil.isNullOrEmpty(middlewareMessageVO.getAccountBalance())) {

//					logger.info("@\nRDV Balance : "+middlewareMessageVO.getAccountBalance()); 

					switchWrapper.setAgentBalance(Double.valueOf(middlewareMessageVO.getAccountBalance()));
				}
			} else {
				logger.error("RDV switch down..escalating the exception");
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}


			if(middlewareMessageVO != null && !ResponseCodeEnum.PROCESSED_OK.getValue().equals(middlewareMessageVO.getResponseCode())) {

				throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
			}

		} catch (Exception ex) {
			if (ex instanceof WorkFlowException) {
				throw (WorkFlowException) ex;
			} else if (ex instanceof IOException)
				throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			else if (ex instanceof RemoteAccessException || ex instanceof ConnectException) {
				if(middlewareMessageVO != null) {
					middlewareMessageVO.setResponseCode("404");
					throw new WorkFlowException(this.getResponseCodeDetail(middlewareMessageVO.getResponseCode()));
				} else {
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				}
			}
			else
				throw new WorkFlowException(ex.getLocalizedMessage());
		} finally {

			if(baseWrapper != null){
				createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
			}
		}

		return switchWrapper;
	}


	public String getResponseCodeDetail(String _responseCode) {

		logger.info("\n\n\n@RDV Response Code : "+_responseCode);

		String responseCodeNarration = "";

		try {

			if(!StringUtil.isNullOrEmpty(_responseCode)) {

				responseCodeNarration = MessageUtil.getMessage("rdv.response.code."+_responseCode.trim());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if(StringUtil.isNullOrEmpty(responseCodeNarration)){
			responseCodeNarration = MessageUtil.getMessage("rdv.response.code.err");
		}
		logger.info("\n@RDV Response Code ("+_responseCode+") Narration : "+responseCodeNarration);

		return responseCodeNarration;
	}


	private String getSysTraceAuditNum() {

		logger.info("getSysTraceAuditNum()");

		String sysTraceAuditNum = null;

		StringBuilder query = new StringBuilder(0);
		query.append("select stan_seq.nextval as stan from dual");

		Long stan = null;

		try {

			logger.info(query.toString());

			stan = jdbcTemplate.queryForLong(query.toString());

			if(stan != null) {

				sysTraceAuditNum = String.valueOf(stan);
				sysTraceAuditNum = StringUtils.leftPad(sysTraceAuditNum, 6, '0');

				logger.info("\ni8 STAN => "+sysTraceAuditNum);

			} else {

				throw new WorkFlowException("System unable to generate System Trace Audit Number.");
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new WorkFlowException("System unable to generate System Trace Audit Number.");
		}

		return sysTraceAuditNum;
	}


	private String getRetrievalReferenceNumber(String stan) {

		Date now = new Date(uniqueCurrentTimeMS());
		SimpleDateFormat format = new SimpleDateFormat("ddhhmm");
		String rrn = format.format(now);
		return rrn + stan;
	}

	/**
	 * @param stan
	 * @param date
	 * @return UBPS-BBCCYYMMDDHHmmssSTAN| UBPS-BB20150327124010123456	
	 */
	private String getBillPaymentField127(String stan, Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuilder sb = new StringBuilder("UBPS-BB");
		sb.append(format.format(date));
		sb.append(stan);

		return sb.toString();
	}

	private long uniqueCurrentTimeMS() {

		AtomicLong LAST_TIME_MS = new AtomicLong();

		long now = System.currentTimeMillis();

		while (true) {
			long lastTime = LAST_TIME_MS.get();
			if (lastTime >= now)
				now = lastTime + 1;
			if (LAST_TIME_MS.compareAndSet(lastTime, now))
				return now;
		}
	}

	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}

	@Override
	public SwitchWrapper transaction(SwitchWrapper transactions) throws WorkFlowException, FrameworkCheckedException {
		return null;
	}

	@Override
	public SwitchWrapper rollback(SwitchWrapper transactions) throws WorkFlowException, FrameworkCheckedException {
		return null;
	}

	@Override
	public SwitchWrapper getLedger(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
		return null;
	}
}
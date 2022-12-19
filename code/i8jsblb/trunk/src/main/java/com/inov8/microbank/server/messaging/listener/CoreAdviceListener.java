package com.inov8.microbank.server.messaging.listener;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.controller.IBFTSwitchController;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.PostedTransactionReportDAO;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.List;

public class CoreAdviceListener implements MessageListener {
	
	private static Log logger = LogFactory.getLog(CoreAdviceListener.class);
	
    private TransactionReversalManager transactionReversalManager;
    private CommonCommandManager commonCommandManager;
	private PostedTransactionReportDAO postedTransactionReportDAO;
	private IBFTSwitchController ibftSwitchController;
	private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");

	@Override
	public void onMessage(Message message) {
		MiddlewareAdviceVO middlewareAdviceVO = null;
		Long productId = null;
		String ibftStatus = PortalConstants.IBFT_STATUS_FAILED;
//		Long accountOpeningStatus = 0L;
		Long isValid = 1L;
		try {
			middlewareAdviceVO = (MiddlewareAdviceVO) ((ObjectMessage) message).getObject();

			productId = middlewareAdviceVO.getProductId();
			logger.info("Message Received at CoreAdviceListener... Is_Credit_Advice:"+middlewareAdviceVO.getIsCreditAdvice());
			if(checkAlreadySuccessful(middlewareAdviceVO)){
				logger.info("Core Advice already successful in posted_trx... so ignoring trx_code_Id:"+middlewareAdviceVO.getMicrobankTransactionCodeId());
				// Update status from Failed to Successful
				transactionReversalManager.updateRetryAdviceReportStatus(middlewareAdviceVO.getMicrobankTransactionCodeId());
				return;
			}
			if(productId.equals(ProductConstantsInterface.AGENT_BB_TO_IBFT) || productId.equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT))
			{
				if(transactionReversalManager.findIBFTRetryByStanAndStatus(middlewareAdviceVO) == null){
					transactionReversalManager.saveNewIBFTRecord(middlewareAdviceVO);
				}
			}
			if(productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION)
					|| productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION)
					|| productId.equals(ProductConstantsInterface.AGENT_VALLENCIA_COLLECTION)
					|| productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION_BY_ACCOUNT)
					|| productId.equals(ProductConstantsInterface.AGENT_LICENSE_FEE_COLLECTION)
					|| productId.equals(ProductConstantsInterface.AGENT_BALUCHISTAN_ET_COLLECTION)
					|| productId.equals(ProductConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM)
					|| productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION)
					|| productId.equals(ProductConstantsInterface.CUSTOMER_KP_CHALLAN_COLLECTION)
					|| productId.equals(ProductConstantsInterface.CUSTOMER_VALLENCIA_COLLECTION)
					|| productId.equals(ProductConstantsInterface.CUSTOMER_BALUCHISTAN_ET_COLLECTION)
					|| productId.equals(ProductConstantsInterface.CUSTOMER_E_LEARNING_MANAGEMENT_SYSTEM)
					||productId.equals(ProductConstantsInterface.CUST_IHL_Gujranawala_CAMP)
					||productId.equals(ProductConstantsInterface.CUST_IHL_ISLAMABAD_CAMP)
					||productId.equals(ProductConstantsInterface.CUST_SARHAD_UNIVERSITY_COLLECTION)
					||productId.equals(ProductConstantsInterface.IHL_ISLAMABAD_CAMP)
					||productId.equals(ProductConstantsInterface.IHL_Gujranawala_CAMP)
					||productId.equals(ProductConstantsInterface.SARHAD_UNIVERSITY_COLLECTION)
					|| productId.equals(ProductConstantsInterface.LICENSE_FEE_COLLECTION)
					|| productId.equals(50055L) || productId.equals(50056L)){
				middlewareAdviceVO.setIsCreditAdvice(false);
			}
			if(productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION))
			{
                middlewareAdviceVO.setIsCreditAdvice(false);
				SwitchWrapper switchWrapper = prepareSwitchWrapper(middlewareAdviceVO);
				transactionReversalManager.makeCoreAdvice(switchWrapper,middlewareAdviceVO);
			}
			if(productId.equals(ProductConstantsInterface.ACCOUNT_OPENING) || productId.equals(ProductConstantsInterface.CUST_ACCOUNT_OPENING)
					|| productId.equals(ProductConstantsInterface.PORTAL_ACCOUNT_OPENING))
			{
				middlewareAdviceVO.setIsCreditAdvice(false);
				SwitchWrapper switchWrapper = prepareSwitchWrapperForAccOpening(middlewareAdviceVO);
				transactionReversalManager.makeCoreAdvice(switchWrapper,middlewareAdviceVO);
//				accountOpeningStatus = 1L;
				isValid = 0L;
			}
			else
			{
				if(productId.equals(ProductConstantsInterface.AGENT_BB_TO_IBFT) || productId.equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)) {
					middlewareAdviceVO.setIsCreditAdvice(true);
				}
				SwitchWrapper switchWrapper = prepareSwitchWrapper(middlewareAdviceVO);
				transactionReversalManager.makeCoreAdvice(switchWrapper,middlewareAdviceVO);
				ibftStatus = PortalConstants.IBFT_STATUS_SUCCESS;
			}
		}catch (JMSException ex){
			logger.error(ex);
			throw new RuntimeException(ex.getMessage(), ex);
		}catch (Exception ex)
		{
			ibftStatus = PortalConstants.IBFT_STATUS_FAILED;
			ex.printStackTrace();
			logger.error("Exception occured CoreAdviceListener...TransactionCode:"+middlewareAdviceVO.getMicrobankTransactionCode());
			if(productId != null && productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION))
			{
				try
				{
					TransactionDetailMasterModel tempModel = new TransactionDetailMasterModel();
					tempModel.setTransactionCode(middlewareAdviceVO.getMicrobankTransactionCode());
					tempModel.setProductId(productId);
					tempModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH);
					BaseWrapper wrapper = new BaseWrapperImpl();
					wrapper.setBasePersistableModel(tempModel);
					try {
						tempModel = transactionReversalManager.loadTDMForReversal(wrapper);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//
					if(tempModel != null && tempModel.getPk() != null && tempModel.getExciseChallanNo() == null)
					{
						MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
						middlewareMessageVO.setProductId(tempModel.getProductId());
						middlewareMessageVO.setReserved1(tempModel.getTransactionCodeId().toString());
						ibftSwitchController.cashWithDrawalReversal(middlewareMessageVO);
						logger.info("Excise & Taxation Transaction Reversal completed....");
						String date = dtf.print(new DateTime());
						String time = tf.print(new LocalTime());
						Object[] agentSMSParam = new Object[] {tempModel.getTransactionCode(),time,date};
						BaseWrapper smsBaseWrapper = new BaseWrapperImpl();
						SmsMessage agentSmsMessage = new SmsMessage(tempModel.getRecipientMobileNo(),
								MessageUtil.getMessage ("excise.tax.agent.failure",agentSMSParam));
						SmsMessage customerSmsMessage = new SmsMessage(tempModel.getRecipientMobileNo(),
								MessageUtil.getMessage ("excise.tax.customer.failure",agentSMSParam));

						smsBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, agentSmsMessage);
						ibftSwitchController.getCommonCommandManager().sendSMSToUser(smsBaseWrapper);

						smsBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, customerSmsMessage);
						ibftSwitchController.getCommonCommandManager().sendSMSToUser(smsBaseWrapper);
						//this.updateRetryAdviceReportStatus(middlewareAdviceVO.getMicrobankTransactionCodeId());
					}
					else
					{
						int retry = (int)((ActiveMQObjectMessage) ((ActiveMQObjectMessage) ((ObjectMessage) message))).getRedeliveryCounter();
						//
						if(retry >= 5)
						{
							tempModel = new TransactionDetailMasterModel();
							tempModel.setTransactionCode(middlewareAdviceVO.getMicrobankTransactionCode());
							tempModel.setProductId(productId);
							tempModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH);
							wrapper = new BaseWrapperImpl();
							wrapper.setBasePersistableModel(tempModel);
							tempModel = transactionReversalManager.loadTDMForReversal(wrapper);
							MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
							middlewareMessageVO.setProductId(tempModel.getProductId());
							middlewareMessageVO.setReserved1(tempModel.getTransactionCodeId().toString());
							ibftSwitchController.cashWithDrawalReversal(middlewareMessageVO);
							logger.info("Excise & Taxation Transaction Reversal completed....");
							String date = dtf.print(new DateTime());
							String time = tf.print(new LocalTime());
							Object[] agentSMSParam = new Object[] {tempModel.getTransactionCode(),time,date};
							BaseWrapper smsBaseWrapper = new BaseWrapperImpl();
							SmsMessage agentSmsMessage = new SmsMessage(tempModel.getRecipientMobileNo(),
									MessageUtil.getMessage ("excise.tax.agent.failure",agentSMSParam));
							SmsMessage customerSmsMessage = new SmsMessage(tempModel.getRecipientMobileNo(),
									MessageUtil.getMessage ("excise.tax.customer.failure",agentSMSParam));

							smsBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, agentSmsMessage);
							ibftSwitchController.getCommonCommandManager().sendSMSToUser(smsBaseWrapper);

							smsBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, customerSmsMessage);
							ibftSwitchController.getCommonCommandManager().sendSMSToUser(smsBaseWrapper);
							//this.updateRetryAdviceReportStatus(middlewareAdviceVO.getMicrobankTransactionCodeId());
						}
					}
				}
				catch (Exception ex1)
				{
					try {
						throw new Exception(ex.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				throw new RuntimeException(ex.getMessage(), ex);
			}
			else
				throw new RuntimeException(ex.getMessage(), ex);
		}
		finally {
			if(productId.equals(ProductConstantsInterface.AGENT_BB_TO_IBFT) || productId.equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)) {
				try {
					transactionReversalManager.updateIBFTStatus(middlewareAdviceVO.getStan(),middlewareAdviceVO.getRequestTime(),
                            ibftStatus,middlewareAdviceVO.getMicrobankTransactionCode());
				} catch (FrameworkCheckedException e) {
					e.printStackTrace();
				}
			}
			if(productId.equals(ProductConstantsInterface.ACCOUNT_OPENING) || productId.equals(ProductConstantsInterface.CUST_ACCOUNT_OPENING)
					|| productId.equals(ProductConstantsInterface.PORTAL_ACCOUNT_OPENING)){
				try {
					transactionReversalManager.saveOrUpdateAccountOpeningStatus(middlewareAdviceVO.getConsumerNo(), middlewareAdviceVO.getCnicNo(), 1L, isValid,
							middlewareAdviceVO.getRetrievalReferenceNumber(), middlewareAdviceVO.getResponseCode());
				} catch (FrameworkCheckedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private SwitchWrapper prepareSwitchWrapper(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException{
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel(middlewareAdviceVO.getMicrobankTransactionCode()));
		workFlowWrapper.getTransactionCodeModel().setTransactionCodeId(middlewareAdviceVO.getMicrobankTransactionCodeId());
		TransactionDetailMasterModel transactionDetailMasterModel = getTransactionDetailMasterModel(middlewareAdviceVO.getMicrobankTransactionCode());
		workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
		Long productId = middlewareAdviceVO.getProductId();
		if(productId != null && (productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION)
				|| productId.equals(ProductConstantsInterface.AGENT_VALLENCIA_COLLECTION)
				|| productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION_BY_ACCOUNT)
				|| productId.equals(ProductConstantsInterface.AGENT_LICENSE_FEE_COLLECTION)
				|| productId.equals(ProductConstantsInterface.AGENT_BALUCHISTAN_ET_COLLECTION)
				|| productId.equals(ProductConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM)
				|| productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION)
                || productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION)
				|| productId.equals(ProductConstantsInterface.CUSTOMER_KP_CHALLAN_COLLECTION)
				|| productId.equals(ProductConstantsInterface.CUSTOMER_VALLENCIA_COLLECTION)
				|| productId.equals(ProductConstantsInterface.CUSTOMER_BALUCHISTAN_ET_COLLECTION)
				|| productId.equals(ProductConstantsInterface.CUSTOMER_E_LEARNING_MANAGEMENT_SYSTEM)
				|| productId.equals(ProductConstantsInterface.LICENSE_FEE_COLLECTION)
				||productId.equals(ProductConstantsInterface.CUST_IHL_ISLAMABAD_CAMP)
				||productId.equals(ProductConstantsInterface.CUST_SARHAD_UNIVERSITY_COLLECTION)
				||productId.equals(ProductConstantsInterface.CUST_IHL_Gujranawala_CAMP)
				||productId.equals(ProductConstantsInterface.IHL_Gujranawala_CAMP)
				||productId.equals(ProductConstantsInterface.SARHAD_UNIVERSITY_COLLECTION)
				||productId.equals(ProductConstantsInterface.IHL_ISLAMABAD_CAMP)
				|| productId.longValue() == 50056L
				|| productId.longValue() == 50055L
				|| productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION)))
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			ProductModel model = new ProductModel();
			model.setProductId(middlewareAdviceVO.getProductId());
			baseWrapper.setBasePersistableModel(model);
			baseWrapper = commonCommandManager.loadProduct(baseWrapper);
			model = (ProductModel) baseWrapper.getBasePersistableModel();
			workFlowWrapper.setProductModel(model);
		}
		else
		{
			workFlowWrapper.setProductModel(new ProductModel());
			workFlowWrapper.getProductModel().setProductId(productId);
		}
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		
		switchWrapper.setIntgTransactionTypeId(middlewareAdviceVO.getIntgTransactionTypeId());
		switchWrapper.setFromAccountNo(middlewareAdviceVO.getAccountNo1());
		switchWrapper.setToAccountNo(middlewareAdviceVO.getAccountNo2());
		switchWrapper.setTransactionAmount(Double.parseDouble(middlewareAdviceVO.getTransactionAmount()));
		
		MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
		middlewareMessageVO.setAccountNo1(middlewareAdviceVO.getAccountNo1());
		middlewareMessageVO.setAccountNo2(middlewareAdviceVO.getAccountNo2());
		middlewareMessageVO.setTransactionAmount(middlewareAdviceVO.getTransactionAmount());
		middlewareMessageVO.setMicrobankTransactionCode(middlewareAdviceVO.getMicrobankTransactionCode());
		middlewareMessageVO.setTransmissionTime(middlewareAdviceVO.getTransmissionTime());
		middlewareMessageVO.setRequestTime(middlewareAdviceVO.getRequestTime());
		middlewareMessageVO.setStan(middlewareAdviceVO.getStan());
		middlewareMessageVO.setReversalSTAN(middlewareAdviceVO.getReversalSTAN());
		middlewareMessageVO.setReversalRequestTime(middlewareAdviceVO.getReversalRequestTime());
		
		middlewareMessageVO.setCompnayCode(middlewareAdviceVO.getCompnayCode());
		middlewareMessageVO.setCnicNo(middlewareAdviceVO.getCnicNo());
		middlewareMessageVO.setConsumerNo(middlewareAdviceVO.getConsumerNo());
		middlewareMessageVO.setBillCategoryId(middlewareAdviceVO.getBillCategoryId());
		middlewareMessageVO.setBillAggregator(middlewareAdviceVO.getBillAggregator());
		middlewareMessageVO.setDateLocalTransaction(middlewareAdviceVO.getDateTimeLocalTransaction());
		middlewareMessageVO.setTimeLocalTransaction(middlewareAdviceVO.getDateTimeLocalTransaction());
		middlewareMessageVO.setUbpSTAN(middlewareAdviceVO.getUbpStan());

		middlewareMessageVO.setRetrievalReferenceNumber(middlewareAdviceVO.getRetrievalReferenceNumber());
		
		switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
		switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

		if(middlewareAdviceVO.getDataMap().get("ACTION_LOG_ID") != null) {			
			switchWrapper.getWorkFlowWrapper().putObject("ACTION_LOG_ID", (Long)middlewareAdviceVO.getDataMap().get("ACTION_LOG_ID"));
		}
		
		TransactionModel transactionModel = new TransactionModel();
		switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
		
		if(middlewareAdviceVO.getDataMap().get("TRANSACTION_ID") != null) {	
			Long transactionId = (Long)middlewareAdviceVO.getDataMap().get("TRANSACTION_ID");
			switchWrapper.getWorkFlowWrapper().putObject("TRANSACTION_ID", transactionId);
			switchWrapper.getWorkFlowWrapper().getTransactionModel().setTransactionId(transactionId);
		}
		if(productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION))
		{
			middlewareAdviceVO.setExciseAssessmentTotalAmount(transactionDetailMasterModel.getExciseAssessmentAmount().toString());
			middlewareAdviceVO.setExciseAssessmentNumber(transactionDetailMasterModel.getExciseAssessmentNumber());
			middlewareAdviceVO.setVehicleRegNumber(transactionDetailMasterModel.getVehicleRegNo());
		}
		if(productId != null && (productId.equals(ProductConstantsInterface.AGENT_BB_TO_IBFT) || (productId.equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT))))
		{
			logger.info("Preparing IBFT Advice in CoreAdviceListener()");
			if(middlewareAdviceVO.getAccountTitle() == null)
				middlewareMessageVO.setAccountTitle("");
			else
				middlewareMessageVO.setAccountTitle(middlewareAdviceVO.getAccountTitle());
			if(middlewareAdviceVO.getSenderIBAN() == null)
				middlewareMessageVO.setSenderIban("");
			else
				middlewareMessageVO.setSenderIban(middlewareAdviceVO.getSenderIBAN());
			middlewareMessageVO.setAccountNo1(middlewareAdviceVO.getAccountNo1());
			if(middlewareAdviceVO.getSenderName() == null)
				middlewareMessageVO.setSenderName("");
			else
				middlewareMessageVO.setSenderName(middlewareAdviceVO.getSenderName());

			if(productId.equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)) {
				middlewareMessageVO.setCardAcceptorNameAndLocation(middlewareAdviceVO.getCardAcceptorNameAndLocation());
			}

			if(middlewareAdviceVO.getSenderBankImd() == null)
				middlewareMessageVO.setSenderBankImd("");
			else
				middlewareMessageVO.setSenderBankImd(middlewareAdviceVO.getSenderBankImd());

			if(productId.equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)) {
				middlewareMessageVO.setAgentId("00000000");
				switchWrapper.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE);
			}
			else
				middlewareMessageVO.setAgentId(middlewareAdviceVO.getAgentId());

			middlewareMessageVO.setAccountNo2(middlewareAdviceVO.getAccountNo2());
			middlewareMessageVO.setPAN(middlewareAdviceVO.getCnicNo());
			if(middlewareAdviceVO.getBankIMD() == null)
				middlewareMessageVO.setBankIMD("");
			else
				middlewareMessageVO.setBankIMD(middlewareAdviceVO.getBankIMD());
			middlewareMessageVO.setPurposeOfPayment(middlewareAdviceVO.getPurposeOfPayment());
			middlewareMessageVO.setCrdr(middlewareAdviceVO.getCrDr());
			if(middlewareAdviceVO.getBeneIBAN() == null)
				middlewareMessageVO.setBenificieryIban("");
			else
				middlewareMessageVO.setBenificieryIban(middlewareAdviceVO.getBeneIBAN());
			if(middlewareAdviceVO.getBeneBankName() == null)
				middlewareMessageVO.setAccountBankName("");
			else
				middlewareMessageVO.setAccountBankName(middlewareAdviceVO.getBeneBankName());
			if(middlewareAdviceVO.getBeneBranchName() == null)
				middlewareMessageVO.setAccountBranchName("");
			else
				middlewareMessageVO.setAccountBranchName(middlewareAdviceVO.getBeneBranchName());
		}
		switchWrapper.putObject("IBFT_VO",middlewareMessageVO);
		return switchWrapper;
	}

	private SwitchWrapper prepareSwitchWrapperForAccOpening(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException{
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		Long productId = middlewareAdviceVO.getProductId();

		switchWrapper.setIntgTransactionTypeId(middlewareAdviceVO.getIntgTransactionTypeId());
		switchWrapper.setFromAccountNo(middlewareAdviceVO.getAccountNo1());
		switchWrapper.setToAccountNo(middlewareAdviceVO.getAccountNo2());
		if(!middlewareAdviceVO.getTransactionAmount().equals("null")) {
			switchWrapper.setTransactionAmount(Double.parseDouble(middlewareAdviceVO.getTransactionAmount()));
		}
		MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
		middlewareMessageVO.setAccountNo1(middlewareAdviceVO.getAccountNo1());
		middlewareMessageVO.setAccountNo2(middlewareAdviceVO.getAccountNo2());
		middlewareMessageVO.setTransactionAmount(middlewareAdviceVO.getTransactionAmount());
		middlewareMessageVO.setMicrobankTransactionCode(middlewareAdviceVO.getMicrobankTransactionCode());
		middlewareMessageVO.setTransmissionTime(middlewareAdviceVO.getTransmissionTime());
		middlewareMessageVO.setRequestTime(middlewareAdviceVO.getRequestTime());
		middlewareMessageVO.setStan(middlewareAdviceVO.getStan());
		middlewareMessageVO.setReversalSTAN(middlewareAdviceVO.getReversalSTAN());
		middlewareMessageVO.setReversalRequestTime(middlewareAdviceVO.getReversalRequestTime());

		middlewareMessageVO.setCompnayCode(middlewareAdviceVO.getCompnayCode());
		middlewareMessageVO.setCnicNo(middlewareAdviceVO.getCnicNo());
		middlewareMessageVO.setConsumerNo(middlewareAdviceVO.getConsumerNo());
		middlewareMessageVO.setBillCategoryId(middlewareAdviceVO.getBillCategoryId());
		middlewareMessageVO.setBillAggregator(middlewareAdviceVO.getBillAggregator());
		middlewareMessageVO.setDateLocalTransaction(middlewareAdviceVO.getDateTimeLocalTransaction());
		middlewareMessageVO.setTimeLocalTransaction(middlewareAdviceVO.getDateTimeLocalTransaction());
		middlewareMessageVO.setUbpSTAN(middlewareAdviceVO.getUbpStan());

		middlewareMessageVO.setRetrievalReferenceNumber(middlewareAdviceVO.getRetrievalReferenceNumber());

		switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
		switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

		if(middlewareAdviceVO.getDataMap().get("ACTION_LOG_ID") != null) {
			switchWrapper.getWorkFlowWrapper().putObject("ACTION_LOG_ID", (Long)middlewareAdviceVO.getDataMap().get("ACTION_LOG_ID"));
		}

		switchWrapper.putObject("IBFT_VO",middlewareMessageVO);
		return switchWrapper;
	}

	private TransactionDetailMasterModel getTransactionDetailMasterModel(String transactionCode) throws FrameworkCheckedException {
		
		TransactionDetailMasterModel _transactionDetailMasterModel = new TransactionDetailMasterModel();
		_transactionDetailMasterModel.setTransactionCode(transactionCode);
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(_transactionDetailMasterModel);
		
		commonCommandManager.loadTransactionDetailMaster(searchBaseWrapper);
		
		TransactionDetailMasterModel transactionDetailMasterModel = (TransactionDetailMasterModel) searchBaseWrapper.getBasePersistableModel();
		
		return transactionDetailMasterModel;
	}
	
    public boolean checkAlreadySuccessful(MiddlewareAdviceVO middlewareAdviceVO){
		boolean result = false;
		try{
			if(middlewareAdviceVO.getProductId() != null && middlewareAdviceVO.getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)
					&& middlewareAdviceVO.getMicrobankTransactionCode() != null){
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
				transactionCodeModel.setCode(middlewareAdviceVO.getMicrobankTransactionCode());
				baseWrapper.setBasePersistableModel(transactionCodeModel);
				baseWrapper = commonCommandManager.loadTransactionCodeByCode(baseWrapper);
				TransactionCodeModel model = (TransactionCodeModel) baseWrapper.getBasePersistableModel();
				middlewareAdviceVO.setMicrobankTransactionCodeId(model.getTransactionCodeId());
			}
			PostedTransactionReportModel model = new PostedTransactionReportModel();
			model.setTransactionCodeId(middlewareAdviceVO.getMicrobankTransactionCodeId());
			model.setProductId(middlewareAdviceVO.getProductId());
			if(!"".equals(middlewareAdviceVO.getStan()) && null!=middlewareAdviceVO.getStan()) {
				model.setSystemTraceAuditNumber(middlewareAdviceVO.getStan());

			}
			model.setResponseCode("00");

			CustomList<PostedTransactionReportModel> customList = postedTransactionReportDAO.findByExample(model, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
			
			List<PostedTransactionReportModel> list = customList.getResultsetList();
			
			if(list != null && list.size() > 0){
				result = true;
			}
		}catch (Exception ex){
			//not throwing exception
			logger.error("Exception occured while CoreAdviceListener.checkAlreadySuccessful...TransactionCode:"+middlewareAdviceVO.getMicrobankTransactionCode(), ex);
		}
		
		return result;
	}

	
	public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
		this.transactionReversalManager = transactionReversalManager;
	}

	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}
    
	public void setPostedTransactionReportDAO( PostedTransactionReportDAO postedTransactionReportDAO ) {
        this.postedTransactionReportDAO = postedTransactionReportDAO;
    }

	public void setIbftSwitchController(IBFTSwitchController ibftSwitchController) {
		this.ibftSwitchController = ibftSwitchController;
	}
}
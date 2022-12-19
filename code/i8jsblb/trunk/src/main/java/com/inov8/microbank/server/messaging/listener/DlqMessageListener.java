package com.inov8.microbank.server.messaging.listener;

import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.microbank.common.model.MiddlewareRetryAdviceModel;
import com.inov8.microbank.common.model.ThirdPartyRetryAdviceModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.service.account.AccountManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Date;

public class DlqMessageListener implements MessageListener {
    protected static Log logger = LogFactory.getLog(DlqMessageListener.class);
    private TransactionReversalManager transactionReversalManager;
    private AccountManager accountManager;
    private SmsSenderService smsSenderService;

    public void onMessage(Message message) {

        MiddlewareAdviceVO middlewareAdviceVO = null;
        logger.info("Inside DlqMessageListener.onMessage()");
        try {
            if (message instanceof ObjectMessage) {
                Object obj = ((ObjectMessage) message).getObject();
                if (null != obj && obj instanceof MiddlewareAdviceVO) {
                    middlewareAdviceVO = (MiddlewareAdviceVO) obj;

                    if (middlewareAdviceVO.getAdviceType() != null && (middlewareAdviceVO.getAdviceType().equals(PortalConstants.IBFT_ADVICE_TYPE) || middlewareAdviceVO.getAdviceType().equals(PortalConstants.CREDIT_PAYMENT_ADVICE_TYPE))||middlewareAdviceVO.getAdviceType().equals(PortalConstants.DEBIT_PAYMENT_ADVICE_TYPE)) {
                        logger.info("[IBFT] Retries exhausted for mobileNo: " + middlewareAdviceVO.getAccountNo2() + " - Now saving data in DB.");
                        updateFailedIBFTStatus(middlewareAdviceVO);

                    } else {
                        logger.info("Retries exhausted for Trx ID: " + middlewareAdviceVO.getMicrobankTransactionCode() + " - Now saving data in DB. Is_Credit_Advice:" + middlewareAdviceVO.getIsCreditAdvice());
                        logFailedRecord(middlewareAdviceVO);
                    }

                } else if (obj instanceof OLAVO) {

                    OLAVO olaVO = (OLAVO) obj;
                    //populate AccountCreditQueueLogModel and update SafRepo
                    accountManager.saveFailedOLACreditDebit(olaVO);
                } else if (obj instanceof SmsMessage) {
                    SmsMessage msg = (SmsMessage) obj;
                    logger.info("[DLQ] Going to save Failed SMS in DB against mobile no:" + msg.getMobileNo());
                    smsSenderService.saveFailedSms(msg);
                    logger.info("[DLQ] Completed - Saving Failed SMS in DB against mobile no:" + msg.getMobileNo());
                } else if (obj instanceof SwitchWrapper) {
                    SwitchWrapper sWrapper = (SwitchWrapper) obj;
                    logger.info("[DLQ] Going to save Failed Third Party Cash Out in DB against Customer mobile no:" + sWrapper.getI8SBSwitchControllerRequestVO().getConsumerNumber() + " and Session ID:" + sWrapper.getI8SBSwitchControllerRequestVO().getSessionId());
                    logFailedRecord(sWrapper);
                    logger.info("[DLQ] Completed - Saving Failed Third Party Cash Out in DB against Customer mobile no:" + sWrapper.getI8SBSwitchControllerRequestVO().getConsumerNumber() + " and Session ID:" + sWrapper.getI8SBSwitchControllerRequestVO().getSessionId());
                }
            }
        } catch (Exception e) {
            logger.error("Unable to persist data in DlqMessageListener.logFailedRecord()", e);
        }

    }

    private void updateFailedIBFTStatus(MiddlewareAdviceVO middlewareAdviceVO) throws Exception {
        transactionReversalManager.updateIBFTStatus(middlewareAdviceVO.getStan(), middlewareAdviceVO.getRequestTime(), PortalConstants.IBFT_STATUS_FAILED, null);
    }

    private void logFailedRecord(MiddlewareAdviceVO middlewareAdviceVO) throws Exception {
        if (middlewareAdviceVO == null) {
            return;
        }

        MiddlewareRetryAdviceModel model = new MiddlewareRetryAdviceModel();
        model.setProductId(middlewareAdviceVO.getProductId());
        model.setIntgTransactionTypeId(middlewareAdviceVO.getIntgTransactionTypeId());
        model.setTransactionCodeId(middlewareAdviceVO.getMicrobankTransactionCodeId());
        model.setTransactionCode(middlewareAdviceVO.getMicrobankTransactionCode());
        model.setFromAccount(middlewareAdviceVO.getAccountNo1());
        model.setToAccount(middlewareAdviceVO.getAccountNo2());
        if (!middlewareAdviceVO.getTransactionAmount().equals("null")) {
            model.setTransactionAmount(Double.parseDouble(middlewareAdviceVO.getTransactionAmount()));
        }
        model.setStan(middlewareAdviceVO.getStan());
        model.setReversalStan(middlewareAdviceVO.getReversalSTAN());
        model.setTransmissionTime(middlewareAdviceVO.getTransmissionTime());
        model.setRequestTime(middlewareAdviceVO.getRequestTime());
        model.setReversalRequestTime(middlewareAdviceVO.getReversalRequestTime());

        model.setResponseCode(middlewareAdviceVO.getResponseCode());
        model.setCreatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
        model.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
        model.setCreatedOn(new Date());
        model.setUpdatedOn(new Date());
        model.setStatus("Failed");
        model.setTransactionId((Long) middlewareAdviceVO.getDataMap().get("TRANSACTION_ID"));
        model.setRetrievalReferenceNumber(middlewareAdviceVO.getRetrievalReferenceNumber());
        if (middlewareAdviceVO.getProductId() == 50055L || middlewareAdviceVO.getProductId() == 50056L)
            model.setConsumerNo(middlewareAdviceVO.getConsumerNo());

        if (middlewareAdviceVO.getIsBillPaymentRequest()) {
            model.setCnicNo(middlewareAdviceVO.getCnicNo());
            model.setCompnayCode(middlewareAdviceVO.getCompnayCode());
            model.setBillCategoryCode(middlewareAdviceVO.getBillCategoryId());
            model.setBillAggregator(middlewareAdviceVO.getBillAggregator());
            model.setActionLogId((Long) middlewareAdviceVO.getDataMap().get("ACTION_LOG_ID"));
            model.setConsumerNo(middlewareAdviceVO.getConsumerNo());
            model.setUbpBBStan(middlewareAdviceVO.getUbpStan());
//			model.setRrnKey(middlewareAdviceVO.getRrnKey());
        }

//		String adviceType = CoreAdviceUtil.getAdviceTypeName(middlewareAdviceVO);
        model.setAdviceType(middlewareAdviceVO.getAdviceType());

        transactionReversalManager.saveFailedAdviceRequiresNewTransaction(model);
    }

    private void logFailedRecord(SwitchWrapper switchWrapper) throws Exception {
        I8SBSwitchControllerRequestVO requestVO = switchWrapper.getI8SBSwitchControllerRequestVO();
        ThirdPartyRetryAdviceModel model = new ThirdPartyRetryAdviceModel();

        model.setCreatedBy(2L);
        model.setUpdatedBy(2L);
        model.setAgentAccountNumber(requestVO.getSenderMobile());
        model.setTransactionAmount(Double.valueOf(requestVO.getTransactionAmount()));
        model.setTransactionStatus(PortalConstants.SAF_STATUS_FAILED);
        model.setTransmissionTime(requestVO.getTransmissionDateAndTime());
        model.setStatus(PortalConstants.SAF_STATUS_FAILED);
        model.setProjectCode(requestVO.getProductCode());
        model.setTransactionCode(requestVO.getTranCode());
        model.setCustomerCnic(requestVO.getCNIC());
        model.setCustomerAccountNumber(requestVO.getConsumerNumber());
        model.setTerminalId(requestVO.getTerminalID());
        model.setSellerCode(requestVO.getAgentId());
        model.setSessionId(requestVO.getSessionId());
        model.setResponseCode(switchWrapper.getI8SBSwitchControllerResponseVO().getResponseCode());
        //model.setReserve1(requestVO.ge);

        transactionReversalManager.saveFailedAdviceRequiresNewTransaction(model);
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void setSmsSenderService(SmsSenderService smsSenderService) {
        this.smsSenderService = smsSenderService;
    }

}

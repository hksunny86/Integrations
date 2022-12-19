package com.inov8.microbank.authenticationmethod.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Atieq Rehman
 * @since 23 07 2015
 *
 */
public class OTPAuthentication implements AuthenticationMethod {
    private TransactionModuleManager transactionModuleManager;

    /**
     * perform step for for One Time PIN authentication
     *
     * <li>generates One Time PIN </li>
     * <li>sets {@link MiniTransactionModel} with available information </li>
     * <li>send SMS to recipient</li>
     *
     */
    @Override
    public WorkFlowWrapper initiate(WorkFlowWrapper workflowWrapper) throws FrameworkCheckedException {

        // generate new transaction code
        TransactionCodeModel txCodeModel = workflowWrapper.getTransactionCodeModel();
        if (txCodeModel == null) {
            workflowWrapper = transactionModuleManager.generateTransactionCodeRequiresNewTransaction(workflowWrapper);
            txCodeModel = workflowWrapper.getTransactionCodeModel();
        }

        Long commandId = Long.valueOf(String.valueOf(workflowWrapper.getObject(CommandFieldConstants.KEY_CURR_COMMAND_ID)));
        String mobileNo = String.valueOf(workflowWrapper.getObject(CommandFieldConstants.KEY_MOB_NO));

        CommissionAmountsHolder commissionAmountsHolder = workflowWrapper.getCommissionAmountsHolder();
        String productName = workflowWrapper.getProductModel().getName();

        if(StringUtils.isEmpty(mobileNo)) {
            throw new FrameworkCheckedException("Mobile no is required");
        }

        if(StringUtils.isEmpty(productName)) {
            throw new FrameworkCheckedException("Product Name is required");
        }

        String pin = null;

        if (workflowWrapper.getOneTimePin() != null) {
            pin = workflowWrapper.getOneTimePin();
        }else{// Generate OneTimePin
            pin = CommonUtils.generateOneTimePin(CommandFieldConstants.KEY_MFS_PIN_LENGTH);
            workflowWrapper.setOneTimePin(pin);
        }

        Double transactionAmount = 0.0;
        if(commissionAmountsHolder != null)
            transactionAmount = commissionAmountsHolder.getTransactionAmount();


        //Populate & Save Mini Transaction Model
        String encryptedPin = EncoderUtils.encodeToSha(pin);

        MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
        miniTransactionModel.setTransactionCodeIdTransactionCodeModel(txCodeModel);
        miniTransactionModel.setCommandId(commandId) ;
        miniTransactionModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
        miniTransactionModel.setTimeDate(new Date()) ;
        miniTransactionModel.setMobileNo(mobileNo);
        miniTransactionModel.setSmsText(mobileNo + " " + transactionAmount) ;
        miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT) ;
        miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
        miniTransactionModel.setPlainOTP(pin);
        miniTransactionModel.setOneTimePin(encryptedPin);
        if(workflowWrapper.getObject(CommandFieldConstants.KEY_MAN_OT_PIN) != null) {
            miniTransactionModel.setIsManualOTPin((Boolean) workflowWrapper.getObject(CommandFieldConstants.KEY_MAN_OT_PIN)? Boolean.TRUE : Boolean.FALSE);
        }
        if(commissionAmountsHolder != null) {
            miniTransactionModel.setCAMT(commissionAmountsHolder.getTotalCommissionAmount()) ;
            miniTransactionModel.setBAMT(commissionAmountsHolder.getTransactionAmount()) ;
            miniTransactionModel.setTAMT(commissionAmountsHolder.getTotalAmount()) ;
            miniTransactionModel.setTPAM(commissionAmountsHolder.getTransactionProcessingAmount()) ;
        }

        // save mini transaction
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(miniTransactionModel);
        transactionModuleManager.saveMiniTransaction(baseWrapper);

        workflowWrapper.setMiniTransactionModel(miniTransactionModel);
        String smsSendingRequired = (String)workflowWrapper.getObject("SEND_SMS");

        if(StringUtil.isNullOrEmpty(smsSendingRequired) || CommonUtils.convertToBoolean(smsSendingRequired)) {
            //send SMS having OTP
            ArrayList<SmsMessage> messageList = new ArrayList<>();
            messageList.add(new SmsMessage(mobileNo, MessageUtil.getMessage("otpSmsMessage", new Object[]{productName, pin, txCodeModel.getCode()})));
            workflowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
        }

        else {
            // calling method will manage sending sms by itself
        }

        return workflowWrapper;
    }

    /**
     * perform step 2 for One Time PIN authentication
     *
     * <li>validates required inputs</li>
     * <li>validates OTP</li>
     * <li>manage re-try count on wrong attempts</li>
     * <li>marked OTP as expired when retries exhausted </li>
     */
    @Override
    public WorkFlowWrapper validate(WorkFlowWrapper workflowWrapper) throws FrameworkCheckedException {

        TransactionCodeModel transactionCodeModel = workflowWrapper.getTransactionCodeModel();
        String transactionCode = transactionCodeModel.getCode();

        if(StringUtils.isEmpty(transactionCode)) {
            throw new FrameworkCheckedException("Transaction Code is required");
        }

        String oneTimePIN = workflowWrapper.getOneTimePin();
        if(StringUtil.isNullOrEmpty(oneTimePIN)) {
            throw new FrameworkCheckedException("One time PIN is required.");
        }

        oneTimePIN = CommonUtils.decryptPin(oneTimePIN, EncryptionUtil.ENCRYPTION_TYPE_AES);
        if(StringUtils.isEmpty(oneTimePIN)) {
            throw new FrameworkCheckedException("One time PIN is required");
        }

        MiniTransactionModel miniTransactionModel = workflowWrapper.getMiniTransactionModel();
        if(miniTransactionModel == null) {
            miniTransactionModel = transactionModuleManager.loadMiniTransactionModelByTransactionCode(transactionCode);
        }

        long errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
        if(miniTransactionModel == null) {
            throw new FrameworkCheckedException("Error in loading OTP details", null, errorCode, new Throwable());
        }

        long transactionStateId = miniTransactionModel.getMiniTransactionStateId().longValue();
        if(transactionStateId != MiniTransactionStateConstant.PIN_SENT) {
            String errorMessage = MessageUtil.getMessage("MINI.TransactionClosed");

            if(MiniTransactionStateConstant.OT_PIN_EXPIRED == transactionStateId)
                errorMessage = MessageUtil.getMessage("MINI.ExpiredPIN");

            throw new FrameworkCheckedException(errorMessage, null, errorCode, null);
        }

        boolean isTxCodeInvalid = false;
        String errorMessage = "";
        String encryptedOTPin = EncoderUtils.encodeToSha(oneTimePIN);
        Long retryCount = miniTransactionModel.getOtRetryCount();
        long otPinRetryCount = retryCount != null ? retryCount.longValue() : 0;

        if (!miniTransactionModel.getOneTimePin().equals(encryptedOTPin)) {
            isTxCodeInvalid = true;

            if(otPinRetryCount + 1 >= MessageUtil.getOTPRetryLimit()) {
                miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.OT_PIN_EXPIRED);
                errorMessage = MessageUtil.getMessage("MINI.ExpiredPIN");
                errorCode = ErrorCodes.OTP_EXPIRED;
            }else {
                errorMessage = MessageUtil.getMessage("MINI.MismatchedTransactionCode");
            }

            miniTransactionModel.setOtRetryCount(++otPinRetryCount);
        }

        workflowWrapper.setMiniTransactionModel(miniTransactionModel);

        if(isTxCodeInvalid) {
            miniTransactionModel = transactionModuleManager.updateMiniTransactionRequiresNewTransaction(miniTransactionModel) ;

            throw new FrameworkCheckedException(errorMessage, null, errorCode, null);
        }

        return workflowWrapper;
    }

    /**
     *  perform step 3 for One Time PIN authentication
     *
     *  <li>load and lock respective {@link MiniTransactionModel}</li>
     *  <li>mark as completed</li>
     *  <li>throws exception if already in progress</li>
     */
    @Override
    public WorkFlowWrapper process(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        try {
            // update and release lock on mini transaction
            if(workFlowWrapper.getMiniTransactionModel() != null){
                MiniTransactionModel miniTransactionModel = workFlowWrapper.getMiniTransactionModel();
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(miniTransactionModel);
                baseWrapper = transactionModuleManager.loadAndLockMiniTransaction(baseWrapper);
                miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();

                if(MiniTransactionStateConstant.PIN_SENT.longValue() != miniTransactionModel.getMiniTransactionStateId()) {
                    throw new FrameworkCheckedException("Transaction is already processed");
                }

                miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.COMPLETED);

                baseWrapper.setBasePersistableModel(miniTransactionModel);
                baseWrapper = transactionModuleManager.updateMiniTransaction(baseWrapper);

                workFlowWrapper.setMiniTransactionModel(miniTransactionModel);
                workFlowWrapper.setTransactionCodeModel(miniTransactionModel.getTransactionCodeIdTransactionCodeModel());
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            workFlowWrapper.putObject(CommandFieldConstants.KEY_IS_CONCURRENT_TRANSACTION, Boolean.TRUE);
            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.TRX_IN_PROCESS_MSG);
        }

        return workFlowWrapper;
    }

    @Override
    public WorkFlowWrapper updateTransactionDetails(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		/*TransactionDetailMasterModel tdmModel = workFlowWrapper.getTransactionDetailMasterModel();
		tdmModel.setAuthenticationMethodId(AuthenticationMethodEnum.ONE_TIME_PIN.getId());
		tdmModel.setAuthenticationMethod(AuthenticationMethodEnum.ONE_TIME_PIN.getName());

		TransactionDetailModel tdModel = workFlowWrapper.getTransactionDetailModel();
		tdModel.setAuthenticationMethodId(AuthenticationMethodEnum.ONE_TIME_PIN.getId());*/

        return workFlowWrapper;
    }

    public void setTransactionModuleManager(TransactionModuleManager transactionModuleManager) {
        this.transactionModuleManager = transactionModuleManager;
    }
}
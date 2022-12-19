package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.ola.util.CustomerAccountTypeConstants;

import java.util.List;

public class BLBToCNICInfoCommand extends FundTransferInfoCommand {

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        super.prepare(baseWrapper);

        senderMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        receiverMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
        receiverCNIC = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
        String senderCNIC = null;

        try {
            senderAppUserModel = ccManager.loadAppUserByQuery(senderMobileNo, UserTypeConstantsInterface.CUSTOMER);
            //receiverAppUserModelList = ccManager.getAppUserManager().findAppUserByCnicAndMobile(receiverMobileNo , receiverCNIC);
            //no registered user should exists with receiver mobile no
            receiverAppUserModel = ccManager.checkAppUserTypeAsWalkinCustoemr(receiverMobileNo);

//			productVO = ccManager.loadProductVO(baseWrapper);

            senderSegmentId = ccManager.getCustomerSegmentIdByMobileNo(senderMobileNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        super.validate(validationErrors);

        String classNMethodName = getClass().getSimpleName() + ".validate(): ";
        ValidatorWrapper.doRequired(senderMobileNo, validationErrors, "Sender Mobile No");
        ValidatorWrapper.doRequired(receiverMobileNo, validationErrors, "Receiver Mobile No");
        ValidatorWrapper.doRequired(receiverCNIC, validationErrors, "Receiver CNIC");
        String error = "";
        if (senderMobileNo.equals(receiverMobileNo)) {
            error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_REC_SAME_MOB", null, null);
            logger.error(classNMethodName + error);
            ValidatorWrapper.addError(validationErrors, error);
        }

        if (agentMobileNo.equals(senderMobileNo)) {
            error = this.getMessageSource().getMessage("MONEYTRANSFER.AGENT_MOB_CANT_BE_SENDER", null, null);
            logger.error(classNMethodName + error);
            ValidatorWrapper.addError(validationErrors, error);
        }

        if (agentMobileNo.equals(receiverMobileNo)) {
            error = this.getMessageSource().getMessage("MONEYTRANSFER.AGENT_MOB_CANT_BE_RECEIVER", null, null);
            logger.error(classNMethodName + error);
            ValidatorWrapper.addError(validationErrors, error);
        }

        if (null == senderAppUserModel) {
            error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_NOT_REG", null, null);
            logger.error(classNMethodName + error);
            ValidatorWrapper.addError(validationErrors, error);
        } else {
            try {
                validateBBCustomer(senderAppUserModel, "Sender - ");
                validsateSmartMoneyAccount(senderAppUserModel, "Sender - ");
            } catch (FrameworkCheckedException e) {
                logger.error(classNMethodName + e.getMessage());
                ValidatorWrapper.addError(validationErrors, e.getMessage());
            }

            //***************************************************************************************************************************
            //									Check if sender or receiver cnic is blacklisted
            //***************************************************************************************************************************
            senderCNIC = senderAppUserModel.getNic();

            if (!validationErrors.hasValidationErrors()) {
                if (this.getCommonCommandManager().isCnicBlacklisted(senderCNIC)) {
                    validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
                }
                if (this.getCommonCommandManager().isCnicBlacklisted(receiverCNIC)) {
                    validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
                }
            }
            //***************************************************************************************************************************

        }


        try {
            if (ccManager.loadAppUserByCnicAndType(receiverCNIC) != null) {
                error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_NIC_ALREADY_REG", null, null);
                logger.error(classNMethodName + error);
                ValidatorWrapper.addError(validationErrors, error);
            }

        } catch (FrameworkCheckedException fe) {
            fe.printStackTrace();
        }
        if (null != receiverAppUserModel) {
            error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_MOB_ALREAY_REG", null, null);
            logger.error(classNMethodName + error);
            ValidatorWrapper.addError(validationErrors, error);
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        super.execute();

        try {
//				 if(senderAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
            CustomerModel customerModel = new CustomerModel();
            customerModel = commonCommandManager.getCustomerModelById(senderAppUserModel.getCustomerId());
            if (customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                logger.error("[CashDepositInfoCommand.execute] Upgrade Account L0 to L1 to perform Transaction.: " + productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + senderMobileNo);
                throw new CommandException(MessageUtil.getMessage("upgrade.customer.L1.account"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

            } else {
                ccManager.checkCustomerBalance(senderMobileNo, commissionAmountsHolder.getTotalAmount());
                SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
                sma.setCustomerId(senderAppUserModel.getCustomerId());

                sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);
                commonCommandManager.validateBalance(senderAppUserModel, smartMoneyAccountModel, commissionAmountsHolder.getTotalAmount(), true);
            }
        } catch (WorkFlowException wex) {
            wex.printStackTrace();
            throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }
    }

    @Override
    public String response() {

        List<LabelValueBean> params = super.getResponse();

        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(senderMobileNo)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE, replaceNullWithEmpty(receiverMobileNo)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_R_W_CNIC, replaceNullWithEmpty(receiverCNIC)));


        return MiniXMLUtil.createInfoResponseXMLByParams(params);
    }
}
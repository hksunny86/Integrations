package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.inov8.microbank.common.model.TransactionModel;


import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class AgentCashDepositCommand extends BaseCommand {

    protected AppUserModel agentAppUserModel;
    protected String productId;
    protected String accountId;
    protected String txProcessingAmount;
    protected String pin;
    protected String deviceTypeId;
    protected String commissionAmount;
    protected String totalAmount;
    protected String cvv;
    protected String tPin;

    protected String accountType;
    protected String accountCurrency;
    protected String accountStatus;
    protected String accountNumber;
    protected double discountAmount = 0d;


    TransactionModel transactionModel;
    ProductModel productModel;
    String successMessage;
    BaseWrapper baseWrapper;
    SmartMoneyAccountModel smartMoneyAccountModel;
    UserDeviceAccountsModel userDeviceAccountsModel;
    WorkFlowWrapper workFlowWrapper;
    RetailerModel retailerModel;

    RetailerContactModel fromRetailerContactModel;
    long segmentId;

    //    protected String customerMobileNo;
//    protected String customerCNIC;
    protected String agentMobileNumber;
    protected String agentCNIC;
    protected String txAmount;
    protected String retailerId;
    private String terminalId;
    private String channelId;
    private String paymentMode;
    private String thirdPartyTransactionId;
    private String stan;
    DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");
    double balance = 0D;

    protected final Log logger = LogFactory.getLog(AgentCashDepositCommand.class);

    @Override
    public void execute() throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of AgentCashDepositCommand.execute()");
        }

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        if (agentAppUserModel.getRetailerContactId() != null) {
            try {
                String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

                // Velocity validation - start
                BaseWrapper bWrapper = new BaseWrapperImpl();
                bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
                bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
                bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, fromRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
                bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, fromRetailerContactModel.getDistributorLevelId());
                bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(txAmount));
                bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, fromRetailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
                bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//                bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);

                bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segmentId));
                boolean result = commonCommandManager.checkVelocityCondition(bWrapper);
                // Velocity validation - end


                ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(agentAppUserModel);

                if (!validationErrors.hasValidationErrors()) {
                    AccountInfoModel accountInfoModel = new AccountInfoModel();
                    accountInfoModel.setOldPin(pin);

                    productModel = new ProductModel();
                    productModel.setProductId(Long.parseLong(productId));

                    agentAppUserModel.setMobileNo(agentMobileNumber);
                    agentAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);

                    TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                    transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.AGENT_CASH_DEPOSIT_TX);

                    smartMoneyAccountModel = new SmartMoneyAccountModel();
                    smartMoneyAccountModel.setRetailerContactId(agentAppUserModel.getRetailerContactId());
                    Long paymentModeId = null;

                    if (paymentMode.equals("Agent")) {
                        paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
                    }

                    smartMoneyAccountModel.setPaymentModeId(paymentModeId);
                    smartMoneyAccountModel.setDistributorContactId(agentAppUserModel.getDistributorContactId());

                    SmartMoneyAccountModel smaVerification = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(agentAppUserModel, paymentModeId);
                    if (smaVerification == null)
                        throw new CommandException("HRA Account does not Exist.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    else if (smaVerification != null && smaVerification.getStatusId() != null && smaVerification.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE))
                        throw new CommandException("Your Account is In-Active.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    else if (smaVerification != null && smaVerification.getStatusId() != null && smaVerification.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED))
                        throw new CommandException("Your Account is Blocked.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    retailerModel = new RetailerModel();
                    retailerModel.setPrimaryKey(fromRetailerContactModel.getRetailerId());

                    NotificationMessageModel notificationMessageModel = new NotificationMessageModel();
                    transactionModel = new TransactionModel();
                    transactionModel.setDeviceTypeId(Long.valueOf(deviceTypeId));
                    userDeviceAccountsModel = new UserDeviceAccountsModel();
                    DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                    deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

                    workFlowWrapper.setProductModel(productModel);
                    workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                    workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                    workFlowWrapper.setTransactionModel(transactionModel);
                    workFlowWrapper.setAppUserModel(agentAppUserModel);
                    baseWrapper.setBasePersistableModel(retailerModel);
                    baseWrapper = commonCommandManager.loadRetailer(baseWrapper);
                    retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();
                    workFlowWrapper.setRetailerModel(retailerModel);
                    workFlowWrapper.setUserDeviceAccountModel(userDeviceAccountsModel);
                    workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
                    workFlowWrapper.setAccountInfoModel(accountInfoModel);
                    workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
                    workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
                    workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
                    workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
                    workFlowWrapper.setRetailerAppUserModel(agentAppUserModel);
                    workFlowWrapper.setFromRetailerContactAppUserModel(agentAppUserModel);
                    workFlowWrapper.setCcCVV(this.cvv);
                    workFlowWrapper.setMPin(this.tPin);
                    workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
                    workFlowWrapper.setSuccessMessage(notificationMessageModel);
                    workFlowWrapper.setFromRetailerContactModel(fromRetailerContactModel);
                    workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
                    workFlowWrapper.setToSegmentId(segmentId);

                    SegmentModel segmentModel = new SegmentModel();
                    segmentModel.setSegmentId(segmentId);
                    workFlowWrapper.setSegmentModel(segmentModel);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, agentAppUserModel.getNic());
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, channelId);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, paymentMode);
                    workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, thirdPartyTransactionId);



                    logger.info("[CashDepositCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + agentAppUserModel.getAppUserId() +
                            " Agent Mobile No:" + agentMobileNumber + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);

                    workFlowWrapper.setLeg2Transaction(Boolean.FALSE);
                    workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

                    transactionModel = workFlowWrapper.getTransactionModel();
                    smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();

                    productModel = workFlowWrapper.getProductModel();
                    userDeviceAccountsModel = workFlowWrapper.getUserDeviceAccountModel();
                    discountAmount = workFlowWrapper.getDiscountAmount();
                    successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();

                } else {
                    logger.error("[AgentCashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + agentAppUserModel.getAppUserId() + " Agent Mobile No:" + agentMobileNumber + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
            } catch (FrameworkCheckedException ex) {
                if (logger.isErrorEnabled()) {
                    logger.error("[AgentCashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + agentAppUserModel.getAppUserId() + " Agent Mobile No:" + agentMobileNumber + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
                }
                throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
            } catch (WorkFlowException wex) {
                if (logger.isErrorEnabled()) {
                    logger.error("[AgentCashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + agentAppUserModel.getAppUserId() + " Agent Mobile No:" + agentMobileNumber + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
                }
                throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
            } catch (Exception ex) {
                ex.printStackTrace();

                if (logger.isErrorEnabled()) {
                    logger.error("[AgentCashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + agentAppUserModel.getAppUserId() + " Agent Mobile No:" + agentMobileNumber + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
                }
                throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
            }
        } else {
            logger.error("[AgentCashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + agentAppUserModel.getAppUserId() + " Agent Mobile No:" + agentMobileNumber + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);
            throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of AgentCashDepositCommand.execute()");
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        this.baseWrapper = baseWrapper;
        agentMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOB_NO);
        Long[] appUserTypeIds = {UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};
        try {
            agentAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(agentMobileNumber, appUserTypeIds);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }

        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
        accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
        accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
        accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);

        agentMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        agentCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
        totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
        commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
        txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        channelId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        terminalId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        paymentMode = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE);
        thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
        stan = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_STAN);

        pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
        pin = StringUtil.replaceSpacesWithPlus(pin);
        cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
        cvv = this.decryptPin(cvv);
        tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
        tPin = StringUtil.replaceSpacesWithPlus(tPin);
        tPin = this.decryptPin(tPin);

        if (null != this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT) && !"".equals(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT))) {
            discountAmount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT));
        }

        BaseWrapper bWrapper = new BaseWrapperImpl();
        RetailerContactModel retailerContactModel = new RetailerContactModel();
        retailerContactModel.setRetailerContactId(agentAppUserModel.getRetailerContactId());
        bWrapper.setBasePersistableModel(retailerContactModel);

        try {
            bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);

            this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

        } catch (Exception ex) {
            logger.error("[AgentCashDepositCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {

        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
//        validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Processing Amount");
//        validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
//        validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
        validationErrors = ValidatorWrapper.doRequired(txAmount, validationErrors, "Tx Amount");
        validationErrors = ValidatorWrapper.doRequired(agentMobileNumber, validationErrors, "Agent Mobile No");

        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
//            validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Processing Amount");
            validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");
//            validationErrors = ValidatorWrapper.doNumeric(totalAmount, validationErrors, "Total Amount");
        }
        return validationErrors;
    }

    @Override
    public String response() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start/End of AgentCashDepositCommand.response()");
        }
        return toXML();
    }

    private String toXML() {

        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
        params.add(new LabelValueBean(ATTR_TRXID, replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(ATTR_CMOB, replaceNullWithEmpty(agentMobileNumber)));
        params.add(new LabelValueBean(ATTR_CNIC, replaceNullWithEmpty(agentCNIC)));
        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn() + "")));
        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT)));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        params.add(new LabelValueBean(ATTR_PROD, replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount() + "")));
        params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
        params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(txProcessingAmount)));
        params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(Double.parseDouble(txProcessingAmount))));
        params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount() + "")));
        params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount() + "")));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
        params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(workFlowWrapper.getSwitchWrapper().getAgentBalance())));

        return MiniXMLUtil.createResponseXMLByParams(params);

    }
}
package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class DebitPaymentApiCommand extends BaseCommand {
    private final Log logger = LogFactory.getLog(DebitInquiryApiCommand.class);

    private String customerMobileNo;
    private String productId;
    private AppUserModel appUserModel;
    private ProductModel productModel;
    private CommissionAmountsHolder commissionAmountsHolder;
    private String transactionAmount;
    private WorkFlowWrapper workFlowWrapper;
    private BaseWrapper baseWrapper;
    private String commissionAmount;
    private String processingAmount;
    private String totalAmount;
    private Long transactionTypeId = null;
    private TransactionModel transactionModel;
    private String accountTitle = "";
    private String accountId;
    private Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
    private Double balance = 0D;
    private String channelId;
    private String terminalId;
    private String thirdPartyTransactionId;
    private String stan;
    private String profitKey;
    private String reserved4;
    private String reserved5;
    private String reserved8;
    private String reserved9;
    private String reserved10;


    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of DebitPaymentApiCommand.prepare()");
        }

        //********************************Request Parameters**************************************************
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        commissionAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAMT);
        transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        processingAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
        totalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        channelId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        terminalId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        stan = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_STAN);
        thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
        profitKey = this.getCommandParameter(baseWrapper , CommandFieldConstants.KEY_PROFIT_KEY);
        reserved4 = this.getCommandParameter(baseWrapper , CommandFieldConstants.KEY_RESERVED_4);
        reserved5 = this.getCommandParameter(baseWrapper , CommandFieldConstants.KEY_RESERVED_5);
        reserved8 = this.getCommandParameter(baseWrapper , CommandFieldConstants.KEY_RESERVED_8);
        reserved9 = this.getCommandParameter(baseWrapper , CommandFieldConstants.KEY_RESERVED_9);
        reserved10 = this.getCommandParameter(baseWrapper , CommandFieldConstants.KEY_RESERVED_10);

        logger.info("[DebitPaymentApiCommand.prepare] \nLogged In AppUserModel: " + appUserModel.getAppUserId() +
                "\n Product ID:" + productId +
                "\n deviceTypeId: " + deviceTypeId +
                "\n customerMobileNumber: " + customerMobileNo);

        if (logger.isDebugEnabled()) {
            logger.debug("End of DebitPaymentApiCommand.prepare()");
        }
        this.baseWrapper = baseWrapper;
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of DebitInfoApiCommand.validate()");
        }

        BaseWrapper bWrapper = new BaseWrapperImpl();
        ProductModel productModel = new ProductModel();
        productModel.setProductId(Long.valueOf(productId));
        bWrapper.setBasePersistableModel(productModel);

        try {
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
            throw new CommandException("Product not found against given product code " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        productModel = (ProductModel) bWrapper.getBasePersistableModel();
        if (productModel.getProductId() != null) {
            productId = productModel.getProductId().toString();
        }

        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
        }


        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        workFlowWrapper = new WorkFlowWrapperImpl();

        try {
            ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
            if (validationErrors.hasValidationErrors()) {
                logger.error("[DebitPaymentApi.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo);
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            CustomerModel customerModel = null;
            if (appUserModel != null) {
                customerModel = new CustomerModel();
                customerModel.setCustomerId(appUserModel.getCustomerId());

                BaseWrapper bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(customerModel);
                bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
                if (bWrapper.getBasePersistableModel() != null) {
                    customerModel = (CustomerModel) bWrapper.getBasePersistableModel();
                    segmentId = customerModel.getSegmentId();
                }
                workFlowWrapper.setTransactionAmount(Double.parseDouble(transactionAmount));
                if (!StringUtil.isNullOrEmpty(processingAmount)) {
                    workFlowWrapper.setTxProcessingAmount(Double.parseDouble(processingAmount));
                } else {
                    workFlowWrapper.setTxProcessingAmount(0.0D);
                }

                if (!StringUtil.isNullOrEmpty(commissionAmount)) {
                    workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
                } else {
                    workFlowWrapper.setTotalCommissionAmount(0.0D);
                }


                baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);

                workFlowWrapper.setCustomerAppUserModel(appUserModel);
                workFlowWrapper.setCustomerAppUserModel(appUserModel);
                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(segmentId);
                workFlowWrapper.setSegmentModel(segmentModel);
                workFlowWrapper.setIsCustomerInitiatedTransaction(true);
                transactionTypeId = TransactionTypeConstantsInterface.DEBIT_API_TX;

                //**********************************************************************
                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId(transactionTypeId);

                //*********************************************************************
                DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
                SmartMoneyAccountModel olaSmartMoneyAccountModel = getsmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                AccountInfoModel accountInfoModel = null;
                accountInfoModel = getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                if (olaSmartMoneyAccountModel == null) {
                    throw new CommandException("Branchless Banking Account is not defined", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, new Throwable());
                } else {
                    accountId = olaSmartMoneyAccountModel.getSmartMoneyAccountId().toString();
                    accountTitle = olaSmartMoneyAccountModel.getName();
                }
                SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                long accountType = customerModel.getCustomerAccountTypeId();
                String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
                this.checkVelocityCondition(segmentId, accountType, userId);
                workFlowWrapper.setSwitchWrapper(switchWrapper);
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setCustomerAppUserModel(appUserModel);
                workFlowWrapper.setCustomerModel(customerModel);
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                workFlowWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
                workFlowWrapper.setOlaSmartMoneyAccountModel(olaSmartMoneyAccountModel);
                workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
                workFlowWrapper.setAccountInfoModel(accountInfoModel);
                workFlowWrapper.setAppUserModel(appUserModel);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
                transactionModel = new TransactionModel();
                transactionModel.setTransactionAmount(Double.parseDouble(transactionAmount));
                workFlowWrapper.setTransactionModel(transactionModel);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, channelId);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
                workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, thirdPartyTransactionId);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_PROFIT_KEY, profitKey);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_RESERVED_4, reserved4);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_RESERVED_5, reserved5);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_RESERVED_8, reserved8);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_RESERVED_9, reserved9);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_RESERVED_10, reserved10);


                logger.info("[DebitPaymentCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() +
                        " Customer Mobile No:" + customerMobileNo);
                //Boolean isPayByCustomerAccount = ((Integer)workFlowWrapper.getCustomField()).intValue() == 0;
                workFlowWrapper.setCustomField(0);
                workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
                //*************************************************************************************************************
                //*************************************************************************************************************
//                isInclusiveCharges = workFlowWrapper.getCommissionAmountsHolder().getIsInclusiveCharges();
                transactionModel = workFlowWrapper.getTransactionModel();
                productModel = workFlowWrapper.getProductModel();
                //Customer Balance
                balance = workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction();
                //consumerNumber = ((UtilityBillVO) workFlowWrapper.getProductVO()).getConsumerNo();
                commonCommandManager.sendSMS(workFlowWrapper);


            }
        } catch (FrameworkCheckedException ex) {
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        } catch (WorkFlowException wex) {
            throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }
    }

    @Override
    public String response() {
        return toXML();
    }

    private String toXML() {

        TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
        params.add(new LabelValueBean(ATTR_TRXID, replaceNullWithEmpty(workFlowWrapper.getTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_BILL_AMOUNT, replaceNullWithEmpty(transactionAmount)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, Formatter.formatNumbers(replaceNullWithZero(Double.parseDouble(transactionAmount)))));
        params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount() + "")));
        params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(replaceNullWithZero(transactionModel.getTotalCommissionAmount()))));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(customerMobileNo)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, replaceNullWithEmpty(productModel.getDescription())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PESSENGER_NAME, replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount() + "")));
        params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
        params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount() + "")));
        params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount())));
        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount() + "")));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn() + "")));
        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));
        params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(balance)));

        return MiniXMLUtil.createResponseXMLByParams(params);


    }

    private SmartMoneyAccountModel getsmartMoneyAccountModel(AppUserModel _appUserModel, Long paymentModeId) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SmartMoneyAccountModel customerSMAModel = new SmartMoneyAccountModel();
        customerSMAModel.setCustomerId(appUserModel.getCustomerId());
        customerSMAModel.setActive(Boolean.TRUE);
        customerSMAModel.setDeleted(Boolean.FALSE);
        customerSMAModel.setDefAccount(Boolean.TRUE);
        customerSMAModel.setPaymentModeId(paymentModeId);
        searchBaseWrapper.setBasePersistableModel(customerSMAModel);

        searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

        @SuppressWarnings("rawtypes")
        CustomList smaList = searchBaseWrapper.getCustomList();
        if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
            customerSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
        }

        return customerSMAModel;
    }

    void checkVelocityCondition(Long segmentId, Long accountType, String userId) throws FrameworkCheckedException {

        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
        bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
        bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, -1L);
        bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, -1L);
        bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(transactionAmount));
//		bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, -1L);

        if (segmentId != null && CommissionConstantsInterface.DEFAULT_SEGMENT_ID.longValue() != segmentId.longValue()) {
            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, segmentId);
        }

        if (accountType != null) {
            bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, accountType);
        }
        if (userId != null) {
            bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
        }
        getCommonCommandManager().checkVelocityCondition(bWrapper);
    }
}

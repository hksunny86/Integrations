package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DebitCardCashWithDrawlCommand extends BaseCommand {
    private AppUserModel appUserModel, customerAppUserModel;
    private BaseWrapper baseWrapper;
    private String customerMobileNo, withdrawalAmount;
    private String productId, encryptionType;
    private String processingAmount, totalAmount;
    private ProductModel productModel;
    private CustomerModel customerModel;
    private CommissionAmountsHolder commissionAmountsHolder;
    private WorkFlowWrapper workFlowWrapper;
    private TransactionModel transactionModel;

    private String terminalId;
    private String stan;
    private String rrn;
    private String paymentMode;
    private String cardAcceptor;
    private String appId;
    String userId;

    protected final Log logger = LogFactory.getLog(DebitCardCashWithDrawlCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[DebitCardCashWithDrawlCommand.prepare] ";
        this.logger.info("Start of " + classNMethodName);
        this.baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        appId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_ID);
        terminalId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        stan = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_STAN);
        rrn = getCommandParameter(baseWrapper, "RRN");
        deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        customerMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        withdrawalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
        encryptionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        processingAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
        totalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
        paymentMode = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE);
        cardAcceptor = getCommandParameter(baseWrapper, "ACCEPTOR_DETAILS");
        try {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel) bWrapper.getBasePersistableModel();
        } catch (Exception ex) {
            logger.error(classNMethodName + " Product model not found: ", ex);
        }

        try {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            customerAppUserModel = new AppUserModel();
            customerAppUserModel.setMobileNo(customerMobileNo);
            customerAppUserModel = getCommonCommandManager().loadAppUserByQuery(customerMobileNo, UserTypeConstantsInterface.CUSTOMER);
            if (customerAppUserModel != null) {
                customerModel = new CustomerModel();
                customerModel.setCustomerId(customerAppUserModel.getCustomerId());
                bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(customerModel);
                bWrapper = getCommonCommandManager().loadCustomer(bWrapper);
                customerModel = (CustomerModel) bWrapper.getBasePersistableModel();
            }

        } catch (Exception ex) {
            logger.error(classNMethodName + "Customer App user model not found: ", ex);
        }
        this.logger.info("End of " + classNMethodName);
    }

    @Override
    public void doValidate() throws CommandException {
        String classNMethodName = "[DebitCardCashWithDrawlCommand.validate] ";
        this.logger.info("Start of " + classNMethodName);
        validationErrors = new ValidationErrors();
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product Id");
        validationErrors = ValidatorWrapper.doRequired(stan, validationErrors, "STAN");
        validationErrors = ValidatorWrapper.doRequired(terminalId, validationErrors, "Terminal ID");
        //validationErrors = ValidatorWrapper.doRequired(encryptionType, validationErrors, "Encryption Type");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");
        validationErrors = ValidatorWrapper.doRequired(withdrawalAmount, validationErrors, "Withdrawal Amount");
        validationErrors = ValidatorWrapper.doRequired(processingAmount, validationErrors, "Processing Amount");
        validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");
        if (validationErrors != null && !validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(withdrawalAmount, validationErrors, "Withdrawal Amount");
            validationErrors = ValidatorWrapper.doNumeric(processingAmount, validationErrors, "Processing Amount");
            validationErrors = ValidatorWrapper.doNumeric(totalAmount, validationErrors, "Total Amount");
        }
        if (null == productModel) {
            logger.error(classNMethodName + "Product model not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Product not found.");
        }
        if (null == customerAppUserModel) {
            logger.error(classNMethodName + "Customer App User not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
        }

        if (null == customerModel) {
            logger.error(classNMethodName + "Customer model not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
        }

        try {
            ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(appUserModel);
            if (userValidation.hasValidationErrors()) {
                logger.error(classNMethodName + " Customer validation failed.");
                validationErrors = ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
            }
        } catch (FrameworkCheckedException e) {
            logger.error(classNMethodName + e.getMessage());
            validationErrors = ValidatorWrapper.addError(validationErrors, e.getMessage());
        }
        this.logger.info("End of " + classNMethodName);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return new ValidationErrors();
    }

    @Override
    public void execute() throws CommandException {
        String classNMethodName = "[DebitCardCashWithDrawlCommand.execute()] ";
        this.logger.info("Start of " + classNMethodName);
        workFlowWrapper = new WorkFlowWrapperImpl();

        try {
            AccountInfoModel accountInfoModel = new AccountInfoModel();
            accountInfoModel.setOldPin(""); // This PIN should never be used for validation
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            workFlowWrapper.setTxProcessingAmount(Double.parseDouble(processingAmount));
            workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
            workFlowWrapper.setTransactionAmount(Double.parseDouble(withdrawalAmount));
            workFlowWrapper.setCardAcceptorDetails(cardAcceptor);
            workFlowWrapper.setHandlerModel(handlerModel);
            workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
            if (ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel() != null) {
                 userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
            }
            // Velocity validation
            BaseWrapper vWrapper = new BaseWrapperImpl();
            vWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            vWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
            vWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.valueOf(withdrawalAmount));
            vWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, customerModel.getSegmentId());
            vWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, customerModel.getCustomerAccountTypeId());
            vWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            vWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
            getCommonCommandManager().checkVelocityCondition(vWrapper);

            ProductVO productVo = getCommonCommandManager().loadProductVO(this.baseWrapper);
            if (productVo == null) {
                throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            workFlowWrapper.setCustomerModel(customerModel);
            workFlowWrapper.setSegmentModel(new SegmentModel());
            workFlowWrapper.getSegmentModel().setSegmentId(customerModel.getSegmentId());

            workFlowWrapper.setProductVO(productVo);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setAppUserModel(customerAppUserModel);
            workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
            if (appId != null && appId.equals("2")) {
                BaseWrapper userWrapper = new BaseWrapperImpl();
                userWrapper.setBasePersistableModel(customerAppUserModel);
                userWrapper = commonCommandManager.loadUserDeviceAccountByMobileNumber(userWrapper);
                ThreadLocalAppUser.setAppUserModel(customerAppUserModel);
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel((UserDeviceAccountsModel) userWrapper.getBasePersistableModel());
            }
            workFlowWrapper.setUserDeviceAccountModel(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel());
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            @SuppressWarnings("rawtypes")
            CustomList smaList = searchBaseWrapper.getCustomList();
            //Customer smart money account
            searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel customerSMAModel = new SmartMoneyAccountModel();
            customerSMAModel.setCustomerId(customerAppUserModel.getCustomerId());
            customerSMAModel.setActive(Boolean.TRUE);
            customerSMAModel.setDeleted(Boolean.FALSE);
            customerSMAModel.setDefAccount(Boolean.TRUE);
            if (paymentMode.equals("HRA"))
                customerSMAModel.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
            else
                customerSMAModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            searchBaseWrapper.setBasePersistableModel(customerSMAModel);

            searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

            smaList = searchBaseWrapper.getCustomList();
            if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
                customerSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
            }

            workFlowWrapper.setOlaSmartMoneyAccountModel(customerSMAModel);
            TaxRegimeModel taxRegimeModel = customerModel.getTaxRegimeIdTaxRegimeModel();
            if (appId != null && appId.equals("1")) {
                BaseWrapper retailerWrapper = new BaseWrapperImpl();
                Long retailerContactId = ThreadLocalAppUser.getAppUserModel().getRetailerContactId();
                RetailerContactModel retailerContactModel = new RetailerContactModel();
                retailerContactModel.setPrimaryKey(retailerContactId);
                retailerWrapper.setBasePersistableModel(retailerContactModel);
                retailerWrapper = commonCommandManager.loadRetailerContact(retailerWrapper);
                retailerContactModel = (RetailerContactModel) retailerWrapper.getBasePersistableModel();
                //taxRegimeModel = retailerContactModel.getTaxRegimeIdTaxRegimeModel();
                workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
                workFlowWrapper.setRetailerContactModel(retailerContactModel);
                //workFlowWrapper.setToRetailerContactModel(retailerContactModel);
                SmartMoneyAccountModel agentSMAModel = new SmartMoneyAccountModel();
                agentSMAModel.setRetailerContactId(retailerContactId);
                agentSMAModel.setActive(Boolean.TRUE);
                agentSMAModel.setDeleted(Boolean.FALSE);
                agentSMAModel.setDefAccount(Boolean.TRUE);
                searchBaseWrapper = new SearchBaseWrapperImpl();
                searchBaseWrapper.setBasePersistableModel(agentSMAModel);
                searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);
                smaList = searchBaseWrapper.getCustomList();
                if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
                    agentSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
                }
                workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);
            }
            workFlowWrapper.setTaxRegimeModel(taxRegimeModel);
            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DEBIT_CARD_CW_TX);
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);

            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
            workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
            workFlowWrapper.putObject("RRN", rrn);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_RESERVED_4, cardAcceptor);


            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            workFlowWrapper.setLeg2Transaction(Boolean.FALSE); // To avoid saving of TransactionDetailMaster incase of Exception

            commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
            transactionModel = workFlowWrapper.getTransactionModel();
            commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();
            workFlowWrapper.putObject("productTile",productModel.getName());
            commonCommandManager.sendSMS(workFlowWrapper);

        } catch (Exception e) {
            logger.error("Debit Card Cash Withdrawal Command Execution Error :: " + e.getMessage(), e);
            handleException(e);
        }
    }

    @Override
    public String response() {
        return MiniXMLUtil.createResponseXMLByParams(this.toXML());
    }

    private List<LabelValueBean> toXML() {

        List<LabelValueBean> params = new ArrayList<LabelValueBean>(20);
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(customerAppUserModel.getMobileNo())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_NAME, replaceNullWithEmpty(customerAppUserModel.getFullName())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, replaceNullWithEmpty(customerAppUserModel.getNic())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_COMM_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TOTAL_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));

        params.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, workFlowWrapper.getTransactionModel().getCreatedOn().toString()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(workFlowWrapper.getTransactionModel().getCreatedOn())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_BAL, "" + workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BAL, "" + workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction()));
        return params;
    }
}

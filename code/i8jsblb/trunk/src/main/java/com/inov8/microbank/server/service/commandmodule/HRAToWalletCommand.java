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
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.integration.vo.CashWithdrawalVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class HRAToWalletCommand extends BaseCommand {

    private AppUserModel customerAppUserModel;
    private BaseWrapper baseWrapper;

    private String withdrawalAmount;
    private String productId, customerMobileNo;
    private String commissionAmount, processingAmount, totalAmount;
    private ProductModel productModel;
    private CustomerModel customerModel;
    private Double customerBalance;
    private String paymentMode;
    private String channelId;
    private String terminalId;
    private TransactionModel transactionModel;
    private String thirdPartyTransactionId;
    private String otp;
    private String stan;

    protected final Log logger = LogFactory.getLog(HRACashWithdrawalCommand.class);

    public void prepare(BaseWrapper baseWrapper) {

        String classNMethodName = "[HRATOWALLETCommand.prepare] ";
        if (logger.isDebugEnabled()) {
            logger.debug("Start of " + classNMethodName);
        }


        this.baseWrapper = baseWrapper;
        customerAppUserModel = ThreadLocalAppUser.getAppUserModel();
        channelId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        terminalId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        withdrawalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
        commissionAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAMT);
        processingAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
        totalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
        thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
        stan=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_STAN);
        try {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel) bWrapper.getBasePersistableModel();

            if (customerAppUserModel != null) {
                customerModel = new CustomerModel();
                customerModel.setCustomerId(customerAppUserModel.getCustomerId());
                bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(customerModel);
                bWrapper = getCommonCommandManager().loadCustomer(bWrapper);
                customerModel = (CustomerModel) bWrapper.getBasePersistableModel();
            }
            customerMobileNo = customerAppUserModel.getMobileNo();
        } catch (Exception ex) {
            logger.error(classNMethodName + " Product model not found: " + ex.getStackTrace().toString());
        }


        if (logger.isDebugEnabled()) {
            logger.debug("End of " + classNMethodName);
        }
    }

    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {

        String classNMethodName = "[HRAToWALLETCommand.validate] ";
        String inputParams = "Logged in AppUserID: " +
                ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " + productId + " Customer Mobile No:" + customerMobileNo;

        if (logger.isDebugEnabled()) {
            logger.debug("Start of " + classNMethodName);
        }

        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product Id");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");

        validationErrors = ValidatorWrapper.doRequired(withdrawalAmount, validationErrors, "Withdrawal Amount");
        validationErrors = ValidatorWrapper.doRequired(commissionAmount, validationErrors, "Commission Amount");
        validationErrors = ValidatorWrapper.doRequired(processingAmount, validationErrors, "Processing Amount");
        validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");

        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(withdrawalAmount, validationErrors, "Withdrawal Amount");
            validationErrors = ValidatorWrapper.doNumeric(commissionAmount, validationErrors, "Commission Amount");
            validationErrors = ValidatorWrapper.doNumeric(processingAmount, validationErrors, "Processing Amount");
            validationErrors = ValidatorWrapper.doNumeric(totalAmount, validationErrors, "Total Amount");
        }
        if (null == productModel) {
            logger.error(classNMethodName + "Product model not found: " + inputParams);

            validationErrors = ValidatorWrapper.addError(validationErrors, "Product not found.");
        }

        if (null == customerAppUserModel) {
            logger.error(classNMethodName + "Customer App User not found: " + inputParams);

            validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
        }

        if (null == customerModel) {
            logger.error(classNMethodName + "Customer model not found: " + inputParams);

            validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of PayCashInfoCommand.validate()");
        }
        return validationErrors;

    }

    public void execute() throws CommandException {
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        String classNMethodName = "[HRATOWALLETCOMMAND.execute]";
        String exceptionMessage = "Exception Occured for Logged in AppUser";
        String inputParams = " ID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " + productId + " Mobile No:" + customerMobileNo;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;

        try {
            AccountInfoModel accountInfoModel = new AccountInfoModel();
            workFlowWrapper.setAccountInfoModel(accountInfoModel);

            workFlowWrapper.setTxProcessingAmount(Double.parseDouble(processingAmount));
            workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
            workFlowWrapper.setTransactionAmount(Double.parseDouble(withdrawalAmount));
            workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));


            // check product limit
            getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), customerAppUserModel.getMobileNo(),
                    Long.valueOf(deviceTypeId), Double.parseDouble(withdrawalAmount), productModel, null, workFlowWrapper.getHandlerModel());

            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
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

            ProductVO productVo = getCommonCommandManager().loadProductVO(baseWrapper);

            if (productVo == null) {
                throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            workFlowWrapper.setCustomerModel(customerModel);
            workFlowWrapper.setSegmentModel(new SegmentModel());
            workFlowWrapper.getSegmentModel().setSegmentId(customerModel.getSegmentId());

            workFlowWrapper.setProductVO(productVo);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
            workFlowWrapper.setAppUserModel(customerAppUserModel);


            //Customer BLB smart money account
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel customerBLBAccount = new SmartMoneyAccountModel();
            customerBLBAccount.setCustomerId(customerAppUserModel.getCustomerId());
            customerBLBAccount.setActive(Boolean.TRUE);
            customerBLBAccount.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            searchBaseWrapper.setBasePersistableModel(customerBLBAccount);

            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);

            @SuppressWarnings("rawtypes")
            CustomList smaList = searchBaseWrapper.getCustomList();
            if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
                customerBLBAccount = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
            }

            workFlowWrapper.setSmartMoneyAccountModel(customerBLBAccount);

            //Customer HRA smart money account
            searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel customerHRAAccount = new SmartMoneyAccountModel();
            customerHRAAccount.setCustomerId(customerAppUserModel.getCustomerId());
            customerHRAAccount.setActive(Boolean.TRUE);
            customerHRAAccount.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
            searchBaseWrapper.setBasePersistableModel(customerHRAAccount);

            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);

            smaList = searchBaseWrapper.getCustomList();
            if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
                customerHRAAccount = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
            }

            workFlowWrapper.setOlaSmartMoneyAccountModel(customerHRAAccount);


            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.HRA_TO_WALLET_TX);


            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);

            workFlowWrapper.setReceiverBvs(true);

            workFlowWrapper.setTaxRegimeModel(customerModel.getTaxRegimeIdTaxRegimeModel());
            logger.info(classNMethodName + " Going to execute Transaction flow. " + inputParams);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, channelId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
            workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, thirdPartyTransactionId);
            workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);
            processingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount().toString();
            transactionModel = workFlowWrapper.getTransactionModel();
            productModel = workFlowWrapper.getProductModel();

            if (((CashWithdrawalVO) workFlowWrapper.getProductVO()).getCustomerBalance() != null) {
                customerBalance = ((CashWithdrawalVO) workFlowWrapper.getProductVO()).getCustomerBalance();
            }
            workFlowWrapper.putObject("productTile",productModel.getName());
            getCommonCommandManager().sendSMS(workFlowWrapper);

        } catch (CommandException e) {

            ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

            logger.error(genericExceptionMessage + e.getMessage());
            throw e;
        } catch (WorkFlowException we) {

            ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

            logger.error(genericExceptionMessage + we.getMessage());
            throw new CommandException(we.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, we);
        } catch (ClassCastException cce) {
            logger.error(genericExceptionMessage + cce.getMessage());
            throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, cce);
        } catch (Exception ex) {

            ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

            logger.error(genericExceptionMessage + ex.getMessage());

            if (ex.getMessage() != null && ex.getMessage().indexOf("JTA") != -1) {
                throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, ex);
            } else {
                throw new CommandException(ex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, ex);
            }
        }
    }

    public String response() {

        List<LabelValueBean> lvbs = new ArrayList<LabelValueBean>();

        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode().toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, transactionModel.getCreatedOn().toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(transactionModel.getCreatedOn())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, productModel.getName()));

        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TXAM, replaceNullWithZero(transactionModel.getTransactionAmount()).toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TXAMF, Formatter.formatNumbers(replaceNullWithZero(transactionModel.getTransactionAmount()))));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount().toString())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAM, processingAmount));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(Double.parseDouble(processingAmount))));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMT, String.valueOf(transactionModel.getTotalAmount().doubleValue())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));

        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BAL, Formatter.formatNumbers(customerBalance)));

        return MiniXMLUtil.createResponseXMLByParams(lvbs);

    }
}

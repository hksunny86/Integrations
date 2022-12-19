package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * Created by Jawad on 05/16/2019.
 */
public class POSRefundCommand extends BaseCommand {

    protected AppUserModel appUserModel;
    protected String productId;
    protected String txProcessingAmount;
    protected String deviceTypeId;
    protected String accountNumber;
    protected String Stan;
    protected String terminalID;

    TransactionModel transactionModel;
    ProductModel productModel;
    String successMessage;
    BaseWrapper baseWrapper;
    SmartMoneyAccountModel smartMoneyAccountModel;
    UserDeviceAccountsModel userDeviceAccountsModel;
    CustomerModel customerModel;
    WorkFlowWrapper workFlowWrapper;

    RetailerContactModel fromRetailerContactModel;
    long segmentId;

    protected String customerMobileNo;
    protected String customerCNIC;
    protected String txAmount;


    DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");
    double balance = 0D;
    protected final Log logger = LogFactory.getLog(POSRefundCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        this.baseWrapper = baseWrapper;
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        Stan = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_STAN);
        terminalID = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);

        try {
            appUserModel = commonCommandManager.loadAppUserByMobileAndType(customerMobileNo,new Long[]{UserTypeConstantsInterface.CUSTOMER});
            segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(customerMobileNo);

            BaseWrapper bWrapper = new BaseWrapperImpl();
            CustomerModel customerModel = new CustomerModel();
            customerModel.setCustomerId( appUserModel.getCustomerId() );
            bWrapper.setBasePersistableModel(customerModel);
            bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
            this.customerModel = (CustomerModel) bWrapper.getBasePersistableModel();

            if(appUserModel != null) {
               ThreadLocalAppUser.setAppUserModel(appUserModel);
                logger.info("[POSRefund.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txProcessingAmount);
            }
        } catch (Exception e) {
            logger.error("[POSRefund.prepare] Unable to load Customer Segment info... ", e);
        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {

        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(txProcessingAmount, validationErrors, "Tx Processing Amount");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Mobile No");
        validationErrors = ValidatorWrapper.doRequired(accountNumber, validationErrors, "Account Number");
        validationErrors = ValidatorWrapper.doRequired(Stan, validationErrors, "Stan");
        validationErrors = ValidatorWrapper.doRequired(terminalID, validationErrors, "Terminal ID");


        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Processing Amount");
            txAmount = txProcessingAmount;

        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {

        if (logger.isDebugEnabled()) {
            logger.debug("Start of POSRefund.execute()");
        }
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        try {

            // Velocity validation - start
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
            bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(txAmount));

            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segmentId));
            boolean result = commonCommandManager.checkVelocityCondition(bWrapper);
            ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);


            if (!validationErrors.hasValidationErrors()) {
                AccountInfoModel accountInfoModel = new AccountInfoModel();
                // setting account no which is being received from Integration
                accountInfoModel.setAccountNo(accountNumber);
                productModel = new ProductModel();
                productModel.setProductId(Long.parseLong(productId));

                ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);

                AppUserModel customerAppUserModel = new AppUserModel();
                customerAppUserModel.setMobileNo(customerMobileNo);
                customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.POS_CUSTOMER_REFUND_TX);

                smartMoneyAccountModel = new SmartMoneyAccountModel();
                smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());

                SmartMoneyAccountModel smaVerification = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,1L);

                 if(smaVerification != null && smaVerification.getStatusId() != null && smaVerification.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE))
                    throw new CommandException("Your Account is In-Active.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM,new Throwable());
                else if(smaVerification != null && smaVerification.getStatusId() != null && smaVerification.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED))
                    throw new CommandException("Your Account is Blocked.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM,new Throwable());


                DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
                //CustomerAccount customerAccount = new CustomerAccount(accountNumber, accountType, accountCurrency, accountStatus);
               // workFlowWrapper.setCustomerAccount(customerAccount);
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
                workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
                workFlowWrapper.setAccountInfoModel(accountInfoModel);
                workFlowWrapper.setProductVO(productVo);
                workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
                workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
                workFlowWrapper.setCustomerModel(customerModel);
                workFlowWrapper.setAppUserModel(appUserModel);
                workFlowWrapper.setToSegmentId(segmentId);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN,Stan);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID,terminalID);
                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(segmentId);
                workFlowWrapper.setSegmentModel(segmentModel);


                workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());

                logger.info("[POSRefund.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() +
                        " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount);


                workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

                transactionModel = workFlowWrapper.getTransactionModel();
                smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();

                productModel = workFlowWrapper.getProductModel();
                userDeviceAccountsModel = workFlowWrapper.getUserDeviceAccountModel();

                logger.info(workFlowWrapper.getSwitchWrapper().getAgentBalance()+"-"+workFlowWrapper.getSwitchWrapper().getBalance());

            } else {
                logger.error("[POSRefund.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount);
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
        } catch (FrameworkCheckedException ex) {
            if (logger.isErrorEnabled()) {
                logger.error("[POSRefund.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            }
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        } catch (WorkFlowException wex) {
            if (logger.isErrorEnabled()) {
                logger.error("[POSRefund.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
            }
            throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
        } catch (Exception ex) {

            ex.printStackTrace();

            if (logger.isErrorEnabled()) {
                logger.error("[POSRefund.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission:" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            }
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }

        if(logger.isDebugEnabled())
        {
        logger.debug("End of POSRefund.execute()");
        }

    }

    @Override
    public String response() {
        return toXML();
    }

    private String toXML()
    {

        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
        params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(ATTR_CMOB, replaceNullWithEmpty(customerMobileNo)));
        params.add(new LabelValueBean(ATTR_CNIC, replaceNullWithEmpty(customerCNIC)));
        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT)));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        params.add(new LabelValueBean(ATTR_PROD, replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount()+"")));
        params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
        params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(txProcessingAmount)));
        params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(Double.parseDouble(txProcessingAmount))));
        params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
        params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_BAL, "" + workFlowWrapper.getOLASwitchWrapper().getOlavo().getToBalanceAfterTransaction()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BAL, "" + workFlowWrapper.getOLASwitchWrapper().getOlavo().getToBalanceAfterTransaction()));


        return MiniXMLUtil.createResponseXMLByParams(params);

    }
}

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
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * Created by Attique on 1/11/2018.
 */
public class WebServiceCashInCommand extends BaseCommand {

    protected AppUserModel appUserModel;
    protected String productId;
    protected String accountId;
    protected String txProcessingAmount;
    protected String deviceTypeId;
    protected String commissionAmount;
    protected String totalAmount;

    TransactionModel transactionModel;
    ProductModel productModel;
    String successMessage;
    BaseWrapper baseWrapper;
    SmartMoneyAccountModel smartMoneyAccountModel;
    UserDeviceAccountsModel userDeviceAccountsModel;
    WorkFlowWrapper workFlowWrapper;

    CustomerModel customerModel;
    long segmentId;

    protected String customerMobileNo;
    protected String customerCNIC;
    protected String agentMobileNo;
    protected String txAmount;
    private String terminalId;
    private String channelId;
    private  String paymentMode;
    private String thirdPartyTransactionId;
    private String cashInType;
    private String stan;


    DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
    double balance=0D;

    protected final Log logger = LogFactory.getLog(CashDepositCommand.class);

    @Override
    public void execute() throws CommandException
    {
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        try
        {
            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
            // Velocity validation - start
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
            bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(txAmount));
            bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, customerModel.getCustomerAccountTypeId());
            bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segmentId));
            boolean result = commonCommandManager.checkVelocityCondition(bWrapper);
            ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);

            if(!validationErrors.hasValidationErrors())
            {
                AccountInfoModel accountInfoModel = new AccountInfoModel();

                productModel = new ProductModel();
                productModel.setProductId(Long.parseLong(productId));

                AppUserModel customerAppUserModel = new AppUserModel();
                customerAppUserModel.setMobileNo(customerMobileNo);
                customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.WEB_SERVICE_CASH_IN_TX);
                Long paymentModeId = null;

                smartMoneyAccountModel = new SmartMoneyAccountModel();
                smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
                if(paymentMode.equals("HRA"))
                    paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
                else
                    paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

                smartMoneyAccountModel.setPaymentModeId(paymentModeId);
                smartMoneyAccountModel.setDistributorContactId(appUserModel.getDistributorContactId());

                SmartMoneyAccountModel smaVerification = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,paymentModeId);
                if(smaVerification == null)
                    throw new CommandException("HRA Account does not Exist.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                else if(smaVerification != null && smaVerification.getStatusId() != null && smaVerification.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE))
                    throw new CommandException("Your Account is In-Active.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                else if(smaVerification != null && smaVerification.getStatusId() != null && smaVerification.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED))
                    throw new CommandException("Your Account is Blocked.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                // commonCommandManager.checkProductLimit(segmentId, productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.parseDouble( txAmount), productModel, null, workFlowWrapper.getHandlerModel());

                DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
                workFlowWrapper.setAccountInfoModel(accountInfoModel);
                workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
                workFlowWrapper.setAppUserModel(appUserModel);
                workFlowWrapper.setCustomerAppUserModel(appUserModel);
                workFlowWrapper.setCustomerModel(customerModel);
                workFlowWrapper.setToSegmentId(segmentId);
                workFlowWrapper.setTaxRegimeModel(customerModel.getTaxRegimeIdTaxRegimeModel());
                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(segmentId);
                workFlowWrapper.setSegmentModel(segmentModel);
                workFlowWrapper.setIsCustomerInitiatedTransaction(true);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
                workFlowWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE,paymentMode);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,channelId);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID,terminalId);
                workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE,thirdPartyTransactionId);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN,stan);
                workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME,cashInType);

                logger.info("[CashDepositCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() +
                        " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);

                workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
                txProcessingAmount=workFlowWrapper.getObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT).toString();
                transactionModel = workFlowWrapper.getTransactionModel();
                smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();

                productModel = workFlowWrapper.getProductModel();
                userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
                successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();

                commonCommandManager.sendSMS(workFlowWrapper);

            }
            else
            {
                logger.error("[CashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);
                throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }
        }
        catch(FrameworkCheckedException ex)
        {
            if(logger.isErrorEnabled())
            {
                logger.error("[CashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            }
            throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
        }

        catch(WorkFlowException wex)
        {
            if(logger.isErrorEnabled())
            {
                logger.error("[CashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
            }
            throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
        }
        catch(Exception ex)
        {

            ex.printStackTrace();

            if(logger.isErrorEnabled())
            {
                logger.error("[CashDepositCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            }
            throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
        }

        if(logger.isDebugEnabled())
        {
            logger.debug("End of CashDepositCommand.execute()");
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper)
    {
        this.baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        thirdPartyTransactionId = getCommandParameter(baseWrapper,FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
        cashInType = getCommandParameter(baseWrapper,FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        customerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        channelId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID);
        terminalId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TERMINAL_ID);
        stan=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_STAN);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        paymentMode = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE) == null || this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE).equals("")
                ? "BLB":"HRA";
        logger.info("[CashDepositCommand.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);

        BaseWrapper bWrapper = new BaseWrapperImpl();
        CustomerModel customerModel = new CustomerModel();
        customerModel.setCustomerId( appUserModel.getCustomerId() );
        bWrapper.setBasePersistableModel(customerModel);

        try{
            bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);

            this.customerModel = (CustomerModel) bWrapper.getBasePersistableModel();

        }catch(Exception ex){
            logger.error("[CashDepositCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
        }

        try {
            segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(customerMobileNo);
        } catch (Exception e) {
            logger.error("[CashDepositCommand.prepare] Unable to load Customer Segment info... ",e);
        }

    }

    @Override
    public String response()
    {
        if(logger.isDebugEnabled())
        {
            logger.debug("Start/End of CashDepositCommand.response()");
        }
        return toXML();
    }

    @Override
    public ValidationErrors validate(com.inov8.microbank.common.util.ValidationErrors validationErrors) throws CommandException
    {
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Tx Amount");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Mobile No");
        validationErrors = ValidatorWrapper.doRequired(customerCNIC,validationErrors,"CNIC");

        if(!validationErrors.hasValidationErrors())
        {
            validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");
        }

        return validationErrors;
    }

    private String toXML()
    {

        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
        params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(ATTR_CMOB, replaceNullWithEmpty(customerMobileNo)));
        params.add(new LabelValueBean(ATTR_CNIC, replaceNullWithEmpty(customerCNIC)));
        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.LONG_DATE_FORMAT)));
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
        params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(workFlowWrapper.getSwitchWrapper().getAgentBalance())));

        return MiniXMLUtil.createResponseXMLByParams(params);

    }
}

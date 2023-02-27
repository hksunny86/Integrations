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
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attique on 9/3/2018.
 */
public class ThirdPartyCashOutCommand extends BaseCommand {

    private AppUserModel appUserModel, customerAppUserModel;
    private BaseWrapper baseWrapper;
    private UserDeviceAccountsModel userDeviceAccountsModel;

    private String customerMobileNo, agentMobileNo, withdrawalAmount;
    private String productId, encryptionType, CNIC, agentPIN;
    private String commissionAmount, processingAmount, totalAmount;
    private String bvsConfigId, terminalId, sellerCode, sessionId, customerAccount;
    private RetailerContactModel retailerContactModel;
    private ProductModel productModel;
    private String otp;
    private CustomerModel customerModel;
    private Double customerBalance;
    private String isWalletExists;
    private String walletAccountIdBAFL;
    private String isWalletTransfer;

    private String fingerIndex, templateType, fingerTemplate, minutiaeCount, nIfq, errorCounter, nadraSessionId, thirdPartyTransactionId,
            latitude,longitude, macAddress, imeiNumber;

    private TransactionModel transactionModel;

    protected final Log logger = LogFactory.getLog(ThirdPartyCashOutCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        String classNMethodName = "[ThirdPartyCashOutCommand.prepare] ";
        if (logger.isDebugEnabled()) {
            logger.debug("Start of " + classNMethodName);
        }

        this.baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
        deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);

        if (productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
            otp = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_OTP);

        isWalletTransfer = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BAFL_WALLET_TRANSFER);
        walletAccountIdBAFL = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BAFL_WALLET_ACCOUNT_ID);
        isWalletExists = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BAFL_WALLET);
        if (isWalletExists != null && isWalletExists.equals("0")
                && !productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
            productId = ProductConstantsInterface.BISP_CASH_OUT.toString();
        else if (!productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
            productId = ProductConstantsInterface.BISP_CASH_OUT_WALLET.toString();
        customerMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        withdrawalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
        nadraSessionId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NADRA_SESSION_ID);
        thirdPartyTransactionId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID);
        encryptionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        CNIC = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        agentPIN = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
        commissionAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAMT);
        processingAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
        totalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
        customerAccount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
        terminalId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        sessionId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SESSION_ID);
        sellerCode = getCommandParameter(baseWrapper, "SELLER_CODE");
        bvsConfigId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_BVS_REQUIRED);
        minutiaeCount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NADRA_MINUTIAE_COUNT);
        nIfq = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NADRA_NIFQ);
        errorCounter = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ERROR_COUNTER);
        latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        macAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MAC_ADDRESS);
        imeiNumber = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOBILE_IMEI_NUMBER);

        if (bvsConfigId.equals("2")) {
            fingerIndex = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_INDEX);
            templateType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TEMPLATE_TYPE);
            fingerTemplate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_TEMPLATE);
        }

        try {
            if (productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
                otp = EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, otp);

            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel) bWrapper.getBasePersistableModel();
        } catch (Exception ex) {
            logger.error(classNMethodName + " Product model not found: " + ex.getStackTrace().toString());
        }

        try {
            RetailerContactModel retContactModel = new RetailerContactModel();
            retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(retContactModel);
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

            retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

//            GeoLocationModel geoLocationModel  = new GeoLocationModel();
//            if(retailerContactModel.getGeoLocationId()!=null){
//                geoLocationModel = commonCommandManager.getGeoLocationDao().findByPrimaryKey(retailerContactModel.getGeoLocationId());
//            }
//            String existingLatitude = String.valueOf(geoLocationModel.getLatitude());
//            String existingLongitude = String.valueOf(geoLocationModel.getLongitude());
//
//            if(latitude.contains(existingLatitude.substring(0,7))) {
//                latitude = existingLatitude;
//            }
//            else{
//                throw new CommandException("You are not currently at the authorized location to conduct this transaction.",ErrorCodes.COMMAND_EXECUTION_ERROR,
//                        ErrorLevel.HIGH);
//            }
//            if(longitude.contains(existingLongitude.substring(0,7))) {
//                longitude = existingLongitude;
//            }
//            else{
//                throw new CommandException("You are not currently at the authorized location to conduct this transaction.",ErrorCodes.COMMAND_EXECUTION_ERROR,
//                        ErrorLevel.HIGH);
//            }

        } catch (Exception ex) {
            logger.error(classNMethodName + "Retailer contact model not found: " + ex.getStackTrace());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of " + classNMethodName);
        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        String classNMethodName = "[ThirdPartyCashOutCommand.validate] ";
        String inputParams = "Logged in AppUserID: " +
                ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " + productId + " Customer Mobile No:" + customerMobileNo;

        if (logger.isDebugEnabled()) {
            logger.debug("Start of " + classNMethodName);
        }

        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product Id");
        validationErrors = ValidatorWrapper.doRequired(CNIC, validationErrors, "CNIC");

        validationErrors = ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile No");

        validationErrors = ValidatorWrapper.doRequired(agentPIN, validationErrors, "Product Id");

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

        if (null == retailerContactModel) {
            logger.error(classNMethodName + "Retailer Contact Model is null. " + inputParams);

            validationErrors = ValidatorWrapper.addError(validationErrors, "Retailer Contact not found.");
        }

        try {
            ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(appUserModel);

            if (userValidation.hasValidationErrors()) {
                logger.error(classNMethodName + " User validation failed.");

                validationErrors = ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
            }


        } catch (FrameworkCheckedException e) {
            logger.error(classNMethodName + e.getMessage());

            validationErrors = ValidatorWrapper.addError(validationErrors, e.getMessage());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of PayCashInfoCommand.validate()");
        }
        return validationErrors;

    }

    @Override
    public void execute() throws CommandException {

        Boolean skipForLocalTesting = Boolean.FALSE;
        String integTargetEnv = MessageUtil.getMessage("I8MICROBANK.TARGET.ENVIRONMENT");
        if (integTargetEnv != null && !integTargetEnv.equals("") && integTargetEnv.equals("MOCK"))
            skipForLocalTesting = Boolean.TRUE;

        //skipForLocalTesting = Boolean.TRUE;
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        String classNMethodName = "[ThirdPartyCashOutCommand.execute] ";
        String exceptionMessage = "Exception Occurred for Logged in AppUser";
        String inputParams = " ID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " + productId + " Mobile No:" + customerMobileNo;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;

        try {
            //Dummy Customer_Id as Walk-In Customer involves in 3rd Party CashOut
            Long result = commonCommandManager.getBispCustNadraVerificationDAO().isBVSSuccessful(null,
                    null,CNIC,-1L,UserTypeConstantsInterface.CUSTOMER);
            if(result >= 16)
                throw new CommandException(MessageUtil.getMessage("BISP.cust.daily.NADRA.retries"), ErrorCodes.INVALID_INPUT,ErrorLevel.MEDIUM,null);
            else{
                result = commonCommandManager.getBispCustNadraVerificationDAO().isBVSSuccessful(null,
                        userDeviceAccountsModel.getUserId(),CNIC,-1L,UserTypeConstantsInterface.CUSTOMER);
                if(result >= 8)
                    throw new CommandException(MessageUtil.getMessage("BISP.cust.agent.NADRA.retries"), ErrorCodes.INVALID_INPUT,ErrorLevel.MEDIUM,null);
            }
            workFlowWrapper.setTxProcessingAmount(Double.parseDouble(processingAmount));
            workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
            workFlowWrapper.setTransactionAmount(Double.parseDouble(withdrawalAmount));
            workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
            workFlowWrapper.setHandlerModel(handlerModel);
            workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
            // check product limit
            getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(),
                    Long.valueOf(deviceTypeId), Double.parseDouble(withdrawalAmount), productModel, null, workFlowWrapper.getHandlerModel());

            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
            //Velocity validation
            BaseWrapper vWrapper = new BaseWrapperImpl();
            vWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            vWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
            vWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
            vWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
            vWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.valueOf(withdrawalAmount));
            vWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
            vWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
            vWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            vWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
            getCommonCommandManager().checkVelocityCondition(vWrapper);
            workFlowWrapper.setCustomerModel(customerModel);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setAppUserModel(appUserModel);
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel agentSMAModel = new SmartMoneyAccountModel();
            agentSMAModel.setRetailerContactId(appUserModel.getRetailerContactId());
            agentSMAModel.setActive(Boolean.TRUE);
            searchBaseWrapper.setBasePersistableModel(agentSMAModel);
            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            @SuppressWarnings("rawtypes")
            CustomList smaList = searchBaseWrapper.getCustomList();
            if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
                agentSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
            }
            Long customerId = null;
            if (appUserModel.getCustomerId() != null)
                customerId = appUserModel.getCustomerId();
            else
                customerId = appUserModel.getAppUserId();
            workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);
            AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(customerId, agentSMAModel.getName());
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX);
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            if (productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
                workFlowWrapper.putObject(CommandFieldConstants.KEY_OTP, otp);
            //
            workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_CNIC, CNIC);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
            workFlowWrapper.putObject("SElLER_CODE", sellerCode);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_SESSION_ID, sessionId);
            if (productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
                workFlowWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER, customerMobileNo);
            else
                workFlowWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER, customerAccount);

            if (bvsConfigId.equals("2")) {
                workFlowWrapper.putObject(CommandFieldConstants.KEY_FINGER_INDEX, fingerIndex);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_TEMPLATE_TYPE, templateType);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_FINGER_TEMPLATE, fingerTemplate);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_IS_BVS_REQUIRED, bvsConfigId);

            }

            ActionLogModel actionLogModel = new ActionLogModel();
            actionLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
            actionLogModel = commonCommandManager.getActionLogDao().findByPrimaryKey(ThreadLocalActionLog.getActionLogId());
            String ipAddress = actionLogModel.getClientIpAddress();

            /*minutiaeCount = "7";
            nIfq = "2";*/
            workFlowWrapper.putObject(CommandFieldConstants.KEY_PRODUCT_ID, productId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_NADRA_MINUTIAE_COUNT, minutiaeCount);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_NADRA_NIFQ, nIfq);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_ERROR_COUNTER, errorCounter);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_NADRA_SESSION_ID, nadraSessionId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID, thirdPartyTransactionId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_MAC_ADDRESS, macAddress);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_LONGITUDE, longitude);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_LATITUDE, latitude);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_MOBILE_IMEI_NUMBER, imeiNumber);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_IP_ADDRESS, ipAddress);
            if(bvsConfigId.equals("2")){
                workFlowWrapper.setSenderBvs(Boolean.TRUE);
                workFlowWrapper.setReceiverBvs(Boolean.TRUE);
            }
            if(isWalletExists.equals("0"))
                isWalletExists = "N";
            else
                isWalletExists = "Y";
            workFlowWrapper.putObject(CommandFieldConstants.KEY_BAFL_WALLET, isWalletExists);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_BAFL_WALLET_ACCOUNT_ID, walletAccountIdBAFL);

            logger.info(classNMethodName + " Going to execute Transaction flow. " + inputParams);
            workFlowWrapper.putObject("skipForLocalTesting", skipForLocalTesting);

            workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);
            if (productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
                CNIC = workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE).toString();

            transactionModel = workFlowWrapper.getTransactionModel();
            productModel = workFlowWrapper.getProductModel();
            //customerAccount = workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE).toString();
          /*  if (((CashWithdrawalVO)workFlowWrapper.getProductVO()).getCustomerBalance() != null) {
                customerBalance = ((CashWithdrawalVO)workFlowWrapper.getProductVO()).getCustomerBalance();
            }*/
            workFlowWrapper.putObject("productTile",productModel.getName());
            getCommonCommandManager().sendSMS(workFlowWrapper);

			/*if (((CashWithdrawalVO)workFlowWrapper.getProductVO()).getAgentBalance() != null) {
				agentBalance = ((CashWithdrawalVO)workFlowWrapper.getProductVO()).getAgentBalance();
			}*/
        } catch (CommandException e) {
            if (e.getErrorCode() == 122 || e.getErrorCode() == 131)
                ivrErrorCode = "122";
            else if (e.getErrorCode() == 121)
                ivrErrorCode = "121";
            else if (e.getErrorCode() == 118)
                ivrErrorCode = "118";
            else if (e.getErrorCode() == 135)
                ivrErrorCode = "135";
            else if (e.getErrorCode() == ErrorCodes.THIRD_PARTY_TRANSACTION_TIME_OUT)
                ivrErrorCode = ErrorCodes.THIRD_PARTY_TRANSACTION_TIME_OUT.toString();
            else
                ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

            logger.error(genericExceptionMessage + e.getMessage());
            String nadraSessionId = null;
            if (workFlowWrapper.getObject(CommandFieldConstants.KEY_NADRA_SESSION_ID) != null)
                nadraSessionId = (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_NADRA_SESSION_ID);
            String thirdPartyTransactionId = null;
            if (workFlowWrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID) != null)
                thirdPartyTransactionId = (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID);
            throw new CommandException(e.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, e, nadraSessionId, thirdPartyTransactionId);
        } catch (WorkFlowException we) {
            ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

            logger.error(genericExceptionMessage + we.getMessage());
            if (null != workFlowWrapper.getObject(CommandFieldConstants.KEY_NADRA_SESSION_ID))
                throw new CommandException(we.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, we, workFlowWrapper.getObject(CommandFieldConstants.KEY_NADRA_SESSION_ID).toString());
            else
                throw new CommandException(we.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, we);
        } catch (ClassCastException cce) {

            ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
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

    @Override
    public String response() {

        List<LabelValueBean> lvbs = new ArrayList<LabelValueBean>();

        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode().toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, CNIC));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, transactionModel.getCreatedOn().toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(transactionModel.getCreatedOn())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, productModel.getName()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, CNIC));

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

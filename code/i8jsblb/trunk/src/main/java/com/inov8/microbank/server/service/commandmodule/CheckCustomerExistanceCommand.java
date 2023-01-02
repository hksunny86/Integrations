package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * @author Soofiafa
 */

public class CheckCustomerExistanceCommand extends BaseCommand {
    protected final Log logger = LogFactory
            .getLog(CheckCustomerExistanceCommand.class);
    protected AppUserModel appUserModel;
    protected BaseWrapper preparedBaseWrapper;

    private String cMsisdn, cNic, customerMsisdn, customerNic, cregComment, customerAccountType, initialDeposit,
            customerRegistrationState, customerName, customerRegistrationStateId, isReceiveCash, isPin = "";
    private Date cnicExpiry, dob;
    private CommonCommandManager commonCommandManager;
    private MessageSource messageSource;
    private RegistrationStateModel regStateModel;
    private boolean flagMsisdn = false, flagCNic = false;
    private Long appUserId1, appUserType;
    private String agentId;
    private String segmentId;
    private String productId;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private List<CustomerPictureModel> customerPictureModelList = null;
    private FonePayManager fonePayManager;
    private AgentSegmentRestrictionManager agentSegmentRestrictionManager;
    private AgentSegmentRestriction agentSegmentRestriction;
    private SmsSender smsSender;

    private String isUpgrade;
    private String isOTPRequired;

    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CheckCustomerExistanceCommand.prepare()");
        this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        this.isUpgrade = getCommandParameter(baseWrapper, "IS_UPGRADE");
        this.preparedBaseWrapper = baseWrapper;
        this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");
        this.agentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_ID);
        this.segmentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SEGMENT_ID);
        this.productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);

        this.isReceiveCash = getCommandParameter(baseWrapper, "IS_RECEIVE_CASH");
        this.isPin = getCommandParameter(baseWrapper, "IS_PIN_VERIFY");
        this.isOTPRequired = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_OTP_REQUIRED);

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CheckCustomerExistanceCommand.prepare()");
    }

	/*public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException 
	{
		if (this.logger.isDebugEnabled())
			this.logger.debug("Start of CheckCustomerExistanceCommand.validate()");
		validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "MSISDN");
		validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
		validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");
		if (!validationErrors.hasValidationErrors())
			validationErrors = ValidatorWrapper.doInteger(this.deviceTypeId, validationErrors, "Device Type");
		if (this.logger.isDebugEnabled())
			this.logger.debug("End of CheckCustomerExistanceCommand.validate()");
		return validationErrors;
	}*/

    @Override
    public void doValidate() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CheckCustomerExistanceCommand.validate()");
        ValidationErrors validationErrors = new ValidationErrors();

        validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "MSISDN");
        validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
        boolean isValid = CommonUtils.isValidCnic(cNic);
        if (!validationErrors.hasValidationErrors()) {
            if (isValid == false) {
                validationErrors.getStringBuilder().append("Invalid CNIC");
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");


        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }

        if (!validationErrors.hasValidationErrors()) {
            if (getCommonCommandManager().isCnicBlacklisted(this.cNic)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CheckCustomerExistanceCommand.validate()");
    }


    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return new ValidationErrors();
    }


    public void execute() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger
                    .debug("Start of CheckCustomerExistanceCommand.execute()");
        String retVal = null;
        Boolean result = null;

        this.commonCommandManager = getCommonCommandManager();
        messageSource = getMessageSource();
        this.smsSender = getCommonCommandManager().getSmsSender();
        this.fonePayManager = commonCommandManager.getFonePayManager();
        this.agentSegmentRestrictionManager = commonCommandManager.getAgentSegmentRestriction();
        agentSegmentRestriction = new AgentSegmentRestriction();
        Long error_code = ErrorCodes.COMMAND_EXECUTION_ERROR;
        //this check implement on upgrade l0 account
        if (segmentId != null&& !segmentId.equals("")) {
            agentSegmentRestriction.setAgentID(agentId);
            agentSegmentRestriction.setSegmentId(Long.valueOf(segmentId));
            agentSegmentRestriction.setProductId(Long.valueOf(productId));
            agentSegmentRestriction.setIsActive(true);
            BaseWrapperImpl baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(agentSegmentRestriction);

            try {
                result = agentSegmentRestrictionManager.checkAgentSegmentRestriction(baseWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
            if (result.equals(false)) {
                throw new CommandException(MessageUtil.getMessage("account.opening.segment.restrict"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            } else {
                try {
                    flagMsisdn = isMobileNumUnique(this.cMsisdn);
                    if (!flagMsisdn) { // MSISDN is not Unique.
                        appUserId1 = appUserModel.getAppUserId();
                        appUserType = appUserModel.getAppUserTypeId();

                        if (appUserType == UserTypeConstantsInterface.RETAILER.longValue() || appUserType == UserTypeConstantsInterface.HANDLER.longValue()) {
                            // Throw exception if MSISDN is of Retailers/Agents/Handler
                            throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.MsisdnNotUnique", null, null));
                        } else { // MSISDN is of Customer. Check if CNIC is of Customer
                            // and Registration State.
                            checkCnicAndRegState();
                        }
                    } else { // MSISDN is Unique. Going to Check CNIC and Registration
                        // State.
                        checkCnicAndRegState();

                        if (isReceiveCash == null || isReceiveCash.equalsIgnoreCase("0")) {
                            commonCommandManager = this.getCommonCommandManager();
                            int pendingTransactionCount = commonCommandManager.countCustomerPendingTrx(this.cNic);
                            if (pendingTransactionCount > 0) {
                                throw new CommandException("Customer has pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                            }

                            pendingTransactionCount = commonCommandManager.countCustomerPendingTrxByMobile(this.cMsisdn);
                            if (pendingTransactionCount > 0) {
                                throw new CommandException("Customer has pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                            }
                        }
                    }
                } catch (FrameworkCheckedException ex) {
                    error_code = ErrorCodes.INVALID_USER;
                    retVal = ex.getMessage();
                } catch (Exception e) {
                    retVal = e.getMessage();
                }

                if (retVal != null)
                    throw new CommandException(retVal, error_code.longValue(),
                            ErrorLevel.MEDIUM, new Throwable());
                if (this.logger.isDebugEnabled())
                    this.logger.debug("End of CheckCustomerExistanceCommand.execute()");
            }

        }
        else {
            try {
                flagMsisdn = isMobileNumUnique(this.cMsisdn);
                if (!flagMsisdn) { // MSISDN is not Unique.
                    appUserId1 = appUserModel.getAppUserId();
                    appUserType = appUserModel.getAppUserTypeId();

                    if (appUserType == UserTypeConstantsInterface.RETAILER.longValue() || appUserType == UserTypeConstantsInterface.HANDLER.longValue()) {
                        // Throw exception if MSISDN is of Retailers/Agents/Handler
                        throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.MsisdnNotUnique", null, null));
                    } else { // MSISDN is of Customer. Check if CNIC is of Customer
                        // and Registration State.
                        checkCnicAndRegState();
                    }
                } else { // MSISDN is Unique. Going to Check CNIC and Registration
                    // State.
                    checkCnicAndRegState();

                    if (isReceiveCash == null || isReceiveCash.equalsIgnoreCase("0")) {
                        commonCommandManager = this.getCommonCommandManager();
                        int pendingTransactionCount = commonCommandManager.countCustomerPendingTrx(this.cNic);
                        if (pendingTransactionCount > 0) {
                            throw new CommandException("Customer has pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }

                        pendingTransactionCount = commonCommandManager.countCustomerPendingTrxByMobile(this.cMsisdn);
                        if (pendingTransactionCount > 0) {
                            throw new CommandException("Customer has pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }
                    }
                }
            } catch (FrameworkCheckedException ex) {
                error_code = ErrorCodes.INVALID_USER;
                retVal = ex.getMessage();
            } catch (Exception e) {
                retVal = e.getMessage();
            }

            if (retVal != null)
                throw new CommandException(retVal, error_code.longValue(),
                        ErrorLevel.MEDIUM, new Throwable());
            if (this.logger.isDebugEnabled())
                this.logger.debug("End of CheckCustomerExistanceCommand.execute()");
        }

    }

    public String response() {
        if (this.logger.isDebugEnabled())
            this.logger
                    .debug("Start of CheckCustomerExistanceCommand.response()");
        return toXML();
    }

    private String toXML() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CheckCustomerExistanceCommand.toXML()");
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);


        if (regStateModel != null) {
            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_CNIC)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.customerNic).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(XMLConstants.ATTR_CNIC_EXPIRY)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.dateFormat.format(this.cnicExpiry)).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_CUSTOMER_MOBILE)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.customerMsisdn).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_CREG_STATE)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.customerRegistrationState)
                    .append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_CREG_STATE_ID)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.customerRegistrationStateId)
                    .append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_CNAME)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.customerName).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);

            if (regStateModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)) {
                strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_CDOB)
                        .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                        .append(this.dateFormat.format(this.dob))
                        .append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);

                strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_CREG_COMMENT)
                        .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE);
                if (this.cregComment != null) {
                    strBuilder.append(this.cregComment);
                }
                strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);

                strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_DEPOSIT_AMT)
                        .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE);
                if (this.initialDeposit != null) {
                    strBuilder.append(this.initialDeposit);
                } else {
                    strBuilder.append("0");
                }
                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);

                strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_CUST_ACC_TYPE)
                        .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                        .append(this.customerAccountType).append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);


                for (CustomerPictureModel picture : this.customerPictureModelList) {
                    strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                            .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                            .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE);

                    if (picture.getPictureTypeId().equals(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY)) {
                        strBuilder.append(CommandFieldConstants.KEY_TERMS_PHOTO);
                    } else if (picture.getPictureTypeId().equals(PictureTypeConstants.SIGNATURE_SNAPSHOT)) {
                        strBuilder.append(CommandFieldConstants.KEY_SIGNATURE_PHOTO);
                    } else if (picture.getPictureTypeId().equals(PictureTypeConstants.CUSTOMER_PHOTO)) {
                        strBuilder.append(CommandFieldConstants.KEY_CUSTOMER_PHOTO);
                    } else if (picture.getPictureTypeId().equals(PictureTypeConstants.ID_FRONT_SNAPSHOT)) {
                        strBuilder.append(CommandFieldConstants.KEY_CNIC_FRONT_PHOTO);
                    } else if (picture.getPictureTypeId().equals(PictureTypeConstants.ID_BACK_SNAPSHOT)) {
                        strBuilder.append(CommandFieldConstants.KEY_CNIC_BACK_PHOTO);
                    }
                    if (picture.getPictureTypeId().equals(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT)) {
                        strBuilder.append(CommandFieldConstants.KEY_L1_FORM_PHOTO);
                    }
                    strBuilder.append(TAG_SYMBOL_QUOTE)
                            .append(TAG_SYMBOL_CLOSE).append(picture.getDiscrepant() ? 1 : 0)
                            .append(TAG_SYMBOL_OPEN)
                            .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                            .append(TAG_SYMBOL_CLOSE);
                }

                if (this.customerAccountType.equals(String.valueOf(CustomerAccountTypeConstants.LEVEL_0))) {
                    strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                            .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                            .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                            .append(CommandFieldConstants.KEY_L1_FORM_PHOTO)
                            .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                            .append("0").append(TAG_SYMBOL_OPEN)
                            .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                            .append(TAG_SYMBOL_CLOSE);
                }

            }
        } else {

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_DEVICE_TYPE_ID)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.deviceTypeId).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);
        }

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("End of CheckCustomerExistanceCommand.toXML()");
        }

        return strBuilder.toString();
    }


    private boolean isMobileNumUnique(String mobileNo) throws NoSuchMessageException, FrameworkCheckedException {

        /*AppUserModel appModel = null;*/
        Long registrationStateId = -1L;
        try {

            Long[] appUserTypes = new Long[]{UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};

            appUserModel = this.commonCommandManager.loadAppUserByMobileAndType(mobileNo, appUserTypes);

            if (appUserModel != null && appUserModel.getAppUserTypeId() != null && appUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.HANDLER.longValue()) {

                registrationStateId = appUserModel.getRegistrationStateId();
            }

        } catch (Exception e) {
            this.logger.error("CommonCommandManager Exception: " + e.getMessage());
            e.printStackTrace();
        }


        if (isUpgrade.equals("1") && appUserModel != null) {
            UserDeviceAccountsModel userDeviceAccountsModel = this.commonCommandManager.loadUserDeviceAccountByUserId(appUserModel.getUsername());
            if (userDeviceAccountsModel.getAccountEnabled() == false)
                throw new CommandException("Your Account is not Active.Please contact your service provider",
                        ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            if (userDeviceAccountsModel.getAccountLocked() == true)
                throw new CommandException("Your Account is Closed.Please contact your service provider",
                        ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
            sma.setCustomerId(appUserModel.getCustomerId());

            sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);

            if (smartMoneyAccountModel != null && smartMoneyAccountModel.getStatusId() != null && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_DORMANT))
                throw new CommandException("Your L1 account is marked as Dormant.Please contact your service provider.",
                        ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        if (registrationStateId == -1L && appUserModel == null && isUpgrade.equals("1"))
            throw new CommandException("No Customer exists against this Mobile No.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

        if (appUserModel != null && !appUserModel.getNic().equals(this.cNic) && isUpgrade.equals("1"))
            throw new CommandException("No customer does exist in the system against the provided CNIC and Mobile No.",
                    ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

//        if (isUpgrade.equals("1") && registrationStateId.equals(RegistrationStateConstantsInterface.VERIFIED.toString())) {
//            throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.RegistrationStateNotVerified", null, null));
//        }
        if (registrationStateId.longValue() == RegistrationStateConstantsInterface.DECLINE.longValue()) {

            throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.declinedCustomer", null, null));
        }
        if (registrationStateId.longValue() == RegistrationStateConstantsInterface.REJECTED.longValue()) {

            throw new FrameworkCheckedException("Customer is rejected.");
        }

        this.appUserModel = appUserModel;
        return (appUserModel == null);
    }

    private boolean isCNICUnique(String cnic) {
        if (isUpgrade.equals("1"))
            appUserModel = new AppUserModel();
        try {
            appUserModel = this.commonCommandManager
                    .loadAppUserByCnicAndType(cnic);
        } catch (Exception e) {
            this.logger.debug("CommonCommandManager Exception: "
                    + e.getMessage());
            e.printStackTrace();
        }

        return appUserModel == null;
    }


    public FonePayManager getFonepayManager() {
        return fonePayManager;
    }

    public void setFonePayManager(FonePayManager fonePayManager) {
        this.fonePayManager = fonePayManager;
    }

    public CommonCommandManager getCommonCommandManager() {
        return commonCommandManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public SmsSender getSmsSender() {
        return smsSender;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    private void checkCnicAndRegState() throws FrameworkCheckedException {


        flagCNic = isCNICUnique(this.cNic);
        if (isUpgrade.equals("1") && appUserModel == null)
            throw new CommandException("No Customer exists against this CNIC.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        if (!flagCNic && !flagMsisdn) {
            // CNIC & MSISDN both are not Unique.
            long appUserId2 = appUserModel.getAppUserId();
            appUserType = appUserModel.getAppUserTypeId();
            // Going to check if:
            // 1. CNIC of Retailers
            // 2. CNIC and MSIDN belongs to same App User.
            if ((appUserType == UserTypeConstantsInterface.RETAILER.longValue()) || (appUserId1 != appUserId2)) {
                throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.CnicNotUnique", null, null));
            } else if ((appUserId1 == appUserId2)) {
                CustomerModel customerModel = null;
                // MSISDN and CNIC belongs
                // to same App User
                // Going to check Registration State
                appUserModel = this.commonCommandManager.getAppUserWithRegistrationState(this.cMsisdn, this.cNic, RegistrationStateConstants.BULK_REQUEST_RECEIVED);
                if (appUserModel != null) {
                    customerModel = getCustomerAccountType(appUserModel.getCustomerId());
                    if (isUpgrade.equals("1") && customerModel != null && !customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0))
                        throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.accountTypeNotL0", null, null));
//                    if (isUpgrade.equals("1") && !appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.VERIFIED))
//                        throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.RegistrationStateNotVerified", null, null));
                }
                if (appUserModel != null) {
                    Long registrationStateId = appUserModel.getRegistrationStateId();
                    if (registrationStateId.equals(RegistrationStateConstants.BULK_REQUEST_RECEIVED)) {
                        appUserModel.setRegistrationStateModel(this.commonCommandManager.getRegistrationStateById(registrationStateId));
                        regStateModel = appUserModel.getRegistrationStateModel();
                        // if Registration State: 'Request Received via Bulk
                        // Registration' then send customer information in
                        // response
                        if (regStateModel != null) {
                            customerMsisdn = appUserModel.getMobileNo();
                            customerNic = appUserModel.getNic();
                            customerRegistrationState = regStateModel.getName();
                            customerRegistrationStateId = regStateModel.getRegistrationStateId().toString();
                            customerName = appUserModel.getFirstName() + " " + appUserModel.getLastName();
                            if (null != appUserModel.getNicExpiryDate()) {
                                cnicExpiry = appUserModel.getNicExpiryDate();
                            }
                        }
                    } else if (registrationStateId.equals(RegistrationStateConstants.DISCREPANT)) {
                        appUserModel.setRegistrationStateModel(this.commonCommandManager.getRegistrationStateById(registrationStateId));
                        regStateModel = appUserModel.getRegistrationStateModel();
                        // if Registration State: 'Request Received via Bulk
                        // Registration' then send customer information in
                        // response

                        //*********************************************************************************
                        //*********************************************************************************
                        //*********************************************************************************
                        ///Generate OTP and store in MiniTransaction For Discrepant Flow

                        String otp = CommonUtils.generateOneTimePin(5);
                        String encryptedPin = EncoderUtils.encodeToSha(otp);
                        try {
                            fonePayManager.createMiniTransactionModel(encryptedPin, this.cMsisdn, "", CommandFieldConstants.CMD_OTP_VERIFICATION);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Send OTP via SMS to Customer
                        String smsText = MessageUtil.getMessage("otpSmsAccOpening", new String[]{"JS Mobile account opening", "account opening", otp});
                        SmsMessage smsMessage = new SmsMessage(this.cMsisdn, smsText);
                        logger.info("CNIC : " + this.cNic + "Mobile # : " + this.cMsisdn + " : " + smsText);
                        smsSender.send(smsMessage);
                        //***********************************************************************************

                        if (regStateModel != null) {
                            customerMsisdn = appUserModel.getMobileNo();
                            customerNic = appUserModel.getNic();
                            cnicExpiry = appUserModel.getNicExpiryDate();
                            customerRegistrationState = regStateModel.getName();
                            customerRegistrationStateId = regStateModel.getRegistrationStateId().toString();
                            dob = appUserModel.getDob();
                            customerName = appUserModel.getFirstName() + " " + appUserModel.getLastName();

                            BaseWrapper baseWrapper = new BaseWrapperImpl();
                            customerModel = new CustomerModel();
                            customerModel.setCustomerId(appUserModel.getCustomerId());
                            baseWrapper.setBasePersistableModel(customerModel);
                            this.commonCommandManager.loadCustomer(baseWrapper);
                            customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();

                            cregComment = customerModel.getRegStateComments();
                            initialDeposit = customerModel.getInitialDeposit();
                            customerAccountType = String.valueOf(customerModel.getCustomerAccountTypeId());
                            customerPictureModelList = this.commonCommandManager.getDiscrepantCustomerPictures(appUserModel.getCustomerId());
                        }
                    } else if (registrationStateId.equals(RegistrationStateConstants.DECLINE)) {
                        throw new FrameworkCheckedException(
                                messageSource
                                        .getMessage(
                                                "checkCustomerExistanceCommand.declinedCustomer", null, null));
                    } else if (isUpgrade.equals("1") && (registrationStateId.equals(RegistrationStateConstants.VERIFIED)||registrationStateId.equals(RegistrationStateConstants.CLSPENDING))) {
                        appUserModel.setRegistrationStateModel(this.commonCommandManager.getRegistrationStateById(registrationStateId));
                        regStateModel = appUserModel.getRegistrationStateModel();
                        if (regStateModel != null) {
                            customerRegistrationState = registrationStateId.toString();
                            customerMsisdn = appUserModel.getMobileNo();
                            customerNic = appUserModel.getNic();
                            cnicExpiry = appUserModel.getNicExpiryDate();
                            customerRegistrationState = regStateModel.getName();
                            customerRegistrationStateId = regStateModel.getRegistrationStateId().toString();
                            dob = appUserModel.getDob();
                            customerName = appUserModel.getFirstName() + " " + appUserModel.getLastName();

                            cregComment = customerModel.getRegStateComments();
                            initialDeposit = customerModel.getInitialDeposit();
                            customerAccountType = String.valueOf(customerModel.getCustomerAccountTypeId());
                            //customerPictureModelList = this.commonCommandManager.getDiscrepantCustomerPictures(appUserModel.getCustomerId());
                        }
                        ///Generate OTP and store in MiniTransaction For Discrepant Flow
                        if (isOTPRequired != null && (isOTPRequired.equals("01") || isOTPRequired.equals("01"))) {

                        } else {
                            if (isPin == null || (isPin != null && (isPin.equals("") || isPin.equals("02")))) {
                                String otp = CommonUtils.generateOneTimePin(5);
                                String encryptedPin = EncoderUtils.encodeToSha(otp);
                                try {
                                    if (deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                                        fonePayManager.createMiniTransactionModel(encryptedPin, this.cMsisdn, otp, CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT_INQUIRY);
                                    } else
                                        fonePayManager.createMiniTransactionModel(encryptedPin, this.cMsisdn, otp, CommandFieldConstants.CMD_OTP_VERIFICATION);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //Send OTP via SMS to Customer
                                String msgToText = "OTP to Upgrade Account is: " + otp;
                                logger.info("CNIC : " + this.cNic + "Mobile # : " + this.cMsisdn + " : " + msgToText);
                                BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
                                msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(this.cMsisdn, msgToText));
                                commonCommandManager.sendSMSToUser(msgBaseWrapper);
                            }
                        }
                    } else {
                        throw new FrameworkCheckedException(
                                messageSource
                                        .getMessage(
                                                "checkCustomerExistanceCommand.AlreadyRegistered", null, null));
                    }
                } else {
                    throw new FrameworkCheckedException(
                            messageSource
                                    .getMessage(
                                            "checkCustomerExistanceCommand.RegistrationStateNull",
                                            null, null));
                }
            }
        } else if (!flagCNic) {
            throw new FrameworkCheckedException(messageSource.getMessage(
                    "checkCustomerExistanceCommand.CnicNotUnique", null, null));
        }

        if (!flagMsisdn && flagCNic) { // MSISDN is not Unique but CNIC is.
            throw new FrameworkCheckedException(
                    messageSource.getMessage(
                            "checkCustomerExistanceCommand.MsisdnNotUnique",
                            null, null));
        }
        // New Customer case, send OTP
        // Check if customer has no pending transactions, then send OTP
        int pendingTransactionCountByCnic = commonCommandManager.countCustomerPendingTrx(this.cNic);
        int pendingTransactionCountByMob = commonCommandManager.countCustomerPendingTrxByMobile(this.cMsisdn);
        if (pendingTransactionCountByCnic == 0 && pendingTransactionCountByMob == 0) {
            if (flagMsisdn == true && flagCNic == true) {
                ///Generate OTP and store in MiniTransaction
                String otp = CommonUtils.generateOneTimePin(5);
                String encryptedPin = EncoderUtils.encodeToSha(otp);
                logger.info("**************************");
                logger.info("Your otp is :" + otp);
                logger.info("**************************");

                try {
                    fonePayManager.createMiniTransactionModel(encryptedPin, this.cMsisdn, "", CommandFieldConstants.CMD_OTP_VERIFICATION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Send OTP via SMS to Customer
                String smsText = MessageUtil.getMessage("otpSmsAccOpening", new String[]{"JS Mobile account opening", "account opening", otp});
                SmsMessage smsMessage = new SmsMessage(this.cMsisdn, smsText);
                logger.info("CNIC : " + this.cNic + " , Mobile # : " + this.cMsisdn + " : " + smsText);
                smsSender.send(smsMessage);
            }
        }
    }

    public AgentSegmentRestrictionManager getAgentSegmentRestrictionManager() {
        return agentSegmentRestrictionManager;
    }

    public void setAgentSegmentRestrictionManager(AgentSegmentRestrictionManager agentSegmentRestrictionManager) {
        this.agentSegmentRestrictionManager = agentSegmentRestrictionManager;
    }

    private CustomerModel getCustomerAccountType(Long customerId) {
        CustomerModel customerModel = null;
        try {
            customerModel = this.getCommonCommandManager().getCustomerModelById(customerId);
        } catch (CommandException e) {
            e.printStackTrace();
        }
        return customerModel;
    }
}
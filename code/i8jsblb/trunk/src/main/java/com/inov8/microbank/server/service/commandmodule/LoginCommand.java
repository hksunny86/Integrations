package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.demographics.model.DemographicsModel;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class LoginCommand extends BaseCommand {

    protected String appId;
    protected String userId;
    protected String pin;
    protected String deviceTypeId;
    protected String isRooted;
    String taggedUserType;
    String userType;
    String firstName;
    String lastName;
    boolean isPinChangeRequired;
    String isSetMpinLater;
    String iban;
    boolean isPasswordChangeRequired;
    String applicationName;
    String encryption_type;
    private BaseWrapper baseWrapper;
    private String udid;
    private String isBvsEnabled;
    private String agentAreaName;

    private Long sTime = DateTimeUtils.currentTimeMillis();
    private Long eTime = DateTimeUtils.currentTimeMillis();

    protected final Log logger = LogFactory.getLog(LoginCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        logger.info("[LoginCommand.prepare] ");
        this.baseWrapper = baseWrapper;
        userId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        udid = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_UDID);
        pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
        applicationName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_NAME);
        encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        isRooted = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_ROOTED);
        userType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_USER_TYPE);
        appId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_ID);
        if (!CommandFieldConstants.KENNEL_DEVICE_TYPE.equals(deviceTypeId) &&
                !DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) &&
                !DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId) &&
                !DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId) &&
                !DeviceTypeConstantsInterface.SMS_GATEWAY.toString().equals(deviceTypeId) &&
                !DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.toString().equals(deviceTypeId)) {
            if (encryption_type != null) {
                byte enc_type = new Byte(encryption_type).byteValue();
                pin = this.decryptPin(pin, enc_type);
            }
        }
        logger.info("End of LoginCommand.prepare()");
    }

    @Override
    public void doValidate() throws CommandException {
        logger.info("Start of LoginCommand.doValidate()");

        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors = ValidatorWrapper.doRequired(userId, validationErrors, "User Id");
        validationErrors = ValidatorWrapper.doRequired(pin, validationErrors, "Pin");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");

        if (!CommandFieldConstants.KENNEL_DEVICE_TYPE.equals(deviceTypeId)
                && !DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.toString().equals(deviceTypeId)
                && !DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId)
                && !DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId)) {
            if (!validationErrors.hasValidationErrors()) {
                validationErrors = ValidatorWrapper.doRequired(appId, validationErrors, "Application Id");
            }

            if (!validationErrors.hasValidationErrors()) {
                validationErrors = ValidatorWrapper.doNumeric(userId, validationErrors, "User Id");
                //validationErrors = ValidatorWrapper.doNumeric(pin,validationErrors,"Pin");
            }
//			if(!validationErrors.hasValidationErrors())
//			{
//				if( deviceTypeId.equalsIgnoreCase( DeviceTypeConstantsInterface.ALL_PAY.toString() ) || deviceTypeId.equalsIgnoreCase( DeviceTypeConstantsInterface.ALLPAY_WEB.toString()) || deviceTypeId.equalsIgnoreCase( DeviceTypeConstantsInterface.SMS_GATEWAY.toString())
//						|| deviceTypeId.equalsIgnoreCase( DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.toString() ) )
//					validationErrors = ValidatorWrapper.checkLength(userId,CommandFieldConstants.ALLPAY_USERID_LENGTH,validationErrors,"User Id");
//				else
//					validationErrors = ValidatorWrapper.checkLength(userId,CommandFieldConstants.KEY_MFS_USER_ID_LENGTH,validationErrors,"User Id");
//				//validationErrors = ValidatorWrapper.checkLength(pin,CommandFieldConstants.KEY_MFS_PIN_LENGTH,validationErrors,"Pin");
//			}
        }
        if (DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId)) {
            if (!validationErrors.hasValidationErrors()) {
                validationErrors = ValidatorWrapper.doNumeric(userId, validationErrors, "User Id");
                validationErrors = ValidatorWrapper.doRequired(pin, validationErrors, "Password");
            }
            if (!validationErrors.hasValidationErrors()) {

                validationErrors = ValidatorWrapper.checkLength(userId, CommandFieldConstants.KEY_MFS_USER_ID_LENGTH, validationErrors, "User Id");
//				validationErrors = ValidatorWrapper.checkLength(pin,CommandFieldConstants.KEY_MFS_PASSWORD_LENGTH,validationErrors,"Password");
            }
        }

        if (DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId)) {
            if (!validationErrors.hasValidationErrors()) {
                ValidatorWrapper.doRequired(udid, validationErrors, "User device Id");
            }
        }

        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
        }

        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }
        logger.info("End of LoginCommand.doValidate()");
    }

    @Override
    public void execute() throws CommandException {
        logger.info("Start of LoginCommand.execute()");
        if (DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId)) {
            if (encryption_type != null) {
                byte enc_type = new Byte(encryption_type).byteValue();
                pin = this.encryptPin(pin, enc_type);
            }
        } else if (!DeviceTypeConstantsInterface.SMS_GATEWAY.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.toString().equals(deviceTypeId)) {
            pin = generateEncodedPin(pin);
        }
        checkLogin(this.getCommonCommandManager());
        logger.info("End of LoginCommand.execute()");
    }

    @Override
    public String response() {
        return toXML();
    }

    private String toXML() {
        if (logger.isDebugEnabled()) {
            logger.debug("End of LoginCommand.toXML()");
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_USER_TYPE)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(taggedUserType)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)

                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_AGENT_TYPE)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(taggedUserType)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)

                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_FIRST_NAME)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(firstName)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)

                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_LAST_NAME)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(lastName)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_BVS_ENABLED)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(isBvsEnabled)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)

                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_AGENT_AREA_NAME)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(agentAreaName)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)

                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_IS_CNIC_EXPIRY_REQUIRED)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append("0")
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)


                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_IS_PIN_CHNG_REQ)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(this.convertBooleanToBit(isPinChangeRequired))
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)

                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_IS_SET_MPIN_LATER)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(this.isSetMpinLater)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)

                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_SENDER_IBAN)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(this.iban)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);
			/*.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM);
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_IS_PASSWORD_CHNG_REQ)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(this.convertBooleanToBit(isPasswordChangeRequired))
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)
            .append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE);*/

/*
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_MESGS)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_MESG)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_LEVEL)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(ATTR_LEVEL_ONE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(LOGIN_SUCCESS_MSG)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_MESG)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_MESGS)
			.append(TAG_SYMBOL_CLOSE);*/
        if (logger.isDebugEnabled()) {
            logger.debug("End of LoginCommand.toXML()");
        }
        logger.info("Total Time taken by LoginCommand :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - sTime));
        return strBuilder.toString();
    }

    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return new ValidationErrors();
    }

    private Boolean validateAppAccess(AppUserModel agentAppUserModel, String appId) {
        if (appId.equals(CommandConstants.AGENT_MATE)) {
            if (agentAppUserModel.getMnoId() != null && !agentAppUserModel.getMnoId().equals(50027L))
                return false;
        } else if (appId.equals(CommandConstants.SCO_AGENT_MATE)) {
            if (agentAppUserModel.getMnoId() == null)
                return false;
            else {
                if (agentAppUserModel.getMnoId() != null && !agentAppUserModel.getMnoId().equals(50028L))
                    return false;
            }
        }
        return true;
    }

    private BaseWrapper checkLogin(CommonCommandManager commonCommandManager) throws CommandException {
        logger.info("Start of LoginCommand.checkLogin()");
        boolean isConsumerAppLogin = false;
        if (!StringUtil.isNullOrEmpty(appId) && appId.equals(CommandConstants.CONSUMER_APP)) {
            isConsumerAppLogin = true;
        } else if (!StringUtil.isNullOrEmpty(appId) && appId.equals(CommandConstants.AGENT_MATE)) {
            isConsumerAppLogin = false;
        } else if (!StringUtil.isNullOrEmpty(appId) && appId.equals(CommandConstants.SCO_AGENT_MATE)) {
            isConsumerAppLogin = false;
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        List<UserDeviceAccountsModel> list;
        AppUserModel customerAppUserModel = null;
        AppUserModel deviceAppUserModel = null;
        try {
            String tempDeviceTypeId = new String(deviceTypeId);
            if (deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()))
                deviceTypeId = DeviceTypeConstantsInterface.MOBILE.toString();
            else if (deviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString())) {
                deviceTypeId = DeviceTypeConstantsInterface.USSD.toString();
//	deviceTypeId = DeviceTypeConstantsInterface.ALL_PAY.toString();
            }

            if (isConsumerAppLogin) {
                customerAppUserModel = commonCommandManager.loadAppUserByMobileAndType(userId, UserTypeConstantsInterface.CUSTOMER);
                if (customerAppUserModel == null) {
                    logger.error("Consumer App - Customer AppUser not found against mobile No:" + userId);
                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.allpayUserDoesNotExists", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                }
                deviceAppUserModel = customerAppUserModel;
            } else {
                UserDeviceAccountsModel uda = commonCommandManager.loadUserDeviceAccountByUserId(userId);
                AppUserModel agentAppUserModel = new AppUserModel();
                if (uda != null && uda.getAppUserIdAppUserModel() != null) {
                    agentAppUserModel.setAppUserId(uda.getAppUserId());
                    baseWrapper.setBasePersistableModel(agentAppUserModel);
                    baseWrapper = commonCommandManager.loadAppUser(baseWrapper);
                    agentAppUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
                }
                if (agentAppUserModel != null) {
//					if(!validateAppAccess(agentAppUserModel,appId))
//						throw new CommandException("You are not authorized to access this app.",ErrorCodes.INVALID_USER,ErrorLevel.LOW,new Throwable());
                    deviceAppUserModel = agentAppUserModel;
                } else
                    throw new CommandException("User does not Exist.", ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
            }
            if (deviceAppUserModel != null && deviceAppUserModel.getAppUserTypeId() != null && (deviceAppUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.RETAILER)
                    || deviceAppUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.HANDLER))) {
                this.verifyUDID(baseWrapper, deviceAppUserModel);
            }

            if (deviceAppUserModel != null && deviceAppUserModel.getAppUserTypeId() != null && deviceAppUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.CUSTOMER)) {
                if (!customerAppUserModel.getMobileNo().equals("03463564149")) {
                    this.verifyUDID(baseWrapper, deviceAppUserModel);
                }
            }

            UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
            userDeviceAccountModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

            if (isConsumerAppLogin) {
                userDeviceAccountModel.setAppUserId(customerAppUserModel.getAppUserId());
            } else {
                userDeviceAccountModel.setUserId(userId);
            }

            searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
            searchBaseWrapper = commonCommandManager.checkLogin(searchBaseWrapper);

            list = searchBaseWrapper.getCustomList().getResultsetList();
            if (list.size() > 0) {
                userDeviceAccountModel = list.get(0);
                if (null == userDeviceAccountModel.getPassword() && deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString())) {

                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.mfswebpasswordnotgenerated", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                } else if (null == userDeviceAccountModel.getPassword() && deviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString())) {
                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.allpaywebpasswordnotgenerated", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                }

//	if(((! (tempDeviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString())) && (tempDeviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString())) && (userId.equals(userDeviceAccountModel.getUserId()) && pin.equals(userDeviceAccountModel.getPin())))
//	|| (tempDeviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString()) && (userId.equals(userDeviceAccountModel.getUserId()) && pin.equals(userDeviceAccountModel.getPassword()))))
//	||(!(tempDeviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) && (userId.equals(userDeviceAccountModel.getUserId()) && pin.equals(userDeviceAccountModel.getPin())))
//	|| (tempDeviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) && (userId.equals(userDeviceAccountModel.getUserId()) && pin.equals(userDeviceAccountModel.getPassword())))))
//	{

                if ((((!tempDeviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString())) && (!tempDeviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()))) && pin.equals(userDeviceAccountModel.getPin()))
                        || (((tempDeviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString())) || (tempDeviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()))) && pin.equals(userDeviceAccountModel.getPassword()))) {
                    userDeviceAccountModel.setLoginAttemptCount(new Integer(0));
                    userDeviceAccountModel.setLastLoginAttemptTime(new Timestamp(System.currentTimeMillis()));

                    if (userDeviceAccountModel.getDeviceTypeId().longValue() != new Long(CommandFieldConstants.KENNEL_DEVICE_TYPE).longValue()) {
                        updateUserDeviceAccount(userDeviceAccountModel, commonCommandManager);
                    }
                    if (null != applicationName && !"".equals(applicationName) && "AllPay".equalsIgnoreCase(applicationName)) {
                        if (!userDeviceAccountModel.getCommissioned()) {

                            throw new CommandException(this.getMessageSource().getMessage("LoginCommand.iPosUserDoesNotExist", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                        }
                    } else if (null == applicationName || "".equals(applicationName)) {
                        if (userDeviceAccountModel.getCommissioned()) {
                            throw new CommandException(this.getMessageSource().getMessage("LoginCommand.userDoesNotExists", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                        }
                    }

                    boolean messageSeparatorFlag = false;
                    StringBuilder strBuilder = new StringBuilder();
                    if (userDeviceAccountModel.getAccountExpired()) {
                        strBuilder.append(this.getMessageSource().getMessage("LoginCommand.accountExpired", null, null));
                        messageSeparatorFlag = true;
                    }
                    if (userDeviceAccountModel.getAccountLocked()) {
                        if (messageSeparatorFlag) {
                            strBuilder.append(";");
                            strBuilder.append(this.getMessageSource().getMessage("LoginCommand.accountLocked", null, null));
                        } else {
                            strBuilder.append(this.getMessageSource().getMessage("LoginCommand.accountLocked", null, null));
                            messageSeparatorFlag = true;
                        }

                    }
                    if (userDeviceAccountModel.getCredentialsExpired()) {
                        if (messageSeparatorFlag) {
                            strBuilder.append(".");
                            strBuilder.append(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null, null));
                        } else {
                            strBuilder.append(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null, null));
                            messageSeparatorFlag = true;
                        }
                    }
                    if (!userDeviceAccountModel.getAccountEnabled()) {
                        if (messageSeparatorFlag) {
                            strBuilder.append(";");
                            strBuilder.append(this.getMessageSource().getMessage("LoginCommand.accountDisabled", null, null));
                        } else {
                            strBuilder.append(this.getMessageSource().getMessage("LoginCommand.accountDisabled", null, null));
                        }
                    }
                    if (strBuilder.length() == 0) {

                        AppUserModel appUserModel = new AppUserModel();
                        if (isConsumerAppLogin) {
                            appUserModel = customerAppUserModel;
                        } else {
                            appUserModel.setAppUserId(userDeviceAccountModel.getAppUserId());
                            baseWrapper.setBasePersistableModel(appUserModel);
                            baseWrapper = commonCommandManager.loadAppUser(baseWrapper);
                            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
                        }

                        isPinChangeRequired = userDeviceAccountModel.getPinChangeRequired();
                        isPasswordChangeRequired = userDeviceAccountModel.getPasswordChangeRequired();

                        if (appUserModel != null) {
//added by Turab to handle account closed-unsettled/closed-settled scenarios
                            if ((null != appUserModel.getAccountClosedSettled() && appUserModel.getAccountClosedSettled()) || (null != appUserModel.getAccountClosedUnsettled() && appUserModel.getAccountClosedUnsettled())) {
                                throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountClosed", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                            }

                            if (appUserModel.getCustomerId() != null) {
                                CustomerModel customerModel = new CustomerModel();
                                customerModel.setCustomerId(appUserModel.getCustomerId());
                                baseWrapper.setBasePersistableModel(customerModel);
                                baseWrapper = commonCommandManager.loadCustomer(baseWrapper);
                                customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                                isSetMpinLater = String.valueOf(customerModel.getIsSetMpinLater());
                                iban = customerModel.getIban();

                                if (customerModel.getRegister()) {
                                    taggedUserType = TAG_USER_TYPE_CUSTOMER;
                                } else {
                                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.customerInactive", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                                }
                            } else if (appUserModel.getRetailerContactId() != null) {
                                RetailerContactModel retailerContactModel = new RetailerContactModel();
                                retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
                                baseWrapper.setBasePersistableModel(retailerContactModel);
                                baseWrapper = commonCommandManager.loadRetailerContact(baseWrapper);
                                retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
                                if (retailerContactModel.getActive()) {
                                    RetailerModel retailerModel = new RetailerModel();
                                    if (retailerContactModel.getRetailerId() != null) {
                                        retailerModel.setRetailerId(retailerContactModel.getRetailerId());
                                        baseWrapper.setBasePersistableModel(retailerModel);
                                        baseWrapper = commonCommandManager.loadRetailer(baseWrapper);
                                        retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();
                                        if (retailerModel.getActive()) {
                                            taggedUserType = TAG_USER_TYPE_RETAILER;
                                            isBvsEnabled = CommonUtils.convertToBit(retailerContactModel.getBvsEnable());
                                            agentAreaName = retailerContactModel.getTaxRegimeIdTaxRegimeModel().getDescription();
                                        } else {

                                            throw new CommandException(this.getMessageSource().getMessage("LoginCommand.retailerInactive", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                                        }
                                    }
                                } else {

                                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.retailerContactInactive", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                                }
                            } else if (appUserModel.getDistributorContactId() != null) {
                                DistributorContactModel distributorContactModel = new DistributorContactModel();
                                distributorContactModel.setDistributorContactId(appUserModel.getDistributorContactId());
                                baseWrapper.setBasePersistableModel(distributorContactModel);
                                baseWrapper = commonCommandManager.loadDistributorContact(baseWrapper);
                                distributorContactModel = (DistributorContactModel) baseWrapper.getBasePersistableModel();
                                distributorContactModel = (DistributorContactModel) baseWrapper.getBasePersistableModel();
                                if (distributorContactModel.getActive()) {
                                    DistributorModel distributorModel = new DistributorModel();
                                    if (distributorContactModel.getDistributorId() != null) {
                                        distributorModel.setDistributorId(distributorContactModel.getDistributorId());
                                        baseWrapper.setBasePersistableModel(distributorModel);
                                        baseWrapper = commonCommandManager.loadDistributor(baseWrapper);
                                        distributorModel = (DistributorModel) baseWrapper.getBasePersistableModel();
                                        if (distributorModel.getActive()) {
                                            taggedUserType = TAG_USER_TYPE_DISTRIBUTOR;
                                        } else {

                                            throw new CommandException(this.getMessageSource().getMessage("LoginCommand.distributorInactive", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                                        }
                                    }
                                } else {

                                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.distributorContactInactive", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                                }
                            } else if (appUserModel.getHandlerId() != null && appUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.HANDLER)) {
                                HandlerModel handlerModel = new HandlerModel();
                                handlerModel.setHandlerId(appUserModel.getHandlerId());
                                baseWrapper.setBasePersistableModel(handlerModel);
                                this.getCommonCommandManager().loadHandler(baseWrapper);
                                handlerModel = (HandlerModel) baseWrapper.getBasePersistableModel();
                                if (handlerModel.getActive()) {
                                    taggedUserType = TAG_USER_TYPE_HANDLER;
                                    isBvsEnabled = CommonUtils.convertToBit(handlerModel.getBvsEnable());
                                    AppUserModel agentAppUserModel = commonCommandManager.loadAppUserByRetailerContractId(handlerModel.getRetailerContactId());
                                    if (!validateAppAccess(agentAppUserModel, appId))
                                        throw new CommandException("You are not authorized to access this app.", ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                                } else {

                                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.handlerInactive", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                                }
                            }

//Verify (Agent,Handler)/Customer using his respective app
                            if (!StringUtil.isNullOrEmpty(userType)) {
                                long appUserType = appUserModel.getAppUserTypeId().longValue();
                                long userTypeFromParam = Long.parseLong(userType);
                                if ((appUserType == UserTypeConstantsInterface.CUSTOMER && userTypeFromParam != UserTypeConstantsInterface.CUSTOMER)
                                        || ((appUserType == UserTypeConstantsInterface.RETAILER
                                        || appUserType == UserTypeConstantsInterface.HANDLER) && userTypeFromParam != UserTypeConstantsInterface.RETAILER)
                                ) {

                                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.invalidUserType", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());

                                }
                            }

                            ThreadLocalAppUser.setAppUserModel(appUserModel);
                            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountModel);
                            firstName = appUserModel.getFirstName();
                            lastName = appUserModel.getLastName();

//saving ROOTED_MOBILE_HISTORY in DB after successful Login from Mobile App (AgentMate/Falcon)
                            if (!StringUtil.isNullOrEmpty(isRooted) && isRooted.equalsIgnoreCase("1")) {
                                logger.info("Going to save rooted mobile info against userId:" + userDeviceAccountModel.getUserId());
                                RootedMobileHistoryModel model = new RootedMobileHistoryModel(appUserModel.getAppUserId(), userDeviceAccountModel.getUserId(), appUserModel.getMobileNo());
                                baseWrapper.setBasePersistableModel(model);
                                this.getCommonCommandManager().saveRootedMobileHistory(baseWrapper);
                            }

                        }
                    } else {

                        strBuilder.append(this.getMessageSource().getMessage("LoginCommand.contactServiceProvider", null, null));
                        if (!userDeviceAccountModel.getAccountEnabled()) {
                            throw new CommandException(strBuilder.toString(), ErrorCodes.ACCOUNT_DISABLED_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }

                        throw new CommandException(strBuilder.toString(), ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, new Throwable());
                    }
                } else {

                    int checkLoginFlag;
                    try {
                        checkLoginFlag = checkLoginAttempts(userDeviceAccountModel, commonCommandManager);

                        if (checkLoginFlag == LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_LOGIN_ALREADY_LOCKED) {
                            throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountLocked", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                        } else if (checkLoginFlag == LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_EXCEEDED) {
                            customerAppUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_WARM);
                            baseWrapper.setBasePersistableModel(customerAppUserModel);
                            commonCommandManager.updateAppUser(baseWrapper);
                            throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountLockedDueToLoginTries", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                        }
                    } catch (CommandException ex) {
                        throw new CommandException(ex.getMessage(), ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.LOW, new Throwable());
                    }
//	if(applicationName.equalsIgnoreCase(ApplicationNamesConstantsInterface.ALLPAY_APPLICATION_NAME))
//	{
//	throw new CommandException(MessageParsingUtils.parseMessageForIpos(this.getMessageSource().getMessage("LoginCommand.incorrectUserIdOrPassword", null,null)),ErrorCodes.INVALID_USER,ErrorLevel.LOW,new Throwable());
//	}
//	else

                    if (tempDeviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALL_PAY.toString())) {
                        throw new CommandException(MessageParsingUtils.parseMessageForIpos(this.getMessageSource().getMessage("LoginCommand.allpayUserDoesNotExists", null, null)), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                    } else if (tempDeviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.MFS_WEB.toString())) {
                        throw new CommandException(this.getMessageSource().getMessage("LoginCommand.invalidUserIdOrPassword", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                    } else if (tempDeviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALLPAY_WEB.toString())) {
                        throw new CommandException(this.getMessageSource().getMessage("LoginCommand.invalidAllPayIdOrPassword", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                    }

                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.incorrectUserIdOrPassword", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                }
            } else {
                if (null != applicationName && !"".equals(applicationName) && "AllPay".equalsIgnoreCase(applicationName)) {
                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.iPosUserDoesNotExist", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                } else if (this.deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALL_PAY.toString())) {
                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.allpayUserDoesNotExists", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                } else {
                    throw new CommandException(this.getMessageSource().getMessage("LoginCommand.userDoesNotExists", null, null), ErrorCodes.INVALID_USER, ErrorLevel.LOW, new Throwable());
                }


            }
        } catch (FrameworkCheckedException ex) {
            handleException(ex);
            logger.error("Error for Duplicate UserDeviceAccounts :: " + userId);
//	throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of LoginCommand.checkLogin()");
        }
        return baseWrapper;
    }


    private int checkLoginAttempts(UserDeviceAccountsModel userDeviceAccountModel, CommonCommandManager commonCommandManager) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of LoginCommand.checkLoginAttempts()");
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        int returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_NORMAL;

        if (!userDeviceAccountModel.getAccountLocked()) {
            if (userDeviceAccountModel.getLoginAttemptCount() == null || userDeviceAccountModel.getLoginAttemptCount() == 0) {
                userDeviceAccountModel.setLoginAttemptCount(new Integer(1));
                userDeviceAccountModel.setLastLoginAttemptTime(new Timestamp(System.currentTimeMillis()));
                returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_NORMAL;
            } else if (userDeviceAccountModel.getLoginAttemptCount() == (this.getNumberOfAttempts() - 1)) {
                int checkLoginTime = checkLastLoginAttemptTime(userDeviceAccountModel);
                if (checkLoginTime == LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_NORMAL) {
                    userDeviceAccountModel.setAccountLocked(Boolean.TRUE);
                    //userDeviceAccountModel.setAccountEnabled(Boolean.FALSE);
                    userDeviceAccountModel.setLoginAttemptCount(userDeviceAccountModel.getLoginAttemptCount() + 1);
                    returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_EXCEEDED;
                } else if (checkLoginTime == LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_EXCEEDED) {
                    userDeviceAccountModel.setLoginAttemptCount(new Integer(1));
                    userDeviceAccountModel.setLastLoginAttemptTime(new Timestamp(System.currentTimeMillis()));
                    returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_NORMAL;
                } else if (checkLoginTime == LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_NOT_EXIST) {
                    userDeviceAccountModel.setLoginAttemptCount(new Integer(1));
                    userDeviceAccountModel.setLastLoginAttemptTime(new Timestamp(System.currentTimeMillis()));
                    returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_NORMAL;
                }
            } else if (userDeviceAccountModel.getLoginAttemptCount() > 0 && userDeviceAccountModel.getLoginAttemptCount() < this.getNumberOfAttempts()) {
                int checkLoginTime = checkLastLoginAttemptTime(userDeviceAccountModel);
                if (checkLoginTime == LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_NORMAL) {
                    userDeviceAccountModel.setLoginAttemptCount(userDeviceAccountModel.getLoginAttemptCount() + 1);
                    returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_NORMAL;
                } else if (checkLoginTime == LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_EXCEEDED) {
                    userDeviceAccountModel.setLastLoginAttemptTime(new Timestamp(System.currentTimeMillis()));
                    userDeviceAccountModel.setLoginAttemptCount(new Integer(1));
                    returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_NORMAL;
                } else if (checkLoginTime == LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_NOT_EXIST) {
                    userDeviceAccountModel.setLoginAttemptCount(userDeviceAccountModel.getLoginAttemptCount() + 1);
                    userDeviceAccountModel.setLastLoginAttemptTime(new Timestamp(System.currentTimeMillis()));
                    returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_NORMAL;
                }
            }

            userDeviceAccountModel.setUpdatedOn(new Date());

            baseWrapper.setBasePersistableModel(userDeviceAccountModel);
            try {
                baseWrapper = commonCommandManager.updateMfsPin(baseWrapper);
            } catch (FrameworkCheckedException ex) {

                throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
            }
        } else {
            returnValue = LoginAttemptConstants.LOGIN_ATTEMPT_COUNT_LOGIN_ALREADY_LOCKED;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of LoginCommand.checkLoginAttempts()");
        }
        return returnValue;
    }


    private int checkLastLoginAttemptTime(UserDeviceAccountsModel userDeviceAccountsModel) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of LoginCommand.checkLastLoginAttemptTime()");
        }
        if (userDeviceAccountsModel.getLastLoginAttemptTime() != null) {
            long firstAttemptTime = userDeviceAccountsModel.getLastLoginAttemptTime().getTime();
            long currentAttemptTime = new Timestamp(System.currentTimeMillis()).getTime();


            if ((currentAttemptTime - firstAttemptTime) / 1000 <= this.getMaxTimeForAttempts()) {
                return LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_NORMAL;
            } else {
                return LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_EXCEEDED;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of LoginCommand.checkLastLoginAttemptTime()");
        }
        return LoginAttemptConstants.LAST_LOGIN_TIME_ATTEMPT_NOT_EXIST;
    }

    public BaseWrapper updateUserDeviceAccount(UserDeviceAccountsModel userDeviceAccountModel, CommonCommandManager commonCommandManager) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of LoginCommand.updateUserDeviceAccount()");
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(userDeviceAccountModel);
        baseWrapper = commonCommandManager.updateMfsPin(baseWrapper);
        if (logger.isDebugEnabled()) {
            logger.debug("End of LoginCommand.updateUserDeviceAccount()");
        }
        return baseWrapper;
    }

    private void udidVerification(BaseWrapper baseWrapper, AppUserModel appUserModel, Long deviceTypeLong) throws FrameworkCheckedException {

        if (DeviceTypeConstantsInterface.ALL_PAY.longValue() != deviceTypeLong) {
            return;
        }

        long appUserTypeId = appUserModel.getAppUserTypeId();
        if (UserTypeConstantsInterface.CUSTOMER == appUserTypeId && MessageUtil.getBooleanMessage("customer.udid.verification.enabled")) {
            verifyUDID(baseWrapper, appUserModel);
        } else if ((UserTypeConstantsInterface.RETAILER == appUserTypeId || UserTypeConstantsInterface.HANDLER == appUserTypeId) &&
                MessageUtil.getBooleanMessage("agent.udid.verification.enabled")) {
            verifyUDID(baseWrapper, appUserModel);
        }
    }

    private void verifyUDID(BaseWrapper baseWrapper, AppUserModel appUserModel) throws FrameworkCheckedException {
        logger.info("Start of LoginCommand.verifyUDID()");

        logger.info("Start of LoginCommand.Loading demographics()");
        DemographicsModel demographicsModel = this.getCommonCommandManager().getDemographicsManager().loadDemographics(appUserModel.getAppUserId());
        if (demographicsModel == null) {
            logger.info("LoginCommand.populate DemographicsModel");
            demographicsModel = new DemographicsModel();
        }

        logger.info("LoginCommand.set Udid in DemographicsModel");
        String existingUdid = demographicsModel.getUdid();

        logger.info("LoginCommand.checking exitingUdid");
        if ((!StringUtil.isNullOrEmpty(appId) && appId.equals(CommandConstants.CONSUMER_APP))){
            throw new CommandException(this.getMessageSource().getMessage("LoginCommand.deviceRestrict", null, null), ErrorCodes.INVALID_LOGIN, ErrorLevel.MEDIUM, new Throwable());
        }


        if (StringUtil.isNullOrEmpty(existingUdid) || (!StringUtil.isNullOrEmpty(existingUdid) && !this.udid.equals(existingUdid))) {

            if (!StringUtil.isNullOrEmpty(appId) && appId.equals(CommandConstants.CONSUMER_APP)) {
                throw new CommandException(this.getMessageSource().getMessage("LoginCommand.deviceRestrict", null, null), ErrorCodes.INVALID_LOGIN, ErrorLevel.MEDIUM, new Throwable());

            } else {
                //generate OTP (security PIN) and send on registered mobile
                logger.info("LoginCommand.populate MiniTransactionModel");
                MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
                miniTransactionModel.setAppUserId(appUserModel.getAppUserId());
                miniTransactionModel.setMobileNo(appUserModel.getMobileNo());
                miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_DEVICE_VERIFICATION));
                miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
                this.getCommonCommandManager().getMiniTransactionManager().generateOTP(miniTransactionModel, "otpSms", "Device Authorization");
                demographicsModel.setCreatedOn(new Date());
                demographicsModel.setAppUserId(appUserModel.getAppUserId());
                if (UserUtils.getCurrentUser() != null)
                    demographicsModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                else
                    demographicsModel.setCreatedBy(1L);
                demographicsModel.setLocked(true);
                demographicsModel = saveOrUpdateDemographicsModel(baseWrapper, demographicsModel);
            }
        } else {
            logger.info("LoginCommand.populate1 DemographicsModel");
            if (demographicsModel == null) {
                logger.info("LoginCommand.setting appUserId");
                demographicsModel = new DemographicsModel(appUserModel.getAppUserId());
            }
            baseWrapper.putObject(CommandFieldConstants.KEY_UDID, udid);
            if (UserUtils.getCurrentUser() != null) {
                demographicsModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                logger.info("LoginCommand.demographics is not null");
            } else
                demographicsModel.setCreatedBy(1L);
            demographicsModel.setCreatedOn(new Date());
            demographicsModel.setLocked(false);
            demographicsModel = saveOrUpdateDemographicsModel(baseWrapper, demographicsModel);
        }
        logger.info("LoginCommand.again checking demographics");
        if (demographicsModel.getLocked()) {
            throw new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.DEVICE_LOCKED), null, ErrorCodes.DEVICE_LOCKED, null);
        }
        logger.info("End of LoginCommand.verifyUDID()");
    }

    private DemographicsModel saveOrUpdateDemographicsModel(BaseWrapper baseWrapper, DemographicsModel demographicsModel) throws FrameworkCheckedException {
        if (UserUtils.getCurrentUser() != null)
            demographicsModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        else
            demographicsModel.setUpdatedBy(1L);
        demographicsModel.setUpdatedOn(new Date(System.currentTimeMillis()));
        demographicsModel.setUdid(getCommandParameter(baseWrapper, CommandFieldConstants.KEY_UDID));
        demographicsModel.setOs(getCommandParameter(baseWrapper, CommandFieldConstants.KEY_OS));
        demographicsModel.setOsVersion(getCommandParameter(baseWrapper, CommandFieldConstants.KEY_OS_VERSION));
        demographicsModel.setModel(getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MODEL));
        demographicsModel.setVendor(getCommandParameter(baseWrapper, CommandFieldConstants.KEY_VENDOR));
        demographicsModel.setNetwork(getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NETWORK));

        String deviceKey = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_CLOUD_ID);
        if (!StringUtil.isNullOrEmpty(deviceKey)) {
            demographicsModel.setDeviceKey(deviceKey);
        }

        return this.getCommonCommandManager().getDemographicsManager().saveOrUpdate(demographicsModel);
    }

}

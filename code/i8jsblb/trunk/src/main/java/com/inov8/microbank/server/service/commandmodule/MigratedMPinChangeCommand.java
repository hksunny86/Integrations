package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.constants.VeriflyConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class MigratedMPinChangeCommand extends BaseCommand {

    protected AppUserModel appUserModel;
    protected String accountId;
    protected String bankId;
    protected String newPin;
    protected String confirmPin;
    protected String deviceTypeId;
    String veriflyErrorMessage;
    boolean errorMessagesFlag;
    UserDeviceAccountsModel userDeviceAccountsModel;
    protected String encryption_type;
    private String isForceful;
    private String channelId;
    private String terminalId;
    protected final Log logger = LogFactory.getLog(MigratedMPinChangeCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        if (logger.isDebugEnabled())
            logger.debug("Start of MigratedPinChangeCommand.prepare()");

        appUserModel = ThreadLocalAppUser.getAppUserModel();
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
        bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
        encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);

        newPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NEW_PIN);
        newPin = StringUtil.replaceSpacesWithPlus(newPin);
        confirmPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CONF_PIN);
        confirmPin = StringUtil.replaceSpacesWithPlus(confirmPin);
        terminalId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        channelId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        isForceful = getCommandParameter(baseWrapper, "IS_FORCEFUL");

        if (logger.isDebugEnabled())
            logger.debug("End of MigratedPinChangeCommand.prepare()");
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (logger.isDebugEnabled())
            logger.debug("Start of MigratedPinChangeCommand.validate()");
        validationErrors = ValidatorWrapper.doRequired(newPin, validationErrors, "New Pin");
        validationErrors = ValidatorWrapper.doRequired(confirmPin, validationErrors, "Confirm Pin");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");

        if (!validationErrors.hasValidationErrors()) {
            byte enc_type = new Byte(encryption_type).byteValue();
            ThreadLocalEncryptionType.setEncryptionType(enc_type);
        }

        if (!validationErrors.hasValidationErrors())
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
        if (logger.isDebugEnabled())
            logger.debug("End of MigratedPinChangeCommand.validate()");
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        if (logger.isDebugEnabled())
            logger.debug("Start of MigratedPinChangeCommand.execute()");
        userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();

        logger.info("\n\nMigratedPinChangeCommand");
        logger.info(
                "\nApp User Id = " + appUserModel.getAppUserId()
                        + "\nAppUser Type Id " + appUserModel.getAppUserTypeId()
                        + "\nAppUser Retailer Id " + appUserModel.getRetailerContactId()
                        + "\nAppUser Handler Id " + appUserModel.getHandlerId()
                        + "\nAppUser Customer Id " + appUserModel.getCustomerId()
        );

        try {
            if (appUserModel.getCustomerId() != null || appUserModel.getRetailerContactId() != null ||
                    (appUserModel.getHandlerId() != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue())) {

                ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);

                if (!validationError.hasValidationErrors()) {
                    BaseWrapper smaWrapper = new BaseWrapperImpl();

                    if (appUserModel.getHandlerId() != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {
                        smartMoneyAccountModel.setHandlerId(appUserModel.getHandlerId());
                    } else if (appUserModel.getRetailerContactId() != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

                        smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
                        smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                    } else if (appUserModel.getCustomerId() != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

                        smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
                    }
                    smartMoneyAccountModel.setActive(Boolean.TRUE);
                    smartMoneyAccountModel.setAccountClosedSetteled(0L);
                    smartMoneyAccountModel.setAccountClosedUnsetteled(0L);
                    smaWrapper.setBasePersistableModel(smartMoneyAccountModel);
                    smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                    if (smartMoneyAccountModel == null) {
                        logger.error("smart money account not found");
                        throw new CommandException(this.getMessageSource().getMessage("phoenix.trans.02", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }
                    BaseWrapper baseWrapper = new BaseWrapperImpl();
                    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
                    AbstractFinancialInstitution abstractFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);
                    if (abstractFinancialInstitution != null) {
                        baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
                        smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
                        if (smartMoneyAccountModel.getActive()) {
                            if (smartMoneyAccountModel.getName() != null) {
                                if ((null != smartMoneyAccountModel.getCustomerId() && smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString())) ||
                                        (null != smartMoneyAccountModel.getRetailerContactId() && smartMoneyAccountModel.getRetailerContactId().toString().equals(appUserModel.getRetailerContactId().toString())) ||
                                        (null != smartMoneyAccountModel.getHandlerId() && smartMoneyAccountModel.getHandlerId().longValue() == appUserModel.getHandlerId().longValue())) {
                                    AccountInfoModel accountInfoModel = new AccountInfoModel();
                                    if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                                        accountInfoModel.setCustomerId(appUserModel.getCustomerId());
                                    } else {
                                        accountInfoModel.setCustomerId(appUserModel.getAppUserId());
                                    }
                                    accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
                                    accountInfoModel.setNewPin(newPin);
                                    accountInfoModel.setConfirmNewPin(confirmPin);
                                    accountInfoModel.setIsMigrated(1L);
                                    commonCommandManager.updateAccountInfoModel(accountInfoModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT
                                            , 1L);
                                    LogModel logModel = new LogModel();
                                    logModel.setCreatedBy(appUserModel.getUsername());
                                    logModel.setCreatdByUserId(appUserModel.getAppUserId());
                                    veriflyBaseWrapper.setLogModel(logModel);
                                    veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

                                    SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                                    switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
                                    switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
                                    switchWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, newPin);
                                    switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
                                    switchWrapper.putObject(CommandFieldConstants.KEY_OPERATOR_MODEL, this.getOperatorModel());
                                    veriflyBaseWrapper.putObject(SwitchConstants.KEY_SWITCHWRAPPER, switchWrapper);


                                    veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

                                    veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
                                    veriflyBaseWrapper.putObject("IS_MIGRATED", "1");
                                    veriflyBaseWrapper.putObject("IS_FORCEFUL", isForceful);

                                    veriflyBaseWrapper = abstractFinancialInstitution.changePin(veriflyBaseWrapper);
                                    logger.info("\n-------------------Migrated MPinChangeCommand-----------------------------");
                                    logger.info(appUserModel.getUsername());
                                    logger.info(appUserModel.getAppUserId());
                                    logger.info(smartMoneyAccountModel.getSmartMoneyAccountId());
                                    logger.info(smartMoneyAccountModel.getHandlerId());
                                    logger.info("\n-------------------Migrated MPinChangeCommand-----------------------------");
                                    errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

                                    if (errorMessagesFlag) {
                                        //accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
                                        smartMoneyAccountModel.setChangePinRequired(Boolean.FALSE);
                                        smartMoneyAccountModel.setUpdatedOn(new Date());
                                        smartMoneyAccountModel.setUpdatedByAppUserModel(appUserModel);
                                        baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
                                        commonCommandManager.updateSmartMoneyAccount(baseWrapper);
                                    } else {
                                        veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
                                        throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                                    }

                                    logger.info("Channel ID:" + channelId);
                                    if(!channelId.equals("NOVA")) {
                                        String smsText = MessageUtil.getMessage("createdMpin", new String[]{String.valueOf(new Date())});
                                        SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), smsText);
                                        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, smsMessage);
                                        if (deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                                            if (!(channelId.equals("AMA") && terminalId.equals("AMA"))) {
                                                this.getCommonCommandManager().getSmsSender().send(smsMessage);
                                            }
                                        } else {
                                            this.getCommonCommandManager().getSmsSender().send(smsMessage);
                                        }
                                    }
                                    if (appUserModel.getCustomerId() != null) {
                                        BaseWrapper _baseWrapper = new BaseWrapperImpl();
                                        _baseWrapper.setBasePersistableModel(new CustomerModel(appUserModel.getCustomerId()));
                                        this.getCommonCommandManager().loadCustomer(_baseWrapper);
                                        CustomerModel customerModel = (CustomerModel) _baseWrapper.getBasePersistableModel();
                                        customerModel.setIsMPINGenerated(Boolean.TRUE);
                                        this.getCommonCommandManager().saveOrUpdateCustomerModel(customerModel);
                                    } else {
                                        BaseWrapper bwrapper = new BaseWrapperImpl();
                                        bwrapper.setBasePersistableModel(new RetailerContactModel(appUserModel.getRetailerContactId()));
                                        this.getCommonCommandManager().loadRetailerContact(bwrapper);
                                        RetailerContactModel retailerContactModel = (RetailerContactModel) bwrapper.getBasePersistableModel();
                                        this.getCommonCommandManager().saveOrUpdateRetailerContactModel(retailerContactModel);
                                    }
                                } else
                                    throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.invalidAccountId", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                            } else
                                throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.invalidAccountId", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        } else
                            throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    } else
                        throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.veriflyManagerNotExists", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                } else
                    throw new CommandException(validationError.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            } else
                throw new CommandException(this.getMessageSource().getMessage("command.invalidAppUserType", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of MigratedMPinChangeCommand.execute()");
        }
    }

    @Override
    public String response() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of MigratedMPinChangeCommand.response()");
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN)
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
                .append(TAG_SYMBOL_CLOSE);
        strBuilder.append(MessageParsingUtils.parseMessageForIpos(this.getMessageSource().getMessage("mPinset", null, null)));
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_MESG)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_MESGS)
                .append(TAG_SYMBOL_CLOSE);
        return strBuilder.toString();
    }
}

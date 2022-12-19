package com.inov8.microbank.server.service.commandmodule;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class ChangeLoginPinCommand extends BaseCommand {
    protected AppUserModel appUserModel;
    private AccountInfoModel accountInfoModel;
    FinancialInstitution olaVeriflyFinancialInstitution;

    protected String mobileNo, cnic;
    private Boolean isError=false;

    protected final Log logger = LogFactory.getLog(ChangeLoginPinCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        logger.info ("Start of ChangeLoginPinCommand.prepare()");
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        cnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        try {
            appUserModel = this.getCommonCommandManager().loadAppUserByCnicAndMobileAndAppUserType(mobileNo, cnic,UserTypeConstantsInterface.CUSTOMER);
            if(appUserModel!=null){
                ThreadLocalAppUser.setAppUserModel(appUserModel);
            }
            else{
                isError=true;
            }
        }
        catch (Exception e){
            logger.error("[ChangeLoginPinCommand.prepare] Unable to Load AppUserModel by mobile: " + mobileNo ,e);

        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doNumeric(mobileNo, validationErrors, "Mobile No");
        if(isError)
            throw new CommandException("No Customer found against given Cnic",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        logger.info("[ChangeLoginPinCommand.prepare] ChangeLoginPinCommand.execute");

        try {
            UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
            validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
            if (validationErrors.hasValidationErrors()) {
                throw new Exception("Customer is not in correct state.");
            }
            String randomPin = RandomUtils.generateRandom(4, false, true);
            logger.info("************************");
            logger.info("Login Pin: "+randomPin);
            logger.info("************************");
            String password =com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin);//EncoderUtils.encodeToSha(randomPin);

            appUserModel.setPassword(password);
            appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(appUserModel);
            commonCommandManager.getAppUserManager().saveOrUpdateAppUser(baseWrapper);

            String smsText = MessageUtil.getMessage("loginPin", new String[]{randomPin, String.valueOf(new Date())});
            SmsMessage smsMessage = new SmsMessage(mobileNo, smsText);

            this.getCommonCommandManager().getSmsSender().send(smsMessage);

            baseWrapper = new BaseWrapperImpl();
            userDeviceAccountsModel = this.getCommonCommandManager().loadUserDeviceAccountByUserId(appUserModel.getUsername());
            baseWrapper.setBasePersistableModel(userDeviceAccountsModel);

            accountInfoModel = this.getAccountInfoModel();
            if(userDeviceAccountsModel.getCredentialsExpired().equals(Boolean.TRUE)) {
                if (accountInfoModel.getIsMigrated().equals(0L)) {
                    accountInfoModel.setIsMigrated(1L);
                    commonCommandManager.updateAccountInfoModel(accountInfoModel.getCustomerId(), accountInfoModel.getPaymentModeId(), 1L);
                }
            }
            SmartMoneyAccountModel sma = this.commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel
                    ,PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            baseWrapper.setBasePersistableModel(sma);
            if(sma.getChangePinRequired().equals(Boolean.TRUE)){
                sma.setChangePinRequired(Boolean.FALSE);
            }
            if(sma.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED)){
                sma.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
            }

            sma.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);

            this.commonCommandManager.updateSmartMoneyAccount(baseWrapper);

            if(!userDeviceAccountsModel.getAccountLocked().equals(Boolean.FALSE)) {
                userDeviceAccountsModel.setAccountLocked(Boolean.FALSE);
            }
            userDeviceAccountsModel.setPin(password);
            userDeviceAccountsModel.setLoginAttemptCount(new Integer(0));
            userDeviceAccountsModel.setPinChangeRequired(Boolean.TRUE);
            if(userDeviceAccountsModel.getCredentialsExpired().equals(Boolean.TRUE)) {
                userDeviceAccountsModel.setCredentialsExpired(Boolean.FALSE);
            }
            baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
            this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
        }
    }

    private AccountInfoModel getAccountInfoModel() throws FrameworkCheckedException
    {
        AccountInfoModel accountInfoModel = null;
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        if(appUserModel != null)
        {
            Long customerId;
            if(appUserModel.getCustomerId() != null)
                customerId =  appUserModel.getCustomerId();
            else
                customerId = appUserModel.getAppUserId();

            try {

                accountInfoModel = commonCommandManager.getAccountInfoModel(customerId,PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            } catch (Exception e) {
                //e.printStackTrace();
                logger.error("Error in ChangeLoginPinCommand.getAccountinfoModel() :: " + e);
            }
        }
        return accountInfoModel;
    }
    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        String message = "Your Login Pin has been sent to your Mobile Number";

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_MESGS).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_MESG)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_RESP_XML_MSG)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(message).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_MESG)
                .append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_MESGS).append(TAG_SYMBOL_CLOSE);

        return strBuilder.toString();
    }
}

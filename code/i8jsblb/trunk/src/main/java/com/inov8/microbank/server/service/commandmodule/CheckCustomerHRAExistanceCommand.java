package com.inov8.microbank.server.service.commandmodule;

import com.ibm.icu.text.SimpleDateFormat;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.NoSuchMessageException;

import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * Created by Inov8 on 6/19/2018.
 */
public class CheckCustomerHRAExistanceCommand extends BaseCommand {
    protected final Log logger = LogFactory.getLog(CheckCustomerHRAExistanceCommand.class);
    protected AppUserModel appUserModel;
    private CustomerModel customerModel;
    protected BaseWrapper preparedBaseWrapper;

    private String cMsisdn, cNic;
    private CommonCommandManager commonCommandManager;

    private FonePayManager fonePayManager;

    private SmsSender smsSender;

    private String appId;

    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger
                    .debug("Start of CheckCustomerExistanceCommand.prepare()");

        this.appId = getCommandParameter(baseWrapper, "APPID");
        this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        this.preparedBaseWrapper = baseWrapper;
        this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CheckCustomerExistanceCommand.prepare()");
    }

    @Override
    public void doValidate() throws CommandException
    {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CheckCustomerExistanceCommand.validate()");
        ValidationErrors validationErrors = new ValidationErrors();

        if(!appId.equals("2"))
            validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "MSISDN");
        validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
        boolean isValid = CommonUtils.isValidCnic(cNic);
        if(!validationErrors.hasValidationErrors()){
            if(isValid == false){
                validationErrors.getStringBuilder().append("Invalid CNIC");
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");


        if(validationErrors.hasValidationErrors())
        {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,new Throwable());
        }

        if(!validationErrors.hasValidationErrors()) {
            if(getCommonCommandManager().isCnicBlacklisted(this.cNic)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM,new Throwable());
            }
        }

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CheckCustomerExistanceCommand.validate()");
    }


    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
    {
        return new ValidationErrors();
    }


    public void execute() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger
                    .debug("Start of CheckCustomerExistanceCommand.execute()");
        String retVal = null;

        this.commonCommandManager = getCommonCommandManager();
        this.smsSender = getCommonCommandManager().getSmsSender();
        this.fonePayManager = commonCommandManager.getFonePayManager();
        Long error_code = ErrorCodes.COMMAND_EXECUTION_ERROR;

        try {

            if(appId.equals("2"))
            {
                AppUserModel appModel = ThreadLocalAppUser.getAppUserModel();
                if(!appModel.getNic().equals(this.cNic))
                    throw new CommandException("You can update your own Account only",ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
            }
            verifyCustomerAccountType(this.cMsisdn);

            if(appId.equals("2"))
                this.cMsisdn = appUserModel.getMobileNo();

            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
            sma.setCustomerId(appUserModel.getCustomerId());
            sma.setActive(Boolean.TRUE);
            sma.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);

            SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);
            if(smartMoneyAccountModel != null )
            {
                throw new CommandException(MessageUtil.getMessage("hra.account.exists"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
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

        String formattedDOB ;

        if(appUserModel.getDob() == null)
            formattedDOB = "";
        else{
            Date dob = appUserModel.getDob();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            formattedDOB = simpleDateFormat.format(dob);
        }
        String fatherHusbandName;
        if(customerModel.getFatherHusbandName() == null)
            fatherHusbandName = "";
        else
            fatherHusbandName = customerModel.getFatherHusbandName();

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_CNIC)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(appUserModel.getNic()).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.CMD_AGNETMATE_CUSTOMER_MOBILE_NUMBER)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(appUserModel.getMobileNo()).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_CUSTOMER_NAME)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(appUserModel.getFirstName() + " " + appUserModel.getLastName()).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append("FATHER_NAME")
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(fatherHusbandName).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append("CDOB")
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(formattedDOB).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);


        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return strBuilder.toString();
    }

    private void loadAppUserByCNIC(String cnic)
    {
        try {
            if(!appId.equals("2"))
            {
                AppUserModel appModel = this.commonCommandManager.loadAppUserByCNICAndAccountType(cnic,new Long[]{UserTypeConstantsInterface.CUSTOMER});
                if(appModel ==null || appModel.getNic() == null || !appModel.getNic().equals(appUserModel.getNic()))
                    appUserModel = null;
                else
                    appUserModel = appModel;
            }
            else
                appUserModel = this.commonCommandManager.loadAppUserByCNICAndAccountType(cnic,new Long[]{UserTypeConstantsInterface.CUSTOMER});
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }

    private void verifyCustomerAccountType(String mobileNo) throws NoSuchMessageException, FrameworkCheckedException
    {
        String errorMessage = null;
        if(appId.equals("2"))
            errorMessage = "No Customer exists against this CNIC.";
        else
            errorMessage = "No customer exists against this Mobile No.";
        Long[] appUserTypes = new Long[]{UserTypeConstantsInterface.CUSTOMER};

        if(!appId.equals("2"))
        {
            appUserModel = this.commonCommandManager.loadAppUserByMobileAndType(mobileNo, appUserTypes);

            if(appUserModel == null)
                throw new CommandException(errorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

            if(appUserModel.getCustomerId() == null)
            {
                throw new CommandException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            UserDeviceAccountsModel userDeviceAccountsModel = this.commonCommandManager.loadUserDeviceAccountByUserId(appUserModel.getUsername());

            if(userDeviceAccountsModel != null && userDeviceAccountsModel.getAccountEnabled() == false)
                throw new CommandException("Your Account is Disabled.Please contact your service provider",
                        ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

            if(userDeviceAccountsModel != null && userDeviceAccountsModel.getAccountLocked() == true)
                throw new CommandException("Your Account is Closed.Please contact your service provider",
                        ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
            sma.setCustomerId(appUserModel.getCustomerId());
            sma.setActive(Boolean.TRUE);
            sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);

            if(smartMoneyAccountModel != null && smartMoneyAccountModel.getStatusId() !=null && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_DORMANT))
                throw new CommandException("Your L1 account is marked as Dormant.Please contact your service provider.",
                        ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        }
        else
            loadAppUserByCNIC(this.cNic);

        if(appUserModel != null  && !appId.equals("2"))
        {
            loadAppUserByCNIC(this.cNic);
            if(appUserModel == null)
                throw new CommandException("No Customer exists against this CNIC.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        if(appUserModel.getCustomerId() == null)
        {
            throw new CommandException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        CustomerModel cModel=new CustomerModel();
        cModel.setCustomerId(appUserModel.getCustomerId());

        customerModel=commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());

        if(customerModel != null && !(customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)||customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)))//RegistrationStateConstantsInterface.VERIFIED
        {
            throw new CommandException(MessageUtil.getMessage("upgrade.customer.L1.account"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        if(appUserModel != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING) &&
                appUserModel.getAccountStateId().equals(AccountStateConstantsInterface.CLS_STATE_BLOCKED))){
            throw new CommandException(MessageUtil.getMessage("acc.pending.account"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
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
}

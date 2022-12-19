package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;

import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CheckCustomerExistanceAndAccountStatusCommand extends BaseCommand{
    protected final Log logger = LogFactory.getLog(CheckCustomerExistanceAndAccountStatusCommand.class);
    private String mobileNumber;
    private String cnic;
    private boolean dormant =false;
    private AppUserModel customerAppUserModel;
    private MessageSource messageSource;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CheckCustomerExistanceAndAccountStatusCommand.prepare()");
        this.mobileNumber = getCommandParameter(baseWrapper, "CMOB");
        this.cnic = getCommandParameter(baseWrapper, "CNIC");
        this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");
        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CheckCustomerExistanceAndAccountStatusCommand.prepare()");

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
       return new ValidationErrors();
    }

    @Override
    public void doValidate() throws CommandException {
          if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CheckCustomerExistanceAndAccountStatusCommand.doValidate()");

        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors = ValidatorWrapper.doRequired(mobileNumber, validationErrors, "MSISDN");
        validationErrors = ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
        boolean isValid = CommonUtils.isValidCnic(cnic);
        if (!validationErrors.hasValidationErrors()) {
            if (!isValid) {
                validationErrors.getStringBuilder().append("Invalid CNIC");
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        if (validationErrors.hasValidationErrors())
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());

        if (!validationErrors.hasValidationErrors()) {
            if (getCommonCommandManager().isCnicBlacklisted(cnic)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }


        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CheckCustomerExistanceAndAccountStatusCommand.doValidate()");
    }
    //Return cnic mobile number in case customer exists and account is dormant else throw error with account state
    @Override
    public void execute() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CheckCustomerExistanceAndAccountStatusCommand.execute()");
            ValidationErrors validationErrors = new ValidationErrors();
            messageSource = getMessageSource();
        try {
            customerAppUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(this.mobileNumber,UserTypeConstantsInterface.CUSTOMER);

            if(customerAppUserModel == null)
                throw new CommandException("No customer exists against this Mobile No.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            else if(!customerAppUserModel.getNic().equalsIgnoreCase(cnic)){
                throw new CommandException("No customer exists against the provided CNIC and Mobile No", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            UserDeviceAccountsModel uda = this.getCommonCommandManager().getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(customerAppUserModel.getAppUserId());
            if (uda.getAccountLocked()){
                throw new CommandException("Customer Account is Blocked", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            } else if (!uda.getAccountEnabled()){
                throw new CommandException("Customer Account is Deactived", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            Long stateId = customerAppUserModel.getAccountStateId();
            if(customerAppUserModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.DORMANT.longValue()){
                dormant = true;
            } else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)){
                dormant = true;
            }

            else if (customerAppUserModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.REJECTED.longValue() ||
                       customerAppUserModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.DECLINE.longValue() ||
                       customerAppUserModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.CLOSED.longValue() ||
                       customerAppUserModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.BLACKLISTED.longValue() ||
                       customerAppUserModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.DISCREPANT.longValue() ) {
                  logger.info("CheckCustomerExistanceAndAccountStatusCommand::execute():: Customer registration state ID is "+customerAppUserModel.getRegistrationStateId().longValue());
                  throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.RegistrationStateNotVerified", null, null));

            }

              else if(stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_HOT)){
                throw new CommandException("Account is in Hot State", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            } else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_WARM) || stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_COLD)){
                throw new CommandException("Account is already active", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }  else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DECEASED)){
                throw new CommandException("Account is in Deceased State", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }  else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED)){
                throw new CommandException("Account is in Closed State", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
        } catch (Exception e) {
            logger.error(e);
            throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
        }
        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CheckCustomerExistanceAndAccountStatusCommand.execute()");

    }

    @Override
    public String response() {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CheckCustomerExistanceAndAccountStatusCommand.response()");
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);
        if(dormant) {
            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_CNIC)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.cnic).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);


            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_CUSTOMER_MOBILE)
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.mobileNumber).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);
        }
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CheckCustomerExistanceAndAccountStatusCommand.response()");
        return strBuilder.toString();

    }
}

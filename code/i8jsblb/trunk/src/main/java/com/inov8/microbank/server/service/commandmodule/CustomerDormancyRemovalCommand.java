package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CustomerDormancyRemovalCommand extends  BaseCommand {
    protected final Log logger = LogFactory.getLog(CustomerDormancyRemovalCommand.class);
    private String mobileNumber;
    private String cnic;
    private boolean dormant =false;
    private AppUserModel customerAppUserModel;
    private MessageSource messageSource;
    private Boolean flag;
    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CustomerDormancyRemovalCommand.prepare()");
        this.mobileNumber = getCommandParameter(baseWrapper, "CMOB");
        this.cnic = getCommandParameter(baseWrapper, "CNIC");
        this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");
        if (this.logger.isDebugEnabled())
            this.logger.debug("End of CustomerDormancyRemovalCommand.prepare()");

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return new ValidationErrors();
    }


    @Override
    public void doValidate() throws CommandException{
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CustomerDormancyRemovalCommand.doValidate()");

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
            this.logger.debug("End of CustomerDormancyRemovalCommand.doValidate()");
    }

    @Override
    public void execute() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of CustomerDormancyRemovalCommand.execute()");
        try {
            flag = false;
            customerAppUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(this.mobileNumber,UserTypeConstantsInterface.CUSTOMER);
            if(customerAppUserModel == null)
                throw new CommandException("No customer exists against this Mobile No.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

            SmartMoneyAccountModel  smartMoneyAccountModel = loadSmartMoneyAccount(customerAppUserModel);
             if (smartMoneyAccountModel == null){
                 flag =false;
                 throw new CommandException("Unable to load the account at the moment",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
             }
             flag = this.getCommonCommandManager().removeCustomerDormancy(customerAppUserModel,smartMoneyAccountModel);
             if(!flag)
                  throw new CommandException("Unable to process your request at the moment",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        } catch (Exception e) {
            logger.error(e);
            flag = false;
            throw new CommandException("Unable to process your request at the moment",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
        }


    }

    private SmartMoneyAccountModel loadSmartMoneyAccount (AppUserModel customerAppUserModel) throws  CommandException{
        SmartMoneyAccountModel sma = null;
        Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
        sma = this.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(customerAppUserModel, paymentModeId);
        if(sma == null){
            sma = this.getCommonCommandManager().getInActiveSMA(customerAppUserModel,paymentModeId);
        }
        paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
        if(sma == null){
            sma = this.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(customerAppUserModel, paymentModeId);
        }
        if(sma == null){
            sma = this.getCommonCommandManager().getInActiveSMA(customerAppUserModel,paymentModeId);
        }
        return sma;
    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        String message ;
        if(flag) {
             message = "Your request for customer Dormancy Removal has been processed successfully.";
        }else{
            message = "Your request cannot be processed at the moment";
        }

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

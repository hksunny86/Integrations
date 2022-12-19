package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class ForgotMpinCommand extends BaseCommand {
    protected AppUserModel appUserModel;
    protected String cnic, mobileNo;
    private Boolean isError=false;

    final Log logger = LogFactory.getLog(getClass());

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        logger.info("Start of ForgotMpinCommand.prepare()");
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        cnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        try {
            appUserModel = this.commonCommandManager.loadAppUserByCnicAndMobileAndAppUserType(mobileNo, cnic,
                    UserTypeConstantsInterface.CUSTOMER);
            if(appUserModel!=null){
                ThreadLocalAppUser.setAppUserModel(appUserModel);
            }
            else{
                isError=true;
            }
        } catch (FrameworkCheckedException e) {
            logger.error("[ForgotMpinCommand.prepare] Unable to Load AppUserModel by cnic: " + cnic ,e);
            e.printStackTrace();
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
        if(isError)
            throw new CommandException("No Customer found against given CNIC",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        logger.info("[ForgotMpinCommand.prepare] ForgotMpinCommand.execute");

        String otp = CommonUtils.generateOneTimePin(5);
        logger.info("************************");
        logger.info("The plain otp is "+otp);
        logger.info("************************");
        String encryptedPin = EncoderUtils.encodeToSha(otp);
        try {
            this.getCommonCommandManager().getFonePayManager().createMiniTransactionModel(encryptedPin, mobileNo,"",CommandFieldConstants.CMD_OTP_VERIFICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Send OTP via SMS to Customer
        String smsText = MessageUtil.getMessage("otpSms", new String[]{"Reset Mpin", otp});
        SmsMessage smsMessage = new SmsMessage(mobileNo, smsText);
        logger.info("************************");
        logger.info("SMS Sent: "+smsText);
        logger.info("************************");
        try {
            this.getCommonCommandManager().getSmsSender().send(smsMessage);
        } catch (FrameworkCheckedException e) {

            e.printStackTrace();
            throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
        }
    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append("DTID")
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE);
        strBuilder.append("5");
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);

        return strBuilder.toString();
    }
}

package com.inov8.microbank.server.service.commandmodule;

import com.inov8.common.util.RandomUtils;
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
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class GenerateOTPCommand extends BaseCommand {

    protected AppUserModel appUserModel;
    protected String mobileNo;
    private Boolean isError=false;

    final Log logger = LogFactory.getLog(getClass());

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
//        encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        try {
            appUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(mobileNo, UserTypeConstantsInterface.CUSTOMER);
            if(appUserModel!=null){
                ThreadLocalAppUser.setAppUserModel(appUserModel);
            }
            else{
                isError=true;
            }
        }
        catch (Exception e){
            logger.error("[ForgotPinInfoCommand.prepare] Unable to Load AppUserModel by mobile: " + mobileNo ,e);

        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
        if(isError)
            throw new CommandException("No Customer found against given Mobile Number",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        logger.info("[ForgotPinInfoCommand.prepare] GenerateOTPCommand.execute");

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
        String smsText = MessageUtil.getMessage("otpSms", new String[]{"Reset Login Pin", otp});
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
        strBuilder.append("OTP has been Generated");
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

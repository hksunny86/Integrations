package com.inov8.microbank.fonepay.commands;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * Created by Attique on 7/10/2017.
 */
public class FonePayOTPVerificationCommand extends BaseCommand {
	AppUserModel appUserModel = null;
    private String response;
    private String pin;
    private String mobile_no;
    private int actionId = 0;
    private String cnic;
    private String requestDate;
    private String RRN;
    private String responseCode;
    private String responseMsg;
    private String channel;
    private Long commandId;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
        if(this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.APIGEE_CHANNEL)||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.NOVA_CHANNEL)||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.PAYFAST_CHANNEL)||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.VENDI_CHANNEL)
                ||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.PAYFAST_COMM_CHANNEL)
                ||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.PAYFAST_UBPS_CHANNEL)
                ||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.PAYFAST_WTOW_CHANNEL)
                ||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.BRANDVERSE_CHANNEL)
        ||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.ECOFIN_CHANNEL)
                ||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.TELEMART_CHANNEL)
        ||this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID).equals(FonePayConstants.RAHPER_CHANNEL)){
            pin=ThirdPartyEncryptionUtil.decryptWithAES(XMLConstants.THIRD_PARTY_ENCRYPTION_KEY,pin);
        }
        else {
            pin = EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, pin);
        }
        pin= EncoderUtils.encodeToSha(pin);
        mobile_no = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        String action = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACTION);
        cnic=getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        requestDate=getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACTION);
        RRN=getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACTION);
        actionId = action.equals("") ? 0 : Integer.parseInt(action);
        commandId=Long.valueOf(getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CURR_COMMAND_ID));
        channel="";
        //AppUserModel appUserModel=new AppUserModel();
        channel=getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID);
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        /*if(appUserModel == null){

        	appUserModel = new AppUserModel();
        	appUserModel.setAppUserId(PortalConstants.WEB_SERVICE_APP_USER_ID);
            ThreadLocalAppUser.setAppUserModel(appUserModel);
        }*/
        
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if(actionId == 0)
            ValidatorWrapper.doRequired(pin, validationErrors, "PIN");

        ValidatorWrapper.doRequired(mobile_no, validationErrors, "Mobile No");
        responseMsg=validationErrors.getErrors();
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {

        try{
            MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
            miniTransactionModel.setMobileNo(mobile_no);
            //miniTransactionModel.setAppUserId(appUserModel.getAppUserId());
            miniTransactionModel.setCommandId(commandId);
            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
            if(!channel.equals(null) && !"".equals(channel)){
                miniTransactionModel.setChannelId(channel);
            }
            switch (actionId) {
                case 0:
                    verifyOTP(miniTransactionModel);
                    break;

                case 1:
                    //resendOTP(miniTransactionModel);
                    throw new CommandException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_RESENT),  ErrorCodes.DEVICE_OTP_RESENT,ErrorLevel.MEDIUM,new Throwable());

                case 2:
                    //regenerateOTP(miniTransactionModel);
                    throw new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_REGENERATED), null, ErrorCodes.DEVICE_OTP_REGENERATED, null);
                default:
                    throw new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.DEVICE_INVALID_INPUT), null, ErrorCodes.DEVICE_INVALID_INPUT, null);
            }

        }
        catch (Exception ex) {
            handleException(ex);
        }
        

    }

    @Override
    public String response() {
    	StringBuilder response = new StringBuilder();
    	
    	 response.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE).
		 append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_DEVICE_TYPE_ID,this.deviceTypeId));
    	 response.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

    	return response.toString();

    }

    private void verifyOTP(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException {
        Long errorCode = getCommonCommandManager().getMiniTransactionManager().verifyOTP(miniTransactionModel, pin);
        if (errorCode == -1) {
            //update UDID and unlock the device
            responseCode="00";
            responseMsg="OTP Verified Succesfully";
        }

        // use custom message for device verification
        else {
            responseCode=errorCode.toString();
            if(errorCode==9023L){
                responseMsg=MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_INVALID);
            }
            else{
                responseMsg=MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_EXPIRED);

            }
            handleErrorCase(errorCode);
        }
    }


    private void handleErrorCase(Long errorCode) throws FrameworkCheckedException {
        if(ErrorCodes.OTP_EXPIRED.longValue() == errorCode)
            throw new CommandException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_EXPIRED),  ErrorCodes.DEVICE_OTP_EXPIRED, ErrorLevel.MEDIUM,new Throwable());


        else if(ErrorCodes.OTP_INVALID.longValue() == errorCode)
            throw new CommandException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_INVALID),  ErrorCodes.OTP_INVALID,ErrorLevel.MEDIUM,new Throwable() );
    }


}

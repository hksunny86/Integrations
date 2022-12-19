package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;

public class DeviceVerificationCommand extends BaseCommand {

    private String pin;
    private String udid;
    private String userType;
    private String userId;
    private int actionId = 0;  // 0 = verify, 1= resend, 2 = re-generate

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
        udid = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_UDID);
        userId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
        userType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_USER_TYPE);
        String action = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACTION);

        actionId = action.equals("") ? 0 : Integer.parseInt(action);
    }

    @Override
    public void doValidate() throws CommandException {
        ValidationErrors validationErrors = new ValidationErrors();

        validate(validationErrors);

        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }
    }

    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {

        if(actionId == 0)
            ValidatorWrapper.doRequired(pin, validationErrors, "PIN");

        ValidatorWrapper.doRequired(udid, validationErrors, "User Device ID");
        ValidatorWrapper.doRequired(userId, validationErrors, "Login Id");
        ValidatorWrapper.doRequired(userType, validationErrors, "User Type");

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        try {

            long userTypeIdKey = Long.parseLong(userType);
            AppUserModel appUserModel = null;
            if (UserTypeConstantsInterface.RETAILER == userTypeIdKey || UserTypeConstantsInterface.HANDLER == userTypeIdKey) {
                appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserModelByUserId(userId);
            }

            else if (UserTypeConstantsInterface.CUSTOMER == userTypeIdKey) {
                appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByQuery(userId, userTypeIdKey);
            }

            if(appUserModel == null) {
                throw new FrameworkCheckedException("Unable to find profile against given mobile no.");
            }

            MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
            miniTransactionModel.setMobileNo(appUserModel.getMobileNo());
            miniTransactionModel.setAppUserId(appUserModel.getAppUserId());
            miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_DEVICE_VERIFICATION));
            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);

            switch (actionId) {
                case 0:
                    verifyOTP(miniTransactionModel);
                    break;

                case 1:
                    resendOTP(miniTransactionModel);
                    throw new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_RESENT), null, ErrorCodes.DEVICE_OTP_RESENT, null);

                case 2:
                    regenerateOTP(miniTransactionModel);
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
        return MiniXMLUtil.createMessageXML(MessageUtil.getMessage("device.UDIDUpdated"));
    }

    private void verifyOTP(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException {
        Long errorCode = getCommonCommandManager().getMiniTransactionManager().verifyOTP(miniTransactionModel, pin);
        if (errorCode == -1) {
            //update UDID and unlock the device
        	getCommonCommandManager().getDemographicsManager().unlockDevice(miniTransactionModel.getMobileNo(), udid);
        }

        // use custom message for device verification
        else
            handleErrorCase(errorCode);
    }

    private void resendOTP(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException {
        Long errorCode = getCommonCommandManager().getMiniTransactionManager().resendOTP(miniTransactionModel, "otpSms", "Device Authorization");

        // use custom message for device verification
        if (errorCode != -1) {
            handleErrorCase(errorCode);
        }
    }

    private void regenerateOTP(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException {
    	getCommonCommandManager().getMiniTransactionManager().generateOTP(miniTransactionModel, "otpSms", "Device Authorization");
    }

    private void handleErrorCase(Long errorCode) throws FrameworkCheckedException {
        if(ErrorCodes.OTP_EXPIRED.longValue() == errorCode)
            throw new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_EXPIRED), null, ErrorCodes.DEVICE_OTP_EXPIRED, null);

        else if(ErrorCodes.OTP_INVALID.longValue() == errorCode)
            throw new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_INVALID), null, ErrorCodes.DEVICE_OTP_INVALID, null);
    }
}
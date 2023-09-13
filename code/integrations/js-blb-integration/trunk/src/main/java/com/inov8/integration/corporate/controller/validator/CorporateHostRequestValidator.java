package com.inov8.integration.corporate.controller.validator;

import com.inov8.integration.corporate.pdu.request.*;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import org.apache.commons.lang.StringUtils;

public class CorporateHostRequestValidator {

    public static void validateLogin(LoginRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getPortalId())) {
            throw new ValidationException("[FAILED] Validation Failed Portal Id: " + integrationVO.getPortalId());
        }
        if (StringUtils.isEmpty(integrationVO.getPortalPassword())) {
            throw new ValidationException("[FAILED] Validation Failed Portal Password: " + integrationVO.getPortalPassword());
        }

    }

    public static void validateAccountStateInquiry(AccountStateInquiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Cnic: " + integrationVO.getCnicNumber());
        }

    }

    public static void validateAccountState(AccountStateRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Cnic: " + integrationVO.getCnicNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getOtp())) {
            throw new ValidationException("[FAILED] Validation Failed OTP: " + integrationVO.getOtp());
        }

    }

    public static void validateMpinResetInquiry(MpinResetInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
    }

    public static void validateMpinReset(MpinResetRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getOldMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Old Mpin: " + integrationVO.getOldMpin());
        }
        if (StringUtils.isEmpty(integrationVO.getNewMpin())) {
            throw new ValidationException("[FAILED] Validation Failed New Mpin: " + integrationVO.getNewMpin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + integrationVO.getConfirmMpin());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getOtp())) {
            throw new ValidationException("[FAILED] Validation Failed OTP: " + integrationVO.getOtp());
        }
    }

    public static void validateAccountStatementInquiry(AccountStatementInquiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateAccountStatement(AccountStatementRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getOtp())) {
            throw new ValidationException("[FAILED] Validation Failed OTP: " + integrationVO.getOtp());
        }
        if (StringUtils.isEmpty(integrationVO.getFromDate())) {
            throw new ValidationException("[FAILED] Validation Failed From Date: " + integrationVO.getFromDate());
        }
        if (StringUtils.isEmpty(integrationVO.getToDate())) {
            throw new ValidationException("[FAILED] Validation Failed To Date: " + integrationVO.getToDate());
        }
    }

    public static void validateDeviceVerificationInquiry(DeviceVerificationInquiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateDeviceVerification(DeviceVerificationRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getOtp())) {
            throw new ValidationException("[FAILED] Validation Failed OTP: " + integrationVO.getOtp());
        }
    }

    public static void validateTitleFetch(CorporateTitleFetchRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static boolean authenticate(String userName, String password, String channelID) {
        if (ConfigReader.getInstance().getProperty("channel.ids", "").contains(channelID))
            return ConfigReader.getInstance().getProperty("channel.usernames", "").contains(userName) && ConfigReader.getInstance().getProperty("channel.passwords", "").contains(password);
        return Boolean.FALSE;
    }
}

package com.inov8.integration.l2Account.controller.validator;

import com.inov8.integration.l2Account.pdu.request.*;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import org.apache.commons.lang.StringUtils;

public class L2AccountHostRequestValidator {

    public static void validateL2Account(L2AccountRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (!StringUtils.isEmpty(integrationVO.getRrn())) {
            String rrn = integrationVO.getRrn();
            if (rrn.length() > 20) {
                throw new ValidationException("[FAILED] RRN should not be more than 20 characters: " + integrationVO.getRrn());
            }
            // Check if all characters are alphanumeric
            for (char c : rrn.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new ValidationException("[FAILED] RRN should not include special characters: " + integrationVO.getRrn());
                }
            }
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (!StringUtils.isEmpty(integrationVO.getDateTime())) {
            String dateTime = integrationVO.getDateTime();
            if (dateTime.length() < 14) {
                throw new ValidationException("[FAILED] Incorrect Date Time: " + integrationVO.getDateTime());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }
    }

    public static void validateL2AccountFields(L2AccountFieldsRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (!StringUtils.isEmpty(integrationVO.getRrn())) {
            String rrn = integrationVO.getRrn();
            if (rrn.length() > 20) {
                throw new ValidationException("[FAILED] RRN should not be more than 20 characters: " + integrationVO.getRrn());
            }
            // Check if all characters are alphanumeric
            for (char c : rrn.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new ValidationException("[FAILED] RRN should not include special characters: " + integrationVO.getRrn());
                }
            }
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (!StringUtils.isEmpty(integrationVO.getDateTime())) {
            String dateTime = integrationVO.getDateTime();
            if (dateTime.length() < 14) {
                throw new ValidationException("[FAILED] Incorrect Date Time: " + integrationVO.getDateTime());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
    }

    public static void validateUpdatePmd(UpdatePmdRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (!StringUtils.isEmpty(integrationVO.getRrn())) {
            String rrn = integrationVO.getRrn();
            if (rrn.length() > 20) {
                throw new ValidationException("[FAILED] RRN should not be more than 20 characters: " + integrationVO.getRrn());
            }
            // Check if all characters are alphanumeric
            for (char c : rrn.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new ValidationException("[FAILED] RRN should not include special characters: " + integrationVO.getRrn());
                }
            }
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (!StringUtils.isEmpty(integrationVO.getDateTime())) {
            String dateTime = integrationVO.getDateTime();
            if (dateTime.length() < 14) {
                throw new ValidationException("[FAILED] Incorrect Date Time: " + integrationVO.getDateTime());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile No: " + integrationVO.getMobileNumber());
        }
    }

    public static void validateRateConversion(RateConversionRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (!StringUtils.isEmpty(integrationVO.getRrn())) {
            String rrn = integrationVO.getRrn();
            if (rrn.length() > 20) {
                throw new ValidationException("[FAILED] RRN should not be more than 20 characters: " + integrationVO.getRrn());
            }
            // Check if all characters are alphanumeric
            for (char c : rrn.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new ValidationException("[FAILED] RRN should not include special characters: " + integrationVO.getRrn());
                }
            }
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (!StringUtils.isEmpty(integrationVO.getDateTime())) {
            String dateTime = integrationVO.getDateTime();
            if (dateTime.length() < 14) {
                throw new ValidationException("[FAILED] Incorrect Date Time: " + integrationVO.getDateTime());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }

    }

    public static void validateFreelanceToWalletInquiry(FreelanceToWalletInquiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (!StringUtils.isEmpty(integrationVO.getRrn())) {
            String rrn = integrationVO.getRrn();
            if (rrn.length() > 20) {
                throw new ValidationException("[FAILED] RRN should not be more than 20 characters: " + integrationVO.getRrn());
            }
            // Check if all characters are alphanumeric
            for (char c : rrn.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new ValidationException("[FAILED] RRN should not include special characters: " + integrationVO.getRrn());
                }
            }
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (!StringUtils.isEmpty(integrationVO.getDateTime())) {
            String dateTime = integrationVO.getDateTime();
            if (dateTime.length() < 14) {
                throw new ValidationException("[FAILED] Incorrect Date Time: " + integrationVO.getDateTime());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }
        if (!StringUtils.isEmpty(integrationVO.getAmount())) {
            String amount = integrationVO.getAmount();
            if (amount.length() > 15) {
                throw new ValidationException("[FAILED] Amount should not be more than 15 characters: " + integrationVO.getRrn());
            }
        }

    }

    public static void validateFreelanceToWallet(FreelanceToWalletRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (!StringUtils.isEmpty(integrationVO.getRrn())) {
            String rrn = integrationVO.getRrn();
            if (rrn.length() > 20) {
                throw new ValidationException("[FAILED] RRN should not be more than 20 characters: " + integrationVO.getRrn());
            }
            // Check if all characters are alphanumeric
            for (char c : rrn.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new ValidationException("[FAILED] RRN should not include special characters: " + integrationVO.getRrn());
                }
            }
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (!StringUtils.isEmpty(integrationVO.getDateTime())) {
            String dateTime = integrationVO.getDateTime();
            if (dateTime.length() < 14) {
                throw new ValidationException("[FAILED] Incorrect Date Time: " + integrationVO.getDateTime());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }
        if (!StringUtils.isEmpty(integrationVO.getAmount())) {
            String amount = integrationVO.getAmount();
            if (amount.length() > 15) {
                throw new ValidationException("[FAILED] Amount should not be more than 15 characters: " + integrationVO.getRrn());
            }
        }

    }

    public static void validateFreelanceBalanceInquiry(FreelanceBalanceInquiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (!StringUtils.isEmpty(integrationVO.getRrn())) {
            String rrn = integrationVO.getRrn();
            if (rrn.length() > 20) {
                throw new ValidationException("[FAILED] RRN should not be more than 20 characters: " + integrationVO.getRrn());
            }
            // Check if all characters are alphanumeric
            for (char c : rrn.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new ValidationException("[FAILED] RRN should not include special characters: " + integrationVO.getRrn());
                }
            }
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (!StringUtils.isEmpty(integrationVO.getDateTime())) {
            String dateTime = integrationVO.getDateTime();
            if (dateTime.length() < 14) {
                throw new ValidationException("[FAILED] Incorrect Date Time: " + integrationVO.getDateTime());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
    }


    public static boolean authenticate(String userName, String password, String channelID) {
        if (ConfigReader.getInstance().getProperty("channel.ids", "").contains(channelID))
            return ConfigReader.getInstance().getProperty("channel.usernames", "").contains(userName) && ConfigReader.getInstance().getProperty("channel.passwords", "").contains(password);
        return Boolean.FALSE;
    }

//    public static boolean validateRRN(String rrn) {
//        // Check if the length is exactly 20
//        if (rrn.length() != 20) {
//            return false;
//        }
//
//        // Check if all characters are alphanumeric
//        for (char c : rrn.toCharArray()) {
//            if (!Character.isLetterOrDigit(c)) {
//                return false;
//            }
//        }
//
//        // If it passes both checks, it's valid
//        return true;
//    }
//
//    public static void main(String[] args) {
//
//        String validRRN = "A1B2C3D4E5F6G7H8I9J0";
//        String invalidRRN1 = "123456789901'and'1'='2'--";   // Only 19 characters
//        String invalidRRN2 = "A1B2C3D4E5F6G7H8I9J0!"; // Contains special character '!'
//        String invalidRRN3 = "123456789901'and'1'='1'--"; // More than 20 characters
//
//        System.out.println("Valid RRN: " + validateRRN(validRRN)); // Should print: true
//        System.out.println("Invalid RRN 1: " + validateRRN(invalidRRN1)); // Should print: false
//        System.out.println("Invalid RRN 2: " + validateRRN(invalidRRN2)); // Should print: false
//        System.out.println("Invalid RRN 3: " + validateRRN(invalidRRN3)); // Should print: false
//    }
}

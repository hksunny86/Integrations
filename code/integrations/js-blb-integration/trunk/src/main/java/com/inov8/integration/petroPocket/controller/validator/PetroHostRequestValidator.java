package com.inov8.integration.petroPocket.controller.validator;

import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.petroPocket.pdu.request.PetroBalanceInquiryRequest;
import com.inov8.integration.petroPocket.pdu.request.*;
import org.apache.commons.lang.StringUtils;

public class PetroHostRequestValidator {

    public static void validatePetroInquiry(PetroInquiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
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
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Amount : " + integrationVO.getAmount());
        }
        if (!StringUtils.isEmpty(integrationVO.getAmount())) {
            String amount = integrationVO.getAmount();
            if (amount.length() > 15) {
                throw new ValidationException("[FAILED] Amount should not be more than 15 characters: " + integrationVO.getRrn());
            }
        }

    }

    public static void validatePetroPayment(PetroPaymentRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
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
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Amount : " + integrationVO.getAmount());
        }
        if (!StringUtils.isEmpty(integrationVO.getAmount())) {
            String amount = integrationVO.getAmount();
            if (amount.length() > 15) {
                throw new ValidationException("[FAILED] Amount should not be more than 15 characters: " + integrationVO.getRrn());
            }
        }

    }

    public static void validatePetroWalletToWalletPayment(PetroWalletToWalletPaymentRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
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
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Amount : " + integrationVO.getAmount());
        }
        if (!StringUtils.isEmpty(integrationVO.getAmount())) {
            String amount = integrationVO.getAmount();
            if (amount.length() > 15) {
                throw new ValidationException("[FAILED] Amount should not be more than 15 characters: " + integrationVO.getRrn());
            }
        }


    }

    public static void validatePetroWalletToWalletInquiryPayment(PetroWalletToWalletInquiryRequest walletToWalletPaymentInquiryRequest) throws ValidationException {
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + walletToWalletPaymentInquiryRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getDateTime());
        }
        if (!StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getDateTime())) {
            String dateTime = walletToWalletPaymentInquiryRequest.getDateTime();
            if (dateTime.length() < 14) {
                throw new ValidationException("[FAILED] Incorrect Date Time: " + walletToWalletPaymentInquiryRequest.getDateTime());
            }
        }
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Retrieval Reffrence Number for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getRrn());
        }
        if (!StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getRrn())) {
            String rrn = walletToWalletPaymentInquiryRequest.getRrn();
            if (rrn.length() > 20) {
                throw new ValidationException("[FAILED] RRN should not be more than 20 characters: " + walletToWalletPaymentInquiryRequest.getRrn());
            }
            // Check if all characters are alphanumeric
            for (char c : rrn.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new ValidationException("[FAILED] RRN should not include special characters: " + walletToWalletPaymentInquiryRequest.getRrn());
                }
            }
        }
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getChannelId());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getTerminalId());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getReceiverMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Receiver mobile number for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getReceiverMobileNumber());
        }

        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed amount for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getAmount());
        }
        if (!StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getAmount())) {
            String amount = walletToWalletPaymentInquiryRequest.getAmount();
            if (amount.length() > 15) {
                throw new ValidationException("[FAILED] Amount should not be more than 15 characters: " + walletToWalletPaymentInquiryRequest.getRrn());
            }
        }
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1 for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getReserved1());
        }


    }

    public static void validateWalletToPetroInquiry(WalletToPetroInquiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
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
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Amount : " + integrationVO.getAmount());
        }
        if (!StringUtils.isEmpty(integrationVO.getAmount())) {
            String amount = integrationVO.getAmount();
            if (amount.length() > 15) {
                throw new ValidationException("[FAILED] Amount should not be more than 15 characters: " + integrationVO.getRrn());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }

    }

    public static void validateWalletToPetroPayment(WalletToPetroPaymentRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
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
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Amount : " + integrationVO.getAmount());
        }
        if (!StringUtils.isEmpty(integrationVO.getAmount())) {
            String amount = integrationVO.getAmount();
            if (amount.length() > 15) {
                throw new ValidationException("[FAILED] Amount should not be more than 15 characters: " + integrationVO.getRrn());
            }
        }

    }

    public static void validatePetroBalanceInquiry(PetroBalanceInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
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

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1 : " + integrationVO.getReserved1());
        }

    }

    public static boolean authenticate(String userName, String password, String channelID) {
        if (ConfigReader.getInstance().getProperty("channel.ids", "").contains(channelID))
            return ConfigReader.getInstance().getProperty("channel.usernames", "").contains(userName) && ConfigReader.getInstance().getProperty("channel.passwords", "").contains(password);
        return Boolean.FALSE;
    }
}

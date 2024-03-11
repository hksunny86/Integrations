package com.inov8.integration.walletToWallet.controller.validator;

import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.walletToWallet.pdu.request.ZToZPaymentInquiryRequest;
import com.inov8.integration.walletToWallet.pdu.request.ZToZPaymentRequest;
import org.apache.commons.lang.StringUtils;

public class ZtoZHostRequestValidator {

    public static void validateZtoZPaymentInquiry(ZToZPaymentInquiryRequest walletToWalletPaymentInquiryRequest) {

        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + walletToWalletPaymentInquiryRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getDateTime());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Retrieval Reffrence Number for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getRrn());
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
        if (StringUtils.isEmpty(walletToWalletPaymentInquiryRequest.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1 for wallet to wallet: " + walletToWalletPaymentInquiryRequest.getReserved1());
        }
    }

    public static void validateZtoZPayment(ZToZPaymentRequest walletToWalletPaymentRequest) {

        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + walletToWalletPaymentRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed date time " + walletToWalletPaymentRequest.getDateTime());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + walletToWalletPaymentRequest.getRrn());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + walletToWalletPaymentRequest.getChannelId());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + walletToWalletPaymentRequest.getTerminalId());
        }
//        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getOtp())) {
//            throw new ValidationException("[FAILED] Validation Failed OTP: " + walletToWalletPaymentRequest.getOtp());
//        }
        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getReceiverMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number: " + walletToWalletPaymentRequest.getReceiverMobileNumber());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + walletToWalletPaymentRequest.getAmount());
        }
        if (StringUtils.isEmpty(walletToWalletPaymentRequest.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1: " + walletToWalletPaymentRequest.getReserved1());
        }
    }


    public static boolean authenticate(String userName, String password, String channelID) {
        if (ConfigReader.getInstance().getProperty("channel.ids", "").contains(channelID))
            return ConfigReader.getInstance().getProperty("channel.usernames", "").contains(userName) && ConfigReader.getInstance().getProperty("channel.passwords", "").contains(password);
        return Boolean.FALSE;
    }
}

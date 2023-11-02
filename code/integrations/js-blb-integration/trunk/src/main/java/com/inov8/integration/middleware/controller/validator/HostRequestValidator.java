package com.inov8.integration.middleware.controller.validator;

import com.inov8.integration.corporate.pdu.request.AccountStateRequest;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.util.ConfigReader;
import org.apache.commons.lang.StringUtils;

public class HostRequestValidator {


    public static void validateAccountOpening(AccountOpeningRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }
        if (StringUtils.isEmpty(integrationVO.getAccountTitle())) {
            throw new ValidationException("[FAILED] Validation Failed Account Title: " + integrationVO.getAccountTitle());
        }

        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
        }
        if (StringUtils.isEmpty(integrationVO.getPresentAddress())) {
            throw new ValidationException("[FAILED] Validation Failed Present Address: " + integrationVO.getPresentAddress());
        }

        if (StringUtils.isEmpty(integrationVO.getCnicStatus())) {
            throw new ValidationException("[FAILED] Validation Failed Cnic Status: " + integrationVO.getCnicStatus());
        }

//        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
//        }
        if (StringUtils.isEmpty(integrationVO.getDob())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDob());
        }

//        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
//            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
//            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getGender())) {
//            throw new ValidationException("[FAILED] Validation Failed Gender: " + integrationVO.getGender());
//        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getAccountType())) {
            throw new ValidationException("[FAILED] Validation Failed Account Type: " + integrationVO.getAccountType());
        }

    }

    public static void validateVerifyAccount(VerifyAccountRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed getTransactionType: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateVerifyLoginAccount(VerifyLoginAccountRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateM0VerifyAccount(M0VerifyAccountRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed getTransactionType: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateChequeBook(ChequeBookRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getOrignalTransactionRRN())) {
            throw new ValidationException("[FAILED] Validation Failed Orignal Transaction RRN: " + integrationVO.getOrignalTransactionRRN());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateConventionalAccountOpening(ConventionalAccountOpening integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
        }
        if (StringUtils.isEmpty(integrationVO.getDob())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDob());
        }
        /*if (StringUtils.isEmpty(integrationVO.getCustomerPhoto())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Photo: " + integrationVO.getCustomerPhoto());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicFrontPhoto())) {
            throw new ValidationException("[FAILED] Validation Failed cnic Front Photo: " + integrationVO.getCnicFrontPhoto());
        }*/
//        if (StringUtils.isEmpty(integrationVO.getCnicBackPhoto())) {
//            throw new ValidationException("[FAILED] Validation Failed cnic Back Photo: " + integrationVO.getCnicBackPhoto());
//        }

      /*  if (StringUtils.isEmpty(integrationVO.getTermsPhoto())) {
            throw new ValidationException("[FAILED] Validation Failed Terms Photo: " + integrationVO.getTermsPhoto());
        }*/
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getAccountType())) {
            throw new ValidationException("[FAILED] Validation Failed Account Type: " + integrationVO.getAccountType());
        }
    }

    public static void validatePaymentInquiry(PaymentInquiryRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed getSecurityCode: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validatePaymentRequest(PaymentRequest integrationVO) {
        if (StringUtils.isEmpty(integrationVO.getAccountNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Account Number: " + integrationVO.getAccountNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getAmount());
        }
        if (StringUtils.isEmpty(integrationVO.getCharges())) {
            throw new ValidationException("[FAILED] Validation Failed Charges: " + integrationVO.getCharges());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Type: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getPaymentType())) {
            throw new ValidationException("[FAILED] Validation Failed Payment Type: " + integrationVO.getPaymentType());
        }

//        if (!integrationVO.getPaymentType().equals(ConstantEnums.SETTLEMENT_TYPE.getValue())) {
//            if (StringUtils.isEmpty(integrationVO.getMpin())||StringUtils.isEmpty(integrationVO.getOtp())) {
//                throw new ValidationException("[FAILED] Validation Failed MPIN And OTP:");
//            }
//        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionCode())) {
            throw new ValidationException("[FAILED] Validation Failed getTransactionCode: " + integrationVO.getTransactionCode());
        }

    }

    public static void validatePaymentReversalRequest(PaymentReversalRequest integrationVO) {

        /*if (StringUtils.isEmpty(integrationVO.getTransactionCode())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Code: " + integrationVO.getTransactionCode());
        }*/

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed getRrn: " + integrationVO.getRrn());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateOtpRequest(OtpVerificationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getOtpPin())) {
            throw new ValidationException("[FAILED] Validation Failed OTP Pin: " + integrationVO.getOtpPin());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC No: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateCardTagging(CardTagging integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getCardExpiry())) {
            throw new ValidationException("[FAILED] Validation Failed Card Expiry: " + integrationVO.getCardExpiry());
        }
        if (StringUtils.isEmpty(integrationVO.getCardNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Card Number: " + integrationVO.getCardNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getFirstName())) {
            throw new ValidationException("[FAILED] Validation Failed First Name: " + integrationVO.getFirstName());
        }

        if (StringUtils.isEmpty(integrationVO.getLastName())) {
            throw new ValidationException("[FAILED] Validation Failed Last Name: " + integrationVO.getLastName());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionId())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Id: " + integrationVO.getTransactionId());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateAccountLinkDeLink(AccountLinkDeLink integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Type: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC No: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateSetCardStatus(SetCardStatus integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Type: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC No: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getCardNo())) {
            throw new ValidationException("[FAILED] Validation Failed Card No: " + integrationVO.getCardNo());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateGenerateOTP(GenerateOtpRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getOtpPurpose())) {
            throw new ValidationException("[FAILED] Validation Failed OTP Purpose: " + integrationVO.getOtpPurpose());
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
    }

    public static void validateBalanceInquiry(BalanceInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
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
        if (StringUtils.isEmpty(integrationVO.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1 : " + integrationVO.getReserved1());
        }

    }

    public static void validateMiniStatement(MiniStatementRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1 : " + integrationVO.getReserved1());
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
        if (StringUtils.isEmpty(integrationVO.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1: " + integrationVO.getReserved1());
        }

    }

    public static void validateBillPaymentInquiry(BillPaymentInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Failed Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerNo())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer No: " + integrationVO.getConsumerNo());
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
        if (StringUtils.isEmpty(integrationVO.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1 : " + integrationVO.getReserved1());
        }
    }

    public static void validateBillPayment(BillPaymentRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Failed Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerNo())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer No: " + integrationVO.getConsumerNo());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
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
        if (StringUtils.isEmpty(integrationVO.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1 : " + integrationVO.getReserved1());
        }

    }

    public static void cashInInquiry(CashInInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
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
    }

    public static void cashIn(CashInRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
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
    }

    public static void TitleFetch(TitleFetchRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
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

    public static void cashInAgent(CashInAgentRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
    }

    public static void cashOutInquiry(CashOutInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
    }

    public static void cashOut(CashOutRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactiondateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getTransactiondateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1: " + integrationVO.getReserved1());
        }
    }

    public static void validateMpinRegistration(MpinRegistrationRequest mpinRegistrationRequest) {

        if (StringUtils.isEmpty(mpinRegistrationRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + mpinRegistrationRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(mpinRegistrationRequest.getMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Mpin: " + mpinRegistrationRequest.getMpin());
        }
        if (StringUtils.isEmpty(mpinRegistrationRequest.getConfirmMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + mpinRegistrationRequest.getConfirmMpin());
        }
        if (StringUtils.isEmpty(mpinRegistrationRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + mpinRegistrationRequest.getDateTime());
        }

        if (StringUtils.isEmpty(mpinRegistrationRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + mpinRegistrationRequest.getTerminalId());
        }
        if (StringUtils.isEmpty(mpinRegistrationRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + mpinRegistrationRequest.getChannelId());
        }
        if (StringUtils.isEmpty(mpinRegistrationRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + mpinRegistrationRequest.getRrn());
        }
    }

    public static void validateMpinChange(MpinChangeRequest mpinChangeRequest) {

        if (StringUtils.isEmpty(mpinChangeRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + mpinChangeRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(mpinChangeRequest.getOldMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Old Mpin: " + mpinChangeRequest.getOldMpin());
        }
        if (StringUtils.isEmpty(mpinChangeRequest.getNewMpin())) {
            throw new ValidationException("[FAILED] Validation Failed New Mpin: " + mpinChangeRequest.getNewMpin());
        }
        if (StringUtils.isEmpty(mpinChangeRequest.getConfirmMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + mpinChangeRequest.getConfirmMpin());
        }
        if (StringUtils.isEmpty(mpinChangeRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + mpinChangeRequest.getDateTime());
        }

        if (StringUtils.isEmpty(mpinChangeRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + mpinChangeRequest.getTerminalId());
        }
        if (StringUtils.isEmpty(mpinChangeRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + mpinChangeRequest.getChannelId());
        }
        if (StringUtils.isEmpty(mpinChangeRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + mpinChangeRequest.getRrn());
        }
    }

    public static void validateMpinUpgadeAccount(UpgradeAccountRequest upgradeAccountRequest) {

        if (StringUtils.isEmpty(upgradeAccountRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + upgradeAccountRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(upgradeAccountRequest.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed Old Mpin: " + upgradeAccountRequest.getCnic());
        }
//        if (StringUtils.isEmpty(upgradeAccountRequest.getFingerIndex())) {
//            throw new ValidationException("[FAILED] Validation Failed New Mpin: " + upgradeAccountRequest.getFingerIndex());
//        }
//        if (StringUtils.isEmpty(upgradeAccountRequest.getFingerTemplate())) {
//            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + upgradeAccountRequest.getFingerTemplate());
//        }
//        if (StringUtils.isEmpty(upgradeAccountRequest.getTemplateType())) {
//            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + upgradeAccountRequest.getTemplateType());
//        }
        if (StringUtils.isEmpty(upgradeAccountRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + upgradeAccountRequest.getDateTime());
        }

        if (StringUtils.isEmpty(upgradeAccountRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + upgradeAccountRequest.getTerminalId());
        }
        if (StringUtils.isEmpty(upgradeAccountRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + upgradeAccountRequest.getChannelId());
        }
        if (StringUtils.isEmpty(upgradeAccountRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + upgradeAccountRequest.getRrn());
        }

        if (!(upgradeAccountRequest.getReserved2().equals("1"))) {
            if (StringUtils.isEmpty(upgradeAccountRequest.getFingerIndex())) {
                throw new ValidationException("[FAILED] Validation Failed New Mpin: " + upgradeAccountRequest.getFingerIndex());
            }
        }
        if (!(upgradeAccountRequest.getReserved2().equals("1"))) {

            if (StringUtils.isEmpty(upgradeAccountRequest.getFingerTemplate())) {
                throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + upgradeAccountRequest.getFingerTemplate());
            }
        }
        if (!(upgradeAccountRequest.getReserved2().equals("1"))) {

            if (StringUtils.isEmpty(upgradeAccountRequest.getTemplateType())) {
                throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + upgradeAccountRequest.getTemplateType());
            }
        }
    }

    public static void validateM0UpgadeAccount(UpgradeMinorAccountRequest upgradeAccountRequest) {

        if (StringUtils.isEmpty(upgradeAccountRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + upgradeAccountRequest.getMobileNumber());
        }

        if (StringUtils.isEmpty(upgradeAccountRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + upgradeAccountRequest.getDateTime());
        }

        if (StringUtils.isEmpty(upgradeAccountRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + upgradeAccountRequest.getTerminalId());
        }
        if (StringUtils.isEmpty(upgradeAccountRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + upgradeAccountRequest.getChannelId());
        }
        if (StringUtils.isEmpty(upgradeAccountRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + upgradeAccountRequest.getRrn());
        }


    }

    public static void validateMinorFatherBVSVerfication(FatherBvsVerification fatherBvsVerification) {

        if (StringUtils.isEmpty(fatherBvsVerification.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + fatherBvsVerification.getMobileNumber());
        }

        if (StringUtils.isEmpty(fatherBvsVerification.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + fatherBvsVerification.getDateTime());
        }

        if (StringUtils.isEmpty(fatherBvsVerification.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + fatherBvsVerification.getTerminalId());
        }
        if (StringUtils.isEmpty(fatherBvsVerification.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + fatherBvsVerification.getChannelId());
        }
        if (StringUtils.isEmpty(fatherBvsVerification.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + fatherBvsVerification.getRrn());
        }


    }

    public static void validateWalletToWalletPaymentInquiry(WalletToWalletPaymentInquiryRequest walletToWalletPaymentInquiryRequest) {

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

    public static void validateWalletToWalletPayment(WalletToWalletPaymentRequest walletToWalletPaymentRequest) {

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

    public static void validateAccountStatusChange(AccountStatusChangeRequest accountStatusChangeRequest) {

        if (StringUtils.isEmpty(accountStatusChangeRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + accountStatusChangeRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(accountStatusChangeRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed date time " + accountStatusChangeRequest.getDateTime());
        }
        if (StringUtils.isEmpty(accountStatusChangeRequest.getAccountStatus())) {
            throw new ValidationException("[FAILED] Validation Failed date time " + accountStatusChangeRequest.getAccountStatus());
        }
        if (StringUtils.isEmpty(accountStatusChangeRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + accountStatusChangeRequest.getRrn());
        }
        if (StringUtils.isEmpty(accountStatusChangeRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + accountStatusChangeRequest.getChannelId());
        }
        if (StringUtils.isEmpty(accountStatusChangeRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + accountStatusChangeRequest.getTerminalId());
        }

    }

    public static void validateIbftTitleFetch(IbftTitleFetchRequest ibftTitleFetchRequest) {

        if (StringUtils.isEmpty(ibftTitleFetchRequest.getSenderMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed  Sender Mobile Number: " + ibftTitleFetchRequest.getSenderMobileNumber());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed date time " + ibftTitleFetchRequest.getDateTime());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getDestinationAccount())) {
            throw new ValidationException("[FAILED] Validation Failed Destination Account Number " + ibftTitleFetchRequest.getDestinationAccount());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getSourceBankImd())) {
            throw new ValidationException("[FAILED] Validation Failed Source Bank Imd " + ibftTitleFetchRequest.getSourceBankImd());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + ibftTitleFetchRequest.getRrn());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + ibftTitleFetchRequest.getChannelId());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + ibftTitleFetchRequest.getTerminalId());
        }

    }

    public static void validateIbftAdvice(IbftAdviceRequest ibftTitleFetchRequest) {

        if (StringUtils.isEmpty(ibftTitleFetchRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed date time " + ibftTitleFetchRequest.getDateTime());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getDestinationAccount())) {
            throw new ValidationException("[FAILED] Validation Failed Destination Account Number " + ibftTitleFetchRequest.getDestinationAccount());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getSourceBankImd())) {
            throw new ValidationException("[FAILED] Validation Failed Source Bank Imd " + ibftTitleFetchRequest.getSourceBankImd());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + ibftTitleFetchRequest.getRrn());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + ibftTitleFetchRequest.getChannelId());
        }
        if (StringUtils.isEmpty(ibftTitleFetchRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + ibftTitleFetchRequest.getTerminalId());
        }

    }

    public static void validateUpgradeAccountInquiry(UpgradeAccountInquiryRequest upgradeAccountInquiryRequest) {

        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + upgradeAccountInquiryRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed date time " + upgradeAccountInquiryRequest.getDateTime());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + upgradeAccountInquiryRequest.getRrn());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + upgradeAccountInquiryRequest.getChannelId());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + upgradeAccountInquiryRequest.getTerminalId());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1: " + upgradeAccountInquiryRequest.getReserved1());
        }
    }

    public static void validateMinorUpgradeAccountInquiry(UpgradeMinorAccountInquiryRequest upgradeAccountInquiryRequest) {

        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + upgradeAccountInquiryRequest.getMobileNumber());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed date time " + upgradeAccountInquiryRequest.getDateTime());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + upgradeAccountInquiryRequest.getRrn());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + upgradeAccountInquiryRequest.getChannelId());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + upgradeAccountInquiryRequest.getTerminalId());
        }
        if (StringUtils.isEmpty(upgradeAccountInquiryRequest.getReserved1())) {
            throw new ValidationException("[FAILED] Validation Failed Reserved1: " + upgradeAccountInquiryRequest.getReserved1());
        }
    }

    public static void walletToCnic(WalletToCnicRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number: " + integrationVO.getReceiverMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getReceiverCnic())) {
            throw new ValidationException("[FAILED] Validation Failed Receiver Cnic Type: " + integrationVO.getReceiverCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }
        if (StringUtils.isEmpty(integrationVO.getPaymentPurpose())) {
            throw new ValidationException("[FAILED] Validation Failed Payment  Purpose: " + integrationVO.getPaymentPurpose());
        }
    }

    public static void walletToCnicInquiry(WalletToCnicInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number: " + integrationVO.getReceiverMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getReceiverCnic())) {
            throw new ValidationException("[FAILED] Validation Failed Receiver Cnic Type: " + integrationVO.getReceiverCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }
        if (StringUtils.isEmpty(integrationVO.getPaymentPurpose())) {
            throw new ValidationException("[FAILED] Validation Failed Payment  Purpose: " + integrationVO.getPaymentPurpose());
        }
    }

    public static void challanPayment(ChallanPaymentRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getChallanNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Challan Number: " + integrationVO.getChallanNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getChallanAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Challan Amount: " + integrationVO.getChallanAmount());
        }
        if (StringUtils.isEmpty(integrationVO.getTotalAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Total Amount: " + integrationVO.getTotalAmount());
        }


    }

    public static void challanPaymentInquiry(ChallanPaymentInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getChallanNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Challan Number: " + integrationVO.getChallanNumber());
        }
//
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
    }

    public static void debitCardIssuanceInquiry(DebitCardIssuanceInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction  Type: " + integrationVO.getTransactionType());
        }
    }

    public static void debitCardIssuance(DebitCardIssuanceRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction  Type: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC  : " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getCardDescription())) {
            throw new ValidationException("[FAILED] Validation Failed Card Description : " + integrationVO.getCardDescription());
        }
        if (StringUtils.isEmpty(integrationVO.getMailingAddress())) {
            throw new ValidationException("[FAILED] Validation Failed Mailing Address : " + integrationVO.getMailingAddress());
        }

        if (StringUtils.isEmpty(integrationVO.getCardType())) {
            throw new ValidationException("[FAILED] Validation Card Type Id : " + integrationVO.getCardType());
        }
    }

    public static void hraRegistrationInquiry(HRARegistrationInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number: " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }

    }

    public static void hraRegistration(HRARegistrationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getName())) {
            throw new ValidationException("[FAILED] Validation Failed Name : " + integrationVO.getName());
        }
        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth : " + integrationVO.getDateOfBirth());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number: " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getSourceOfIncome())) {
            throw new ValidationException("[FAILED] Validation Failed Source Of Income: " + integrationVO.getSourceOfIncome());
        }
        if (StringUtils.isEmpty(integrationVO.getOccupation())) {
            throw new ValidationException("[FAILED] Validation Failed Occupation : " + integrationVO.getOccupation());
        }
        if (StringUtils.isEmpty(integrationVO.getPurposeOfAccount())) {
            throw new ValidationException("[FAILED] Validation Failed Purpose Of Account  Type: " + integrationVO.getPurposeOfAccount());
        }
        if (StringUtils.isEmpty(integrationVO.getKinName())) {
            throw new ValidationException("[FAILED] Validation Failed KIN  Name: " + integrationVO.getKinName());
        }
//        if (StringUtils.isEmpty(integrationVO.getKinMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed KIN Mobile Number : " + integrationVO.getKinMobileNumber());
//        }
//        if (StringUtils.isEmpty(integrationVO.getKinCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed KIN CNIC : " + integrationVO.getKinCnic());
//        }
        if (StringUtils.isEmpty(integrationVO.getKinRelation())) {
            throw new ValidationException("[FAILED] Validation Failed KIN Relation : " + integrationVO.getKinRelation());
        }
        if (StringUtils.isEmpty(integrationVO.getInternationalRemittanceLocation())) {
            throw new ValidationException("[FAILED] Validation International Remittance Location : " + integrationVO.getInternationalRemittanceLocation());
        }
        if (StringUtils.isEmpty(integrationVO.getOriginatorRelation())) {
            throw new ValidationException("[FAILED] Validation Failed Originator Realtion : " + integrationVO.getOriginatorRelation());
        }

    }

    public static void walletToCoreInquiry(WalletToCoreInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Receiver Mobile Number  : " + integrationVO.getReceiverMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getReceiverAccountNumber())) {
            throw new ValidationException("[FAILED] Validation Receiver Account Number  : " + integrationVO.getReceiverAccountNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Amount : " + integrationVO.getAmount());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }

    }

    public static void walletToCore(WalletToCoreRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Receiver Mobile Number  : " + integrationVO.getReceiverMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getReceiverAccountNumber())) {
            throw new ValidationException("[FAILED] Validation Receiver Account Number  : " + integrationVO.getReceiverAccountNumber());
        }

    }

    public static void hraToWalletInquiry(HRAToWalletInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }

    }

    public static void hraToWallet(HRAToWalletRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
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


    }

    public static void debitInquiry(DebitInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void fundDebitInquiry(FundDebitInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void debit(DebitRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void fundDebit(FundDebitRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void validateAgentBillPaymentInquiry(AgentBillPaymentInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
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
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Failed Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin Type : " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getAgentMobileNo())) {
            throw new ValidationException("[FAILED] Validation Agent Mobile No: " + integrationVO.getAgentMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerNo())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer No: " + integrationVO.getConsumerNo());
        }

        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }


    }

    public static void validateAgentBillPayment(AgentBillPaymentRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Failed Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getAgentMobileNo())) {
            throw new ValidationException("[FAILED] Validation Agent Mobile No: " + integrationVO.getAgentMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerNo())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer No: " + integrationVO.getConsumerNo());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }
    }

    public static void creditInquiry(CreditInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void credit(CreditRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void hraCashWithdrawalInquiry(HRACashWithdrawalInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Agent Mobile Number: " + integrationVO.getAgentMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCNIC())) {
            throw new ValidationException("[FAILED] Validation Customer CNIC: " + integrationVO.getCNIC());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void hraCashWithdrawal(HRACashWithdrawalRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Agent Mobile Number: " + integrationVO.getAgentMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Customer CNIC: " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void validateLoginAuthenticationRequest(LoginAuthenticationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getPin())) {
            throw new ValidationException("[FAILED] Validation Failed Pin: " + integrationVO.getPin());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateZindigiLoginAuthenticationRequest(ZindigiLoginAuthenticationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getPin())) {
            throw new ValidationException("[FAILED] Validation Failed Pin: " + integrationVO.getPin());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateLoginPinRequest(LoginPinRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getPin())) {
            throw new ValidationException("[FAILED] Validation Failed Pin: " + integrationVO.getPin());
        }

    }

    public static void validateLoginPinChangeRequest(LoginPinChangeRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getOldLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Old Pin: " + integrationVO.getOldLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getNewLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed New Pin: " + integrationVO.getNewLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Pin: " + integrationVO.getConfirmLoginPin());
        }

    }

    public static void validateResetPinRequest(ResetPinRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getNewLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed New Pin: " + integrationVO.getNewLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Pin: " + integrationVO.getConfirmLoginPin());
        }

    }

    public static void validateAdvanceLoanSalary(AdvanceLoanSalaryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation CNIC: " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getLoanAmount())) {
            throw new ValidationException("[FAILED] Validation Loan Amount: " + integrationVO.getLoanAmount());
        }
        if (StringUtils.isEmpty(integrationVO.getNumberOfInstallments())) {
            throw new ValidationException("[FAILED] Validation No. Of Installments: " + integrationVO.getNumberOfInstallments());
        }
        if (StringUtils.isEmpty(integrationVO.getInstallmentAmount())) {
            throw new ValidationException("[FAILED] Validation Installment Amount: " + integrationVO.getInstallmentAmount());
        }
        if (StringUtils.isEmpty(integrationVO.getGracePeriod())) {
            throw new ValidationException("[FAILED] Validation Grace Period: " + integrationVO.getGracePeriod());
        }
        if (StringUtils.isEmpty(integrationVO.getEarlyPaymentCharges())) {
            throw new ValidationException("[FAILED] Validation Early Payment Charges: " + integrationVO.getEarlyPaymentCharges());
        }
        if (StringUtils.isEmpty(integrationVO.getLatePaymentCharges())) {
            throw new ValidationException("[FAILED] Validation Installment Amount: " + integrationVO.getLatePaymentCharges());
        }

    }

    public static void validateSmsGeneration(SmsGenerationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getMessage())) {
            throw new ValidationException("[FAILED] Validation Message: " + integrationVO.getMessage());
        }

    }

    public static void validateAgentLoginRequest(AgentAccountLoginRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
            throw new ValidationException("[FAILED] Validation Failed Agent ID: " + integrationVO.getAgentId());
        }
        if (StringUtils.isEmpty(integrationVO.getPin())) {
            throw new ValidationException("[FAILED] Validation Failed Pin: " + integrationVO.getPin());
        }

    }

    public static void validateAgentLoginPinGenerationRequest(AgentLoginPinGenerationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
            throw new ValidationException("[FAILED] Validation Failed Agent ID: " + integrationVO.getAgentId());
        }
        if (StringUtils.isEmpty(integrationVO.getPin())) {
            throw new ValidationException("[FAILED] Validation Failed Pin: " + integrationVO.getPin());
        }

    }

    public static void validateAgentLoginPinResetRequest(AgentLoginPinResetRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getOldLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Old Pin: " + integrationVO.getOldLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getNewLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed New Pin: " + integrationVO.getNewLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Pin: " + integrationVO.getConfirmLoginPin());
        }

    }

    public static void validateAgentMpinPinGenerationRequest(AgentMpinGenerationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Mpin: " + integrationVO.getMpin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmMPIN())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + integrationVO.getConfirmMPIN());
        }

    }

    public static void validateAgentMpinReset(AgentMpinResetRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
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

    }

    //    public static void validateAgentMpinPinVerificationRequest(AgentMpinVerificationRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
//        }
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//        if (StringUtils.isEmpty(integrationVO.getRrn())) {
//            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
//        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed Mpin: " + integrationVO.getMpin());
//        }
//        if (StringUtils.isEmpty(integrationVO.getConfirmMPIN())) {
//            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin: " + integrationVO.getConfirmMPIN());
//        }
//
//    }
    public static void validateAgentBalanceInquiry(AgentBalanceInquiryRequest integrationVO) {

//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }

//        if (StringUtils.isEmpty(integrationVO.getOtp())) {
//            throw new ValidationException("[FAILED] Validation Failed OTP : " + integrationVO.getOtp());
//        }

        if (StringUtils.isEmpty(integrationVO.getPin())) {
            throw new ValidationException("[FAILED] Validation Failed PIN : " + integrationVO.getPin());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Id : " + integrationVO.getAgentId());
        }

    }

    //    public static void validateAgentToAgentInquiry(AgentToAgentInquiryRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Agent Mobile Number : " + integrationVO.getReceiverAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed MPIN : " + integrationVO.getMpin());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Id : " + integrationVO.getAgentId());
//        }
//
//    }
//
//    public static void validateAgentToAgentPayment(AgentToAgentPaymentRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getCharges())) {
//            throw new ValidationException("[FAILED] Validation Failed Charges : " + integrationVO.getCharges());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Type : " + integrationVO.getTransactionType());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getPaymentType())) {
//            throw new ValidationException("[FAILED] Validation Failed Payment Type : " + integrationVO.getPaymentType());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionCode())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Code : " + integrationVO.getTransactionCode());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSettlementType())) {
//            throw new ValidationException("[FAILED] Validation Failed Settlement Type : " + integrationVO.getSettlementType());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed MPIN : " + integrationVO.getMpin());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Id : " + integrationVO.getAgentId());
//        }
//
//    }
//
//    public static void validateAgentAccountOpening(AgentAccountOpeningRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed MPIN : " + integrationVO.getMpin());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
//            throw new ValidationException("[FAILED] Validation Failed Consumer Name : " + integrationVO.getConsumerName());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAccountTitle())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Title : " + integrationVO.getAccountTitle());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAccountType())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Type : " + integrationVO.getAccountType());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed CNIC : " + integrationVO.getCnic());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getCnicStatus())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Status : " + integrationVO.getCnicStatus());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDob())) {
//            throw new ValidationException("[FAILED] Validation Failed Date of Birth : " + integrationVO.getDob());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getPresentAddress())) {
//            throw new ValidationException("[FAILED] Validation Failed Present Address : " + integrationVO.getPresentAddress());
//        }
//
//    }
//
//    public static void validateAgentUpgadeAccountInquiry(AgentUpgradeAccountInquiryRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//        if (StringUtils.isEmpty(integrationVO.getCustomerCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Old Mpin: " + integrationVO.getCustomerCnic());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Id : " + integrationVO.getAgentId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getSegmentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Segment Id: " + integrationVO.getSegmentId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getRrn())) {
//            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
//        }
//    }
//
//    public static void validateAgentUpgradeAccount(AgentUpgradeAccountRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed MPIN : " + integrationVO.getMpin());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
//            throw new ValidationException("[FAILED] Validation Failed Consumer Name : " + integrationVO.getConsumerName());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAccountTitle())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Title : " + integrationVO.getAccountTitle());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAccountType())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Type : " + integrationVO.getAccountType());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed CNIC : " + integrationVO.getCnic());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getCnicStatus())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Status : " + integrationVO.getCnicStatus());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDob())) {
//            throw new ValidationException("[FAILED] Validation Failed Date of Birth : " + integrationVO.getDob());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getPresentAddress())) {
//            throw new ValidationException("[FAILED] Validation Failed Present Address : " + integrationVO.getPresentAddress());
//        }
//
//    }
//
//    public static void validateAgentCashInInquiry(AgentCashInInquiryRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//    }
//
//    public static void validateAgentCashIn(AgentCashInRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//    }
//
//    public static void validateAgentCashOutInquiry(AgentCashOutInquiryRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getBankId())) {
//            throw new ValidationException("[FAILED] Validation Failed Bank Id : " + integrationVO.getBankId());
//        }
//    }
//
//    public static void validateAgentCashOut(AgentCashOutRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getBankId())) {
//            throw new ValidationException("[FAILED] Validation Failed Bank Id : " + integrationVO.getBankId());
//        }
//
//    }
//
//    public static void validateAgentWalletToWalletInquiry(AgentWalletToWalletInquiryRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//    }
//
//    public static void validateAgentWalletToWalletPayment(AgentWalletToWalletPaymentRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//    }
//
//    public static void validateAgentWalletToCnicInquiry(AgentWalletToCnicInquiryRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//    }
//
//    public static void validateAgentWalletToCnicPayment(AgentWalletToCnicPaymentRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//    }
    public static void validateAgentIbftInquiry(AgentIbftInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }

        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
        }


        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
        }


        if (StringUtils.isEmpty(integrationVO.getCoreAccountId())) {
            throw new ValidationException("[FAILED] Validation Failed Core Account Id : " + integrationVO.getCoreAccountId());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
            throw new ValidationException("[FAILED] Validation Failed Core Agent Id : " + integrationVO.getAgentId());
        }


        if (StringUtils.isEmpty(integrationVO.getPaymentPurpose())) {
            throw new ValidationException("[FAILED] Validation Failed Purpose of Payment : " + integrationVO.getPaymentPurpose());
        }


    }

    public static void validateAgentIbftPayment(AgentIbftPaymentRequest integrationVO) {

//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getTransactionAmount());
        }


        if (StringUtils.isEmpty(integrationVO.getPaymentPurpose())) {
            throw new ValidationException("[FAILED] Validation Failed Purpose of Payment : " + integrationVO.getPaymentPurpose());
        }


    }

//    public static void validateAgentRetailPaymentInquiry(AgentRetailPaymentInquiryRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//
//    }
//
//    public static void validateAgentRetailPayment(AgentRetailPaymentRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getComissionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Comission Amount : " + integrationVO.getComissionAmount());
//        }
//        if (StringUtils.isEmpty(integrationVO.getTransactionProcessingAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Processing Amount : " + integrationVO.getTransactionProcessingAmount());
//        }
//        if (StringUtils.isEmpty(integrationVO.getTotalAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Total Transaction Amount : " + integrationVO.getTotalAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//
//    }
//
//
//    public static void validateAgentWalletToCoreInquiry(AgentWalletToCoreInquiryRequest integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
//        }
//
//
//    }
//
//    public static void validateAgentWalletToCorePayment(AgentWalletToCorePaymentRequest integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//
//    }
//
//    public static void validateAgentReceiveMoneyInquiry(AgentReceiveMoneyInquiryRequest integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductId())) {
//            throw new ValidationException("[FAILED] Validation Failed Product ID: " + integrationVO.getProductId());
//        }
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Cnic : " + integrationVO.getReceiverCnic());
//        }
//
//    }
//
//    public static void validateAgentReceiveMoneyPayment(AgentReceiveMoneyPaymentRequest integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Mobile Number : " + integrationVO.getSenderMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Cnic : " + integrationVO.getSenderCnic());
//        }
//
//
//    }

    //    public static void validateAgentCnicToCnicInquiry(AgentCnicToCnicInquiryRequest integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Mobile Number : " + integrationVO.getSenderMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Cnic : " + integrationVO.getSenderCnic());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Cnic : " + integrationVO.getReceiverCnic());
//        }
//
//
//    }
//
//    public static void validateAgentCnicToCnicPayment(AgentCnicToCnicPaymentRequest integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Mobile Number : " + integrationVO.getSenderMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Cnic : " + integrationVO.getSenderCnic());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Cnic : " + integrationVO.getReceiverCnic());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getPin())) {
//            throw new ValidationException("[FAILED] Validation Failed PIN  : " + integrationVO.getPin());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount  : " + integrationVO.getAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Id  : " + integrationVO.getAgentId());
//        }
//
//    }
//    public static void agentHRARegistrationInquiry(AgentHRARegistrationInquiryRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
//        }
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//        if (StringUtils.isEmpty(integrationVO.getRrn())) {
//            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
//        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number: " + integrationVO.getCnic());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Id: " + integrationVO.getAgentId());
//        }
//
//    }
//
//    public static void agentHRARegistration(AgentHRARegistrationRequest integrationVO) {
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number : " + integrationVO.getMobileNumber());
//        }
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//        if (StringUtils.isEmpty(integrationVO.getRrn())) {
//            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
//        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getPinType())) {
//            throw new ValidationException("[FAILED] Validation Failed Pin Type: " + integrationVO.getPinType());
//        }
//        if (StringUtils.isEmpty(integrationVO.getName())) {
//            throw new ValidationException("[FAILED] Validation Failed Name : " + integrationVO.getName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Of Birth : " + integrationVO.getDateOfBirth());
//        }
//        if (StringUtils.isEmpty(integrationVO.getCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number: " + integrationVO.getCnic());
//        }
//        if (StringUtils.isEmpty(integrationVO.getSourceOfIncome())) {
//            throw new ValidationException("[FAILED] Validation Failed Source Of Income: " + integrationVO.getSourceOfIncome());
//        }
//        if (StringUtils.isEmpty(integrationVO.getOccupation())) {
//            throw new ValidationException("[FAILED] Validation Failed Occupation : " + integrationVO.getOccupation());
//        }
//        if (StringUtils.isEmpty(integrationVO.getPurposeOfAccount())) {
//            throw new ValidationException("[FAILED] Validation Failed Purpose Of Account  Type: " + integrationVO.getPurposeOfAccount());
//        }
//        if (StringUtils.isEmpty(integrationVO.getKinName())) {
//            throw new ValidationException("[FAILED] Validation Failed KIN  Name: " + integrationVO.getKinName());
//        }
////        if (StringUtils.isEmpty(integrationVO.getKinMobileNumber())) {
////            throw new ValidationException("[FAILED] Validation Failed KIN Mobile Number : " + integrationVO.getKinMobileNumber());
////        }
////        if (StringUtils.isEmpty(integrationVO.getKinCnic())) {
////            throw new ValidationException("[FAILED] Validation Failed KIN CNIC : " + integrationVO.getKinCnic());
////        }
//        if (StringUtils.isEmpty(integrationVO.getKinRelation())) {
//            throw new ValidationException("[FAILED] Validation Failed KIN Relation : " + integrationVO.getKinRelation());
//        }
//        if (StringUtils.isEmpty(integrationVO.getInternationalRemittanceLocation())) {
//            throw new ValidationException("[FAILED] Validation International Remittance Location : " + integrationVO.getInternationalRemittanceLocation());
//        }
//        if (StringUtils.isEmpty(integrationVO.getOriginatorRelation())) {
//            throw new ValidationException("[FAILED] Validation Failed Originator Realtion : " + integrationVO.getOriginatorRelation());
//        }
//
//    }
//
//    public static void validateAgentCnicToCoreInquiry(AgentCnicToCoreInquiryRequest integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Mobile Number : " + integrationVO.getSenderMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Cnic : " + integrationVO.getSenderCnic());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Cnic : " + integrationVO.getReceiverCnic());
//        }
//
//
//    }
//
//    public static void validateAgentCnicToCorePayment(AgentCnicToCorePaymentRequest integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Mobile Number : " + integrationVO.getSenderMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getSenderCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Sender Cnic : " + integrationVO.getSenderCnic());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Mobile Number : " + integrationVO.getReceiverMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getReceiverCnic())) {
//            throw new ValidationException("[FAILED] Validation Failed Receiver Cnic : " + integrationVO.getReceiverCnic());
//        }
//
//
//    }
    public static void validateAgentCashDepositInquiry(AgentCashDepositInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }

        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
        }


        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
        }


    }

    public static void validateAgentCashDepositPayment(AgentCashDepositPaymentRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
            throw new ValidationException("[FAILED] Validation Failed Agent ID: " + integrationVO.getAgentId());
        }

        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
        }


        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getPin())) {
            throw new ValidationException("[FAILED] Validation Failed PIN : " + integrationVO.getPin());
        }


    }

    public static void validateAgentCashWithdrawalInquiry(AgentCashWithdrawalInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }

        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC : " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
        }


    }

    public static void validateAgentCashWithdrawalPayment(AgentCashWithdrawalPaymentRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalID())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalID());
        }

        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount : " + integrationVO.getAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number : " + integrationVO.getAgentMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCustomerCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC : " + integrationVO.getCustomerCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id : " + integrationVO.getProductId());
        }

        if (StringUtils.isEmpty(integrationVO.getPin())) {
            throw new ValidationException("[FAILED] Validation Failed PIN : " + integrationVO.getPin());
        }


    }

    public static void validateMpinVerification(MpinVerificationRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }

        if (StringUtils.isEmpty(integrationVO.getMpin())) {
            throw new ValidationException("[FAILED] Validation Failed MPIN : " + integrationVO.getMpin());
        }


    }

    public static void validatesegmentList(SegmentListRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + integrationVO.getRrn());
        }


    }

    public static void validateCatalogList(AgentCatalogsRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + integrationVO.getRrn());
        }


    }

    public static void validateL2AccountOpening(L2AccountOpeningRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }


        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
        }
        if (StringUtils.isEmpty(integrationVO.getPermanentAddress())) {
            throw new ValidationException("[FAILED] Validation Failed Present Address: " + integrationVO.getPermanentAddress());
        }


//        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
//        }
        if (StringUtils.isEmpty(integrationVO.getDob())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDob());
        }

//        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
//            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
//            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getGender())) {
//            throw new ValidationException("[FAILED] Validation Failed Gender: " + integrationVO.getGender());
//        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateL2AccountUpgrade(L2AccountUpgradeRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }


        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
        }
        if (StringUtils.isEmpty(integrationVO.getPermanentAddress())) {
            throw new ValidationException("[FAILED] Validation Failed Present Address: " + integrationVO.getPermanentAddress());
        }


//        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
//        }
        if (StringUtils.isEmpty(integrationVO.getDob())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDob());
        }

//        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
//            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
//            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getGender())) {
//            throw new ValidationException("[FAILED] Validation Failed Gender: " + integrationVO.getGender());
//        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateAccountDetail(AccountDetails integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateCustomerNameUpdate(CustomerNameUpdateRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getFirstName())) {
            throw new ValidationException("[FAILED] Validation Failed First Name: " + integrationVO.getFirstName());
        }

        if (StringUtils.isEmpty(integrationVO.getLastName())) {
            throw new ValidationException("[FAILED] Validation Failed Last Name: " + integrationVO.getLastName());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateClsStatusUpdate(CLSStatusUpdateRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCaseStatus())) {
            throw new ValidationException("[FAILED] Validation Failed Case Status: " + integrationVO.getCaseStatus());
        }

        if (StringUtils.isEmpty(integrationVO.getCaseId())) {
            throw new ValidationException("[FAILED] Validation Failed Case Id: " + integrationVO.getCaseId());
        }
        if (StringUtils.isEmpty(integrationVO.getClsComment())) {
            throw new ValidationException("[FAILED] Validation Failed CLS Comment: " + integrationVO.getClsComment());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }


    public static void validateBlinkAccountVerificationInquiry(BlinkAccountVerificationInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateBlinkAccountVerification(BlinkAccountVerificationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
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

        if (!(integrationVO.getReserved2().equals("1"))) {
            if (StringUtils.isEmpty(integrationVO.getFingerIndex())) {
                throw new ValidationException("[FAILED] Validation Failed Finger Index: " + integrationVO.getFingerIndex());
            }
        }
        if (!(integrationVO.getReserved2().equals("1"))) {

            if (StringUtils.isEmpty(integrationVO.getFingerTemplate())) {
                throw new ValidationException("[FAILED] Validation Failed Finger Template: " + integrationVO.getFingerTemplate());
            }
        }
        if (!(integrationVO.getReserved2().equals("1"))) {

            if (StringUtils.isEmpty(integrationVO.getTemplateType())) {
                throw new ValidationException("[FAILED] Validation Failed Template Type: " + integrationVO.getTemplateType());
            }
        }
    }

    public static void validateDebitCardStatusVerification(DebitCardStatusRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
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

    public static void validateAdvanceLoanPaymentSettlement(AdvanceLoanEarlyPaymentSettlementRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
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

    public static void validateFeeInquiryPayment(FeePaymentInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
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

    public static void validateFeePayment(FeePaymentRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
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

    public static void validateM0AccountOpening(MinorAccountOpeningRequest integrationVO) {

//        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getRrn())) {
//            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
//        }

    }

    public static void validateOptasiaCreditInquiry(OptasiaCreditInquiryRequest integrationVO) throws Exception {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void validateOptasiaCredit(OptasiaCreditRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id : " + integrationVO.getCustomerId());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void validateOptasiadebitInquiry(OptasiaDebitInquiryRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id : " + integrationVO.getCustomerId());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void validateOptasiaDebit(OptasiaDebitRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id : " + integrationVO.getCustomerId());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Product ID: " + integrationVO.getProductId());
        }
        if (StringUtils.isEmpty(integrationVO.getPinType())) {
            throw new ValidationException("[FAILED] Validation Failed Pin  Type: " + integrationVO.getPinType());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Transaction Amount : " + integrationVO.getTransactionAmount());
        }
    }

    public static void validateTransactionStatus(TransactionStatusRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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

    public static void validateProfileStatus(ProfileStatusRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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

    public static void validateLienStatus(LienStatusRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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

    public static void validateSelectLoan(LoanOfferRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }

    }

    public static void validateOutstanding(LoansRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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

    public static void validateOutstandingLoan(OutstandingLoanRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getProductId())) {
            throw new ValidationException("[FAILED] Validation Failed Product ID: " + integrationVO.getProductId());
        }


    }

    //
    public static void validateMerchantAccountUpgrade(MerchantAccountUpgradeRequest integrationVO) throws ValidationException {

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
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }

    }


    public static void validateMerchantPictureUpgrade(MerchantPictureUpgradeRequest integrationVO) throws ValidationException {

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
//        if (StringUtils.isEmpty(integrationVO.getCnicNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic: " + integrationVO.getCnicNumber());
//        }
//        if (StringUtils.isEmpty(integrationVO.getProfilePic())) {
//            throw new ValidationException("[FAILED] Validation Failed Customer Picture: " + integrationVO.getProfilePic());
//        }
//        if (StringUtils.isEmpty(integrationVO.getCNICFrontPic())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Front Picture: " + integrationVO.getCNICFrontPic());
//        }
//        if (StringUtils.isEmpty(integrationVO.getCNICBackPic())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Back Picture: " + integrationVO.getCNICBackPic());
//        }
//        if (StringUtils.isEmpty(integrationVO.getBusinessName())) {
//            throw new ValidationException("[FAILED] Validation Failed Business Name: " + integrationVO.getBusinessName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getBusinessAddress())) {
//            throw new ValidationException("[FAILED] Validation Failed Business Address: " + integrationVO.getBusinessAddress());
//        }

    }

//    public static void validateCustomerOutstandingLoanStatus(OutstandingRequest integrationVO) throws ValidationException {
//
//        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
//            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getRrn())) {
//            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
//        }
//
//    }

    public static void validateLoanPayment(LoanPaymentRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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

        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }
        if (StringUtils.isEmpty(integrationVO.getCurrencyCode())) {
            throw new ValidationException("[FAILED] Validation Failed Currency Code: " + integrationVO.getCurrencyCode());
        }

    }

    //    public static void validateLoanStatus(LoanStatusRequest integrationVO) throws ValidationException {
//
//        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
//            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
//            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getRrn())) {
//            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
//        }
//
//    }
    public static void validateLoanCallBack(LoanCallBackRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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
//        if (StringUtils.isEmpty(integrationVO.getThirdPartyTransactionId())) {
//            throw new ValidationException("[FAILED] Validation Failed Third Party Transaction Id: " + integrationVO.getThirdPartyTransactionId());
//        }
        if (StringUtils.isEmpty(integrationVO.getInternalLoanId())) {
            throw new ValidationException("[FAILED] Validation Failed Internal Loan Id: " + integrationVO.getInternalLoanId());
        }

    }

    public static void validateInitiateLoan(OfferListForCommodityRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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

    public static void validateSelectLoanOffer(ProjectionRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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

    public static void validateLoanHistory(LoansHistoryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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
        if (StringUtils.isEmpty(String.valueOf(integrationVO.getFromDate()))) {
            throw new ValidationException("[FAILED] Validation Failed From Date: " + integrationVO.getFromDate());
        }

        if (StringUtils.isEmpty(String.valueOf(integrationVO.getToDate()))) {
            throw new ValidationException("[FAILED] Validation Failed To Date: " + integrationVO.getToDate());
        }
    }

    public static void validateLoanPlan(LoansPlanRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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
        if (StringUtils.isEmpty(String.valueOf(integrationVO.getFromDate()))) {
            throw new ValidationException("[FAILED] Validation Failed From Date: " + integrationVO.getFromDate());
        }

        if (StringUtils.isEmpty(String.valueOf(integrationVO.getToDate()))) {
            throw new ValidationException("[FAILED] Validation Failed To Date: " + integrationVO.getToDate());
        }
        if (StringUtils.isEmpty(integrationVO.getAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Amount: " + integrationVO.getAmount());
        }
    }

    public static void validateTransactionActive(TransactionActiveRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id: " + integrationVO.getCustomerId());
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
        if (StringUtils.isEmpty(String.valueOf(integrationVO.getFromDate()))) {
            throw new ValidationException("[FAILED] Validation Failed From Date: " + integrationVO.getFromDate());
        }

        if (StringUtils.isEmpty(String.valueOf(integrationVO.getToDate()))) {
            throw new ValidationException("[FAILED] Validation Failed To Date: " + integrationVO.getToDate());
        }
    }

    public static void validateOptasiaSmsGeneration(OptasiaSmsGenerationRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getCustomerId())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Id : " + integrationVO.getCustomerId());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN : " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getMessage())) {
            throw new ValidationException("[FAILED] Validation Message: " + integrationVO.getMessage());
        }

    }

    public static void validateSimpleAccountOpening(SimpleAccountOpeningRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
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
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(String.valueOf(integrationVO.getCnicIssuanceDate()))) {
            throw new ValidationException("[FAILED] Validation Failed Cnic Issuance Date: " + integrationVO.getCnicIssuanceDate());
        }

        if (StringUtils.isEmpty(String.valueOf(integrationVO.getCnicExpiryDate()))) {
            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry Date: " + integrationVO.getCnicExpiryDate());
        }
    }

    public static void validateAccountStatus(AccountStatusRequest integrationVO) throws ValidationException {

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

    public static void validateUpdateCnicExpiry(UpdateCnicExpiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed Cnic Number: " + integrationVO.getCnic());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getIssuanceDate())) {
            throw new ValidationException("[FAILED] Validation Failed Cnic Issuance Date: " + integrationVO.getIssuanceDate());
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

    }

    public static void validateL2AccountUpgradeDiscrepant(L2AccountUpgradeDiscrepantRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }


        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
        }
        if (StringUtils.isEmpty(integrationVO.getPermanentAddress())) {
            throw new ValidationException("[FAILED] Validation Failed Present Address: " + integrationVO.getPermanentAddress());
        }


//        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
//        }
//        if (StringUtils.isEmpty(integrationVO.getDob())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDob());
//        }

//        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
//            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
//            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getGender())) {
//            throw new ValidationException("[FAILED] Validation Failed Gender: " + integrationVO.getGender());
//        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateGetL2AccountUpgradeDiscrepant(GetL2AccountUpgradeDiscrepantRequest integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnic())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnic());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateThirdPartyCreditInquiry(ThirdPartyCreditInquiryRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getUserName())) {
            throw new ValidationException("[FAILED] Validation Failed Username: " + integrationVO.getUserName());
        }
        if (StringUtils.isEmpty(integrationVO.getPassword())) {
            throw new ValidationException("[FAILED] Validation Failed Password: " + integrationVO.getPassword());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateThirdPartyCredit(ThirdPartyCreditRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getUserName())) {
            throw new ValidationException("[FAILED] Validation Failed Username: " + integrationVO.getUserName());
        }
        if (StringUtils.isEmpty(integrationVO.getPassword())) {
            throw new ValidationException("[FAILED] Validation Failed Password: " + integrationVO.getPassword());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateCustomerCliStatus(CustomerCliStatusRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateCustomerValidation(CustomerValidationRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getStan())) {
            throw new ValidationException("[FAILED] Validation Failed STAN: " + integrationVO.getStan());
        }
        if (StringUtils.isEmpty(integrationVO.getCustomerIdentification())) {
            throw new ValidationException("[FAILED] Validation Failed Customer Identification Number: " + integrationVO.getCustomerIdentification());
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

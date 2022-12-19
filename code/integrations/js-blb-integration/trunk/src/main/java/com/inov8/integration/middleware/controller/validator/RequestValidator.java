package com.inov8.integration.middleware.controller.validator;

import com.inov8.integration.middleware.enums.ConstantEnums;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.apache.commons.lang.StringUtils;

public class RequestValidator {


    public static void validateVerifyAccount(WebServiceVO integrationVO) {



        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed getTransactionType: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateAccountOpening(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
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
        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDateOfBirth());
        }

//        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
//            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
//            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
//        }

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

    public static void validateConventionalAccountOpening(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
        }
        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDateOfBirth());
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
//        if (StringUtils.isEmpty(integrationVO.getSignaturePhoto())) {
//            throw new ValidationException("[FAILED] Validation Failed Signature Photo: " + integrationVO.getSignaturePhoto());
//        }
        /*if (StringUtils.isEmpty(integrationVO.getTermsPhoto())) {
            throw new ValidationException("[FAILED] Validation Failed Terms Photo: " + integrationVO.getTermsPhoto());
        }*/
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getAccountType())) {
            throw new ValidationException("[FAILED] Validation Failed Account Type: " + integrationVO.getAccountType());
        }
    }

    public static void validatePaymentInquiry(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed getSecurityCode: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateLoginPin(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Login Pin:  " + integrationVO.getLoginPin());
        }

    }

    public static void validateLoginPinChange(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getOldLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Old Pin:  " + integrationVO.getOldLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getNewLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed New Pin:  " + integrationVO.getNewLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Pin:  " + integrationVO.getConfirmLoginPin());
        }

    }

    public static void validateResetPin(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC:  " + integrationVO.getCnicNo());
        }
        if (StringUtils.isEmpty(integrationVO.getNewLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed New Pin:  " + integrationVO.getNewLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Pin:  " + integrationVO.getConfirmLoginPin());
        }

    }

    public static void validatePaymentRequest(WebServiceVO integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getAccountNo1())) {
            throw new ValidationException("[FAILED] Validation Failed Account Number: " + integrationVO.getAccountNo1());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
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

        if (!integrationVO.getPaymentType().equals(ConstantEnums.SETTLEMENT_TYPE.getValue())){
            if (StringUtils.isEmpty(integrationVO.getMobilePin())) {
                throw new ValidationException("[FAILED] Validation Failed M PIN: " + integrationVO.getMobilePin());
            }
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getMicrobankTransactionCode())) {
            throw new ValidationException("[FAILED] Validation Failed getMicrobankTransactionCode: " + integrationVO.getMicrobankTransactionCode());
        }

    }

    public static void validatePaymentReversalRequest(WebServiceVO integrationVO) {

        /*if (StringUtils.isEmpty(integrationVO.getMicrobankTransactionCode())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Code: " + integrationVO.getMicrobankTransactionCode());
        }*/

        if (StringUtils.isEmpty(integrationVO.getRetrievalReferenceNumber())) {
            throw new ValidationException("[FAILED] Validation Failed getRetrievalReferenceNumber: " + integrationVO.getRetrievalReferenceNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateOtpRequest(WebServiceVO integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getOtpPin())) {
            throw new ValidationException("[FAILED] Validation Failed OTP Pin: " + integrationVO.getOtpPin());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC No: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateCardTagging(WebServiceVO integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getCardExpiry())) {
            throw new ValidationException("[FAILED] Validation Failed Card Expiry: " + integrationVO.getCardExpiry());
        }
        if (StringUtils.isEmpty(integrationVO.getCardNo())) {
            throw new ValidationException("[FAILED] Validation Failed Card Number: " + integrationVO.getCardNo());
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

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateAccountLinkDeLink(WebServiceVO integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Type: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC No: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateSetCardStatus(WebServiceVO integrationVO)    {

        if (StringUtils.isEmpty(integrationVO.getTransactionType())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Type: " + integrationVO.getTransactionType());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC No: " + integrationVO.getCnicNo());
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

    public static void validateAdvanceLoanSalary(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC:  " + integrationVO.getCnicNo());
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

    public static void validateSmsGeneration(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getMessage())) {
            throw new ValidationException("[FAILED] Validation Failed Message: " + integrationVO.getMessage());
        }


    }

    public static void validateAgentLoginPin(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Login Pin:  " + integrationVO.getLoginPin());
        }

    }

    public static void validateAgentLoginPinReset(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getOldLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Old Login Pin:  " + integrationVO.getOldLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getNewLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed New Login Pin:  " + integrationVO.getNewLoginPin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmLoginPin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Login Pin:  " + integrationVO.getConfirmLoginPin());
        }

    }

    public static void validateAgentMpinGeneration(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getAgentMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Mpin:  " + integrationVO.getAgentMpin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin:  " + integrationVO.getConfirmMpin());
        }

    }

    public static void validateAgentMpinReset(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getOldMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Old Mpin:  " + integrationVO.getOldMpin());
        }
        if (StringUtils.isEmpty(integrationVO.getNewMpin())) {
            throw new ValidationException("[FAILED] Validation Failed New Mpin:  " + integrationVO.getNewMpin());
        }
        if (StringUtils.isEmpty(integrationVO.getConfirmMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin:  " + integrationVO.getConfirmMpin());
        }

    }
//
//    public static void validateAgentMpinVerification(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAgentMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed Mpin:  " + integrationVO.getAgentMpin());
//        }
//        if (StringUtils.isEmpty(integrationVO.getConfirmMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed Confirm Mpin:  " + integrationVO.getConfirmMpin());
//        }
//
//    }

    public static void validateAgentBalanceInquiry(WebServiceVO integrationVO) {


//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getAgentMpin())) {
            throw new ValidationException("[FAILED] Validation Failed Mpin:  " + integrationVO.getAgentMpin());
        }
        if (StringUtils.isEmpty(integrationVO.getOtpPin())) {
            throw new ValidationException("[FAILED] Validation Failed OTP :  " + integrationVO.getOtpPin());
        }

    }
//
//    public static void validateAgentToAgentInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAgentMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed Mpin:  " + integrationVO.getAgentMpin());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Id: " + integrationVO.getAgentId());
//        }
//    }
//
//    public static void validateAgentToAgentPayment(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAgentMpin())) {
//            throw new ValidationException("[FAILED] Validation Failed Mpin:  " + integrationVO.getAgentMpin());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Id: " + integrationVO.getAgentId());
//        }
//    }
//
//    public static void validateAgentAccountOpening(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
//            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
//            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAccountTitle())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Title: " + integrationVO.getAccountTitle());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
//            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
//        }
//        if (StringUtils.isEmpty(integrationVO.getPresentAddress())) {
//            throw new ValidationException("[FAILED] Validation Failed Present Address: " + integrationVO.getPresentAddress());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getCnicStatus())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Status: " + integrationVO.getCnicStatus());
//        }
//
////        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
////            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
////        }
//        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDateOfBirth());
//        }
//
////        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
////            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
////        }
////        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
////            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
////        }
//
////        if (StringUtils.isEmpty(integrationVO.getGender())) {
////            throw new ValidationException("[FAILED] Validation Failed Gender: " + integrationVO.getGender());
////        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAccountType())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Type: " + integrationVO.getAccountType());
//        }
//    }
//
//    public static void validateAgentUpgradeAccountInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id:  " + integrationVO.getProductID());
//        }
//        if (StringUtils.isEmpty(integrationVO.getSegmentCode())) {
//            throw new ValidationException("[FAILED] Validation Failed Segment Id :  " + integrationVO.getSegmentCode());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Id: " + integrationVO.getAgentId());
//        }
//    }
//
//    public static void validateAgentUpgradeAccount(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
//            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
//            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAccountTitle())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Title: " + integrationVO.getAccountTitle());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
//            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
//        }
//        if (StringUtils.isEmpty(integrationVO.getPresentAddress())) {
//            throw new ValidationException("[FAILED] Validation Failed Present Address: " + integrationVO.getPresentAddress());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getCnicStatus())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Status: " + integrationVO.getCnicStatus());
//        }
//
////        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
////            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
////        }
//        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDateOfBirth());
//        }
//
////        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
////            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
////        }
////        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
////            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
////        }
//
////        if (StringUtils.isEmpty(integrationVO.getGender())) {
////            throw new ValidationException("[FAILED] Validation Failed Gender: " + integrationVO.getGender());
////        }
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//        if (StringUtils.isEmpty(integrationVO.getAccountType())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Type: " + integrationVO.getAccountType());
//        }
//    }
//
//    public static void validateAgentCashInInquiry (WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentCashIn(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentCashOutInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentCashOut(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentWalletToWalletInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentWalletToWalletPayment(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentWalletToCnicInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentWalletToCnicPayment(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }

    public static void validateAgentIbftInquiry(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getProductID())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
        }
    }

    public static void validateAgentIbftPayment(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getProductID())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
        }
    }

//    public static void validateAgentRetailPaymentInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentRetailPayment(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentWalletToCoreInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentWalletToCorePayment(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentReceiveMoneyInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentReceiveMoneyPayment(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }

//    public static void validateAgentCnicToCnicInquiry(WebServiceVO integrationVO) {
//
//
//        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }
//
//    public static void validateAgentCnicToCnicPayment(WebServiceVO integrationVO)   {
//
//
//        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
//            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number: " + integrationVO.getAgentMobileNumber());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
//            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
//            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
//            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
//        }
//
//        if (StringUtils.isEmpty(integrationVO.getProductID())) {
//            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
//        }
//    }

    public static void validateAgentHRARegistrationInquiry(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Id: " + integrationVO.getAgentId());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateAgentHRARegistration(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentId())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Id: " + integrationVO.getAgentId());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

    }

    public static void validateAgentCnicToCoreInquiry(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getProductID())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
        }
    }

    public static void validateAgentCnicToCorePayment(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getProductID())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
        }
    }

    public static void validateAgentCashDepositInquiry(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getProductID())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
        }
    }

    public static void validateAgentCashDepositPayment(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getAgentMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Agent Mobile Number :  " + integrationVO.getAgentMobileNumber());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getProductID())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
        }
    }

    public static void validateAgentCashWithdrawalInquiry(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getProductID())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
        }
    }

    public static void validateAgentCashWithdrawalPayment(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(integrationVO.getProductID())) {
            throw new ValidationException("[FAILED] Validation Failed Product Id: " + integrationVO.getProductID());
        }
    }

    public static void validateMpinVerification(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getMobilePin())) {
            throw new ValidationException("[FAILED] Validation Failed MPIN: " + integrationVO.getMobilePin());
        }


    }

    public static void validateSegmentList(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }


    }

    public static void validateCatalogList(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }

        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal Id: " + integrationVO.getTerminalId());
        }


    }

    public static void validatel2AccountOpening(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }
//        if (StringUtils.isEmpty(integrationVO.getAccountTitle())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Title: " + integrationVO.getAccountTitle());
//        }

        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
        }
        if (StringUtils.isEmpty(integrationVO.getPermanentAddress())) {
            throw new ValidationException("[FAILED] Validation Failed Permanent Address: " + integrationVO.getPermanentAddress());
        }

//        if (StringUtils.isEmpty(integrationVO.getCnicStatus())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Status: " + integrationVO.getCnicStatus());
//        }

//        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
//        }
        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDateOfBirth());
        }

//        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
//            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
//            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
//        }

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

    public static void validatel2AccountUpgrade(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }
//        if (StringUtils.isEmpty(integrationVO.getAccountTitle())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Title: " + integrationVO.getAccountTitle());
//        }

        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
        }
        if (StringUtils.isEmpty(integrationVO.getPermanentAddress())) {
            throw new ValidationException("[FAILED] Validation Failed Permanent Address: " + integrationVO.getPermanentAddress());
        }

//        if (StringUtils.isEmpty(integrationVO.getCnicStatus())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Status: " + integrationVO.getCnicStatus());
//        }

//        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
//        }
        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDateOfBirth());
        }

//        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
//            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
//            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
//        }

//        if (StringUtils.isEmpty(integrationVO.getGender())) {
//            throw new ValidationException("[FAILED] Validation Failed Gender: " + integrationVO.getGender());
//        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validatel2AccountDetail(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getCnicNo())) {
            throw new ValidationException("[FAILED] Validation Failed CNIC: " + integrationVO.getCnicNo());
        }

        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getConsumerName())) {
            throw new ValidationException("[FAILED] Validation Failed Consumer Name: " + integrationVO.getConsumerName());
        }
//        if (StringUtils.isEmpty(integrationVO.getAccountTitle())) {
//            throw new ValidationException("[FAILED] Validation Failed Account Title: " + integrationVO.getAccountTitle());
//        }

        if (StringUtils.isEmpty(integrationVO.getBirthPlace())) {
            throw new ValidationException("[FAILED] Validation Failed Birth Place: " + integrationVO.getBirthPlace());
        }
        if (StringUtils.isEmpty(integrationVO.getPermanentAddress())) {
            throw new ValidationException("[FAILED] Validation Failed Permanent Address: " + integrationVO.getPermanentAddress());
        }

//        if (StringUtils.isEmpty(integrationVO.getCnicStatus())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Status: " + integrationVO.getCnicStatus());
//        }

//        if (StringUtils.isEmpty(integrationVO.getCnicExpiry())) {
//            throw new ValidationException("[FAILED] Validation Failed Cnic Expiry: " + integrationVO.getCnicExpiry());
//        }
        if (StringUtils.isEmpty(integrationVO.getDateOfBirth())) {
            throw new ValidationException("[FAILED] Validation Failed Date Of Birth: " + integrationVO.getDateOfBirth());
        }

//        if (StringUtils.isEmpty(integrationVO.getFatherHusbandName())) {
//            throw new ValidationException("[FAILED] Validation Failed Father Hasband Name: " + integrationVO.getFatherHusbandName());
//        }
//        if (StringUtils.isEmpty(integrationVO.getMotherMaiden())) {
//            throw new ValidationException("[FAILED] Validation Failed Mother Maiden: " + integrationVO.getMotherMaiden());
//        }

//        if (StringUtils.isEmpty(integrationVO.getGender())) {
//            throw new ValidationException("[FAILED] Validation Failed Gender: " + integrationVO.getGender());
//        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static void validateCustomerNameUpdate(WebServiceVO integrationVO) {


        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }

        if (StringUtils.isEmpty(integrationVO.getMobileNo())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNo());
        }
        if (StringUtils.isEmpty(integrationVO.getFirstName())) {
            throw new ValidationException("[FAILED] Validation Failed First Name: " + integrationVO.getFirstName());
        }
        if (StringUtils.isEmpty(integrationVO.getLastName())) {
            throw new ValidationException("[FAILED] Validation Failed Last Name: " + integrationVO.getLastName());
        }

        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

}

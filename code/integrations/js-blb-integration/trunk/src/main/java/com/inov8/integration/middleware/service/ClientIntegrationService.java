package com.inov8.integration.middleware.service;

import com.inov8.integration.middleware.controller.hostController.JsBLBIntegration;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.FieldUtil;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Random;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service("ClientIntegrationService")
public class ClientIntegrationService {
    private static Logger logger = LoggerFactory.getLogger(ClientIntegrationService.class);

    private final String USER_NAME = ConfigReader.getInstance().getProperty("ws.user.name", "");
    private final String PASSWORD = ConfigReader.getInstance().getProperty("ws.password", "");

    @Autowired
    @Qualifier(value = "jsFonePayIntegration")
    private JsBLBIntegration fonePayIntegration;

    public WebServiceVO accountVerify(WebServiceVO webServiceVO) throws RuntimeException {
        VerifyAccountRequest request = new VerifyAccountRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setCnic(webServiceVO.getCnicNo());
        request.setDateTime(webServiceVO.getDateTime());
        request.setMobileNumber(webServiceVO.getMobileNo());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setTransactionType(webServiceVO.getTransactionType());
        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getCnic() + request.getDateTime() + request.getMobileNumber() + request.getRrn() + request.getTransactionType() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        VerifyAccountResponse response = null;
        try {
            response = fonePayIntegration.verifyAccount(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
            webServiceVO.setAccountTitle(response.getAccountTitle());
            webServiceVO.setFirstName(response.getFirstName());
            webServiceVO.setLastName(response.getLastName());
            webServiceVO.setCnicExpiry(response.getCnicExpiry());
            webServiceVO.setAccountType(response.getAccountType());
            webServiceVO.setMobileNo(response.getMobileNumber());
            webServiceVO.setCnicNo(response.getCnic());
            webServiceVO.setCustomerStatus(response.getAccountStatus());
            webServiceVO.setDateOfBirth(response.getDateOfBirth());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO accountOpening(WebServiceVO webServiceVO) throws RuntimeException {
        AccountOpeningRequest request = new AccountOpeningRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setCnic(webServiceVO.getCnicNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setConsumerName(webServiceVO.getConsumerName());
        request.setAccountTitle(webServiceVO.getAccountTitle());
        request.setBirthPlace(webServiceVO.getBirthPlace());
        request.setPresentAddress(webServiceVO.getPresentAddress());
        request.setCnicStatus(webServiceVO.getCnicStatus());
        request.setCnicExpiry(webServiceVO.getCnicExpiry());
        request.setDob(webServiceVO.getDateOfBirth());
        request.setFatherHusbandName(webServiceVO.getFatherHusbandName());
        request.setMotherMaiden(webServiceVO.getMotherMaiden());
        request.setGender(webServiceVO.getGender());
        request.setChannelId(webServiceVO.getChannelId());
        request.setAccountType(webServiceVO.getAccountType());
        request.setTrackingId(webServiceVO.getTrackingId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getCnic() + request.getDateTime() + request.getRrn() + request.getMobileNumber() + request.getConsumerName() + request.getAccountTitle() + request.getBirthPlace() + request.getPresentAddress() + request.getCnicStatus() + request.getCnicExpiry() + request.getDob() + request.getFatherHusbandName() + request.getMotherMaiden() + request.getGender() + request.getChannelId() + request.getAccountType() + request.getTrackingId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AccountOpeningResponse response = null;
        try {
            response = fonePayIntegration.accountOpening(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO conventionalAccountOpening(WebServiceVO webServiceVO) throws RuntimeException {
        ConventionalAccountOpening request = new ConventionalAccountOpening();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setCnic(webServiceVO.getCnicNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setConsumerName(webServiceVO.getConsumerName());
//        request.setMpin(webServiceVO.getMobilePin());
        request.setCnicExpiry(webServiceVO.getCnicExpiry());
        request.setDob(webServiceVO.getDateOfBirth());
        request.setCustomerPhoto(webServiceVO.getCustomerPhoto());
        request.setCnicFrontPhoto(webServiceVO.getCnicFrontPhoto());
        request.setCnicBackPhoto(webServiceVO.getCnicBackPhoto());
        request.setSignaturePhoto(webServiceVO.getSignaturePhoto());
        request.setTermsPhoto(webServiceVO.getTermsPhoto());
        request.setChannelId(webServiceVO.getChannelId());
        request.setAccountType(webServiceVO.getAccountType());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getCnic() + request.getDateTime() + request.getRrn() + request.getMobileNumber() + request.getConsumerName() + request.getCnicExpiry() + request.getDob() + request.getCustomerPhoto() + request.getCnicFrontPhoto() + request.getCnicBackPhoto() + request.getSignaturePhoto() + request.getTermsPhoto() + request.getChannelId() + request.getAccountType());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        ConventionalAccountOpeningResponse response = null;
        try {
            response = fonePayIntegration.conventionalAccountOpening(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO paymentInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        PaymentInquiryRequest request = new PaymentInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());
        request.setAmount(webServiceVO.getTransactionAmount());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setTransactionType(webServiceVO.getTransactionType());
        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getAmount() + request.getRrn() + request.getTransactionType() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        PaymentInquiryResponse response = null;
        try {
            response = fonePayIntegration.paymentInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
            webServiceVO.setCharges(response.getCharges());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO payment(WebServiceVO webServiceVO) throws RuntimeException {
        PaymentRequest request = new PaymentRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setAccountNumber(webServiceVO.getAccountNo1());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setAmount(webServiceVO.getTransactionAmount());
        request.setCharges(webServiceVO.getCharges());
        request.setTransactionType(webServiceVO.getTransactionType());
        request.setmPin(webServiceVO.getMobilePin());
        request.setTerminalId(webServiceVO.getTerminalId());
        request.setPaymentType(webServiceVO.getPaymentType());
        request.setChannelId(webServiceVO.getChannelId());

        request.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
        request.setSettlementType(webServiceVO.getSettlementType());

        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getmPin() + request.getAccountNumber() + request.getDateTime() + request.getRrn() + request.getAmount() + request.getCharges() + request.getTransactionType() + request.getTerminalId() + request.getPaymentType() + request.getChannelId() + request.getTransactionCode() + request.getSettlementType());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        PaymentResponse response = null;
        try {
            response = fonePayIntegration.paymentRequest(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setMicrobankTransactionCode(response.getTransactionCode());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO paymentReversal(WebServiceVO webServiceVO) throws RuntimeException {
        PaymentReversalRequest request = new PaymentReversalRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
        request.setDateTime(webServiceVO.getDateTime());

        logger.info("Reversal Number " + webServiceVO.getRetrievalReferenceNumber());

        request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        request.setChannelId(webServiceVO.getChannelId());

        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getTransactionCode() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        PaymentReversalResponse response = null;
        try {
            response = fonePayIntegration.paymentReversal(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO otpVerification(WebServiceVO webServiceVO) throws RuntimeException {
        OtpVerificationRequest request = new OtpVerificationRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setOtpPin(webServiceVO.getOtpPin());
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setCnic(webServiceVO.getCnicNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getOtpPin() + request.getMobileNumber() + request.getCnic() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        OtpVerificationResponse response = null;
        try {
            response = fonePayIntegration.otpVerification(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO cardTagging(WebServiceVO webServiceVO) throws RuntimeException {
        CardTagging request = new CardTagging();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setCardExpiry(webServiceVO.getCardExpiry());
        request.setCardNumber(webServiceVO.getCardNo());
        request.setFirstName(webServiceVO.getFirstName());
        request.setLastName(webServiceVO.getLastName());
        request.setDateTime(webServiceVO.getDateTime());
        request.setTransactionId(webServiceVO.getTransactionId());
        request.setMobileNumber(webServiceVO.getMobileNo());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setCnic(webServiceVO.getCnicNo());
        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getCardExpiry() + request.getCardNumber() + request.getFirstName() + request.getLastName() + request.getDateTime() + request.getTransactionId() + request.getMobileNumber() + request.getCnic() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        CardTaggingResponse response = null;
        try {
            response = fonePayIntegration.cardTagging(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO accountLinkDeLink(WebServiceVO webServiceVO) throws RuntimeException {
        AccountLinkDeLink request = new AccountLinkDeLink();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setTransactionType(webServiceVO.getTransactionType());
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setCnic(webServiceVO.getCnicNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());


        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getTransactionType() + request.getMobileNumber() + request.getCnic() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AccountLinkDeLinkResponse response = null;
        try {
            response = fonePayIntegration.accountLinkDeLink(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO setCardStatus(WebServiceVO webServiceVO) throws RuntimeException {
        SetCardStatus request = new SetCardStatus();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setTransactionType(webServiceVO.getTransactionType());
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setCnic(webServiceVO.getCnicNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setCardNo(webServiceVO.getCardNo());
        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getTransactionType() + request.getMobileNumber() + request.getCnic() + request.getDateTime() + request.getRrn() + request.getCardNo() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        SetCardStatusResponse response = null;
        try {
            response = fonePayIntegration.setCardStatus(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO challanPaymentInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        ChallanPaymentInquiryRequest request = new ChallanPaymentInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        ChallanPaymentInquiryResponse response = null;
        try {
            response = fonePayIntegration.challanPaymentInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO challanPayment(WebServiceVO webServiceVO) throws RuntimeException {
        ChallanPaymentRequest request = new ChallanPaymentRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        ChallanPaymentResponse response = null;
        try {
            response = fonePayIntegration.challanPayment(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO debitCardIssuanceInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        DebitCardIssuanceInquiryRequest request = new DebitCardIssuanceInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        DebitCardIssuanceInquiryResponse response = null;
        try {
            response = fonePayIntegration.debitCardIssuanceInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO debitCardIssuance(WebServiceVO webServiceVO) throws RuntimeException {
        DebitCardIssuanceRequest request = new DebitCardIssuanceRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        DebitCardIssuanceResponse response = null;
        try {
            response = fonePayIntegration.debitCardIssuance(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO walletToCnic(WebServiceVO webServiceVO) throws RuntimeException {
        WalletToCnicRequest request = new WalletToCnicRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        WalletToCnicResponse response = null;
        try {
            response = fonePayIntegration.walleToCnic(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO walletToCnicInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        WalletToCnicInquiryRequest request = new WalletToCnicInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        WalletToCnicInquiryResponse response = null;
        try {
            response = fonePayIntegration.walletToCnicInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO hraRegistrationInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        HRARegistrationInquiryRequest request = new HRARegistrationInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        HRARegistrationInquiryResponse response = null;
        try {
            response = fonePayIntegration.hraRegistrationInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO hraRegistration(WebServiceVO webServiceVO) throws RuntimeException {
        HRARegistrationRequest request = new HRARegistrationRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        HRARegistrationResponse response = null;
        try {
            response = fonePayIntegration.hraRegistration(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO walletToCoreInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        WalletToCoreInquiryRequest request = new WalletToCoreInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        WalletToCoreInquiryResponse response = null;
        try {
            response = fonePayIntegration.walletToCoreInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO walletToCore(WebServiceVO webServiceVO) throws RuntimeException {
        WalletToCoreRequest request = new WalletToCoreRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        WalletToCoreResponse response = null;
        try {
            response = fonePayIntegration.walletToCore(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO hraToWalletInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        HRAToWalletInquiryRequest request = new HRAToWalletInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        HRAToWalletInquiryResponse response = null;
        try {
            response = fonePayIntegration.hraToWalletInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO hraToWallet(WebServiceVO webServiceVO) throws RuntimeException {
        HRAToWalletRequest request = new HRAToWalletRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        HRAToWalletResponse response = null;
        try {
            response = fonePayIntegration.hraToWallet(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO debitInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        DebitInquiryRequest request = new DebitInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        DebitInquiryResponse response = null;
        try {
            response = fonePayIntegration.debitInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO debit(WebServiceVO webServiceVO) throws RuntimeException {
        DebitRequest request = new DebitRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        DebitResponse response = null;
        try {
            response = fonePayIntegration.debit(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentBillPaymentInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        AgentBillPaymentInquiryRequest request = new AgentBillPaymentInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentBillPaymentInquiryResponse response = null;
        try {
            response = fonePayIntegration.agentBillPaymentInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentBillPayment(WebServiceVO webServiceVO) throws RuntimeException {
        AgentBillPaymentRequest request = new AgentBillPaymentRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentBillPaymentResponse response = null;
        try {
            response = fonePayIntegration.agentBillPayment(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO creditInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        CreditInquiryRequest request = new CreditInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        CreditInquiryResponse response = null;
        try {
            response = fonePayIntegration.creditInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO credit(WebServiceVO webServiceVO) throws RuntimeException {
        CreditRequest request = new CreditRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        CreditResponse response = null;
        try {
            response = fonePayIntegration.credit(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO hraCashWithdrawalInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        HRACashWithdrawalInquiryRequest request = new HRACashWithdrawalInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        HRACashWithdrawalInquiryResponse response = null;
        try {
            response = fonePayIntegration.hraCashWithdrawalInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO hraCashWithdrawal(WebServiceVO webServiceVO) throws RuntimeException {
        HRACashWithdrawalRequest request = new HRACashWithdrawalRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        HRACashWithdrawalResponse response = null;
        try {
            response = fonePayIntegration.hraCashWithdrawal(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO loginPin(WebServiceVO webServiceVO) throws RuntimeException {
        LoginPinRequest request = new LoginPinRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        LoginPinResponse response = null;
        try {
            response = fonePayIntegration.loginPin(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO loginPinChange(WebServiceVO webServiceVO) throws RuntimeException {
        LoginPinChangeRequest request = new LoginPinChangeRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        LoginPinChangeResponse response = null;
        try {
//            response = fonePayIntegration.loginPinChange(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO resetPin(WebServiceVO webServiceVO) throws RuntimeException {
        ResetPinRequest request = new ResetPinRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        ResetPinResponse response = null;
        try {
//            response = fonePayIntegration.resetPin(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO advanceLoanSalary(WebServiceVO webServiceVO) throws RuntimeException {
        AdvanceLoanSalaryRequest request = new AdvanceLoanSalaryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AdvanceLoanSalaryResponse response = null;
        try {
            response = fonePayIntegration.advanceLoanSalary(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO smsGeneration(WebServiceVO webServiceVO) throws RuntimeException {
        SmsGenerationRequest request = new SmsGenerationRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        SmsGenerationResponse response = null;
        try {
            response = fonePayIntegration.smsGeneration(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentAccountLogin(WebServiceVO webServiceVO) throws RuntimeException {
        AgentAccountLoginRequest request = new AgentAccountLoginRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentAccountLoginResponse response = null;
        try {
            response = fonePayIntegration.agentAccountLogin(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentLoginPinGeneration(WebServiceVO webServiceVO) throws RuntimeException {
        AgentLoginPinGenerationRequest request = new AgentLoginPinGenerationRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentLoginPinGenerationResponse response = null;
        try {
            response = fonePayIntegration.agentLoginPinGeneration(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentLoginPinReset(WebServiceVO webServiceVO) throws RuntimeException {
        AgentLoginPinResetRequest request = new AgentLoginPinResetRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentLoginPinResetResponse response = null;
        try {
            response = fonePayIntegration.agentLoginPinReset(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentMpinGeneration(WebServiceVO webServiceVO) throws RuntimeException {
        AgentMpinGenerationRequest request = new AgentMpinGenerationRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentMpinGenerationResponse response = null;
        try {
            response = fonePayIntegration.agentMpinGeneration(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentMpinReset(WebServiceVO webServiceVO) throws RuntimeException {
        AgentMpinResetRequest request = new AgentMpinResetRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentMpinResetResponse response = null;
        try {
            response = fonePayIntegration.agentMpinReset(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

//    public WebServiceVO agentMpinVerification(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentMpinVerificationRequest request = new AgentMpinVerificationRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentMpinVerificationResponse response = null;
//        try {
//            response = fonePayIntegration.agentMpinVerification(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }

    public WebServiceVO agentBalanceInquriy(WebServiceVO webServiceVO) throws RuntimeException {
        AgentBalanceInquiryRequest request = new AgentBalanceInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentBalanceInquiryResponse response = null;
        try {
            response = fonePayIntegration.agentBalanceInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

//    public WebServiceVO agentToAgentInquriy(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentToAgentInquiryRequest request = new AgentToAgentInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentToAgentInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentToAgentInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentToAgentPayment(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentToAgentPaymentRequest request = new AgentToAgentPaymentRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentToAgentPaymentResponse response = null;
//        try {
//            response = fonePayIntegration.agentToAgentPayment(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentAccountOpening(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentAccountOpeningRequest request = new AgentAccountOpeningRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentAccountOpeningResponse response = null;
//        try {
//            response = fonePayIntegration.agentAccountOpening(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentUpgradeAccountInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentUpgradeAccountInquiryRequest request = new AgentUpgradeAccountInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentUpgradeAccountInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentUpgradeAccountInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentUpgradeAccount(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentUpgradeAccountRequest request = new AgentUpgradeAccountRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentUpgradeAccountResponse response = null;
//        try {
//            response = fonePayIntegration.agentUpgradeAccount(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentCashInInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentCashInInquiryRequest request = new AgentCashInInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentCashInInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentCashInInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentCashIn(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentCashInRequest request = new AgentCashInRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentCashInResponse response = null;
//        try {
//            response = fonePayIntegration.agentCashIn(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentCashOutInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentCashOutInquiryRequest request = new AgentCashOutInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentCashOutInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentCashOutInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentCashOut(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentCashOutRequest request = new AgentCashOutRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentCashOutResponse response = null;
//        try {
//            response = fonePayIntegration.agentCashOut(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentWalletToWalletInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentWalletToWalletInquiryRequest request = new AgentWalletToWalletInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentWalletToWalletInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentWalletToWalletInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentWalletToWalletPayment(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentWalletToWalletPaymentRequest request = new AgentWalletToWalletPaymentRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentWalletToWalletPaymentResponse response = null;
//        try {
//            response = fonePayIntegration.agentWalletToWalletPayment(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentWalletToCnicInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentWalletToCnicInquiryRequest request = new AgentWalletToCnicInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentWalletToCnicInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentWalletToCnicInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentWalletToCnicPayment(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentWalletToCnicPaymentRequest request = new AgentWalletToCnicPaymentRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentWalletToCnicPaymentResponse response = null;
//        try {
//            response = fonePayIntegration.agentWalletToCnicPayment(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }

    public WebServiceVO agentIbftInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        AgentIbftInquiryRequest request = new AgentIbftInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentIbftInquiryResponse response = null;
        try {
            response = fonePayIntegration.agentIbftInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentIbftPayment(WebServiceVO webServiceVO) throws RuntimeException {

        AgentIbftPaymentRequest request = new AgentIbftPaymentRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentIbftPaymentResponse response = null;
        try {
            response = fonePayIntegration.agentIbftPayment(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

//    public WebServiceVO agentRetailPaymentInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentRetailPaymentInquiryRequest request = new AgentRetailPaymentInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentRetailPaymentInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentRetailPaymentInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentRetailPayment(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentRetailPaymentRequest request = new AgentRetailPaymentRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentRetailPaymentResponse response = null;
//        try {
//            response = fonePayIntegration.agentRetailPayment(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentWalletToCoreInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentWalletToCoreInquiryRequest request = new AgentWalletToCoreInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentWalletToCoreInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentWalletToCoreInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentWalletToCorePayment(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentWalletToCorePaymentRequest request = new AgentWalletToCorePaymentRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentWalletToCorePaymentResponse response = null;
//        try {
//            response = fonePayIntegration.agentWalletToCorePayment(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentReceiveMoneyInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentReceiveMoneyInquiryRequest request = new AgentReceiveMoneyInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentReceiveMoneyInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentReceiveMoneyInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentReceiveMoneyPayment(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentReceiveMoneyPaymentRequest request = new AgentReceiveMoneyPaymentRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentReceiveMoneyPaymentResponse response = null;
//        try {
//            response = fonePayIntegration.agentReceiveMoneyPayment(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }

//    public WebServiceVO agentCnicToCnicInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentCnicToCnicInquiryRequest request = new AgentCnicToCnicInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentCnicToCnicInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentCnicToCnicInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentCnicToCnicPayment(WebServiceVO webServiceVO) throws RuntimeException
//    {
//
//        AgentCnicToCnicPaymentRequest request = new AgentCnicToCnicPaymentRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentCnicToCnicPaymentResponse response = null;
//        try {
//            response = fonePayIntegration.agentCnicToCnicPayment(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }

//    public WebServiceVO agentHRARegistrationInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentHRARegistrationInquiryRequest request = new AgentHRARegistrationInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentHRARegistrationInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentHRARegistrationInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentHRARegistration(WebServiceVO webServiceVO) throws RuntimeException {
//        AgentHRARegistrationRequest request = new AgentHRARegistrationRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentHRARegistrationResponse response = null;
//        try {
//            response = fonePayIntegration.agentHRARegistration(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }
//
//    public WebServiceVO agentCnicToCoreInquiry(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentCnicToCoreInquiryRequest request = new AgentCnicToCoreInquiryRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentCnicToCoreInquiryResponse response = null;
//        try {
//            response = fonePayIntegration.agentCnicToCoreInquiry(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }

//    public WebServiceVO agentCnicToCorePayment(WebServiceVO webServiceVO) throws RuntimeException {
//
//        AgentCnicToCorePaymentRequest request = new AgentCnicToCorePaymentRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        AgentCnicToCorePaymentResponse response = null;
//        try {
//            response = fonePayIntegration.agentCnicToCorePayment(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }

    public WebServiceVO agentCashDepositInquiry(WebServiceVO webServiceVO) throws RuntimeException {

        AgentCashDepositInquiryRequest request = new AgentCashDepositInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentCashDepositInquiryResponse response = null;
        try {
            response = fonePayIntegration.agentCashDepositInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentCashDepositPayment(WebServiceVO webServiceVO) throws RuntimeException {

        AgentCashDepositPaymentRequest request = new AgentCashDepositPaymentRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentCashDepositPaymentResponse response = null;
        try {
            response = fonePayIntegration.agentCashDepositPayment(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentCashWithdrawalInquiry(WebServiceVO webServiceVO) throws RuntimeException {

        AgentCashWithdrawalInquiryRequest request = new AgentCashWithdrawalInquiryRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentCashWithdrawalInquiryResponse response = null;
        try {
            response = fonePayIntegration.agentCashWithdrawalInquiry(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO agentCashWithdrawalPayment(WebServiceVO webServiceVO) throws RuntimeException {

        AgentCashWithdrawalPaymentRequest request = new AgentCashWithdrawalPaymentRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentCashWithdrawalPaymentResponse response = null;
        try {
            response = fonePayIntegration.agentCashWithdrawalPayment(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO mpinVerification(WebServiceVO webServiceVO) throws RuntimeException {

        MpinVerificationRequest request = new MpinVerificationRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        MpinVerificationResponse response = null;
        try {
            response = fonePayIntegration.mpinVerification(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO segmentList(WebServiceVO webServiceVO) throws RuntimeException {

        SegmentListRequest request = new SegmentListRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        SegmentListResponse response = null;
        try {
            response = fonePayIntegration.segmentList(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO catalogList(WebServiceVO webServiceVO) throws RuntimeException {

        AgentCatalogsRequest request = new AgentCatalogsRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AgentCatalogResponse response = null;
        try {
            response = fonePayIntegration.catalogList(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO l2AccountOpening(WebServiceVO webServiceVO) throws RuntimeException {

        L2AccountOpeningRequest request = new L2AccountOpeningRequest();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        L2AccountOpeningResponse response = null;
        try {
            response = fonePayIntegration.l2AccountOpening(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

    public WebServiceVO l2AccountUpgrade(WebServiceVO webServiceVO) throws RuntimeException {

//        L2AccountUpgradeRequest request = new L2AccountUpgradeRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
////        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        L2AccountUpgradeResponse response = null;
//        try {
//            response = fonePayIntegration.l2AccountUpgrade(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }

        return webServiceVO;
    }

    public WebServiceVO accountDetail(WebServiceVO webServiceVO) throws RuntimeException {

        AccountDetails request = new AccountDetails();
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
        request.setDateTime(webServiceVO.getDateTime());

        Random ran = new Random();
        String stan = Integer.toString(100000 + ran.nextInt(899999));
        request.setRrn(FieldUtil.buildRRN(stan));

        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
            request.setRrn(webServiceVO.getRetrievalReferenceNumber());

        webServiceVO.setRetrievalReferenceNumber(request.getRrn());

        request.setChannelId(webServiceVO.getChannelId());
        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getDateTime() + request.getRrn() + request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        request.setHashData(sha256hex);
        AccountDetailResponse response = null;
        try {
            response = fonePayIntegration.accountDetail(request);
        } catch (Exception e) {
            logger.error("ERROR: WebService Exception ", e);
        }
        if (response != null) {
            webServiceVO.setResponseCode(response.getResponseCode());
            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
        } else {
            webServiceVO.setResponseCode("911");
            webServiceVO.setResponseCodeDescription("Web Server Exception");
        }

        return webServiceVO;
    }

//    public WebServiceVO customerNameUpdate(WebServiceVO webServiceVO) throws RuntimeException {
//
//        CustomerNameUpdateRequest request = new CustomerNameUpdateRequest();
//        request.setUserName(USER_NAME);
//        request.setPassword(PASSWORD);
//        request.setMobileNumber(webServiceVO.getMobileNo());
//        request.setDateTime(webServiceVO.getDateTime());
//
//        Random ran = new Random();
//        String stan = Integer.toString(100000 + ran.nextInt(899999));
//        request.setRrn(FieldUtil.buildRRN(stan));
//
//        if (StringUtils.isNotEmpty(webServiceVO.getRetrievalReferenceNumber()))
//            request.setRrn(webServiceVO.getRetrievalReferenceNumber());
//
//        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
//
//        request.setChannelId(webServiceVO.getChannelId());
//        StringBuffer stringText = new StringBuffer(USER_NAME + PASSWORD + request.getMobileNumber() + request.getDateTime() + request.getRrn() + request.getChannelId());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        request.setHashData(sha256hex);
//        CustomerNameUpdateResponse response = null;
//        try {
//            response = fonePayIntegration.customerNameUpdate(request);
//        } catch (Exception e) {
//            logger.error("ERROR: WebService Exception ", e);
//        }
//        if (response != null) {
//            webServiceVO.setResponseCode(response.getResponseCode());
//            webServiceVO.setResponseCodeDescription(response.getResponseDescription());
//            webServiceVO.setRetrievalReferenceNumber(response.getRrn());
//        } else {
//            webServiceVO.setResponseCode("911");
//            webServiceVO.setResponseCodeDescription("Web Server Exception");
//        }
//
//        return webServiceVO;
//    }

}

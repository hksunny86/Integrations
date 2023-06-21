package com.inov8.integration.middleware.controller.hostController;

import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.service.hostService.HostIntegrationService;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.XMLUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.jws.WebService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

@WebService(serviceName = "JsBLBIntegrationService", portName = "JsBLBIntegrationPort"
        , endpointInterface = "com.inov8.integration.middleware.controller.hostController.JsBLBIntegration", targetNamespace = "http://tempuri.org/")
@Controller("JsBLBIntegration")
public class JsBLBIntegrationImpl implements JsBLBIntegration {


    private static final Logger logger = LoggerFactory.getLogger(JsBLBIntegrationImpl.class);
    private static String loginPinMatch = ConfigReader.getInstance().getProperty("loginPinMatch", "");
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");
    @Autowired
    HostIntegrationService integrationService;

    public JsBLBIntegrationImpl() {
        super();
    }

    public static String escapeUnicode(String input) {
        logger.info("Input string is " + input);
        StringBuilder b = new StringBuilder(input.length());
        Formatter f = new Formatter(b);
        for (char c : input.toCharArray()) {
            if (c < 128) {
                b.append(c);
            } else {
                f.format("\\%04x", (int) c);
            }
        }
        logger.info("Output string is " + b.toString());
        return b.toString();
    }

    @Override
    public VerifyAccountResponse verifyAccount(VerifyAccountRequest request) {
        long start = System.currentTimeMillis();
        VerifyAccountResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Account Verify Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
//        logger.info("Start Processing Account Verify Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getMobileNumber() +
                        request.getRrn() +
                        request.getTransactionType() +
                        request.getChannelId() +
                        request.getReserved1() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (sha256hex.equalsIgnoreCase(request.getHashData())) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {

                try {
                    HostRequestValidator.validateVerifyAccount(request);
                    response = integrationService.verifyAccount(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR ACCOUNT VERIFY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Verify Account TRANSACTION AUTHENTICATION *********");
                response = new VerifyAccountResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR ACCOUNT VERIFY TRANSACTION *********");
            response = new VerifyAccountResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Account Verify Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }


    @Override
    public VerifyLoginAccountResponse verifyLoginAccount(VerifyLoginAccountRequest request) {
        long start = System.currentTimeMillis();
        VerifyLoginAccountResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Account Verify Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getDateTime() +
                        request.getMobileNumber() +
                        request.getRrn() +
                        request.getChannelId() +
                        request.getReserved1() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (sha256hex.equalsIgnoreCase(request.getHashData())) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {

                try {
                    HostRequestValidator.validateVerifyLoginAccount(request);
                response = integrationService.verifyLoginAccountResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR ACCOUNT VERIFY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Verify Account TRANSACTION AUTHENTICATION *********");
                response = new VerifyLoginAccountResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR ACCOUNT VERIFY TRANSACTION *********");
            response = new VerifyLoginAccountResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Account Verify Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AccountOpeningResponse accountOpening(AccountOpeningRequest request) {
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException var12) {
            var12.printStackTrace();
        }
        long start = System.currentTimeMillis();
        AccountOpeningResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Account Opening Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
//        logger.info("Start Processing Account Opening Transaction Request with {}", requestXML);

        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getMobileNumber() +
                        request.getRrn() +
                        request.getConsumerName() +
                        request.getAccountTitle() +
                        request.getBirthPlace() +
                        request.getPresentAddress() +
                        request.getCnicStatus() +
                        request.getCnicExpiry() +
                        request.getDob() +
                        request.getFatherHusbandName() +
                        request.getMotherMaiden() +
                        request.getGender() +
                        request.getChannelId() +
                        request.getAccountType() +
                        request.getTrackingId() +
                        request.getCnicIssuanceDate() +
                        request.getEmailAddress() +
                        request.getMobileNetwork()
                        + request.getReserved()
                        + request.getReserved2()
                        + request.getReserved3()
                        + request.getReserved4()
                        + request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());

        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAccountOpening(request);
                    response = integrationService.accountOpening(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR ACCOUNT OPENING TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Account Opening TRANSACTION AUTHENTICATION *********");
                response = new AccountOpeningResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR ACCOUNT OPENING TRANSACTION *********");
            response = new AccountOpeningResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Account Opening Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public ConventionalAccountOpeningResponse conventionalAccountOpening(ConventionalAccountOpening request) {
        long start = System.currentTimeMillis();
        ConventionalAccountOpeningResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Conventional Account Opening  Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
//        logger.info("Start Processing Conventional Account Opening Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getRrn() +
                        request.getMobileNumber() +
                        request.getConsumerName() +
                        request.getCnicExpiry() +
                        request.getDob() +
                        request.getCustomerPhoto() +
                        request.getCnicFrontPhoto() +
                        request.getCnicBackPhoto() +
                        request.getSignaturePhoto() +
                        request.getTermsPhoto() +
                        request.getChannelId() +
                        request.getAccountType());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());

        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateConventionalAccountOpening(request);
                    response = integrationService.conventionalAccountOpening(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR CONVENTIONAL ACCOUNT OPENING TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Conventional Account Opening TRANSACTION AUTHENTICATION *********");
                response = new ConventionalAccountOpeningResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR CONVENTIONAL ACCOUNT OPENING TRANSACTION *********");
            response = new ConventionalAccountOpeningResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Conventional Account Opening Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public PaymentInquiryResponse paymentInquiry(PaymentInquiryRequest request) {
        long start = System.currentTimeMillis();
        PaymentInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Payment Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
//        logger.info("Start Processing Payment Inquiry Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getMobileNumber() +
                        request.getDateTime() +
                        request.getAmount() +
                        request.getRrn() +
                        request.getTransactionType() +
                        request.getChannelId() +
                        request.getReserved1() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validatePaymentInquiry(request);
                    response = integrationService.paymentInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Payment Inquiry TRANSACTION AUTHENTICATION *********");
                response = new PaymentInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
            response = new PaymentInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Account Inquiry Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public PaymentResponse paymentRequest(PaymentRequest request) {

        long start = System.currentTimeMillis();
        PaymentResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        StringBuffer stringText = null;
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Payment Request Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Payment Request Transaction Request with {}", requestXML);
        if (request.getChannelId().equalsIgnoreCase("FONEPAY")) {
            stringText = new StringBuffer(
                    request.getUserName() +
                            request.getPassword() +
                            request.getAccountNumber() +
                            request.getDateTime() +
                            request.getRrn() +
                            request.getAmount() +
                            request.getCharges() +
                            request.getTransactionType()
                            + request.getmPin()
                            + request.getTerminalId() +
                            request.getPaymentType() + request.getChannelId() +
                            request.getTransactionCode() +
                            request.getSettlementType());
        } else {

            stringText = new StringBuffer(
                    request.getUserName() +
                            request.getAccountNumber() +
                            request.getPassword() +
                            request.getmPin() +
                            request.getOtp() +
                            request.getDateTime() +
                            request.getRrn() +
                            request.getAmount() +
                            request.getCharges() +
                            request.getTransactionType() +
                            request.getTerminalId() +
                            request.getPaymentType() +
                            request.getSettlementType() +
                            request.getChannelId() +
                            request.getTransactionCode() +
                            request.getReserved1() +
                            request.getReserved2() +
                            request.getReserved3() +
                            request.getReserved4() +
                            request.getReserved5()
            );
        }
//        if (StringUtils.isNotEmpty(request.getSettlementType())) stringText.append(request.getSettlementType());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        StringBuffer stringTextBuffer = new StringBuffer(
                request.getUserName() +
                        request.getAccountNumber() +
                        request.getPassword() +
                        request.getmPin() +
                        request.getOtp() +
                        request.getDateTime() +
                        request.getRrn() +
                        request.getAmount() +
                        request.getCharges() +
                        request.getTransactionType() +
                        request.getTerminalId() +
                        request.getPaymentType() +
                        request.getSettlementType() +
                        request.getChannelId() +
                        request.getTransactionCode() +
                        request.getReserved1() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5());
        if (StringUtils.isNotEmpty(request.getSettlementType())) stringTextBuffer.append(request.getSettlementType());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringTextBuffer.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validatePaymentRequest(request);
                    response = integrationService.paymentRequest(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR PAYMENT REQUEST TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());

            } else {
                logger.info("******* DEBUG LOGS FOR Payment TRANSACTION AUTHENTICATION *********");
                response = new PaymentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR PAYMENT REQUEST TRANSACTION *********");
            response = new PaymentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Payment Response Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public PaymentReversalResponse paymentReversal(PaymentReversalRequest request) {
        long start = System.currentTimeMillis();
        PaymentReversalResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Payment Reversal Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Payment Reversal Transaction Request with {}", requestXML);

        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getTransactionCode() +
                        request.getDateTime() +
                        request.getRrn() +
                        request.getChannelId());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {

                try {
                    HostRequestValidator.validatePaymentReversalRequest(request);
                    response = integrationService.paymentReversal(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR PAYMENT REVERSAL TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Payment Reversal TRANSACTION AUTHENTICATION *********");
                response = new PaymentReversalResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR PAYMENT REVERSAL TRANSACTION *********");
            response = new PaymentReversalResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Payment Reversal Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public OtpVerificationResponse otpVerification(OtpVerificationRequest request) {
        long start = System.currentTimeMillis();
        OtpVerificationResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing OTP Verification Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing OTP Verification Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getOtpPin() +
                        request.getMobileNumber() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getRrn() +
                        request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {

                try {
                    HostRequestValidator.validateOtpRequest(request);
                    response = integrationService.otpVerification(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR OTP VERIFICATION TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Otp Verification TRANSACTION AUTHENTICATION *********");
                response = new OtpVerificationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  OTP VERIFICATION TRANSACTION *********");
            response = new OtpVerificationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("OTP VERIFICATION Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public CardTaggingResponse cardTagging(CardTagging request) {
        long start = System.currentTimeMillis();
        CardTaggingResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Card Tagging Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Card Tagging Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getCardExpiry() +
                        request.getCardNumber() +
                        request.getFirstName() +
                        request.getLastName() +
                        request.getDateTime() +
                        request.getRrn() +
                        request.getTransactionId() +
                        request.getMobileNumber() +
                        request.getCnic() +
                        request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        logger.info("HasH value " + sha256hex);
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateCardTagging(request);
                    response = integrationService.cardTagging(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR CARD TAGGING TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Card Tagging TRANSACTION AUTHENTICATION *********");
                response = new CardTaggingResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  CARD TAGGING TRANSACTION *********");
            response = new CardTaggingResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Card Tagging Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AccountLinkDeLinkResponse accountLinkDeLink(AccountLinkDeLink request) {
        long start = System.currentTimeMillis();
        AccountLinkDeLinkResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Account Link De-Link Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Account Link De-Link Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getTransactionType() +
                        request.getMobileNumber() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getRrn() +
                        request.getChannelId() +
                        request.getOtp() +
                        request.getmPin() +
                        request.getReserved() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAccountLinkDeLink(request);
                    response = integrationService.accountLInkDeLink(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR ACCOUNT LINK DE-LINK TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Account Link De Link TRANSACTION AUTHENTICATION *********");
                response = new AccountLinkDeLinkResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  ACCOUNT LINK DE-LINK TRANSACTION *********");
            response = new AccountLinkDeLinkResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Account Link De-Link Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public SetCardStatusResponse setCardStatus(SetCardStatus request) {
        long start = System.currentTimeMillis();
        SetCardStatusResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Set Card Status Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Set Card Status Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getTransactionType() +
                        request.getMobileNumber() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getRrn() +
                        request.getCardNo() +
                        request.getChannelId());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateSetCardStatus(request);
                    response = integrationService.setCardStatus(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR SET CARD STATUS TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Set Card Status TRANSACTION AUTHENTICATION *********");
                response = new SetCardStatusResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  SET CARD STATUS TRANSACTION *********");
            response = new SetCardStatusResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Set Card Status Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public GenerateOtpResponse generateOTP(GenerateOtpRequest request) {
        long start = System.currentTimeMillis();
        GenerateOtpResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Generate OTP Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
//        logger.info("Start Processing Generate OTP Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getOtpPurpose());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateGenerateOTP(request);
                    response = integrationService.generateOTP(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR GENERATE OTP TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Generate Otp TRANSACTION AUTHENTICATION *********");
                response = new GenerateOtpResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  GENERATE OTP TRANSACTION *********");
            response = new GenerateOtpResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Generate OTP Request Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public BalanceInquiryResponse balanceInquiry(BalanceInquiryRequest request) {
        long start = System.currentTimeMillis();
        BalanceInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Balance Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Balance Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMpin())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getOtpPin())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateBalanceInquiry(request);
                    response = integrationService.balanceInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR BALANCE INQUIRY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Balance Inquiry TRANSACTION AUTHENTICATION *********");
                response = new BalanceInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  BALANCE INQUIRY TRANSACTION *********");
            response = new BalanceInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Balance Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public MiniStatementResponse miniStatement(MiniStatementRequest request) {
        long start = System.currentTimeMillis();
        MiniStatementResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Mini Statement Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Mini Statement Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMpin())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getOtpPin())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateMiniStatement(request);
                    response = integrationService.miniStatement(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR MINI STATEMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Mini Statement TRANSACTION AUTHENTICATION *********");
                response = new MiniStatementResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  MINI STATEMENT TRANSACTION *********");
            response = new MiniStatementResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Mini Statement Request  Processed in : {} ms {}", end, response);

        return response;
    }

    public BillPaymentInquiryResponse billPaymentInquiry(BillPaymentInquiryRequest request) {
        long start = System.currentTimeMillis();
        BillPaymentInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Bill Payment Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Bill Payment Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getProductId())
                .append(request.getConsumerNo())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateBillPaymentInquiry(request);
                    response = integrationService.billPaymentInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR BILL PAYMENT INQUIRY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Bill Payment Inquiry TRANSACTION AUTHENTICATION *********");
                response = new BillPaymentInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  BILL PAYMENT INQUIRY TRANSACTION *********");
            response = new BillPaymentInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Bill Payment Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

    public BillPaymentResponse billPayment(BillPaymentRequest request) {
        long start = System.currentTimeMillis();
        BillPaymentResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Bill Payment Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Bill Payment  Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getOtpPin())
                .append(request.getProductId())
                .append(request.getConsumerNo())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());


        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {

                    HostRequestValidator.validateBillPayment(request);
                    response = integrationService.billPayment(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {

                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR BILL PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Bill Payment TRANSACTION AUTHENTICATION *********");
                response = new BillPaymentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  BILL PAYMENT TRANSACTION *********");
            response = new BillPaymentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Bill Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    public CashInInquiryResponse cashInInquiry(CashInInquiryRequest request) {
        long start = System.currentTimeMillis();
        CashInInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Cash-in Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Cash In Inquiry  Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getAmount());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {

                    HostRequestValidator.cashInInquiry(request);
                    response = integrationService.cashInInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Cash In Inquiry TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Cash In Inquiry TRANSACTION AUTHENTICATION *********");
                response = new CashInInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Cash In Inquiry TRANSACTION *********");
            response = new CashInInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Cash In Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

    public CashInResponse cashIn(CashInRequest request) {
        long start = System.currentTimeMillis();
        CashInResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Cash-in Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Cash In  Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getAmount())
                .append(request.getPaymentMode())
                .append(request.getSegmentCode())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {

                    HostRequestValidator.cashIn(request);
                    response = integrationService.cashIn(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Cash In TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());

            } else {
                logger.info("******* DEBUG LOGS FOR  Cash In TRANSACTION AUTHENTICATION *********");
                response = new CashInResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Cash In TRANSACTION *********");
            response = new CashInResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Cash In Request  Processed in : {} ms {}", end, response);

        return response;
    }

    public TitleFetchResponse titleFetch(TitleFetchRequest request) {
        long start = System.currentTimeMillis();
        TitleFetchResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Title Fetch Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Title Fetch Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPaymentMode())
                .append(request.getSegmentCode())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());

        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {

                    HostRequestValidator.TitleFetch(request);
                    response = integrationService.titleFetch(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Title Fetch TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());

            } else {
                logger.info("******* DEBUG LOGS FOR Title Fetch TRANSACTION AUTHENTICATION *********");
                response = new TitleFetchResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Title Fetch TRANSACTION *********");
            response = new TitleFetchResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Title Fetch Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public CashInAgentResponse cashInAgent(CashInAgentRequest request) {
        long start = System.currentTimeMillis();
        CashInAgentResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Cash-in agent Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Title Fetch Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getAmount())
                .append(request.getPaymentMode())
                .append(request.getSegmentCode())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());

        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {

                    HostRequestValidator.cashInAgent(request);
                    response = integrationService.cashInAgent(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Cash In Agent TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());

            } else {
                logger.info("******* DEBUG LOGS FOR Cash In Agent TRANSACTION AUTHENTICATION *********");
                response = new CashInAgentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Cash In Agent TRANSACTION *********");
            response = new CashInAgentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Cash In Agent Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public CashOutInquiryResponse cashOutInquiry(CashOutInquiryRequest request) {
        long start = System.currentTimeMillis();
        CashOutInquiryResponse response = new CashOutInquiryResponse();
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Cash-out Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Customer Cash Out Inquiry Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());

        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {

                    HostRequestValidator.cashOutInquiry(request);
                    response = integrationService.cashOutInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Customer Cash Out Inquiry TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());

            } else {
                logger.info("******* DEBUG LOGS FOR Customer Cash Out Inquiry TRANSACTION AUTHENTICATION *********");
                response = new CashOutInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Cash In Agent TRANSACTION *********");
            response = new CashOutInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Customer Cash Out Inquiry Agent Request  Processed in : {} ms {}", end, response);

        return response;

    }

    @Override
    public CashOutResponse cashOut(CashOutRequest request) {
        long start = System.currentTimeMillis();
        CashOutResponse response = new CashOutResponse();
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Cash-out Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Customer Cash Out Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getTransactiondateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getAmount())
                .append(request.getCharges())
                .append(request.getMpin())
                .append(request.getOtp())
                .append(request.getPaymentType())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());

        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {

                    HostRequestValidator.cashOut(request);
                    response = integrationService.cashOut(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR  Customer Cash Out  TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());

            } else {
                logger.info("******* DEBUG LOGS FOR  Customer Cash Out  TRANSACTION AUTHENTICATION *********");
                response = new CashOutResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Cash In Agent TRANSACTION *********");
            response = new CashOutResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info(" Customer Cash Out  Request  Processed in : {} ms {}", end, response);

        return response;

    }

    @Override
    public MpinRegistrationResponse mpinRegistration(MpinRegistrationRequest request) {
        long start = System.currentTimeMillis();
        MpinRegistrationResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Mini Registration Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Generate OTP Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMpin())
                .append(request.getConfirmMpin())
                .append(request.getDateTime())
                .append(request.getMobileNumber())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateMpinRegistration(request);
                    response = integrationService.mpinRegistration(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR GENERATE OTP TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Generate Otp TRANSACTION AUTHENTICATION *********");
                response = new MpinRegistrationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  GENERATE OTP TRANSACTION *********");
            response = new MpinRegistrationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Generate OTP Request Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public MpinChangeResponse mpinChange(MpinChangeRequest request) {
        long start = System.currentTimeMillis();
        MpinChangeResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing MPIN Change Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Generate OTP Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getOldMpin())
                .append(request.getNewMpin())
                .append(request.getConfirmMpin())
                .append(request.getDateTime())
                .append(request.getMobileNumber())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateMpinChange(request);
                    response = integrationService.mpinChange(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR GENERATE OTP TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Generate Otp TRANSACTION AUTHENTICATION *********");
                response = new MpinChangeResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  GENERATE OTP TRANSACTION *********");
            response = new MpinChangeResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Generate OTP Request Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public WalletToWalletPaymentInquiryResponse walletToWalletPaymentInquery(WalletToWalletPaymentInquiryRequest request) {
        long start = System.currentTimeMillis();
        WalletToWalletPaymentInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Wallet to Wallet Payment Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Generate OTP Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReceiverMobileNumber())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateWalletToWalletPaymentInquiry(request);
                    response = integrationService.walletToWalletPaymentInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment Inquiry *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Wallet to wallet Payment Inquiry AUTHENTICATION *********");
                response = new WalletToWalletPaymentInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet to wallet Payment Inquiry *********");
            response = new WalletToWalletPaymentInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet to wallet Payment Inquiry in : {} ms {}", end, response);

        return response;
    }

    @Override
    public WalletToWalletPaymentResponse walletToWalletPayment(WalletToWalletPaymentRequest request) {
        long start = System.currentTimeMillis();
        WalletToWalletPaymentResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Wallet to Wallet Payment Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Wallet To Wallet Payment Response Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMpin())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getOtp())
                .append(request.getReceiverMobileNumber())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateWalletToWalletPayment(request);
                    response = integrationService.walletToWalletPaymentResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment AUTHENTICATION *********");
                response = new WalletToWalletPaymentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet to wallet Payment *********");
            response = new WalletToWalletPaymentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet to wallet Payment Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public UpgradeAccountInquiryResponse upgradeAccountInquiry(UpgradeAccountInquiryRequest request) {
        long start = System.currentTimeMillis();
        UpgradeAccountInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Upgrade Account Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Upgrade Account inquiry Response Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getChannelId())
                .append(request.getRrn())
                .append(request.getTerminalId())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateUpgradeAccountInquiry(request);
                    response = integrationService.upgradeAccountInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment AUTHENTICATION *********");
                response = new UpgradeAccountInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet to wallet Payment *********");
            response = new UpgradeAccountInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Generate OTP Request Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public UpgradeAccountResponse upgradeAccount(UpgradeAccountRequest request) {
        long start = System.currentTimeMillis();
        UpgradeAccountResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Upgrade Account Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Upgrade Account Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getMpin())
                .append(request.getCnic())
                .append(request.getFingerIndex())
                .append(request.getFingerTemplate())
                .append(request.getTemplateType())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateMpinUpgadeAccount(request);
                    response = integrationService.upgradeAccountResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Upgrade Account TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Upgrade Account TRANSACTION AUTHENTICATION *********");
                response = new UpgradeAccountResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Upgrade Account TRANSACTION *********");
            response = new UpgradeAccountResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Upgrade Account Request Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AccountStatusChangeResponse accountStatusChange(AccountStatusChangeRequest request) {
        long start = System.currentTimeMillis();
        AccountStatusChangeResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Account Status Change Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Wallet To Wallet Payment Response Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCnic())
                .append(request.getAccountStatus())
                .append(request.getChannelId())
                .append(request.getRrn())
                .append(request.getMpin())
                .append(request.getTerminalId())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAccountStatusChange(request);
                    response = integrationService.accountStatusChange(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment AUTHENTICATION *********");
                response = new AccountStatusChangeResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet to wallet Payment *********");
            response = new AccountStatusChangeResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Generate OTP Request Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public IbftTitleFetchResponse ibftTitleFetchResponse(IbftTitleFetchRequest request) {
        long start = System.currentTimeMillis();
        IbftTitleFetchResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing IBFT Title Fetch Response Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing IBFT Title Fetch Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getSenderMobileNumber())
                .append(request.getRecieverMobileNumber())
                .append(request.getSourceBankImd())
                .append(request.getDestinationBankIMD())
                .append(request.getDestinationAccount())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateIbftTitleFetch(request);
                    response = integrationService.ibftTitleFetchResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR IBFT Title Fetch*********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR IBFT Title Fetch AUTHENTICATION *********");
                response = new IbftTitleFetchResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  IBFT Title Fetch *********");
            response = new IbftTitleFetchResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info(" IBFT Title Fetch Request Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public IbftAdviceResponse ibftAdviceResponse(IbftAdviceRequest ibftAdviceRequest) {
        long start = System.currentTimeMillis();
        IbftAdviceResponse response = null;
        String requestXML = XMLUtil.convertRequest(ibftAdviceRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing IBFT Advice Response Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing IBFT Title Fetch Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(ibftAdviceRequest.getUserName())
                .append(ibftAdviceRequest.getPassword())
                .append(ibftAdviceRequest.getDateTime())
                .append(ibftAdviceRequest.getRrn())
                .append(ibftAdviceRequest.getChannelId())
                .append(ibftAdviceRequest.getTerminalId())
                .append(ibftAdviceRequest.getSenderMobileNumber())
                .append(ibftAdviceRequest.getRecieverMobileNumber())
                .append(ibftAdviceRequest.getSourceBankImd())
                .append(ibftAdviceRequest.getDestinationBankIMD())
                .append(ibftAdviceRequest.getDestinationAccount())
                .append(ibftAdviceRequest.getAmount())
                .append(ibftAdviceRequest.getmPIN())
                .append(ibftAdviceRequest.getOtpPin())
                .append(ibftAdviceRequest.getPurposeOfPayment())
                .append(ibftAdviceRequest.getSenderAccountTitle())
                .append(ibftAdviceRequest.getRecieverAccountTitle())
                .append(ibftAdviceRequest.getToBankName())
                .append(ibftAdviceRequest.getToBranchName())
                .append(ibftAdviceRequest.getBenificieryIban())
                .append(ibftAdviceRequest.getReserved())
                .append(ibftAdviceRequest.getReserved2())
                .append(ibftAdviceRequest.getReserved3())
                .append(ibftAdviceRequest.getReserved4())
                .append(ibftAdviceRequest.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (ibftAdviceRequest.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(ibftAdviceRequest.getUserName(), ibftAdviceRequest.getPassword(), ibftAdviceRequest.getChannelId())) {
                try {
                    HostRequestValidator.validateIbftAdvice(ibftAdviceRequest);
                    response = integrationService.ibftAdviceResponse(ibftAdviceRequest);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR IBFT Advice*********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR IBFT Advice AUTHENTICATION *********");
                response = new IbftAdviceResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  IBFT Advice *********");
            response = new IbftAdviceResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info(" IBFT Advice Request Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public ChallanPaymentResponse challanPayment(ChallanPaymentRequest request) {
        long start = System.currentTimeMillis();
        ChallanPaymentResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Challan Payment Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Challan Payment Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getChallanNumber())
                .append(request.getProductCode())
                .append(request.getChallanAmount())
                .append(request.getCommisionAmount())
                .append(request.getTotalAmount())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.challanPayment(request);
                    response = integrationService.challanPaymentResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR CHALLAN PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Challan Payment TRANSACTION AUTHENTICATION *********");
                response = new ChallanPaymentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  CHALLAN PAYMENT INQUIRY TRANSACTION *********");
            response = new ChallanPaymentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Challan Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public ChallanPaymentInquiryResponse challanPaymentInquiry(ChallanPaymentInquiryRequest request) {

        long start = System.currentTimeMillis();
        ChallanPaymentInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Challan Payment Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Challan Payment Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getChallanNumber())
                .append(request.getPinType())
                .append(request.getProductCode())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.challanPaymentInquiry(request);
                    response = integrationService.challanPaymentInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR CHALLAN PAYMENT INQUIRY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Challan Payment Inquiry TRANSACTION AUTHENTICATION *********");
                response = new ChallanPaymentInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  CHALLAN PAYMENT INQUIRY TRANSACTION *********");
            response = new ChallanPaymentInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Challan Payment Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public DebitCardIssuanceInquiryResponse debitCardIssuanceInquiry(DebitCardIssuanceInquiryRequest request) {
        long start = System.currentTimeMillis();
        DebitCardIssuanceInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Debit Card Issuance Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Debit Card Issuance Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPinType())
                .append(request.getTransactionType())
                .append(request.getCnic())
                .append(request.getCardType())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.debitCardIssuanceInquiry(request);
                    response = integrationService.debitCardIssuanceInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR DEBIT CARD ISSUANCE INQUIRY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Debit Card Issuance Inquiry TRANSACTION AUTHENTICATION *********");
                response = new DebitCardIssuanceInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  DEBIT CARD ISSUANCE INQUIRY TRANSACTION *********");
            response = new DebitCardIssuanceInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Debit Card Issuance Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public DebitCardIssuanceResponse debitCardIssuance(DebitCardIssuanceRequest request) {
        long start = System.currentTimeMillis();
        DebitCardIssuanceResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Debit Card Issuance Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Debit Card Issuance Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getTransactionType())
                .append(request.getCardType())
                .append(request.getCnic())
                .append(request.getCardDescription())
                .append(request.getMailingAddress())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.debitCardIssuance(request);
                    response = integrationService.debitCardIssuanceResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR DEBIT CARD ISSUANCE  TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Debit Card Issuance  TRANSACTION AUTHENTICATION *********");
                response = new DebitCardIssuanceResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  DEBIT CARD ISSUANCE  TRANSACTION *********");
            response = new DebitCardIssuanceResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Debit Card Issuance  Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public WalletToCnicResponse walleToCnic(WalletToCnicRequest request) {
        long start = System.currentTimeMillis();
        WalletToCnicResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Wallet to CNIC Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Wallet To CNIC Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getReceiverMobileNumber())
                .append(request.getReceiverCnic())
                .append(request.getAmount())
                .append(request.getPaymentPurpose())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.walletToCnic(request);
                    response = integrationService.walletToCnicResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR Wallet To CNIC PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Wallet To CNIC Payment TRANSACTION AUTHENTICATION *********");
                response = new WalletToCnicResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet To CNIC PAYMENT TRANSACTION *********");
            response = new WalletToCnicResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To CNIC Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public WalletToCnicInquiryResponse walletToCnicInquiry(WalletToCnicInquiryRequest request) {
        long start = System.currentTimeMillis();
        WalletToCnicInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Wallet to CNIC Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Wallet To CNIC Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReceiverMobileNumber())
                .append(request.getReceiverCnic())
                .append(request.getAmount())
                .append(request.getPaymentPurpose())
                .append(request.getPinType())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.walletToCnicInquiry(request);
                    response = integrationService.walletToCnicInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Wallet To CNIC Inquiry PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Wallet To CNIC Inquiry Payment TRANSACTION AUTHENTICATION *********");
                response = new WalletToCnicInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet To CNIC Inquiry PAYMENT TRANSACTION *********");
            response = new WalletToCnicInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To CNIC Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public HRARegistrationInquiryResponse hraRegistrationInquiry(HRARegistrationInquiryRequest request) {

        long start = System.currentTimeMillis();
        HRARegistrationInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing HRA Registration Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing HRA Registration Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getCnic())
                .append(request.getPinType())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.hraRegistrationInquiry(request);
                    response = integrationService.hraRegistrationInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR HRA Registration Inquiry PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR HRA Registration  Inquiry Payment TRANSACTION AUTHENTICATION *********");
                response = new HRARegistrationInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  HRA Registration  Inquiry PAYMENT TRANSACTION *********");
            response = new HRARegistrationInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("HRA Registration  Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public HRARegistrationResponse hraRegistration(HRARegistrationRequest request) {
        long start = System.currentTimeMillis();
        HRARegistrationResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing HRA Registration Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing HRA Registration Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getName())
                .append(request.getFatherName())
                .append(request.getDateOfBirth())
                .append(request.getCnic())
                .append(request.getSourceOfIncome())
                .append(request.getOccupation())
                .append(request.getPurposeOfAccount())
                .append(request.getKinName())
                .append(request.getKinMobileNumber())
                .append(request.getKinCnic())
                .append(request.getKinRelation())
                .append(request.getInternationalRemittanceLocation())
                .append(request.getOriginatorRelation())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.hraRegistration(request);
                    response = integrationService.hraRegistrationResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR HRA Registration PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR HRA Registration Payment TRANSACTION AUTHENTICATION *********");
                response = new HRARegistrationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  HRA Registration  Inquiry PAYMENT TRANSACTION *********");
            response = new HRARegistrationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("HRA Registration Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public WalletToCoreInquiryResponse walletToCoreInquiry(WalletToCoreInquiryRequest request) {
        long start = System.currentTimeMillis();
        WalletToCoreInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Wallet to Core Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Wallet To Core Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReceiverMobileNumber())
                .append(request.getReceiverAccountNumber())
                .append(request.getAmount())
                .append(request.getPinType())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.walletToCoreInquiry(request);
                    response = integrationService.walletToCoreInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Wallet To Core Inquiry PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Wallet To Core Inquiry Payment TRANSACTION AUTHENTICATION *********");
                response = new WalletToCoreInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet To Core Inquiry PAYMENT TRANSACTION *********");
            response = new WalletToCoreInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Core Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public WalletToCoreResponse walletToCore(WalletToCoreRequest request) {
        long start = System.currentTimeMillis();
        WalletToCoreResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Wallet to Core Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Wallet To Core Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getReceiverMobileNumber())
                .append(request.getReceiverAccountNumber())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.walletToCore(request);
                    response = integrationService.walletToCoreResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Wallet To Core PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Wallet To Core Payment TRANSACTION AUTHENTICATION *********");
                response = new WalletToCoreResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet To Core PAYMENT TRANSACTION *********");
            response = new WalletToCoreResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Core Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }


    @Override
    public WalletToCoreInquiryResponse FundwalletToCoreInquiry(WalletToCoreInquiryRequest request) {
        long start = System.currentTimeMillis();
        WalletToCoreInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Fund Wallet to Core Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Fund Wallet To Core Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReceiverMobileNumber())
                .append(request.getReceiverAccountNumber())
                .append(request.getAmount())
                .append(request.getPinType())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.walletToCoreInquiry(request);
                    response = integrationService.fundWalletToCoreInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Fund Wallet To Core Inquiry PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Fund Wallet To Core Inquiry Payment TRANSACTION AUTHENTICATION *********");
                response = new WalletToCoreInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Fund Wallet To Core Inquiry PAYMENT TRANSACTION *********");
            response = new WalletToCoreInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Fund Wallet To Core Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public WalletToCoreResponse FundwalletToCore(WalletToCoreRequest request) {
        long start = System.currentTimeMillis();
        WalletToCoreResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Fund Wallet to Core Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Fund Wallet To Core Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getReceiverMobileNumber())
                .append(request.getReceiverAccountNumber())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.walletToCore(request);
                    response = integrationService.fundWalletToCoreResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Fund Wallet To Core PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Fund  Wallet To Core Payment TRANSACTION AUTHENTICATION *********");
                response = new WalletToCoreResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Fund Wallet To Core PAYMENT TRANSACTION *********");
            response = new WalletToCoreResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Fund Wallet To Core Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public HRAToWalletInquiryResponse hraToWalletInquiry(HRAToWalletInquiryRequest request) {
        long start = System.currentTimeMillis();
        HRAToWalletInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing HRA to Wallet Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing HRA TO Wallet Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getAmount())
                .append(request.getPinType())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.hraToWalletInquiry(request);
                    response = integrationService.hraToWalletInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Wallet To Core Inquiry PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Wallet To Core Inquiry Payment TRANSACTION AUTHENTICATION *********");
                response = new HRAToWalletInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Wallet To Core Inquiry PAYMENT TRANSACTION *********");
            response = new HRAToWalletInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Core Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public HRAToWalletResponse hraToWallet(HRAToWalletRequest request) {
        long start = System.currentTimeMillis();
        HRAToWalletResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing HRA to Wallet Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing HRA TO Wallet Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.hraToWallet(request);
                    response = integrationService.hraToWalletResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR HRA To Wallet PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR HRA To Wallet Payment TRANSACTION AUTHENTICATION *********");
                response = new HRAToWalletResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  HRA To Wallet PAYMENT TRANSACTION *********");
            response = new HRAToWalletResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Core Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public DebitInquiryResponse debitInquiry(DebitInquiryRequest request) {
        long start = System.currentTimeMillis();
        DebitInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Debit Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Debit Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getProductId())
                .append(request.getPinType())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5())
                .append(request.getReserved6())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.debitInquiry(request);
                    response = integrationService.debitInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Debit Inquiry PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Debit Inquiry Payment TRANSACTION AUTHENTICATION *********");
                response = new DebitInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Debit Inquiry PAYMENT TRANSACTION *********");
            response = new DebitInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Core Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public DebitResponse debit(DebitRequest request) {
        long start = System.currentTimeMillis();
        DebitResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Debit Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Debit Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getProductId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5())
                .append(request.getReserved6())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.debit(request);
                    response = integrationService.debitResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Debit PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Debit Payment TRANSACTION AUTHENTICATION *********");
                response = new DebitResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Debit PAYMENT TRANSACTION *********");
            response = new DebitResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Debit Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentBillPaymentInquiryResponse agentBillPaymentInquiry(AgentBillPaymentInquiryRequest request) {
        long start = System.currentTimeMillis();
        AgentBillPaymentInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Agent Bill Payment Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing  Agent Bill Payment Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getProductId())
                .append(request.getPinType())
                .append(request.getAgentMobileNo())
                .append(request.getConsumerNo())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentBillPaymentInquiry(request);
                    response = integrationService.agentBillPaymentInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR AGENT BILL PAYMENT INQUIRY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR AGENT Bill Payment Inquiry TRANSACTION AUTHENTICATION *********");
                response = new AgentBillPaymentInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  AGENT BILL PAYMENT INQUIRY TRANSACTION *********");
            response = new AgentBillPaymentInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Bill Payment Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentBillPaymentResponse agentBillPayment(AgentBillPaymentRequest request) {
        long start = System.currentTimeMillis();
        AgentBillPaymentResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Agent Bill Payment Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Bill Payment Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getProductId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getAgentMobileNo())
                .append(request.getConsumerNo())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentBillPayment(request);
                    response = integrationService.agentBillPaymentResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Agent Bill PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Agent Bill Payment TRANSACTION AUTHENTICATION *********");
                response = new AgentBillPaymentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Agent Bill PAYMENT TRANSACTION *********");
            response = new AgentBillPaymentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Bill Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public CreditInquiryResponse creditInquiry(CreditInquiryRequest request) {
        long start = System.currentTimeMillis();
        CreditInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Credit Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Credit Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getProductId())
                .append(request.getPinType())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5())
                .append(request.getReserved6())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.creditInquiry(request);
                    response = integrationService.creditInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Credit Inquiry PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Credit Inquiry Payment TRANSACTION AUTHENTICATION *********");
                response = new CreditInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Credit Inquiry PAYMENT TRANSACTION *********");
            response = new CreditInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Credit Inquiry Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public CreditResponse credit(CreditRequest request) {
        long start = System.currentTimeMillis();
        CreditResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Credit Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Credit Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getProductId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5())
                .append(request.getReserved6())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.credit(request);
                    response = integrationService.creditResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Credit PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Credit Payment TRANSACTION AUTHENTICATION *********");
                response = new CreditResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Credit PAYMENT TRANSACTION *********");
            response = new CreditResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Credit Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public HRACashWithdrawalInquiryResponse hraCashWithdrawalInquiry(HRACashWithdrawalInquiryRequest request) {
        long start = System.currentTimeMillis();
        HRACashWithdrawalInquiryResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing HRA Cash Withdrawal Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing HRA Cash Withdrawal Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getAgentMobileNumber())
                .append(request.getCNIC())
                .append(request.getPinType())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5())
                .append(request.getReserved6())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.hraCashWithdrawalInquiry(request);
                    response = integrationService.hraCashWithdrawalInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR HRA Cash Withdrawal Inquiry PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR HRA Cash Withdrawal Inquiry Payment TRANSACTION AUTHENTICATION *********");
                response = new HRACashWithdrawalInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR HRA Cash Withdrawal Inquiry PAYMENT TRANSACTION *********");
            response = new HRACashWithdrawalInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("HRA Cash Withdrawal Inquiry Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public HRACashWithdrawalResponse hraCashWithdrawal(HRACashWithdrawalRequest request) {
        long start = System.currentTimeMillis();
        HRACashWithdrawalResponse response = null;
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing HRA Cash Withdrawal Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing HRA Cash Withdrawal Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getAgentMobileNumber())
                .append(request.getCnic())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5())
                .append(request.getReserved6())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.hraCashWithdrawal(request);
                    response = integrationService.hraCashWithdrawalResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR HRA Cash Withdrawal PAYMENT TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR HRA Cash Withdrawal Payment TRANSACTION AUTHENTICATION *********");
                response = new HRACashWithdrawalResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR HRA Cash Withdrawal PAYMENT TRANSACTION *********");
            response = new HRACashWithdrawalResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("HRA Cash Withdrawal Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public LoginAuthenticationResponse loginAuthentication(LoginAuthenticationRequest loginAuthenticationRequest) {
        long start = System.currentTimeMillis();
        LoginAuthenticationResponse response = null;
        String requestXML = XMLUtil.convertRequest(loginAuthenticationRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Login Authentication Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Login Authentication Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                loginAuthenticationRequest.getUserName() +
                        loginAuthenticationRequest.getPassword() +
                        loginAuthenticationRequest.getMobileNumber() +
                        loginAuthenticationRequest.getDateTime() +
                        loginAuthenticationRequest.getRrn() +
                        loginAuthenticationRequest.getChannelId() +
                        loginAuthenticationRequest.getPin() +
                        loginAuthenticationRequest.getCnic());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (loginAuthenticationRequest.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(loginAuthenticationRequest.getUserName(), loginAuthenticationRequest.getPassword(), loginAuthenticationRequest.getChannelId())) {

                try {
                    HostRequestValidator.validateLoginAuthenticationRequest(loginAuthenticationRequest);
                    response = integrationService.loginAuthentication(loginAuthenticationRequest);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Login AuthenticationTRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Login Authentication TRANSACTION AUTHENTICATION *********");
                response = new LoginAuthenticationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Login Authentication  TRANSACTION *********");
            response = new LoginAuthenticationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Login Authentication VERIFICATION Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }


    @Override
    public ZindigiLoginAuthenticationResponse zindigiLoginAuthentication(ZindigiLoginAuthenticationRequest zindigiLoginAuthenticationRequest) {
        long start = System.currentTimeMillis();
        ZindigiLoginAuthenticationResponse response = null;
        String requestXML = XMLUtil.convertRequest(zindigiLoginAuthenticationRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Zindgi Login Authentication Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Login Authentication Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                zindigiLoginAuthenticationRequest.getUserName() +
                        zindigiLoginAuthenticationRequest.getPassword() +
                        zindigiLoginAuthenticationRequest.getMobileNumber() +
                        zindigiLoginAuthenticationRequest.getDateTime() +
                        zindigiLoginAuthenticationRequest.getRrn() +
                        zindigiLoginAuthenticationRequest.getChannelId() +
                        zindigiLoginAuthenticationRequest.getPin() +
                        zindigiLoginAuthenticationRequest.getCnic());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (zindigiLoginAuthenticationRequest.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(zindigiLoginAuthenticationRequest.getUserName(), zindigiLoginAuthenticationRequest.getPassword(), zindigiLoginAuthenticationRequest.getChannelId())) {

                try {
                    HostRequestValidator.validateZindigiLoginAuthenticationRequest(zindigiLoginAuthenticationRequest);
                    response = integrationService.zindigiLoginAuthenticationResponse(zindigiLoginAuthenticationRequest);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Login AuthenticationTRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Login Authentication TRANSACTION AUTHENTICATION *********");
                response = new ZindigiLoginAuthenticationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Login Authentication  TRANSACTION *********");
            response = new ZindigiLoginAuthenticationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Zindigi Login Authentication VERIFICATION Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public LoginPinResponse loginPin(LoginPinRequest loginPinRequest) {
        long start = System.currentTimeMillis();
        LoginPinResponse response = null;
        List<String> loginPin = Arrays.asList(loginPinMatch.split("\\s*,\\s*"));
        String requestXML = XMLUtil.convertRequest(loginPinRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Login PIN Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Login Pin Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                loginPinRequest.getUserName() +
                        loginPinRequest.getPassword() +
                        loginPinRequest.getMobileNumber() +
                        loginPinRequest.getDateTime() +
                        loginPinRequest.getRrn() +
                        loginPinRequest.getChannelId() +
                        loginPinRequest.getTerminalId() +
                        loginPinRequest.getPin() +
                        loginPinRequest.getReserved1() +
                        loginPinRequest.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (loginPin.contains(loginPinRequest.getPin())) {
            response = new LoginPinResponse();
            response.setResponseCode("112");
            response.setResponseDescription("This combination of pin is not allowed");
            return response;
        } else {
            if (loginPinRequest.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(loginPinRequest.getUserName(), loginPinRequest.getPassword(), loginPinRequest.getChannelId())) {

                    try {
                        HostRequestValidator.validateLoginPinRequest(loginPinRequest);
                        response = integrationService.loginPin(loginPinRequest);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Login PIN TRANSACTION *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                    logger.info("RRN Number: " + response.getRrn());
                } else {
                    logger.info("******* DEBUG LOGS FOR  Login PIN TRANSACTION AUTHENTICATION *********");
                    response = new LoginPinResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR  Login PIN  TRANSACTION *********");
                response = new LoginPinResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Login PIN Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public LoginPinChangeResponse loginPinChange(LoginPinChangeRequest loginPinChangeRequest) {
        long start = System.currentTimeMillis();
        LoginPinChangeResponse response = null;
        List<String> loginPin = Arrays.asList(loginPinMatch.split("\\s*,\\s*"));
        String requestXML = XMLUtil.convertRequest(loginPinChangeRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Login PIN Change Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Login Pin Change Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                loginPinChangeRequest.getUserName() +
                        loginPinChangeRequest.getPassword() +
                        loginPinChangeRequest.getMobileNumber() +
                        loginPinChangeRequest.getDateTime() +
                        loginPinChangeRequest.getRrn() +
                        loginPinChangeRequest.getChannelId() +
                        loginPinChangeRequest.getTerminalId() +
                        loginPinChangeRequest.getOldLoginPin() +
                        loginPinChangeRequest.getNewLoginPin() +
                        loginPinChangeRequest.getConfirmLoginPin() +
                        loginPinChangeRequest.getReserved1() +
                        loginPinChangeRequest.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (loginPin.contains(loginPinChangeRequest.getConfirmLoginPin())) {
            response = new LoginPinChangeResponse();
            response.setResponseCode("112");
            response.setResponseDescription("This combination of pin is not allowed");
            return response;
        } else {
            if (loginPinChangeRequest.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(loginPinChangeRequest.getUserName(), loginPinChangeRequest.getPassword(), loginPinChangeRequest.getChannelId())) {

                    try {
                        HostRequestValidator.validateLoginPinChangeRequest(loginPinChangeRequest);
                        response = integrationService.loginPinChange(loginPinChangeRequest);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Login PIN Change TRANSACTION *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                    logger.info("RRN Number: " + response.getRrn());
                } else {
                    logger.info("******* DEBUG LOGS FOR  Login PIN Change TRANSACTION AUTHENTICATION *********");
                    response = new LoginPinChangeResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR  Login PIN Change TRANSACTION *********");
                response = new LoginPinChangeResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Login PIN Change Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public ResetPinResponse resetPin(ResetPinRequest resetPinRequest) {
        long start = System.currentTimeMillis();
        ResetPinResponse response = null;
        List<String> loginPin = Arrays.asList(loginPinMatch.split("\\s*,\\s*"));
        String requestXML = XMLUtil.convertRequest(resetPinRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Reset PIN Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Reset PIN Change Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                resetPinRequest.getUserName() +
                        resetPinRequest.getPassword() +
                        resetPinRequest.getMobileNumber() +
                        resetPinRequest.getDateTime() +
                        resetPinRequest.getRrn() +
                        resetPinRequest.getChannelId() +
                        resetPinRequest.getTerminalId() +
                        resetPinRequest.getNewLoginPin() +
                        resetPinRequest.getConfirmLoginPin() +
                        resetPinRequest.getCnic() +
                        resetPinRequest.getReserved1() +
                        resetPinRequest.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (loginPin.contains(resetPinRequest.getConfirmLoginPin())) {
            response = new ResetPinResponse();
            response.setResponseCode("112");
            response.setResponseDescription("This combination of pin is not allowed");
            return response;
        } else {
            if (resetPinRequest.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(resetPinRequest.getUserName(), resetPinRequest.getPassword(), resetPinRequest.getChannelId())) {

                    try {
                        HostRequestValidator.validateResetPinRequest(resetPinRequest);
                        response = integrationService.resetPin(resetPinRequest);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Reset PIN Change TRANSACTION *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                    logger.info("RRN Number: " + response.getRrn());
                } else {
                    logger.info("******* DEBUG LOGS FOR  Reset PIN Change TRANSACTION AUTHENTICATION *********");
                    response = new ResetPinResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR  Reset PIN Change TRANSACTION *********");
                response = new ResetPinResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Reset PIN Change Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AdvanceLoanSalaryResponse advanceLoanSalary(AdvanceLoanSalaryRequest request) {
        long start = System.currentTimeMillis();

        AdvanceLoanSalaryResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Advance Loan Salary Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Advance Loan Salary Payment Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getCnic())
                .append(request.getLoanAmount())
                .append(request.getNumberOfInstallments())
                .append(request.getInstallmentAmount())
                .append((request.getProductId()))
                .append(request.getGracePeriod())
                .append(request.getEarlyPaymentCharges())
                .append(request.getLatePaymentCharges());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAdvanceLoanSalary(request);
                    response = integrationService.advanceLoanSalary(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Advance Loan Salary TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Advance Loan Salary TRANSACTION AUTHENTICATION *********");
                response = new AdvanceLoanSalaryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Advance Loan Salary TRANSACTION *********");
            response = new AdvanceLoanSalaryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Advance Loan Salary Request  Processed in : {} ms {}", end, response);

        return response;
    }


    @Override
    public SmsGenerationResponse smsGeneration(SmsGenerationRequest request) {
        long start = System.currentTimeMillis();

        SmsGenerationResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing SMS Generation Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing SMS Generation Request Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getMessage())
                .append(request.getReserved1())
                .append(request.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateSmsGeneration(request);
                    response = integrationService.smsGeneration(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR SMS Generation TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR SMS Generation TRANSACTION AUTHENTICATION *********");
                response = new SmsGenerationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR SMS Generation TRANSACTION *********");
            response = new SmsGenerationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("SMS Generation Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentAccountLoginResponse agentAccountLogin(AgentAccountLoginRequest agentAccountLoginRequest) {
        long start = System.currentTimeMillis();
        AgentAccountLoginResponse response = null;
        String requestXML = XMLUtil.convertRequest(agentAccountLoginRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Agent Account Login Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Account Login Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                agentAccountLoginRequest.getUserName() +
                        agentAccountLoginRequest.getPassword() +
                        agentAccountLoginRequest.getDateTime() +
                        agentAccountLoginRequest.getRrn() +
                        agentAccountLoginRequest.getChannelId() +
                        agentAccountLoginRequest.getTerminalId() +
                        agentAccountLoginRequest.getAgentId() +
                        agentAccountLoginRequest.getPin() +
                        agentAccountLoginRequest.getReserved1() +
                        agentAccountLoginRequest.getReserved2() +
                        agentAccountLoginRequest.getReserved3() +
                        agentAccountLoginRequest.getReserved4() +
                        agentAccountLoginRequest.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (agentAccountLoginRequest.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(agentAccountLoginRequest.getUserName(), agentAccountLoginRequest.getPassword(), agentAccountLoginRequest.getChannelId())) {

                try {
                    HostRequestValidator.validateAgentLoginRequest(agentAccountLoginRequest);
                    response = integrationService.agentAccountLogin(agentAccountLoginRequest);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Agent Account Login TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Agent Account Login TRANSACTION AUTHENTICATION *********");
                response = new AgentAccountLoginResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Agent Account Login TRANSACTION *********");
            response = new AgentAccountLoginResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Agent Account Login Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentLoginPinGenerationResponse agentLoginPinGeneration(AgentLoginPinGenerationRequest agentLoginPinGenerationRequest) {
        long start = System.currentTimeMillis();
        AgentLoginPinGenerationResponse response = null;
        String requestXML = XMLUtil.convertRequest(agentLoginPinGenerationRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Agent Login PIN Generation Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Login PIN Generation Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                agentLoginPinGenerationRequest.getUserName() +
                        agentLoginPinGenerationRequest.getPassword() +
                        agentLoginPinGenerationRequest.getDateTime() +
                        agentLoginPinGenerationRequest.getRrn() +
                        agentLoginPinGenerationRequest.getChannelId() +
                        agentLoginPinGenerationRequest.getTerminalId() +
                        agentLoginPinGenerationRequest.getPin() +
                        agentLoginPinGenerationRequest.getAgentId() +
                        agentLoginPinGenerationRequest.getReserved1() +
                        agentLoginPinGenerationRequest.getReserved2() +
                        agentLoginPinGenerationRequest.getReserved3() +
                        agentLoginPinGenerationRequest.getReserved4() +
                        agentLoginPinGenerationRequest.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (agentLoginPinGenerationRequest.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(agentLoginPinGenerationRequest.getUserName(), agentLoginPinGenerationRequest.getPassword(), agentLoginPinGenerationRequest.getChannelId())) {

                try {
                    HostRequestValidator.validateAgentLoginPinGenerationRequest(agentLoginPinGenerationRequest);
                    response = integrationService.agentLoginPinGeneration(agentLoginPinGenerationRequest);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Agent Login PIN Generation TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Agent Login PIN Generation TRANSACTION AUTHENTICATION *********");
                response = new AgentLoginPinGenerationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Agent Login PIN Generation TRANSACTION *********");
            response = new AgentLoginPinGenerationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Agent Login PIN Generation Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentLoginPinResetResponse agentLoginPinReset(AgentLoginPinResetRequest agentLoginPinResetRequest) {
        long start = System.currentTimeMillis();
        AgentLoginPinResetResponse response = null;
        String requestXML = XMLUtil.convertRequest(agentLoginPinResetRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Agent Login PIN Reset Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Login PIN Reset Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                agentLoginPinResetRequest.getUserName() +
                        agentLoginPinResetRequest.getPassword() +
                        agentLoginPinResetRequest.getDateTime() +
                        agentLoginPinResetRequest.getRrn() +
                        agentLoginPinResetRequest.getChannelId() +
                        agentLoginPinResetRequest.getTerminalId() +
                        agentLoginPinResetRequest.getOldLoginPin() +
                        agentLoginPinResetRequest.getNewLoginPin() +
                        agentLoginPinResetRequest.getConfirmLoginPin() +
                        agentLoginPinResetRequest.getAgentId() +
                        agentLoginPinResetRequest.getReserved1() +
                        agentLoginPinResetRequest.getReserved2() +
                        agentLoginPinResetRequest.getReserved3() +
                        agentLoginPinResetRequest.getReserved4() +
                        agentLoginPinResetRequest.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (agentLoginPinResetRequest.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(agentLoginPinResetRequest.getUserName(), agentLoginPinResetRequest.getPassword(), agentLoginPinResetRequest.getChannelId())) {

                try {
                    HostRequestValidator.validateAgentLoginPinResetRequest(agentLoginPinResetRequest);
                    response = integrationService.agentLoginPinReset(agentLoginPinResetRequest);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Agent Login PIN Reset TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Agent Login PIN Reset TRANSACTION AUTHENTICATION *********");
                response = new AgentLoginPinResetResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Agent Login PIN Reset TRANSACTION *********");
            response = new AgentLoginPinResetResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Agent Login PIN Generation Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentMpinGenerationResponse agentMpinGeneration(AgentMpinGenerationRequest agentMpinGenerationRequest) {
        long start = System.currentTimeMillis();
        AgentMpinGenerationResponse response = null;
        String requestXML = XMLUtil.convertRequest(agentMpinGenerationRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Agent MPIN Generation Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Mpin Generation Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                agentMpinGenerationRequest.getUserName() +
                        agentMpinGenerationRequest.getPassword() +
                        agentMpinGenerationRequest.getMobileNumber() +
                        agentMpinGenerationRequest.getDateTime() +
                        agentMpinGenerationRequest.getRrn() +
                        agentMpinGenerationRequest.getChannelId() +
                        agentMpinGenerationRequest.getTerminalId() +
                        agentMpinGenerationRequest.getAgentId() +
                        agentMpinGenerationRequest.getMpin() +
                        agentMpinGenerationRequest.getConfirmMPIN() +
                        agentMpinGenerationRequest.getReserved1() +
                        agentMpinGenerationRequest.getReserved2() +
                        agentMpinGenerationRequest.getReserved3() +
                        agentMpinGenerationRequest.getReserved4() +
                        agentMpinGenerationRequest.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (agentMpinGenerationRequest.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(agentMpinGenerationRequest.getUserName(), agentMpinGenerationRequest.getPassword(), agentMpinGenerationRequest.getChannelId())) {

                try {
                    HostRequestValidator.validateAgentMpinPinGenerationRequest(agentMpinGenerationRequest);
                    response = integrationService.agentMpinGeneration(agentMpinGenerationRequest);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Agent Mpin Generation TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Agent MPIN TRANSACTION AUTHENTICATION *********");
                response = new AgentMpinGenerationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Agent MPIN Generation TRANSACTION *********");
            response = new AgentMpinGenerationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Agent Login PIN Generation Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentMpinResetResponse agentMpinReset(AgentMpinResetRequest agentMpinResetRequest) {
        long start = System.currentTimeMillis();
        AgentMpinResetResponse response = null;
        String requestXML = XMLUtil.convertRequest(agentMpinResetRequest);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Agent Mpin Reset Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Mpin Reset Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                agentMpinResetRequest.getUserName() +
                        agentMpinResetRequest.getPassword() +
                        agentMpinResetRequest.getMobileNumber() +
                        agentMpinResetRequest.getDateTime() +
                        agentMpinResetRequest.getRrn() +
                        agentMpinResetRequest.getChannelId() +
                        agentMpinResetRequest.getTerminalId() +
                        agentMpinResetRequest.getOldMpin() +
                        agentMpinResetRequest.getNewMpin() +
                        agentMpinResetRequest.getConfirmMpin() +
                        agentMpinResetRequest.getAgentId() +
                        agentMpinResetRequest.getReserved1() +
                        agentMpinResetRequest.getReserved2() +
                        agentMpinResetRequest.getReserved3() +
                        agentMpinResetRequest.getReserved4() +
                        agentMpinResetRequest.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (agentMpinResetRequest.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(agentMpinResetRequest.getUserName(), agentMpinResetRequest.getPassword(), agentMpinResetRequest.getChannelId())) {

                try {
                    HostRequestValidator.validateAgentMpinReset(agentMpinResetRequest);
                    response = integrationService.agentMpinReset(agentMpinResetRequest);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Agent Mpin Reset TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
                logger.info("RRN Number: " + response.getRrn());
            } else {
                logger.info("******* DEBUG LOGS FOR  Agent MPIN Reset TRANSACTION AUTHENTICATION *********");
                response = new AgentMpinResetResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Agent MPIN Reset TRANSACTION *********");
            response = new AgentMpinResetResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Agent MPIN Reset Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }
//
//    @Override
//    public AgentMpinVerificationResponse agentMpinVerification(AgentMpinVerificationRequest agentMpinVerificationRequest) {
//        long start = System.currentTimeMillis();
//        AgentMpinVerificationResponse response = null;
//        String requestXML = XMLUtil.convertRequest(agentMpinVerificationRequest);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Mpin Verification Transaction Request with {}", requestXML);
//        StringBuffer stringText = new StringBuffer(
//                agentMpinVerificationRequest.getUserName() +
//                        agentMpinVerificationRequest.getPassword() +
//                        agentMpinVerificationRequest.getMobileNumber() +
//                        agentMpinVerificationRequest.getDateTime() +
//                        agentMpinVerificationRequest.getRrn() +
//                        agentMpinVerificationRequest.getChannelId() +
//                        agentMpinVerificationRequest.getTerminalId() +
//                        agentMpinVerificationRequest.getAgentId() +
//                        agentMpinVerificationRequest.getMpin() +
//                        agentMpinVerificationRequest.getConfirmMPIN() +
//                        agentMpinVerificationRequest.getReserved1() +
//                        agentMpinVerificationRequest.getReserved2() +
//                        agentMpinVerificationRequest.getReserved3() +
//                        agentMpinVerificationRequest.getReserved4() +
//                        agentMpinVerificationRequest.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (agentMpinVerificationRequest.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(agentMpinVerificationRequest.getUserName(), agentMpinVerificationRequest.getPassword(), agentMpinVerificationRequest.getChannelId())) {
//
//                try {
//                    HostRequestValidator.validateAgentMpinPinVerificationRequest(agentMpinVerificationRequest);
//                    response = integrationService.agentMpinVerification(agentMpinVerificationRequest);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent Mpin Verification TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//                logger.info("RRN Number: " + response.getRrn());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Agent MPIN Verification TRANSACTION AUTHENTICATION *********");
//                response = new AgentMpinVerificationResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR  Agent MPIN Verification TRANSACTION *********");
//            response = new AgentMpinVerificationResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent MPIN Verification Transaction Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }

    @Override
    public AgentBalanceInquiryResponse agentBalanceInquiry(AgentBalanceInquiryRequest request) {
        long start = System.currentTimeMillis();

        AgentBalanceInquiryResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Agent Balance Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Balance Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getAgentId())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentBalanceInquiry(request);
                    response = integrationService.agentBalanceInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR AGENT BALANCE INQUIRY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  AGENT Balance Inquiry TRANSACTION AUTHENTICATION *********");
                response = new AgentBalanceInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR AGENT BALANCE INQUIRY TRANSACTION *********");
            response = new AgentBalanceInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Balance Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

//    @Override
//    public AgentToAgentInquiryResponse agentToAgentInquiry(AgentToAgentInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentToAgentInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent To Agent Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getMpin())
//                .append(request.getReceiverAgentMobileNumber())
//                .append(request.getTransactionAmount())
//                .append(request.getAgentId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentToAgentInquiry(request);
//                    response = integrationService.agentToAgentInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT To Agent INQUIRY TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT To Agent Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentToAgentInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT To Agent INQUIRY TRANSACTION *********");
//            response = new AgentToAgentInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent To Agent Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentToAgentPaymentResponse agentToAgentPayment(AgentToAgentPaymentRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentToAgentPaymentResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent To Agent Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getMpin())
//                .append(request.getAgentMobileNumber())
//                .append(request.getTransactionAmount())
//                .append(request.getCharges())
//                .append(request.getTransactionType())
//                .append(request.getPaymentType())
//                .append(request.getTransactionCode())
//                .append(request.getSettlementType())
//                .append(request.getAgentId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentToAgentPayment(request);
//                    response = integrationService.agentToAgentPayment(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT To Agent PAYMENT TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT To Agent PAYMENT TRANSACTION AUTHENTICATION *********");
//                response = new AgentToAgentPaymentResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT To Agent Payment TRANSACTION *********");
//            response = new AgentToAgentPaymentResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent To Agent Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentAccountOpeningResponse agentAccountOpening(AgentAccountOpeningRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentAccountOpeningResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Account Opening Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getMpin())
//                .append(request.getCustomerRegState())
//                .append(request.getCustomerRegStateId())
//                .append(request.getCnic())
//                .append(request.getCnicStatus())
//                .append(request.getCnicExpiry())
//                .append(request.getBirthPlace())
//                .append(request.getConsumerName())
//                .append(request.getMotherMaiden())
//                .append(request.getDob())
//                .append(request.getPresentAddress())
//                .append(request.getPresentCity())
//                .append(request.getPermanentAddress())
//                .append(request.getPermanentCity())
//                .append(request.getAccountTitle())
//                .append(request.getGender())
//                .append(request.getFatherHusbandName())
//                .append(request.getIsCnicSeen())
//                .append(request.getDepositAmount())
//                .append(request.getAgentMobileNumber())
//                .append(request.getProductId())
//                .append(request.getIsBVSAccount())
//                .append(request.getSegmentId())
//                .append(request.getAccountType())
//                .append(request.getTransactionId())
//                .append(request.getMobileNetwork())
//                .append(request.getIsHRA())
//                .append(request.getNokMobileNumber())
//                .append(request.getTransactionPurpose())
//                .append(request.getOccupation())
//                .append(request.getOrgLocation1())
//                .append(request.getOrgLocation2())
//                .append(request.getOrgLocation3())
//                .append(request.getOrgLocation4())
//                .append(request.getOrgLocation5())
//                .append(request.getOrgRelation1())
//                .append(request.getOrgRelation2())
//                .append(request.getOrgRelation3())
//                .append(request.getOrgRelation4())
//                .append(request.getOrgRelation5())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (
//                    HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentAccountOpening(request);
//                    response = integrationService.agentAccountOpening(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Account Opening TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Account Opening TRANSACTION AUTHENTICATION *********");
//                response = new AgentAccountOpeningResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Account Opening TRANSACTION *********");
//            response = new AgentAccountOpeningResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Account Opening Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentUpgradeAccountInquiryResponse agentUpgradeAccountInquiry(AgentUpgradeAccountInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentUpgradeAccountInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Upgrade Account Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getCustomerCnic())
//                .append(request.getIsReceiveCash())
//                .append(request.getIsHRA())
//                .append(request.getIsUpgrade())
//                .append(request.getAgentId())
//                .append(request.getProductId())
//                .append(request.getSegmentId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentUpgadeAccountInquiry(request);
//                    response = integrationService.agentUpgradeAccountInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Upgrade Account Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Upgrade Account Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentUpgradeAccountInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Upgrade Account Inquiry TRANSACTION *********");
//            response = new AgentUpgradeAccountInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Upgrade Account Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentUpgradeAccountResponse agentUpgradeAccount(AgentUpgradeAccountRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentUpgradeAccountResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Upgrade Account Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getMpin())
//                .append(request.getCustomerRegState())
//                .append(request.getCustomerRegStateId())
//                .append(request.getCnic())
//                .append(request.getCnicStatus())
//                .append(request.getCnicExpiry())
//                .append(request.getBirthPlace())
//                .append(request.getConsumerName())
//                .append(request.getMotherMaiden())
//                .append(request.getDob())
//                .append(request.getPresentAddress())
//                .append(request.getPresentCity())
//                .append(request.getPermanentAddress())
//                .append(request.getPermanentCity())
//                .append(request.getAccountTitle())
//                .append(request.getGender())
//                .append(request.getFatherHusbandName())
//                .append(request.getIsCnicSeen())
//                .append(request.getDepositAmount())
//                .append(request.getAgentMobileNumber())
//                .append(request.getProductId())
//                .append(request.getIsBVSAccount())
//                .append(request.getSegmentId())
//                .append(request.getAccountType())
//                .append(request.getTransactionId())
//                .append(request.getMobileNetwork())
//                .append(request.getIsHRA())
//                .append(request.getNokMobileNumber())
//                .append(request.getTransactionPurpose())
//                .append(request.getOccupation())
//                .append(request.getOrgLocation1())
//                .append(request.getOrgLocation2())
//                .append(request.getOrgLocation3())
//                .append(request.getOrgLocation4())
//                .append(request.getOrgLocation5())
//                .append(request.getOrgRelation1())
//                .append(request.getOrgRelation2())
//                .append(request.getOrgRelation3())
//                .append(request.getOrgRelation4())
//                .append(request.getOrgRelation5())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (
//                    HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentUpgradeAccount(request);
//                    response = integrationService.agentUpgradeAccount(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Upgrade Account TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Upgrade Account TRANSACTION AUTHENTICATION *********");
//                response = new AgentUpgradeAccountResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Upgrade Account TRANSACTION *********");
//            response = new AgentUpgradeAccountResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Upgrade Account Request  Processed in : {} ms {}", end, response);
//
//        return response;
//
//    }
//
//    @Override
//    public AgentCashInInquiryResponse agentCashInInquiry(AgentCashInInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentCashInInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Cash In Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalID())
//                .append(request.getAmount())
//                .append(request.getAgentMobileNumber())
//                .append(request.getProductId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentCashInInquiry(request);
//                    response = integrationService.agentCashInInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Cash In Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Cash In Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentCashInInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Cash In Inquiry TRANSACTION *********");
//            response = new AgentCashInInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cash In Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentCashInResponse agentCashIn(AgentCashInRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentCashInResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Cash In Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalID())
//                .append(request.getProductId())
//                .append(request.getPin())
//                .append(request.getBranchlessAccountId())
//                .append(request.getCoreAccountId())
//                .append(request.getBankId())
//                .append(request.getAmount())
//                .append(request.getComissionAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getTotalAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentCashIn(request);
//                    response = integrationService.agentCashIn(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Cash In Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Cash In Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentCashInResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Cash In Payment TRANSACTION *********");
//            response = new AgentCashInResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cash In Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentCashOutInquiryResponse agentCashOutInquiry(AgentCashOutInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentCashOutInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Cash Out Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalID())
//                .append(request.getAmount())
//                .append(request.getAgentMobileNumber())
//                .append(request.getProductId())
//                .append(request.getBankId())
//                .append(request.getReserved1())
//                .append(request.getReserved2());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentCashOutInquiry(request);
//                    response = integrationService.agentCashOutInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Cash Out Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Cash Out Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentCashOutInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Cash Out Inquiry TRANSACTION *********");
//            response = new AgentCashOutInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cash Out Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentCashOutResponse agentCashOut(AgentCashOutRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentCashOutResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Cash Out Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalID())
//                .append((request.getProductId()))
//                .append(request.getPin())
//                .append(request.getAgentMobileNumber())
//                .append(request.getBranchlessAccountId())
//                .append(request.getCoreAccountId())
//                .append(request.getCoreAccountTitle())
//                .append(request.getBankId())
//                .append(request.getAmount())
//                .append(request.getComissionAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getTotalAmount());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentCashOut(request);
//                    response = integrationService.agentCashOut(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Cash Out Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Cash Out Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentCashOutResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Cash Out Payment TRANSACTION *********");
//            response = new AgentCashOutResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cash Out Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentWalletToWalletInquiryResponse agentWalletToWalletInquiry(AgentWalletToWalletInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentWalletToWalletInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Wallet To Wallet Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append((request.getProductId()))
//                .append(request.getReceiverMobileNumber())
//                .append(request.getAgentMobileNumber())
//                .append(request.getAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentWalletToWalletInquiry(request);
//                    response = integrationService.agentWalletToWalletInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Wallet To Wallet Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Wallet To Wallet Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentWalletToWalletInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Wallet To Wallet Inquiry TRANSACTION *********");
//            response = new AgentWalletToWalletInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Wallet To Wallet Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentWalletToWalletPaymentResponse agentWalletToWalletPayment(AgentWalletToWalletPaymentRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentWalletToWalletPaymentResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Wallet To Wallet Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getMpin())
//                .append(request.getProductId())
//                .append(request.getOtp())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getAgentMobileNumber())
//                .append(request.getAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getCommissionAmount())
//                .append(request.getTotalAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentWalletToWalletPayment(request);
//                    response = integrationService.agentWalletToWalletPayment(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Wallet To Wallet Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Wallet To Wallet Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentWalletToWalletPaymentResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Wallet To Wallet Payment TRANSACTION *********");
//            response = new AgentWalletToWalletPaymentResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Wallet To Wallet Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentWalletToCnicInquiryResponse agentWalletToCnicInquiry(AgentWalletToCnicInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentWalletToCnicInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Wallet To Cnic Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getProductId())
//                .append(request.getAgentMobileNumber())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getReceiverCnic())
//                .append(request.getAmount())
//                .append(request.getPaymentPurpose())
//                .append(request.getPinType())
//                .append(request.getReserved1())
//                .append(request.getReserved2());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentWalletToCnicInquiry(request);
//                    response = integrationService.agentWalletToCnicInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Wallet To Cnic Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Wallet To Cnic Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentWalletToCnicInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Wallet To Cnic Inquiry TRANSACTION *********");
//            response = new AgentWalletToCnicInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Wallet To Cnic Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentWalletToCnicPaymentResponse agentWalletToCnicPayment(AgentWalletToCnicPaymentRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentWalletToCnicPaymentResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Wallet To Cnic Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getPaymentPurpose())
//                .append(request.getPin())
//                .append(request.getAgentMobileNumber())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getReceiverCnic())
//                .append(request.getAmount())
//                .append(request.getCommissionAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getTotalAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentWalletToCnicPayment(request);
//                    response = integrationService.agentWalletToCnicPayment(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Wallet To Cnic Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Wallet To Cnic Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentWalletToCnicPaymentResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Wallet To Cnic Payment TRANSACTION *********");
//            response = new AgentWalletToCnicPaymentResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Wallet To Cnic Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }

    @Override
    public AgentIbftInquiryResponse agentIbftInquiry(AgentIbftInquiryRequest request) {
        long start = System.currentTimeMillis();

        AgentIbftInquiryResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Agent IBFT Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Ibft Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getAgentId())
                .append(request.getProductId())
                .append(request.getCoreAccountId())
                .append(request.getBankIMD())
                .append(request.getPaymentPurpose())
                .append(request.getBeneficiaryBankName())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentIbftInquiry(request);
                    response = integrationService.agentIbftInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR AGENT Ibft Inquiry TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  AGENT Ibft Inquiry TRANSACTION AUTHENTICATION *********");
                response = new AgentIbftInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR AGENT Ibft Inquiry TRANSACTION *********");
            response = new AgentIbftInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Ibft Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentIbftPaymentResponse agentIbftPayment(AgentIbftPaymentRequest request) {
        long start = System.currentTimeMillis();

        AgentIbftPaymentResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Agent IBFT Payment Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Ibft Payment Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getMpin())
                .append(request.getOtp())
                .append((request.getAgentId()))
                .append(request.getProductId())
                .append(request.getCoreAccountId())
                .append(request.getCoreAccountTitle())
                .append(request.getBankIMD())
                .append(request.getPaymentPurpose())
                .append(request.getBeneficiaryBankName())
                .append(request.getBeneficiaryiciaryBranchName())
                .append(request.getBeneficiaryIban())
                .append(request.getTransactionAmount())
                .append(request.getTotalAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentIbftPayment(request);
                    response = integrationService.agentIbftPayment(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR AGENT Ibft Payment TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  AGENT Ibft Payment TRANSACTION AUTHENTICATION *********");
                response = new AgentIbftPaymentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR AGENT Ibft Payment TRANSACTION *********");
            response = new AgentIbftPaymentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Ibft Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

//    @Override
//    public AgentRetailPaymentInquiryResponse agentRetailPaymentInquiry(AgentRetailPaymentInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentRetailPaymentInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Retail Payment Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getProductId())
//                .append(request.getAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentRetailPaymentInquiry(request);
//                    response = integrationService.agentRetailPaymentInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Retail Payment  TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Retail Payment Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentRetailPaymentInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Retail Payment Inquiry TRANSACTION *********");
//            response = new AgentRetailPaymentInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Retail Payment Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentRetailPaymentResponse agentRetailPayment(AgentRetailPaymentRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentRetailPaymentResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Retail Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getProductId())
//                .append(request.getPin())
//                .append(request.getAmount())
//                .append(request.getComissionAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getTotalAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentRetailPayment(request);
//                    response = integrationService.agentRetailPayment(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Retail Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Retail Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentRetailPaymentResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Retail Payment TRANSACTION *********");
//            response = new AgentRetailPaymentResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Retail Payment  Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentWalletToCoreInquiryResponse agentWalletToCoreInquiry(AgentWalletToCoreInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentWalletToCoreInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Wallet To Core Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getProductId())
//                .append(request.getAgentMobileNumber())
//                .append(request.getCoreAccountId())
//                .append(request.getAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentWalletToCoreInquiry(request);
//                    response = integrationService.agentWalletToCoreInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent Wallet To Core Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Agent Wallet To Core Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentWalletToCoreInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Agent Wallet To Core Inquiry TRANSACTION *********");
//            response = new AgentWalletToCoreInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Wallet To Core Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentWalletToCorePaymentResponse agentWalletToCorePayment(AgentWalletToCorePaymentRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentWalletToCorePaymentResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Wallet To Core Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getPin())
//                .append(request.getAgentMobileNumber())
//                .append(request.getReceiverCustomerName())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getReceiverAccountNumber())
//                .append(request.getTransactionAmount())
//                .append(request.getComissionAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getTotalTransactionAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentWalletToCorePayment(request);
//                    response = integrationService.agentWalletToCorePayment(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent Wallet To Core Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Agent Wallet To Core Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentWalletToCorePaymentResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Agent Wallet To Core Payment TRANSACTION *********");
//            response = new AgentWalletToCorePaymentResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Wallet To Core Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentReceiveMoneyInquiryResponse agentReceiveMoneyInquiry(AgentReceiveMoneyInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentReceiveMoneyInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Receive Money Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getProductId())
//                .append(request.getAgentMobileNumber())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getReceiverCnic())
//                .append(request.getTransactionId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentReceiveMoneyInquiry(request);
//                    response = integrationService.agentReceiveMoneyInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent Receive Money Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Agent Receive Money Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentReceiveMoneyInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Agent Receive Money Inquiry TRANSACTION *********");
//            response = new AgentReceiveMoneyInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Receive Money Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentReceiveMoneyPaymentResponse agentReceiveMoneyPayment(AgentReceiveMoneyPaymentRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentReceiveMoneyPaymentResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Receive Money Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getTransactionId())
//                .append(request.getPin())
//                .append(request.getOtp())
//                .append(request.getSenderMobileNumber())
//                .append(request.getSenderCnic())
//                .append(request.getAmount())
//                .append(request.getCommissionAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getTotalAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentReceiveMoneyPayment(request);
//                    response = integrationService.agentReceiveMoneyPayment(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent Receive Money Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Agent Receive Money Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentReceiveMoneyPaymentResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Agent Receive Money Payment TRANSACTION *********");
//            response = new AgentReceiveMoneyPaymentResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Receive Money Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }

//    @Override
//    public AgentCnicToCnicInquiryResponse agentCnicToCnicInquiry(AgentCnicToCnicInquiryRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentCnicToCnicInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Cnic To Cnic Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalID())
//                .append(request.getProductId())
//                .append(request.getAgentId())
//                .append(request.getAgentMobileNumber())
//                .append(request.getSenderMobileNumber())
//                .append(request.getSenderCnic())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getReceiverCnic())
//                .append(request.getAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentCnicToCnicInquiry(request);
//                    response = integrationService.agentCnicToCnicInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent Cnic To Cnic Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Agent Cnic To Cnic Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentCnicToCnicInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Agent Cnic To Cnic Inquiry TRANSACTION *********");
//            response = new AgentCnicToCnicInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cnic To Cnic Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentCnicToCnicPaymentResponse agentCnicToCnicPayment(AgentCnicToCnicPaymentRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentCnicToCnicPaymentResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Cnic To Cnic Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalID())
//                .append(request.getAgentId())
//                .append(request.getProductId())
//                .append(request.getAgentMobileNumber())
//                .append(request.getBvsRequest())
//                .append(request.getPin())
//                .append(request.getSenderCnic())
//                .append(request.getSenderMobileNumber())
//                .append(request.getSenderCnic())
//                .append(request.getReceiverCity())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getReceiverCnic())
//                .append(request.getTransactionPurposeCode())
//                .append(request.getAmount())
//                .append(request.getComissionAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getTotalAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentCnicToCnicPayment(request);
//                    response = integrationService.agentCnicToCnicPayment(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent Cnic To Cnic Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Agent Cnic To Cnic Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentCnicToCnicPaymentResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Agent Cnic To Cnic Payment TRANSACTION *********");
//            response = new AgentCnicToCnicPaymentResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cnic To Cnic Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }


//    @Override
//    public AgentHRARegistrationInquiryResponse agentHRARegistrationInquiry(AgentHRARegistrationInquiryRequest request) {
//
//        long start = System.currentTimeMillis();
//        AgentHRARegistrationInquiryResponse response = null;
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent HRA Registration Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getCnic())
//                .append(request.getAgentId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.agentHRARegistrationInquiry(request);
//                    response = integrationService.agentHRARegistrationInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent HRA Registration Inquiry PAYMENT TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR Agent HRA Registration  Inquiry Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentHRARegistrationInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR  Agent HRA Registration  Inquiry PAYMENT TRANSACTION *********");
//            response = new AgentHRARegistrationInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent HRA Registration  Inquiry Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentHRARegistrationResponse agentHRARegistration(AgentHRARegistrationRequest request) {
//        long start = System.currentTimeMillis();
//        AgentHRARegistrationResponse response = null;
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent HRA Registration Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getPin())
//                .append(request.getPinType())
//                .append(request.getAgentMobileNumber())
//                .append(request.getAgentId())
//                .append(request.getProductId())
//                .append(request.getName())
//                .append(request.getFatherName())
//                .append(request.getDateOfBirth())
//                .append(request.getCnic())
//                .append(request.getSourceOfIncome())
//                .append(request.getOccupation())
//                .append(request.getCustomerMobileNetwork())
//                .append(request.getPurposeOfAccount())
//                .append(request.getKinName())
//                .append(request.getKinMobileNumber())
//                .append(request.getKinCnic())
//                .append(request.getKinRelation())
//                .append(request.getInternationalRemittanceLocation())
//                .append(request.getOriginatorRelation())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.agentHRARegistration(request);
//                    response = integrationService.agentHRARegistration(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent HRA Registration PAYMENT TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR Agent HRA Registration Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentHRARegistrationResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR  Agent HRA Registration  Inquiry PAYMENT TRANSACTION *********");
//            response = new AgentHRARegistrationResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent HRA Registration Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @Override
//    public AgentCnicToCoreInquiryResponse agentCnicToCoreInquiry(AgentCnicToCoreInquiryRequest request) {
//                long start = System.currentTimeMillis();
//
//        AgentCnicToCoreInquiryResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Cnic To Core Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalID())
//                .append(request.getProductId())
//                .append(request.getAgentId())
//                .append(request.getAgentMobileNumber())
//                .append(request.getSenderMobileNumber())
//                .append(request.getSenderCnic())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getReceiverCnic())
//                .append(request.getAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentCnicToCoreInquiry(request);
//                    response = integrationService.agentCnicToCoreInquiry(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Agent Cnic To Core Inquiry TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Agent Cnic To Core Inquiry TRANSACTION AUTHENTICATION *********");
//                response = new AgentCnicToCoreInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Agent Cnic To Core Inquiry TRANSACTION *********");
//            response = new AgentCnicToCoreInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cnic To Core Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }


//    @Override
//    public AgentCnicToCorePaymentResponse agentCnicToCorePayment(AgentCnicToCorePaymentRequest request) {
//        long start = System.currentTimeMillis();
//
//        AgentCnicToCorePaymentResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Agent Cnic To Core Payment Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalID())
//                .append(request.getProductId())
//                .append(request.getAgentId())
//                .append(request.getPin())
//                .append(request.getAgentMobileNumber())
//                .append(request.getBvsRequest())
//                .append(request.getImpressioin())
//                .append(request.getFingerIndex())
//                .append(request.getTemplateType())
//                .append(request.getFingerTemplate())
//                .append(request.getSenderMobileNumber())
//                .append(request.getSenderCnic())
//                .append(request.getReceiverMobileNumber())
//                .append(request.getReceiverAccountNumber())
//                .append(request.getCoreAccountId())
//                .append(request.getCoreAccountTitle())
//                .append(request.getAmount())
//                .append(request.getComissionAmount())
//                .append(request.getTransactionProcessingAmount())
//                .append(request.getTotalAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateAgentCnicToCorePayment(request);
//                    response = integrationService.agentCnicToCorePayment(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR AGENT Cnic To Core Payment TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  AGENT Cnic To Core Payment TRANSACTION AUTHENTICATION *********");
//                response = new AgentCnicToCorePaymentResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR AGENT Cnic To Core Payment TRANSACTION *********");
//            response = new AgentCnicToCorePaymentResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cash In Inquiry Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }

    @Override
    public AgentCashDepositInquiryResponse agentCashDepositInquiry(AgentCashDepositInquiryRequest request) {

        long start = System.currentTimeMillis();

        AgentCashDepositInquiryResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Agent Cash Deposit Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Cash In Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getProductId())
                .append(request.getAgentId())
                .append(request.getAgentMobileNumber())
                .append(request.getAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentCashDepositInquiry(request);
                    response = integrationService.agentCashDepositInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR AGENT Cash In Inquiry TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  AGENT Cash In Inquiry TRANSACTION AUTHENTICATION *********");
                response = new AgentCashDepositInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR AGENT Cash In Inquiry TRANSACTION *********");
            response = new AgentCashDepositInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Cash In Inquiry Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentCashDepositPaymentResponse agentCashDepositPayment(AgentCashDepositPaymentRequest request) {
        long start = System.currentTimeMillis();

        AgentCashDepositPaymentResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Agent Cash Deposit Payment Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Cash In Payment Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getProductId())
                .append(request.getAgentId())
                .append(request.getPin())
                .append(request.getAgentMobileNumber())
                .append(request.getCnic())
//                .append(request.getFingerIndex())
//                .append(request.getFingerTemplate())
//                .append(request.getTemplateType())
                .append(request.getAmount())
                .append(request.getComissionAmount())
                .append(request.getTransactionProcessingAmount())
                .append(request.getTotalAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentCashDepositPayment(request);
                    response = integrationService.agentCashDepositPayment(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR AGENT Cash In Payment TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  AGENT Cash In Payment TRANSACTION AUTHENTICATION *********");
                response = new AgentCashDepositPaymentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR AGENT Cash In Payment TRANSACTION *********");
            response = new AgentCashDepositPaymentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Cash In Payment Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentCashWithdrawalInquiryResponse agentCashWithdrawalInquiry(AgentCashWithdrawalInquiryRequest request) {
        long start = System.currentTimeMillis();

        AgentCashWithdrawalInquiryResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Agent Cash Withdrawal Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Cash Withdrawal Inquiry Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getAgentId())
                .append(request.getProductId())
                .append(request.getAgentMobileNumber())
                .append(request.getCnic())
                .append(request.getAmount())
                .append(request.getPaymentMode())
                .append(request.getIsOtpRequest())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentCashWithdrawalInquiry(request);
                    response = integrationService.agentCashWithdrawalInquiry(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR AGENT Agent Cash Withdrawal Inquiry TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  AGENT Agent Cash Withdrawal Inquiry TRANSACTION AUTHENTICATION *********");
                response = new AgentCashWithdrawalInquiryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR AGENT Agent Cash Withdrawal Inquiry TRANSACTION *********");
            response = new AgentCashWithdrawalInquiryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Agent Cash Withdrawal Inquiry  Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentCashWithdrawalPaymentResponse agentCashWithdrawalPayment(AgentCashWithdrawalPaymentRequest request) {
        long start = System.currentTimeMillis();

        AgentCashWithdrawalPaymentResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Agent Cash Withdrawal Payment Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Agent Cash Withdrawal Payment Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalID())
                .append(request.getProductId())
                .append(request.getAgentId())
                .append(request.getPin())
                .append(request.getPinType())
                .append(request.getOtp())
                .append(request.getTransactionId())
                .append(request.getAgentMobileNumber())
                .append(request.getCustomerMobileNumber())
                .append(request.getCustomerCnic())
//                .append(request.getFingerIndex())
//                .append(request.getFingerTemplate())
//                .append(request.getTemplateType())
                .append(request.getAmount())
                .append(request.getComissionAmount())
                .append(request.getTransactionProcessingAmount())
                .append(request.getTotalAmount())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAgentCashWithdrawalPayment(request);
                    response = integrationService.agentCashWithdrawalPayment(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR AGENT Agent Cash Withdrawal Payment TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  AGENT Agent Cash Withdrawal Payment TRANSACTION AUTHENTICATION *********");
                response = new AgentCashWithdrawalPaymentResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR AGENT Agent Cash Withdrawal Payment TRANSACTION *********");
            response = new AgentCashWithdrawalPaymentResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Agent Cash Withdrawal Payment  Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public MpinVerificationResponse mpinVerification(MpinVerificationRequest request) {
        long start = System.currentTimeMillis();

        MpinVerificationResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing MPIN Verification Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing MPIN Verification Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getMpin());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateMpinVerification(request);
                    response = integrationService.mpinVerification(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR MPIN Verification TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  MPIN Verification TRANSACTION AUTHENTICATION *********");
                response = new MpinVerificationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR MPIN Verification TRANSACTION *********");
            response = new MpinVerificationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Agent MPIN Verification  Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public SegmentListResponse segmentList(SegmentListRequest request) {
        long start = System.currentTimeMillis();

        SegmentListResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Segment List Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Segment List Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validatesegmentList(request);
                    response = integrationService.segmentList(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Segment List TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Segment List TRANSACTION AUTHENTICATION *********");
                response = new SegmentListResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Segment List TRANSACTION *********");
            response = new SegmentListResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Segment List  Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AgentCatalogResponse catalogList(AgentCatalogsRequest request) {
        long start = System.currentTimeMillis();

        AgentCatalogResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Catalog List Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Catalog List Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateCatalogList(request);
                    response = integrationService.catalogList(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Catalog List TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Catalog List TRANSACTION AUTHENTICATION *********");
                response = new AgentCatalogResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Catalog List TRANSACTION *********");
            response = new AgentCatalogResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Catalog List  Request  Processed in : {} ms {}", end, response);

        return response;

    }

    @Override
    public L2AccountOpeningResponse l2AccountOpening(L2AccountOpeningRequest request) {
        long start = System.currentTimeMillis();

        L2AccountOpeningResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing L2 Account Opening Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing L2 Account Opening Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getConsumerName())
                .append(request.getFatherHusbandName())
                .append(request.getGender())
                .append(request.getCnic())
                .append(request.getCnicIssuanceDate())
                .append(request.getDob())
                .append(request.getBirthPlace())
                .append(request.getMotherMaiden())
                .append(request.getEmailAddress())
                .append(request.getMailingAddress())
                .append(request.getPermanentAddress())
                .append(request.getPurposeOfAccount())
                .append(request.getSourceOfIncome())
                .append(request.getExpectedMonthlyTurnover())
                .append(request.getNextOfKin())
                .append(request.getCnicFrontPic())
                .append(request.getCnicBackPic())
                .append(request.getCustomerPic())
                .append(request.getLatitude())
                .append(request.getLongitude())
                .append(request.getAccountType())
                .append(request.getReserved())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateL2AccountOpening(request);
                    response = integrationService.l2AccountOpening(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR L2 Account Opening TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  L2 Account Opening TRANSACTION AUTHENTICATION *********");
                response = new L2AccountOpeningResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR L2 Account Opening TRANSACTION *********");
            response = new L2AccountOpeningResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("L2 Account Opening  Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public L2AccountUpgradeResponse l2AccountUpgrade(L2AccountUpgradeRequest request) {
        long start = System.currentTimeMillis();

        L2AccountUpgradeResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing L2 Account Upgrade Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing L2 Account Upgrade Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getMpin())
                .append(request.getCnic())
                .append(request.getFingerIndex())
                .append(request.getFingerTemplate())
                .append(request.getTemplateType())
                .append(request.getConsumerName())
                .append(request.getFatherHusbandName())
                .append(request.getGender())
                .append(request.getCnicIssuanceDate())
                .append(request.getDob())
                .append(request.getBirthPlace())
                .append(request.getMotherMaiden())
                .append(request.getEmailAddress())
                .append(request.getMailingAddress())
                .append(request.getPermanentAddress())
                .append(request.getPurposeOfAccount())
                .append(request.getSourceOfIncome())
                .append(request.getSourceOfIncomePic())
                .append(request.getExpectedMonthlyTurnover())
                .append(request.getNextOfKin())
                .append(request.getCnicFrontPic())
                .append(request.getCnicBackPic())
                .append(request.getCustomerPic())
                .append(request.getLatitude())
                .append(request.getLongitude())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5())
                .append(request.getReserved6())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10())
                .append(request.getReserved11())
                .append(request.getReserved12())
                .append(request.getReserved13())
                .append(request.getReserved14())
                .append(request.getReserved15());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
        if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
            try {
                    HostRequestValidator.validateL2AccountUpgrade(request);
                response = integrationService.l2AccountUpgrade(request);

            } catch (ValidationException ve) {
                response.setResponseCode("420");
                response.setResponseDescription(ve.getMessage());

                logger.error("ERROR: Request Validation", ve);
            } catch (Exception e) {
                response.setResponseCode("220");
                response.setResponseDescription(e.getMessage());
                logger.error("ERROR: General Processing ", e);
            }

            logger.info("******* DEBUG LOGS FOR L2 Account Upgrade TRANSACTION *********");
            logger.info("ResponseCode: " + response.getResponseCode());
        } else {
            logger.info("******* DEBUG LOGS FOR  L2 Account Upgrade TRANSACTION AUTHENTICATION *********");
            response = new L2AccountUpgradeResponse();
            response.setResponseCode("420");
            response.setResponseDescription("Request is not authenticated");
            logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
        }
        } else {
            logger.info("******* DEBUG LOGS FOR L2 Account Upgrade TRANSACTION *********");
            response = new L2AccountUpgradeResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("L2 Account Upgrade  Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @Override
    public AccountDetailResponse accountDetail(AccountDetails request) {
        long start = System.currentTimeMillis();

        AccountDetailResponse response = null;

        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());

        logger.info("Start Processing Account Detail Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Account Details Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getCnic())
                .append(request.getStrockTrading())
                .append(request.getMutualFunds())
                .append(request.getReserved())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAccountDetail(request);
                    response = integrationService.accountDetailResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR  Account Details TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR   Account Details TRANSACTION AUTHENTICATION *********");
                response = new AccountDetailResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Account Details TRANSACTION *********");
            response = new AccountDetailResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Account Details Request  Processed in : {} ms {}", end, response);

        return response;
    }

//    @Override
//    public CustomerNameUpdateResponse customerNameUpdate(CustomerNameUpdateRequest request) {
//        long start = System.currentTimeMillis();
//
//        CustomerNameUpdateResponse response = null;
//
//        String requestXML = XMLUtil.convertRequest(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Customer Name Update Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getFirstName())
//                .append(request.getLastName())
//                .append(request.getReserved())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateCustomerNameUpdate(request);
//                    response = integrationService.customerNameUpdateResponse(request);
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR  Customer Name Update TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Customer Name Update TRANSACTION AUTHENTICATION *********");
//                response = new CustomerNameUpdateResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR  Customer Name Update TRANSACTION *********");
//            response = new CustomerNameUpdateResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Customer Name Update Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }

    @Override
    public ChequeBookResponse chequeBook(ChequeBookRequest request) {
        logger.info("Start Processing Cheque Book Status Update Transaction Request with {}");
        long start = System.currentTimeMillis();
        ChequeBookResponse response = new ChequeBookResponse();
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Cheque Book Status Update Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));

//        logger.info("Start Processing Cheque Book Status Update  Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getMobileNumber() +
                        request.getRrn() +
                        request.getOrignalTransactionRRN() +
                        request.getChannelId() +
                        request.getTerminalId() +
                        request.getTransactionStatus() +
                        request.getReserved1() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (sha256hex.equalsIgnoreCase(request.getHashData())) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {

                try {
                    HostRequestValidator.validateChequeBook(request);
                    //for Mock
                    response = integrationService.chequeBookResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR Cheque Book Status Update TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Cheque Book Status Update TRANSACTION AUTHENTICATION *********");
                response = new ChequeBookResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Cheque Book Status Update TRANSACTION *********");
            response = new ChequeBookResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Cheque Book Status Update Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }
}

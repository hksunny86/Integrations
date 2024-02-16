package com.inov8.integration.middleware.controller;

import com.inov8.integration.middleware.controller.validator.RequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.service.ClientIntegrationService;
import com.inov8.integration.webservice.controller.WebServiceSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("JsIntegrationController")
public class JsIntegrationController implements WebServiceSwitchController {
    private static Logger logger = LoggerFactory.getLogger(JsIntegrationController.class.getSimpleName());

    @Autowired
    ClientIntegrationService integrationService;

    @Override
    public WebServiceVO verifyAccount(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Verify AccountTransaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Verify Account Request");
            RequestValidator.validateVerifyAccount(webServiceVO);
            webServiceVO = integrationService.accountVerify(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR VERIFY ACCOUNT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Verify Account Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO accountOpening(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Account Opening Transaction Request with {}", webServiceVO);
//        byte[] b=null;
//        String s=null;
//        try {
//             b = (webServiceVO.getPresentAddress()).getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        try {
//            s = new String(b, "US-ASCII");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.out.print(s);

        try {

            logger.info("Validating Transaction Account Opening Request");
            RequestValidator.validateAccountOpening(webServiceVO);
            webServiceVO = integrationService.accountOpening(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());

            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR ACCOUNT OPENING TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Account Opening Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }


    @Override
    public WebServiceVO conventionalAccountOpening(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Conventional Account Opening Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Conventional Account Opening Request");
            RequestValidator.validateConventionalAccountOpening(webServiceVO);
            webServiceVO = integrationService.conventionalAccountOpening(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());

            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR CONVENTIONAL ACCOUNT OPENING TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Conventional Account Opening Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }


    @Override
    public WebServiceVO paymentInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Payment Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.paymentInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Payment Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO paymentRequest(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Account Opening Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Request");
            RequestValidator.validatePaymentRequest(webServiceVO);
            webServiceVO = integrationService.payment(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO paymentReversal(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing PaymentReversal Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction PaymentReversal Request");
            RequestValidator.validatePaymentReversalRequest(webServiceVO);
            webServiceVO = integrationService.paymentReversal(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT REVERSAL TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Payment Reversal Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO otpVerification(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing OTP Verification Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating OTP Verification Request");
            RequestValidator.validateOtpRequest(webServiceVO);
            webServiceVO = integrationService.otpVerification(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR OTP VERIFICATION TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("OTP Verification Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO cardTagging(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Card Tagging Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Card Tagging Request");
            RequestValidator.validateCardTagging(webServiceVO);
            webServiceVO = integrationService.cardTagging(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR CARD TAGGING TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Card Tagging Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO accountLinkDelink(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Account Link De-Link Request with {}", webServiceVO);

        try {

            logger.info("Validating Account Link De-Link Request");
            RequestValidator.validateAccountLinkDeLink(webServiceVO);
            webServiceVO = integrationService.accountLinkDeLink(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR ACCOUNT LINK DE-LINK TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Account Link De-Link Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO setCardStatus(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Set Card Status Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Set Card Status Request");
            RequestValidator.validateSetCardStatus(webServiceVO);
            webServiceVO = integrationService.setCardStatus(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());

            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR SET CARD STATUS TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Set Card Status Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO challanPaymentInquiry(WebServiceVO webServiceVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Payment Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.challanPaymentInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Payment Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO walletToCnic(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Wallet To Cnic Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.walletToCnic(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR WALLET TO CNIC PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Cnic Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO walletToCnicInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Wallet To Cnic Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.walletToCnicInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR WALLET TO CNIC PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Cnic Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }


    @Override
    public WebServiceVO walletToCore(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Wallet To Core Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.walletToCore(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR WALLET TO Core PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Core Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO walletToCoreInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Wallet To Core Inquiry Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.walletToCoreInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR WALLET TO Core Inquiry PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Wallet To Core Inquiry Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO debitCardIssuance(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Debit Card Issuance Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.debitCardIssuance(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Payment Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO debitCardIssuanceInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Debit Card Issuance Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.debitCardIssuanceInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Payment Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO hraRegistration(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing HRA Registration Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.hraRegistrationInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("HRA Registration  Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;

    }

    @Override
    public WebServiceVO hraRegistrationInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing HRA Registration  Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.hraRegistration(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("HRA Registration  Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO generateOTP(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO balanceInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO miniStatement(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO billPaymentInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO billPayment(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO cashInInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO cashIn(WebServiceVO webServiceVO) {
        return null;
    }


    @Override
    public WebServiceVO TitleFetch(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO cashInAgent(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO cashOutInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO cashOut(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO mpinRegistration(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO mpinChange(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO upgradeAccount(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO walletToWalletPaymentInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO walletToWalletPayment(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO upgradeAccountInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO accountStatusChange(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO ibftTitleFetch(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO ibftAdvice(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO challanPayment(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Challan Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.challanPayment(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Payment Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO hraToWallet(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing HRA TO Wallet Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.hraToWallet(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR HRA TO WALLET PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("HRA TO Wallet Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO hraToWalletInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing HRA TO Wallet Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.hraToWalletInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR HRA TO WALLET PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("HRA TO Wallet Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO debitInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Debit Inquiry Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.debitInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Debit Inquiry PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Debit Inquiry Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO debit(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Debit Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.debit(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Debit PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Debit Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentBillPaymentInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Bill Payment Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Agent Bill Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.agentBillPaymentInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Bill Payment Inquiry TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Bill Payment Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentBillPayment(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Bill Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Agent Bill Payment Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.agentBillPayment(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Bill Payment TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Bill Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO creditInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Credit Inquiry Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.creditInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Credit Inquiry PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Credit Inquiry Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO credit(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Credit Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.credit(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Credit PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Credit Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO hraCashWithDrawlInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing HRA Cash Withdrawal Inquiry Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.hraCashWithdrawalInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR HRA Cash Withdrawal Inquiry PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("HRA Cash Withdrawal Inquiry Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO hraCashWithDrawl(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing HRA Cash Withdrawal Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Transaction Payment Inquiry Request");
            RequestValidator.validatePaymentInquiry(webServiceVO);
            webServiceVO = integrationService.hraCashWithdrawal(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR HRA Cash Withdrawal PAYMENT TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("HRA Cash Withdrawal Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO accountAuthentication(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO loginPin(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Login PIN Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Login Pin Transaction  Request");
            RequestValidator.validateLoginPin(webServiceVO);
            webServiceVO = integrationService.loginPin(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Login Pin TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Login Pin Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO loginPinChange(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Login PIN Change Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Login Pin Change Transaction  Request");
            RequestValidator.validateLoginPinChange(webServiceVO);
            webServiceVO = integrationService.loginPinChange(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Login Pin Change TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Login Pin Change Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO resetPin(WebServiceVO webServiceVO) {

        long start = System.currentTimeMillis();
        logger.info("Start Processing Reset PIN Change Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Reset Pin Change Transaction  Request");
            RequestValidator.validateResetPin(webServiceVO);
            webServiceVO = integrationService.resetPin(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Reset Pin Change TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Reset Pin Change Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO advanceLoanSalary(WebServiceVO webServiceVO) {

        long start = System.currentTimeMillis();
        logger.info("Start Processing Advance Loan Salary Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Advance Loan Salary Transaction  Request");
            RequestValidator.validateAdvanceLoanSalary(webServiceVO);
            webServiceVO = integrationService.advanceLoanSalary(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Advance Loan Salary TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Advance Loan Salary Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }


    @Override
    public WebServiceVO smsGeneration(WebServiceVO webServiceVO) {

        long start = System.currentTimeMillis();
        logger.info("Start Processing SMS Generation Request Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating SMS Generation Request Transaction  Request");
            RequestValidator.validateSmsGeneration(webServiceVO);
            webServiceVO = integrationService.smsGeneration(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR SMS Generation Request TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("SMS Generation Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentAccountLogin(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Account Login Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Login Transaction  Request");
            RequestValidator.validateAgentLoginPin(webServiceVO);
            webServiceVO = integrationService.agentAccountLogin(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Account Login TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet Account Login Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentLoginPinGeneration(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Login PIN Generation Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Login Transaction  Request");
            RequestValidator.validateAgentLoginPin(webServiceVO);
            webServiceVO = integrationService.agentLoginPinGeneration(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Login PIN Generation TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet Login PIN Generation Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentLoginPinReset(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Login PIN Reset Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Login PIN Reset Transaction  Request");
            RequestValidator.validateAgentLoginPinReset(webServiceVO);
            webServiceVO = integrationService.agentLoginPinReset(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Login PIN Reset TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet Login PIN Reset Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentMpinGeneration(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent MPIN Generation Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent MPIN Generation Transaction  Request");
            RequestValidator.validateAgentMpinGeneration(webServiceVO);
            webServiceVO = integrationService.agentMpinGeneration(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent MPIN Generation TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet MPIN Generation Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentMpinReset(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent MPIN Reset Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent MPIN Reset Transaction  Request");
            RequestValidator.validateAgentMpinReset(webServiceVO);
            webServiceVO = integrationService.agentMpinReset(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent MPIN Reset TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet MPIN Reset Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

//    @Override
//    public WebServiceVO agentMpinVerification(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent MPIN Verification Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent MPIN Verification Transaction  Request");
//            RequestValidator.validateAgentMpinVerification(webServiceVO);
//            webServiceVO = integrationService.agentMpinVerification(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent MPIN Verification TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet MPIN Verification Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }

    @Override
    public WebServiceVO agentBalanceInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Balance Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Balance Inquiry Transaction  Request");
            RequestValidator.validateAgentBalanceInquiry(webServiceVO);
            webServiceVO = integrationService.agentBalanceInquriy(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Balance Inquiry TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet Balance Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

//    @Override
//    public WebServiceVO agentToAgentInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent To Agent Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent To Agent Inquiry Transaction  Request");
//            RequestValidator.validateAgentToAgentInquiry(webServiceVO);
//            webServiceVO = integrationService.agentToAgentInquriy(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent To Agent Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet To Agent Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentToAgentPayment(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent To Agent Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent To Agent Payment Transaction  Request");
//            RequestValidator.validateAgentToAgentPayment(webServiceVO);
//            webServiceVO = integrationService.agentToAgentPayment(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent To Agent Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet To Agent Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentAccountOpening(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Account Opening Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Account Opening Transaction  Request");
//            RequestValidator.validateAgentAccountOpening(webServiceVO);
//            webServiceVO = integrationService.agentAccountOpening(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Account Opening TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Account Opening Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentUpgradeAccountInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Upgrade Account Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Upgrade Account Inquiry Transaction  Request");
//            RequestValidator.validateAgentUpgradeAccountInquiry(webServiceVO);
//            webServiceVO = integrationService.agentUpgradeAccountInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Upgrade Account Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Upgrade Account Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentUpgradeAccount(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Upgrade  Account Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Upgrade Account Transaction  Request");
//            RequestValidator.validateAgentUpgradeAccount(webServiceVO);
//            webServiceVO = integrationService.agentUpgradeAccount(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Upgrade Account TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Upgrade Account Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentCashInInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Cash In Inquiry  Account Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Cash In Inquriy Transaction  Request");
//            RequestValidator.validateAgentCashInInquiry(webServiceVO);
//            webServiceVO = integrationService.agentCashInInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Cash In Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Cash In Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentCashIn(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Cash In Payment  Account Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Cash In Payment Transaction  Request");
//            RequestValidator.validateAgentCashIn(webServiceVO);
//            webServiceVO = integrationService.agentCashIn(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Cash In Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Cash In Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentCashOutInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Cash Out Inquiry Account Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Cash Out Inquiry Transaction  Request");
//            RequestValidator.validateAgentCashOutInquiry(webServiceVO);
//            webServiceVO = integrationService.agentCashOutInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Cash Out Inquiry Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Cash In Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentCashOut(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Cash Out Payment Account Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Cash Out Payment Transaction  Request");
//            RequestValidator.validateAgentCashOut(webServiceVO);
//            webServiceVO = integrationService.agentCashOut(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Cash Out Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Cash Out Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentWalletToWalletInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Wallet To Wallet Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Wallet To Wallet Inquiry Transaction  Request");
//            RequestValidator.validateAgentWalletToWalletInquiry(webServiceVO);
//            webServiceVO = integrationService.agentWalletToWalletInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Wallet To Wallet Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Wallet To Wallet Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentWalletToWalletPayment(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Wallet To Wallet Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Wallet To Wallet Payment Transaction  Request");
//            RequestValidator.validateAgentWalletToWalletPayment(webServiceVO);
//            webServiceVO = integrationService.agentWalletToWalletInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Wallet To Wallet Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Wallet To Wallet Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentWalletToCnicInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Wallet To Cnic Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Wallet To Cnic Inquiry Transaction  Request");
//            RequestValidator.validateAgentWalletToCnicInquiry(webServiceVO);
//            webServiceVO = integrationService.agentWalletToCnicInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Wallet To Cnic Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Wallet To Cnic Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentWalletToCnicPayment(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Wallet To Cnic Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Wallet To Cnic Payment Transaction  Request");
//            RequestValidator.validateAgentWalletToCnicPayment(webServiceVO);
//            webServiceVO = integrationService.agentWalletToCnicPayment(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Wallet To Cnic Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Wallet To Cnic Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }

    @Override
    public WebServiceVO agentIbftInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Ibft Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Ibft Inquiry Payment Transaction  Request");
            RequestValidator.validateAgentIbftInquiry(webServiceVO);
            webServiceVO = integrationService.agentIbftInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Ibft Inquiry TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet Ibft Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentIbftPayment(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Ibft Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Ibft Payment Transaction  Request");
            RequestValidator.validateAgentIbftPayment(webServiceVO);
            webServiceVO = integrationService.agentIbftPayment(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Ibft Payment TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet Ibft Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

//    @Override
//    public WebServiceVO agentRetailPaymentInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Retail Payment Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Retail Payment Inquiry Transaction  Request");
//            RequestValidator.validateAgentRetailPaymentInquiry(webServiceVO);
//            webServiceVO = integrationService.agentRetailPaymentInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Retail Payment Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Retail Payment Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentRetailPayment(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Retail Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Retail Payment Transaction  Request");
//            RequestValidator.validateAgentRetailPayment(webServiceVO);
//            webServiceVO = integrationService.agentRetailPayment(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Retail Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Retail Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentWalletToCoreInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Wallet To Core Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Wallet To Core Inquiry Transaction  Request");
//            RequestValidator.validateAgentWalletToCoreInquiry(webServiceVO);
//            webServiceVO = integrationService.agentWalletToCoreInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Wallet To Core Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Wallet To Core Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentWalletToCorePayment(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Wallet To Core Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Wallet To Core Payment Transaction  Request");
//            RequestValidator.validateAgentWalletToCorePayment(webServiceVO);
//            webServiceVO = integrationService.agentWalletToCorePayment(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Wallet To Core Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Wallet To Core Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentReceiveMoneyInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Receive Money Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Receive Money Inquiry Transaction  Request");
//            RequestValidator.validateAgentReceiveMoneyInquiry(webServiceVO);
//            webServiceVO = integrationService.agentReceiveMoneyInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Receive Money Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Receive Money Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentReceiveMoneyPayment(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Receive Money Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Receive Money Payment Transaction  Request");
//            RequestValidator.validateAgentReceiveMoneyPayment(webServiceVO);
//            webServiceVO = integrationService.agentReceiveMoneyPayment(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Receive Money Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Receive Money Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }

//    @Override
//    public WebServiceVO agentCnicToCnicInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Cnic To Cnic Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Cnic To Cnic Inquiry Transaction  Request");
//            RequestValidator.validateAgentCnicToCnicInquiry(webServiceVO);
//            webServiceVO = integrationService.agentCnicToCnicInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Cnic To Cnic Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cnic To Cnic Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentCnicToCnicPayment(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Cnic To Cnic Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Cnic To Cnic Payment Transaction  Request");
//            RequestValidator.validateAgentCnicToCnicPayment(webServiceVO);
//            webServiceVO = integrationService.agentCnicToCnicPayment(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Cnic To Cnic Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cnic To Cnic Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }


//    @Override
//    public WebServiceVO agentHRARegistrationInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent HRA Registration  Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Transaction Agent HRA Registration Inquiry Request");
//            RequestValidator.validateAgentHRARegistrationInquiry(webServiceVO);
//            webServiceVO = integrationService.agentHRARegistrationInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent HRA Registration  Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentHRARegistration(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent HRA Registration Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Transaction Agent HRA Registration Payment Request");
//            RequestValidator.validateAgentHRARegistration(webServiceVO);
//            webServiceVO = integrationService.agentHRARegistration(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR PAYMENT INQUIRY TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent HRA Registration Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentCnicToCoreInquiry(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Cnic To Core Inquiry Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Cnic To Core Inquiry Transaction  Request");
//            RequestValidator.validateAgentCnicToCoreInquiry(webServiceVO);
//            webServiceVO = integrationService.agentCnicToCoreInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Cnic To Core Inquiry TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agent Cnic To Core Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }
//
//    @Override
//    public WebServiceVO agentCnicToCore(WebServiceVO webServiceVO) {
//        return null;
//    }


//    @Override
//    public WebServiceVO agentCnicToCorePayment(WebServiceVO webServiceVO) {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Agent Cnic To Core Payment Transaction Request with {}", webServiceVO);
//
//        try {
//
//            logger.info("Validating Agent Cnic To Core Payment Transaction  Request");
//            RequestValidator.validateAgentCnicToCorePayment(webServiceVO);
//            webServiceVO = integrationService.agentCashDepositInquiry(webServiceVO);
//
//        } catch (ValidationException ve) {
//            webServiceVO.setResponseCode("420");
//            webServiceVO.setResponseCodeDescription(ve.getMessage());
//            logger.error("ERROR: Request Validation", ve);
//        } catch (Exception e) {
//            webServiceVO.setResponseCode("220");
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//
//        logger.info("******* DEBUG LOGS FOR Agent Cnic To Core Payment TRANSACTION *********");
//        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
//        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Agnet Cnic To Core Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);
//
//        return webServiceVO;
//    }

    @Override
    public WebServiceVO agentCashDepositInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Cash In Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Cash In Inquiry Transaction  Request");
            RequestValidator.validateAgentCashDepositInquiry(webServiceVO);
            webServiceVO = integrationService.agentCashDepositInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Cash In Inquiry TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet Cash In Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentCashDepositPayment(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Cash In Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Cash In Payment Transaction  Request");
            RequestValidator.validateAgentCashDepositPayment(webServiceVO);
            webServiceVO = integrationService.agentCashDepositPayment(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Cash In Payment TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agnet Cash In Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentCashWithdrawalInquiry(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Cash Withdrawal Inquiry Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Cash Withdrawal Inquiry Transaction  Request");
            RequestValidator.validateAgentCashWithdrawalInquiry(webServiceVO);
            webServiceVO = integrationService.agentCashWithdrawalInquiry(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Cash Withdrawal Inquiry TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Cash Withdrawal Inquiry Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentCashWithdrawalPayment(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Agent Cash Withdrawal Payment Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Agent Cash Withdrawal Payment Transaction  Request");
            RequestValidator.validateAgentCashWithdrawalPayment(webServiceVO);
            webServiceVO = integrationService.agentCashWithdrawalPayment(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Agent Cash Withdrawal Payment TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Agent Cash Withdrawal Payment Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO mpinVerification(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing MPIN Verification Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating MPIN Verification Transaction  Request");
            RequestValidator.validateMpinVerification(webServiceVO);
            webServiceVO = integrationService.mpinVerification(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR MPIN Verification TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("MPIN Verification Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO listSegments(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Segment List Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Segment List Transaction  Request");
            RequestValidator.validateSegmentList(webServiceVO);
            webServiceVO = integrationService.segmentList(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Segment List TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Segment List Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO listCatalogs(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Catalog List Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating Catalog List Transaction  Request");
            RequestValidator.validateCatalogList(webServiceVO);
            webServiceVO = integrationService.catalogList(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR Catalog List TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("Catalog List Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO l2AccountOpening(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing L2 Account Opening Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating L2 Account Opening Transaction  Request");
            RequestValidator.validatel2AccountOpening(webServiceVO);
            webServiceVO = integrationService.l2AccountOpening(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR L2 Account Opening TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("L2 Account Opening Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO l2AccountUpgrade(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing L2 Account Upgrade Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating L2 Account Upgrade Transaction  Request");
            RequestValidator.validatel2AccountUpgrade(webServiceVO);
            webServiceVO = integrationService.l2AccountUpgrade(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR L2 Account Upgrade TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("L2 Account Upgrade Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO accountDetail(WebServiceVO webServiceVO) {
        long start = System.currentTimeMillis();
        logger.info("Start Processing  Account Detail Transaction Request with {}", webServiceVO);

        try {

            logger.info("Validating  Account Detail Transaction  Request");
            RequestValidator.validatel2AccountDetail(webServiceVO);
            webServiceVO = integrationService.accountDetail(webServiceVO);

        } catch (ValidationException ve) {
            webServiceVO.setResponseCode("420");
            webServiceVO.setResponseCodeDescription(ve.getMessage());
            logger.error("ERROR: Request Validation", ve);
        } catch (Exception e) {
            webServiceVO.setResponseCode("220");
            webServiceVO.setResponseCodeDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        logger.info("******* DEBUG LOGS FOR L2 Account Upgrade TRANSACTION *********");
        logger.info("ResponseCode: " + webServiceVO.getResponseCode());
        logger.info("Microbank Transaction Code: " + webServiceVO.getMicrobankTransactionCode());

        long end = System.currentTimeMillis() - start;
        logger.info("L2 Account Upgrade Transaction Request  Processed in : {} ms {}", end, webServiceVO);

        return webServiceVO;
    }

    @Override
    public WebServiceVO customerNameUpdate(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO clsStatusUpdate(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO blinkAccountVerificationInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO blinkAccountVerification(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO debitCardStatusVerification(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO advanceLoanPaymentSettlement(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO feePaymentInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO feePayment(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO fundWalletToCoreInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO fundWalletToCore(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO updateMinorAccount(WebServiceVO webServiceVO) {
        return null;
    }

//    @Override
//    public WebServiceVO minorFatherBvsVerification(WebServiceVO webServiceVO) {
//        return null;
//    }
//
//
//
//    @Override
//    public WebServiceVO checqueBookStatus(WebServiceVO webServiceVO) {
//        return null;
//    }

    @Override
    public WebServiceVO verifyLoginAccount(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO minorFatherBvsVerification(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO checqueBookStatus(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO cnicTo256(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO transactionStatus(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO profileStatus(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO lienStatus(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO initiateLoan(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO selectLoanOffer(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO selectLoan(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO payLoan(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO outstandingLoanStatus(WebServiceVO webServiceVO) {
        return null;
    }


    @Override
    public WebServiceVO loanPlan(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO loanHistory(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO transactionActive(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO loanCallBack(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO simpleAccountOpening(WebServiceVO webServiceVO) {
        return null;
    }

    //
//    @Override
//    public WebServiceVO getOutstandingLoan(WebServiceVO webServiceVO) {
//        return null;
//    }
//
    @Override
    public WebServiceVO MerchantAccountUpgrade(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO merchantPictureUpgrade(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO accountStatus(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO getOutstandingLoan(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO updateCnicExpiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO l2AccountUpgradeDiscrepant(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO getL2AccountUpgradeDiscrepant(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO thirdPartyCreditInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO thirdPartyCredit(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO customerCliStatus(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO customerValidation(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO digiWalletStatement(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO accountInfo(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO glToGl(WebServiceVO webServiceVO) {
        return null;
    }
}

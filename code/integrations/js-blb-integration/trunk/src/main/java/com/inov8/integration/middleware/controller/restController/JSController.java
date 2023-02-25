package com.inov8.integration.middleware.controller.restController;

import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.service.hostService.HostIntegrationService;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.middleware.util.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class JSController {

    private static Logger logger = LoggerFactory.getLogger(JSController.class.getSimpleName());

    @Autowired
    HostIntegrationService integrationService;

    @RequestMapping(value = "api/CustomerNameUpdate", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    CustomerNameUpdateResponse customerNameUpdateResponse(@RequestBody CustomerNameUpdateRequest request) {

        CustomerNameUpdateResponse customerNameUpdateResponse = new CustomerNameUpdateResponse();
        long start = System.currentTimeMillis();

        logger.info("Customer Name Update Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Customer Name Update Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getFirstName())
                .append(request.getLastName())
                .append(request.getReserved())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateCustomerNameUpdate(request);
                    customerNameUpdateResponse = integrationService.customerNameUpdateResponse(request);

                } catch (ValidationException ve) {
                    customerNameUpdateResponse.setResponseCode("420");
                    customerNameUpdateResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    customerNameUpdateResponse.setResponseCode("220");
                    customerNameUpdateResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR  Customer Name Update TRANSACTION *********");
                logger.info("ResponseCode: " + customerNameUpdateResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Customer Name Update TRANSACTION AUTHENTICATION *********");
                customerNameUpdateResponse = new CustomerNameUpdateResponse();
                customerNameUpdateResponse.setResponseCode("420");
                customerNameUpdateResponse.setResponseDescription("Request is not authenticated");
                customerNameUpdateResponse.setRrn(request.getRrn());
                customerNameUpdateResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Customer Name Update TRANSACTION *********");
            customerNameUpdateResponse = new CustomerNameUpdateResponse();
            customerNameUpdateResponse.setResponseCode("111");
            customerNameUpdateResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Customer Name Update Request  Processed in : {} ms {}", end, customerNameUpdateResponse);

        return customerNameUpdateResponse;
    }


    @RequestMapping(value = "api/CLSStatusUpdate", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    CLSStatusUpdateResponse clsStatusUpdateResponse(@RequestBody CLSStatusUpdateRequest request) {

        CLSStatusUpdateResponse clsStatusUpdateResponse = new CLSStatusUpdateResponse();
        long start = System.currentTimeMillis();

        logger.info("CLS Status Update Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing CLS Status Update Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getCaseStatus())
                .append(request.getCaseId())
                .append(request.getClsComment())
                .append(request.getReserved())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
        if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
            try {
                HostRequestValidator.validateClsStatusUpdate(request);
                clsStatusUpdateResponse = integrationService.clsStatusUpdateResponse(request);

            } catch (ValidationException ve) {
                clsStatusUpdateResponse.setResponseCode("420");
                clsStatusUpdateResponse.setResponseDescription(ve.getMessage());

                logger.error("ERROR: Request Validation", ve);
            } catch (Exception e) {
                clsStatusUpdateResponse.setResponseCode("220");
                clsStatusUpdateResponse.setResponseDescription(e.getMessage());
                logger.error("ERROR: General Processing ", e);
            }

            logger.info("******* DEBUG LOGS FOR  CLS Status Update TRANSACTION *********");
            logger.info("ResponseCode: " + clsStatusUpdateResponse.getResponseCode());
        } else {
            logger.info("******* DEBUG LOGS FOR  CLS Status Update TRANSACTION AUTHENTICATION *********");
            clsStatusUpdateResponse = new CLSStatusUpdateResponse();
            clsStatusUpdateResponse.setResponseCode("420");
            clsStatusUpdateResponse.setResponseDescription("Request is not authenticated");
            clsStatusUpdateResponse.setRrn(request.getRrn());
            clsStatusUpdateResponse.setResponseDateTime(request.getDateTime());
            logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

        }
//        } else {
//            logger.info("******* DEBUG LOGS FOR CLS Status Update TRANSACTION *********");
//            clsStatusUpdateResponse = new CLSStatusUpdateResponse();
//            clsStatusUpdateResponse.setResponseCode("111");
//            clsStatusUpdateResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }

        long end = System.currentTimeMillis() - start;
        logger.info("CLS Status Update Request  Processed in : {} ms {}", end, clsStatusUpdateResponse);

        return clsStatusUpdateResponse;
    }

    @RequestMapping(value = "api/BlinkAccountVerificationInquiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    BlinkAccountVerificationInquiryResponse blinkAccountVerificationInquiryResponse(@RequestBody BlinkAccountVerificationInquiryRequest request) {

        BlinkAccountVerificationInquiryResponse blinkAccountVerificationInquiryResponse = new BlinkAccountVerificationInquiryResponse();
        long start = System.currentTimeMillis();

        logger.info("Blink Account Verification Inquiry Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Blink Account Verification Inquiry Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getCnic())
                .append(request.getReserved())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateBlinkAccountVerificationInquiry(request);
                    blinkAccountVerificationInquiryResponse = integrationService.blinkAccountVerificationInquiryResponse(request);

                } catch (ValidationException ve) {
                    blinkAccountVerificationInquiryResponse.setResponseCode("420");
                    blinkAccountVerificationInquiryResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    blinkAccountVerificationInquiryResponse.setResponseCode("220");
                    blinkAccountVerificationInquiryResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR  Blink Account Verification Inquiry TRANSACTION *********");
                logger.info("ResponseCode: " + blinkAccountVerificationInquiryResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Blink Account Verification Inquiry TRANSACTION AUTHENTICATION *********");
                blinkAccountVerificationInquiryResponse = new BlinkAccountVerificationInquiryResponse();
                blinkAccountVerificationInquiryResponse.setResponseCode("420");
                blinkAccountVerificationInquiryResponse.setResponseDescription("Request is not authenticated");
                blinkAccountVerificationInquiryResponse.setRrn(request.getRrn());
                blinkAccountVerificationInquiryResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Blink Account Verification Inquiry TRANSACTION *********");
            blinkAccountVerificationInquiryResponse = new BlinkAccountVerificationInquiryResponse();
            blinkAccountVerificationInquiryResponse.setResponseCode("111");
            blinkAccountVerificationInquiryResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Blink Account Verification Inquiry Request  Processed in : {} ms {}", end, blinkAccountVerificationInquiryResponse);

        return blinkAccountVerificationInquiryResponse;
    }

    @RequestMapping(value = "api/BlinkAccountVerification", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    BlinkAccountVerificationResponse blinkAccountVerificationResponse(@RequestBody BlinkAccountVerificationRequest request) {

        BlinkAccountVerificationResponse blinkAccountVerificationResponse = new BlinkAccountVerificationResponse();
        long start = System.currentTimeMillis();

        logger.info("Blink Account Verification Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Blink Account Verification Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getCnic())
                .append(request.getFingerIndex())
                .append(request.getFingerTemplate())
                .append(request.getTemplateType())
                .append(request.getConsumerName())
                .append(request.getFatherHusbandName())
                .append(request.getMotherMaiden())
                .append(request.getGender())
                .append(request.getReserved())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
        if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
            try {
                HostRequestValidator.validateBlinkAccountVerification(request);
                blinkAccountVerificationResponse = integrationService.blinkAccountVerificationResponse(request);

            } catch (ValidationException ve) {
                blinkAccountVerificationResponse.setResponseCode("420");
                blinkAccountVerificationResponse.setResponseDescription(ve.getMessage());

                logger.error("ERROR: Request Validation", ve);
            } catch (Exception e) {
                blinkAccountVerificationResponse.setResponseCode("220");
                blinkAccountVerificationResponse.setResponseDescription(e.getMessage());
                logger.error("ERROR: General Processing ", e);
            }

            logger.info("******* DEBUG LOGS FOR  Blink Account Verification TRANSACTION *********");
            logger.info("ResponseCode: " + blinkAccountVerificationResponse.getResponseCode());
        } else {
            logger.info("******* DEBUG LOGS FOR  Blink Account Verification TRANSACTION AUTHENTICATION *********");
            blinkAccountVerificationResponse = new BlinkAccountVerificationResponse();
            blinkAccountVerificationResponse.setResponseCode("420");
            blinkAccountVerificationResponse.setResponseDescription("Request is not authenticated");
            blinkAccountVerificationResponse.setRrn(request.getRrn());
            blinkAccountVerificationResponse.setResponseDateTime(request.getDateTime());
            logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

        }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Blink Account Verification TRANSACTION *********");
//            blinkAccountVerificationResponse = new BlinkAccountVerificationResponse();
//            blinkAccountVerificationResponse.setResponseCode("111");
//            blinkAccountVerificationResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }

        long end = System.currentTimeMillis() - start;
        logger.info("Blink Account Verification Request  Processed in : {} ms {}", end, blinkAccountVerificationResponse);

        return blinkAccountVerificationResponse;
    }


    @RequestMapping(value = "api/DebitCardStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    DebitCardStatusReponse debitCardStatusResponse(@RequestBody DebitCardStatusRequest request) {

        DebitCardStatusReponse debitCardStatusResponse = new DebitCardStatusReponse();
        long start = System.currentTimeMillis();

        logger.info("Debit Card Status Verification Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Debit Card Status Verification Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getCnic())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateDebitCardStatusVerification(request);
                    debitCardStatusResponse = integrationService.debitCardStatusReponse(request);

                } catch (ValidationException ve) {
                    debitCardStatusResponse.setResponseCode("420");
                    debitCardStatusResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    debitCardStatusResponse.setResponseCode("220");
                    debitCardStatusResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR  Debit Card Status Verification TRANSACTION *********");
                logger.info("ResponseCode: " + debitCardStatusResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Debit Card Status Verification TRANSACTION AUTHENTICATION *********");
                debitCardStatusResponse = new DebitCardStatusReponse();
                debitCardStatusResponse.setResponseCode("420");
                debitCardStatusResponse.setResponseDescription("Request is not authenticated");
                debitCardStatusResponse.setRrn(request.getRrn());
                debitCardStatusResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Blink Account Verification TRANSACTION *********");
            debitCardStatusResponse = new DebitCardStatusReponse();
            debitCardStatusResponse.setResponseCode("111");
            debitCardStatusResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Debit Card Status Verification Request  Processed in : {} ms {}", end, debitCardStatusResponse);

        return debitCardStatusResponse;
    }


    @RequestMapping(value = "api/AdvanceLoanEarlyPaymentSettlement", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    AdvanceLoanEarlyPyamentResponse advanceLoanEarlyPyamentResponse(@RequestBody AdvanceLoanEarlyPaymentSettlementRequest request) {

        AdvanceLoanEarlyPyamentResponse advanceLoanEarlyPyamentResponse = new AdvanceLoanEarlyPyamentResponse();
        long start = System.currentTimeMillis();

        logger.info("Advance Loan Payment Settlement Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Advance Loan Payment Settlement Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getCnic())
                .append(request.getReserved())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateAdvanceLoanPaymentSettlement(request);
                    advanceLoanEarlyPyamentResponse = integrationService.advanceLoanEarlyPyamentResponse(request);
                } catch (ValidationException ve) {
                    advanceLoanEarlyPyamentResponse.setResponseCode("420");
                    advanceLoanEarlyPyamentResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    advanceLoanEarlyPyamentResponse.setResponseCode("220");
                    advanceLoanEarlyPyamentResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR  Advance Loan Payment Settlement  TRANSACTION *********");
                logger.info("ResponseCode: " + advanceLoanEarlyPyamentResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Advance Loan Payment Settlement TRANSACTION AUTHENTICATION *********");
                advanceLoanEarlyPyamentResponse = new AdvanceLoanEarlyPyamentResponse();
                advanceLoanEarlyPyamentResponse.setResponseCode("420");
                advanceLoanEarlyPyamentResponse.setResponseDescription("Request is not authenticated");
                advanceLoanEarlyPyamentResponse.setRrn(request.getRrn());
                advanceLoanEarlyPyamentResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Advance Loan Payment Settlement Request TRANSACTION *********");
            advanceLoanEarlyPyamentResponse = new AdvanceLoanEarlyPyamentResponse();
            advanceLoanEarlyPyamentResponse.setResponseCode("111");
            advanceLoanEarlyPyamentResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Advance Loan Payment Settlement Request  Processed in : {} ms {}", end, advanceLoanEarlyPyamentResponse);

        return advanceLoanEarlyPyamentResponse;
    }


    @RequestMapping(value = "api/FeePaymentInquiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    FeePaymentInquiryResponse feePaymentInquiryResponse(@RequestBody FeePaymentInquiryRequest request) {

        FeePaymentInquiryResponse feePaymentInquiryResponse = new FeePaymentInquiryResponse();
        long start = System.currentTimeMillis();

        logger.info("Fee Payment  Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Fee Payment  Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPinType())
                .append(request.getCnic())
                .append(request.getCardFeeType())
                .append(request.getProductId())
                .append(request.getReserved1())
                .append(request.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateFeeInquiryPayment(request);
                    feePaymentInquiryResponse = integrationService.feePaymentInquiryResponse(request);

                } catch (ValidationException ve) {
                    feePaymentInquiryResponse.setResponseCode("420");
                    feePaymentInquiryResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    feePaymentInquiryResponse.setResponseCode("220");
                    feePaymentInquiryResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR  Fee Payment Request  TRANSACTION *********");
                logger.info("ResponseCode: " + feePaymentInquiryResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Fee Payment Request TRANSACTION AUTHENTICATION *********");
                feePaymentInquiryResponse = new FeePaymentInquiryResponse();
                feePaymentInquiryResponse.setResponseCode("420");
                feePaymentInquiryResponse.setResponseDescription("Request is not authenticated");
                feePaymentInquiryResponse.setRrn(request.getRrn());
                feePaymentInquiryResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Fee Payment Request Request TRANSACTION *********");
            feePaymentInquiryResponse = new FeePaymentInquiryResponse();
            feePaymentInquiryResponse.setResponseCode("111");
            feePaymentInquiryResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Fee Payment Request  Processed in : {} ms {}", end, feePaymentInquiryResponse);

        return feePaymentInquiryResponse;
    }

    @RequestMapping(value = "api/FeePayment", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    FeePaymentResponse feePaymentResponse(@RequestBody FeePaymentRequest request) {

        FeePaymentResponse feePaymentResponse = new FeePaymentResponse();
        long start = System.currentTimeMillis();

        logger.info("Fee Payment  Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Fee Payment  Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getMobileNumber())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getPinType())
                .append(request.getPin())
                .append(request.getCnic())
                .append(request.getProductId())
                .append(request.getCardfeetypeid())
                .append(request.getTransactionAmount())
                .append(request.getReserved1())
                .append(request.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateFeePayment(request);
                    feePaymentResponse = integrationService.feePaymentResponse(request);

                } catch (ValidationException ve) {
                    feePaymentResponse.setResponseCode("420");
                    feePaymentResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    feePaymentResponse.setResponseCode("220");
                    feePaymentResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR  Fee Payment Request  TRANSACTION *********");
                logger.info("ResponseCode: " + feePaymentResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Fee Payment Request TRANSACTION AUTHENTICATION *********");
                feePaymentResponse = new FeePaymentResponse();
                feePaymentResponse.setResponseCode("420");
                feePaymentResponse.setResponseDescription("Request is not authenticated");
                feePaymentResponse.setRrn(request.getRrn());
                feePaymentResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Fee Payment Request Request TRANSACTION *********");
            feePaymentResponse = new FeePaymentResponse();
            feePaymentResponse.setResponseCode("111");
            feePaymentResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Fee Payment Request  Processed in : {} ms {}", end, feePaymentResponse);

        return feePaymentResponse;
    }

//    @RequestMapping(value = "api/transactionStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    TransactionStatusResponse transactionStatusResponse(@RequestBody TransactionStatusRequest request) {
//        TransactionStatusResponse transactionStatusResponse = new TransactionStatusResponse();
//
//        long start = System.currentTimeMillis();
//
//        logger.info("Transaction Status Request Received at Controller at time: " + start);
//        String requestXML = JSONUtil.getJSON(request);
////        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Transaction Status Request with {}", requestXML);
//
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getCustomerId())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5())
//                .append(request.getReserved6())
//                .append(request.getReserved7())
//                .append(request.getReserved8())
//                .append(request.getReserved9())
//                .append(request.getReserved10());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateTransactionStatus(request);
//                    transactionStatusResponse = integrationService.transactionStatusResponse(request);
//
//                } catch (ValidationException ve) {
//                    transactionStatusResponse.setResponseCode("420");
//                    transactionStatusResponse.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    transactionStatusResponse.setResponseCode("220");
//                    transactionStatusResponse.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR  Transaction Status Request *********");
//                logger.info("ResponseCode: " + transactionStatusResponse.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Transaction Status Request AUTHENTICATION *********");
//                transactionStatusResponse = new TransactionStatusResponse();
//                transactionStatusResponse.setResponseCode("420");
//                transactionStatusResponse.setResponseDescription("Request is not authenticated");
//                transactionStatusResponse.setRrn(request.getRrn());
//                transactionStatusResponse.setResponseDateTime(request.getDateTime());
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Transaction Status Request *********");
//            transactionStatusResponse = new TransactionStatusResponse();
//            transactionStatusResponse.setResponseCode("111");
//            transactionStatusResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Transaction Status Request Processed in : {} ms {}", end, transactionStatusResponse);
//
//        return transactionStatusResponse;
//    }
//
//    @RequestMapping(value = "api/profileStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    ProfileStatusResponse profileStatusResponse(@RequestBody ProfileStatusRequest request) {
//        ProfileStatusResponse profileStatusResponse = new ProfileStatusResponse();
//
//        long start = System.currentTimeMillis();
//
//        logger.info("Profile Status Request Received at Controller at time: " + start);
//        String requestXML = JSONUtil.getJSON(request);
////        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Profile Status Request with {}", requestXML);
//
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getCustomerId())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5())
//                .append(request.getReserved6())
//                .append(request.getReserved7())
//                .append(request.getReserved8())
//                .append(request.getReserved9())
//                .append(request.getReserved10());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateProfileStatus(request);
//                    profileStatusResponse = integrationService.profileStatusResponse(request);
//
//                } catch (ValidationException ve) {
//                    profileStatusResponse.setResponseCode("420");
//                    profileStatusResponse.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    profileStatusResponse.setResponseCode("220");
//                    profileStatusResponse.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Profile Status Request *********");
//                logger.info("ResponseCode: " + profileStatusResponse.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR Profile Status Request AUTHENTICATION *********");
//                profileStatusResponse = new ProfileStatusResponse();
//                profileStatusResponse.setResponseCode("420");
//                profileStatusResponse.setResponseDescription("Request is not authenticated");
//                profileStatusResponse.setRrn(request.getRrn());
//                profileStatusResponse.setResponseDateTime(request.getDateTime());
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Profile Status Request *********");
//            profileStatusResponse = new ProfileStatusResponse();
//            profileStatusResponse.setResponseCode("111");
//            profileStatusResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Profile Status Request Processed in : {} ms {}", end, profileStatusResponse);
//
//        return profileStatusResponse;
//    }
//
//    @RequestMapping(value = "api/lienStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    LienStatusResponse lienStatusResponse(@RequestBody LienStatusRequest request) {
//        LienStatusResponse lienStatusResponse = new LienStatusResponse();
//
//        long start = System.currentTimeMillis();
//
//        logger.info("Lien Status Request Received at Controller at time: " + start);
//        String requestXML = JSONUtil.getJSON(request);
////        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Lien Status Request with {}", requestXML);
//
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getCustomerId())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5())
//                .append(request.getReserved6())
//                .append(request.getReserved7())
//                .append(request.getReserved8())
//                .append(request.getReserved9())
//                .append(request.getReserved10());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateLienStatus(request);
//                    lienStatusResponse = integrationService.lienStatusResponse(request);
//
//                } catch (ValidationException ve) {
//                    lienStatusResponse.setResponseCode("420");
//                    lienStatusResponse.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    lienStatusResponse.setResponseCode("220");
//                    lienStatusResponse.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Lien Status Request *********");
//                logger.info("ResponseCode: " + lienStatusResponse.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR Lien Status Request AUTHENTICATION *********");
//                lienStatusResponse = new LienStatusResponse();
//                lienStatusResponse.setResponseCode("420");
//                lienStatusResponse.setResponseDescription("Request is not authenticated");
//                lienStatusResponse.setRrn(request.getRrn());
//                lienStatusResponse.setResponseDateTime(request.getDateTime());
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Lien Status Request *********");
//            lienStatusResponse = new LienStatusResponse();
//            lienStatusResponse.setResponseCode("111");
//            lienStatusResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Lien Status Request Processed in : {} ms {}", end, lienStatusResponse);
//
//        return lienStatusResponse;
//    }

    @RequestMapping(value = "api/initiateLoan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OfferListForCommodityResponse initiateLoanResponse(@RequestBody OfferListForCommodityRequest request) {
        OfferListForCommodityResponse offerListForCommodityResponse = new OfferListForCommodityResponse();

        long start = System.currentTimeMillis();

        logger.info("Initiate Loan Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Initiate Loan Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getIdentityType())
                .append(request.getIdentityValue())
                .append(request.getOrigSource())
                .append(request.getCommodityType())
                .append(request.getSourceRequestId())
                .append(request.getOfferName())
                .append(request.getAmount())
                .append(request.getFed())
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
                    HostRequestValidator.validateInitiateLoan(request);
                    offerListForCommodityResponse = integrationService.initiateLoanResponse(request);

                } catch (ValidationException ve) {
                    offerListForCommodityResponse.setResponseCode("420");
                    offerListForCommodityResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    offerListForCommodityResponse.setResponseCode("220");
                    offerListForCommodityResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Initiate Loan Request *********");
                logger.info("ResponseCode: " + offerListForCommodityResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Initiate Loan Request AUTHENTICATION *********");
                offerListForCommodityResponse = new OfferListForCommodityResponse();
                offerListForCommodityResponse.setResponseCode("420");
                offerListForCommodityResponse.setResponseDescription("Request is not authenticated");
                offerListForCommodityResponse.setRrn(request.getRrn());
                offerListForCommodityResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Initiate Loan Request *********");
            offerListForCommodityResponse = new OfferListForCommodityResponse();
            offerListForCommodityResponse.setResponseCode("111");
            offerListForCommodityResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Initiate Loan Request Processed in : {} ms {}", end, offerListForCommodityResponse);

        return offerListForCommodityResponse;
    }

    @RequestMapping(value = "api/selectLoan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoanOfferResponse selectLoanResponse(@RequestBody LoanOfferRequest request) {
        LoanOfferResponse loanOfferResponse = new LoanOfferResponse();

        long start = System.currentTimeMillis();

        logger.info("Select Loan Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Select Loan Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getSourceRequestId())
                .append(request.getOfferName())
                .append(request.getAmount())
                .append(request.getExternalLoanId())
                .append(request.getMerchantId())
                .append(request.getFed())
                .append(request.getLoanPurpose())
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
                    HostRequestValidator.validateSelectLoan(request);
                    loanOfferResponse = integrationService.selectLoanResponse(request);

                } catch (ValidationException ve) {
                    loanOfferResponse.setResponseCode("420");
                    loanOfferResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    loanOfferResponse.setResponseCode("220");
                    loanOfferResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Select Loan Request *********");
                logger.info("ResponseCode: " + loanOfferResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Select Loan Request AUTHENTICATION *********");
                loanOfferResponse = new LoanOfferResponse();
                loanOfferResponse.setResponseCode("420");
                loanOfferResponse.setResponseDescription("Request is not authenticated");
                loanOfferResponse.setRrn(request.getRrn());
                loanOfferResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Select Loan Request *********");
            loanOfferResponse = new LoanOfferResponse();
            loanOfferResponse.setResponseCode("111");
            loanOfferResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Select Loan Request Processed in : {} ms {}", end, loanOfferResponse);

        return loanOfferResponse;
    }

    @RequestMapping(value = "api/selectLoanOffer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ProjectionResponse selectLoanOfferResponse(@RequestBody ProjectionRequest request) {
        ProjectionResponse projectionResponse = new ProjectionResponse();

        long start = System.currentTimeMillis();

        logger.info("Loan Offer Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Loan Offer Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getIdentityType())
                .append(request.getOrigSource())
                .append(request.getIdentityValue())
                .append(request.getOfferName())
                .append(request.getLoanAmount())
                .append(request.getUpToPeriod())
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
                    HostRequestValidator.validateSelectLoanOffer(request);
                    projectionResponse = integrationService.selectLoanOfferResponse(request);

                } catch (ValidationException ve) {
                    projectionResponse.setResponseCode("420");
                    projectionResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    projectionResponse.setResponseCode("220");
                    projectionResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Loan Offer Request *********");
                logger.info("ResponseCode: " + projectionResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Loan Offer Request AUTHENTICATION *********");
                projectionResponse = new ProjectionResponse();
                projectionResponse.setResponseCode("420");
                projectionResponse.setResponseDescription("Request is not authenticated");
                projectionResponse.setRrn(request.getRrn());
                projectionResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR ILoan Offer Request *********");
            projectionResponse = new ProjectionResponse();
            projectionResponse.setResponseCode("111");
            projectionResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Loan Offer Request Processed in : {} ms {}", end, projectionResponse);

        return projectionResponse;
    }

    @RequestMapping(value = "api/payLoan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoanPaymentResponse loanPaymentResponse(@RequestBody LoanPaymentRequest request) {
        LoanPaymentResponse loanPaymentResponse = new LoanPaymentResponse();

        long start = System.currentTimeMillis();

        logger.info("Loan Payment Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Loan Payment Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getSourceRequestId())
                .append(request.getAmount())
                .append(request.getCurrencyCode())
                .append(request.getReason())
                .append(request.getIdentityType())
                .append(request.getOrigSource())
                .append(request.getIdentityValue())
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
                    HostRequestValidator.validateLoanPayment(request);
                    loanPaymentResponse = integrationService.loanPaymentResponse(request);

                } catch (ValidationException ve) {
                    loanPaymentResponse.setResponseCode("420");
                    loanPaymentResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    loanPaymentResponse.setResponseCode("220");
                    loanPaymentResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Loan Payment Request *********");
                logger.info("ResponseCode: " + loanPaymentResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Loan Payment Request AUTHENTICATION *********");
                loanPaymentResponse = new LoanPaymentResponse();
                loanPaymentResponse.setResponseCode("420");
                loanPaymentResponse.setResponseDescription("Request is not authenticated");
                loanPaymentResponse.setRrn(request.getRrn());
                loanPaymentResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Loan Payment Request *********");
            loanPaymentResponse = new LoanPaymentResponse();
            loanPaymentResponse.setResponseCode("111");
            loanPaymentResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Loan Payment Request Processed in : {} ms {}", end, loanPaymentResponse);

        return loanPaymentResponse;
    }

    @RequestMapping(value = "api/outstanding", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoansResponse outstandingResponse(@RequestBody LoansRequest request) {
        LoansResponse loansResponse = new LoansResponse();

        long start = System.currentTimeMillis();

        logger.info("Outstanding Loan Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
        //        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Outstanding Loan Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getIdentityType())
                .append(request.getOrigSource())
                .append(request.getIdentityValue())
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
                    HostRequestValidator.validateOutstanding(request);
                    loansResponse = integrationService.outstandingResponse(request);

                } catch (ValidationException ve) {
                    loansResponse.setResponseCode("420");
                    loansResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    loansResponse.setResponseCode("220");
                    loansResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Outstanding Loan Request *********");
                logger.info("ResponseCode: " + loansResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Outstanding Loan Request AUTHENTICATION *********");
                loansResponse = new LoansResponse();
                loansResponse.setResponseCode("420");
                loansResponse.setResponseDescription("Request is not authenticated");
                loansResponse.setRrn(request.getRrn());
                loansResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Customer Loans Request *********");
            loansResponse = new LoansResponse();
            loansResponse.setResponseCode("111");
            loansResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Outstanding Loan Request Processed in : {} ms {}", end, loansResponse);

        return loansResponse;
    }

//    @RequestMapping(value = "api/optasiaCreditInquiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    OptasiaCreditInquiryResponse optasiaCreditInuqiryResponse(@RequestBody OptasiaCreditInquiryRequest request) {
//        OptasiaCreditInquiryResponse optasiaCreditInquiryResponse = new OptasiaCreditInquiryResponse();
//
//        long start = System.currentTimeMillis();
//
//        logger.info("Optasia Credit Inquiry Request Received at Controller at time: " + start);
//        String requestXML = JSONUtil.getJSON(request);
////        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Optasia Credit Inquiry Request with {}", requestXML);
//
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getCustomerId())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getProductId())
//                .append(request.getPinType())
//                .append(request.getTransactionAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5())
//                .append(request.getReserved6())
//                .append(request.getReserved7())
//                .append(request.getReserved8())
//                .append(request.getReserved9())
//                .append(request.getReserved10());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateOptasiaCreditInquiry(request);
//                    optasiaCreditInquiryResponse = integrationService.optasiaCreditInquiryResponse(request);
//
//                } catch (ValidationException ve) {
//                    optasiaCreditInquiryResponse.setResponseCode("420");
//                    optasiaCreditInquiryResponse.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    optasiaCreditInquiryResponse.setResponseCode("220");
//                    optasiaCreditInquiryResponse.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR  Optasia Credit Inquiry Request *********");
//                logger.info("ResponseCode: " + optasiaCreditInquiryResponse.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR  Optasia Credit Inquiry Request AUTHENTICATION *********");
//                optasiaCreditInquiryResponse = new OptasiaCreditInquiryResponse();
//                optasiaCreditInquiryResponse.setResponseCode("420");
//                optasiaCreditInquiryResponse.setResponseDescription("Request is not authenticated");
//                optasiaCreditInquiryResponse.setRrn(request.getRrn());
//                optasiaCreditInquiryResponse.setResponseDateTime(request.getDateTime());
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Optasia Credit Inquiry Request *********");
//            optasiaCreditInquiryResponse = new OptasiaCreditInquiryResponse();
//            optasiaCreditInquiryResponse.setResponseCode("111");
//            optasiaCreditInquiryResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Optasia Credit Inquiry Request Processed in : {} ms {}", end, optasiaCreditInquiryResponse);
//
//        return optasiaCreditInquiryResponse;
//    }
//
//    @RequestMapping(value = "api/optasiaCredit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    OptasiaCreditResponse optasiaCredit(@RequestBody OptasiaCreditRequest request) {
//        long start = System.currentTimeMillis();
//        OptasiaCreditResponse response = null;
//        String requestXML = JSONUtil.getJSON(request);
////        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Optasia Credit Transaction Request with {}", requestXML);
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
//                .append(request.getPinType())
//                .append(request.getTransactionAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5())
//                .append(request.getReserved6())
//                .append(request.getReserved7())
//                .append(request.getReserved8())
//                .append(request.getReserved9())
//                .append(request.getReserved10());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateOptasiaCredit(request);
//                    response = integrationService.optasiaCreditResponse(request);
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
//                logger.info("******* DEBUG LOGS FOR Optasia Credit PAYMENT TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR Optasia Credit Payment TRANSACTION AUTHENTICATION *********");
//                response = new OptasiaCreditResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Optasia Credit PAYMENT TRANSACTION *********");
//            response = new OptasiaCreditResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Optasia Credit Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @RequestMapping(value = "api/optasiaDebitInquiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    OptasiaDebitInquiryResponse optasiaDebitInquiry(@RequestBody OptasiaDebitInquiryRequest request) {
//        long start = System.currentTimeMillis();
//        OptasiaDebitInquiryResponse response = null;
//        String requestXML = JSONUtil.getJSON(request);
////        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Optasia Debit Inquiry Transaction Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getRrn())
//                .append(request.getChannelId())
//                .append(request.getTerminalId())
//                .append(request.getProductId())
//                .append(request.getPinType())
//                .append(request.getTransactionAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5())
//                .append(request.getReserved6())
//                .append(request.getReserved7())
//                .append(request.getReserved8())
//                .append(request.getReserved9())
//                .append(request.getReserved10());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateOptasiadebitInquiry(request);
//                    response = integrationService.optasiaDebitInquiryResponse(request);
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
//                logger.info("******* DEBUG LOGS FOR Optasia Debit Inquiry PAYMENT TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR Optasia Debit Inquiry Payment TRANSACTION AUTHENTICATION *********");
//                response = new OptasiaDebitInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR  Optasia Debit Inquiry PAYMENT TRANSACTION *********");
//            response = new OptasiaDebitInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Optasia Debit Inquiry Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }
//
//    @RequestMapping(value = "api/optasiaDebit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    OptasiaDebitResponse optasiaDebit(@RequestBody OptasiaDebitRequest request) {
//        long start = System.currentTimeMillis();
//        OptasiaDebitResponse response = null;
//        String requestXML = JSONUtil.getJSON(request);
////        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Optasia Debit Transaction Request with {}", requestXML);
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
//                .append(request.getPinType())
//                .append(request.getTransactionAmount())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5())
//                .append(request.getReserved6())
//                .append(request.getReserved7())
//                .append(request.getReserved8())
//                .append(request.getReserved9())
//                .append(request.getReserved10());
//
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateOptasiaDebit(request);
//                    response = integrationService.optasiaDebitResponse(request);
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
//                logger.info("******* DEBUG LOGS FOR Optasia Debit PAYMENT TRANSACTION *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR Optasia Debit Payment TRANSACTION AUTHENTICATION *********");
//                response = new OptasiaDebitResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR  Optasia Debit PAYMENT TRANSACTION *********");
//            response = new OptasiaDebitResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Optasia Debit Payment Request  Processed in : {} ms {}", end, response);
//
//        return response;
//    }

    @RequestMapping(value = "api/loanHistory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoansHistoryResponse loansHistoryResponse(@RequestBody LoansHistoryRequest request) {
        long start = System.currentTimeMillis();
        LoansHistoryResponse response = new LoansHistoryResponse();
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Loan History Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getFromDate())
                .append(request.getToDate())
                .append(request.getReason())
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
                    HostRequestValidator.validateLoanHistory(request);
                    response = integrationService.loansHistoryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Loan History Request TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Loan History Request TRANSACTION AUTHENTICATION *********");
                response = new LoansHistoryResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Loan History Request TRANSACTION *********");
            response = new LoansHistoryResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Loan History Request  Processed in : {} ms {}", end, response);

        return response;
    }

    @RequestMapping(value = "api/loanPlan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoanPlanResponse loanPlanResponse(@RequestBody LoansPlanRequest request) {

        long start = System.currentTimeMillis();
        logger.info("Loan Plan Request Received at Controller at time: " + start);
        LoanPlanResponse response = new LoanPlanResponse();
        String requestXML = JSONUtil.getJSON(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Loan Plan Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getFromDate())
                .append(request.getToDate())
                .append(request.getAmount())
                .append(request.getReason())
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
                    HostRequestValidator.validateLoanPlan(request);
                    response = integrationService.loanPlanResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Loan Plan Request TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Loan Plan Request TRANSACTION AUTHENTICATION *********");
                response = new LoanPlanResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Loan Plan Request TRANSACTION *********");
            response = new LoanPlanResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Loan Plan Request  Processed in : {} ms {}", end, response);

        return response;
    }


    @RequestMapping(value = "api/transactionActive", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransactionActiveResponse transactionActiveResponse(@RequestBody TransactionActiveRequest request) {
        long start = System.currentTimeMillis();
        logger.info("Initiate Loan Request Received at Controller at time: " + start);

        TransactionActiveResponse response = new TransactionActiveResponse();
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Transaction Active Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getFromDate())
                .append(request.getToDate())
                .append(request.getReason())
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
                    HostRequestValidator.validateTransactionActive(request);
                    response = integrationService.transactionActiveResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Transaction Active Request TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Transaction Active Request TRANSACTION AUTHENTICATION *********");
                response = new TransactionActiveResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Transaction Active Request TRANSACTION *********");
            response = new TransactionActiveResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Transaction Active Request  Processed in : {} ms {}", end, response);

        return response;
    }

 /*   @RequestMapping(value = "api/callBack", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    LoanCallBackResponse loanCallBackResponse(@RequestBody LoanCallBackRequest request) {
        LoanCallBackResponse loanCallBackResponse = new LoanCallBackResponse();

        long start = System.currentTimeMillis();

        logger.info("Loan Call Back Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Loan Call Back Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getIdentityType())
                .append(request.getOrigSource())
                .append(request.getIdentityValue())
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
                    HostRequestValidator.validateLoanCallBack(request);
                    loanCallBackResponse = integrationService.loanCallBackResponse(request);

                } catch (ValidationException ve) {
                    loanCallBackResponse.setResponseCode("420");
                    loanCallBackResponse.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    loanCallBackResponse.setResponseCode("220");
                    loanCallBackResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Loan Call Back Request *********");
                logger.info("ResponseCode: " + loanCallBackResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Loan Call Back Request AUTHENTICATION *********");
                loanCallBackResponse = new LoanCallBackResponse();
                loanCallBackResponse.setResponseCode("420");
                loanCallBackResponse.setResponseDescription("Request is not authenticated");
                loanCallBackResponse.setRrn(request.getRrn());
                loanCallBackResponse.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
        } else {
            logger.info("******* DEBUG LOGS FOR Loan Call Back Request *********");
            loanCallBackResponse = new LoanCallBackResponse();
            loanCallBackResponse.setResponseCode("111");
            loanCallBackResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Loan Call Back Request Processed in : {} ms {}", end, loanCallBackResponse);

        return loanCallBackResponse;
    }


    @RequestMapping(value = "api/outstanding", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OutstandingResponse outstandingResponse(@RequestBody OutstandingRequest request) {
        OutstandingResponse outstandingResponse = new OutstandingResponse();

        long start = System.currentTimeMillis();

        logger.info("Customer Outstanding Loan Status Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Customer Outstanding Loan Status Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getIdentityType())
                .append(request.getOrigSource())
                .append(request.getIdentityValue())
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
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
        if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
            try {
//                    HostRequestValidator.validateCustomerOutstandingLoanStatus(request);
                outstandingResponse = integrationService.customerOutstandingLoanStatus(request);

            } catch (ValidationException ve) {
                outstandingResponse.setResponseCode("420");
                outstandingResponse.setResponseDescription(ve.getMessage());

                logger.error("ERROR: Request Validation", ve);
            } catch (Exception e) {
                outstandingResponse.setResponseCode("220");
                outstandingResponse.setResponseDescription(e.getMessage());
                logger.error("ERROR: General Processing ", e);
            }

            logger.info("******* DEBUG LOGS FOR Customer Outstanding Loan Status Request *********");
            logger.info("ResponseCode: " + outstandingResponse.getResponseCode());
        } else {
            logger.info("******* DEBUG LOGS FOR Customer Outstanding Loan Status Request AUTHENTICATION *********");
            outstandingResponse = new OutstandingResponse();
            outstandingResponse.setResponseCode("420");
            outstandingResponse.setResponseDescription("Request is not authenticated");
            outstandingResponse.setRrn(request.getRrn());
            outstandingResponse.setResponseDateTime(request.getDateTime());
            logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

        }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Customer Outstanding Loan Status Request *********");
//            outstandingResponse = new OutstandingResponse();
//            outstandingResponse.setResponseCode("111");
//            outstandingResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }

        long end = System.currentTimeMillis() - start;
        logger.info("Customer Outstanding Loan Status Request Processed in : {} ms {}", end, outstandingResponse);

        return outstandingResponse;
    }


    @RequestMapping(value = "api/loanStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoanStatusResponse loanStatusResponse(@RequestBody LoanStatusRequest request) {
        LoanStatusResponse loanStatusResponse = new LoanStatusResponse();

        long start = System.currentTimeMillis();

        logger.info("Loan Payment Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Loan Payment Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getIdentityType())
                .append(request.getOrigSource())
                .append(request.getIdentityValue())
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
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
        if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
            try {
//                    HostRequestValidator.validateLoanStatus(request);
                loanStatusResponse = integrationService.loanStatusResponse(request);

            } catch (ValidationException ve) {
                loanStatusResponse.setResponseCode("420");
                loanStatusResponse.setResponseDescription(ve.getMessage());

                logger.error("ERROR: Request Validation", ve);
            } catch (Exception e) {
                loanStatusResponse.setResponseCode("220");
                loanStatusResponse.setResponseDescription(e.getMessage());
                logger.error("ERROR: General Processing ", e);
            }

            logger.info("******* DEBUG LOGS FOR Loan Status Request *********");
            logger.info("ResponseCode: " + loanStatusResponse.getResponseCode());
        } else {
            logger.info("******* DEBUG LOGS FOR Loan Status Request AUTHENTICATION *********");
            loanStatusResponse = new LoanStatusResponse();
            loanStatusResponse.setResponseCode("420");
            loanStatusResponse.setResponseDescription("Request is not authenticated");
            loanStatusResponse.setRrn(request.getRrn());
            loanStatusResponse.setResponseDateTime(request.getDateTime());
            logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

        }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Loan Status Request *********");
//            loanStatusResponse = new LoanStatusResponse();
//            loanStatusResponse.setResponseCode("111");
//            loanStatusResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }

        long end = System.currentTimeMillis() - start;
        logger.info("Loan Status Request Processed in : {} ms {}", end, loanStatusResponse);

        return loanStatusResponse;
    }


   @RequestMapping(value = "api/customerAnalytics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    CustomerAnalyticsResponse customAnalyticsResponse(@RequestBody CustomerAnalyticsRequest request) {
        CustomerAnalyticsResponse customerAnalyticsResponse = new CustomerAnalyticsResponse();

        long start = System.currentTimeMillis();

        logger.info("Customer Analytics Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Customer Analytics Request with {}", requestXML);

        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getCustomerId())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getFullName())
                .append(request.getDateOfBirth())
                .append(request.getCity())
                .append(request.getLoanAmount())
                .append(request.getGender())
                .append(request.getCurrentAddress())
                .append(request.getFatherHusbandName())
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
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
        if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
            try {
//                    HostRequestValidator.validateCustomerAnalytics(request);
                customerAnalyticsResponse = integrationService.customAnalyticsResponse(request);

            } catch (ValidationException ve) {
                customerAnalyticsResponse.setResponseCode("420");
                customerAnalyticsResponse.setResponseDescription(ve.getMessage());

                logger.error("ERROR: Request Validation", ve);
            } catch (Exception e) {
                customerAnalyticsResponse.setResponseCode("220");
                customerAnalyticsResponse.setResponseDescription(e.getMessage());
                logger.error("ERROR: General Processing ", e);
            }

            logger.info("******* DEBUG LOGS FOR Customer Analytics Request *********");
            logger.info("ResponseCode: " + customerAnalyticsResponse.getResponseCode());
        } else {
            logger.info("******* DEBUG LOGS FOR Customer Analytics Request AUTHENTICATION *********");
            customerAnalyticsResponse = new CustomerAnalyticsResponse();
            customerAnalyticsResponse.setResponseCode("420");
            customerAnalyticsResponse.setResponseDescription("Request is not authenticated");
            customerAnalyticsResponse.setRrn(request.getRrn());
            customerAnalyticsResponse.setResponseDateTime(request.getDateTime());
            logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

        }
//        } else {
//            logger.info("******* DEBUG LOGS FOR Customer Analytics Request *********");
//            loanPaymentResponse = new LoanPaymentResponse();
//            loanPaymentResponse.setResponseCode("111");
//            loanPaymentResponse.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }

        long end = System.currentTimeMillis() - start;
        logger.info("Customer Analytics Request Processed in : {} ms {}", end, customerAnalyticsResponse);

        return customerAnalyticsResponse;
    }
*/


}
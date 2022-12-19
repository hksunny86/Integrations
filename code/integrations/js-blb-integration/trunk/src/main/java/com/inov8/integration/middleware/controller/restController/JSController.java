package com.inov8.integration.middleware.controller.restController;

import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.RequestValidator;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.Date;

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

                }catch (ValidationException ve) {
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


    @RequestMapping(value = "api/CLSStatusUpdate" , method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
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

                }catch (ValidationException ve) {
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

    @RequestMapping(value = "api/BlinkAccountVerificationInquiry" , method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
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

                }catch (ValidationException ve) {
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

    @RequestMapping(value = "api/BlinkAccountVerification" , method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
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
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateBlinkAccountVerification(request);
                    blinkAccountVerificationResponse = integrationService.blinkAccountVerificationResponse(request);

                }catch (ValidationException ve) {
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
        } else {
            logger.info("******* DEBUG LOGS FOR Blink Account Verification TRANSACTION *********");
            blinkAccountVerificationResponse = new BlinkAccountVerificationResponse();
            blinkAccountVerificationResponse.setResponseCode("111");
            blinkAccountVerificationResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Blink Account Verification Request  Processed in : {} ms {}", end, blinkAccountVerificationResponse);

        return blinkAccountVerificationResponse;
    }


    @RequestMapping(value = "api/DebitCardStatus" , method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
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

                }catch (ValidationException ve) {
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




    @RequestMapping(value = "api/AdvanceLoanEarlyPaymentSettlement" , method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
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
                }catch (ValidationException ve) {
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


    @RequestMapping(value = "api/FeePaymentInquiry" , method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
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

                }catch (ValidationException ve) {
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



    @RequestMapping(value = "api/FeePayment" , method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
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

            }catch (ValidationException ve) {
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


}
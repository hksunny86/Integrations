package com.inov8.integration.middleware.controller.restController;

import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.service.hostService.HostIntegrationService;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.middleware.util.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Api(value = "Restful APIs", description = "SwaggerUI is located under /documentation. This mapping redirects the necessary resources for the ui.", hidden = true)
@RestController
//@RequestMapping(value = "/documentation")

public class JSController {

    private static Logger logger = LoggerFactory.getLogger(JSController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");
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
//        logger.info("Start Processing Customer Name Update Request with {}", requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Customer Name Update Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
//        logger.info("Start Processing CLS Status Update Request with {}", requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("tart Processing CLS Status Update Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
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
        } else {
            logger.info("******* DEBUG LOGS FOR CLS Status Update TRANSACTION *********");
            clsStatusUpdateResponse = new CLSStatusUpdateResponse();
            clsStatusUpdateResponse.setResponseCode("111");
            clsStatusUpdateResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

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
//        logger.info("Start Processing Blink Account Verification Inquiry Request with {}", requestXML);
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        String datetime = "";
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Blink Account Verification Inquiry Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
//        logger.info("Start Processing Blink Account Verification Request with {}", requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Blink Account Verification Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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

    @RequestMapping(value = "api/DebitCardStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    DebitCardStatusReponse debitCardStatusResponse(@RequestBody DebitCardStatusRequest request) {

        DebitCardStatusReponse debitCardStatusResponse = new DebitCardStatusReponse();
        long start = System.currentTimeMillis();

        logger.info("Debit Card Status Verification Request Recieve at Controller at time: " + start);
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
//        logger.info("Start Processing Debit Card Status Verification Request with {}", requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Debit Card Status Verification Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
//        logger.info("Start Processing Advance Loan Payment Settlement Request with {}", requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Advance Loan Payment Settlement Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Fee Payment  Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
//        logger.info("Start Processing Fee Payment  Request with {}", requestXML);
        String datetime = "";
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        datetime = DateFor.format(new Date());
        logger.info("Start Processing Fee Payment  Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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

    @RequestMapping(value = "api/optasiaDebitInquiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OptasiaDebitInquiryResponse optasiaDebitInquiry(@RequestBody OptasiaDebitInquiryRequest request) throws Exception {

        OptasiaDebitInquiryResponse response = null;
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {
//            logger.info("Start Processing Optasia Debit Inquiry Transaction Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Optasia Debit Inquiry Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
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
                        HostRequestValidator.validateOptasiadebitInquiry(request);
                        response = integrationService.optasiaDebitInquiryResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Optasia Debit Inquiry PAYMENT TRANSACTION *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Optasia Debit Inquiry Payment TRANSACTION AUTHENTICATION *********");
                    response = new OptasiaDebitInquiryResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR  Optasia Debit Inquiry PAYMENT TRANSACTION *********");
                response = new OptasiaDebitInquiryResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new OptasiaDebitInquiryResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Optasia Debit Inquiry Payment Request  Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return response;
    }

    @RequestMapping(value = "api/optasiaDebit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OptasiaDebitResponse optasiaDebit(@RequestBody OptasiaDebitRequest request) throws Exception {

        OptasiaDebitResponse response = null;
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {


//            logger.info("Start Processing Optasia Debit Transaction Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Optasia Debit Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
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
                        HostRequestValidator.validateOptasiaDebit(request);
                        response = integrationService.optasiaDebitResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Optasia Debit PAYMENT TRANSACTION *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Optasia Debit Payment TRANSACTION AUTHENTICATION *********");
                    response = new OptasiaDebitResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR  Optasia Debit PAYMENT TRANSACTION *********");
                response = new OptasiaDebitResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new OptasiaDebitResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Optasia Debit Payment Request  Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return response;
    }

    @RequestMapping(value = "api/optasiaCreditInquiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OptasiaCreditInquiryResponse optasiaCreditInuqiryResponse(@RequestBody OptasiaCreditInquiryRequest request) throws Exception {
        OptasiaCreditInquiryResponse optasiaCreditInquiryResponse = new OptasiaCreditInquiryResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {


            logger.info("Optasia Credit Inquiry Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Optasia Credit Inquiry Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Fee Payment  Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
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
                        HostRequestValidator.validateOptasiaCreditInquiry(request);
                        optasiaCreditInquiryResponse = integrationService.optasiaCreditInquiryResponse(request);

                    } catch (ValidationException ve) {
                        optasiaCreditInquiryResponse.setResponseCode("420");
                        optasiaCreditInquiryResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        optasiaCreditInquiryResponse.setResponseCode("220");
                        optasiaCreditInquiryResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR  Optasia Credit Inquiry Request *********");
                    logger.info("ResponseCode: " + optasiaCreditInquiryResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR  Optasia Credit Inquiry Request AUTHENTICATION *********");
                    optasiaCreditInquiryResponse = new OptasiaCreditInquiryResponse();
                    optasiaCreditInquiryResponse.setResponseCode("420");
                    optasiaCreditInquiryResponse.setResponseDescription("Request is not authenticated");
                    optasiaCreditInquiryResponse.setRrn(request.getRrn());
                    optasiaCreditInquiryResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Optasia Credit Inquiry Request *********");
                optasiaCreditInquiryResponse = new OptasiaCreditInquiryResponse();
                optasiaCreditInquiryResponse.setResponseCode("111");
                optasiaCreditInquiryResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {
            optasiaCreditInquiryResponse = new OptasiaCreditInquiryResponse();
            optasiaCreditInquiryResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            optasiaCreditInquiryResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(optasiaCreditInquiryResponse);
        logger.info("Optasia Credit Inquiry Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return optasiaCreditInquiryResponse;
    }

    @RequestMapping(value = "api/optasiaCredit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OptasiaCreditResponse optasiaCredit(@RequestBody OptasiaCreditRequest request) throws Exception {

        OptasiaCreditResponse response = null;
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

//            logger.info("Start Processing Optasia Credit Transaction Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Optasia Credit Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
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
                        HostRequestValidator.validateOptasiaCredit(request);
                        response = integrationService.optasiaCreditResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERRORa: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Optasia Credit PAYMENT TRANSACTION *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Optasia Credit Payment TRANSACTION AUTHENTICATION *********");
                    response = new OptasiaCreditResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR Optasia Credit PAYMENT TRANSACTION *********");
                response = new OptasiaCreditResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new OptasiaCreditResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Optasia Credit Payment Request  Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return response;
    }

    @RequestMapping(value = "api/transactionStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransactionStatusResponse transactionStatusResponse(@RequestBody TransactionStatusRequest request) throws Exception {
        TransactionStatusResponse transactionStatusResponse = new TransactionStatusResponse();

        long start = System.currentTimeMillis();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            logger.info("Transaction Status Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Transaction Status Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Transaction Status Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateTransactionStatus(request);
                        transactionStatusResponse = integrationService.transactionStatusResponse(request);

                    } catch (ValidationException ve) {
                        transactionStatusResponse.setResponseCode("420");
                        transactionStatusResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        transactionStatusResponse.setResponseCode("220");
                        transactionStatusResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR  Transaction Status Request *********");
                    logger.info("ResponseCode: " + transactionStatusResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR  Transaction Status Request AUTHENTICATION *********");
                    transactionStatusResponse = new TransactionStatusResponse();
                    transactionStatusResponse.setResponseCode("420");
                    transactionStatusResponse.setResponseDescription("Request is not authenticated");
                    transactionStatusResponse.setRrn(request.getRrn());
                    transactionStatusResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Transaction Status Request *********");
                transactionStatusResponse = new TransactionStatusResponse();
                transactionStatusResponse.setResponseCode("111");
                transactionStatusResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            transactionStatusResponse = new TransactionStatusResponse();
            transactionStatusResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            transactionStatusResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(transactionStatusResponse);
        logger.info("Transaction Status Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return transactionStatusResponse;
    }

    @RequestMapping(value = "api/profileStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ProfileStatusResponse profileStatusResponse(@RequestBody ProfileStatusRequest request) throws Exception {
        ProfileStatusResponse profileStatusResponse = new ProfileStatusResponse();
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();
        logger.info("Profile Status Request Received at Controller at time: " + start);

        try {
            String requestXML = JSONUtil.getJSON(request);
//            logger.info("Start Processing Profile Status Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Profile Status Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", requestXML.replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateProfileStatus(request);
                        profileStatusResponse = integrationService.profileStatusResponse(request);

                    } catch (ValidationException ve) {
                        profileStatusResponse.setResponseCode("420");
                        profileStatusResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        profileStatusResponse.setResponseCode("220");
                        profileStatusResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Profile Status Request *********");
                    logger.info("ResponseCode: " + profileStatusResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Profile Status Request AUTHENTICATION *********");
                    profileStatusResponse = new ProfileStatusResponse();
                    profileStatusResponse.setResponseCode("420");
                    profileStatusResponse.setResponseDescription("Request is not authenticated");
                    profileStatusResponse.setRrn(request.getRrn());
                    profileStatusResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Profile Status Request *********");
                profileStatusResponse = new ProfileStatusResponse();
                profileStatusResponse.setResponseCode("111");
                profileStatusResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {
            profileStatusResponse = new ProfileStatusResponse();
            profileStatusResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            profileStatusResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(profileStatusResponse);
        logger.info("Profile Status Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return profileStatusResponse;
    }

    @RequestMapping(value = "api/lienStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LienStatusResponse lienStatusResponse(@RequestBody LienStatusRequest request) throws Exception {
        LienStatusResponse lienStatusResponse = new LienStatusResponse();

        long start = System.currentTimeMillis();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            logger.info("Lien Status Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
            logger.info("Start Processing Lien Status Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Lien Status Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateLienStatus(request);
                        lienStatusResponse = integrationService.lienStatusResponse(request);

                    } catch (ValidationException ve) {
                        lienStatusResponse.setResponseCode("420");
                        lienStatusResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        lienStatusResponse.setResponseCode("220");
                        lienStatusResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Lien Status Request *********");
                    logger.info("ResponseCode: " + lienStatusResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Lien Status Request AUTHENTICATION *********");
                    lienStatusResponse = new LienStatusResponse();
                    lienStatusResponse.setResponseCode("420");
                    lienStatusResponse.setResponseDescription("Request is not authenticated");
                    lienStatusResponse.setRrn(request.getRrn());
                    lienStatusResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Lien Status Request *********");
                lienStatusResponse = new LienStatusResponse();
                lienStatusResponse.setResponseCode("111");
                lienStatusResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {
            lienStatusResponse = new LienStatusResponse();
            lienStatusResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            lienStatusResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(lienStatusResponse);
        logger.info("Lien Status Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return lienStatusResponse;
    }

    @RequestMapping(value = "api/optasiaSMS", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OptasiaSmsGenerationResponse optasiaSmsGenerationResponse(@RequestBody OptasiaSmsGenerationRequest request) throws
            Exception {

        OptasiaSmsGenerationResponse response = new OptasiaSmsGenerationResponse();
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

//            logger.info("Start Processing Optasia Sms Generation Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Optasia Sms Generation Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
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
                        HostRequestValidator.validateOptasiaSmsGeneration(request);
                        response = integrationService.optasiaSmsGenerationResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Optasia Sms Generation Request TRANSACTION *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Optasia Sms Generation Request TRANSACTION AUTHENTICATION *********");
                    response = new OptasiaSmsGenerationResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR Optasia Sms Generation Request TRANSACTION *********");
                response = new OptasiaSmsGenerationResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new OptasiaSmsGenerationResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Optasia Sms Generation Request  Processed in : {} ms {}" + "\n", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return response;
    }

    @RequestMapping(value = "api/initiateLoan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OfferListForCommodityResponse initiateLoanResponse(@RequestBody OfferListForCommodityRequest request) throws Exception {
        OfferListForCommodityResponse offerListForCommodityResponse = new OfferListForCommodityResponse();
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Initiate Loan Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Initiate Loan Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Initiate Loan Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
        } catch (Exception e) {
            offerListForCommodityResponse = new OfferListForCommodityResponse();
            offerListForCommodityResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            offerListForCommodityResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(offerListForCommodityResponse);
        logger.info("Initiate Loan Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return offerListForCommodityResponse;
    }

    @RequestMapping(value = "api/selectLoan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoanOfferResponse selectLoanResponse(@RequestBody LoanOfferRequest request) throws Exception {
        LoanOfferResponse loanOfferResponse = new LoanOfferResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {
            logger.info("Select Loan Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Select Loan Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Select Loan Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
                    .append(request.getProcessingFee())
                    .append(request.getStartDate())
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

        } catch (Exception e) {
            loanOfferResponse = new LoanOfferResponse();
            loanOfferResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            loanOfferResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(loanOfferResponse);
        logger.info("Select Loan Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return loanOfferResponse;
    }

    @RequestMapping(value = "api/selectLoanOffer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ProjectionResponse selectLoanOfferResponse(@RequestBody ProjectionRequest request) throws Exception {
        ProjectionResponse projectionResponse = new ProjectionResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Loan Offer Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Loan Offer Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Loan Offer Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
                logger.info("******* DEBUG LOGS FOR Loan Offer Request *********");
                projectionResponse = new ProjectionResponse();
                projectionResponse.setResponseCode("111");
                projectionResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            projectionResponse = new ProjectionResponse();
            projectionResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            projectionResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(projectionResponse);
        logger.info("Loan Offer Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return projectionResponse;
    }

    @RequestMapping(value = "api/payLoan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoanPaymentResponse loanPaymentResponse(@RequestBody LoanPaymentRequest request) throws Exception {
        LoanPaymentResponse loanPaymentResponse = new LoanPaymentResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {


            logger.info("Loan Payment Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Loan Payment Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Loan Payment Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
        } catch (Exception e) {
            loanPaymentResponse = new LoanPaymentResponse();
            loanPaymentResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            loanPaymentResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(loanPaymentResponse);
        logger.info("Loan Payment Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return loanPaymentResponse;
    }

    @RequestMapping(value = "api/outstanding", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoansResponse outstandingResponse(@RequestBody LoansRequest request) throws Exception {
        LoansResponse loansResponse = new LoansResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Outstanding Loan Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Outstanding Loan Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Outstanding Loan Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
        } catch (Exception e) {

            loansResponse = new LoansResponse();
            loansResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            loansResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(loansResponse);
        logger.info("Outstanding Loan Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return loansResponse;
    }

    @RequestMapping(value = "api/loanHistory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoansHistoryResponse loansHistoryResponse(@RequestBody LoansHistoryRequest request) throws Exception {

        LoansHistoryResponse response = new LoansHistoryResponse();
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {
//            logger.info("Start Processing Loan History Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Loan History Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
        } catch (Exception e) {

            response = new LoansHistoryResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Loan History Request  Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return response;
    }

    @RequestMapping(value = "api/loanPlan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoanPlanResponse loanPlanResponse(@RequestBody LoansPlanRequest request) throws Exception {

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();
        logger.info("Loan Plan Request Received at Controller at time: " + start);
        LoanPlanResponse response = new LoanPlanResponse();

        try {
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Loan Plan Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Loan Plan Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
        } catch (Exception e) {

            response = new LoanPlanResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Loan Plan Request  Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return response;
    }

    @RequestMapping(value = "api/transactionActive", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransactionActiveResponse transactionActiveResponse(@RequestBody TransactionActiveRequest request) throws
            Exception {
        TransactionActiveResponse response = new TransactionActiveResponse();
        String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

//            logger.info("Start Processing Transaction Active Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Transaction Active Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
        } catch (Exception e) {

            response = new TransactionActiveResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Transaction Active Request  Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return response;
    }

    @RequestMapping(value = "api/advance_callback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    LoanCallBackResponse loanCallBackResponse(@RequestBody LoanCallBackRequest request) throws Exception {
        LoanCallBackResponse loanCallBackResponse = new LoanCallBackResponse();

//        requestXML = XMLUtil.maskPassword(requestXML);
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        logger.info("Loan Call Back Request Received at Controller at time: " + start);
        String requestXML = JSONUtil.getJSON(request);
        try {

//            logger.info("Start Processing Loan Call Back Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Loan Call Back Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getCustomerId())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getLoanEvent())
                    .append(request.getLoanEventStatus())
                    .append(request.getOrigSource())
                    .append(request.getInternalLoanId());

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
        } catch (Exception e) {

            loanCallBackResponse = new LoanCallBackResponse();
            loanCallBackResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            loanCallBackResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(loanCallBackResponse);
        logger.info("Loan Call Back Request Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return loanCallBackResponse;
    }

    @RequestMapping(value = "api/simpleAccountOpening", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    SimpleAccountOpeningResponse simpleAccountOpeningResponse(@RequestBody SimpleAccountOpeningRequest request) throws
            Exception {
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();
        SimpleAccountOpeningResponse response = new SimpleAccountOpeningResponse();
        logger.info("Simple Account Opening Request Received at Controller at time: " + start);

        try {
            String requestXML = JSONUtil.getJSON(request);
//        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Simple Account Opening Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Simple Account Opening Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getCnic())
                    .append(request.getCnicIssuanceDate())
                    .append(request.getCnicExpiryDate())
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
                        HostRequestValidator.validateSimpleAccountOpening(request);
                        response = integrationService.simpleAccountOpeningResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Processing Simple Account Opening Request TRANSACTION *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Processing Simple Account Opening Request TRANSACTION AUTHENTICATION *********");
                    response = new SimpleAccountOpeningResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR Processing Simple Account Opening Request TRANSACTION *********");
                response = new SimpleAccountOpeningResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new SimpleAccountOpeningResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Simple Account Opening Request  Processed in : {} ms {}", end + "\n", Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return response;
    }

    @RequestMapping(value = "api/getOutstandingLoan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    OutstandingLoanResponse outstandingLoanResponse(@RequestBody OutstandingLoanRequest request) throws Exception {
        OutstandingLoanResponse outstandingLoanResponse = new OutstandingLoanResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Get Outstanding Loan Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Get Outstanding Loan Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Get Outstanding Loan Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getProductId())
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
                        HostRequestValidator.validateOutstandingLoan(request);
                        outstandingLoanResponse = integrationService.outstandingLoanResponse(request);

                    } catch (ValidationException ve) {
                        outstandingLoanResponse.setResponseCode("420");
                        outstandingLoanResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        outstandingLoanResponse.setResponseCode("220");
                        outstandingLoanResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Get Outstanding Loan Request *********");
                    logger.info("ResponseCode: " + outstandingLoanResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Get Outstanding Loan Request AUTHENTICATION *********");
                    outstandingLoanResponse = new OutstandingLoanResponse();
                    outstandingLoanResponse.setResponseCode("420");
                    outstandingLoanResponse.setResponseDescription("Request is not authenticated");
                    outstandingLoanResponse.setRrn(request.getRrn());
                    outstandingLoanResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Get Outstanding Loan Request *********");
                outstandingLoanResponse = new OutstandingLoanResponse();
                outstandingLoanResponse.setResponseCode("111");
                outstandingLoanResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            outstandingLoanResponse = new OutstandingLoanResponse();
            outstandingLoanResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            outstandingLoanResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(outstandingLoanResponse);
        logger.info("Get Outstanding Loan Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return outstandingLoanResponse;
    }

    @RequestMapping(value = "api/merchantAccountUpgrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    MerchantAccountUpgradeResponse merchantAccountUpgradeResponse(@RequestBody MerchantAccountUpgradeRequest request) throws Exception {
        MerchantAccountUpgradeResponse merchantAccountUpgradeResponse = new MerchantAccountUpgradeResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Merchant Account Upgrade Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Merchant Account Upgrade Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Merchant Account Upgrade Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNo())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getName())
                    .append(request.getCnicNumber())
                    .append(request.getBusinessName())
                    .append(request.getBusinessAddress())
                    .append(request.getCity())
                    .append(request.getEstimatedMonthlySales())
                    .append(request.getTypeOfBusiness())
                    .append(request.getProfilePic())
                    .append(request.getCNICFrontPic())
                    .append(request.getCNICBackPic())
                    .append(request.getLongitude())
                    .append(request.getLatitude())
                    .append(request.getIDType())
                    .append(request.getIdN())
                    .append(request.getTillID())
                    .append(request.getQrCode())
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
                        HostRequestValidator.validateMerchantAccountUpgrade(request);
                        merchantAccountUpgradeResponse = integrationService.merchantAccountUpgradeResponse(request);

                    } catch (ValidationException ve) {
                        merchantAccountUpgradeResponse.setResponseCode("420");
                        merchantAccountUpgradeResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        merchantAccountUpgradeResponse.setResponseCode("220");
                        merchantAccountUpgradeResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Merchant Account Upgrade Request *********");
                    logger.info("ResponseCode: " + merchantAccountUpgradeResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Merchant Account Upgrade Request AUTHENTICATION *********");
                    merchantAccountUpgradeResponse = new MerchantAccountUpgradeResponse();
                    merchantAccountUpgradeResponse.setResponseCode("420");
                    merchantAccountUpgradeResponse.setResponseDescription("Request is not authenticated");
                    merchantAccountUpgradeResponse.setRrn(request.getRrn());
                    merchantAccountUpgradeResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Merchant Account Upgrade Request *********");
                merchantAccountUpgradeResponse = new MerchantAccountUpgradeResponse();
                merchantAccountUpgradeResponse.setResponseCode("111");
                merchantAccountUpgradeResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            merchantAccountUpgradeResponse = new MerchantAccountUpgradeResponse();
            merchantAccountUpgradeResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            merchantAccountUpgradeResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(merchantAccountUpgradeResponse);
        logger.info("Merchant Account Upgrade Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return merchantAccountUpgradeResponse;
    }

    @RequestMapping(value = "api/merchantPictureUpgrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    MerchantPictureUpgradeResponse merchantPictureUpgradeResponse(@RequestBody MerchantPictureUpgradeRequest request) throws Exception {
        MerchantPictureUpgradeResponse merchantPictureUpgradeResponse = new MerchantPictureUpgradeResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Merchant Picture Upgrade Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Merchant Picture Upgrade Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Merchant Picture Upgrade Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNo())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getCnicNumber())
                    .append(request.getBusinessName())
                    .append(request.getBusinessAddress())
                    .append(request.getProfilePic())
                    .append(request.getCNICFrontPic())
                    .append(request.getCNICBackPic())
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
                        HostRequestValidator.validateMerchantPictureUpgrade(request);
                        merchantPictureUpgradeResponse = integrationService.merchantPictureUpgradeResponse(request);

                    } catch (ValidationException ve) {
                        merchantPictureUpgradeResponse.setResponseCode("420");
                        merchantPictureUpgradeResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        merchantPictureUpgradeResponse.setResponseCode("220");
                        merchantPictureUpgradeResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Merchant Picture Upgrade Request *********");
                    logger.info("ResponseCode: " + merchantPictureUpgradeResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Merchant Picture Upgrade Request AUTHENTICATION *********");
                    merchantPictureUpgradeResponse = new MerchantPictureUpgradeResponse();
                    merchantPictureUpgradeResponse.setResponseCode("420");
                    merchantPictureUpgradeResponse.setResponseDescription("Request is not authenticated");
                    merchantPictureUpgradeResponse.setRrn(request.getRrn());
                    merchantPictureUpgradeResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Merchant Picture Upgrade Request *********");
                merchantPictureUpgradeResponse = new MerchantPictureUpgradeResponse();
                merchantPictureUpgradeResponse.setResponseCode("111");
                merchantPictureUpgradeResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            merchantPictureUpgradeResponse = new MerchantPictureUpgradeResponse();
            merchantPictureUpgradeResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            merchantPictureUpgradeResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(merchantPictureUpgradeResponse);
        logger.info("Merchant Picture Upgrade Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return merchantPictureUpgradeResponse;
    }

    @RequestMapping(value = "api/updateCnicExpiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    UpdateCnicExpiryResponse updateCnicExpiryResponse(@RequestBody UpdateCnicExpiryRequest request) throws Exception {
        UpdateCnicExpiryResponse updateCnicExpiryResponse = new UpdateCnicExpiryResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Update Cnic Expiry Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Merchant Picture Upgrade Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Update Cnic Expiry Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getCnic())
                    .append(request.getIssuanceDate())
                    .append(request.getReserved1())
                    .append(request.getReserved2())
                    .append(request.getReserved3())
                    .append(request.getReserved4())
                    .append(request.getReserved5());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateUpdateCnicExpiry(request);
                        updateCnicExpiryResponse = integrationService.updateCnicExpiryResponse(request);

                    } catch (ValidationException ve) {
                        updateCnicExpiryResponse.setResponseCode("420");
                        updateCnicExpiryResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        updateCnicExpiryResponse.setResponseCode("220");
                        updateCnicExpiryResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Update Cnic Expiry Request *********");
                    logger.info("ResponseCode: " + updateCnicExpiryResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Update Cnic Expiry Request AUTHENTICATION *********");
                    updateCnicExpiryResponse = new UpdateCnicExpiryResponse();
                    updateCnicExpiryResponse.setResponseCode("420");
                    updateCnicExpiryResponse.setResponseDescription("Request is not authenticated");
                    updateCnicExpiryResponse.setRrn(request.getRrn());
                    updateCnicExpiryResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Update Cnic Expiry Request *********");
                updateCnicExpiryResponse = new UpdateCnicExpiryResponse();
                updateCnicExpiryResponse.setResponseCode("111");
                updateCnicExpiryResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            updateCnicExpiryResponse = new UpdateCnicExpiryResponse();
            updateCnicExpiryResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            updateCnicExpiryResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(updateCnicExpiryResponse);
        logger.info("Update Cnic Expiry Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return updateCnicExpiryResponse;
    }

    @RequestMapping(value = "api/l2AccountUpgradeDiscrepant", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    L2AccountUpgradeDiscrepantResponse l2AccountUpgradeDiscrepantResponse(@RequestBody L2AccountUpgradeDiscrepantRequest request) throws Exception {
        L2AccountUpgradeDiscrepantResponse l2AccountUpgradeDiscrepantResponse = new L2AccountUpgradeDiscrepantResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info(" L2 Account Upgrade Discrepant Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Merchant Picture Upgrade Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing L2 Account Upgrade Discrepant Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getCnic())
                    .append(request.getCnicFrontPic())
                    .append(request.getCnicBackPic())
                    .append(request.getCustomerPic())
                    .append(request.getConsumerName())
                    .append(request.getFatherHusbandName())
                    .append(request.getPurposeOfAccount())
                    .append(request.getSourceOfIncome())
                    .append(request.getSourceOfIncomePic())
                    .append(request.getExpectedMonthlyTurnover())
                    .append(request.getBirthPlace())
                    .append(request.getMotherMaiden())
                    .append(request.getEmailAddress())
                    .append(request.getMailingAddress())
                    .append(request.getPermanentAddress())
                    .append(request.getReserved1())
                    .append(request.getReserved2())
                    .append(request.getReserved3())
                    .append(request.getReserved4())
                    .append(request.getReserved5());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateL2AccountUpgradeDiscrepant(request);
                        l2AccountUpgradeDiscrepantResponse = integrationService.l2AccountUpgradeDiscrepantResponse(request);

                    } catch (ValidationException ve) {
                        l2AccountUpgradeDiscrepantResponse.setResponseCode("420");
                        l2AccountUpgradeDiscrepantResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        l2AccountUpgradeDiscrepantResponse.setResponseCode("220");
                        l2AccountUpgradeDiscrepantResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR L2 Account Upgrade Discrepant Request *********");
                    logger.info("ResponseCode: " + l2AccountUpgradeDiscrepantResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR L2 Account Upgrade Discrepant Request AUTHENTICATION *********");
                    l2AccountUpgradeDiscrepantResponse = new L2AccountUpgradeDiscrepantResponse();
                    l2AccountUpgradeDiscrepantResponse.setResponseCode("420");
                    l2AccountUpgradeDiscrepantResponse.setResponseDescription("Request is not authenticated");
                    l2AccountUpgradeDiscrepantResponse.setRrn(request.getRrn());
                    l2AccountUpgradeDiscrepantResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR L2 Account Upgrade Discrepant Request *********");
                l2AccountUpgradeDiscrepantResponse = new L2AccountUpgradeDiscrepantResponse();
                l2AccountUpgradeDiscrepantResponse.setResponseCode("111");
                l2AccountUpgradeDiscrepantResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            l2AccountUpgradeDiscrepantResponse = new L2AccountUpgradeDiscrepantResponse();
            l2AccountUpgradeDiscrepantResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            l2AccountUpgradeDiscrepantResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(l2AccountUpgradeDiscrepantResponse);
        logger.info(" L2 Account Upgrade Discrepant Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return l2AccountUpgradeDiscrepantResponse;
    }

    @RequestMapping(value = "api/getL2AccountUpgradeDiscrepant", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GetL2AccountUpgradeDiscrepantResponse getL2AccountUpgradeDiscrepantResponse(@RequestBody GetL2AccountUpgradeDiscrepantRequest request) throws Exception {
        GetL2AccountUpgradeDiscrepantResponse getL2AccountUpgradeDiscrepantResponse = new GetL2AccountUpgradeDiscrepantResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Get L2 Account Upgrade Discrepant Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Merchant Picture Upgrade Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Get L2 Account Upgrade Discrepant Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNo())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getCnic())
                    .append(request.getReserved1())
                    .append(request.getReserved2())
                    .append(request.getReserved3())
                    .append(request.getReserved4())
                    .append(request.getReserved5());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateGetL2AccountUpgradeDiscrepant(request);
                        getL2AccountUpgradeDiscrepantResponse = integrationService.getL2AccountUpgradeDiscrepantResponse(request);

                    } catch (ValidationException ve) {
                        getL2AccountUpgradeDiscrepantResponse.setResponseCode("420");
                        getL2AccountUpgradeDiscrepantResponse.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        getL2AccountUpgradeDiscrepantResponse.setResponseCode("220");
                        getL2AccountUpgradeDiscrepantResponse.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Get L2 Account Upgrade Discrepant Request *********");
                    logger.info("ResponseCode: " + getL2AccountUpgradeDiscrepantResponse.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Get L2 Account Upgrade Discrepant Request AUTHENTICATION *********");
                    getL2AccountUpgradeDiscrepantResponse = new GetL2AccountUpgradeDiscrepantResponse();
                    getL2AccountUpgradeDiscrepantResponse.setResponseCode("420");
                    getL2AccountUpgradeDiscrepantResponse.setResponseDescription("Request is not authenticated");
                    getL2AccountUpgradeDiscrepantResponse.setRrn(request.getRrn());
                    getL2AccountUpgradeDiscrepantResponse.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Get L2 Account Upgrade Discrepant Request *********");
                getL2AccountUpgradeDiscrepantResponse = new GetL2AccountUpgradeDiscrepantResponse();
                getL2AccountUpgradeDiscrepantResponse.setResponseCode("111");
                getL2AccountUpgradeDiscrepantResponse.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            getL2AccountUpgradeDiscrepantResponse = new GetL2AccountUpgradeDiscrepantResponse();
            getL2AccountUpgradeDiscrepantResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            getL2AccountUpgradeDiscrepantResponse.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(getL2AccountUpgradeDiscrepantResponse);
        logger.info("Get L2 Account Upgrade Discrepant Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));

        return getL2AccountUpgradeDiscrepantResponse;
    }

    @RequestMapping(value = "api/l2AccountUpgrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    L2AccountUpgradeResponse l2AccountUpgrade(@RequestBody L2AccountUpgradeRequest request) throws Exception {
        L2AccountUpgradeResponse response = new L2AccountUpgradeResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {
            String requestXML = JSONUtil.getJSON(request);
//            requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());

            logger.info("Start Processing L2 Account Upgrade Transaction Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));

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
        } catch (Exception e) {
            response = new L2AccountUpgradeResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }

        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("L2 Account Upgrade  Request  Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }
}
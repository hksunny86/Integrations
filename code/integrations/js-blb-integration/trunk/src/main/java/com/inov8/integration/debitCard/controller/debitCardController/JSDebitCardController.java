package com.inov8.integration.debitCard.controller.debitCardController;

import com.inov8.integration.debitCard.controller.validator.DebitCardHostRequestValidator;
import com.inov8.integration.debitCard.pdu.request.*;
import com.inov8.integration.debitCard.pdu.response.*;
import com.inov8.integration.debitCard.service.DebitCardService;
import com.inov8.integration.l2Account.pdu.response.L2AccountFieldsResponse;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.GetL2AccountUpgradeDiscrepantRequest;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@RestController
public class JSDebitCardController {

    private static Logger logger = LoggerFactory.getLogger(JSDebitCardController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");


    @Autowired
    DebitCardService debitCardService;

    @RequestMapping(value = "api/debitCardRevamp/debitCardFee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    DebitCardFeeResponse debitCardFeeResponse(@Valid @RequestBody DebitCardFeeRequest request) throws Exception {
        DebitCardFeeResponse response = new DebitCardFeeResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Debit Card Fees Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Debit Card Fees Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getTransactionType())
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
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (DebitCardHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        DebitCardHostRequestValidator.validateDebitCardFee(request);
                        response = debitCardService.debitCardFeeResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Debit Card Fees Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Debit Card Fees Request AUTHENTICATION *********");
                    response = new DebitCardFeeResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Debit Card Fees Request *********");
//                response = new DebitCardFeeResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {

            response = new DebitCardFeeResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Debit Card Fees Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

    @RequestMapping(value = "api/debitCardRevamp/debitCardDiscrepant", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    DebitCardDiscrepantResponse debitCardDiscrepantResponse(@Valid @RequestBody DebitCardDiscrepantRequest request) throws Exception {
        DebitCardDiscrepantResponse response = new DebitCardDiscrepantResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Debit Card Discrepant Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Debit Card Discrepant Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getName())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getStreetNumber())
                    .append(request.getHouseNumber())
                    .append(request.getCity())
                    .append(request.getArea())
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
                if (DebitCardHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        DebitCardHostRequestValidator.validateDebitCardDiscrepant(request);
                        response = debitCardService.debitCardDiscrepantResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Debit Card Discrepant Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Debit Card Discrepant Request AUTHENTICATION *********");
                    response = new DebitCardDiscrepantResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Debit Card Discrepant Request *********");
                response = new DebitCardDiscrepantResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new DebitCardDiscrepantResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Debit Card Discrepant Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

    @RequestMapping(value = "api/debitCardRevamp/v2DebitCardIssuanceInquiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    AppRebrandDebitCardIssuanceInquiryResponse appRebrandDebitCardIssuanceInquiryResponse(@Valid @RequestBody AppRebrandDebitCardIssuanceInquiryRequest request) throws Exception {
        AppRebrandDebitCardIssuanceInquiryResponse response = new AppRebrandDebitCardIssuanceInquiryResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Debit Card Issuance Inquiry Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Debit Card Issuance Inquiry Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
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
                    .append(request.getTransactionType())
                    .append(request.getCnic())
                    .append(request.getCardType())
                    .append(request.getReserved1())
                    .append(request.getReserved2());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (DebitCardHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        DebitCardHostRequestValidator.validateAppRebrandDebitCardIssuanceInquiry(request);
                        response = debitCardService.appRebrandDebitCardIssuanceInquiryResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Debit Card Issuance Inquiry Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Debit Card Issuance Inquiry Request AUTHENTICATION *********");
                    response = new AppRebrandDebitCardIssuanceInquiryResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Debit Card Issuance Inquiry Request *********");
//                response = new AppRebrandDebitCardIssuanceInquiryResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {

            response = new AppRebrandDebitCardIssuanceInquiryResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Debit Card Issuance Inquiry Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

    @RequestMapping(value = "api/debitCardRevamp/v2DebitCardIssuance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    AppRebrandDebitCardIssuanceResponse appRebrandDebitCardIssuanceInquiryResponse(@Valid @RequestBody AppRebrandDebitCardIssuanceRequest request) throws Exception {
        AppRebrandDebitCardIssuanceResponse response = new AppRebrandDebitCardIssuanceResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Debit Card Issuance  Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Debit Card Issuance  Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
                    .append(request.getCity())
                    .append(request.getArea())
                    .append(request.getStreetNumber())
                    .append(request.getHouseNumber())
                    .append(request.getReserved1())
                    .append(request.getReserved2());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (DebitCardHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        DebitCardHostRequestValidator.validateAppRebrandDebitCardIssuance(request);
                        response = debitCardService.appRebrandDebitCardIssuanceResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Debit Card Issuance  Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Debit Card Issuance  Request AUTHENTICATION *********");
                    response = new AppRebrandDebitCardIssuanceResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Debit Card Issuance  Request *********");
//                response = new AppRebrandDebitCardIssuanceResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {

            response = new AppRebrandDebitCardIssuanceResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Debit Card Issuance  Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

    @RequestMapping(value = "api/debitCardRevamp/getDebitCardDiscrepant", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GetDebitCardDiscrepantResponse getDebitCardDiscrepantResponse(@Valid @RequestBody GetDebitCardDiscrepantRequest request) throws Exception {
        GetDebitCardDiscrepantResponse response = new GetDebitCardDiscrepantResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Get Debit Card Discrepant Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Get Debit Card Discrepant Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
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
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (DebitCardHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        DebitCardHostRequestValidator.validateGetDebitCardDiscrepant(request);
                        response = debitCardService.getDebitCardDiscrepantResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Get Debit Card Discrepant Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Get Debit Card Discrepant Request AUTHENTICATION *********");
                    response = new GetDebitCardDiscrepantResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Get Debit Card Discrepant Request *********");
//                response = new GetDebitCardDiscrepantResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {

            response = new GetDebitCardDiscrepantResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Get Debit Card Discrepant Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }
}

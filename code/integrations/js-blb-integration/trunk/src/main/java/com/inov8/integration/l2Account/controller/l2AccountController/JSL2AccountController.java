package com.inov8.integration.l2Account.controller.l2AccountController;

import com.inov8.integration.l2Account.controller.validator.L2AccountHostRequestValidator;
import com.inov8.integration.l2Account.pdu.request.L2AccountFieldsRequest;
import com.inov8.integration.l2Account.pdu.request.L2AccountRequest;
import com.inov8.integration.l2Account.pdu.request.RateConversionRequest;
import com.inov8.integration.l2Account.pdu.request.UpdatePmdRequest;
import com.inov8.integration.l2Account.pdu.response.L2AccountFieldsResponse;
import com.inov8.integration.l2Account.pdu.response.L2AccountResponse;
import com.inov8.integration.l2Account.pdu.response.RateConversionResponse;
import com.inov8.integration.l2Account.pdu.response.UpdatePmdResponse;
import com.inov8.integration.l2Account.service.l2AccountHostService.L2AccountService;
import com.inov8.integration.middleware.controller.validator.ValidationException;
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
public class JSL2AccountController {

    private static Logger logger = LoggerFactory.getLogger(JSL2AccountController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");


    @Autowired
    L2AccountService l2AccountService;

    @RequestMapping(value = "api/l2Account/level2Accounts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    L2AccountResponse l2AccountResponse(@Valid @RequestBody L2AccountRequest request) throws Exception {
        L2AccountResponse response = new L2AccountResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Level 2 Accounts Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Level 2 Accounts Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
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
                    .append(request.getReserved5())
                    .append(request.getReserved6())
                    .append(request.getReserved7())
                    .append(request.getReserved8())
                    .append(request.getReserved9())
                    .append(request.getReserved10());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (L2AccountHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        L2AccountHostRequestValidator.validateL2Account(request);
                        response = l2AccountService.l2AccountResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Level 2 Accounts Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Level 2 Accounts Request AUTHENTICATION *********");
                    response = new L2AccountResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Level 2 Accounts Request *********");
                response = new L2AccountResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new L2AccountResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Level 2 Accounts Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

    @RequestMapping(value = "api/l2Account/l2AccountFields", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    L2AccountFieldsResponse l2AccountFieldsResponse(@Valid @RequestBody L2AccountFieldsRequest request) throws Exception {
        L2AccountFieldsResponse response = new L2AccountFieldsResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Level 2 Account Fields Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Level 2 Account Fields Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
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
                    .append(request.getReserved5())
                    .append(request.getReserved6())
                    .append(request.getReserved7())
                    .append(request.getReserved8())
                    .append(request.getReserved9())
                    .append(request.getReserved10());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (L2AccountHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        L2AccountHostRequestValidator.validateL2AccountFields(request);
                        response = l2AccountService.l2AccountFieldsResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Level 2 Account Fields Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Level 2 Account Fields Request AUTHENTICATION *********");
                    response = new L2AccountFieldsResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Level 2 Account Fields Request *********");
                response = new L2AccountFieldsResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new L2AccountFieldsResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Level 2 Account Fields Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

    @RequestMapping(value = "api/l2Account/updatePMDAndKYC", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    UpdatePmdResponse updatePmdResponse(@Valid @RequestBody UpdatePmdRequest request) throws Exception {
        UpdatePmdResponse response = new UpdatePmdResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Update PMD Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Update PMD Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getAccountID())
                    .append(request.getPmd())
                    .append(request.getKyc())
                    .append(request.getMotherName())
                    .append(request.getPlaceOfBirth())
                    .append(request.getReserved1())
                    .append(request.getReserved2())
                    .append(request.getReserved3())
                    .append(request.getReserved4())
                    .append(request.getReserved5());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (L2AccountHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        L2AccountHostRequestValidator.validateUpdatePmd(request);
                        response = l2AccountService.updatePmdResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Update PMD Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Update PMD Request AUTHENTICATION *********");
                    response = new UpdatePmdResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Update PMD Request *********");
                response = new UpdatePmdResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new UpdatePmdResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Update PMD Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

//    @RequestMapping(value = "api/l2Account/rateConversion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    RateConversionResponse rateConversionResponse(@Valid @RequestBody RateConversionRequest request) throws Exception {
//        RateConversionResponse response = new RateConversionResponse();
//
//        String className = this.getClass().getSimpleName();
//        String methodName = new Object() {
//        }.getClass().getEnclosingMethod().getName();
//        long start = System.currentTimeMillis();
//
//        try {
//
//            logger.info("Rate Conversion Request Received at Controller at time: " + start);
//            String requestXML = JSONUtil.getJSON(request);
//            //        requestXML = XMLUtil.maskPassword(requestXML);
//            String datetime = "";
//            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
//            datetime = DateFor.format(new Date());
//            logger.info("Start Processing Rate Conversion Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
//                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
//            StringBuilder stringText = new StringBuilder()
//                    .append(request.getUserName())
//                    .append(request.getPassword())
//                    .append(request.getMobileNumber())
//                    .append(request.getDateTime())
//                    .append(request.getRrn())
//                    .append(request.getChannelId())
//                    .append(request.getTerminalId())
//                    .append(request.getCurrencyId())
//                    .append(request.getReserved1())
//                    .append(request.getReserved2())
//                    .append(request.getReserved3())
//                    .append(request.getReserved4())
//                    .append(request.getReserved5());
//
//            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//                if (L2AccountHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                    try {
////                        L2AccountHostRequestValidator.validateRateConversion(request);
//                        response = l2AccountService.rateConversionResponse(request);
//
//                    } catch (ValidationException ve) {
//                        response.setResponseCode("420");
//                        response.setResponseDescription(ve.getMessage());
//
//                        logger.error("ERROR: Request Validation", ve);
//                    } catch (Exception e) {
//                        response.setResponseCode("220");
//                        response.setResponseDescription(e.getMessage());
//                        logger.error("ERROR: General Processing ", e);
//                    }
//
//                    logger.info("******* DEBUG LOGS FOR Rate Conversion Request *********");
//                    logger.info("ResponseCode: " + response.getResponseCode());
//                } else {
//                    logger.info("******* DEBUG LOGS FOR Rate Conversion Request AUTHENTICATION *********");
//                    response = new RateConversionResponse();
//                    response.setResponseCode("420");
//                    response.setResponseDescription("Request is not authenticated");
//                    response.setRrn(request.getRrn());
//                    response.setResponseDateTime(request.getDateTime());
//                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//
//                }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Rate Conversion Request *********");
//                response = new RateConversionResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
//        } catch (Exception e) {
//
//            response = new RateConversionResponse();
//            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
//            response.setResponseDescription(e.getLocalizedMessage());
//            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
//            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
//            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
//            logger.info("Critical Error ::" + e.getLocalizedMessage());
//        }
//        long end = System.currentTimeMillis() - start;
//        String responseXML = JSONUtil.getJSON(response);
//        logger.info("Rate Conversion Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));
//
//
//        return response;
//    }
}

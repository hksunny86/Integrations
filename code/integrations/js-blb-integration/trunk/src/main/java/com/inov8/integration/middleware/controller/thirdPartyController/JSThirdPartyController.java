package com.inov8.integration.middleware.controller.thirdPartyController;

import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.service.hostService.HostIntegrationService;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.webservice.vo.Data;
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
public class JSThirdPartyController {

    private static Logger logger = LoggerFactory.getLogger(JSThirdPartyController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");


    @Autowired
    HostIntegrationService integrationService;

    @RequestMapping(value = "api/accountStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    AccountStatusResponse accountStatusResponse(@Valid @RequestBody AccountStatusRequest request) throws Exception {
        AccountStatusResponse response = new AccountStatusResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Account Status Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            logger.info("Start Processing Account Status Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Account Status Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
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
                        HostRequestValidator.validateAccountStatus(request);
                        response = integrationService.accountStatusResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Account Status Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Account Status Request AUTHENTICATION *********");
                    response = new AccountStatusResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Account Status Request *********");
                response = new AccountStatusResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new AccountStatusResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        logger.info("Account Status Request Processed in : {} ms {}", end, "");

        return response;
    }

    @RequestMapping(value = "api/creditInquiryRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ThirdPartyCreditInquiryResponse thirdPartyCreditInquiryResponse(@Valid @RequestBody ThirdPartyCreditInquiryRequest request) throws Exception {
        ThirdPartyCreditInquiryResponse response = new ThirdPartyCreditInquiryResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Third Party Credit Inquiry Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Third Party Credit Inquiry Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Third Party Credit Inquiry Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
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
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateThirdPartyCreditInquiry(request);
                    response = integrationService.thirdPartyCreditInquiryResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setMessages(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setMessages(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Third Party Credit Inquiry Request *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Third Party Credit Inquiry Request AUTHENTICATION *********");
                response = new ThirdPartyCreditInquiryResponse();
                response.setResponseCode("420");
                response.setMessages("Request is not authenticated");
//                Data data = new Data();
//                data.setRrn(request.getRrn());
//                data.setResponseDateTime(request.getDateTime());
//                response.setData(data);
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Third Party Credit Inquiry Request *********");
//                response = new ThirdPartyCreditInquiryResponse();
//                response.setResponseCode("111");
//                response.setMessages("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {

            response = new ThirdPartyCreditInquiryResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setMessages(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        logger.info("Third Party Credit Inquiry Request Processed in : {} ms {}", end, "");

        return response;
    }

    @RequestMapping(value = "api/creditRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ThirdPartyCreditResponse thirdPartyCreditResponse(@Valid @RequestBody ThirdPartyCreditRequest request) throws Exception {
        ThirdPartyCreditResponse response = new ThirdPartyCreditResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Third Party Credit Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Third Party Credit Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Third Party Credit Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
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
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateThirdPartyCredit(request);
                    logger.info("Authentication successful");
                    response = integrationService.thirdPartyCreditResponse(request);
                    logger.info("Service Response successful");
                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setMessages(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setMessages(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR Third Party Credit Request *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Third Party Credit Request AUTHENTICATION *********");
                response = new ThirdPartyCreditResponse();
                response.setResponseCode("420");
                response.setMessages("Request is not authenticated");
//                Data data = new Data();
//                data.setRrn(request.getRrn());
//                data.setResponseDateTime(request.getDateTime());
//                response.setData(data);
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Third Party Credit Request *********");
//                response = new ThirdPartyCreditResponse();
//                response.setResponseCode("111");
//                response.setMessages("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {
            logger.error("Exception while processing request" + e.getMessage());
            response = new ThirdPartyCreditResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setMessages(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        logger.info("Third Party Credit Request Processed in : {} ms {}", end, "");

        return response;
    }

    @RequestMapping(value = "api/CustomerCliStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    CustomerCliStatusResponse customerCliStatusResponse(@Valid @RequestBody CustomerCliStatusRequest request) throws Exception {
        CustomerCliStatusResponse response = new CustomerCliStatusResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Customer Cli Status Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Third Party Credit Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Customer Cli Status Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getStan())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateCustomerCliStatus(request);
                        response = integrationService.customerCliStatusResponse(request);
                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }
                    logger.info("******* DEBUG LOGS FOR Customer Cli Status Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Customer Cli Status Request AUTHENTICATION *********");
                    response = new CustomerCliStatusResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR Customer Cli Status Request *********");
                response = new CustomerCliStatusResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {
            logger.error("Exception while processing request" + e.getMessage());
            response = new CustomerCliStatusResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        logger.info("Customer Cli Status Request Processed in : {} ms {}", end, "");

        return response;
    }

    @RequestMapping(value = "api/CustomerValidation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    CustomerValidationResponse customerValidationResponse(@Valid @RequestBody CustomerValidationRequest request) throws Exception {
        CustomerValidationResponse response = new CustomerValidationResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Customer Validation Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Third Party Credit Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Customer Validation Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getDateTime())
                    .append(request.getStan())
                    .append(request.getValueField())
                    .append(request.getCustomerIdentification())
                    .append(request.getChannelId())
                    .append(request.getTerminalId());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateCustomerValidation(request);
                        response = integrationService.customerValidationResponse(request);
                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());
                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }
                    logger.info("******* DEBUG LOGS FOR Customer Validation Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Customer Validation Request AUTHENTICATION *********");
                    response = new CustomerValidationResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR Customer Validation Request *********");
                response = new CustomerValidationResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {
            logger.error("Exception while processing request" + e.getMessage());
            response = new CustomerValidationResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        logger.info("Customer Validation Request Processed in : {} ms {}", end, "");

        return response;
    }
}

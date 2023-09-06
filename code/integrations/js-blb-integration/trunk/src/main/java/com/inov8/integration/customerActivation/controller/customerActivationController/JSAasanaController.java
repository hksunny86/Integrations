package com.inov8.integration.customerActivation.controller.customerActivationController;

import com.inov8.integration.customerActivation.controller.validator.AasanaHostRequestValidator;
import com.inov8.integration.customerActivation.pdu.request.CustomerActiveRequest;
import com.inov8.integration.customerActivation.pdu.response.CustomerActiveResponse;
import com.inov8.integration.customerActivation.service.customerActivationHostService.AasanaHostIntegrationService;
import com.inov8.integration.l2Account.controller.validator.L2AccountHostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.middleware.util.RSAEncryption;
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
public class JSAasanaController {

    private static Logger logger = LoggerFactory.getLogger(JSAasanaController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");
    private static final String ACCESS_TOKEN = ConfigReader.getInstance().getProperty("aasana.accessToken", "");
    private static final String ACCESS_TOKEN_PRIVATE_KEY = ConfigReader.getInstance().getProperty("aasana.accessToken.privateKey", "");


    @Autowired
    AasanaHostIntegrationService customerActivationService;

    @RequestMapping(value = "api/aasana/customerActive", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    CustomerActiveResponse customerActiveResponse(@RequestHeader("Access_Token") String accessToken, @Valid @RequestBody CustomerActiveRequest request) throws Exception {
        CustomerActiveResponse response = new CustomerActiveResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {
            String decryptedToke = Objects.requireNonNull(RSAEncryption.decrypt(accessToken, ACCESS_TOKEN_PRIVATE_KEY));
            if (decryptedToke.equalsIgnoreCase(ACCESS_TOKEN)) {
                logger.info("Asana Customer Active Request Received at Controller at time: " + start);
                String requestXML = JSONUtil.getJSON(request);
                String datetime = "";
                SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                datetime = DateFor.format(new Date());
                logger.info("Start Processing Asana Customer Active Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
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
                if (AasanaHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
//                        AasanaHostRequestValidator.validateCustomerActive(request);
                        response = customerActivationService.customerActiveResponse(request);

                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Asana Customer Active Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Asana Customer Active Request AUTHENTICATION *********");
                    response = new CustomerActiveResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Asana Customer Active Request *********");
//                response = new CustomerActiveResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
            } else {
                response = new CustomerActiveResponse();
                response.setResponseCode(HttpStatus.UNAUTHORIZED.toString());
                response.setResponseDescription("Unauthorized");
                logger.info("******* REQUEST IS UNAUTHORIZED, USE VALID ACCESS TOKEN *********");
            }
        } catch (Exception e) {

            response = new CustomerActiveResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Asana Customer Active Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

}

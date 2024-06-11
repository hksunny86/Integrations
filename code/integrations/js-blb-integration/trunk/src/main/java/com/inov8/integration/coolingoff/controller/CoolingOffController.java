package com.inov8.integration.coolingoff.controller;

import com.inov8.integration.coolingoff.controller.validator.CoolingOffRequestValidator;
import com.inov8.integration.coolingoff.pdu.request.ReleaseIbftRequest;
import com.inov8.integration.coolingoff.pdu.request.ToggleNotificationRequest;
import com.inov8.integration.coolingoff.pdu.response.ReleaseIbftResponse;
import com.inov8.integration.coolingoff.pdu.response.ToggleNotificationResponse;
import com.inov8.integration.coolingoff.service.CoolingOffService;
import com.inov8.integration.corporate.controller.validator.CorporateHostRequestValidator;
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
public class CoolingOffController {
    private static Logger logger = LoggerFactory.getLogger(CoolingOffController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");

    @Autowired
    CoolingOffService coolingOffService;

    @RequestMapping(value = "api/coolingOff/releaseAmount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ReleaseIbftResponse releaseIbftReponse(@Valid @RequestBody ReleaseIbftRequest request) throws Exception {
        ReleaseIbftResponse response = new ReleaseIbftResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Cooling Off Release IBFT Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Cooling Off Release IBFT Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getTransactionId())
                    .append(request.getReferenceNo())
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
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (CorporateHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    CoolingOffRequestValidator.validateReleaseIbftAmount(request);
                    response = coolingOffService.releaseIbftResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Cooling Off Release IBFT Request *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Cooling Off Release IBFT Request AUTHENTICATION *********");
                response = new ReleaseIbftResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                response.setRrn(request.getRrn());
                response.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Cooling Off Release IBFT Request *********");
//                response = new ReleaseIbftResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {

            response = new ReleaseIbftResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Cooling Off Release IBFT Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

    @RequestMapping(value = "api/coolingOff/toggleNotification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ToggleNotificationResponse toggleNotificationResponse(@Valid @RequestBody ToggleNotificationRequest request) throws Exception {
        ToggleNotificationResponse response = new ToggleNotificationResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {
            logger.info("Toggle Notificaiton Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Toggle Notificaiton Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getMobileNumber())
                    .append(request.getEmailAddress())
                    .append(request.getIsEnable())
                    .append(request.getType())
                    .append(request.getMpin())
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
            if (CorporateHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    CoolingOffRequestValidator.validateToggleNotification(request);
                    response = coolingOffService.toggleNotificationResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Toggle Notificaiton Request *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR Toggle Notificaiton Request AUTHENTICATION *********");
                response = new ToggleNotificationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                response.setRrn(request.getRrn());
                response.setResponseDateTime(request.getDateTime());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

            }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Toggle Notificaiton Request *********");
//                response = new LoginResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {

            response = new ToggleNotificationResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Toggle Notificaiton Request Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }
}

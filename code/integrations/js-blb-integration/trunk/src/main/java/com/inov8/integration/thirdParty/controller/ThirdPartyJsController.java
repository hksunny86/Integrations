package com.inov8.integration.thirdParty.controller;

import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.thirdParty.controller.validator.ThirdPartyHostRequestValidator;
import com.inov8.integration.thirdParty.pdu.request.*;
import com.inov8.integration.thirdParty.pdu.response.*;
import com.inov8.integration.thirdParty.service.ThirdPartyHostIntegrationService;
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


public class ThirdPartyJsController {

    private static Logger logger = LoggerFactory.getLogger(ThirdPartyJsController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");
    @Autowired
    ThirdPartyHostIntegrationService integrationService;

    @RequestMapping(value = "api/referralAccountInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ReferralAccountInfoResponse referralAccountInfo(@Valid @RequestBody ReferralAccountInfoRequest request) throws Exception {
        ReferralAccountInfoResponse response = new ReferralAccountInfoResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Referral Account Info Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Third Party Credit Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Referral Account Info Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (ThirdPartyHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        ThirdPartyHostRequestValidator.validateAccountInfo(request);
                        response = integrationService.referralAccountInfoResponse(request);
                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());
                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }
                    logger.info("******* DEBUG LOGS FOR Referral Account Info Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Referral Account Info Request AUTHENTICATION *********");
                    response = new ReferralAccountInfoResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            } else {
                logger.info("******* DEBUG LOGS FOR Referral Account Info Request *********");
                response = new ReferralAccountInfoResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {
            logger.error("Exception while processing request" + e.getMessage());
            response = new ReferralAccountInfoResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        logger.info("Referral Account Info Request Processed in : {} ms {}", end, "");

        return response;
    }
}
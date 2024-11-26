package com.inov8.integration.lending.controller;

import com.inov8.integration.lending.controller.validator.LendingHostRequestValidator;
import com.inov8.integration.lending.pdu.request.*;
import com.inov8.integration.lending.pdu.response.*;
import com.inov8.integration.lending.service.LendingHostIntegrationService;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.thirdParty.controller.validator.ThirdPartyHostRequestValidator;
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


public class LendingController {

    private static Logger logger = LoggerFactory.getLogger(LendingController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");
    @Autowired
    LendingHostIntegrationService integrationService;

    @RequestMapping(value = "lending/debitBlock", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    DebitBlockResponse debitBlock(@Valid @RequestBody DebitBlockRequest request) throws Exception {
        DebitBlockResponse response = new DebitBlockResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Debit Block Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Third Party Credit Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Debit Block Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getMobileNumber())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getLienMark())
                    .append(request.getLienAmount());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (ThirdPartyHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        LendingHostRequestValidator.validateDebitBlock(request);
                        response = integrationService.debitBlockResponse(request);
                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());
                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }
                    logger.info("******* DEBUG LOGS FOR Debit Block Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Debit Block Request AUTHENTICATION *********");
                    response = new DebitBlockResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
//            } else {
//                logger.info("******* DEBUG LOGS FOR Debit Block Request *********");
//                response = new DebitBlockResponse();
//                response.setResponseCode("111");
//                response.setResponseDescription("Request is not recognized");
//                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//            }
        } catch (Exception e) {
            logger.error("Exception while processing request" + e.getMessage());
            response = new DebitBlockResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        logger.info("Debit Block Request Processed in : {} ms {}", end, "");

        return response;
    }
}
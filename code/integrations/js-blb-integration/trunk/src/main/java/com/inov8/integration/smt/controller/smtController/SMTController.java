package com.inov8.integration.smt.controller.smtController;

// @Created On 4/17/2024 : Wednesday
// @Created By muhammad.aqeel

import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.AccountInfoRequest;
import com.inov8.integration.middleware.pdu.response.AccountInfoResponse;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.smt.pdu.request.AccountInfoSMTRequest;
import com.inov8.integration.smt.pdu.response.AccountInfoSMTResponse;
import com.inov8.integration.smt.service.SMTService;
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
public class SMTController {
    private static Logger logger = LoggerFactory.getLogger(SMTController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");
    @Autowired
    SMTService smtService;

    @RequestMapping(value = "api/accountInfoSMT", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    AccountInfoSMTResponse accountInfoSMTResponse(@Valid @RequestBody AccountInfoSMTRequest request) throws Exception {
        AccountInfoSMTResponse response = new AccountInfoSMTResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Account Info SMT Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
//            logger.info("Start Processing Third Party Credit Request with {}", requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Account Info SMT Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
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
//            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        HostRequestValidator.validateAccountInfoSMT(request);
                        response = smtService.accountInfoSMTResponse(request);
                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());
                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }
                    logger.info("******* DEBUG LOGS FOR Account Info Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Account Info Request AUTHENTICATION *********");
                    response = new AccountInfoSMTResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
                }
            /*} else {
                logger.info("******* DEBUG LOGS FOR Account Info Request *********");
                response = new AccountInfoSMTResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }*/
        } catch (Exception e) {
            logger.error("Exception while processing request" + e.getMessage());
            response = new AccountInfoSMTResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        logger.info("Account Info Request Processed in : {} ms {}", end, "");

        return response;
    }

}

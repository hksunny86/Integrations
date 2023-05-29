package com.inov8.integration.middleware.controller.thirdPartyController;

import com.inov8.integration.middleware.controller.restController.JSController;
import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.AccountStatusRequest;
import com.inov8.integration.middleware.pdu.request.MerchantPictureUpgradeRequest;
import com.inov8.integration.middleware.pdu.response.AccountStatusResponse;
import com.inov8.integration.middleware.pdu.response.MerchantPictureUpgradeResponse;
import com.inov8.integration.middleware.service.hostService.HostIntegrationService;
import com.inov8.integration.middleware.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class JSThirdPartyController {

    private static Logger logger = LoggerFactory.getLogger(JSThirdPartyController.class.getSimpleName());

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
        logger.info("Account Status Request Processed in : {} ms {}", end, response);

        return response;
    }
}

package com.inov8.integration.internationalRemittance.controller;

import com.inov8.integration.coolingoff.service.CoolingOffService;
import com.inov8.integration.corporate.controller.validator.CorporateHostRequestValidator;
import com.inov8.integration.internationalRemittance.controller.validator.InternationalRemittanceRequestValidator;
import com.inov8.integration.internationalRemittance.pdu.request.CoreToWalletCreditRequest;
import com.inov8.integration.internationalRemittance.pdu.request.TitleFetchRequestV2;
import com.inov8.integration.internationalRemittance.pdu.response.CoreToWalletCreditResponse;
import com.inov8.integration.internationalRemittance.pdu.response.TitleFetchResponseV2;
import com.inov8.integration.internationalRemittance.service.InternationalRemittanceService;
import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
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
public class InternationalRemittanceController {

    private static Logger logger = LoggerFactory.getLogger(InternationalRemittanceController.class.getSimpleName());
    private String uri = ConfigReader.getInstance().getProperty("logger.uri", "");
    private String ip = ConfigReader.getInstance().getProperty("logger.ip", "");
    private String guid = ConfigReader.getInstance().getProperty("logger.guid", "");

    @Autowired
    InternationalRemittanceService remittanceService;

    @RequestMapping(value = "api/v2/credit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    CoreToWalletCreditResponse CoreToWalletCredit(@Valid @RequestBody CoreToWalletCreditRequest request) throws Exception {
        CoreToWalletCreditResponse response = new CoreToWalletCreditResponse();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info(" Core to wallet Credit V2 Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Core to wallet Credit V2 Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getAccountNo())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getPin())
                    .append(request.getProductId())
                    .append(request.getPinType())
                    .append(request.getTransactionAmount())
                    .append(request.getStan())
                    .append(request.getBankIMD())
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
                if (CorporateHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        InternationalRemittanceRequestValidator.validateCoreToWalletCredit(request);
                        response = remittanceService.CoreToWalletCredit(request);
                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Core to Wallet Credit V2 Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Core to Wallet Credit V2 Request AUTHENTICATION *********");
                    response = new CoreToWalletCreditResponse();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Core to Wallet Credit V2 Request *********");
                response = new CoreToWalletCreditResponse();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new CoreToWalletCreditResponse();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Title fetch V2 Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }

    @RequestMapping(value = "api/v2/titleFetch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TitleFetchResponseV2 TitleFetchResponseV2(@Valid @RequestBody TitleFetchRequestV2 request) throws Exception {
        TitleFetchResponseV2 response = new TitleFetchResponseV2();

        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        long start = System.currentTimeMillis();

        try {

            logger.info("Title fetch V2 Request Received at Controller at time: " + start);
            String requestXML = JSONUtil.getJSON(request);
            //        requestXML = XMLUtil.maskPassword(requestXML);
            String datetime = "";
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            datetime = DateFor.format(new Date());
            logger.info("Start Processing Title fetch V2 Request with DateTime:" + datetime + " | URI: " + uri + " | IP: "
                    + ip + " | GUID: " + guid + " {}", Objects.requireNonNull(requestXML).replaceAll(System.getProperty("line.separator"), " "));
            StringBuilder stringText = new StringBuilder()
                    .append(request.getUserName())
                    .append(request.getPassword())
                    .append(request.getAccountNo())
                    .append(request.getDateTime())
                    .append(request.getRrn())
                    .append(request.getChannelId())
                    .append(request.getTerminalId())
                    .append(request.getPaymentMode())
                    .append(request.getSegmentCode())
                    .append(request.getReserved1())
                    .append(request.getReserved2())
                    .append(request.getReserved3())
                    .append(request.getReserved4())
                    .append(request.getReserved5());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            if (request.getHashData().equalsIgnoreCase(sha256hex)) {
                if (CorporateHostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                    try {
                        InternationalRemittanceRequestValidator.validateTitleFetchV2(request);
                        response = remittanceService.TitleFetchResponseV2(request);
                    } catch (ValidationException ve) {
                        response.setResponseCode("420");
                        response.setResponseDescription(ve.getMessage());

                        logger.error("ERROR: Request Validation", ve);
                    } catch (Exception e) {
                        response.setResponseCode("220");
                        response.setResponseDescription(e.getMessage());
                        logger.error("ERROR: General Processing ", e);
                    }

                    logger.info("******* DEBUG LOGS FOR Title fetch V2 Request *********");
                    logger.info("ResponseCode: " + response.getResponseCode());
                } else {
                    logger.info("******* DEBUG LOGS FOR Title fetch V2 Request AUTHENTICATION *********");
                    response = new TitleFetchResponseV2();
                    response.setResponseCode("420");
                    response.setResponseDescription("Request is not authenticated");
                    response.setRrn(request.getRrn());
                    response.setResponseDateTime(request.getDateTime());
                    logger.info("******* REQUEST IS NOT AUTHENTICATED *********");

                }
            } else {
                logger.info("******* DEBUG LOGS FOR Title fetch V2 Request *********");
                response = new TitleFetchResponseV2();
                response.setResponseCode("111");
                response.setResponseDescription("Request is not recognized");
                logger.info("******* REQUEST IS NOT RECOGNIZED *********");
            }
        } catch (Exception e) {

            response = new TitleFetchResponseV2();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setResponseDescription(e.getLocalizedMessage());
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e);
            logger.error("\n CLASS == " + className + " \n METHOD == " + methodName + "  ERROR ----- " + e.getLocalizedMessage());
            logger.info("\n EXITING THIS METHOD == " + methodName + " OF CLASS = " + className + " \n\n\n");
            logger.info("Critical Error ::" + e.getLocalizedMessage());
        }
        long end = System.currentTimeMillis() - start;
        String responseXML = JSONUtil.getJSON(response);
        logger.info("Title fetch V2 Processed in : {} ms {}", end, Objects.requireNonNull(responseXML).replaceAll(System.getProperty("line.separator"), ""));


        return response;
    }


}

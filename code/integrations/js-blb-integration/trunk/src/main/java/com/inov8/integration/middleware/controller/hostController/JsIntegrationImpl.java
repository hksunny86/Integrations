package com.inov8.integration.middleware.controller.hostController;

import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.service.hostService.HostIntegrationService;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.middleware.util.XMLUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.jws.WebService;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

@WebService(serviceName = "JsIntegrationService", portName = "JsIntegrationPort"
        , endpointInterface = "com.inov8.integration.middleware.controller.hostController.JsIntegration", targetNamespace = "http://tempuri.org/")
@Controller("JsIntegration")
public class JsIntegrationImpl implements JsIntegration {


    private static final Logger logger = LoggerFactory.getLogger(JsIntegrationImpl.class);
    private static String loginPinMatch = ConfigReader.getInstance().getProperty("loginPinMatch", "");

    @Autowired
    HostIntegrationService integrationService;

    public JsIntegrationImpl() {
        super();
    }


    @Override
    public ChequeBookResponse chequeBook(ChequeBookRequest request) {
        logger.info("Start Processing Cheque Book Status Update Transaction Request with {}");
        long start = System.currentTimeMillis();
        ChequeBookResponse response = new ChequeBookResponse();
        String requestXML = XMLUtil.convertRequest(request);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Start Processing Cheque Book Status Update  Transaction Request with {}", requestXML);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getMobileNumber() +
                        request.getRrn() +
                        request.getOrignalTransactionRRN() +
                        request.getChannelId() +
                        request.getTerminalId() +
                        request.getTransactionStatus() +
                        request.getReserved1() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (sha256hex.equalsIgnoreCase(request.getHashData())) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {

                try {
                    HostRequestValidator.validateChequeBook(request);
                    //for Mock
                    response = integrationService.chequeBookResponse(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR Cheque Book Status Update TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Cheque Book Status Update TRANSACTION AUTHENTICATION *********");
                response = new ChequeBookResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Cheque Book Status Update TRANSACTION *********");
            response = new ChequeBookResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Cheque Book Status Update Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }
}

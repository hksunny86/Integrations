package com.inov8.integration.middleware.controller;

import com.inov8.integration.middleware.controller.validation.RequestValidator;
import com.inov8.integration.middleware.controller.validation.ValidationException;
import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.request.BiometricVerificationRequest;
import com.inov8.integration.middleware.pdu.request.OTCVerificationRequest;
import com.inov8.integration.middleware.pdu.response.FingerPrintVerificationResponse;
import com.inov8.integration.middleware.pdu.response.OTCResponse;
import com.inov8.integration.middleware.service.IntegrationService;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.middleware.util.UnicodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController("jsNadraControllerImpl")
@Path("/api")
public class JsNadraControllerImpl {

    private static Logger logger = LoggerFactory.getLogger(JsNadraControllerImpl.class.getName());

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    TransactionDAO transactionDAO;

    @POST
    @Path("/verify")
    @Consumes("application/json")
    @Produces("application/json")
    public String fingerPrintVerification(BiometricVerificationRequest request) throws Exception {

        logger.info("Enter in Finger Print Verification Controller ");
        FingerPrintVerificationResponse response = new FingerPrintVerificationResponse();
        long start = System.currentTimeMillis();
        logger.info("request received " + JSONUtil.getJSON(request));
        TransactionLogModel logModel = new TransactionLogModel();
        long startTime = new Date().getTime(); // start time


        try {
            //removing sqa string from request to save xml in database as its length is too long
            String wsq = request.getForward().getWsq();
            request.getForward().setWsq("");
            logModel = prepareTransaction();
            logModel.setTransactionCode("FingerPrint Verification");
            logModel.setPduRequestString(UnicodeUtil.escapeUnicode(JSONUtil.getJSON(request)));
            logModel.setChannelId("JS-Nadra");
            saveTransaction(logModel);
            //setting again sqa string in request
            request.getForward().setWsq(wsq);
            RequestValidator.validateFingerPrintVerification(request);
            response = integrationService.fingerPrintVerification(request);
        } catch (ValidationException ve) {
            logger.error("ValidationException ", ve);
            FingerPrintVerificationResponse.Status status = new FingerPrintVerificationResponse.Status();
            status.setCode("420");
            status.setMessage(ve.getMessage());
            response.setStatus(status);
        } catch (Exception e) {
            logger.error("General Exception Occurred ", e);
            FingerPrintVerificationResponse.Status status = new FingerPrintVerificationResponse.Status();
            status.setCode("505");
            status.setMessage("General Exception Occurred");
            response.setStatus(status);
        }

        long end = System.currentTimeMillis() - start;
        logger.info(" processed in {} ms", end);
        String json = JSONUtil.getJSON(response);
        logModel.setPduResponseString(UnicodeUtil.escapeUnicode(json));
        logModel.setResponseCode(response.getStatus().getCode());
        logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        logModel.setProcessedTime(end);
        updateTransactionInDB(logModel);
        logger.info("Response Json send back " + json);
        return json;
    }

    @POST
    @Path("/otc")
    @Consumes("application/json")
    @Produces("application/json")
    public String OTCVerification(OTCVerificationRequest request) throws Exception {

        logger.info("Enter in OTC Verification Controller ");
        OTCResponse response = new OTCResponse();
        long start = System.currentTimeMillis();
        logger.info("request received " + JSONUtil.getJSON(request));
        TransactionLogModel logModel = new TransactionLogModel();
        long startTime = new Date().getTime(); // start time


        try {
            //Setting in logModel
            logModel = prepareTransaction();
            logModel.setTransactionCode("OTC Verification");
            logModel.setChannelId("JS-Nadra");
            logModel.setPduRequestString(UnicodeUtil.escapeUnicode(JSONUtil.getJSON(request)));
            saveTransaction(logModel);

            RequestValidator.validateOTCVerification(request);
            response = integrationService.otcVerification(request);
        } catch (ValidationException ve) {
            logger.error("ValidationException ", ve);
            OTCResponse.Status status = new OTCResponse.Status();
            status.setCode("420");
            status.setMessage(ve.getMessage());
            response.setStatus(status);
        } catch (Exception e) {
            logger.error("General Exception Occurred ", e);
            OTCResponse.Status status = new OTCResponse.Status();
            status.setCode("505");
            status.setMessage("General Exception Occurred");
            response.setStatus(status);
        }

        String json = JSONUtil.getJSON(response);
        logger.info("Response Json send back " + json);
        long end = System.currentTimeMillis() - start;
        logger.info(" processed in {} ms", end);

        logModel.setPduResponseString(JSONUtil.getJSON(response));
        logModel.setResponseCode(response.getStatus().getCode());
        logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        logModel.setProcessedTime(end);
        updateTransactionInDB(logModel);
        return json;
    }

    private void updateTransactionInDB(TransactionLogModel trx) {
        this.transactionDAO.update(trx);
        logger.debug("[HOST] Transaction updated with RRN: " + trx.getRetrievalRefNo());
    }

    //
//	/*
//	 * SAVES TRANSACTION REQUEST PDU INTO THE DATABASE
//	 */
    private TransactionLogModel saveTransaction(TransactionLogModel transaction) {

        try {
            this.transactionDAO.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("[HOST] Transaction saved with RRN: " + transaction.getRetrievalRefNo());
        return transaction;
    }

    private TransactionLogModel prepareTransaction() {
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmssSSS");
        Date txDateTime = new Date();
        String dateTime = String.valueOf(txDateTime.getTime());
        try {
            txDateTime = dateFormat.parse(dateTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(dateTime);
        logModel.setTransactionDateTime(txDateTime);
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        return logModel;
    }
}

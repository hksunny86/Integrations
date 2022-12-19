package com.inov8.integration.middleware.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.inov8.integration.middleware.controller.validation.MiddlewareTransactionRequestValidator;
import com.inov8.integration.middleware.controller.validation.MiddlewareValidationException;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.service.IntegrationService;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.vo.MiddlewareMessageVO;

@Controller("TransactionSwitchController")
public class TransactionSwitchController implements MiddlewareSwitchController {
    private static Logger logger = LoggerFactory.getLogger(MiddlewareSwitchController.class.getSimpleName());
    private String debug = ConfigReader.getInstance().getProperty("debug", "false");

    @Autowired
    private IntegrationService integrationService;

    @Override
    public MiddlewareMessageVO billInquiry(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validateCheckBillRequest(transactionVO);
            logger.info("Processing Check Bill: " + transactionVO);

            transactionVO = integrationService.checkBill(transactionVO);

            logger.info("******* DEBUG LOGS FOR CHECK BILL *********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());

        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for Check Bill Request", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ Bill Inquiry", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** BILL INQUIRY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed Bill Inquiry: " + transactionVO);
        return transactionVO;
    }

    @Override
    public MiddlewareMessageVO billPayment(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validatePayBillRequest(transactionVO);
            logger.info("Processing Bill Payment: " + transactionVO);

            transactionVO = integrationService.billPayment(transactionVO);

            logger.info("******* DEBUG LOGS FOR BILL PAYMENT *********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());

        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for Pay Bill Request", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ Bill Payment", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** PAY BILL REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed Bill Payment: " + transactionVO);
        return transactionVO;
    }

    @Override
    public MiddlewareMessageVO titleFetch(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validateTitleFetchRequest(transactionVO);
            logger.info("Processing Title Fetch: " + transactionVO);

            transactionVO = integrationService.titleFetch(transactionVO);

            logger.info("******* DEBUG LOGS FOR TITLE FETCH *********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());

            if (debug.equals("true") && transactionVO != null && transactionVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("Account Number: " + transactionVO.getAccountNo1());
                logger.info("Account Title: " + transactionVO.getAccountTitle());
            }

        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for Title Fetch Request", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ Title Fetch Request", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** TITLE FETCH REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed Title Fetch: " + transactionVO);
        return transactionVO;
    }

    @Override
    public MiddlewareMessageVO fundTransfer(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validateFundTransferRequest(transactionVO);
            logger.info("Processing Fund Transfer: " + transactionVO);

            transactionVO = integrationService.fundTransfer(transactionVO);

            logger.info("******* DEBUG LOGS FOR FUND TRANSFER *********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());

        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for Fund Transfer Request", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ Fund Transfer Request", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** FUND TRANSFER REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed Fund Transfer: " + transactionVO);
        return transactionVO;
    }

    @Override
    public MiddlewareMessageVO fundTransferAdvice(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validateFundTransferAdviceRequest(transactionVO);
            logger.info("Processing Fund Transfer Advice: " + transactionVO);

            transactionVO = integrationService.fundTransferAdvice(transactionVO);

            logger.info("******* DEBUG LOGS FOR FUND TRANSFER ADVICE*********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());

        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for Fund Transfer Advice Request ", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ Fund Transfer Advice Request ", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** FUND TRANSFER Advice REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed Fund Transfer: " + transactionVO);
        return transactionVO;
    }

    @Override
    public MiddlewareMessageVO acquirerReversalAdvice(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validateAcquirerReversalAdviceRequest(transactionVO);
            logger.info("Processing Acquirer Reversal Advice: " + transactionVO);

            transactionVO = integrationService.acquirerReversalAdvice(transactionVO);

            logger.info("******* DEBUG LOGS FOR ACQUIRER REVERSAL ADVICE *********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());

        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for ACQUIRER REVERSAL ADVICE", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ ACQUIRER REVERSAL ADVICE", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** ACQUIRER REVERSAL ADVICE REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed ACQUIRER REVERSAL ADVICE: " + transactionVO);
        return transactionVO;
    }

    @Override
    public MiddlewareMessageVO accountBalanceInquiry(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validateAccountBalanceInquiryRequest(transactionVO);
            logger.info("Processing Account Balance Inquiry: " + transactionVO);

            transactionVO = integrationService.accountBalanceInquiry(transactionVO);

            logger.info("******* DEBUG LOGS FOR ACCOUNT BALANCE INQUIRY *********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());

        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for ACCOUNT BALANCE INQUIRY", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ ACCOUNT BALANCE INQUIRY", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** ACCOUNT BALANCE INQUIRY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed ACCOUNT BALANCE INQUIRY: " + transactionVO);
        return transactionVO;
    }

    @Override
    public MiddlewareMessageVO ibftTitleFetch(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validateIBFTTitleFetchRequest(transactionVO);
            logger.info("Processing IBFT Title Fetch Request: " + transactionVO);
            transactionVO = integrationService.ibftTitleFetch(transactionVO);

            logger.info("******* DEBUG LOGS FOR IBFT Title Fetch *********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());
        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for IBFT Title Fetch ", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ IBFT Title Fetch", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** IBFT Title Fetch REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed IBFT Title Fetch: " + transactionVO);
        return transactionVO;

    }

    @Override
    public MiddlewareMessageVO ibftAdvice(MiddlewareMessageVO transactionVO) throws RuntimeException {
        long startTime = new Date().getTime(); // start time

        try {
            MiddlewareTransactionRequestValidator.validateIBFTAdviceRequest(transactionVO);
            logger.info("Processing IBFT Advice Request: " + transactionVO);
            transactionVO = integrationService.ibftAdvice(transactionVO);

            logger.info("******* DEBUG LOGS FOR IBFT Advice *********");
            logger.info("ResponseCode: " + transactionVO.getResponseCode());
            logger.info("Retrival Reference Number: " + transactionVO.getRetrievalReferenceNumber());
            logger.info("Microbank Transaction Code: " + transactionVO.getMicrobankTransactionCode());
        } catch (MiddlewareValidationException e) {
            logger.error("Validation Failed for IBFT Advice ", e);
            transactionVO.setResponseCode("420");
        } catch (Exception e) {
            logger.error("Internal Error@ IBFT Advice", e);
            transactionVO.setResponseCode("220");
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** IBFT Advice REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        transactionVO.setTransmissionTime(new Date());
        logger.info("Processed IBFT Advice: " + transactionVO);
        return transactionVO;
    }


}

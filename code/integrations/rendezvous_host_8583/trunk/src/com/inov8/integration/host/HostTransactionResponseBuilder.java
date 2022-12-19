package com.inov8.integration.host;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.novatti.Transactions;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.pdu.response.BBFundTransferAdviceResponse;
import com.inov8.integration.middleware.pdu.response.BBTitleFetchResponse;
import com.inov8.integration.middleware.pdu.response.BBWalletIBFTAdviceResponse;
import com.inov8.integration.middleware.pdu.response.CashWithDrawalResponse;
import com.inov8.integration.middleware.pdu.response.CashWithDrawalReversalResponse;
import com.inov8.integration.middleware.pdu.response.CoreToWalletResponse;
import com.inov8.integration.middleware.pdu.response.POSRefundResponse;
import com.inov8.integration.middleware.pdu.response.PosReverseTransactionResponse;
import com.inov8.integration.middleware.pdu.response.PosTransactionResponse;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.*;

import com.inov8.integration.middleware.enums.JSBankDefaultsEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.persistance.TransactionDAO;
import com.inov8.integration.middleware.persistance.model.TransactionLogModel;
import com.inov8.integration.middleware.service.HostIntegrationService;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.middleware.util.FormatUtils;
import com.inov8.integration.middleware.util.ISO8583Utils;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.vo.Transaction;
import com.inov8.integration.webservice.vo.WebServiceVO;

import javax.crypto.Mac;

@Component
public class HostTransactionResponseBuilder {
    private Logger logger = LoggerFactory.getLogger(HostTransactionResponseBuilder.class.getSimpleName());

    @Autowired
    private HostIntegrationService hostIntegrationService = new HostIntegrationService();

    @Autowired
    private TransactionDAO transactionDAO;

    public BBTitleFetchResponse titleFetchBBHandler(BasePDU requestPDU) {
        // Save Transaction Request in database
        saveTransaction(requestPDU);

        BBTitleFetchResponse titleFetchResponse = new BBTitleFetchResponse();

        // Call Microbank via HostIntegrationService
        MiddlewareMessageVO vo = new MiddlewareMessageVO();

        vo.setRequestTime(getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime()));
        vo.setPAN(requestPDU.getPan());
        vo.setStan(requestPDU.getStan());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setAccountNo1(requestPDU.getAccountNo1());
        vo.setAccountNo2(requestPDU.getAccountNo2());
        vo.setTransactionAmount(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
        vo.setBankIMD(requestPDU.getAcquirerIdentification());

        try {
            hostIntegrationService.titleFetchBBCommand(vo);
        } catch (Exception ex) {
            logger.error("ERROR: ", ex);
        }

        // Build Respone back to RDV
        titleFetchResponse.setPan(requestPDU.getPan());
        titleFetchResponse.setProcessingCode(requestPDU.getProcessingCode());
        titleFetchResponse.setStan(requestPDU.getStan());
        titleFetchResponse.setConversionDate(requestPDU.getConversionDate());
        titleFetchResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        titleFetchResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        titleFetchResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        titleFetchResponse.setTransactionDate(requestPDU.getTransactionDate());
        titleFetchResponse.setTransactionAmount(requestPDU.getTransactionAmount());

        if (vo.getResponseCode() != null) {
            titleFetchResponse.setResponseCode(vo.getResponseCode());
            titleFetchResponse.setAdditionalResponseData(vo.getAccountTitle());
            titleFetchResponse.setAdditionalAmount(ISO8583Utils.formatISO8583Amount(vo.getAccountBalance()));
        } else {
            titleFetchResponse.setResponseCode("08"); // Host Comm Down
            titleFetchResponse.setAdditionalResponseData("X");
            titleFetchResponse.setAdditionalAmount("0");
        }

        titleFetchResponse.build();

        // update Transaction response in database
        updateTransaction(titleFetchResponse);

        return titleFetchResponse;
    }

    public CashWithDrawalResponse cashWithDrawalHandler(BasePDU requestPDU) {
        // save transaction Request in database
        String transactionKey = requestPDU.getTransactionLocalDate() + requestPDU.getRrn();
        saveTransaction(requestPDU);
        logger.debug("Request Recieve from Rdv" + requestPDU);
        // call Microbank via HostIntegration
        MiddlewareMessageVO vo = new MiddlewareMessageVO();
        vo.setRequestTime(getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime()));
        vo.setTransactionAmount(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
        vo.setAmountCardHolderBilling(FormatUtils.parseMiddlewareAmount(requestPDU.getAmountCardHolderBilling()));
        vo.setStan(requestPDU.getStan());
        vo.setMerchantType(requestPDU.getMerchantType());
        vo.setPosEntryMode(requestPDU.getPosEntryMode());
        vo.setAccquireIdentificationCode(requestPDU.getAcquirerIdentification());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setPAN(requestPDU.getPan());
        vo.setCardAcceptorNameAndLocation(requestPDU.getCardAcceptorName());
        vo.setCurrencyCode(requestPDU.getTransactionCurrencyCode());
        vo.setRecievingInstitutionCode(requestPDU.getRecievingInstitutionCode());
        vo.setTerminalId("DEBIT_CARD");
        vo.setAccountNo1(requestPDU.getAccountNo1());

        try {
            hostIntegrationService.cashWithDrawalCommand(vo);
        } catch (Exception e) {
            logger.error("Error", e);
        }
        // Build Respone back to RDV
        CashWithDrawalResponse cashWithDrawalResponse = new CashWithDrawalResponse();
        cashWithDrawalResponse.setPan(requestPDU.getPan());
        cashWithDrawalResponse.setProcessingCode(requestPDU.getProcessingCode());
        cashWithDrawalResponse.setStan(requestPDU.getStan());
        cashWithDrawalResponse.setTransactionAmount(requestPDU.getTransactionAmount());
        cashWithDrawalResponse.setAmountCardHolderBilling(requestPDU.getAmountCardHolderBilling());
        cashWithDrawalResponse.setTransactionDate(requestPDU.getTransactionDate());
        cashWithDrawalResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        cashWithDrawalResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        cashWithDrawalResponse.setMerchantType(requestPDU.getMerchantType());
        cashWithDrawalResponse.setPosEntryMode(requestPDU.getPosEntryMode());
        cashWithDrawalResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
        cashWithDrawalResponse.setTrack2Data(StringUtils.rightPad(requestPDU.getTransactionLocalDate(), 37, ' '));
        cashWithDrawalResponse.setRrn(requestPDU.getRrn());
        cashWithDrawalResponse.setTerminalId(StringUtils.leftPad(requestPDU.getTransactionLocalDate(), 8, '0'));
        cashWithDrawalResponse.setCardAcceptorName(requestPDU.getCardAcceptorName());
        cashWithDrawalResponse.setTransactionCurrencyCode(requestPDU.getTransactionCurrencyCode());
        cashWithDrawalResponse.setRecievingInstitutionCode(requestPDU.getRecievingInstitutionCode());

        if (vo != null) {
            String authIdResponse = StringUtils.leftPad(transactionDAO.getNextRRNSequence(), 6, '0');
            cashWithDrawalResponse.setAuthIdResponse(authIdResponse);
        }
        if (vo != null && StringUtils.isNotEmpty(vo.getResponseCode()) && vo.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            String actualBalance = FormatUtils.parseI8Amount(vo.getAccountBalance());
            String additionalResponseData = "2" + StringUtils.leftPad(actualBalance, 12, '0');
            cashWithDrawalResponse.setAdditionalResponseData(additionalResponseData);
            cashWithDrawalResponse.setResponseCode(vo.getResponseCode());
        } else if (vo != null && StringUtils.isNotEmpty(vo.getResponseCode())) {

            logger.info("[HOST] Wallet Cash Withdrawl Request Unsuccessful from Micro Bank RRN: " + transactionKey);
            cashWithDrawalResponse.setResponseCode(vo.getResponseCode());
        } else {
            logger.info(
                    "[HOST] Wallet Cash Withdrawl Request HOST NOT PROCESSING from Micro Bank RRN: " + transactionKey);
            cashWithDrawalResponse.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
        }
        cashWithDrawalResponse.build();
        // update in transactionlog table
        updateTransaction(cashWithDrawalResponse);
        // response send to rdv
        logger.debug("response send to rdv" + cashWithDrawalResponse);
        return cashWithDrawalResponse;
    }

    public MiniStatementResponse miniStatementHandler(BasePDU requestPDU) {
        // save transaction Request in database
        saveTransaction(requestPDU);
        // call Microbank via HostIntegration
        WebServiceVO vo = new WebServiceVO();
        Date dateTime = getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime());
        vo.setTransactionDateTime(requestPDU.getTransactionDate());
        vo.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        vo.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        vo.setCardNo(requestPDU.getPan());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setTerminalId("DEBIT_CARD");
        try {

            hostIntegrationService.MiniStatementCommand(vo);
        } catch (Exception e) {
            logger.error("Error", e);
        }
        // Build Respone back to RDV
        MiniStatementResponse miniStatementResponse = new MiniStatementResponse();
        miniStatementResponse.setPan(requestPDU.getPan());
        miniStatementResponse.setProcessingCode(requestPDU.getProcessingCode());
        miniStatementResponse.setStan(requestPDU.getStan());
        miniStatementResponse.setTransactionAmount(StringUtils.rightPad(requestPDU.getStan(), 12, '0'));
        miniStatementResponse.setTransactionDate(requestPDU.getTransactionDate());
        miniStatementResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        miniStatementResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        miniStatementResponse.setSettlementDate(requestPDU.getTransactionLocalDate());
        miniStatementResponse.setMerchantType(requestPDU.getMerchantType());
        miniStatementResponse.setPosEntryMode(requestPDU.getPosEntryMode());
        miniStatementResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
        miniStatementResponse.setTrack2Data(StringUtils.rightPad(requestPDU.getTransactionLocalDate(), 37, ' '));
        miniStatementResponse.setRrn(requestPDU.getRrn());
        miniStatementResponse.setTerminalId(requestPDU.getTerminalId());
        miniStatementResponse.setCardAcceptorName(requestPDU.getCardAcceptorName());
        miniStatementResponse.setCardAcceptorIdentificationCode(StringUtils.leftPad(requestPDU.getAcquirerIdentification(), 15, '0'));
        miniStatementResponse.setTransactionCurrencyCode(requestPDU.getTransactionCurrencyCode());
        if (vo != null) {

            String authIdResponse = StringUtils.leftPad(transactionDAO.getNextRRNSequence(), 6, '0');
            miniStatementResponse.setAuthIdResponse(authIdResponse);
            miniStatementResponse.setResponseCode(vo.getResponseCode());
            StringBuilder result = new StringBuilder();
            List<Transaction> transactions = vo.getTransactions();
            Iterator<Transaction> transaction = transactions.iterator();

            while (transaction.hasNext()) {
                Transaction transactionlist = transaction.next();
                result.append(transactionlist.getDescription()).append("|").append(transactionlist.getAmount()).append("|").append(transactionlist.getDate()).append("||");

            }

            miniStatementResponse.setRecordData(result.toString());

        } else {
            miniStatementResponse.setResponseCode("08");
            miniStatementResponse.setRecordData("X");
        }
        miniStatementResponse.build();
        // update in transactionlog table
        updateTransaction(miniStatementResponse);

        return miniStatementResponse;
    }

    public BalanceInquiryResponse balanceInquiryResponseHandler(BasePDU requestPDU) {
        saveTransaction(requestPDU);
        WebServiceVO vo = new WebServiceVO();
        String transactionKey = requestPDU.getTransactionLocalDate() + requestPDU.getRrn();
        Date dateTime = getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime());
        vo.setTransactionDateTime(String.valueOf(dateTime));
        vo.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        vo.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setAccountNo1(requestPDU.getAccountNo1());
        vo.setCardNo(requestPDU.getPan());
        vo.setCurrencyCode(requestPDU.getTransactionCurrencyCode());
        vo.setChannelId(requestPDU.getAcquirerIdentification());
        vo.setTerminalId("DEBIT_CARD");
        try {
            hostIntegrationService.BalanceInquiryCommand(vo);
        } catch (Exception e) {
            logger.error("error" + e.getMessage());
        }
        // build response back to Rdv
        BalanceInquiryResponse balanceInquiryResponse = new BalanceInquiryResponse();
        balanceInquiryResponse.setPan(requestPDU.getPan());
        balanceInquiryResponse.setProcessingCode(requestPDU.getProcessingCode());
        balanceInquiryResponse.setStan(requestPDU.getStan());
        balanceInquiryResponse.setTransactionDate(requestPDU.getTransactionDate());
        balanceInquiryResponse.setTransactionAmount(requestPDU.getTransactionFee());
        balanceInquiryResponse.setSettlementAmount(requestPDU.getSettlementFee());
        balanceInquiryResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        balanceInquiryResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        balanceInquiryResponse.setSettlementDate(requestPDU.getSettlementDate());
        balanceInquiryResponse.setMerchantType(requestPDU.getMerchantType());
        balanceInquiryResponse.setNetworkIdentifier(requestPDU.getNetworkIdentifier());
        if (vo != null && StringUtils.isNotEmpty(vo.getResponseCode()) && vo.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

            String availableBalance = FormatUtils.parseI8Amount(vo.getBalance());
            String additionalResponseData = "2" + StringUtils.leftPad(availableBalance, 12, '0');
            balanceInquiryResponse.setAdditionalAmount(additionalResponseData);
            balanceInquiryResponse.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());

        } else if (vo != null && StringUtils.isNotEmpty(vo.getResponseCode())) {
            logger.info("[HOST] Check Balance Request Unsuccessful from Micro Bank RRN: " + transactionKey);
            balanceInquiryResponse.setResponseCode(vo.getResponseCode());
        } else {
            logger.info("[HOST] Check Balance Request HOST NOT PROCESSING from Micro Bank RRN: " + transactionKey);
            balanceInquiryResponse.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
        }
        balanceInquiryResponse.build();
        updateTransaction(balanceInquiryResponse);
        return balanceInquiryResponse;
    }

    public CashWithDrawalReversalResponse cashWithDrawalReversalHandler(BasePDU requestPDU) {
        saveTransaction(requestPDU);
        // call MicroBank Via HostIntegrationService
        MiddlewareMessageVO vo = new MiddlewareMessageVO();


        vo.setRequestTime(getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime()));
        vo.setPAN(requestPDU.getPan());
        vo.setStan(requestPDU.getStan());
        vo.setMerchantType(requestPDU.getMerchantType());
        vo.setPosEntryMode(requestPDU.getPosEntryMode());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setAccquireIdentificationCode(requestPDU.getAcquirerIdentification());
        vo.setOrignalStan(StringUtils.substring(requestPDU.getOriginalDataElement(), 4, 10));
        vo.setOrignalTransactionDateTime(StringUtils.substring(requestPDU.getOriginalDataElement(), 10, 20));
        vo.setTransactionAmount(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
        vo.setTerminalId("DEBIT_CARD");
        try {
            hostIntegrationService.cashWithDrawalReversalCommand(vo);

        } catch (Exception e) {
            logger.error("error" + e.getMessage());

        }
        // response back to Rdv
        CashWithDrawalReversalResponse cashWithDrawalReversalResponse = new CashWithDrawalReversalResponse();
        cashWithDrawalReversalResponse.setPan(requestPDU.getPan());
        cashWithDrawalReversalResponse.setProcessingCode(requestPDU.getProcessingCode());
        cashWithDrawalReversalResponse.setStan(requestPDU.getStan());
        cashWithDrawalReversalResponse.setTransactionDate(requestPDU.getTransactionDate());
        cashWithDrawalReversalResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        cashWithDrawalReversalResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        cashWithDrawalReversalResponse.setMerchantType(requestPDU.getMerchantType());
        cashWithDrawalReversalResponse.setRrn(requestPDU.getRrn());
        cashWithDrawalReversalResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
        cashWithDrawalReversalResponse.setRecievingInstitutionCode(requestPDU.getRecievingInstitutionCode());
        cashWithDrawalReversalResponse.setTransactionCurrencyCode(requestPDU.getTransactionCurrencyCode());
        cashWithDrawalReversalResponse.setCardAcceptorName(requestPDU.getCardAcceptorName());
        cashWithDrawalReversalResponse.setTerminalId(requestPDU.getTerminalId());
        cashWithDrawalReversalResponse.setTransactionAmount(requestPDU.getTransactionAmount());
        if (vo.getResponseCode() != null) {
            cashWithDrawalReversalResponse.setResponseCode(vo.getResponseCode());
        } else {
            cashWithDrawalReversalResponse.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
        }

        return cashWithDrawalReversalResponse;
    }

    public PosTransactionResponse posTransactionHandler(BasePDU requestPDU) {
        // save Transaction Request In Database
        String transactionKey = requestPDU.getTransactionLocalDate() + requestPDU.getRrn();
        saveTransaction(requestPDU);
        // call MicroBank Via HostIntegrationService
        MiddlewareMessageVO vo = new MiddlewareMessageVO();
        vo.setPAN(requestPDU.getPan());
        vo.setTransactionAmount(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
        vo.setAmountCardHolderBilling(FormatUtils.parseMiddlewareAmount(requestPDU.getAmountCardHolderBilling()));
        vo.setRequestTime(getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime()));
        vo.setStan(requestPDU.getStan());
        vo.setMerchantType(requestPDU.getMerchantType());
        vo.setPosEntryMode(requestPDU.getPosEntryMode());
        vo.setAccquireIdentificationCode(requestPDU.getAcquirerIdentification());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setCardAcceptorNameAndLocation(requestPDU.getCardAcceptorName());
        vo.setCurrencyCode(requestPDU.getTransactionCurrencyCode());
        vo.setAccountNo1(requestPDU.getAccountNo1());
        vo.setTerminalId("DEBIT_CARD");
        try {
            hostIntegrationService.PosTransactionCommand(vo);
        } catch (Exception e) {
            logger.info("Error" + e.getMessage());
        }
        // Response Back RDV

        PosTransactionResponse posTransactionResponse = new PosTransactionResponse();
        posTransactionResponse.setPan(requestPDU.getPan());
        posTransactionResponse.setProcessingCode(requestPDU.getProcessingCode());
        posTransactionResponse.setStan(requestPDU.getStan());
        posTransactionResponse.setTransactionDate(requestPDU.getTransactionDate());
        posTransactionResponse.setTransactionAmount(requestPDU.getTransactionAmount());
        posTransactionResponse.setAmountCardHolderBilling(requestPDU.getAmountCardHolderBilling());
        posTransactionResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        posTransactionResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        posTransactionResponse.setMerchantType(requestPDU.getMerchantType());
        posTransactionResponse.setPosEntryMode(requestPDU.getPosEntryMode());
        posTransactionResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
        posTransactionResponse.setRrn(requestPDU.getRrn());
        posTransactionResponse.setTerminalId(requestPDU.getTerminalId());
        posTransactionResponse.setCardAcceptorName(requestPDU.getCardAcceptorName());
        posTransactionResponse.setTrack2Data(requestPDU.getTransactionLocalDate());
        posTransactionResponse.setCardAcceptorIdentificationCode(StringUtils.leftPad(requestPDU.getAcquirerIdentification(), 15, '0'));

        posTransactionResponse.setTransactionCurrencyCode(requestPDU.getTransactionCurrencyCode());
        if (vo != null) {
            String authIdResponse = StringUtils.leftPad(transactionDAO.getNextRRNSequence(), 6, '0');
            posTransactionResponse.setAuthIdResponse(authIdResponse);
        }
        if (vo != null && StringUtils.isNotEmpty(vo.getResponseCode()) && vo.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

            posTransactionResponse.setResponseCode(vo.getResponseCode());
            //posTransactionResponse.setTransactionAmount(FormatUtils.parseMiddlewareAmount(vo.getTransactionAmount()));
            String actualBalance = FormatUtils.parseI8Amount(vo.getAccountBalance());
            String additionalResponseData = "2" + StringUtils.leftPad(actualBalance, 12, '0');
            posTransactionResponse.setAdditionalResponseData(additionalResponseData);

        } else if (vo != null && StringUtils.isNotEmpty(vo.getResponseCode())) {

            logger.info("[HOST] Pos Transaction Request Unsuccessful from Micro Bank RRN: " + transactionKey);
            posTransactionResponse.setResponseCode(vo.getResponseCode());
        } else {
            logger.info("[HOST] Pos Transaction Request Host Not Processed from Micro Bank RRN: " + transactionKey);
            posTransactionResponse.setResponseCode("08");
        }
        posTransactionResponse.build();
        updateTransaction(posTransactionResponse);
        return posTransactionResponse;

    }

    public POSRefundResponse posRefundHandler(BasePDU requestPDU) {
        // save Transaction Request In Database
        String transactionKey = requestPDU.getTransactionLocalDate() + requestPDU.getRrn();
        saveTransaction(requestPDU);
        // call MicroBank Via HostIntegrationService
        MiddlewareMessageVO vo = new MiddlewareMessageVO();
        vo.setPAN(requestPDU.getPan());
        vo.setTransactionAmount(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
        vo.setRequestTime(getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime()));
        vo.setStan(requestPDU.getStan());
        vo.setMerchantType(requestPDU.getMerchantType());
        vo.setPosEntryMode(requestPDU.getPosEntryMode());
        vo.setAccquireIdentificationCode(requestPDU.getAcquirerIdentification());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setCardAcceptorNameAndLocation(requestPDU.getCardAcceptorName());
        vo.setCurrencyCode(requestPDU.getTransactionCurrencyCode());
        vo.setAccountNo1(requestPDU.getAccountNo1());
        vo.setTerminalId("DEBIT_CARD");
        try {

            hostIntegrationService.PosRefundCommand(vo);

        } catch (Exception e) {
            logger.info("Error" + e.getMessage());
        }
        // Response Back RDV

        POSRefundResponse posRefundResponse = new POSRefundResponse();
        posRefundResponse.setPan(requestPDU.getPan());
        posRefundResponse.setProcessingCode(requestPDU.getProcessingCode());
        posRefundResponse.setStan(requestPDU.getStan());
        posRefundResponse.setTransactionAmount(requestPDU.getTransactionAmount());
        posRefundResponse.setTransactionDate(requestPDU.getTransactionDate());
        posRefundResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        posRefundResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        posRefundResponse.setMerchantType(requestPDU.getMerchantType());
        posRefundResponse.setPosEntryMode(requestPDU.getPosEntryMode());
        posRefundResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
        posRefundResponse.setRrn(requestPDU.getRrn());
        posRefundResponse.setTerminalId(requestPDU.getTerminalId());
        posRefundResponse.setCardAcceptorName(requestPDU.getCardAcceptorName());
        posRefundResponse.setTransactionCurrencyCode(requestPDU.getTransactionCurrencyCode());

        if (vo != null) {
            String authIdResponse = StringUtils.leftPad(transactionDAO.getNextRRNSequence(), 6, '0');
            posRefundResponse.setAuthIdResponse(authIdResponse);

        }
        if (vo != null && StringUtils.isNotEmpty(vo.getResponseCode()) && vo.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            String actualBalance = FormatUtils.parseI8Amount(vo.getAccountBalance());
            String additionalResponseData = "2" + StringUtils.leftPad(actualBalance, 12, '0');
            posRefundResponse.setAdditionalResponseData(additionalResponseData);
            posRefundResponse.setResponseCode(vo.getResponseCode());
        } else if (vo != null && StringUtils.isNotEmpty(vo.getResponseCode())) {

            logger.info("[HOST] Wallet POS Refund Request Unsuccessful from Micro Bank RRN: " + transactionKey);
            posRefundResponse.setResponseCode(vo.getResponseCode());
        } else {
            logger.info(
                    "[HOST] Wallet POS Refund Request HOST NOT PROCESSING from Micro Bank RRN: " + transactionKey);
            posRefundResponse.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
        }
        posRefundResponse.build();
        updateTransaction(posRefundResponse);
        return posRefundResponse;

    }

    public PosReverseTransactionResponse posReverseTransactionHandler(BasePDU requestPDU) {
        // save Transaction Request In Database
        String transactionKey = requestPDU.getTransactionLocalDate() + requestPDU.getRrn();
        saveTransaction(requestPDU);
        // call MicroBank Via HostIntegrationService
        MiddlewareMessageVO vo = new MiddlewareMessageVO();
        vo.setPAN(requestPDU.getPan());
        vo.setTransactionAmount(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
        vo.setRequestTime(getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime()));
        vo.setStan(requestPDU.getStan());
        vo.setMerchantType(requestPDU.getMerchantType());
        vo.setPosEntryMode(requestPDU.getPosEntryMode());
        vo.setAccquireIdentificationCode(requestPDU.getAcquirerIdentification());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setCardAcceptorNameAndLocation(requestPDU.getCardAcceptorName());
        vo.setCurrencyCode(requestPDU.getTransactionCurrencyCode());
        vo.setAccountNo1(requestPDU.getAccountNo1());
        vo.setTerminalId("DEBIT_CARD");
        vo.setOrignalStan(StringUtils.substring(requestPDU.getOriginalDataElement(), 4, 10));
        vo.setOrignalTransactionDateTime(StringUtils.substring(requestPDU.getOriginalDataElement(), 10, 20));

        try {
            hostIntegrationService.PosReverseCommand(vo);
        } catch (Exception e) {
            logger.info("Error" + e.getMessage());
        }
        // Response Back RDV

        PosReverseTransactionResponse posRefundResponse = new PosReverseTransactionResponse();
        posRefundResponse.setPan(requestPDU.getPan());
        posRefundResponse.setProcessingCode(requestPDU.getProcessingCode());
        posRefundResponse.setStan(requestPDU.getStan());
        posRefundResponse.setTransactionDate(requestPDU.getTransactionDate());
        posRefundResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        posRefundResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        posRefundResponse.setMerchantType(requestPDU.getMerchantType());
        posRefundResponse.setPosEntryMode(requestPDU.getPosEntryMode());
        posRefundResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
        posRefundResponse.setRrn(requestPDU.getRrn());
        posRefundResponse.setTerminalId(requestPDU.getTerminalId());
        posRefundResponse.setCardAcceptorName(requestPDU.getCardAcceptorName());
        posRefundResponse.setTransactionCurrencyCode(requestPDU.getTransactionCurrencyCode());
        posRefundResponse.setTransactionAmount(requestPDU.getTransactionAmount());
        if (vo.getResponseCode() != null) {
            logger.info("[HOST] Wallet POS Reverse Request Unsuccessful from Micro Bank RRN: " + transactionKey);
            String authIdResponse = StringUtils.leftPad(transactionDAO.getNextRRNSequence(), 6, '0');
            posRefundResponse.setAuthIdResponse(authIdResponse);
            posRefundResponse.setResponseCode(vo.getResponseCode());

        } else {
            logger.info("[HOST] Wallet POS Reverse Request HOST NOT PROCESSING from Micro Bank RRN: " + transactionKey);
            posRefundResponse.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
        }
        posRefundResponse.build();
        updateTransaction(posRefundResponse);
        return posRefundResponse;

    }

    public BBFundTransferAdviceResponse funTransferBBAdviceHandler(BasePDU requestPDU) {
        // Save Transaction Request in database
        saveTransaction(requestPDU);

        // Call Microbank via HostIntegrationService
        MiddlewareMessageVO vo = new MiddlewareMessageVO();

        vo.setRequestTime(getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime()));
        vo.setPAN(requestPDU.getPan());
        vo.setStan(requestPDU.getStan());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setAccountNo1(requestPDU.getAccountNo1());
        vo.setAccountNo2(requestPDU.getAccountNo2());
        vo.setTransactionAmount(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
        vo.setBankIMD(requestPDU.getAcquirerIdentification());

        try {
            hostIntegrationService.funTransferBBAdviceCommand(vo);
        } catch (Exception ex) {
            logger.error("ERROR: ", ex);
        }

        // Build Respone back to RDV
        BBFundTransferAdviceResponse adviceResponse = new BBFundTransferAdviceResponse();
        adviceResponse.setPan(requestPDU.getPan());
        adviceResponse.setProcessingCode(requestPDU.getProcessingCode());
        adviceResponse.setStan(requestPDU.getStan());
        adviceResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        adviceResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        adviceResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        adviceResponse.setTransactionDate(requestPDU.getTransactionDate());
        adviceResponse.setTransactionAmount(requestPDU.getTransactionAmount());
        adviceResponse.setSettlementAmount(requestPDU.getSettlementAmount());

        if (vo.getResponseCode() != null) {
            adviceResponse.setResponseCode(vo.getResponseCode());
        } else {
            adviceResponse.setResponseCode("08"); // Host Comm Down
        }

        adviceResponse.build();

        // update Transaction response in database
        updateTransaction(adviceResponse);

        return adviceResponse;
    }

    public CoreToWalletResponse coreToWalletAdviceHandler(BasePDU requestPDU) {
        // Save Transaction Request in database
        saveTransaction(requestPDU);

        // Call Microbank via HostIntegrationService
        MiddlewareMessageVO vo = new MiddlewareMessageVO();

        vo.setRequestTime(getRequestTime(requestPDU.getTransactionLocalDate(), requestPDU.getTransactionLocalTime()));
        vo.setPAN(requestPDU.getPan());
        vo.setStan(requestPDU.getStan());
        vo.setRetrievalReferenceNumber(requestPDU.getRrn());
        vo.setAccountNo1(requestPDU.getAccountNo1());
        vo.setAccountNo2(requestPDU.getAccountNo2());
        vo.setTransactionAmount(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
        vo.setBankIMD(requestPDU.getAcquirerIdentification());

        try {
            hostIntegrationService.coreToWalletAdviceCommand(vo);
        } catch (Exception ex) {
            logger.error("ERROR: ", ex);
        }

        // Build Respone back to RDV
        CoreToWalletResponse coreToWalletResponse = new CoreToWalletResponse();

        coreToWalletResponse.setPan(requestPDU.getPan());
        coreToWalletResponse.setProcessingCode(requestPDU.getProcessingCode());
        coreToWalletResponse.setTransactionAmount(requestPDU.getTransactionAmount());
        coreToWalletResponse.setSettlementAmount(requestPDU.getSettlementAmount());
        coreToWalletResponse.setTransactionDate(requestPDU.getTransactionDate());
        coreToWalletResponse.setTransactionTime(requestPDU.getTransactionTime());
        coreToWalletResponse.setStan(requestPDU.getStan());
        coreToWalletResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        coreToWalletResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        coreToWalletResponse.setSettlementDate(requestPDU.getSettlementDate());
        coreToWalletResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        coreToWalletResponse.setSettlementProcessingFee("");

        if (vo.getResponseCode() != null) {
            coreToWalletResponse.setResponseCode(vo.getResponseCode());
        } else {
            coreToWalletResponse.setResponseCode("08"); // Host Comm Down
        }

        coreToWalletResponse.build();

        // update Transaction response in database
        updateTransaction(coreToWalletResponse);

        return coreToWalletResponse;
    }

    public BBWalletIBFTAdviceResponse walletIBFTBBAdviceHandler(BasePDU requestPDU) {
        BBWalletIBFTAdviceResponse adviceResponse = new BBWalletIBFTAdviceResponse();
        adviceResponse.setPan(requestPDU.getPan());
        adviceResponse.setProcessingCode(requestPDU.getProcessingCode());
        adviceResponse.setStan(requestPDU.getStan());
        adviceResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        adviceResponse.setTransactionLocalTime(requestPDU.getTransactionLocalTime());
        adviceResponse.setTransactionLocalDate(requestPDU.getTransactionLocalDate());
        adviceResponse.setTransactionDate(requestPDU.getTransactionDate());
        adviceResponse.setResponseCode("00");
        adviceResponse.setTransactionAmount(requestPDU.getTransactionAmount());
        adviceResponse.setSettlementDate(DateTools.dateToString(new Date(), DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        return adviceResponse;
    }

    private void updateTransaction(BasePDU basePDU) {
        TransactionLogModel trx = new TransactionLogModel();
        trx.setRetrievalRefNo(basePDU.getRrn());
        trx.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        // basePDU.assemblePDU();
        try {
            trx.setPduResponseString(new String(basePDU.getRawPdu(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Exception", e);
        }
        trx.setPduResponseHEX(String.valueOf(Hex.encodeHex(basePDU.getRawPdu())));
        trx.setResponseCode(basePDU.getResponseCode());
        trx.setProcessedTime(0L);
//        this.transactionDAO.update(trx);
    }

    private void saveTransaction(BasePDU pdu) {

        TransactionLogModel transaction = new TransactionLogModel();
        BaseHeader requestHeader = pdu.getHeader();
        transaction.setRetrievalRefNo(pdu.getRrn());
        transaction.setMessageType(requestHeader.getMessageType());
        transaction.setTransactionCode(pdu.getProcessingCode());
        transaction.setTransactionDateTime(new Date());

        String requestPDUString;
        try {
            requestPDUString = new String(pdu.getRawPdu(), "UTF-8");
            transaction.setPduRequestString(requestPDUString);
        } catch (UnsupportedEncodingException e1) {
            logger.error("Exception", e1);
        }
        transaction.setPduRequestHEX(String.valueOf(Hex.encodeHex(pdu.getRawPdu())));

        transaction.setStatus(TransactionStatus.PROCESSING.getValue().longValue());

        try {
            this.transactionDAO.save(transaction);
        } catch (Exception e) {
            logger.error("Exception", e);
        }

        logger.debug("Transaction saved with RRN: " + transaction.getRetrievalRefNo());
    }

    private Date getRequestTime(String date, String time) {
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(
                    DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue() + TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue());
            DateTime dt = formatter.parseDateTime(date + time).withYear(new DateTime().getYear());
            return dt.toDate();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return null;
    }

    //    Test
//    public static void main(String[] args) {
//
//
//        HostIntegrationService hostIntegrationService = new HostIntegrationService();
//        WebServiceVO webServiceVO = new WebServiceVO();
//        //MiddlewareMessageVO
//        BasePDU vo = new BasePDU();
//
//        vo.setTransactionLocalTime("190503");
//        vo.setTransactionLocalDate("0423");
//
//        vo.setAccountNo1("03207894562");
//        vo.setAccountNo2("03207894562");
//        vo.setPan("2364463265706663");
//        vo.setMerchantType("0000");
//        vo.setTerminalId("DEBIT_CARD");
//        vo.setRrn("0157629870043");
//        vo.setStan("875028");
//        //        messageVO.setTerminalId("DEBIT_CARD");
//        vo.setMerchantType("0003");
//        vo.setTransactionAmount("000000000700");
//        vo.setAmountCardHolderBilling("000000070000");
//        vo.setAcquirerIdentification("60373300001");
//        vo.setTransactionCurrencyCode("589");
////        messageVO.setRetrievalReferenceNumber("4374590001876010");
//        HostTransactionResponseBuilder hostTransactionResponseBuilder = new HostTransactionResponseBuilder();
//
//
//        hostTransactionResponseBuilder.posTransactionHandler(vo);
//    }

}

package com.inov8.integration.middleware.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

import com.inov8.integration.middleware.pdu.request.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inov8.integration.middleware.enums.JSBankDefaultsEnum;
import com.inov8.integration.middleware.enums.MessageTypeEnum;
import com.inov8.integration.middleware.enums.MiddlewareEnum;
import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.persistance.TransactionDAO;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.middleware.util.FieldUtil;
import com.inov8.integration.middleware.util.ISO8583Utils;
import com.inov8.integration.vo.MiddlewareMessageVO;

@Component
public class IntegrationTransactionRequestBuilder {

    @Autowired
    private TransactionDAO transactionDAO;


    public AccountBalanceInquiryRequest buildAccountBalanceInquiryRequest(MiddlewareMessageVO messageVO) {
        AccountBalanceInquiryRequest accountBalanceInquiry = new AccountBalanceInquiryRequest();

        String stan = transactionDAO.getNextRRNSequence();

        accountBalanceInquiry.setPan(messageVO.getPAN());
        accountBalanceInquiry.setProcessingCode(TransactionCodeEnum.JS_ACCOUNT_BALANCE_INQUIRY.getValue());

        accountBalanceInquiry.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
        accountBalanceInquiry.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));

        // Request Time will come from i8
        if (messageVO.getRequestTime() == null) {
            messageVO.setRequestTime(new Date());
        }

        accountBalanceInquiry.setTransactionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));

        accountBalanceInquiry.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
        accountBalanceInquiry.setStan(stan);
        accountBalanceInquiry.setTransactionLocalTime(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        accountBalanceInquiry.setTransactionLocalDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        accountBalanceInquiry.setSettlementDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        accountBalanceInquiry.setMerchantType("0008");
        accountBalanceInquiry.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        accountBalanceInquiry.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
        accountBalanceInquiry.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
        accountBalanceInquiry.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());

        accountBalanceInquiry.setRrn(FieldUtil.buildRRN(accountBalanceInquiry.getStan()));
        accountBalanceInquiry.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
        accountBalanceInquiry.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
        accountBalanceInquiry.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        accountBalanceInquiry.setAccountNo1(messageVO.getAccountNo1());

        String rrnKey = this.getVersionClass(accountBalanceInquiry.getHeader().getMessageType()) + accountBalanceInquiry.getTransactionDate() + stan;

        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(accountBalanceInquiry.getRrn());

        return accountBalanceInquiry;
    }

    public BillInquiryRequest buildBillInquiryRequest(MiddlewareMessageVO messageVO) {
        BillInquiryRequest bir = new BillInquiryRequest();
        bir.getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());

        String stan = transactionDAO.getNextRRNSequence();
        bir.setPan(messageVO.getPAN());
        bir.setProcessingCode(TransactionCodeEnum.JS_BILL_INQUIRY.getValue());

        bir.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
        bir.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));

        // Request Time will come from i8
        if (messageVO.getRequestTime() == null) {
            messageVO.setRequestTime(new Date());
        }

        bir.setTransactionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
        bir.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
        bir.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
        bir.setStan(stan);
        bir.setTransactionLocalTime(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        bir.setTransactionLocalDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        bir.setSettlementDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        bir.setConversionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        bir.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        bir.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
        bir.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
        bir.setAcquirerIdentification(JSBankDefaultsEnum.BILL_INQUIRY_ACQUIRER_IDENTIFICATION.getValue());
        bir.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
        bir.setRrn(FieldUtil.buildRRN(bir.getStan()));
        bir.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
        bir.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        bir.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        bir.setAccountNo1(messageVO.getAccountNo1());

        String privateDate = messageVO.getCnicNo() + MiddlewareEnum.JS_DELIMITER.getValue() + messageVO.getConsumerNo()
                + MiddlewareEnum.JS_DELIMITER.getValue() + messageVO.getCompanyCode() + MiddlewareEnum.JS_DELIMITER.getValue();

        bir.setPrivateData(privateDate);
        String rrnKey = this.getVersionClass(bir.getHeader().getMessageType()) + bir.getTransactionDate() + stan;


        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(bir.getRrn());

        return bir;
    }


    public TitleFetchRequest buildTitleFetchRequest(MiddlewareMessageVO messageVO) {
        TitleFetchRequest tfr = new TitleFetchRequest();
        tfr.getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());

        String stan = transactionDAO.getNextRRNSequence();

        tfr.setPan(messageVO.getPAN());
        tfr.setProcessingCode(TransactionCodeEnum.JS_TITLE_FETCH.getValue());
        tfr.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));

        // Request Time will come from i8
        if (messageVO.getRequestTime() == null) {
            messageVO.setRequestTime(new Date());
        }

        tfr.setTransactionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
        tfr.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
        tfr.setStan(stan);
        tfr.setTransactionLocalTime(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        tfr.setTransactionLocalDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        tfr.setSettlementDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        tfr.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        tfr.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
        tfr.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
        tfr.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
        tfr.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
        tfr.setRrn(FieldUtil.buildRRN(tfr.getStan()));
        tfr.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
        tfr.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        tfr.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        tfr.setAccountNo1(messageVO.getAccountNo1());


        String rrnKey = this.getVersionClass(tfr.getHeader().getMessageType()) + tfr.getTransactionDate() + stan;

        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(tfr.getRrn());

        return tfr;
    }

    public BillPaymentRequest buildBillPaymentRequest(MiddlewareMessageVO messageVO) {
        BillPaymentRequest bpr = new BillPaymentRequest();
        bpr.getHeader().setMessageType(MessageTypeEnum.MT_0220.getValue());


        if (messageVO.getStan() != null) {
            bpr.setStan(messageVO.getStan());
        } else {
            String stan = transactionDAO.getNextRRNSequence();
            bpr.setStan(stan);
        }

        bpr.setPan(messageVO.getPAN());
        bpr.setProcessingCode(TransactionCodeEnum.JS_BILL_PAYMENT.getValue());
        bpr.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
        bpr.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));


        // Request Time will come from i8
        if (messageVO.getRequestTime() == null) {
            messageVO.setRequestTime(new Date());
        }

        if (messageVO.getTimeLocalTransaction() == null) {
            messageVO.setTimeLocalTransaction(new Date());
        }

        if (messageVO.getDateLocalTransaction() == null) {
            messageVO.setDateLocalTransaction(new Date());
        }

        bpr.setTransactionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
        bpr.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());

        //Requested by Kashif Bashir
        bpr.setTransactionLocalTime(DateTools.dateToString(messageVO.getTimeLocalTransaction(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        bpr.setTransactionLocalDate(DateTools.dateToString(messageVO.getDateLocalTransaction(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        //End

        bpr.setSettlementDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        bpr.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        bpr.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
        bpr.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
        bpr.setSettlementProcessingFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetProcessingFee()));
        bpr.setAcquirerIdentification(JSBankDefaultsEnum.BILL_INQUIRY_ACQUIRER_IDENTIFICATION.getValue());
        bpr.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());

        // RRN will come from
        if (StringUtils.isNotEmpty(messageVO.getRetrievalReferenceNumber())) {
            bpr.setRrn(messageVO.getRetrievalReferenceNumber());
            ;
        } else {
            bpr.setRrn(FieldUtil.buildRRN(bpr.getStan()));
        }

        bpr.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
        bpr.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        bpr.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        bpr.setAccountNo1(messageVO.getAccountNo1());

        String cnicNo = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getCnicNo())) ? " " : messageVO.getCnicNo();
        String consumerNo = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getConsumerNo())) ? " " : messageVO.getConsumerNo();
        String consumerName = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getConsumerName())) ? " " : messageVO.getConsumerName();
        String companyCode = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getCompanyCode())) ? " " : messageVO.getCompanyCode();
        String dueDateStr = DateTools.dateToString(messageVO.getBillDueDate(), "yyMMdd");
        String dueDate = StringUtils.isBlank(dueDateStr) ? " " : dueDateStr;
        String aggregatorCode = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getBillAggregator())) ? " " : messageVO.getBillAggregator();

        // Removed ubp STAN upon JS Request
        String ubpSTAN = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getUbpSTAN())) ? " " : messageVO.getUbpSTAN();

        String privateDate = cnicNo
                + MiddlewareEnum.JS_DELIMITER.getValue() + consumerNo
                + MiddlewareEnum.JS_DELIMITER.getValue() + consumerName
                + MiddlewareEnum.JS_DELIMITER.getValue() + companyCode
                + MiddlewareEnum.JS_DELIMITER.getValue() + dueDate
                + MiddlewareEnum.JS_DELIMITER.getValue() + aggregatorCode
                + MiddlewareEnum.JS_DELIMITER.getValue() + messageVO.getBillCategoryId()


//				+ MiddlewareEnum.JS_DELIMITER.getValue();
                // Removed ubp STAN upon JS Request
                + MiddlewareEnum.JS_DELIMITER.getValue() + ubpSTAN + MiddlewareEnum.JS_DELIMITER.getValue();

        bpr.setPrivateData(privateDate);

        String rrnKey = this.getVersionClass(bpr.getHeader().getMessageType()) + bpr.getTransactionDate() + bpr.getStan();

        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(bpr.getRrn());

        return bpr;
    }

    public FundTransferRequest buildFundTransferRequest(MiddlewareMessageVO messageVO) {
        FundTransferRequest ftr = new FundTransferRequest();
        ftr.getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());
        String amt = null;
        String stan = transactionDAO.getNextRRNSequence();

        ftr.setPan(messageVO.getPAN());

        if (messageVO.getTransactionAmount() == null) {
            amt = String.valueOf(0.0D);
        } else {
            amt = ISO8583Utils.formatDouble(Double.valueOf(messageVO.getTransactionAmount()));

        }

        ftr.setProcessingCode(TransactionCodeEnum.JS_FUND_TRANSFER.getValue());

        ftr.setTransactionAmount(ISO8583Utils.formatISO8583Amount(amt));
        ftr.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));


        // Request Time will come from i8
        if (messageVO.getRequestTime() == null) {
            messageVO.setRequestTime(new Date());
        }

        if (messageVO.getTimeLocalTransaction() == null) {
            messageVO.setTimeLocalTransaction(new Date());
        }

        if (messageVO.getDateLocalTransaction() == null) {
            messageVO.setDateLocalTransaction(new Date());
        }

        ftr.setTransactionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
        ftr.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
        ftr.setStan(stan);

        //Requested by Kashif Bashir
        ftr.setTransactionLocalTime(DateTools.dateToString(messageVO.getTimeLocalTransaction(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ftr.setTransactionLocalDate(DateTools.dateToString(messageVO.getDateLocalTransaction(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        //End

        ftr.setSettlementDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ftr.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        ftr.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
        ftr.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
        ftr.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
        ftr.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());

        // RRN will come from
        if (StringUtils.isNotEmpty(messageVO.getRetrievalReferenceNumber())) {
            ftr.setRrn(messageVO.getRetrievalReferenceNumber());
            ;
        } else {
            ftr.setRrn(FieldUtil.buildRRN(ftr.getStan()));
        }

        ftr.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
        ftr.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        ftr.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        ftr.setAccountNo1(messageVO.getAccountNo1());
        ftr.setAccountNo2(messageVO.getAccountNo2());

        String rrnKey = this.getVersionClass(ftr.getHeader().getMessageType()) + ftr.getTransactionDate() + stan;

        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(ftr.getRrn());

        return ftr;
    }

    public FundTransferAdviceRequest buildFundTransferAdviceRequest(MiddlewareMessageVO messageVO) {
        FundTransferAdviceRequest ftar = new FundTransferAdviceRequest();
        ftar.getHeader().setMessageType(MessageTypeEnum.MT_0220.getValue());
        if (messageVO.getStan() != null) {
            ftar.setStan(messageVO.getStan());
        } else {
            String stan = transactionDAO.getNextRRNSequence();
            ftar.setStan(stan);
        }

        ftar.setPan(messageVO.getPAN());
        ftar.setProcessingCode(TransactionCodeEnum.JS_FUND_TRANSFER_ADVICE.getValue());
        ftar.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
        ftar.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));

        // Request Time will come from i8
        if (messageVO.getRequestTime() == null) {
            messageVO.setRequestTime(new Date());
        }

        if (messageVO.getTimeLocalTransaction() == null) {
            messageVO.setTimeLocalTransaction(new Date());
        }

        if (messageVO.getDateLocalTransaction() == null) {
            messageVO.setDateLocalTransaction(new Date());
        }


        ftar.setTransactionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
        ftar.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());

        //Requested by Kashif Bashir
        ftar.setTransactionLocalTime(DateTools.dateToString(messageVO.getTimeLocalTransaction(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ftar.setTransactionLocalDate(DateTools.dateToString(messageVO.getDateLocalTransaction(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        //End

        ftar.setSettlementDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ftar.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        ftar.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
        ftar.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
        ftar.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
        ftar.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());

        // RRN will come from
        if (StringUtils.isNotEmpty(messageVO.getRetrievalReferenceNumber())) {
            ftar.setRrn(messageVO.getRetrievalReferenceNumber());
            ;
        } else {
            ftar.setRrn(FieldUtil.buildRRN(ftar.getStan()));
        }

        ftar.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
        ftar.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        ftar.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        ftar.setAccountNo1(messageVO.getAccountNo1());
        ftar.setAccountNo2(messageVO.getAccountNo2());

        String rrnKey = this.getVersionClass(ftar.getHeader().getMessageType()) + ftar.getTransactionDate() + ftar.getStan();

        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(ftar.getRrn());
        return ftar;
    }

    public AcquirerReversalAdviceRequest buildAcquirerReversalAdviceRequest(MiddlewareMessageVO messageVO) {
        AcquirerReversalAdviceRequest reversalAdvice = new AcquirerReversalAdviceRequest();
        reversalAdvice.getHeader().setMessageType(MessageTypeEnum.MT_0420.getValue());


        if (messageVO.getStan() != null) {
            reversalAdvice.setStan(messageVO.getStan());
        } else {
            String stan = transactionDAO.getNextRRNSequence();
            reversalAdvice.setStan(stan);
        }

        reversalAdvice.setPan(messageVO.getPAN());
        reversalAdvice.setProcessingCode(TransactionCodeEnum.JS_ACQUIRER_REVERSAL_ADVICE.getValue());
        reversalAdvice.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));

        // Request Time will come from i8
        if (messageVO.getRequestTime() == null) {
            messageVO.setRequestTime(new Date());
        }

        if (messageVO.getTimeLocalTransaction() == null) {
            messageVO.setTimeLocalTransaction(new Date());
        }

        if (messageVO.getDateLocalTransaction() == null) {
            messageVO.setDateLocalTransaction(new Date());
        }

        reversalAdvice.setTransactionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
        reversalAdvice.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());

        //Requested by Kashif Bashir
        reversalAdvice.setTransactionLocalTime(DateTools.dateToString(messageVO.getTimeLocalTransaction(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        reversalAdvice.setTransactionLocalDate(DateTools.dateToString(messageVO.getDateLocalTransaction(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        //End

        reversalAdvice.setSettlementDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        reversalAdvice.setConversionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        reversalAdvice.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        reversalAdvice.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
        reversalAdvice.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
        reversalAdvice.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
        reversalAdvice.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());

        // RRN will come from
        if (StringUtils.isNotEmpty(messageVO.getRetrievalReferenceNumber())) {
            reversalAdvice.setRrn(messageVO.getRetrievalReferenceNumber());
            ;
        } else {
            reversalAdvice.setRrn(FieldUtil.buildRRN(reversalAdvice.getStan()));
        }

        reversalAdvice.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
        reversalAdvice.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        reversalAdvice.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());

        String originalMTI = MessageTypeEnum.MT_0200.getValue();
        String originalAcquiring = "00000000000";
        String originalForwarding = "00000000000";

        String originalDataElements = originalMTI + messageVO.getReversalSTAN() + messageVO.getReversalRequestTime() + originalAcquiring + originalForwarding;

        reversalAdvice.setOriginalDataElement(originalDataElements);

        String rrnKey = this.getVersionClass(reversalAdvice.getHeader().getMessageType()) + reversalAdvice.getTransactionDate() + reversalAdvice.getStan();

        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(reversalAdvice.getRrn());

        return reversalAdvice;
    }

    public IbftTitleFetchRequest buildIbftTitleFetchRequest(MiddlewareMessageVO messageVO) {
        IbftTitleFetchRequest ibftTitleFetchRequest = new IbftTitleFetchRequest();
        ibftTitleFetchRequest.getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());
        String recordData = null;
        String accNo = null;
        String stan = transactionDAO.getNextRRNSequence();

        ibftTitleFetchRequest.setPan(messageVO.getCnicNo() + stan);
        ibftTitleFetchRequest.setProcessingCode(TransactionCodeEnum.JS_IBFT_TITLE_FETCH.getValue());

        ibftTitleFetchRequest.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
        ibftTitleFetchRequest.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));

        // Request Time will come from i8
        if (messageVO.getRequestTime() == null) {
            messageVO.setRequestTime(new Date());
        }

        ibftTitleFetchRequest.setTransactionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
        ibftTitleFetchRequest.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
        ibftTitleFetchRequest.setStan(stan);
        ibftTitleFetchRequest.setTransactionLocalTime(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ibftTitleFetchRequest.setTransactionLocalDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ibftTitleFetchRequest.setConversionDate(DateTools.dateToString(messageVO.getRequestTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ibftTitleFetchRequest.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        ibftTitleFetchRequest.setRrn(FieldUtil.buildRRN(ibftTitleFetchRequest.getStan()));
        ibftTitleFetchRequest.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        ibftTitleFetchRequest.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        ibftTitleFetchRequest.setAccountNo1(messageVO.getAccountNo1());
        ibftTitleFetchRequest.setAccountNo2(messageVO.getAccountNo2());
        ibftTitleFetchRequest.setPurposeOfPayment(messageVO.getPurposeOfPayment());
        String drOrCr = StringUtils.rightPad("C", 1);

        if (messageVO.getAccountNo2().length() > 20) {
            String accountIban=StringUtils.substring(messageVO.getAccountNo2(),0,20);
            accNo = StringUtils.left(accountIban, 20);
        } else {
            accNo = StringUtils.rightPad(messageVO.getAccountNo2(), 20);
        }

        String toBankIMD = StringUtils.rightPad(messageVO.getBankIMD(), 11, "0");
        String fromBankIMD = StringUtils.rightPad(messageVO.getSenderBankImd(), 11, "0");
        String benificieryIBAN = StringUtils.rightPad("", 24);
        String senderName = StringUtils.rightPad("", 30);
        String senderIBAN = StringUtils.rightPad("", 24);
        String bankName = StringUtils.rightPad("", 20);
        String branchName = StringUtils.rightPad("", 25);
        String accountTitle = StringUtils.rightPad("", 30);
        recordData = accNo
                + accountTitle
                + fromBankIMD
                + toBankIMD
                + drOrCr
                + bankName
                + branchName
                + senderName
                + senderIBAN
                + benificieryIBAN;
        ibftTitleFetchRequest.setRecordData(recordData);
        String rrnKey = this.getVersionClass(ibftTitleFetchRequest.getHeader().getMessageType()) + ibftTitleFetchRequest.getTransactionDate() + stan;
        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(ibftTitleFetchRequest.getRrn());

        return ibftTitleFetchRequest;
    }


    public IbftAdviceRequest buildIbftAdviceRequest(MiddlewareMessageVO messageVO) {
        IbftAdviceRequest ibftAdviceRequest = new IbftAdviceRequest();
        String recordData = null;
        String accNo = null;
        String benificieryIBAN = null;
        ibftAdviceRequest.getHeader().setMessageType(MessageTypeEnum.MT_0220.getValue());
        if (messageVO.getStan() != null) {
            ibftAdviceRequest.setStan(messageVO.getStan());
        } else {
            String stan = transactionDAO.getNextRRNSequence();
            ibftAdviceRequest.setStan(stan);
        }

        ibftAdviceRequest.setPan(messageVO.getCnicNo() + messageVO.getStan());
        ibftAdviceRequest.setProcessingCode(TransactionCodeEnum.JS_IBFT_ADVICE.getValue());

        ibftAdviceRequest.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
        ibftAdviceRequest.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));

        // Request Time will come from i8
        if (messageVO.getTransmissionTime() == null) {
            messageVO.setTransmissionTime(new Date());
        }
        ibftAdviceRequest.setTransactionDate(DateTools.dateToString(messageVO.getTransmissionTime(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
//        ibftAdviceRequest.setSettlementAmount(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());

        ibftAdviceRequest.setTransactionLocalTime(DateTools.dateToString(messageVO.getTransmissionTime(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ibftAdviceRequest.setTransactionLocalDate(DateTools.dateToString(messageVO.getTransmissionTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ibftAdviceRequest.setConversionDate(DateTools.dateToString(messageVO.getTransmissionTime(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        ibftAdviceRequest.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
        ibftAdviceRequest.setRrn(FieldUtil.buildRRN(ibftAdviceRequest.getStan()));
        ibftAdviceRequest.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        ibftAdviceRequest.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
        ibftAdviceRequest.setAccountNo1(messageVO.getAccountNo1());
        ibftAdviceRequest.setAccountNo2((messageVO.getAccountNo2()));
        ibftAdviceRequest.setPurposeOfPayment(messageVO.getPurposeOfPayment());
        ibftAdviceRequest.setTerminalId(messageVO.getAgentId());
        ibftAdviceRequest.setCardAcceptorNameAndLocation("JS BRANCHLESS BANK  AGENT             PK");
        String rrnKey = this.getVersionClass(ibftAdviceRequest.getHeader().getMessageType()) + ibftAdviceRequest.getTransactionDate() + ibftAdviceRequest.getStan();
        messageVO.setRrnKey(rrnKey);
        messageVO.setRetrievalReferenceNumber(ibftAdviceRequest.getRrn());
        if (messageVO.getAccountNo2().length() > 20) {
            String accountIban=StringUtils.substring(messageVO.getAccountNo2(),0,20);
            accNo = StringUtils.left(accountIban, 20);
        } else {
            accNo = StringUtils.rightPad(messageVO.getAccountNo2(), 20);
        }


        String accountTitle = StringUtils.rightPad(messageVO.getAccountTitle(), 30);
        String toBankIMD = StringUtils.rightPad(messageVO.getBankIMD(), 11, "0");
        String fromBankIMD = StringUtils.rightPad(messageVO.getSenderBankImd(), 11, "0");
        String drOrCr = StringUtils.rightPad("C", 1);
        String bankName = StringUtils.rightPad(messageVO.getAccountBankName(), 20);
        String branchName = StringUtils.rightPad(messageVO.getAccountBranchName(), 25);

        String senderName = StringUtils.rightPad(messageVO.getSenderName(), 30);
        String senderIBAN = StringUtils.rightPad(messageVO.getSenderIban(), 24);

        if (messageVO.getBenificieryIban() == null || messageVO.getBenificieryIban().equals("null")) {
            benificieryIBAN = StringUtils.rightPad("", 24);
        } else {
            benificieryIBAN = StringUtils.rightPad(messageVO.getBenificieryIban(), 24);
        }
        recordData = accNo
                + accountTitle
                + fromBankIMD
                + toBankIMD
                + drOrCr
                + bankName
                + branchName
                + senderName
                + senderIBAN
                + benificieryIBAN;

        ibftAdviceRequest.setRecordData(recordData);
        return ibftAdviceRequest;
    }

    public String getVersionClass(String str) {
        return str.substring(0, 2);
    }
}

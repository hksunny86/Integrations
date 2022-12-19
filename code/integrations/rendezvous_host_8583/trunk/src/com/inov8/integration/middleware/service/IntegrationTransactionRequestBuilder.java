package com.inov8.integration.middleware.service;

import java.util.Date;

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
		accountBalanceInquiry.setTransactionDate(DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		accountBalanceInquiry.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		accountBalanceInquiry.setStan(stan);
		accountBalanceInquiry.setTransactionLocalTime(DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		accountBalanceInquiry.setTransactionLocalDate(DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		accountBalanceInquiry.setSettlementDate(DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		accountBalanceInquiry.setMerchantType("0008");
		accountBalanceInquiry.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		accountBalanceInquiry.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
		accountBalanceInquiry.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
		accountBalanceInquiry.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
		
		accountBalanceInquiry.setRrn(FieldUtil.buildRRN());
		accountBalanceInquiry.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
		accountBalanceInquiry.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
		accountBalanceInquiry.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		accountBalanceInquiry.setAccountNo1(messageVO.getAccountNo1());
		
		String rrnKey = FieldUtil.getDateYYYYMMDD() + stan;
		messageVO.setRetrievalReferenceNumber(rrnKey);
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
		bir.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		bir.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		bir.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		bir.setStan(stan);
		bir.setTransactionLocalTime(DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		bir.setTransactionLocalDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		bir.setSettlementDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		bir.setConversionDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		bir.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		bir.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
		bir.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
		bir.setAcquirerIdentification(JSBankDefaultsEnum.BILL_INQUIRY_ACQUIRER_IDENTIFICATION.getValue());
		bir.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
		bir.setRrn(FieldUtil.buildRRN());
		bir.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
		bir.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		bir.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		bir.setAccountNo1(messageVO.getAccountNo1());

		String privateDate = messageVO.getCnicNo() + MiddlewareEnum.JS_DELIMITER.getValue() + messageVO.getConsumerNo()
				+ MiddlewareEnum.JS_DELIMITER.getValue() + messageVO.getCompnayCode() + MiddlewareEnum.JS_DELIMITER.getValue();

		bir.setPrivateData(privateDate);

		String rrnKey = FieldUtil.getDateYYYYMMDD() + stan;
		messageVO.setRetrievalReferenceNumber(rrnKey);
		return bir;
	}
	
	
	public TitleFetchRequest buildTitleFetchRequest(MiddlewareMessageVO messageVO) {
		TitleFetchRequest tfr = new TitleFetchRequest();
		tfr.getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());

		String stan = transactionDAO.getNextRRNSequence();

		tfr.setPan(messageVO.getPAN());
		tfr.setProcessingCode(TransactionCodeEnum.JS_TITLE_FETCH.getValue());
		tfr.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
		tfr.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		tfr.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		tfr.setStan(stan);
		tfr.setTransactionLocalTime(DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		tfr.setTransactionLocalDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		tfr.setSettlementDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		tfr.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		tfr.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
		tfr.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
		tfr.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
		tfr.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
		tfr.setRrn(FieldUtil.buildRRN());
		tfr.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
		tfr.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		tfr.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		tfr.setAccountNo1(messageVO.getAccountNo1());

		
		String rrnKey = FieldUtil.getDateYYYYMMDD() + stan;
		messageVO.setRetrievalReferenceNumber(rrnKey);
		
		return tfr;
	}
	
	public BillPaymentRequest buildBillPaymentRequest(MiddlewareMessageVO messageVO) {
		BillPaymentRequest bpr = new BillPaymentRequest();
		bpr.getHeader().setMessageType(MessageTypeEnum.MT_0220.getValue());

		String stan = transactionDAO.getNextRRNSequence();

		bpr.setPan(messageVO.getPAN());
		bpr.setProcessingCode(TransactionCodeEnum.JS_BILL_PAYMENT.getValue());
		bpr.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
		bpr.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));
		bpr.setTransactionDate(DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		bpr.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		bpr.setStan(stan);
		bpr.setTransactionLocalTime(DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		bpr.setTransactionLocalDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		bpr.setSettlementDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		bpr.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		bpr.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
		bpr.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
		bpr.setSettlementProcessingFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetProcessingFee()));
		bpr.setAcquirerIdentification(JSBankDefaultsEnum.BILL_INQUIRY_ACQUIRER_IDENTIFICATION.getValue());
		bpr.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
		bpr.setRrn(FieldUtil.buildRRN());
		bpr.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
		bpr.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		bpr.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		bpr.setAccountNo1(messageVO.getAccountNo1());

		String cnicNo = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getCnicNo())) ? " " : messageVO.getCnicNo();
		String consumerNo = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getConsumerNo())) ? " " : messageVO.getConsumerNo();
		String consumerName = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getConsumerName())) ? " " : messageVO.getConsumerName();
		String companyCode = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getCompnayCode())) ? " " : messageVO.getCompnayCode();
		String dueDateStr = DateTools.dateToString(messageVO.getBillDueDate(), "yyMMdd");
		String dueDate = StringUtils.isBlank(dueDateStr) ? " " : dueDateStr;
		String aggregatorCode = StringUtils.isBlank(StringUtils.trimToEmpty(messageVO.getBillAggregator())) ? " " : messageVO.getBillAggregator();

		String privateDate = cnicNo + MiddlewareEnum.JS_DELIMITER.getValue() + consumerNo + MiddlewareEnum.JS_DELIMITER.getValue() + consumerName
				+ MiddlewareEnum.JS_DELIMITER.getValue() + companyCode + MiddlewareEnum.JS_DELIMITER.getValue() + dueDate
				+ MiddlewareEnum.JS_DELIMITER.getValue() + aggregatorCode
				+ MiddlewareEnum.JS_DELIMITER.getValue() + messageVO.getBillCategoryId() + MiddlewareEnum.JS_DELIMITER.getValue();

		bpr.setPrivateData(privateDate);

		String rrnKey = FieldUtil.getDateYYYYMMDD() + stan;
		messageVO.setRetrievalReferenceNumber(rrnKey);
		return bpr;
	}
	
	public FundTransferRequest buildFundTransferRequest(MiddlewareMessageVO messageVO) {
		FundTransferRequest ftr = new FundTransferRequest();
		ftr.getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());

		String stan = transactionDAO.getNextRRNSequence();

		ftr.setPan(messageVO.getPAN());
		ftr.setProcessingCode(TransactionCodeEnum.JS_FUND_TRANSFER.getValue());

		ftr.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
		ftr.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));
		ftr.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		ftr.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		ftr.setStan(stan);
		ftr.setTransactionLocalTime(DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ftr.setTransactionLocalDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ftr.setSettlementDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ftr.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		ftr.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
		ftr.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
		ftr.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
		ftr.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
		ftr.setRrn(FieldUtil.buildRRN());
		ftr.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
		ftr.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		ftr.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		ftr.setAccountNo1(messageVO.getAccountNo1());
		ftr.setAccountNo2(messageVO.getAccountNo2());

		String rrnKey = FieldUtil.getDateYYYYMMDD() + stan;
		messageVO.setRetrievalReferenceNumber(rrnKey);
		return ftr;
	}
	
	public FundTransferAdviceRequest buildFundTransferAdviceRequest(MiddlewareMessageVO messageVO) {
		FundTransferAdviceRequest ftar = new FundTransferAdviceRequest();
		ftar.getHeader().setMessageType(MessageTypeEnum.MT_0220.getValue());

		String stan = transactionDAO.getNextRRNSequence();

		ftar.setPan(messageVO.getPAN());
		ftar.setProcessingCode(TransactionCodeEnum.JS_FUND_TRANSFER_ADVICE.getValue());

		ftar.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
		ftar.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));
		ftar.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		ftar.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		ftar.setStan(stan);
		ftar.setTransactionLocalTime(DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ftar.setTransactionLocalDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ftar.setSettlementDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ftar.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		ftar.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
		ftar.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
		ftar.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
		ftar.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
		ftar.setRrn(FieldUtil.buildRRN());
		ftar.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
		ftar.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		ftar.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		ftar.setAccountNo1(messageVO.getAccountNo1());
		ftar.setAccountNo2(messageVO.getAccountNo2());

		String rrnKey = FieldUtil.getDateYYYYMMDD() + stan;
		messageVO.setRetrievalReferenceNumber(rrnKey);
		return ftar;
	}

	public CoreToWalletRequest buildCoreToWalletRequest(MiddlewareMessageVO messageVO) {
		CoreToWalletRequest ctwr = new CoreToWalletRequest();
		ctwr.getHeader().setMessageType(MessageTypeEnum.MT_0220.getValue());

		String stan = transactionDAO.getNextRRNSequence();

		ctwr.setPan(messageVO.getPAN());
		ctwr.setProcessingCode(TransactionCodeEnum.JS_CORE_TO_WALLET_ADVICE.getValue());

		ctwr.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
		ctwr.setSettlementAmount(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetAmount()));
		ctwr.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		ctwr.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		ctwr.setStan(stan);
		ctwr.setTransactionLocalTime(DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ctwr.setTransactionLocalDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ctwr.setSettlementDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		ctwr.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		ctwr.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
		ctwr.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
		ctwr.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
		ctwr.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
		ctwr.setRrn(FieldUtil.buildRRN());
		ctwr.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
		ctwr.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		ctwr.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		ctwr.setAccountNo1(messageVO.getAccountNo1());
		ctwr.setAccountNo2(messageVO.getAccountNo2());

		String rrnKey = FieldUtil.getDateYYYYMMDD() + stan;
		messageVO.setRetrievalReferenceNumber(rrnKey);
		return ctwr;
	}
	
	public AcquirerReversalAdviceRequest buildAcquirerReversalAdviceRequest(MiddlewareMessageVO messageVO) {
		AcquirerReversalAdviceRequest reversalAdvice = new AcquirerReversalAdviceRequest();
		reversalAdvice.getHeader().setMessageType(MessageTypeEnum.MT_0420.getValue());

		String stan = transactionDAO.getNextRRNSequence();

		reversalAdvice.setPan(messageVO.getPAN());
		reversalAdvice.setProcessingCode(TransactionCodeEnum.JS_ACQUIRER_REVERSAL_ADVICE.getValue());
		reversalAdvice.setTransactionAmount(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionAmount()));
		reversalAdvice.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		reversalAdvice.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		reversalAdvice.setStan(stan);
		reversalAdvice.setTransactionLocalTime(DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		reversalAdvice.setTransactionLocalDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		reversalAdvice.setSettlementDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		reversalAdvice.setConversionDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		reversalAdvice.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		reversalAdvice.setTransactionFee(ISO8583Utils.formatISO8583Amount(messageVO.getTransactionFee()));
		reversalAdvice.setSettlementFee(ISO8583Utils.formatISO8583Amount(messageVO.getSettlemetFee()));
		reversalAdvice.setAcquirerIdentification(JSBankDefaultsEnum.ACQUIRER_IDENTIFICATION.getValue());
		reversalAdvice.setTerminalId(JSBankDefaultsEnum.TERMINAL_ID.getValue());
		reversalAdvice.setRrn(FieldUtil.buildRRN());
		reversalAdvice.setCardAcceptorName(JSBankDefaultsEnum.CARD_ACCEPTOR_NAME.getValue());
		reversalAdvice.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		reversalAdvice.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());

		String originalMTI = MessageTypeEnum.MT_0200.getValue();
		String originalAcquiring = "00000000000";
		String originalForwarding = "00000000000";

		String originalDataElements = originalMTI + messageVO.getReversalSTAN() + messageVO.getReversalRequestTime() + originalAcquiring + originalForwarding;

		reversalAdvice.setOriginalDataElement(originalDataElements);

		String rrnKey = FieldUtil.getDateYYYYMMDD() + stan;
		messageVO.setRetrievalReferenceNumber(rrnKey);
		return reversalAdvice;
	}
}

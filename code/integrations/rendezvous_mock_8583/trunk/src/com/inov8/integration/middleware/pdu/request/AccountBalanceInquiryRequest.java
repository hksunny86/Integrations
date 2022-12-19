package com.inov8.integration.middleware.pdu.request;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACCOUNCT_NO_1;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACQUIRER_IDENTIFICATION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.CARD_ACCEPTOR_NAME_AND_LOCATION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.DATE_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.MERCHANT_TYPE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.NETWORK_IDENTIFIER;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PAN;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PROCESSING_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.RRN;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_AMOUNT_FEE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_CONVERSION_RATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_CURRENCY_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_DATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SYSTEMS_TRACE_NO;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TERMINAL_ID;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TIME_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_AMOUNT_FEE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_DATE;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import com.inov8.integration.middleware.enums.MessageTypeEnum;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;

public class AccountBalanceInquiryRequest extends BasePDU {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2870497217224562649L;

	public AccountBalanceInquiryRequest() {
		if (getHeader() == null) {
			setHeader(new BaseHeader());
			getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());
		}
	}

	public void assemble() {
		byte[] header = null;
		byte[] body = null;
		try {
			header = getHeader().build().getBytes();

			GenericPackager packager = ISO8583Utils.getISOPackager();
			ISOMsg msg = packager.createISOMsg();
			msg.setPackager(packager);
			
			// @formatter:off
			msg.setMTI(getHeader().getMessageType());
			msg.set(PAN.getValue(), trimToEmpty(getPan()));
			msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
			msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));
			msg.set(SETTLEMENT_AMOUNT.getValue(), trimToEmpty(getSettlementAmount()));
			msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
			msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), trimToEmpty(getSettlementConversionRate()));
			msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
			msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
			msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
			msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));
			msg.set(MERCHANT_TYPE.getValue(), trimToEmpty(getMerchantType()));
			msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
			msg.set(TRANSACTION_AMOUNT_FEE.getValue(), trimToEmpty(getTransactionFee()));
			msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), trimToEmpty(getSettlementFee()));
			msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));
			msg.set(RRN.getValue(), trimToEmpty(getRrn()));
			msg.set(TERMINAL_ID.getValue(), trimToEmpty(getTerminalId()));
			msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), trimToEmpty(getCardAcceptorName()));
			msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), trimToEmpty(getSettlementCurrencyCode()));
//			msg.set(PIN_DATA.getValue(), trimToEmpty(getPin()));
			msg.set(ACCOUNCT_NO_1.getValue(), trimToEmpty(getAccountNo1()));
			
			// @formatter:on

			body = msg.pack();
			logISOMessage(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		super.setRawPdu(ArrayUtils.addAll(header, body));
	}
}

package com.inov8.integration.middleware.pdu.response;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACQUIRER_IDENTIFICATION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.DATE_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.NETWORK_IDENTIFIER;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PAN;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PROCESSING_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.RESPONSE_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_DATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SYSTEMS_TRACE_NO;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TIME_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_AMOUNT_FEE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_DATE;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import com.inov8.integration.middleware.enums.ISO8583FieldEnum;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;

public class AcquirerReversalAdviceResponse extends BasePDU {

	public AcquirerReversalAdviceResponse() {
		setHeader(new BaseHeader());
	}

	private static final long serialVersionUID = 5575613866373922973L;

	public void build() {
		byte[] header = null;
		byte[] body = null;
		try {
			header = getHeader().build().getBytes();

			GenericPackager packager = ISO8583Utils.getISOPackager();
			ISOMsg msg = packager.createISOMsg();
			msg.setPackager(packager);
			msg.setMTI("0430");
			msg.set(PAN.getValue(), trimToEmpty(getPan()));
			msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
			msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));
			msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
			msg.set(SETTLEMENT_AMOUNT.getValue(), trimToEmpty(getSettlementAmount()));
			msg.set(ISO8583FieldEnum.SETTLEMENT_CONVERSION_RATE.getValue(), trimToEmpty(getSettlementConversionRate()));
			msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
			msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
			msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
			msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));
			msg.set(ISO8583FieldEnum.CONVERSION_DATE.getValue(), trimToEmpty(getConversionDate()));
			msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
			msg.set(TRANSACTION_AMOUNT_FEE.getValue(), trimToEmpty(getTransactionFee()));
			msg.set(ISO8583FieldEnum.SETTLEMENT_AMOUNT_FEE.getValue(), trimToEmpty(getSettlementFee()));
			msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));
			msg.set(ISO8583FieldEnum.RRN.getValue(), trimToEmpty(getRrn()));
			msg.set(ISO8583FieldEnum.TERMINAL_ID.getValue(), trimToEmpty(getTerminalId()));
			msg.set(ISO8583FieldEnum.CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), trimToEmpty(getCardAcceptorName()));
			msg.set(ISO8583FieldEnum.TRANSACTION_CURRENCY_CODE.getValue(), trimToEmpty(getTransactionCurrencyCode()));
			msg.set(ISO8583FieldEnum.SETTLEMENT_CURRENCY_CODE.getValue(), trimToEmpty(getSettlementCurrencyCode()));
			msg.set(ISO8583FieldEnum.PRIVATE_DATA.getValue(), trimToEmpty(getPrivateData()));
			msg.set(RESPONSE_CODE.getValue(), trimToEmpty(getResponseCode()));

			body = msg.pack();
			logISOMessage(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		super.setRawPdu(ArrayUtils.addAll(header, body));
	}

}

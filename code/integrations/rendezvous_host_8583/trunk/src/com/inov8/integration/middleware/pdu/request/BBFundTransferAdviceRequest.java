package com.inov8.integration.middleware.pdu.request;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACCOUNCT_NO_1;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACCOUNCT_NO_2;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.CONVERSION_DATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.DATE_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.NETWORK_IDENTIFIER;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PAN;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PROCESSING_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.RRN;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_CONVERSION_RATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_CURRENCY_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_DATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SYSTEMS_TRACE_NO;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TIME_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_CURRENCY_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_DATE;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;

public class BBFundTransferAdviceRequest extends BasePDU {

	private static final long serialVersionUID = -5225569947544102210L;

	public BBFundTransferAdviceRequest(){
		if(getHeader() == null){
			this.setHeader(new BaseHeader());
			this.getHeader().setMessageType("0220");
			this.setProcessingCode(TransactionCodeEnum.JS_BB_ACCOUNT_FT_ADVICE.getValue());
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
			msg.set(CONVERSION_DATE.getValue(), trimToEmpty(getConversionDate()));
			msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
			msg.set(RRN.getValue(), trimToEmpty(getRrn()));
			msg.set(TRANSACTION_CURRENCY_CODE.getValue(), trimToEmpty(getTransactionCurrencyCode()));
			msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), trimToEmpty(getSettlementCurrencyCode()));
			msg.set(ACCOUNCT_NO_1.getValue(), trimToEmpty(getAccountNo1()));
			msg.set(ACCOUNCT_NO_2.getValue(), trimToEmpty(getAccountNo2()));
			
			body = msg.pack();
			logISOMessage(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		super.setRawPdu(ArrayUtils.addAll(header, body));
	}
}

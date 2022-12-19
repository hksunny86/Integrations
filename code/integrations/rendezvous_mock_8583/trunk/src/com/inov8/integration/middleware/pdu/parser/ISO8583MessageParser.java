package com.inov8.integration.middleware.pdu.parser;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACCOUNCT_NO_1;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACCOUNCT_NO_2;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACQUIRER_IDENTIFICATION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ADDITIONAL_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ADDITIONAL_RESPONSE_DATA;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.AUTH_ID_RESPONSE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.CARD_ACCEPTOR_NAME_AND_LOCATION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.CONVERSION_DATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.DATE_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.MESSAGE_SECURITY_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.NETWORK_IDENTIFIER;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.NETWORK_MANAGEMENT_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PAN;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PIN_DATA;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PRIVATE_DATA;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PROCESSING_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.RESPONSE_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.RRN;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_AMOUNT_FEE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_AMOUNT_PROCESSING_FEE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_CONVERSION_RATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_CURRENCY_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SETTLEMENT_DATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SYSTEMS_TRACE_NO;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TERMINAL_ID;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TIME_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_AMOUNT_FEE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_CURRENCY_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_DATE;

import java.util.BitSet;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;

public class ISO8583MessageParser {
	private static Logger logger = LoggerFactory.getLogger(ISO8583MessageParser.class.getSimpleName());
	
	public static void main(String[] args) throws DecoderException {
//		String hexString = "52454e44455a564f55532d38353833312e31303030323031343038313930353532323030323030efbfbdefbfbd411908efbfbdefbfbd000000000004000000393330303030303030303030303030303030303831393035353232303030303030303030303231313230303535323230303831393038313930303038524456303030303030303030303030303030303030303030303030303630303030303031343038313930353532323030303030303030304a53424c204272616e63686c6573732042616e6b696e67204368616e6e656c2050616b697374616e353836353836303131";
//		byte[] bytes = Hex.decodeHex(hexString.toCharArray());
//		parse(new String(bytes));
	}
	
	public static BasePDU parse(byte[] pduMessage) {
		try {
			
			byte[] pduHeaderBytes = ArrayUtils.subarray(pduMessage, 0, 35);
			byte[] pduBodyBytes = ArrayUtils.subarray(pduMessage, 35, pduMessage.length);
			BasePDU basePDU = new BasePDU();
			basePDU.setHeader(parseHeader(new String (pduHeaderBytes)));
			String temp = new String (pduBodyBytes);

			
			GenericPackager packager = ISO8583Utils.getISOPackager();
			ISOMsg isoMsg = packager.createISOMsg();
			isoMsg.setPackager(packager);
			isoMsg.unpack(pduBodyBytes);

			basePDU.getHeader().setMessageType(isoMsg.getMTI());

			logISOMessage(isoMsg);

			for (int i = 1; i <= isoMsg.getMaxField(); i++) {
				if (isoMsg.hasField(i)) {

					if (PAN.getValue() == i) {
						basePDU.setPan(isoMsg.getString(i));
					}
					if (PROCESSING_CODE.getValue() == i) {
						basePDU.setProcessingCode(isoMsg.getString(i));
					}
					if (TRANSACTION_AMOUNT.getValue() == i) {
						basePDU.setTransactionAmount(isoMsg.getString(i));
					}
					if (SETTLEMENT_AMOUNT.getValue() == i) {
						basePDU.setSettlementAmount(isoMsg.getString(i));
					}
					if (TRANSACTION_DATE.getValue() == i) {
						basePDU.setTransactionDate(isoMsg.getString(i));
					}
					if (SETTLEMENT_CONVERSION_RATE.getValue() == i) {
						basePDU.setSettlementConversionRate(isoMsg.getString(i));
					}
					if (SYSTEMS_TRACE_NO.getValue() == i) {
						basePDU.setStan(isoMsg.getString(i));
					}
					if (TIME_LOCAL_TRANSACTION.getValue() == i) {
						basePDU.setTransactionLocalTime(isoMsg.getString(i));
					}
					if (DATE_LOCAL_TRANSACTION.getValue() == i) {
						basePDU.setTransactionLocalDate(isoMsg.getString(i));
					}
					if (SETTLEMENT_DATE.getValue() == i) {
						basePDU.setSettlementDate(isoMsg.getString(i));
					}
					if (CONVERSION_DATE.getValue() == i) {
						basePDU.setConversionDate(isoMsg.getString(i));
					}
					if (NETWORK_IDENTIFIER.getValue() == i) {
						basePDU.setNetworkIdentifier(isoMsg.getString(i));
					}
					if (TRANSACTION_AMOUNT_FEE.getValue() == i) {
						basePDU.setTransactionFee(isoMsg.getString(i));
					}
					if (SETTLEMENT_AMOUNT_FEE.getValue() == i) {
						basePDU.setSettlementFee(isoMsg.getString(i));
					}
					if (SETTLEMENT_AMOUNT_PROCESSING_FEE.getValue() == i) {
						basePDU.setSettlementProcessingFee(isoMsg.getString(i));
					}
					if (ACQUIRER_IDENTIFICATION.getValue() == i) {
						basePDU.setAcquirerIdentification(isoMsg.getString(i));
					}
					if (RRN.getValue() == i) {
						basePDU.setRrn(isoMsg.getString(i));
					}
					if (AUTH_ID_RESPONSE.getValue() == i) {
						basePDU.setAuthIdResponse(isoMsg.getString(i));
					}
					if (RESPONSE_CODE.getValue() == i) {
						basePDU.setResponseCode(isoMsg.getString(i));
					}
					if (TERMINAL_ID.getValue() == i) {
						basePDU.setTerminalId(isoMsg.getString(i));
					}
					if (CARD_ACCEPTOR_NAME_AND_LOCATION.getValue() == i) {
						basePDU.setCardAcceptorName(isoMsg.getString(i));
					}
					if (ADDITIONAL_RESPONSE_DATA.getValue() == i) {
						basePDU.setAdditionalResponseData(isoMsg.getString(i));
					}
					if (TRANSACTION_CURRENCY_CODE.getValue() == i) {
						basePDU.setTransactionCurrencyCode(isoMsg.getString(i));
					}
					if (SETTLEMENT_CURRENCY_CODE.getValue() == i) {
						basePDU.setSettlementCurrencyCode(isoMsg.getString(i));
					}
					if (PIN_DATA.getValue() == i) {
						basePDU.setPin(isoMsg.getString(i));
					}
					if (ADDITIONAL_AMOUNT.getValue() == i) {
						basePDU.setAdditionalAmount(isoMsg.getString(i));
					}
					if (NETWORK_MANAGEMENT_CODE.getValue() == i) {
						basePDU.setNetworkManagementCode(isoMsg.getString(i));
					}
					if (MESSAGE_SECURITY_CODE.getValue() == i) {
						basePDU.setMessageSecurityCode(isoMsg.getString(i));
					}
					if (ACCOUNCT_NO_1.getValue() == i) {
						basePDU.setAccountNo1(isoMsg.getString(i));
					}
					if (ACCOUNCT_NO_2.getValue() == i) {
						basePDU.setAccountNo2(isoMsg.getString(i));
					}
					if (PRIVATE_DATA.getValue() == i) {
						basePDU.setPrivateData(isoMsg.getString(i));
					}

				}
			}
			return basePDU;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	private  static void logISOMessage(ISOMsg msg) {
		StringBuilder log = new StringBuilder();
		log.append("\n----ISO MESSAGE-----");
		log.append("\n");
		try {
			BitSet bset = (BitSet) msg.getValue(-1);
			log.append("BitMap : " + org.jpos.iso.ISOUtil.bitSet2String(bset));
			log.append("\n");
			log.append(" MTI : " + msg.getMTI());
			log.append("\n");
			for (int i = 1; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					log.append("    DE-" + i + " : " + msg.getString(i));
					log.append("\n");
				}
			}
		} catch (Exception e) {
			logger.error("Exception",e);
		} finally {
			log.append("----ISO MESSAGE-----");
			log.append("\n");
		}
		logger.debug(log.toString());
	}

	public static BaseHeader parseHeader(String hexMessage) {
		BaseHeader header = new BaseHeader();

		return header;
	}

}

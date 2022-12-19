package com.inov8.test;

//@formatter:off
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACCOUNCT_NO_1;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACQUIRER_IDENTIFICATION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ADDITIONAL_AMOUNT;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ADDITIONAL_RESPONSE_DATA;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ADDITIONAL_RESPONSE_DATA_PRIVATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.AUTH_ID_RESPONSE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.CARD_ACCEPTOR_NAME_AND_LOCATION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.CONVERSION_DATE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.DATE_LOCAL_TRANSACTION;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.MERCHANT_TYPE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.NETWORK_IDENTIFIER;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PAN;
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
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.PIN_DATA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
//@formatter:on
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import com.inov8.integration.middleware.enums.MiddlewareEnum;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.middleware.util.FieldUtil;
import com.inov8.integration.middleware.util.ISO8583Utils;

public class UPaymentMessageGenrator {

	public static void main(String[] args) throws Exception {
	
		System.out.println("---- Bill Inquiry ----");
		System.out.println("HEX: " + billInquiryRequest());
		System.out.println("HEX: " + billInquiryResponse());
		System.out.println();
		System.out.println("---- Bill Payment ----");
		System.out.println("HEX: " + billPaymentRequest());
		System.out.println("HEX: " + billPaymentResponse());
		System.out.println();
		System.out.println("---- Title Fetch ----");
		System.out.println("HEX: " + titleFetchRequest());
		System.out.println("HEX: " + titleFetchResponse());
		System.out.println();
		System.out.println("---- Account Fund Transfer ----");
		System.out.println("HEX: " + accountFundTransferRequest());
		System.out.println("HEX: " + accountFundTransferResponse());
		System.out.println();
		System.out.println("---- Account Balance Inquiry ----");
		System.out.println("HEX: " + accountBalanceInquiryRequest());
		System.out.println("HEX: " + accountBalanceInquiryResponse());
		System.out.println();
		System.out.println("---- IBFT ----");
		System.out.println("HEX: " + IBFundTransferRequest());
		System.out.println("HEX: " + IBFundTransferResponse());
		System.out.println();
		System.out.println("---- Mini Statement ----");
		System.out.println("HEX: " + miniStatementRequest());
		System.out.println("HEX: " + miniStatementResponse());
		System.out.println();
		System.out.println("---- Customer Relationship Inquiry ----");
		System.out.println("HEX: " + customerRelationshipInquiryRequest());
		System.out.println("HEX: " + customerRelationshipInquiryResponse());
		System.out.println();
	}

	public static String billInquiryRequest() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0200");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "720000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(MERCHANT_TYPE.getValue(), "0008");
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RRN.getValue(), FieldUtil.buildRRN("000000"));
		msg.set(TERMINAL_ID.getValue(), "00000000");
		msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), "SendPesa Banking Channel Pakistan");
		msg.set(TRANSACTION_CURRENCY_CODE.getValue(), "586");
		msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), "586");
		msg.set(PRIVATE_DATA.getValue(), "188746583009|SNGPL001|");
		
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Bill Inquiry Request : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String billInquiryResponse() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0210");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "720000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RESPONSE_CODE.getValue(), "00");
		msg.set(ADDITIONAL_RESPONSE_DATA.getValue(), "188746583009|Rashid Siraj|SNGPL001|U|1410|141012|000000050000|000000054000|");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Bill Inquiry Response : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String billPaymentRequest() throws Exception {
		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0200");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "990000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000050000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(MERCHANT_TYPE.getValue(), "0008");
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_PROCESSING_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RRN.getValue(), FieldUtil.buildRRN("000000"));
		msg.set(TERMINAL_ID.getValue(), "00000000");
		msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), "SendPesa Banking Channel Pakistan");
		msg.set(TRANSACTION_CURRENCY_CODE.getValue(), "586");
		msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), "586");
		msg.set(ACCOUNCT_NO_1.getValue(), "541247533");
		msg.set(PRIVATE_DATA.getValue(), "188746583009|SNGPL001|141012|");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Bill Payment Request : " + new String(result));

		return Hex.encodeHexString(result);
	}

	public static String billPaymentResponse() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0210");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "990000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(AUTH_ID_RESPONSE.getValue(), "000001");
		msg.set(RESPONSE_CODE.getValue(), "00");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Bill Payment Response : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String titleFetchRequest() throws Exception {
		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0200");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "930000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(MERCHANT_TYPE.getValue(), "0008");
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RRN.getValue(), FieldUtil.buildRRN("000000"));
		msg.set(TERMINAL_ID.getValue(), "00000000");
		msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), "SendPesa Banking Channel Pakistan");
		msg.set(TRANSACTION_CURRENCY_CODE.getValue(), "586");
		msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), "586");
		msg.set(ACCOUNCT_NO_1.getValue(), "541247533");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Title Fetch Request : " + new String(result));

		return Hex.encodeHexString(result);
	}

	public static String titleFetchResponse() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0210");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "930000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(CONVERSION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RESPONSE_CODE.getValue(), "00");
		msg.set(ADDITIONAL_RESPONSE_DATA.getValue(), "Ijaz Nazir");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Title Fetch Response : " + new String(result));

		return Hex.encodeHexString(result);

	}
	
	public static String accountFundTransferRequest() throws Exception {
		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0200");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "950000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(MERCHANT_TYPE.getValue(), "0008");
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RRN.getValue(), FieldUtil.buildRRN("000000"));
		msg.set(TERMINAL_ID.getValue(), "00000000");
		msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), "SendPesa Banking Channel Pakistan");
		msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), "586");
		msg.set(ACCOUNCT_NO_1.getValue(), "541247533");
		msg.set(ACCOUNCT_NO_1.getValue(), "242362344");
		msg.set(PIN_DATA.getValue(), new byte[8]);
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Account Fund Transfer Request : " + new String(result));

		return Hex.encodeHexString(result);
	}

	public static String accountFundTransferResponse() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0210");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "950000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RESPONSE_CODE.getValue(), "00");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Account Fund Transfer Response : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String accountBalanceInquiryRequest() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0200");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "310000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(MERCHANT_TYPE.getValue(), "0008");
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RRN.getValue(), FieldUtil.buildRRN("000000"));
		msg.set(TERMINAL_ID.getValue(), "00000000");
		msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), "SendPesa Banking Channel Pakistan");
		msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), "586");
		msg.set(ACCOUNCT_NO_1.getValue(), "541247533");
		msg.set(PIN_DATA.getValue(), new byte[8]);
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Account Balance Inquiry Request : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String accountBalanceInquiryResponse() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0210");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "310000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(RESPONSE_CODE.getValue(), "00");
		msg.set(ADDITIONAL_AMOUNT.getValue(), "000000054000");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Account Balance Inquiry Response : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String IBFundTransferRequest() throws Exception {
		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0220");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "970000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000010000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(MERCHANT_TYPE.getValue(), "0008");
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RRN.getValue(), FieldUtil.buildRRN("000000"));
		msg.set(TERMINAL_ID.getValue(), "00000000");
		msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), "SendPesa Banking Channel Pakistan");
		msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), "586");
		msg.set(ACCOUNCT_NO_1.getValue(), "541247533");
		msg.set(ACCOUNCT_NO_1.getValue(), "242362344");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("IBFT Request : " + new String(result));

		return Hex.encodeHexString(result);
	}

	public static String IBFundTransferResponse() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0230");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "970000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(RESPONSE_CODE.getValue(), "00");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("IBFT Response : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String miniStatementRequest() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0200");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "850000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(MERCHANT_TYPE.getValue(), "0008");
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RRN.getValue(), FieldUtil.buildRRN("000000"));
		msg.set(TERMINAL_ID.getValue(), "00000000");
		msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), "SendPesa Banking Channel Pakistan");
		msg.set(TRANSACTION_CURRENCY_CODE.getValue(), "586");
		msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), "586");
		msg.set(ACCOUNCT_NO_1.getValue(), "541247533");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Mini Statement Request : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String miniStatementResponse() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0210");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "850000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(CONVERSION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RESPONSE_CODE.getValue(), "00");
		
		StringBuffer statement = new StringBuffer();
		statement.append("05|");
		statement.append("06/05/2014?IB Transfer?CR?1000.00?103593.68?|");
		statement.append("06/05/2014?Cash Deposit?CR?1000.00?103593.68?|");
		statement.append("06/05/2014?VISA ATM Cash Withdrawal?DR?1000.00?103593.68?|");
		statement.append("06/05/2014?POS Purchase?DR?1000.00?103593.68?|");
		statement.append("06/05/2014?VISA ATM Cash Withdrawal?DR?1000.00?103593.68?|");
		
		msg.set(ADDITIONAL_RESPONSE_DATA_PRIVATE.getValue(), statement.toString());
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Mini Statement Response : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String customerRelationshipInquiryRequest() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0200");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "770000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), "00000000");
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(TIME_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(DATE_LOCAL_TRANSACTION.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(MERCHANT_TYPE.getValue(), "0008");
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(TRANSACTION_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), "000000000000");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RRN.getValue(), FieldUtil.buildRRN("000000"));
		msg.set(TERMINAL_ID.getValue(), "00000000");
		msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), "SendPesa Banking Channel Pakistan");
		msg.set(TRANSACTION_CURRENCY_CODE.getValue(), "586");
		msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), "586");
		msg.set(ACCOUNCT_NO_1.getValue(), "541247533");
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Customer Relationship Inquiry Request : " + new String(result));

		return Hex.encodeHexString(result);

	}

	public static String customerRelationshipInquiryResponse() throws Exception {

		// Build Message Header

		StringBuilder headerString = new StringBuilder();

		headerString.append("ISO8583");
		headerString.append("1.1");
		headerString.append("000");
		headerString.append(FieldUtil.buildMessageRecieveTime());
		byte[] header = headerString.toString().getBytes();

		// Build Message Body

		GenericPackager packager = ISO8583Utils.getISOPackager();
		ISOMsg msg = packager.createISOMsg();
		msg.setPackager(packager);

		// @formatter:off
		msg.setMTI("0210");
		msg.set(PAN.getValue(), "3610426520012");
		msg.set(PROCESSING_CODE.getValue(), "770000");
		msg.set(TRANSACTION_AMOUNT.getValue(), "000000000000");
		msg.set(TRANSACTION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(SYSTEMS_TRACE_NO.getValue(), "672030");
		msg.set(SETTLEMENT_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(CONVERSION_DATE.getValue(), DateTools.dateToString(new Date(), MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		msg.set(NETWORK_IDENTIFIER.getValue(), "INO");
		msg.set(ACQUIRER_IDENTIFICATION.getValue(), "60373300000");
		msg.set(RESPONSE_CODE.getValue(), "00");
		
		StringBuffer relationShips = new StringBuffer();
		relationShips.append("02|");
		relationShips.append("KAZI MUHAMMAD AAMIR|CURRENT|PKR|1500.00|01|");
		relationShips.append("IJAZ NAZIR|CURRENT|PKR|2500.00|01|");
		
		msg.set(ADDITIONAL_RESPONSE_DATA_PRIVATE.getValue(), relationShips.toString());
		
		// @formatter:on

		byte[] body = msg.pack();

		byte[] result = ArrayUtils.addAll(header, body);

		System.out.println("Customer Relationship Inquiry Response : " + new String(result));

		return Hex.encodeHexString(result);

	}

	
}

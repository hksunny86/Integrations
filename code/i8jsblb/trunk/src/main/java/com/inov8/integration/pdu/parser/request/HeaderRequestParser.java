package com.inov8.integration.pdu.parser.request;

import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.pdu.request.fields.HeaderRequestFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * HeaderRequestParser is common header parser for all request(s) to be parsed.
 * The request parser package is not being used while communicating with real
 * external system.
 * </p>
 * <p>
 * It is only used while DC is supposed to communicate with PHOENIX mock server.
 * The whole request package is used by mock server to parse, validate and
 * respond according to the request received.
 * </p>
 * 
 */
public class HeaderRequestParser {

	/**
	 * <p>
	 * <code> HeaderRequestParser </code> have only one method parseHeader which
	 * takes byte[] and return populated PhoenixRequestHeader object.
	 * </p>
	 * <p>
	 * HeaderRequestFields have header all field definitions. The parsing
	 * processing puts byte[] into String and read characters in fashion, start
	 * index and end index. Starting positions are specified in
	 * <code>HeaderRequestFields</code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return PhoenixRequestHeader
	 */
	public static PhoenixRequestHeader parseHeader(byte[] receivedBytes) {

		PhoenixRequestHeader requestHeader = new PhoenixRequestHeader();
		String receivedString = new String(receivedBytes);

		//logger.debug(receivedBytes.length + " " + receivedString.length());

		// 1 -------- Message Protocol----------
		int fromIndex = HeaderRequestFields.MESSAGE_PROTOCOL.getIndex();
		int toIndex = fromIndex + HeaderRequestFields.MESSAGE_PROTOCOL.getLength();
		String val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getMessageProtocol().setValue(val);

		// 2 -------- version----------
		fromIndex = HeaderRequestFields.VERSION.getIndex();
		toIndex = fromIndex + HeaderRequestFields.VERSION.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getVersion().setValue(val);

		// 3 -------- Field in error----------
		fromIndex = HeaderRequestFields.FIELD_IN_ERROR.getIndex();
		toIndex = fromIndex + HeaderRequestFields.FIELD_IN_ERROR.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getFieldInError().setValue(val);

		// 4 -------- Message Type----------
		fromIndex = HeaderRequestFields.MESSAGE_TYPE.getIndex();
		toIndex = fromIndex + HeaderRequestFields.MESSAGE_TYPE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getMessageType().setValue(val);

		// 5 -------- Transmission Date and Time----------
		fromIndex = HeaderRequestFields.TRANSMISSION_DATE_TIME.getIndex();
		toIndex = fromIndex + HeaderRequestFields.TRANSMISSION_DATE_TIME.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getTransmissionDateTime().setValue(val);

		// 6 -------- Delivery channel type----------
		fromIndex = HeaderRequestFields.DELIVERY_CHANNEL_TYPE.getIndex();
		toIndex = fromIndex + HeaderRequestFields.DELIVERY_CHANNEL_TYPE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getDeliveryChannelType().setValue(val);

		// 7 -------- Delivery Channel ID----------
		fromIndex = HeaderRequestFields.DELIVERY_CHANNEL_ID.getIndex();
		toIndex = fromIndex + HeaderRequestFields.DELIVERY_CHANNEL_ID.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getDeliveryChannelId().setValue(val);

		// 8 -------- Customer Identification----------
		fromIndex = HeaderRequestFields.CUSTOMER_IDENTIFICATION.getIndex();
		toIndex = fromIndex + HeaderRequestFields.CUSTOMER_IDENTIFICATION.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getCustomerIdentification().setValue(val);

		// 9 -------- transaction code ----------
		fromIndex = HeaderRequestFields.TRANSACTION_CODE.getIndex();
		toIndex = fromIndex + HeaderRequestFields.TRANSACTION_CODE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getTransactionCode().setValue(val);

		// 10 -------- transaction Date ----------
		fromIndex = HeaderRequestFields.TRANSACTION_DATE.getIndex();
		toIndex = fromIndex + HeaderRequestFields.TRANSACTION_DATE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getTransactionDate().setValue(val);

		// 11 -------- transaction time ----------
		fromIndex = HeaderRequestFields.TRANSACTION_TIME.getIndex();
		toIndex = fromIndex + HeaderRequestFields.TRANSACTION_TIME.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getTransactionTime().setValue(val);

		// 12 -------- rrn ----------
		fromIndex = HeaderRequestFields.RETRIEVAL_REFERENCE_NUMBER.getIndex();
		toIndex = fromIndex + HeaderRequestFields.RETRIEVAL_REFERENCE_NUMBER.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getRetrievalRefNumber().setValue(val);

		// 13 -------- customer pin data ----------
		fromIndex = HeaderRequestFields.CUSTOMER_PIN_DATA.getIndex();
		toIndex = fromIndex + HeaderRequestFields.CUSTOMER_PIN_DATA.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getCustomerPIN().setValue(val);

		// 14 -------- agent id ----------
		fromIndex = HeaderRequestFields.AGENT_ID.getIndex();
		toIndex = fromIndex + HeaderRequestFields.AGENT_ID.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getAgentId().setValue(val);

		// 15 -------- channel specific data ----------
		fromIndex = HeaderRequestFields.CHANNEL_SPECIFIC_DATA.getIndex();
		toIndex = fromIndex + HeaderRequestFields.CHANNEL_SPECIFIC_DATA.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getChannelSpecificData().setValue(val);

		// 16 -------- channel private data ----------
		fromIndex = HeaderRequestFields.CHANNEL_PRIVATE_DATA.getIndex();
		toIndex = fromIndex + HeaderRequestFields.CHANNEL_PRIVATE_DATA.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getChannelPrivateData().setValue(val);

		// 17 -------- authorization resp ID ----------
		fromIndex = HeaderRequestFields.AUTHORIZATION_RESPONSE_ID.getIndex();
		toIndex = fromIndex + HeaderRequestFields.AUTHORIZATION_RESPONSE_ID.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getAuthResponseId().setValue(val);

		// 18 -------- Response Code----------
		fromIndex = HeaderRequestFields.RESPONSE_CODE.getIndex();
		toIndex = fromIndex + HeaderRequestFields.RESPONSE_CODE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		requestHeader.getResponseCode().setValue(val);

		return requestHeader;
	}

}

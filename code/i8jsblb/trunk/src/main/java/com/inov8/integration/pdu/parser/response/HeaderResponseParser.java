package com.inov8.integration.pdu.parser.response;

import com.inov8.integration.pdu.response.PhoenixResponseHeader;
import com.inov8.integration.pdu.response.fields.HeaderResponseFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * HeaderResponseParser is common header parser for all responses to be parsed.
 * </p>
 * 
 */
public class HeaderResponseParser {

	/**
	 * <p>
	 * <code> HeaderRequestParser </code> have only one method parseHeader which
	 * takes byte[] and return populated PhoenixResponseHeader object.
	 * </p>
	 * <p>
	 * HeaderResponseFields have header all field definitions. The parsing
	 * processing puts byte[] into String and read characters in fashion, start
	 * index and end index. Starting positions are specified in
	 * <code>HeaderResponseFields</code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return PhoenixResponseHeader
	 */
	public static PhoenixResponseHeader parseHeader(byte[] receivedBytes) {
		PhoenixResponseHeader respHeader = new PhoenixResponseHeader();
		String receivedString = new String(receivedBytes);

		// 1 -------- Message Protocol----------
		int fromIndex = HeaderResponseFields.MESSAGE_PROTOCOL.getIndex();
		int toIndex = fromIndex + HeaderResponseFields.MESSAGE_PROTOCOL.getLength();
		String val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getMessageProtocol().setValue(val);

		// 2 -------- version----------
		fromIndex = HeaderResponseFields.VERSION.getIndex();
		toIndex = fromIndex + HeaderResponseFields.VERSION.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getVersion().setValue(val);

		// 3 -------- Field in error----------
		fromIndex = HeaderResponseFields.FIELD_IN_ERROR.getIndex();
		toIndex = fromIndex + HeaderResponseFields.FIELD_IN_ERROR.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getFieldInError().setValue(val);

		// 4 -------- Message Type----------
		fromIndex = HeaderResponseFields.MESSAGE_TYPE.getIndex();
		toIndex = fromIndex + HeaderResponseFields.MESSAGE_TYPE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getMessageType().setValue(val);

		// 5 -------- Transmission Date and Time----------
		fromIndex = HeaderResponseFields.TRANSMISSION_DATE_TIME.getIndex();
		toIndex = fromIndex + HeaderResponseFields.TRANSMISSION_DATE_TIME.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getTransmissionDateTime().setValue(val);

		// 6 -------- Delivery channel type----------
		fromIndex = HeaderResponseFields.DELIVERY_CHANNEL_TYPE.getIndex();
		toIndex = fromIndex + HeaderResponseFields.DELIVERY_CHANNEL_TYPE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getDeliveryChannelType().setValue(val);

		// 7 -------- Delivery Channel ID----------
		fromIndex = HeaderResponseFields.DELIVERY_CHANNEL_ID.getIndex();
		toIndex = fromIndex + HeaderResponseFields.DELIVERY_CHANNEL_ID.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getDeliveryChannelId().setValue(val);

		// 8 -------- Customer Identification----------
		fromIndex = HeaderResponseFields.CUSTOMER_IDENTIFICATION.getIndex();
		toIndex = fromIndex + HeaderResponseFields.CUSTOMER_IDENTIFICATION.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getCustomerIdentification().setValue(val);

		// 9 -------- transaction code ----------
		fromIndex = HeaderResponseFields.TRANSACTION_CODE.getIndex();
		toIndex = fromIndex + HeaderResponseFields.TRANSACTION_CODE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getTransactionCode().setValue(val);

		// 10 -------- transaction Date ----------
		fromIndex = HeaderResponseFields.TRANSACTION_DATE.getIndex();
		toIndex = fromIndex + HeaderResponseFields.TRANSACTION_DATE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getTransactionDate().setValue(val);

		// 11 -------- transaction time ----------
		fromIndex = HeaderResponseFields.TRANSACTION_TIME.getIndex();
		toIndex = fromIndex + HeaderResponseFields.TRANSACTION_TIME.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getTransactionTime().setValue(val);

		// 12 -------- rrn ----------
		fromIndex = HeaderResponseFields.RETRIEVAL_REFERENCE_NUMBER.getIndex();
		toIndex = fromIndex + HeaderResponseFields.RETRIEVAL_REFERENCE_NUMBER.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getRetrievalRefNumber().setValue(val);

		// 13 -------- customer pin data ----------
		fromIndex = HeaderResponseFields.CUSTOMER_PIN_DATA.getIndex();
		toIndex = fromIndex + HeaderResponseFields.CUSTOMER_PIN_DATA.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getCustomerPIN().setValue(val);

		// 14 -------- channel specific data ----------
		fromIndex = HeaderResponseFields.CHANNEL_SPECIFIC_DATA.getIndex();
		toIndex = fromIndex + HeaderResponseFields.CHANNEL_SPECIFIC_DATA.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getChannelSpecificData().setValue(val);

		// 15 -------- channel private data ----------
		fromIndex = HeaderResponseFields.CHANNEL_PRIVATE_DATA.getIndex();
		toIndex = fromIndex + HeaderResponseFields.CHANNEL_PRIVATE_DATA.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getChannelPrivateData().setValue(val);

		// 16 -------- authorization resp ID ----------
		fromIndex = HeaderResponseFields.AUTHORIZATION_RESPONSE_ID.getIndex();
		toIndex = fromIndex + HeaderResponseFields.AUTHORIZATION_RESPONSE_ID.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getAuthResponseId().setValue(val);

		// 17 -------- Response Code----------
		fromIndex = HeaderResponseFields.RESPONSE_CODE.getIndex();
		toIndex = fromIndex + HeaderResponseFields.RESPONSE_CODE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		respHeader.getResponseCode().setValue(val);

		return respHeader;
	}

}

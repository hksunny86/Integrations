package com.inov8.integration.pdu.parser.request;

import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.pdu.request.TransactionStatusInquiry;
import com.inov8.integration.pdu.request.fields.TXStatusInquiryRequestFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>TXStatusInquiryReqParser</code> is responsible to parse Transaction
 * Status Inquiry.
 * </p>
 * <p>
 * It is only used while DC is supposed to communicate with PHOENIX mock server.
 * The whole request package is used by mock server to parse, validate and
 * respond according to the request received.
 * </p>
 * 
 */
public class TXStatusInquiryReqParser {
	/**
	 * <p>
	 * <code> TXStatusInquiryReqParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated TransactionStatusInquiry
	 * object.
	 * </p>
	 * <p>
	 * <code> TXStatusInquiryRequestFields </code> have all field definitions. The
	 * parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code> TXStatusInquiryRequestFields </code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return TransactionStatusInquiry
	 */
	public static TransactionStatusInquiry parse(byte[] receivedBytes, PhoenixRequestHeader header) {
		TransactionStatusInquiry transactionStatusInquiry = parse(receivedBytes);
		transactionStatusInquiry.setHeader(header);
		return transactionStatusInquiry;
	}

	private static TransactionStatusInquiry parse(byte[] receivedBytes) {

		TransactionStatusInquiry transactionStatusInquiry = new TransactionStatusInquiry();
		String receivedString = new String(receivedBytes);

		// 1 -------- Channel Id----------
		int fromIndex = TXStatusInquiryRequestFields.CHANNEL_ID.getIndex();
		int toIndex = fromIndex + TXStatusInquiryRequestFields.CHANNEL_ID.getLength();
		String value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		transactionStatusInquiry.getChannelId().setValue(value);

		// 2 -------- transaction date----------
		fromIndex = TXStatusInquiryRequestFields.TRANSACTION_DATE.getIndex();
		toIndex = fromIndex + TXStatusInquiryRequestFields.TRANSACTION_DATE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		transactionStatusInquiry.getTransactionDate().setValue(value);

		// 3 -------- transaction time ----------
		fromIndex = TXStatusInquiryRequestFields.TRANSACTION_TIME.getIndex();
		toIndex = fromIndex + TXStatusInquiryRequestFields.TRANSACTION_TIME.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		transactionStatusInquiry.getTransactionTime().setValue(value);

		// 4 -------- Retrieval Reference Number time ----------
		fromIndex = TXStatusInquiryRequestFields.RETRIEVAL_REFERENCE_NUMBER.getIndex();
		toIndex = fromIndex + TXStatusInquiryRequestFields.RETRIEVAL_REFERENCE_NUMBER.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		transactionStatusInquiry.getRetrivalReferenceNumber().setValue(value);

		return transactionStatusInquiry;
	}

}

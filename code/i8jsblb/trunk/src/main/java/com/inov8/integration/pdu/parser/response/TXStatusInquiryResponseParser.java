package com.inov8.integration.pdu.parser.response;

import com.inov8.integration.pdu.response.PhoenixResponseHeader;
import com.inov8.integration.pdu.response.TransactionStatusInquiryResponse;
import com.inov8.integration.pdu.response.fields.TXStatusInquiryResponseFields;
import com.inov8.integration.util.FieldUtil;

public class TXStatusInquiryResponseParser {
	
	/**
	 * <p>
	 * <code> TXStatusInquiryResponseFields </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated TransactionStatusInquiryResponse
	 * object.
	 * </p>
	 * <p>
	 * <code> TXStatusInquiryResponseFields </code> have all field definitions. The
	 * parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code> TXStatusInquiryResponseFields </code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return TransactionStatusInquiryResponse
	 */
	public static TransactionStatusInquiryResponse parse(byte[] receivedBytes,PhoenixResponseHeader header) {
		TransactionStatusInquiryResponse resp = parse(receivedBytes);
		resp.setHeader(header);
		return resp;
	}

	private static TransactionStatusInquiryResponse parse(byte[] receivedBytes) {
		TransactionStatusInquiryResponse resp = new TransactionStatusInquiryResponse();
		String receivedString = new String(receivedBytes);
		
		int fromIndex = TXStatusInquiryResponseFields.TX_RESPONSE_CODE.getIndex();
		int toIndex = fromIndex + TXStatusInquiryResponseFields.TX_RESPONSE_CODE.getLength();
		String val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getTransactionResponseCode().setValue(val);

		return resp;
	}
	
}

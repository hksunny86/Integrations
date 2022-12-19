package com.inov8.integration.pdu.parser.request;

import com.inov8.integration.pdu.request.BillInquiry;
import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.pdu.request.fields.BillInquiryRequestFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>BillInquiryRequestParser</code> is responsible to parse Bill Inquiry
 * Request.
 * </p>
 * <p>
 * It is only used while DC is supposed to communicate with PHOENIX mock server.
 * The whole request package is used by mock server to parse, validate and
 * respond according to the request received.
 * </p>
 * 
 */
public class BillInquiryRequestParser {
	/**
	 * <p>
	 * <code> BillInquiryRequestParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated BillInquiry object.
	 * </p>
	 * <p>
	 * BillInquiryRequestFields have all field definitions. The parsing
	 * processing puts byte[] into String and read characters in fashion, start
	 * index and end index. Starting positions are specified in
	 * <code>BillInquiryRequestFields</code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return BillInquiry
	 */
	public static BillInquiry parse(byte[] receivedBytes, PhoenixRequestHeader header) {
		BillInquiry billInquiry = parse(receivedBytes);
		billInquiry.setHeader(header);
		return billInquiry;
	}

	private static BillInquiry parse(byte[] receivedBytes) {

		BillInquiry billInquiry = new BillInquiry();
		String receivedString = new String(receivedBytes);

		// 1 -------- utility company id ----------
		int fromIndex = BillInquiryRequestFields.UTILITY_COMPANY_ID.getIndex();
		int toIndex = fromIndex + BillInquiryRequestFields.UTILITY_COMPANY_ID.getLength();
		String value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		billInquiry.getUtilityCompanyId().setValue(value);

		// 2 -------- consumer number ----------
		fromIndex = BillInquiryRequestFields.CONSUMER_NUMBER.getIndex();
		toIndex = fromIndex + BillInquiryRequestFields.CONSUMER_NUMBER.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		billInquiry.getConsumerNumber().setValue(value);

		return billInquiry;
	}

}

package com.inov8.integration.pdu.parser.response;

import com.inov8.integration.pdu.response.BillInquiryResponse;
import com.inov8.integration.pdu.response.PhoenixResponseHeader;
import com.inov8.integration.pdu.response.fields.BillInquiryResponseFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>BillInquiryResponseParser</code> is responsible to parse Bill Inquiry
 * Response.
 * </p>
 * 
 */
public class BillInquiryResponseParser {

	/**
	 * <p>
	 * <code> BillInquiryResponseParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated BillInquiryResponse
	 * object.
	 * </p>
	 * <p>
	 * <code> BillInquiryResponseFields </code> have all field definitions.
	 * The parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code> BillInquiryResponseFields </code> and end index is calculated
	 * as startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return BillInquiryResponse
	 */
	public static BillInquiryResponse parse(byte[] receivedBytes, PhoenixResponseHeader header) {
		BillInquiryResponse resp = parse(receivedBytes);
		resp.setHeader(header);
		return resp;
	}

	private static BillInquiryResponse parse(byte[] receivedBytes) {
		BillInquiryResponse resp = new BillInquiryResponse();
		String receivedString = new String(receivedBytes);

		// subscriber name
		int fromIndex = BillInquiryResponseFields.SUBSCRIBER_NAME.getIndex();
		int toIndex = fromIndex + BillInquiryResponseFields.SUBSCRIBER_NAME.getLength();
		String val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getSubscriberName().setValue(val);

		// billing month
		fromIndex = BillInquiryResponseFields.BILLING_MONTH.getIndex();
		toIndex = fromIndex + BillInquiryResponseFields.BILLING_MONTH.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getBillingMonth().setValue(val);

		// due date payment amount
		fromIndex = BillInquiryResponseFields.DUEDATE_PAY_AMOUNT.getIndex();
		toIndex = fromIndex + BillInquiryResponseFields.DUEDATE_PAY_AMOUNT.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getDueDatePayableAmount().setValue(val);
		System.out.println();

		// payment due date
		fromIndex = BillInquiryResponseFields.PAYMENT_DUE_DATE.getIndex();
		toIndex = fromIndex + BillInquiryResponseFields.PAYMENT_DUE_DATE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getPaymentDueDate().setValue(val);

		// payment after due date
		fromIndex = BillInquiryResponseFields.PAYMENT_AFTER_DUE_DATE.getIndex();
		toIndex = fromIndex + BillInquiryResponseFields.PAYMENT_AFTER_DUE_DATE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getPaymentAfterDueDate().setValue(val);

		// bill status
		fromIndex = BillInquiryResponseFields.BILL_STATUS.getIndex();
		toIndex = fromIndex + BillInquiryResponseFields.BILL_STATUS.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getBillStatus().setValue(val);

		// payment auth resp id
		fromIndex = BillInquiryResponseFields.PAYMENT_AUTH_RESPONSE_ID.getIndex();
		toIndex = fromIndex + BillInquiryResponseFields.PAYMENT_AUTH_RESPONSE_ID.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getPaymentAuthResponseId().setValue(val);

		// net ced
		fromIndex = BillInquiryResponseFields.NET_CED.getIndex();
		toIndex = fromIndex + BillInquiryResponseFields.NET_CED.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getNetCED().setValue(val);

		// new withholding tax
		fromIndex = BillInquiryResponseFields.NET_WITHHOLDING_TAX.getIndex();
		toIndex = fromIndex + BillInquiryResponseFields.NET_WITHHOLDING_TAX.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getNetWithholdingTAX().setValue(val);

		// additional data
		// fromIndex = BillInquiryResponseFields.ADDITIONAL_DATA.getIndex();
		// toIndex = fromIndex +
		// BillInquiryResponseFields.ADDITIONAL_DATA.getLength();
		// val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		// resp.getAdditionalData().setValue(val);

		return resp;
	}

}

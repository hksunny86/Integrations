package com.inov8.integration.pdu.parser.request;

import com.inov8.integration.pdu.request.BillPaymentMarkingAdvice;
import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.pdu.request.fields.BillPaymentMarkingAdviceRequestFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>BillPaymentMarkingAdviceRequestParser</code> is responsible to parse
 * Bill Payment MarkingAdvice Request.
 * </p>
 * <p>
 * It is only used while DC is supposed to communicate with PHOENIX mock server.
 * The whole request package is used by mock server to parse, validate and
 * respond according to the request received.
 * </p>
 * 
 */
public class BillPaymentMarkingAdviceRequestParser {
	/**
	 * <p>
	 * <code> BillPaymentMarkingAdviceRequestParser </code> have only one method
	 * parseHeader which takes byte[] and return populated
	 * BillPaymentMarkingAdvice object.
	 * </p>
	 * <p>
	 * BillPaymentMarkingAdviceRequestFields have header all field definitions.
	 * The parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code>BillPaymentMarkingAdviceRequestFields</code> and end index is
	 * calculated as startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return BillPaymentMarkingAdvice
	 */
	public static BillPaymentMarkingAdvice parse(byte[] receivedBytes, PhoenixRequestHeader header) {
		BillPaymentMarkingAdvice billPaymentMarkingAdvice = parse(receivedBytes);
		billPaymentMarkingAdvice.setHeader(header);
		return billPaymentMarkingAdvice;
	}

	private static BillPaymentMarkingAdvice parse(byte[] receivedBytes) {

		BillPaymentMarkingAdvice billPaymentMarkingAdvice = new BillPaymentMarkingAdvice();
		String receivedString = new String(receivedBytes);

		// 1 -------- UTILITY COMPANY ID ----------
		int fromIndex = BillPaymentMarkingAdviceRequestFields.UTILITY_COMPANY_ID.getIndex();
		int toIndex = fromIndex + BillPaymentMarkingAdviceRequestFields.UTILITY_COMPANY_ID.getLength();
		String value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		billPaymentMarkingAdvice.getUtilityCompanyId().setValue(value);

		// 2 -------- CONSUMER NUMBER ----------
		fromIndex = BillPaymentMarkingAdviceRequestFields.CONSUMER_NUMBER.getIndex();
		toIndex = fromIndex + BillPaymentMarkingAdviceRequestFields.CONSUMER_NUMBER.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		billPaymentMarkingAdvice.getConsumerNumber().setValue(value);

		// 3 -------- AMOUNT PAID ----------
		fromIndex = BillPaymentMarkingAdviceRequestFields.AMOUNT_PAID.getIndex();
		toIndex = fromIndex + BillPaymentMarkingAdviceRequestFields.AMOUNT_PAID.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		billPaymentMarkingAdvice.getAmountPaid().setValue(value);

		// 4 -------- FROM ACCOUNT NUMBER ----------
		fromIndex = BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_NUMBER.getIndex();
		toIndex = fromIndex + BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_NUMBER.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		billPaymentMarkingAdvice.getFromAccountNumber().setValue(value);

		// 5 -------- FROM ACCOUNT TYPE ----------
		fromIndex = BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_TYPE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		billPaymentMarkingAdvice.getFromAccountType().setValue(value);

		// 6 -------- FROM ACCOUNT CURRENCY ----------
		fromIndex = BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		billPaymentMarkingAdvice.getFromAccountCurrency().setValue(value);

		return billPaymentMarkingAdvice;
	}

}

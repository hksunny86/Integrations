package com.inov8.integration.pdu.parser.request;

import com.inov8.integration.pdu.request.OpenAccountBalanceInquiry;
import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.pdu.request.fields.OpenAccountBIRequestFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>OpenAccountBIRequestParser</code> is responsible to parse Open Account
 * Balance Check Request.
 * </p>
 * <p>
 * It is only used while DC is supposed to communicate with PHOENIX mock server.
 * The whole request package is used by mock server to parse, validate and
 * respond according to the request received.
 * </p>
 * 
 */
public class OpenAccountBIRequestParser {
	/**
	 * <p>
	 * <code> OpenAccountBIRequestParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated
	 * OpenAccountBalanceInquiry object.
	 * </p>
	 * <p>
	 * OpenAccountBIRequestFields have all field definitions. The parsing
	 * processing puts byte[] into String and read characters in fashion, start
	 * index and end index. Starting positions are specified in
	 * <code>OpenAccountBIRequestFields</code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return OpenAccountBalanceInquiry
	 */
	public static OpenAccountBalanceInquiry parse(byte[] receivedBytes, PhoenixRequestHeader header) {
		OpenAccountBalanceInquiry accountInquiry = parse(receivedBytes);
		accountInquiry.setHeader(header);
		return accountInquiry;
	}

	private static OpenAccountBalanceInquiry parse(byte[] receivedBytes) {

		OpenAccountBalanceInquiry accountInquiry = new OpenAccountBalanceInquiry();
		String receivedString = new String(receivedBytes);

		// 1 -------- Channel Id----------
		int fromIndex = OpenAccountBIRequestFields.ACCOUNT_NUMBER.getIndex();
		int toIndex = fromIndex + OpenAccountBIRequestFields.ACCOUNT_NUMBER.getLength();
		String value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		accountInquiry.getAccountNumber().setValue(value);

		// 2 -------- account type ----------
		fromIndex = OpenAccountBIRequestFields.ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + OpenAccountBIRequestFields.ACCOUNT_TYPE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		accountInquiry.getAccountType().setValue(value);

		// 3 -------- transaction time ----------
		fromIndex = OpenAccountBIRequestFields.ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + OpenAccountBIRequestFields.ACCOUNT_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		accountInquiry.getAccountCurrency().setValue(value);

		return accountInquiry;
	}

}

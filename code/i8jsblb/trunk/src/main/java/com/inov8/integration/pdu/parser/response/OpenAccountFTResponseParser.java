package com.inov8.integration.pdu.parser.response;

import com.inov8.integration.pdu.response.OpenAccountFTResponse;
import com.inov8.integration.pdu.response.PhoenixResponseHeader;
import com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>OpenAccountFTResponseParser</code> is responsible to parse Open Account
 * Fund Transfer Response.
 * </p>
 * 
 */
public class OpenAccountFTResponseParser {

	/**
	 * <p>
	 * <code> OpenAccountFTResponseParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated
	 * OpenAccountFTResponse object.
	 * </p>
	 * <p>
	 * <code> OpenAccountFTResponseFields </code> have all field definitions.
	 * The parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code> OpenAccountFTResponseFields </code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return OpenAccountFTResponse
	 */
	public static OpenAccountFTResponse parse(byte[] receivedBytes, PhoenixResponseHeader header) {
		OpenAccountFTResponse resp = parse(receivedBytes);
		resp.setHeader(header);
		return resp;
	}

	private static OpenAccountFTResponse parse(byte[] receivedBytes) {

		OpenAccountFTResponse openAccountFTResponse = new OpenAccountFTResponse();
		String receivedString = new String(receivedBytes);

		// 1 -------- FROM ACCOUNT NUMBER ----------
		int fromIndex = OpenAccountFTResponseFields.FROM_ACCOUNT_NUMBER.getIndex();
		int toIndex = fromIndex + OpenAccountFTResponseFields.FROM_ACCOUNT_NUMBER.getLength();
		String value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFTResponse.getFromAccountNumber().setValue(value);

		// 2 -------- FROM ACCOUNT TYPE ----------
		fromIndex = OpenAccountFTResponseFields.FROM_ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + OpenAccountFTResponseFields.FROM_ACCOUNT_TYPE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFTResponse.getFromAccountType().setValue(value);

		// 3 -------- FROM ACCOUNT CURRENCY ----------
		fromIndex = OpenAccountFTResponseFields.FROM_ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + OpenAccountFTResponseFields.FROM_ACCOUNT_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFTResponse.getFromAccountCurrency().setValue(value);

		// 4 -------- TO ACCOUNT NUMBER ----------
		fromIndex = OpenAccountFTResponseFields.TO_ACCOUNT_NUMBER.getIndex();
		toIndex = fromIndex + OpenAccountFTResponseFields.TO_ACCOUNT_NUMBER.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFTResponse.getToAccountNumber().setValue(value);

		// 5 -------- TO ACCOUNT TYPE ----------
		fromIndex = OpenAccountFTResponseFields.TO_ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + OpenAccountFTResponseFields.TO_ACCOUNT_TYPE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFTResponse.getToAccountType().setValue(value);

		// 6 -------- TO ACCOUNT CURRENCY ----------
		fromIndex = OpenAccountFTResponseFields.TO_ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + OpenAccountFTResponseFields.TO_ACCOUNT_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFTResponse.getToAccountCurrency().setValue(value);

		// 7 -------- TRANSACTION AMOUNT ----------
		fromIndex = OpenAccountFTResponseFields.TRANSACTION_AMOUNT.getIndex();
		toIndex = fromIndex + OpenAccountFTResponseFields.TRANSACTION_AMOUNT.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFTResponse.getTransactionAmount().setValue(value);

		// 8 -------- TRANSACTION CURRENCY ----------
		fromIndex = OpenAccountFTResponseFields.TRANSACTION_CURRENCY.getIndex();
		toIndex = fromIndex + OpenAccountFTResponseFields.TRANSACTION_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFTResponse.getTransactionCurrency().setValue(value);

		return openAccountFTResponse;
	}

}

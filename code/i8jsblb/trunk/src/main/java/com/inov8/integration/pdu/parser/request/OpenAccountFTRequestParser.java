package com.inov8.integration.pdu.parser.request;

import com.inov8.integration.pdu.request.OpenAccountFT;
import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.pdu.request.fields.OpenAccountFTRequestFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>OpenAccountFTRequestParser</code> is responsible to parse Open Account
 * Fund Transfer Request.
 * </p>
 * <p>
 * It is only used while DC is supposed to communicate with PHOENIX mock server.
 * The whole request package is used by mock server to parse, validate and
 * respond according to the request received.
 * </p>
 * 
 */
public class OpenAccountFTRequestParser {
	/**
	 * <p>
	 * <code> OpenAccountFTRequestParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated OpenAccountFT
	 * object.
	 * </p>
	 * <p>
	 * <code>OpenAccountFTRequestFields</code> have all field definitions. The
	 * parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code>OpenAccountFTRequestFields</code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return OpenAccountFT
	 */
	public static OpenAccountFT parse(byte[] receivedBytes, PhoenixRequestHeader header) {
		OpenAccountFT openAccountFT = parse(receivedBytes);
		openAccountFT.setHeader(header);
		return openAccountFT;
	}

	private static OpenAccountFT parse(byte[] receivedBytes) {

		OpenAccountFT openAccountFT = new OpenAccountFT();
		String receivedString = new String(receivedBytes);

		// 1 -------- FROM ACCOUNT NUMBER ----------
		int fromIndex = OpenAccountFTRequestFields.FROM_ACCOUNT_NUMBER.getIndex();
		int toIndex = fromIndex + OpenAccountFTRequestFields.FROM_ACCOUNT_NUMBER.getLength();
		String value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFT.getFromAccountNumber().setValue(value);

		// 2 -------- FROM ACCOUNT TYPE ----------
		fromIndex = OpenAccountFTRequestFields.FROM_ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + OpenAccountFTRequestFields.FROM_ACCOUNT_TYPE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFT.getFromAccountType().setValue(value);

		// 3 -------- FROM ACCOUNT CURRENCY ----------
		fromIndex = OpenAccountFTRequestFields.FROM_ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + OpenAccountFTRequestFields.FROM_ACCOUNT_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFT.getFromAccountCurrency().setValue(value);

		// 4 -------- TO ACCOUNT NUMBER ----------
		fromIndex = OpenAccountFTRequestFields.TO_ACCOUNT_NUMBER.getIndex();
		toIndex = fromIndex + OpenAccountFTRequestFields.TO_ACCOUNT_NUMBER.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFT.getToAccountNumber().setValue(value);

		// 5 -------- TO ACCOUNT TYPE ----------
		fromIndex = OpenAccountFTRequestFields.TO_ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + OpenAccountFTRequestFields.TO_ACCOUNT_TYPE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFT.getToAccountType().setValue(value);

		// 6 -------- TO ACCOUNT CURRENCY ----------
		fromIndex = OpenAccountFTRequestFields.TO_ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + OpenAccountFTRequestFields.TO_ACCOUNT_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFT.getToAccountCurrency().setValue(value);

		// 7 -------- TRANSACTION AMOUNT ----------
		fromIndex = OpenAccountFTRequestFields.TRANSACTION_AMOUNT.getIndex();
		toIndex = fromIndex + OpenAccountFTRequestFields.TRANSACTION_AMOUNT.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFT.getTransactionAmount().setValue(value);

		// 8 -------- TRANSACTION CURRENCY ----------
		fromIndex = OpenAccountFTRequestFields.TRANSACTION_CURRENCY.getIndex();
		toIndex = fromIndex + OpenAccountFTRequestFields.TRANSACTION_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		openAccountFT.getTransactionCurrency().setValue(value);

		return openAccountFT;
	}

}

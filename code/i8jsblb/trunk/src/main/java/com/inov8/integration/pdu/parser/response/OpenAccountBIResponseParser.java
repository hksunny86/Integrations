package com.inov8.integration.pdu.parser.response;

import com.inov8.integration.pdu.response.OpenAccountBIResponse;
import com.inov8.integration.pdu.response.PhoenixResponseHeader;
import com.inov8.integration.pdu.response.fields.OpenAccountBIResponseFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>OpenAccountBIResponseParser</code> is responsible to parse Open Account
 * Balance Inquiry Response.
 * </p>
 * 
 */
public class OpenAccountBIResponseParser {

	/**
	 * <p>
	 * <code> OpenAccountBIResponseParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated OpenAccountBIResponse
	 * object.
	 * </p>
	 * <p>
	 * <code> OpenAccountBIResponseFields </code> have all field definitions. The
	 * parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code> OpenAccountBIResponseFields </code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return OpenAccountBIResponse
	 */

	public static OpenAccountBIResponse parse(byte[] receivedBytes, PhoenixResponseHeader header) {
		OpenAccountBIResponse resp = parse(receivedBytes);
		resp.setHeader(header);
		return resp;
	}

	private static OpenAccountBIResponse parse(byte[] receivedBytes) {
		OpenAccountBIResponse resp = new OpenAccountBIResponse();
		String receivedString = new String(receivedBytes);

		// account number
		int fromIndex = OpenAccountBIResponseFields.ACCOUNT_NUMBER.getIndex();
		int toIndex = fromIndex + OpenAccountBIResponseFields.ACCOUNT_NUMBER.getLength();
		String val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAccountNumber().setValue(val);

		// account type
		fromIndex = OpenAccountBIResponseFields.ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.ACCOUNT_TYPE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAccountType().setValue(val);

		// account currency
		fromIndex = OpenAccountBIResponseFields.ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.ACCOUNT_CURRENCY.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAccountCurrency().setValue(val);

		// account status
		fromIndex = OpenAccountBIResponseFields.ACCOUNT_STATUS.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.ACCOUNT_STATUS.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAccountStatus().setValue(val);

		// available balance
		fromIndex = OpenAccountBIResponseFields.AVAILABLE_BALANCE.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.AVAILABLE_BALANCE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAvailableBalance().setValue(val);

		// actual balance
		fromIndex = OpenAccountBIResponseFields.ACTUAL_BALANCE.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.ACTUAL_BALANCE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getActualBalance().setValue(val);

		// withdraw limit
		fromIndex = OpenAccountBIResponseFields.WITHDRAW_LIMIT.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.WITHDRAW_LIMIT.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getWithdrawLimit().setValue(val);

		// available withdraw limit
		fromIndex = OpenAccountBIResponseFields.AVAILABLE_WITHDRAW_LIMIT.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.AVAILABLE_WITHDRAW_LIMIT.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAvailableWithdrawLimit().setValue(val);

		// draft limit
		fromIndex = OpenAccountBIResponseFields.DRAFT_LIMIT.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.DRAFT_LIMIT.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getDraftLimit().setValue(val);

		// limit expiry date
		fromIndex = OpenAccountBIResponseFields.LIMIT_EXPIRY_DATE.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.LIMIT_EXPIRY_DATE.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getLimitExpiryDate().setValue(val);

		// currency name
		fromIndex = OpenAccountBIResponseFields.CURRENCY_NAME.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.CURRENCY_NAME.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getCurrencyName().setValue(val);

		// currency mnemonic
		fromIndex = OpenAccountBIResponseFields.CURRENCY_MNEMONIC.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.CURRENCY_MNEMONIC.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getCurrencyMnemonic().setValue(val);

		// currency decimal points
		fromIndex = OpenAccountBIResponseFields.CURRENCY_DECIMAL_POINTS.getIndex();
		toIndex = fromIndex + OpenAccountBIResponseFields.CURRENCY_DECIMAL_POINTS.getLength();
		val = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getCurrencyDecimalPoints().setValue(val);

		return resp;
	}

}

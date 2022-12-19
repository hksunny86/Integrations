package com.inov8.integration.pdu.parser.response;

import com.inov8.integration.pdu.response.PhoenixResponseHeader;
import com.inov8.integration.pdu.response.TitleFetchRespone;
import com.inov8.integration.pdu.response.fields.TitleFetchResponseFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>TitleFetchResponseParser</code> is responsible to parse Open Title
 * Fetch Response.
 * </p>
 * 
 */
public class TitleFetchResponseParser {

	/**
	 * <p>
	 * <code> TitleFetchResponseParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated TitleFetchRespone
	 * object.
	 * </p>
	 * <p>
	 * <code> TitleFetchResponseFields </code> have all field definitions. The
	 * parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code> TitleFetchResponseFields </code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return TitleFetchRespone
	 */
	public static TitleFetchRespone parse(byte[] receivedBytes, PhoenixResponseHeader header) {
		TitleFetchRespone resp = parse(receivedBytes);
		resp.setHeader(header);
		return resp;
	}

	private static TitleFetchRespone parse(byte[] receivedBytes) {
		TitleFetchRespone resp = new TitleFetchRespone();
		String receivedString = new String(receivedBytes);

		// 1 -------- bank account imd ----------
		int fromIndex = TitleFetchResponseFields.ACCOUNT_BANK_IMD.getIndex();
		int toIndex = fromIndex + TitleFetchResponseFields.ACCOUNT_BANK_IMD.getLength();
		String value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAccountBankIMD().setValue(value);

		// 2 -------- account number ----------
		fromIndex = TitleFetchResponseFields.ACCOUNT_NUMBER.getIndex();
		toIndex = fromIndex + TitleFetchResponseFields.ACCOUNT_NUMBER.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAccountNumber().setValue(value);

		// 3 -------- account type ----------
		fromIndex = TitleFetchResponseFields.ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + TitleFetchResponseFields.ACCOUNT_TYPE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAccountType().setValue(value);

		// 4 -------- account currency ----------
		fromIndex = TitleFetchResponseFields.ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + TitleFetchResponseFields.ACCOUNT_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getAccountCurrency().setValue(value);

		// 5 -------- title of account ----------
		fromIndex = TitleFetchResponseFields.TITLE_OF_ACCOUNT.getIndex();
		toIndex = fromIndex + TitleFetchResponseFields.TITLE_OF_ACCOUNT.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		resp.getTitleOfAccount().setValue(value);

		return resp;
	}

}

package com.inov8.integration.pdu.parser.request;

import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.pdu.request.TitleFetchRequest;
import com.inov8.integration.pdu.request.fields.TitleFetchRequestFields;
import com.inov8.integration.util.FieldUtil;

/**
 * <p>
 * <code>TitleFetchRequestParser</code> is responsible to parse Title Fetch
 * Request.
 * </p>
 * <p>
 * It is only used while DC is supposed to communicate with PHOENIX mock server.
 * The whole request package is used by mock server to parse, validate and
 * respond according to the request received.
 * </p>
 * 
 */
public class TitleFetchRequestParser {
	/**
	 * <p>
	 * <code> TitleFetchRequestParser </code> have only one method
	 * parse(byte[]) which takes byte[] and return populated TitleFetchRequest
	 * object.
	 * </p>
	 * <p>
	 * <code> TitleFetchRequestFields </code> have all field definitions. The
	 * parsing processing puts byte[] into String and read characters in
	 * fashion, start index and end index. Starting positions are specified in
	 * <code> TitleFetchRequestFields </code> and end index is calculated as
	 * startIndex + length of field.
	 * </p>
	 * 
	 * @param byte[]
	 * @return TitleFetchRequest
	 */
	public static TitleFetchRequest parse(byte[] receivedBytes, PhoenixRequestHeader header) {
		TitleFetchRequest titleFetchRequest = parse(receivedBytes);
		titleFetchRequest.setHeader(header);
		return titleFetchRequest;
	}

	private static TitleFetchRequest parse(byte[] receivedBytes) {

		TitleFetchRequest titleFetchRequest = new TitleFetchRequest();
		String receivedString = new String(receivedBytes);

		// 1 -------- bank account imd ----------
		int fromIndex = TitleFetchRequestFields.ACCOUNT_BANK_IMD.getIndex();
		int toIndex = fromIndex + TitleFetchRequestFields.ACCOUNT_BANK_IMD.getLength();
		String value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		titleFetchRequest.getAccountBankIMD().setValue(value);

		// 2 -------- account number ----------
		fromIndex = TitleFetchRequestFields.ACCOUNT_NUMBER.getIndex();
		toIndex = fromIndex + TitleFetchRequestFields.ACCOUNT_NUMBER.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		titleFetchRequest.getAccountNumber().setValue(value);

		// 3 -------- account type ----------
		fromIndex = TitleFetchRequestFields.ACCOUNT_TYPE.getIndex();
		toIndex = fromIndex + TitleFetchRequestFields.ACCOUNT_TYPE.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		titleFetchRequest.getAccountType().setValue(value);

		// 4 -------- account currency ----------
		fromIndex = TitleFetchRequestFields.ACCOUNT_CURRENCY.getIndex();
		toIndex = fromIndex + TitleFetchRequestFields.ACCOUNT_CURRENCY.getLength();
		value = FieldUtil.getSpecificField(fromIndex, toIndex, receivedString);
		titleFetchRequest.getAccountCurrency().setValue(value);

		return titleFetchRequest;
	}

}

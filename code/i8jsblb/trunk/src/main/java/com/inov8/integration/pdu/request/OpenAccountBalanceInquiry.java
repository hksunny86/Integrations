package com.inov8.integration.pdu.request;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class OpenAccountBalanceInquiry extends BasePDU {
	int pduLength = 239 + 25;
	private PhoenixRequestHeader header = new PhoenixRequestHeader();
	private Field accountNumber = new Field(null, 20, DataTypeEnum.AN);
	private Field accountType = new Field(null, 2, DataTypeEnum.N);
	private Field accountCurrency = new Field(null, 3, DataTypeEnum.N);

	public OpenAccountBalanceInquiry() {
		this.fields.addAll(header.getFields());
		this.fields.add(accountNumber);
		this.fields.add(accountType);
		this.fields.add(accountCurrency);
		
	}
	
	public static void main(String[] args) {
		new OpenAccountBalanceInquiry();
	}

	public PhoenixRequestHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixRequestHeader header) {
		this.header = header;
	}

	public Field getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Field accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Field getAccountType() {
		return accountType;
	}

	public void setAccountType(Field accountType) {
		this.accountType = accountType;
	}

	public Field getAccountCurrency() {
		return accountCurrency;
	}

	public void setAccountCurrency(Field accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public int getPduLength() {
		return pduLength;
	}

	public void setPduLength(int pduLength) {
		this.pduLength = pduLength;
	}
	
}

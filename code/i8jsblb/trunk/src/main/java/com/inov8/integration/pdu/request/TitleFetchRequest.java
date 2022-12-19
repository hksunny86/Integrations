package com.inov8.integration.pdu.request;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class TitleFetchRequest extends BasePDU {
	int pduLength = 239 + 27;
	private PhoenixRequestHeader header = new PhoenixRequestHeader();
	private Field accountBankIMD = new Field(null, 11, DataTypeEnum.AN);
	private Field accountNumber = new Field(null, 20, DataTypeEnum.AN);
	private Field accountType = new Field(null, 2, DataTypeEnum.N);
	private Field accountCurrency = new Field(null, 3, DataTypeEnum.N);

	public TitleFetchRequest() {
		this.fields.addAll(header.getFields());
		this.fields.add(accountBankIMD);
		this.fields.add(accountNumber);
		this.fields.add(accountType);
		this.fields.add(accountCurrency);
		
	}
	
	public static void main(String[] args) {
		new TitleFetchRequest();
	}

	public Field getAccountBankIMD() {
		return accountBankIMD;
	}

	public void setAccountBankIMD(Field accountBankIMD) {
		this.accountBankIMD = accountBankIMD;
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

	public PhoenixRequestHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixRequestHeader header) {
		this.header = header;
	}
}

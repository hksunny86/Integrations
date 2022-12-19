package com.inov8.integration.pdu.response;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class TitleFetchRespone extends BasePDU {
	private int pduLength = 239 + 66;
	private PhoenixResponseHeader header = new PhoenixResponseHeader();
	private Field accountBankIMD = new Field(null, 11, DataTypeEnum.AN);
	private Field accountNumber = new Field(null, 20, DataTypeEnum.AN);
	private Field accountType = new Field(null, 2, DataTypeEnum.N);
	private Field accountCurrency = new Field(null, 3, DataTypeEnum.N);
	private Field titleOfAccount = new Field(null, 30, DataTypeEnum.AN);

	public TitleFetchRespone() {
		this.fields.addAll(header.getHeaderFields());
		this.fields.add(accountBankIMD);
		this.fields.add(accountNumber);
		this.fields.add(accountType);
		this.fields.add(accountCurrency);
		this.fields.add(titleOfAccount);
	}

	public static void main(String[] args) {
		new TitleFetchRespone();
	}

	public PhoenixResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixResponseHeader header) {
		this.header = header;
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

	public Field getTitleOfAccount() {
		return titleOfAccount;
	}

	public void setTitleOfAccount(Field titleOfAccount) {
		this.titleOfAccount = titleOfAccount;
	}
}

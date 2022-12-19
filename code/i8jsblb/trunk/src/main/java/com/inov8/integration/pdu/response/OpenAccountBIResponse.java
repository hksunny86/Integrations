package com.inov8.integration.pdu.response;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class OpenAccountBIResponse extends BasePDU {
	private int pduLength = 239 + 134;
	private PhoenixResponseHeader header = new PhoenixResponseHeader();
	private Field accountNumber = new Field(null, 20, DataTypeEnum.AN);
	private Field accountType = new Field(null, 2, DataTypeEnum.N);
	private Field accountCurrency = new Field(null, 3, DataTypeEnum.N);
	private Field accountStatus = new Field(null, 2, DataTypeEnum.AN);
	private Field availableBalance = new Field(null, 14, DataTypeEnum.XN);
	private Field actualBalance = new Field(null, 14, DataTypeEnum.XN);
	private Field withdrawLimit = new Field(null, 13, DataTypeEnum.N);
	private Field availableWithdrawLimit = new Field(null, 13, DataTypeEnum.N);
	private Field draftLimit = new Field(null, 13, DataTypeEnum.N);
	private Field limitExpiryDate = new Field(null, 8, DataTypeEnum.N);
	private Field currencyName = new Field(null, 30, DataTypeEnum.A);
	private Field currencyMnemonic = new Field(null, 3, DataTypeEnum.A);
	private Field currencyDecimalPoints = new Field(null, 1, DataTypeEnum.N);

	public OpenAccountBIResponse() {
		this.fields.addAll(header.getHeaderFields());
		this.fields.add(accountNumber);
		this.fields.add(accountType);
		this.fields.add(accountCurrency);
		this.fields.add(accountStatus);
		this.fields.add(availableBalance);
		this.fields.add(actualBalance);
		this.fields.add(withdrawLimit);
		this.fields.add(availableWithdrawLimit);
		this.fields.add(draftLimit);
		this.fields.add(limitExpiryDate);
		this.fields.add(currencyName);
		this.fields.add(currencyMnemonic);
		this.fields.add(currencyDecimalPoints);

	}

	public static void main(String[] args) {
		new OpenAccountBIResponse();
	}

	public PhoenixResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixResponseHeader header) {
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

	public Field getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Field accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Field getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(Field availableBalance) {
		this.availableBalance = availableBalance;
	}

	public Field getActualBalance() {
		return actualBalance;
	}

	public void setActualBalance(Field actualBalance) {
		this.actualBalance = actualBalance;
	}

	public Field getWithdrawLimit() {
		return withdrawLimit;
	}

	public void setWithdrawLimit(Field withdrawLimit) {
		this.withdrawLimit = withdrawLimit;
	}

	public Field getAvailableWithdrawLimit() {
		return availableWithdrawLimit;
	}

	public void setAvailableWithdrawLimit(Field availableWithdrawLimit) {
		this.availableWithdrawLimit = availableWithdrawLimit;
	}

	public Field getDraftLimit() {
		return draftLimit;
	}

	public void setDraftLimit(Field draftLimit) {
		this.draftLimit = draftLimit;
	}

	public Field getLimitExpiryDate() {
		return limitExpiryDate;
	}

	public void setLimitExpiryDate(Field limitExpiryDate) {
		this.limitExpiryDate = limitExpiryDate;
	}

	public Field getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(Field currencyName) {
		this.currencyName = currencyName;
	}

	public Field getCurrencyMnemonic() {
		return currencyMnemonic;
	}

	public void setCurrencyMnemonic(Field currencyMnemonic) {
		this.currencyMnemonic = currencyMnemonic;
	}

	public Field getCurrencyDecimalPoints() {
		return currencyDecimalPoints;
	}

	public void setCurrencyDecimalPoints(Field currencyDecimalPoints) {
		this.currencyDecimalPoints = currencyDecimalPoints;
	}

}

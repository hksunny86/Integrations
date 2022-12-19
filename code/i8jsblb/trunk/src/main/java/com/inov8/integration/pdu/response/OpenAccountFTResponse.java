package com.inov8.integration.pdu.response;

import static com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields.FROM_ACCOUNT_CURRENCY;
import static com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields.FROM_ACCOUNT_NUMBER;
import static com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields.FROM_ACCOUNT_TYPE;
import static com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields.TO_ACCOUNT_CURRENCY;
import static com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields.TO_ACCOUNT_NUMBER;
import static com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields.TO_ACCOUNT_TYPE;
import static com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields.TRANSACTION_AMOUNT;
import static com.inov8.integration.pdu.response.fields.OpenAccountFTResponseFields.TRANSACTION_CURRENCY;

import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class OpenAccountFTResponse extends BasePDU {
	int pduLength = 239 + 25;
	private PhoenixResponseHeader header = new PhoenixResponseHeader();
	private Field fromAccountNumber = new Field(null, FROM_ACCOUNT_NUMBER.getLength(), FROM_ACCOUNT_NUMBER.getType());
	private Field fromAccountType = new Field(null, FROM_ACCOUNT_TYPE.getLength(), FROM_ACCOUNT_TYPE.getType());
	private Field fromAccountCurrency = new Field(null, FROM_ACCOUNT_CURRENCY.getLength(), FROM_ACCOUNT_CURRENCY.getType());
	private Field toAccountNumber = new Field(null, TO_ACCOUNT_NUMBER.getLength(), TO_ACCOUNT_NUMBER.getType());
	private Field toAccountType = new Field(null, TO_ACCOUNT_TYPE.getLength(), TO_ACCOUNT_TYPE.getType());
	private Field toAccountCurrency = new Field(null, TO_ACCOUNT_CURRENCY.getLength(), TO_ACCOUNT_CURRENCY.getType());
	private Field transactionAmount = new Field(null, TRANSACTION_AMOUNT.getLength(), TRANSACTION_AMOUNT.getType());
	private Field transactionCurrency = new Field(null, TRANSACTION_CURRENCY.getLength(), TRANSACTION_CURRENCY.getType());

	public OpenAccountFTResponse() {
		this.fields.addAll(header.getHeaderFields());
		this.fields.add(fromAccountNumber);
		this.fields.add(fromAccountType);
		this.fields.add(fromAccountCurrency);

		this.fields.add(toAccountNumber);
		this.fields.add(toAccountType);
		this.fields.add(toAccountCurrency);

		this.fields.add(transactionAmount);
		this.fields.add(transactionCurrency);

	}

	public static void main(String[] args) {
		new OpenAccountFTResponse();
	}

	public PhoenixResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixResponseHeader header) {
		this.header = header;
	}

	public int getPduLength() {
		return pduLength;
	}

	public void setPduLength(int pduLength) {
		this.pduLength = pduLength;
	}

	public Field getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(Field fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public Field getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(Field fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public Field getFromAccountCurrency() {
		return fromAccountCurrency;
	}

	public void setFromAccountCurrency(Field fromAccountCurrency) {
		this.fromAccountCurrency = fromAccountCurrency;
	}

	public Field getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(Field toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public Field getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(Field toAccountType) {
		this.toAccountType = toAccountType;
	}

	public Field getToAccountCurrency() {
		return toAccountCurrency;
	}

	public void setToAccountCurrency(Field toAccountCurrency) {
		this.toAccountCurrency = toAccountCurrency;
	}

	public Field getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Field transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Field getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(Field transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

}

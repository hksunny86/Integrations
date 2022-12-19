package com.inov8.integration.pdu.request;

import static com.inov8.integration.pdu.request.fields.BillPaymentMarkingAdviceRequestFields.AMOUNT_PAID;
import static com.inov8.integration.pdu.request.fields.BillPaymentMarkingAdviceRequestFields.CONSUMER_NUMBER;
import static com.inov8.integration.pdu.request.fields.BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_CURRENCY;
import static com.inov8.integration.pdu.request.fields.BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_NUMBER;
import static com.inov8.integration.pdu.request.fields.BillPaymentMarkingAdviceRequestFields.FROM_ACCOUNT_TYPE;
import static com.inov8.integration.pdu.request.fields.BillPaymentMarkingAdviceRequestFields.UTILITY_COMPANY_ID;

import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class BillPaymentMarkingAdvice extends BasePDU {
	private int pduLength = 239 + 70;
	private PhoenixRequestHeader header = new PhoenixRequestHeader();
	private Field utilityCompanyId = new Field(null, UTILITY_COMPANY_ID.getLength(), UTILITY_COMPANY_ID.getType());
	private Field consumerNumber = new Field(null, CONSUMER_NUMBER.getLength(), CONSUMER_NUMBER.getType());
	private Field amountPaid = new Field(null, AMOUNT_PAID.getLength(), AMOUNT_PAID.getType());
	private Field fromAccountNumber = new Field(null, FROM_ACCOUNT_NUMBER.getLength(), FROM_ACCOUNT_NUMBER.getType());
	private Field fromAccountType = new Field(null, FROM_ACCOUNT_TYPE.getLength(), FROM_ACCOUNT_TYPE.getType());
	private Field fromAccountCurrency = new Field(null, FROM_ACCOUNT_CURRENCY.getLength(), FROM_ACCOUNT_CURRENCY.getType());

	public BillPaymentMarkingAdvice() {
		this.fields.addAll(header.getFields());
		this.fields.add(utilityCompanyId);
		this.fields.add(consumerNumber);
		this.fields.add(amountPaid);
		this.fields.add(fromAccountNumber);
		this.fields.add(fromAccountType);
		this.fields.add(fromAccountCurrency);

	}

	public int getPduLength() {
		return pduLength;
	}

	public PhoenixRequestHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixRequestHeader header) {
		this.header = header;
	}

	public Field getUtilityCompanyId() {
		return utilityCompanyId;
	}

	public void setUtilityCompanyId(Field utilityCompanyId) {
		this.utilityCompanyId = utilityCompanyId;
	}

	public Field getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(Field consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	public Field getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Field amountPaid) {
		this.amountPaid = amountPaid;
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
}

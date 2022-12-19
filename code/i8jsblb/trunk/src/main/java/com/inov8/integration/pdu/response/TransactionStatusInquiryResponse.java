package com.inov8.integration.pdu.response;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class TransactionStatusInquiryResponse extends BasePDU {
	private int pduLength = 239 + 2;

	private PhoenixResponseHeader header = new PhoenixResponseHeader();
	private Field transactionResponseCode = new Field(null, 2, DataTypeEnum.N);

	public TransactionStatusInquiryResponse() {
		this.fields.addAll(header.getHeaderFields());
		this.fields.add(transactionResponseCode);
	}

	public static void main(String[] args) {
		new TransactionStatusInquiryResponse();
	}

	public PhoenixResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixResponseHeader header) {
		this.header = header;
	}

	public Field getTransactionResponseCode() {
		return transactionResponseCode;
	}

	public void setTransactionResponseCode(Field transactionResponseCode) {
		this.transactionResponseCode = transactionResponseCode;
	}
}

package com.inov8.integration.pdu.request;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.enums.PhoenixEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class TransactionStatusInquiry extends BasePDU {
	int pduLength = 239 + 34;
	private PhoenixRequestHeader header = new PhoenixRequestHeader();
	private Field channelId = new Field(PhoenixEnum.DELIVERY_CHANNEL_ID.getValue(), 8, DataTypeEnum.AN);
	private Field transactionDate = new Field(null, 8, DataTypeEnum.N);
	private Field transactionTime = new Field(null, 6, DataTypeEnum.N);
	private Field retrivalReferenceNumber = new Field(null, 12, DataTypeEnum.N);

	public TransactionStatusInquiry() {
		this.fields.addAll(header.getFields());
		fields.add(channelId);
		fields.add(transactionDate);
		fields.add(transactionTime);
		fields.add(retrivalReferenceNumber);
	}

	public static void main(String[] args) {
		new TransactionStatusInquiry();
	}

	public PhoenixRequestHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixRequestHeader header) {
		this.header = header;
	}

	public Field getChannelId() {
		return channelId;
	}

	public void setChannelId(Field channelId) {
		this.channelId = channelId;
	}

	public Field getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Field transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Field getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Field transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Field getRetrivalReferenceNumber() {
		return retrivalReferenceNumber;
	}

	public void setRetrivalReferenceNumber(Field retrivalReferenceNumber) {
		this.retrivalReferenceNumber = retrivalReferenceNumber;
	}

}
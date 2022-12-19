package com.inov8.integration.pdu.response;

import java.util.List;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class BillInquiryResponse extends BasePDU {
	private int pduLength = 239 + 303;
	private PhoenixResponseHeader header = new PhoenixResponseHeader();
	
	private Field utilityCompanyId = new Field(null, 8, DataTypeEnum.AN);
	private Field consumerNumber = new Field(null, 24, DataTypeEnum.AN);
	private Field subscriberName = new Field(null, 30, DataTypeEnum.AN);
	private Field billingMonth = new Field(null, 4, DataTypeEnum.N);
	private Field dueDatePayableAmount = new Field(null, 14, DataTypeEnum.XN);
	private Field paymentDueDate = new Field(null, 6, DataTypeEnum.N);
	private Field paymentAfterDueDate = new Field(null, 14, DataTypeEnum.XN);
	private Field billStatus = new Field(null, 1, DataTypeEnum.A);
	private Field paymentAuthResponseId = new Field(null, 6, DataTypeEnum.AN);
	private Field netCED = new Field(null, 14, DataTypeEnum.XN);
	private Field netWithholdingTAX = new Field(null, 14, DataTypeEnum.XN);

	// private Field additionalData = new Field(null, 200, DataTypeEnum.AN);

	public BillInquiryResponse() {
		this.fields.addAll(header.getHeaderFields());
		this.fields.add(utilityCompanyId);
		this.fields.add(consumerNumber);
		this.fields.add(subscriberName);
		this.fields.add(billingMonth);
		this.fields.add(dueDatePayableAmount);
		this.fields.add(paymentDueDate);
		this.fields.add(paymentAfterDueDate);
		this.fields.add(billStatus);
		this.fields.add(paymentAuthResponseId);
		this.fields.add(netCED);
		this.fields.add(netWithholdingTAX);
		// this.fields.add(additionalData);

	}

	public List<Field> getFields() {
		return fields;
	}

	public PhoenixResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixResponseHeader header) {
		this.header = header;
	}

	public Field getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(Field subscriberName) {
		this.subscriberName = subscriberName;
	}

	public Field getBillingMonth() {
		return billingMonth;
	}

	public void setBillingMonth(Field billingMonth) {
		this.billingMonth = billingMonth;
	}

	public Field getDueDatePayableAmount() {
		return dueDatePayableAmount;
	}

	public void setDueDatePayableAmount(Field dueDatePayableAmount) {
		this.dueDatePayableAmount = dueDatePayableAmount;
	}

	public Field getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(Field paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public Field getPaymentAfterDueDate() {
		return paymentAfterDueDate;
	}

	public void setPaymentAfterDueDate(Field paymentAfterDueDate) {
		this.paymentAfterDueDate = paymentAfterDueDate;
	}

	public Field getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Field billStatus) {
		this.billStatus = billStatus;
	}

	public Field getPaymentAuthResponseId() {
		return paymentAuthResponseId;
	}

	public void setPaymentAuthResponseId(Field paymentAuthResponseId) {
		this.paymentAuthResponseId = paymentAuthResponseId;
	}

	public Field getNetCED() {
		return netCED;
	}

	public void setNetCED(Field netCED) {
		this.netCED = netCED;
	}

	public Field getNetWithholdingTAX() {
		return netWithholdingTAX;
	}

	public void setNetWithholdingTAX(Field netWithholdingTAX) {
		this.netWithholdingTAX = netWithholdingTAX;
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

}

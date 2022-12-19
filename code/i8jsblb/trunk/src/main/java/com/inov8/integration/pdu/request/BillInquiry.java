package com.inov8.integration.pdu.request;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class BillInquiry extends BasePDU {
	int pduLength = 239 + 32;
	private PhoenixRequestHeader header = new PhoenixRequestHeader();
	private Field utilityCompanyId = new Field(null, 8, DataTypeEnum.AN);
	private Field consumerNumber = new Field(null, 24, DataTypeEnum.AN);

	public BillInquiry() {
		this.fields.addAll(header.getFields());
		this.fields.add(utilityCompanyId);
		this.fields.add(consumerNumber);
	}

	public static void main(String[] args) {
		BillInquiry inquiry = new BillInquiry();
		inquiry.assemblePDU();
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

}

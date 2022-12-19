package com.inov8.integration.pdu.response.fields;

import static com.inov8.integration.enums.DataTypeEnum.A;
import static com.inov8.integration.enums.DataTypeEnum.AN;
import static com.inov8.integration.enums.DataTypeEnum.N;
import static com.inov8.integration.enums.DataTypeEnum.XN;

import com.inov8.integration.enums.DataTypeEnum;

public enum BillInquiryResponseFields {
	UTILITY_COMPANY_ID(8, AN, 239),
	CONSUMER_NUMBER(24, N, 247),
	SUBSCRIBER_NAME(30,AN,271),
	BILLING_MONTH(4,N,301),
	DUEDATE_PAY_AMOUNT(14,XN,305),
	PAYMENT_DUE_DATE(6,N,319),
	PAYMENT_AFTER_DUE_DATE(14,XN,325),
	BILL_STATUS(1,A,339),
	PAYMENT_AUTH_RESPONSE_ID(6,AN,340),
	NET_CED(14,XN,346),
	NET_WITHHOLDING_TAX(14,XN,360),
	ADDITIONAL_DATA(200,AN,374);
	
	BillInquiryResponseFields(Integer length, DataTypeEnum type, int index) {
		this.length = length;
		this.type = type;
		this.index = index;
	}

	private Integer length;
	private DataTypeEnum type;
	private int index;

	public Integer getLength() {
		return length;
	}

	public DataTypeEnum getType() {
		return type;
	}
	
	public int getIndex() {
		return index;
	} 
}

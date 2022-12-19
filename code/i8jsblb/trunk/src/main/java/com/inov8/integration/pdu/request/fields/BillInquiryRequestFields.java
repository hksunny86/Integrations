package com.inov8.integration.pdu.request.fields;

import static com.inov8.integration.enums.DataTypeEnum.AN;
import static com.inov8.integration.enums.DataTypeEnum.N;

import com.inov8.integration.enums.DataTypeEnum;

public enum BillInquiryRequestFields {
	UTILITY_COMPANY_ID(8, AN, 239),
	CONSUMER_NUMBER(24, N, 247);
	
	BillInquiryRequestFields(Integer length, DataTypeEnum type, int index) {
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

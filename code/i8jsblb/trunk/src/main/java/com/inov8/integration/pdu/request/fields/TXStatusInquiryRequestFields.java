package com.inov8.integration.pdu.request.fields;

import static com.inov8.integration.enums.DataTypeEnum.AN;
import static com.inov8.integration.enums.DataTypeEnum.N;

import com.inov8.integration.enums.DataTypeEnum;

public enum TXStatusInquiryRequestFields {
	CHANNEL_ID(8, AN, 239),
	TRANSACTION_DATE(2, N, 247),
	TRANSACTION_TIME(3, N,249),
	RETRIEVAL_REFERENCE_NUMBER(2, N,252);
	
	TXStatusInquiryRequestFields(Integer length, DataTypeEnum type, int index) {
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

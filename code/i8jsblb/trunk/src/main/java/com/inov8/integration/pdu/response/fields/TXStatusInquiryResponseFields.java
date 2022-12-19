package com.inov8.integration.pdu.response.fields;

import static com.inov8.integration.enums.DataTypeEnum.N;

import com.inov8.integration.enums.DataTypeEnum;

public enum TXStatusInquiryResponseFields {
	TX_RESPONSE_CODE(2,N,239);
	
	TXStatusInquiryResponseFields(Integer length, DataTypeEnum type, int index) {
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

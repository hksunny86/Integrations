package com.inov8.integration.pdu.response.fields;

import static com.inov8.integration.enums.DataTypeEnum.AN;
import static com.inov8.integration.enums.DataTypeEnum.N;

import com.inov8.integration.enums.DataTypeEnum;

public enum TitleFetchResponseFields {
	ACCOUNT_BANK_IMD(11,AN,239),
	ACCOUNT_NUMBER(20,AN,250),
	ACCOUNT_TYPE(2,N,270),
	ACCOUNT_CURRENCY(3,N,272),
	TITLE_OF_ACCOUNT(30,AN,275);

	TitleFetchResponseFields(Integer length, DataTypeEnum type, int index) {
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

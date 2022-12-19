package com.inov8.integration.pdu.response.fields;

import static com.inov8.integration.enums.DataTypeEnum.AN;
import static com.inov8.integration.enums.DataTypeEnum.N;

import com.inov8.integration.enums.DataTypeEnum;

public enum OpenAccountFTResponseFields{
	FROM_ACCOUNT_NUMBER(20, AN, 239),
	FROM_ACCOUNT_TYPE(2, N, 259),
	FROM_ACCOUNT_CURRENCY(3, N,261),
	TO_ACCOUNT_NUMBER(20, AN, 264),
	TO_ACCOUNT_TYPE(2, N, 284),
	TO_ACCOUNT_CURRENCY(3, N,286),
	TRANSACTION_AMOUNT(13, N, 289),
	TRANSACTION_CURRENCY(3, N, 302);
	
	OpenAccountFTResponseFields(Integer length, DataTypeEnum type, int index) {
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

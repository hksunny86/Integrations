package com.inov8.integration.pdu.response.fields;

import static com.inov8.integration.enums.DataTypeEnum.A;
import static com.inov8.integration.enums.DataTypeEnum.AN;
import static com.inov8.integration.enums.DataTypeEnum.N;
import static com.inov8.integration.enums.DataTypeEnum.XN;

import com.inov8.integration.enums.DataTypeEnum;

public enum OpenAccountBIResponseFields {
	ACCOUNT_NUMBER(20,AN,239),
	ACCOUNT_TYPE(2,N,259),
	ACCOUNT_CURRENCY(3,N,261),
	ACCOUNT_STATUS(2,AN,264),
	AVAILABLE_BALANCE(14,XN,266),
	ACTUAL_BALANCE(14,XN,280),
	WITHDRAW_LIMIT(13,N,294),
	AVAILABLE_WITHDRAW_LIMIT(13,N,307),
	DRAFT_LIMIT(13,N,320),
	LIMIT_EXPIRY_DATE(8,N,333),
	CURRENCY_NAME(30,A,341),
	CURRENCY_MNEMONIC(3,A,371),
	CURRENCY_DECIMAL_POINTS(1,N,374);
	
	OpenAccountBIResponseFields(Integer length, DataTypeEnum type, int index) {
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

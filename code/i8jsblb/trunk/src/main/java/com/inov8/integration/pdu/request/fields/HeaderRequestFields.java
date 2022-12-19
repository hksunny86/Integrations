package com.inov8.integration.pdu.request.fields;

import static com.inov8.integration.enums.DataTypeEnum.A;
import static com.inov8.integration.enums.DataTypeEnum.AN;
import static com.inov8.integration.enums.DataTypeEnum.N;

import java.util.EnumSet;

import com.inov8.integration.enums.DataTypeEnum;

public enum HeaderRequestFields {
//	HEADHER_LENGTH(2, N,0),
	MESSAGE_PROTOCOL(7, A,0),
	VERSION(2, N,7),
	FIELD_IN_ERROR(3, N,9),
	MESSAGE_TYPE(4, N,12),
	TRANSMISSION_DATE_TIME(14, N,16),
	DELIVERY_CHANNEL_TYPE(2, N,30),
	DELIVERY_CHANNEL_ID(20, AN,32),
	CUSTOMER_IDENTIFICATION(30, AN,52),
	TRANSACTION_CODE(3, N, 82),
	TRANSACTION_DATE(8, N, 85),
	TRANSACTION_TIME(6, N,93),
	RETRIEVAL_REFERENCE_NUMBER(12, N,99),
	CUSTOMER_PIN_DATA(20, AN,111),
	AGENT_ID(16, AN,131),
	CHANNEL_SPECIFIC_DATA(64, AN,147),
	CHANNEL_PRIVATE_DATA(20, AN,211),
	AUTHORIZATION_RESPONSE_ID(6,AN,231),
	RESPONSE_CODE(2, AN,237);
	
	HeaderRequestFields(Integer length, DataTypeEnum type, int index) {
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
	
	public static void main(String[] args) {
		int length = 0;
		for(HeaderRequestFields e : EnumSet.allOf(HeaderRequestFields.class)){
			length+=e.getLength();
		}
	}

	public int getIndex() {
		return index;
	} 
}

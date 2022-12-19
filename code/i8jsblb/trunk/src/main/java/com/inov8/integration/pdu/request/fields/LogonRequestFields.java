package com.inov8.integration.pdu.request.fields;

import static com.inov8.integration.enums.DataTypeEnum.AN;

import com.inov8.integration.enums.DataTypeEnum;

public enum LogonRequestFields {
	CHANNEL_PASSWORD(16, AN, 239);

	LogonRequestFields(Integer length, DataTypeEnum type, int index) {
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

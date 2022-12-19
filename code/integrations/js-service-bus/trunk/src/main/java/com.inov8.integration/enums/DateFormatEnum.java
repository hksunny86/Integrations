package com.inov8.integration.enums;

public enum DateFormatEnum {
	// @formatter:off
	TIME_LOCAL_TRANSACTION("HHmmss"),
	DATE_LOCAL_TRANSACTION("yyyyMMdd"),
	DATE_EXPIRY("MMddyyyy"),
	T24_DATE_LOCAL_TRANSACTION("MMdd"),
	TRANSACTION_DATE("yyyyMMddHHmmss"),
	TRANSACTION_DATE_TIME("MMddHHmmss"),
	EXPIRY_DATE("yyyy-MM-dd HH:mm:ss"),
	CNIC_FORMAT("E MMM dd HH:mm:ss z yyyy"),
	DATE_TIME("MM/dd/yyyy HH:mm:ss");
	// @formatter:on
	private DateFormatEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
}

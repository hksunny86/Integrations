package com.inov8.integration.enums;

public enum DateFormatEnum {
	// @formatter:off
	TIME_LOCAL_TRANSACTION("HHmmss"),
	DATE_LOCAL_TRANSACTION("yyyyMMdd"),
	DATE_EXPIRY("MMddyyyy"),
	T24_DATE_LOCAL_TRANSACTION("MMdd"),
	TRANSACTION_DATE("yyyyMMddHHmmss"),
	TRANSACTION_DATE_TIME("MMddHHmmss"),
	IBFT_DATE_TIME("yyMMddHHmmss"),
	EXPIRY_DATE("yyyy-MM-dd HH:mm:ss"),
	CNIC_FORMAT("E MMM dd HH:mm:ss z yyyy"),
	DATE_TIME("MM/dd/yyyy"),
	MERCHANT_CAMPING_DATE_FORMAT("yyyy-MM-dd"),
	ONE_LINK_TRANSACTION_DATE_TIME("ddMMyyyyHHmmss");
	// @formatter:on
	private DateFormatEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
}

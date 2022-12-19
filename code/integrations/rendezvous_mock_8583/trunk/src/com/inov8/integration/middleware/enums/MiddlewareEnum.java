package com.inov8.integration.middleware.enums;

public enum MiddlewareEnum {
	// @formatter:off
	JS_DELIMITER("|"),
	TIME_LOCAL_TRANSACTION_DATE_FORMAT("hhmmss"),
	DATE_LOCAL_TRANSACTION_DATE_FORMAT("MMdd"),
	TRANSACTION_DATE_FORMAT("MMddhhmmss");
	// @formatter:on
	private MiddlewareEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	
}

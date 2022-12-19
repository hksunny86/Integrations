package com.inov8.integration.middleware.enums;

public enum MiddlewareEnum {

	AMBIT_DELIMITER("|"),
	BILL_INQUIRY_DATE_FORMAT("yyMMdd"),
	FULL_SATAEMENT_REQUEST_DATE_FORMAT("yyyyMMdd"),
	STATUS_POSITION_PASSWORD("001101100100000"),
	FULL_SATAEMENT_RESPONSE_DATE_FORMAT("dd/MM/yyyy");

	private MiddlewareEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	
}

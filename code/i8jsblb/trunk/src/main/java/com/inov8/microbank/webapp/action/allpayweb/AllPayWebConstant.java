package com.inov8.microbank.webapp.action.allpayweb;

public enum AllPayWebConstant {
	
	EXTERNAL_XML("PAY_CASH_XML", "EXTERNAL_XML"),
	BLANK_SPACE("BLANK",""),
	SINGLE_SPACE("SINGLE"," "),
	NULL_OBJECT("NULL", null),
	PIN_CHANGE_REQUIRED("PIN_CHANGE_REQUIRED", "pinChangeRequired"),
	INVALID_BANK_PIN("INVALID_BANK_PIN", "INVALID_BANK_PIN"),
	LIMIT_EXCEED("LIMIT_EXCEED", "LIMIT_EXCEED"),
	SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "SERVICE_UNAVAILABLE"),
	GENERIC_PAGE("SERVICE_UNAVAILABLE", "allpay-web/genericnotificationscreen"),
	GENERIC_PAGE_NO_FOOTER("SERVICE_UNAVAILABLE_NO_FOOTER", "allpay-web/genericblanknotificationscreen"),
	CASH_WITHDRAWAL("CASH_WITHDRAWAL", 50006),
	ACCOUNT_TO_CASH("ACCOUNT_TO_CASH", 50010),
	CASH_TO_CASH("CASH_TO_CASH", 50011),
	BULK_PAYMENT("BULK_PAYMENT", 2510801),
	HEADING("HEADING", "HEADING"),
	PRODUCT_NAME("PNAME", "PNAME"),
	PRODUCT_ID("PID", "PID");
	
	private String key;
	private String value;
	private int _value;
	
	private AllPayWebConstant(String key, String value) {
		this.value = value;
		this.key = key;
	}
	
	private AllPayWebConstant(String key, int _value) {
		this._value = _value;
		this.key = key;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public int getIntValue() {
		return this._value;
	}	
	
	public String getKey() {
		return this.key;
	}
}

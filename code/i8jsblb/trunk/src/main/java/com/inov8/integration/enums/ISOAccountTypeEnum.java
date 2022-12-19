package com.inov8.integration.enums;

public enum ISOAccountTypeEnum {
	
	DEFAULT("00"),
	SAVING("10"),
	CHECKING("20"),
	DEBIT_CARD("25"),
	CREDIT_CARD("30"),
	BALANCE_TRANSFER_FACILITY("45"),
	FIXED_DEPOSIT_ACCOUNT("50");

	private ISOAccountTypeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

}

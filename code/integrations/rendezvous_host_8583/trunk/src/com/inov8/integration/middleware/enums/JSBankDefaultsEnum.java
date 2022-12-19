package com.inov8.integration.middleware.enums;

public enum JSBankDefaultsEnum {
	// @formatter:off
	SETTLEMENT_CONVERSION_RATE("00000000"),
	NETWORK_IDENTIFIER("RDV"),
	BILL_INQUIRY_ACQUIRER_IDENTIFICATION("603733"),
	BILL_PAYMENT_ACQUIRER_IDENTIFICATION("603733"),
	ACQUIRER_IDENTIFICATION("000000"),
	TERMINAL_ID("00000000"),
	CARD_ACCEPTOR_NAME("JSBL Branchless Banking Channel Pakistan"),
	PKR_CURRENCY("586"),
	MERCHANT_TYPE("0008");
	// @formatter:on
	private JSBankDefaultsEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
}

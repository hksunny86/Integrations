package com.inov8.jsblconsumer.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum QRCodeEnum {
	PAY_LOAD_FORMAT("00"),
	PAY_LOAD_FORMAT_LENGTH("02"),
	MASTERCARD_PAYLOAD_FORMAT("01"),
	INITIATION_METHOD("01"),
	INITIATION_METHOD_LENGTH("02"),
	MASTERCARD_STATIC("11"),
	MASTERCARD_DYNAMIC("12"),
	PAN("04"),
	MCC("52"),
	CURRENCY_CODE("53"),
	TRANSACTION_AMOUNT("54"),
	CURRENCY_CODE_VALUE("586"),
	COUNTRY_CODE("58"),
	COUNTRY_CODE_VALUE("PK"),
	MERCHANT_NAME("59"),
	MERCHANT_CITY("60"),
	ADDITIONAL_DATA("62"),
	ADDITIONAL_DATA_LENGTH("11"),
	MERCHAN_ID_SUB_CATEGORY("03"),
	BILL_NO("01"),
	REFERENCE_NO("05"),
	PURPOSE("08"),
	CRC("63");

	private static final Map<String,QRCodeEnum> lookup = new HashMap<String,QRCodeEnum>();
	static {
		for(QRCodeEnum qrCodeEnum : EnumSet.allOf(QRCodeEnum.class))
			lookup.put(qrCodeEnum.getValue(), qrCodeEnum);
	}

	private QRCodeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	public static QRCodeEnum lookup(String code) {
		return lookup.get(code);
	}

}

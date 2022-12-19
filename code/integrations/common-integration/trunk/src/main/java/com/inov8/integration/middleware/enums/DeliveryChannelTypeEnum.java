/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.enums;

/**
 * 
 * <p>
 * Constants that will be used when sending request to external system. 
 * </p>
 */
public enum DeliveryChannelTypeEnum {
	// ATM("00"),
	// IVR("01"),
	// INTERNET_BANKING("02"),
	// CALL_CENTER("06"),
	MOBILE_BANKING("08");
	// CORPORATE_BANKING("10"),
	// SMART_DEPOSIT("25");

	private DeliveryChannelTypeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
}

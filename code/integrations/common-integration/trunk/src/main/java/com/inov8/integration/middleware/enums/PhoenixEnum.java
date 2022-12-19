/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.enums;

/**
 * 
 * <code>PhoenixEnum</code> is main Constant holder for values being used in request
 * when sending to external system.
 *
 */
public enum PhoenixEnum {
	DELIVERY_CHANNEL_ID("00000014"),
	CHANNEL_PASSWORD("950973182317F80B"),
	DES_ENCRYPT_KEY("1111111111111111"),
	ASKARI_BANK_IMD("603011"),
	PHOENIX_PKR_CURRENCY("586");

	private PhoenixEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
}

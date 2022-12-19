/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.enums;

/**
 * 
 * An enum which contains possible transaction set that our application will send to 
 * external system.
 *
 */
public enum TransactionCodeEnum {
	OPEN_ACCOUNT_BALANCE_INQUIRY("073"),
	BILL_INQUIRY("010"),
	BILL_INQUIRY_FEE("010"), 
	BILL_PAYMENT("011"),
	ACCOUNT_OPEN_FUND_TRANSFER("076"),
	TITLE_FETCH_OPEN_TRANSFER("018"),
	BILL_PAYMENT_ADVICE("036"),
	TRANSACTION_STATUS_INQUIRY("060"),
	LOGON("801"),
	LOGOFF("802"),
	ECHOTEST("803");

	private TransactionCodeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
}

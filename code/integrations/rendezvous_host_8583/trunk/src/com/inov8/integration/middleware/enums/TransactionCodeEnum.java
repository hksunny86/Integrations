/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.enums;

/**
 * 
 * An enum which contains possible transaction set that our application will send to 
 * external system.
 *
 */
public enum TransactionCodeEnum {
	// @formatter:off
	
	JS_ACCOUNT_BALANCE_INQUIRY("310000"),
	JS_BILL_INQUIRY("720000"),
	JS_BILL_PAYMENT("990000"),
	JS_TITLE_FETCH("930000"),
	JS_CORE_TO_WALLET_ADVICE("490000"),
	JS_FUND_TRANSFER_ADVICE("950000"),
	JS_ACQUIRER_REVERSAL_ADVICE("950000"),
	JS_FUND_TRANSFER("950000"),
	
	JS_BB_TITLE_FETCH("621000"), // HOST TRANSACTION 
	JS_BB_ACCOUNT_FT_ADVICE("910000"), // HOST TRANSACTION
	JS_BB_IBFT_WALLET_CREDIT_ADVICE("970000"), // HOST TRANSACTION
	JS_BB_CASH_WITH_DRAWAL("320000"),
	JS_BB_MINI_STATEMENT("340000"),
	JS_BB_CASH_WITH_DRAWAL_REVERSAL("420000"),
	JS_BB_BALANCE_INQUIRY("360000"),
	JS_BB_POS_TRANSACTION("380000"),
	JS_BB_POS_REVERSAL("520000"),

	JS_BB_TITLE_FETCH_CODE("62"),
	JS_BB_ACCOUNT_FT_ADVICE_CODE("91"),
	JS_BB_IBFT_WALLET_CREDIT_ADVICE_CODE("97"),
	JS_BB_CASH_WITH_DRAWAL_CODE("32"),
	JS_BB_MINI_STATEMENT_CODE("34"),
	JS_BB_CASH_WITH_DRAWAL_REVERSAL_CODE("42"),
	JS_BB_BALANCE_INQUIRY_CODE("36"),
	JS_BB_POS_TRANSACTION_CODE("38"),
	JS_BB_POS_REVERSAL_CODE("52");


	
	// @formatter:on
	private TransactionCodeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
}

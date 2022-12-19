/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.enums;

import com.inov8.integration.middleware.util.VersionInfo;
import com.inov8.integration.middleware.util.VersionInfo.Priority;

/**
 * 
 * An enum which contains possible transaction set that our application will send to 
 * external system.
 *
 */
//@formatter:off
@VersionInfo(
		createdBy = "Zeeshan Ahmad", 
		lastModified = "11-12-2014", 
		priority = Priority.HIGH, 
		tags = { "Meezan","AMBIT", "Transaction Code Enum" }, 
		version = "1.0",
		releaseVersion = "2.4",
		patchVersion = "2.4.1",
		notes = "Transaction Codes updated for phase 2, Version: 2.6")
//@formatter: on
public enum TransactionCodeEnum {
	OPEN_ACCOUNT_BALANCE_INQUIRY("073"),
	PASS_CODE_VERIFICATION("0054"),
	CARD_AND_PIN_VERIFICATION("005"),
	QUICK_CUSTOMER_REGISTRATION("0056"),
	BILL_INQUIRY("010"),
	BILL_INQUIRY_FEE("010"), 
	BILL_PAYMENT("011"),
	ACCOUNT_OPEN_FUND_TRANSFER("076"),
	INTER_BANK_FUND_TRANSFER("017"),
	TITLE_FETCH_OPEN_TRANSFER("018"),
	BILL_PAYMENT_ADVICE("036"),
	TRANSACTION_STATUS_INQUIRY("060"),
	LOGON("801"),
	LOGOFF("802"),
	ECHOTEST("803"),
	CREDIT_CARD_ACCOUNT_INQUIRY("102"),
	CREDIT_CARD_ACCOUNT_PAYMENT("106"),
	MINI_STATEMENT("004"),
	
	
	AMBIT_BALANCE_INQUIRY("0020"),
	AMBIT_BILL_INQUIRY("0013"),
	AMBIT_BILL_PAYMENT("0014"),
	AMBIT_TITLE_FETCH("0030"),
	AMBIT_MINI_STATEMENT("0007"),
	AMBIT_FULL_STATEMENT("0008"),
	AMBIT_UBPS_COMPANY_DETAIL("0073"),
	AMBIT_GET_BENEFICIARY_LIST("0032"),
	AMBIT_FUND_TRANSFER("0009"),
	AMBIT_THIRD_PARTY_FUND_TRANSFER("0031"),
	AMBIT_CUSTOMER_ACCOUNT_INFO("0006"),
	AMBIT_USER_PASSWORD_AUTHENTICATION("0052"),
	AMBIT_CUSTOMER_INFO("0005"),
	AMBIT_UBPS_REGISTERED_CONSUMER_LIST("0071"),
	
	// Phase 2 Transactions
	AMBIT_DEBIT_CARD_STATUS_CHANGE("0085"),
	AMBIT_DEBIT_CARD_LIST("0090"),
	AMBIT_IBFT("0082"),
	AMBIT_IBFT_BENEFICIARY_LIST("0087"),
	AMBIT_IBFT_TITLE_FETCH("0088");
	

	private TransactionCodeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
}

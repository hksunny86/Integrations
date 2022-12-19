package com.inov8.integration.middleware.mock.model;

public class JDBCAccount {
	private String accountTitle;
	private String accountNumber;
	private Double accountBalance;
	private String accountType;
	private String accountCurrency;
	private String accountStatus;
	private long ttlRequest;

	public String getAccountTitle() {
		return accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountCurrency() {
		return accountCurrency;
	}

	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public long getTtlRequest() {
		return ttlRequest;
	}

	public void setTtlRequest(long ttlRequest) {
		this.ttlRequest = ttlRequest;
	}

}

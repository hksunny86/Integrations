package com.inov8.integration.middleware.mock.model;

import java.io.Serializable;
import java.util.Date;

public class JDBCStatement implements Serializable {

	private static final long serialVersionUID = 2339432953413478084L;

	private String accountNo;
	private Date transactionDate;
	private String stan;
	private String description;
	private String drAmount;
	private String crAmount;
	private String balance;

	private String responseCode;
	private int ttl;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDrAmount() {
		return drAmount;
	}

	public void setDrAmount(String drAmount) {
		this.drAmount = drAmount;
	}

	public String getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(String crAmount) {
		this.crAmount = crAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

}

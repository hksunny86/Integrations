package com.inov8.integration.middleware.pdu.response;

import java.io.Serializable;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class CustomerAccountInfo implements Serializable{

	private static final long serialVersionUID = -1308762076026204710L;
	private String accountNo;
	private String accountTitle;
	private String accountType;
	private String branchName;
	private String currency;
	private String balance;
	private String iban;
	
	public String build(){
		StringBuilder pduString = new StringBuilder();

		pduString.append(trimToEmpty(accountNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(accountTitle)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(accountType)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(branchName)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(currency)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(balance)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(iban)).append(AMBIT_DELIMITER.getValue());
		
		return pduString.toString();
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountTitle() {
		return accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	@Override
	public String toString() {
		return "CustomerAccountInfo [accountNo=" + accountNo + ", accountTitle=" + accountTitle + ", accountType=" + accountType + ", branchName=" + branchName
				+ ", currency=" + currency + ", balance=" + balance + ", iban=" + iban + "]";
	}
	
	
}

package com.inov8.integration.middleware.pdu.response;

import java.io.Serializable;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class AccountInfo implements Serializable{
	
	private static final long serialVersionUID = -2653543768634668271L;
	private String branchCode;
	private String accountNo;
	private String iban;
	
	public String build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(trimToEmpty(branchCode)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(accountNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(iban)).append(AMBIT_DELIMITER.getValue());

		return pduString.toString();
	}
	
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}

}
